import cv2
import numpy as np
import math

L4=cv2.imread('L4.jpg')
rows,cols,ch =L4.shape
print (rows)
print (cols)
pts_src=np.matrix([[53.0,53.0],[188.0,20.0],[186.0,147.0],[56.0,185.0]]) #source points
#pts_dst=np.matrix([[0.0,0.0],[132.0,0.0],[132.0,132.0],[0.0,132.0]])
pts_dst=np.matrix([[0.0,0.0],[cols,0.0],[cols,rows],[0.0,rows]])
h,status = cv2.findHomography(pts_src, pts_dst)

im_dst = cv2.warpPerspective(L4, h, (cols,rows))
cv2.imshow('image', im_dst)
#cv2.imshow('i', L3)


cv2.waitKey(0)
cv2.destroyAllWindows()
