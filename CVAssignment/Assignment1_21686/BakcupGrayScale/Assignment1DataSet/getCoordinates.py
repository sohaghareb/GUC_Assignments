import cv2
import numpy as np
import math
#Q1
right_clicks = list()
L3=cv2.imread('L3.jpg',0)
#this function will be called whenever the mouse is right-clicked
def mouse_callback(event, x, y, flags, params):

    #right-click event value is 2
    if event == 2:
        global right_clicks

        #store the coordinates of the right-click event
        right_clicks.append([x, y])
        L3[y,x]=0
        #this just verifies that the mouse data is being collected
        #you probably want to remove this later
        print right_clicks

cv2.namedWindow('image')
cv2.setMouseCallback('image', mouse_callback)
row,col= L3.shape
#print(row)
#print(col)
cv2.imshow('image', L3)
cv2.waitKey(0)
cv2.destroyAllWindows()