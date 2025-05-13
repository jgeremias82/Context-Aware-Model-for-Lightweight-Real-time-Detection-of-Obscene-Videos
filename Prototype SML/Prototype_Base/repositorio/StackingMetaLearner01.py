'''
Descricao : Implementacao do prototipo - Stacking Meta Learner 
Autor     : Jhonatan Geremias
Data      : 09/07/2020
versao    : 0.1
python    : 2.7
'''

import os
import glob
import cv2
import caffe
import lmdb
import numpy as np
from caffe.proto import caffe_pb2
# Utilizar GPU
caffe.set_mode_gpu()

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
        self.printPredict(out)
        return out
   
    def printPredict(self,out):
        pred_probas = out['prob']
        print pred_probas.argmax()

        if  pred_probas.argmax() == 0:
            print out['prob'][0][0]
        else:
            print out['prob'][0][1]



cnn = CNN()
net = cnn.loadNetwork()

img_path = "normal.jpg"
image = Image()
img = image.getImage(img_path)
out = cnn.getPredict(net,img)

img_path2 = "porno.jpg"
image2 = Image()
img2 = image2.getImage(img_path2)
out = cnn.getPredict(net,img2)
   

