import cv2
import numpy as np
import math

L2 = cv2.imread('L2.jpg')
LResult = cv2.imread('L2.jpg')
L2rows,L2cols,channel = L2.shape
for x in xrange(L2rows):
    for y in xrange(L2cols):
        LResult[x,y,0]=(L2[x,y,0]+50)%256
        LResult[x, y, 1] = (L2[x, y, 1] + 50) % 256
        LResult[x, y, 2] = (L2[x, y, 2] + 50) % 256


cv2.imshow('Image after adjusting brightness',LResult)
cv2.waitKey(0)
cv2.destroyAllWindows()