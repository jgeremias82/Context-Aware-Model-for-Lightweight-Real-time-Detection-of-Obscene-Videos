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
Date         : 15/09/2020
Version      : 0.9 - Final Version (CNN)
Python       : 2.7
###
'''
from __future__ import print_function

import caffe
import numpy as np
from caffe.proto import caffe_pb2

# Utilizar GPU
caffe.set_mode_gpu()

##### BEGIN CNN #####
class CNN:
    def __init__(self,path_deploy='Caffenet_Deploy.prototxt',path_model='Caffenet_Full.caffemodel',path_mean='mean_image.binaryproto'):
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
        print(pred_probas.argmax())

        if  pred_probas.argmax() == 0:
            print(out['prob'][0][0])
        else:
            print(out['prob'][0][1])
