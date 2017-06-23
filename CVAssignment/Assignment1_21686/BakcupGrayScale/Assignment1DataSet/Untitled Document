import cv2
import numpy as np
#Q1
original = cv2.imread('L1.jpg',0)
copy=cv2.imread('L1.jpg',0)
rows,cols = original.shape
for x in xrange(rows):
    for y in xrange(cols):
	if copy[x,y] > 127:
		copy[x,y]=255
	else:
 	   copy[x,y]=0

cv2.imshow('dst_rt', copy)

#Q2
print("L1 size")
print(rows)
print(cols)
logoOriginal=cv2.imread('logo.jpg',0)
rows1,cols1 = logoOriginal.shape

print("logo size")
print(rows/rows1)
print(cols/cols1)

cv2.waitKey(0)
cv2.destroyAllWindows()
