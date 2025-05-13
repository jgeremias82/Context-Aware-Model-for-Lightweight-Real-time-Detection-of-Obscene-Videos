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
import numpy as np
import metalearner as ml
import console as csl
import webcam
import imageSML as isml
import scikitlearner as sl
import cnnSML
	
color = csl.Console()

cnn = cnnSML.CNN()
net = cnn.loadNetwork()

cam = webcam.Camera()
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
        image = isml.Image()
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
                meta = ml.MetaLearner(slidingWindow, sizeWindow)
		newFeatures = meta.featuresExtrationScikit() 		

                color.getCiano()
                print(newFeatures)
                color.restore()

		scikit = sl.ScikitLearn()
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

