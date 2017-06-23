import cv2
import numpy as np
import math

# Q1
L1 = cv2.imread('L1.jpg', 0)
rows, cols = L1.shape

logo = cv2.imread('logo.jpg', 0)  # load logo image
rows1, cols1 = logo.shape
result_image = np.ones((rows, cols, 3), np.uint8)  # create new image with the same size
result_image = cv2.cvtColor(result_image, cv2.COLOR_BGR2GRAY)

xScale = rows / float(rows1)
yScale = cols / float(cols1)
scalingMtrix = np.matrix([[xScale, 0], [0, yScale]])
ScalingMatrivInv = np.linalg.inv(scalingMtrix)

# loop from the destination to the source so loop on logo
# scaling L1
for y in range(0, cols,1):
    for x in range(0, rows,1):
        newPoint = np.matrix([[x], [y]])
        oldPoint = ScalingMatrivInv * newPoint
        if (oldPoint.item(0, 0) % 1 == 0.0) is (oldPoint.item(1, 0) % 1 == 0.0):
            oldx = int((oldPoint.item(0, 0)))
            oldy = int((oldPoint.item(1, 0)))
            result_image[x, y] = logo[oldx, oldy]

        elif (oldPoint.item(0, 0) % 1 == 0.0):  # if x=0
            ratioY = oldPoint.item(1, 0) % 1
            exactCellX = int(math.floor(oldPoint.item(0, 0)))
            exactCellY = int(math.floor(oldPoint.item(1, 0)))
            result1 = logo[exactCellX][exactCellY]
            result2 = 0
            if (exactCellY - 1 > 0):
                result2 = logo[exactCellX][exactCellY - 1]
            result_image[x, y] = int(math.ceil((result1 * ratioY + result2 * (1 - ratioY))))
            # print("iam here1")

        elif (oldPoint.item(1, 0) % 1 == 0.0):  # if y=0
            ratioX = oldPoint.item(0, 0) % 1
            exactCellX = int(math.floor(oldPoint.item(0, 0)))
            exactCellY = int(math.floor(oldPoint.item(1, 0)))
            result1 = logo[exactCellX][exactCellY]
            result2 = 0
            if (exactCellX - 1 > 0):
                result2 = logo[exactCellX - 1][exactCellY]
            result_image[x, y] = int(math.ceil((result1 * ratioX + result2 * (1 - ratioX))))
            # print("iam here2")

            # else if((oldPoint.item(1,0)%1 == 0.0)):#if y=0
        else:
            exactCellX = int(math.floor(oldPoint.item(0, 0)))
            exactCellY = int(math.floor(oldPoint.item(1, 0)))
            ratioX = oldPoint.item(0, 0) % 1
            ratioY = oldPoint.item(1, 0) % 1
            result1 = 0
            result2 = 0
            result1 = logo[exactCellX, exactCellY] * ratioX
            if (exactCellX - 1 > -1):
                result1 += (1 - ratioX) * logo[exactCellX - 1, exactCellY]
            if (exactCellY - 1 > -1):
                result2 = ratioX * logo[exactCellX, exactCellY - 1]
            if (exactCellY - 1 > -1 is exactCellX - 1 > -1):
                result2 += (1 - ratioX) * logo[exactCellX - 1][exactCellY - 1]

            intensity = int(math.ceil((result1 * ratioY + result2 * (1 - ratioY))))
            result_image[x, y] = int(intensity)

blended_image = np.zeros((rows,cols,3), np.uint8)#create new image with the same size
for x in xrange(rows):
    for y in xrange(cols):
        i=result_image[x,y]* (float(1)/5) + L1[x,y] * (float(4)/5)
        blended_image[x,y]=round(i)

cv2.imshow('Scaled image logo', result_image)
cv2.imshow('Blended Image ', blended_image)
cv2.waitKey(0)
cv2.destroyAllWindows()
