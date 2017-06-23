import cv2
import numpy as np
import math
#Q1
L1 = cv2.imread('L1.jpg',0)
BinaryImage=cv2.imread('L1.jpg',0)
rows,cols = L1.shape
for x in xrange(rows):
    for y in xrange(cols):
        if BinaryImage[x,y] > 127:
            BinaryImage[x,y]=255
        else:
            BinaryImage[x,y]=0

cv2.imshow('Binary image L1', BinaryImage)
cv2.waitKey(0)
cv2.destroyAllWindows()