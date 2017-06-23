import cv2
import numpy as np
import math

L4=cv2.imread('L4.jpg',0)
pts_src=np.matrix([[53.0,53.0],[188.0,20.0],[186.0,147.0],[56.0,185.0]])
pts_dst=np.matrix([[0.0,0.0],[132.0,0.0],[132.0,132.0],[0.0,132.0]])
h,status = cv2.findHomography(pts_src, pts_dst)
rows,cols =L4.shape
im_dst = cv2.warpPerspective(L4, h, (rows,cols))
cv2.imshow('image', im_dst)
#cv2.imshow('i', L3)


cv2.waitKey(0)
cv2.destroyAllWindows()
