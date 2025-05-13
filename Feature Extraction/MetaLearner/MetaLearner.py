import numpy as np

class MetaLearner:
    def __init__(self,framesWindow, sizeWindows):
        self.framesWindow = framesWindow
        self.sizeWindow = sizeWindows

    def getFrameBase(self):
        intervalPrevious = int(self.sizeWindow / 2) 
        return self.framesWindow[intervalPrevious]
  
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
                    return timePorno
        timePorno += 1
        return timePorno

    def getTimeNextPorno(self):
        intervalNext = int(self.sizeWindow / 2)
        timePorno = 0
        for i,frame in enumerate(self.framesWindow):             
            if intervalNext < i:
                timePorno += 1
                if frame == '1':
                    return timePorno
        timePorno += 1 
        return timePorno
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
			
JanelaDeslizante = ['0','1','0','1','0','1','0','1','1','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','1','0','0','0','0','0','0','0','0','0','0','0','0','0','0','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','0','0','0','0','0','0','0']

teste1 = ['1','1','0','1','1','1','0','1','1','0','1','0','0','1','0','1','0','0','0','0','1']
teste2 = ['1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1']
teste3 = ['0','0','0','0','0','0','0','0','0','0','1','0','0','0','0','0','0','0','0','0','0']
#          0   1   2   3   4   5   6   7   8   9  10   11  12  13  14  15  16  17  18  19  20
teste4 = ['0','0','0','0','0','0','0','0','0','1','1','1','1','1','1','1','1','1','1','1','1'] 
#qtdePorno = 0
meta = MetaLearner(teste4,21)
base = meta.getPredictWindow()
print(base)
#prob = meta.getProbabilityIntervalPorno()
#prob = meta.getProbabilityPreviousPorno()
#prob = meta.getProbabilityNextPorno()
#time = meta.getTimeNextPorno()
#print(time)
#print(prob)
#a = [1,2,3,4,5]
#print(a)

time = meta.getTimePreviousPorno()
#print(a[::-1])

'''

public int getTimeNextPorno(String strArq, int qtdeFrames){
		
		int timePorno = 0;

		for(int i = (qtdeFrames+1); i < strArq.length(); i++){  
			timePorno++;
			if(strArq.charAt(i) == '1'){ 

            	return timePorno;   
            }
		}
		
		return timePorno;
	}

'''

#print(JanelaDeslizante)
