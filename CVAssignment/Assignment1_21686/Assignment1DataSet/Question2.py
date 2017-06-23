import cv2
import numpy as np
import math

# Q1
L1 = cv2.imread('L1.jpg')
rows, cols,channel = L1.shape

logo = cv2.imread('logo.jpg')  # load logo image
rows1, cols1,channel1 = logo.shape
result_image = np.ones((rows, cols, 3), np.uint8)  # create new image with the same size
#result_image = cv2.cvtColor(result_image, cv2.COLOR_BGR2GRAY)

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
            result_image[x, y,0] = logo[oldx, oldy,0]
            result_image[x, y, 1] = logo[oldx, oldy, 1]
            result_image[x, y, 2] = logo[oldx, oldy, 2]

        elif (oldPoint.item(0, 0) % 1 == 0.0):  # if x=0
            ratioY = oldPoint.item(1, 0) % 1
            exactCellX = int(math.floor(oldPoint.item(0, 0)))
            exactCellY = int(math.floor(oldPoint.item(1, 0)))
            result1B = logo[exactCellX][exactCellY][0]
            result1G = logo[exactCellX][exactCellY][1]
            result1R = logo[exactCellX][exactCellY][2]
            result2B = 0
            result2G = 0
            result2R = 0

            if (exactCellY - 1 > 0):
                result2B = logo[exactCellX][exactCellY - 1][0]
                result2G=logo[exactCellX][exactCellY - 1][1]
                result2R=logo[exactCellX][exactCellY - 1][2]

            result_image[x, y,0] = int(math.ceil((result1B * ratioY + result2B * (1 - ratioY))))
            result_image[x, y, 1] = int(math.ceil((result1G * ratioY + result2G * (1 - ratioY))))
            result_image[x, y, 2] = int(math.ceil((result1R * ratioY + result2R * (1 - ratioY))))
            # print("iam here1")

        elif (oldPoint.item(1, 0) % 1 == 0.0):  # if y=0
            ratioX = oldPoint.item(0, 0) % 1
            exactCellX = int(math.floor(oldPoint.item(0, 0)))
            exactCellY = int(math.floor(oldPoint.item(1, 0)))
            result1B = logo[exactCellX][exactCellY][0]
            result1G = logo[exactCellX][exactCellY][1]
            result1R = logo[exactCellX][exactCellY][2]
            result2B = 0
            result2G = 0
            result2R = 0
            if (exactCellX - 1 > 0):
               # result2 = logo[exactCellX - 1][exactCellY]
                result2B = logo[exactCellX - 1][exactCellY][0]
                result2G = logo[exactCellX - 1][exactCellY][1]
                result2R = logo[exactCellX - 1][exactCellY][2]
            result_image[x, y,0] = int(math.ceil((result1B * ratioX + result2B * (1 - ratioX))))
            result_image[x, y, 1] = int(math.ceil((result1G * ratioX + result2G * (1 - ratioX))))
            result_image[x, y, 2] = int(math.ceil((result1R * ratioX + result2R * (1 - ratioX))))
            # print("iam here2")

            # else if((oldPoint.item(1,0)%1 == 0.0)):#if y=0
        else:
            exactCellX = int(math.floor(oldPoint.item(0, 0)))
            exactCellY = int(math.floor(oldPoint.item(1, 0)))
            ratioX = oldPoint.item(0, 0) % 1
            ratioY = oldPoint.item(1, 0) % 1

            result2B = 0
            result2G = 0
            result2R = 0

            result1B = logo[exactCellX, exactCellY,0] * ratioX
            result1G = logo[exactCellX, exactCellY,1] * ratioX
            result1R = logo[exactCellX, exactCellY,2] * ratioX
            #result1 = logo[exactCellX, exactCellY] * ratioX
            if (exactCellX - 1 > -1):
                result1B += (1 - ratioX) * logo[exactCellX - 1, exactCellY,0]
                result1G += (1 - ratioX) * logo[exactCellX - 1, exactCellY,1]
                result1R += (1 - ratioX) * logo[exactCellX - 1, exactCellY,2]
            if (exactCellY - 1 > -1):
                result2B = ratioX * logo[exactCellX, exactCellY - 1,0]
                result2G = ratioX * logo[exactCellX, exactCellY - 1,1]
                result2R  = ratioX * logo[exactCellX, exactCellY - 1,2]
            if (exactCellY - 1 > -1 is exactCellX - 1 > -1):
                result2B += (1 - ratioX) * logo[exactCellX - 1][exactCellY - 1,0]
                result2G += (1 - ratioX) * logo[exactCellX - 1][exactCellY - 1,1]
                result2R += (1 - ratioX) * logo[exactCellX - 1][exactCellY - 1,2]

            intensityB = int(math.ceil((result1B * ratioY + result2B * (1 - ratioY))))
            intensityG = int(math.ceil((result1G * ratioY + result2G * (1 - ratioY))))
            intensityR = int(math.ceil((result1R * ratioY + result2R * (1 - ratioY))))
            result_image[x, y,0] = int(intensityB)
            result_image[x, y, 1] = int(intensityG)
            result_image[x, y, 2] = int(intensityR)

blended_image = np.zeros((rows,cols,3), np.uint8)#create new image with the same size
for x in xrange(rows):
    for y in xrange(cols):
        iB=result_image[x,y,0]* (float(1)/5) + L1[x,y,0] * (float(4)/5)
        iG = result_image[x, y,1] * (float(1) / 5) + L1[x, y,1] * (float(4) / 5)
        iR = result_image[x, y,2] * (float(1) / 5) + L1[x, y,2] * (float(4) / 5)
        blended_image[x,y,0]=round(iB)
        blended_image[x, y, 1] = round(iG)
        blended_image[x, y, 2] = round(iR)

cv2.imshow('Scaled image logo', result_image)
cv2.imshow('Blended Image ', blended_image)
cv2.waitKey(0)
cv2.destroyAllWindows()
