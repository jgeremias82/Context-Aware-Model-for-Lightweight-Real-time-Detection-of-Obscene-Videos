'''
################################################################
###        Pontificia Universidade Catolica do Parana        ###
###                    Escola Politecnica                    ###
###     Programa de Pos-Graduacao em Informatica - PPGIa     ###
################################################################

###
Credits      : Alceu de Souza Britto Junior
               Altair Olivo Santin
               Eduardo Kugler Viegas
               Jhonatan Geremias
###
Description  : Prototype Implementation - Stacking Meta Learner 
Developed by : Jhonatan Geremias
Email        : jhonatan.geremias@pucpr.br
Date         : 14/08/2020
Version      : 0.8 - Final Version (Scikit Learn) 
Python       : 2.7
###
'''

import os
import cv2
import caffe
import numpy as np
import pickle
import pandas as pd
import psutil
from sklearn.naive_bayes import GaussianNB
from caffe.proto import caffe_pb2

# Utilizar GPU
caffe.set_mode_gpu()

##### BEGIN IMAGE #####
class Image:
    def __init__(self, WIDTH = 227, HEIGHT = 227):
        self.width = WIDTH
        self.height = HEIGHT   

    def transform_img(self, img):
        #Equalizacao do Histograma
        img[:, :, 0] = cv2.equalizeHist(img[:, :, 0])
        img[:, :, 1] = cv2.equalizeHist(img[:, :, 1])
        img[:, :, 2] = cv2.equalizeHist(img[:, :, 2])

        #Redimensionamento da image
        img = cv2.resize(img, (self.width, self.height), interpolation = cv2.INTER_CUBIC)
        return img

    def getImage(self, img_path):
        img = cv2.imread(img_path, cv2.IMREAD_COLOR)
        img = self.transform_img(img)
        return img

##### BEGIN CNN #####
class CNN:
    def __init__(self,path_deploy='LightweightCNN_Deploy.prototxt',path_model='LightweightCNN.caffemodel',path_mean='mean_image.binaryproto'):
        self.path_deploy = path_deploy
        self.path_model = path_model
        self.path_mean = path_mean
        self.mean_array = self.readMeanFile()
    
    def readMeanFile(self):
	mean_blob = caffe_pb2.BlobProto()
        with open(self.path_mean) as f:
	    mean_blob.ParseFromString(f.read())
        mean_array = np.asarray(mean_blob.data, dtype=np.float32).reshape((mean_blob.channels, mean_blob.height, mean_blob.width))
        return mean_array

    def loadNetwork(self):
        net = caffe.Net(self.path_deploy,self.path_model,caffe.TEST)
        return net

    def getPredict(self, net, img):
        transformer = caffe.io.Transformer({'data': net.blobs['data'].data.shape})
        transformer.set_mean('data', self.mean_array)
        transformer.set_transpose('data', (2,0,1))
        net.blobs['data'].data[...] = transformer.preprocess('data', img)
        out = net.forward()
        pred = out['prob'].argmax()
        self.printPredict(out)
        return out, pred
   
    def printPredict(self,out):
        pred_probas = out['prob']
        print pred_probas.argmax()

        if  pred_probas.argmax() == 0:
            print out['prob'][0][0]
        else:
            print out['prob'][0][1]

##### BEGIN CAMERA #####
class Camera:
    def __init__(self, FRAME_WIDTH=640, FRAME_HEIGHT=480, FPS=24):
        self.width = FRAME_WIDTH
        self.height = FRAME_HEIGHT
        self.fps = FPS
        
    def getCamera(self):
        cap = cv2.VideoCapture(-1)
        cap.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,640)
        cap.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,480)
        cap.set(cv2.cv.CV_CAP_PROP_FPS, 24)
        return cap

##### BEGIN SCIKIT LEARNER #####
class ScikitLearn:
    def __init__(self, path_model="NaiveBayesScikit.model"):
        self.path_model = path_model

    def loadModel(self):
        loaded_model = pickle.load(open(self.path_model, 'rb'))	
        return loaded_model

    def predict(self,model,value):
        predicts = []
        predicts.append(value)
        pred = model.predict(predicts)
        prob = model.predict_proba(predicts)
        return pred[0], prob

    def printPredict(self,pred,dist):
        if(pred == 0):
            print("predict=" + str(pred) + ", probability=" + str(dist[0][0]))
        else:
            print("predict=" + str(pred) + ", probability=" + str(dist[0][1]))
       
##### BEGIN METALEARNER #####
class MetaLearner:
    def __init__(self,framesWindow, sizeWindows):
        self.framesWindow = framesWindow
        self.sizeWindow = sizeWindows

    def getFrameBase(self):
        intervalPrevious = int(self.sizeWindow / 2)
        if self.framesWindow[intervalPrevious] == '1':
            return 1
        else:
            return 0

    def getProbabilityIntervalPorno(self):
        qtdePorno = 0
        for frame in self.framesWindow:
            if frame == '1':
                qtdePorno += 1
        return float(qtdePorno / (self.sizeWindow))

    def getProbabilityPreviousPorno(self):
        intervalPrevious = int(self.sizeWindow / 2)
        qtdePorno = 0
        for i,frame in enumerate(self.framesWindow):
            if frame == '1':
                qtdePorno += 1
            if i == intervalPrevious:
                break
        return float(qtdePorno / (intervalPrevious+1))

    def getProbabilityNextPorno(self):
        intervalNext = int(self.sizeWindow / 2)
        qtdePorno = 0
        for i,frame in enumerate(self.framesWindow):
            if intervalNext <= i:
                if frame == '1':
                    qtdePorno += 1
        return float(qtdePorno / (intervalNext+1))

    def getTimePreviousPorno(self):
        intervalPrevious = int(self.sizeWindow / 2)
        timePorno = 0
        for i,frame in enumerate(self.framesWindow[::-1]):
            if intervalPrevious < i:
                timePorno += 1
                if frame == '1':
                    return (timePorno - 1)
        timePorno += 1
        return (timePorno - 1)

    def getTimeNextPorno(self):
        intervalNext = int(self.sizeWindow / 2)
        timePorno = 0
        for i,frame in enumerate(self.framesWindow):
            if intervalNext < i:
                timePorno += 1
                if frame == '1':
                    return (timePorno - 1)
        timePorno += 1
        return (timePorno - 1)

    def getPredictWindow(self):
        qtdePorno = 0
        qtdeNoPorno = 0
        for frame in self.framesWindow:
            if frame == '1':
                qtdePorno += 1
            else:
                qtdeNoPorno += 1
        if qtdePorno > qtdeNoPorno:
            return 1
        else:
            return 0

    def featuresExtration(self):
        listFeatures = []
        listFeatures.append(self.getFrameBase())        
        listFeatures.append(self.getProbabilityIntervalPorno())
        listFeatures.append(self.getProbabilityPreviousPorno())
        listFeatures.append(self.getProbabilityNextPorno())
        listFeatures.append(self.getTimePreviousPorno())
        listFeatures.append(self.getTimeNextPorno())
        listFeatures.append(self.getPredictWindow())
        return listFeatures

    def featuresExtrationScikit(self):
        listFeatures = []
        listFeatures.append(self.getFrameBase())
        listFeatures.append(self.getProbabilityIntervalPorno())
        listFeatures.append(self.getProbabilityPreviousPorno())
        listFeatures.append(self.getProbabilityNextPorno())
        listFeatures.append(self.getTimePreviousPorno())
        listFeatures.append(self.getTimeNextPorno())
        return listFeatures
 
class Console:
    def __init__(self, color='\033[0;0m'):
        self.color = color

    def restore(self):
        print('\033[0;0m')

    def getRed(self):
        print('\033[31m')

    def getGreen(self):
        print('\033[32m')

    def getBlue(self):
        print('\033[34m')
    
    def getCiano(self):
        print('\033[36m')
    
    def getMagenta(self):
        print('\033[35m')

    def getYellow(self):
        print('\033[33m')

    def getWhite(self):
        print('\033[37m')
	
color = Console()

cnn = CNN()
net = cnn.loadNetwork()

cam = Camera()
cap = cam.getCamera()
count = 0
sampling = 0
capturedFrame = 0

# Modulo verificador de confianca
confidenceVerifier = 0.7
# Modulo amostragem de frames
frameSampling = 10
# Janela deslizante
slidingWindow = []
# Tamanho janela deslizante
sizeWindow = 7

lockCamera = False
 
while(True):
   
    ret, frame = cap.read()
    if sampling == frameSampling:
        capturedFrame += 1
        name_frame = instr = "frame{0}_{1}.jpg".format(count,capturedFrame)
        cv2.imwrite(name_frame, frame)
        print name_frame
        image = Image()
        img = image.getImage(name_frame)
        out, pred = cnn.getPredict(net,img)
        os.remove(name_frame)
        prob = 0
	if pred == 1:
            prob = out['prob'][0][1]
        else:
            prob = out['prob'][0][0]    

	if prob > confidenceVerifier:
            
            # deslocar janela deslizante
            slidingWindow.insert(0,str(pred))
            if(len(slidingWindow) > sizeWindow ):
                del slidingWindow[-1]
                meta = MetaLearner(slidingWindow, sizeWindow)
		newFeatures = meta.featuresExtrationScikit() 		

                color.getCiano()
                print(newFeatures)
                color.restore()

		scikit = ScikitLearn()
                model = scikit.loadModel()
                pred,dist = scikit.predict(model,newFeatures) 

                if(pred == 0):
                    color.getGreen()
                    scikit.printPredict(pred,dist)
                    color.restore()
                    lockCamera = False
                else:                  
                    color.getRed()
                    scikit.printPredict(pred,dist)
                    color.restore()
                    lockCamera = True

                color.getBlue()
                print(slidingWindow)
                color.restore()
        else:
            color.getYellow()
            print("### Confidence Verifier - Discard "+ str(prob)+" ###")
            color.restore()
        sampling = 0

    if lockCamera:
        frame = cv2.imread('lockCamera.png',1)
        
 
    cv2.imshow('frame',frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break


    count += 1
    sampling += 1
    if count == 100:
        break

cap.release()
cv2.destroyAllWindows()   

