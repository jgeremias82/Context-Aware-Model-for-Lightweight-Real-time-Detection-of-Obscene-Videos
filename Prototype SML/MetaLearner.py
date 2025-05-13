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
			
