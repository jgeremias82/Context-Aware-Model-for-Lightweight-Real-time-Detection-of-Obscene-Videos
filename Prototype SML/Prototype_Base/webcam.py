import numpy as np
import cv2

cap = cv2.VideoCapture(-1)
cap.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,640)
cap.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,480)
cap.set(cv2.cv.CV_CAP_PROP_FPS, 24)
count = 0
sampling = 0
amostra = 0

while(True):
    # Capture frame-by-frame
    ret, frame = cap.read()

    # Our operations on the frame come here
    #gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    #gray = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    if sampling == 10:
        amostra += 1
        name_frame = instr = "frame{0}_{1}.jpg".format(count,amostra)
        cv2.imwrite(name_frame, frame)
        #cv2.imwrite("frame%d%d.jpg" % count, frame)
        sampling = 0
    # Display the resulting frame
    cv2.imshow('frame',frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
    count += 1
    
    sampling += 1
    
    if count == 1000:
	break
# When everything done, release the capture
cap.release()
cv2.destroyAllWindows()
