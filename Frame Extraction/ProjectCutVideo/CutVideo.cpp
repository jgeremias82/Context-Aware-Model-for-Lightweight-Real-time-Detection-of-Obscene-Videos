#include <stdlib.h>
#include <stdio.h>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <time.h>

using namespace cv;
using namespace std;

int mainB( int argc, char** argv )
{  
    if (argc < 2)
    {
        printf("!!! Usage: ./program <filename>\n");
        return -1;
    }

    printf("* Filename: %s\n", argv[1]); 

    CvCapture *capture = cvCaptureFromAVI(argv[1]);
    if(!capture) 
    {
        printf("!!! cvCaptureFromAVI failed (file not found?)\n");
        return -1; 
    }

    //int fps = (int) cvGetCaptureProperty(capture, CV_CAP_PROP_FPS);
	int fps = 1000;
    printf("* FPS: %d\n", fps);
    IplImage* frame = NULL;
    int frame_number = 0;
    char key = 0;   
	int salveFrame = 0;
	int numberFrame = 0;
    while (key != 'q') 
    {
        // get frame 
        frame = cvQueryFrame(capture);       
        if (!frame) 
        {
            printf("!!! cvQueryFrame failed: no frame\n");
            break;
        }       

     
		
		if(salveFrame == 10){
			numberFrame++;

			char filename[100];
			strcpy(filename, "frame_");

			char frame_id[30];
			//itoa(frame_number, frame_id, 10);
			itoa(numberFrame, frame_id, 10);
		    strcat(filename, argv[1]);
			strcat(filename, "_");
			strcat(filename, frame_id);
	
			strcat(filename, ".jpg");

        printf("* Saving: %s\n", filename);

			if (!cvSaveImage(filename, frame))
			{
            printf("!!! cvSaveImage failed\n");
            break;
			}
			salveFrame = 0;
		}

        salveFrame++;

        frame_number++;

        // quit when user press 'q'
        key = cvWaitKey(1000 / fps);
    }

    // free resources
    cvReleaseCapture(&capture);

    return 0;
}