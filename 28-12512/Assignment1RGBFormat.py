import cv2
import numpy as np
import math

def Q1():
    L1 = cv2.imread('L1.jpg', 0)
    BinaryImage = cv2.imread('L1.jpg', 0)
    rows, cols = L1.shape
    for x in xrange(rows):
        for y in xrange(cols):
            if BinaryImage[x, y] > 127:
                BinaryImage[x, y] = 255
            else:
                BinaryImage[x, y] = 0

    cv2.imshow('Binary image L1', BinaryImage)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
#Q1() #call function1
def Q2():
    L1 = cv2.imread('L1.jpg')
    rows, cols, channel = L1.shape

    logo = cv2.imread('logo.jpg')  # load logo image
    rows1, cols1, channel1 = logo.shape
    result_image = np.ones((rows, cols, 3), np.uint8)  # create new image with the same size
    # result_image = cv2.cvtColor(result_image, cv2.COLOR_BGR2GRAY)

    xScale = rows / float(rows1)
    yScale = cols / float(cols1)
    scalingMtrix = np.matrix([[xScale, 0], [0, yScale]])
    ScalingMatrivInv = np.linalg.inv(scalingMtrix)

    # loop from the destination to the source so loop on logo
    # scaling L1
    for y in range(0, cols, 1):
        for x in range(0, rows, 1):
            newPoint = np.matrix([[x], [y]])
            oldPoint = ScalingMatrivInv * newPoint
            if (oldPoint.item(0, 0) % 1 == 0.0) is (oldPoint.item(1, 0) % 1 == 0.0):
                oldx = int((oldPoint.item(0, 0)))
                oldy = int((oldPoint.item(1, 0)))
                result_image[x, y, 0] = logo[oldx, oldy, 0]
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
                    result2G = logo[exactCellX][exactCellY - 1][1]
                    result2R = logo[exactCellX][exactCellY - 1][2]

                result_image[x, y, 0] = int(math.ceil((result1B * ratioY + result2B * (1 - ratioY))))
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
                result_image[x, y, 0] = int(math.ceil((result1B * ratioX + result2B * (1 - ratioX))))
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

                result1B = logo[exactCellX, exactCellY, 0] * ratioX
                result1G = logo[exactCellX, exactCellY, 1] * ratioX
                result1R = logo[exactCellX, exactCellY, 2] * ratioX
                # result1 = logo[exactCellX, exactCellY] * ratioX
                if (exactCellX - 1 > -1):
                    result1B += (1 - ratioX) * logo[exactCellX - 1, exactCellY, 0]
                    result1G += (1 - ratioX) * logo[exactCellX - 1, exactCellY, 1]
                    result1R += (1 - ratioX) * logo[exactCellX - 1, exactCellY, 2]
                if (exactCellY - 1 > -1):
                    result2B = ratioX * logo[exactCellX, exactCellY - 1, 0]
                    result2G = ratioX * logo[exactCellX, exactCellY - 1, 1]
                    result2R = ratioX * logo[exactCellX, exactCellY - 1, 2]
                if (exactCellY - 1 > -1 is exactCellX - 1 > -1):
                    result2B += (1 - ratioX) * logo[exactCellX - 1][exactCellY - 1, 0]
                    result2G += (1 - ratioX) * logo[exactCellX - 1][exactCellY - 1, 1]
                    result2R += (1 - ratioX) * logo[exactCellX - 1][exactCellY - 1, 2]

                intensityB = int(math.ceil((result1B * ratioY + result2B * (1 - ratioY))))
                intensityG = int(math.ceil((result1G * ratioY + result2G * (1 - ratioY))))
                intensityR = int(math.ceil((result1R * ratioY + result2R * (1 - ratioY))))
                result_image[x, y, 0] = int(intensityB)
                result_image[x, y, 1] = int(intensityG)
                result_image[x, y, 2] = int(intensityR)

    blended_image = np.zeros((rows, cols, 3), np.uint8)  # create new image with the same size
    for x in xrange(rows):
        for y in xrange(cols):
            iB = result_image[x, y, 0] * (float(1) / 5) + L1[x, y, 0] * (float(4) / 5)
            iG = result_image[x, y, 1] * (float(1) / 5) + L1[x, y, 1] * (float(4) / 5)
            iR = result_image[x, y, 2] * (float(1) / 5) + L1[x, y, 2] * (float(4) / 5)
            blended_image[x, y, 0] = round(iB)
            blended_image[x, y, 1] = round(iG)
            blended_image[x, y, 2] = round(iR)

    cv2.imshow('Scaled image logo', result_image)
    cv2.imshow('Blended Image ', blended_image)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
#Q2() #function2
def Q3():
    L2 = cv2.imread('L2.jpg')
    LResult = cv2.imread('L2.jpg')
    L2rows, L2cols, channel = L2.shape
    for x in xrange(L2rows):
        for y in xrange(L2cols):
            LResult[x, y, 0] = (L2[x, y, 0] + 50) % 256
            LResult[x, y, 1] = (L2[x, y, 1] + 50) % 256
            LResult[x, y, 2] = (L2[x, y, 2] + 50) % 256

    cv2.imshow('Image after adjusting brightness', LResult)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

#Q3() #call function 3

def Q4():
    L3 = cv2.imread('L3.jpg')

    matrixtmp = np.matrix([[2, 5, 1], [694, 900, 1], [467, 68, 1]])  # 2nd point was [980,127,1]
    OriginalPointMAtrix = np.linalg.inv(matrixtmp)
    destinationYs = np.matrix([[0], [1000], [0]])
    destinationXs = np.matrix([[0], [690], [690]])  # 2nd point was 1000,2
    ones = np.matrix([[1], [1], [1]])
    firstRaw = OriginalPointMAtrix * destinationXs
    firstRaw = firstRaw.transpose()
    secondRaw = OriginalPointMAtrix * destinationYs
    secondRaw = secondRaw.transpose()
    thirdRaw = OriginalPointMAtrix * ones
    thirdRaw = thirdRaw.transpose()

    AffineMatrix = np.concatenate((firstRaw, secondRaw), axis=0)
    AffineMatrix = np.concatenate((AffineMatrix, thirdRaw), axis=0)
    AffineMatrix = np.linalg.inv(AffineMatrix)

    rows3, cols3, ch = L3.shape
    result3_image = np.zeros((rows3, cols3, 3), np.uint8)
    matrixtmp = np.matrix([[10, 5, 1], [121, 970, 1], [694, 900, 1]])  # 2nd point was [980,127,1]
    OriginalPointMAtrix = np.linalg.inv(matrixtmp)
    destinationYs = np.matrix([[0], [1000], [1000]])
    destinationXs = np.matrix([[0], [0], [690]])  # 2nd point was 1000,2
    ones = np.matrix([[1], [1], [1]])
    firstRaw = OriginalPointMAtrix * destinationXs
    firstRaw = firstRaw.transpose()
    secondRaw = OriginalPointMAtrix * destinationYs
    secondRaw = secondRaw.transpose()
    thirdRaw = OriginalPointMAtrix * ones
    thirdRaw = thirdRaw.transpose()

    AffineMatrix2 = np.concatenate((firstRaw, secondRaw), axis=0)
    AffineMatrix2 = np.concatenate((AffineMatrix2, thirdRaw), axis=0)
    AffineMatrix2 = np.linalg.inv(AffineMatrix2)
    print (AffineMatrix2)

    ##################################3
    for x in range(0, 690):
        limit = (500 * x) / 345
        for y in range(0, 1000):
            if (y <= limit):
                newPoint = np.matrix([[x], [y], [1]])
                oldPoint = AffineMatrix * newPoint
            else:
                newPoint = np.matrix([[x], [y], [1]])
                oldPoint = AffineMatrix2 * newPoint

            if (oldPoint.item(0, 0) % 1 == 0.0) is (oldPoint.item(1, 0) % 1 == 0.0):
                oldx = int((oldPoint.item(0, 0)))
                oldy = int((oldPoint.item(1, 0)))
                result3_image[x, y, 0] = L3[oldx, oldy, 0]
                result3_image[x, y, 1] = L3[oldx, oldy, 1]
                result3_image[x, y, 2] = L3[oldx, oldy, 2]
            else:
                intensity = 0
                exactCellX = int(round(oldPoint.item(0, 0)))
                exactCellY = int(round(oldPoint.item(1, 0)))
                ratioX = oldPoint.item(0, 0) % 1
                ratioY = oldPoint.item(1, 0) % 1
                result1B = 0
                result1G = 0
                result1R = 0

                result2B = 0
                result2G = 0
                result2R = 0

                # if exactCellX <= rows & exactCellY <= cols:
                result1B += L3[exactCellX, exactCellY, 0] * ratioX
                result1G += L3[exactCellX, exactCellY, 1] * ratioX
                result1R += L3[exactCellX, exactCellY, 2] * ratioX
                if (exactCellX - 1 > -1):
                    result1B += (1 - ratioX) * L3[exactCellX - 1, exactCellY, 0]
                    result1G += (1 - ratioX) * L3[exactCellX - 1, exactCellY, 1]
                    result1R += (1 - ratioX) * L3[exactCellX - 1, exactCellY, 2]
                if (exactCellY - 1 > -1):
                    result2B = ratioX * L3[exactCellX, exactCellY - 1, 0]
                    result2G = ratioX * L3[exactCellX, exactCellY - 1, 1]
                    result2R = ratioX * L3[exactCellX, exactCellY - 1, 2]
                if (exactCellY - 1 > -1 is exactCellX - 1 > -1):
                    result2B += (1 - ratioX) * L3[exactCellX - 1][exactCellY - 1, 0]
                    result2G += (1 - ratioX) * L3[exactCellX - 1][exactCellY - 1, 1]
                    result2R += (1 - ratioX) * L3[exactCellX - 1][exactCellY - 1, 2]
                intensityB = int(math.ceil((result1B * ratioY + result2B * (1 - ratioY))))
                intensityG = int(math.ceil((result1G * ratioY + result2G * (1 - ratioY))))
                intensityR = int(math.ceil((result1R * ratioY + result2R * (1 - ratioY))))
                result3_image[x, y, 0] = int(intensityB)
                result3_image[x, y, 1] = int(intensityG)
                result3_image[x, y, 2] = int(intensityR)

    cv2.imshow('Affine L3 other', result3_image)

    cv2.waitKey(0)
    cv2.destroyAllWindows()

#Q4() #calling function 4

def Q5():
    L4 = cv2.imread('L4.jpg')
    rows, cols, ch = L4.shape
    print (rows)
    print (cols)
    pts_src = np.matrix([[53.0, 53.0], [188.0, 20.0], [186.0, 147.0], [56.0, 185.0]])  # source points
    # pts_dst=np.matrix([[0.0,0.0],[132.0,0.0],[132.0,132.0],[0.0,132.0]])
    pts_dst = np.matrix([[0.0, 0.0], [cols, 0.0], [cols, rows], [0.0, rows]])
    h, status = cv2.findHomography(pts_src, pts_dst)

    im_dst = cv2.warpPerspective(L4, h, (cols, rows))
    cv2.imshow('image', im_dst)
    # cv2.imshow('i', L3)


    cv2.waitKey(0)
    cv2.destroyAllWindows()

#Q5() calling function5


