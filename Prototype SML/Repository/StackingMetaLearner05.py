'''
Descricao : Implementacao do prototipo - Stacking Meta Learner 
Autor     : Jhonatan Geremias
Data      : 10/07/2020
versao    : 0.2
python    : 2.7
'''

import os
import glob
import cv2
import caffe
import lmdb
import numpy as np
import weka.core.jvm as jvm
import weka.core.serialization as serialization
from weka.core.converters import Loader
from weka.classifiers import Classifier
from weka.core.dataset import Attribute, Instance, Instances
from caffe.proto import caffe_pb2

# Utilizar GPU
caffe.set_mode_gpu()
jvm.start()

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

##### BEGIN WEKA #####
class Weka:
    def __init__(self, path_model="NaiveBayes.model"):
        self.path_model = path_model
        #jvm.start()

    def createDataset(self):
        # create attributes
        predict_base = Attribute.create_numeric("predict_base_transfer_caffenet")
        probability_interval = Attribute.create_numeric("probability_interval_transfer_caffenet")
        probability_previous = Attribute.create_numeric("probability_previous_transfer_caffenet")
        probability_next = Attribute.create_numeric("probability_next_transfer_caffenet")
        time_previous = Attribute.create_numeric("time_previous_transfer_caffenet")
        time_next = Attribute.create_numeric("time_next_transfer_caffenet")
        Class =  Attribute.create_nominal("Class", ['0','1'])
        # create dataset
        dataset = Instances.create_instances("transfer_caffenet_test", [predict_base,probability_interval,probability_previous,\
                                                                        probability_next,time_previous,time_next,Class], 0)
        dataset.class_is_last()
        return dataset

    def loadModelClassifier(self):
        objects = serialization.read_all("NaiveBayes.model")
        classifier = Classifier(jobject=objects[0])
        loader = Loader(classname="weka.core.converters.ArffLoader")
        return classifier

    def predict(self,data, values):
        inst = Instance.create_instance(values)
        data.add_instance(inst)
        for inst in data:
            pred = classifier.classify_instance(inst)
            dist = classifier.distribution_for_instance(inst)
            self.printPredict(pred,dist)
            return pred, dist
    
    def printPredict(self,pred,dist):
        print("predict=" + str(pred) + ", probability=" + str(dist))
        
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

cnn = CNN()
net = cnn.loadNetwork()

#img_path = "normal.jpg"
#image = Image()
#img = image.getImage(img_path)
#out, pred = cnn.getPredict(net,img)
#print(pred)
#print(out['prob'][0][0])

#print("ssssssssssssssss")

#img_path2 = "porno.jpg"
#image2 = Image()
#img2 = image2.getImage(img_path2)
#out,pred = cnn.getPredict(net,img2)

#print(pred)
#print(out['prob'][0][1])

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
sizeWindow = 21

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
                newFeatures = meta.featuresExtration()
                print(newFeatures)     
                
                weka = Weka()
                classifier = weka.loadModelClassifier()
                data = weka.createDataset()
                pred,dist = weka.predict(data,newFeatures)
           #print(slidingWindow)
            
        else:
            print("### Confidence Verifier - Discard "+ str(prob)+" ###")

        sampling = 0
    ### Weka - classificacao janela deslizante ###
    #jvm.start()
    
    #meta = MetaLearner(teste4,21)

#    weka = Weka()
#    classifier = weka.loadModelClassifier()

#    data1 = weka.createDataset()
#    value1 = [0, 0.0, 0.0, 0.0, 10, 10, 0]
    #value1 = [1,0.33333,0.63636,0.09091,1,10,0]
#    pred,dist = weka.predict(data1,value1)

#    data2 = weka.createDataset()
#    value2 = [1,0.33333,0.54545,0.18182,1,1,0]
#    pred,dist = weka.predict(data2,value2)

    #jvm.stop()
    ### 
#    cv2.imshow('frame',frame)
#    if cv2.waitKey(1) & 0xFF == ord('q'):
#        break
    count += 1

    sampling += 1

    if count == 1000:
        break

jvm.stop()
cap.release()
#cv2.destroyAllWindows()   

