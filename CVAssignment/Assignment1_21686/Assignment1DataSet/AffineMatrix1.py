import cv2
import numpy as np
import math

L3=cv2.imread('L3.jpg')

matrixtmp=np.matrix([[5,2,1],[900,694,1],[68,467,1]])#2nd point was [980,127,1]
OriginalPointMAtrix=np.linalg.inv(matrixtmp)
destinationXs=np.matrix([[5],[975],[5]])
destinationYs=np.matrix([[2],[569],[569]])# 2nd point was 1000,2
ones=np.matrix([[1],[1],[1]])
firstRaw=OriginalPointMAtrix * destinationXs
firstRaw=firstRaw.transpose()
secondRaw=OriginalPointMAtrix  * destinationYs
secondRaw=secondRaw.transpose()
thirdRaw=OriginalPointMAtrix *ones
thirdRaw=thirdRaw.transpose()

AffineMatrix=np.concatenate((firstRaw, secondRaw), axis=0)
AffineMatrix=np.concatenate((AffineMatrix, thirdRaw), axis=0)
AffineMatrix=np.linalg.inv(AffineMatrix)
######################33333
matrixtmp=np.matrix([[2,5,1],[121,975,1],[694,900,1]])
OriginalPointMAtrix=np.linalg.inv(matrixtmp)
destinationYs=np.matrix([[5],[975],[975]])
destinationXs=np.matrix([[2],[2],[569]])
ones=np.matrix([[1],[1],[1]])
firstRaw=OriginalPointMAtrix * destinationXs
firstRaw=firstRaw.transpose()
secondRaw=OriginalPointMAtrix  * destinationYs
secondRaw=secondRaw.transpose()
thirdRaw=OriginalPointMAtrix *ones
thirdRaw=thirdRaw.transpose()

AffineMatrix2=np.concatenate((firstRaw, secondRaw), axis=0)
AffineMatrix2=np.concatenate((AffineMatrix2, thirdRaw), axis=0)
AffineMatrix2=np.linalg.inv(AffineMatrix2)
k=1000;
rows3,cols3,channel = L3.shape
print(rows3)
result3_image = np.zeros((rows3,cols3,3), np.uint8)
# for x in range(2,570):
#     for y in range(5,1000):
#         if(y <= x +500):
#             newPoint=np.matrix([[x],[y],[1]])
#             oldPoint=AffineMatrix*newPoint
#             if (oldPoint.item(0,0)%1 == 0.0) is (oldPoint.item(1,0)%1 == 0.0) :
#                 oldx=int((oldPoint.item(0,0)))
#                 oldy=int((oldPoint.item(1,0)))
#                 result3_image[x,y]=L3[oldx,oldy]
#             else:
#                 intensity=0
#                 exactCellX=int(round(oldPoint.item(0,0)))
#                 exactCellY=int(round(oldPoint.item(1,0)))
#                 ratioX=oldPoint.item(0,0)%1
#                 ratioY=oldPoint.item(1,0)%1
#                 result1=0
#                 result2=0
#                 #if exactCellX <= rows & exactCellY <= cols:
#                 result1+=L3[exactCellX,exactCellY]*ratioX
#                 if(exactCellX-1 > -1):
#                     result1+=(1-ratioX)*L3[exactCellX-1,exactCellY]
#                 if(exactCellY-1 > -1):
#                     result2=ratioX*L3[exactCellX,exactCellY-1]
#                 if(exactCellY-1 > -1 is exactCellX-1 > -1):
#                     result2+=(1-ratioX) * L3[exactCellX-1][exactCellY-1]
#                 intensity=int(math.ceil((result1 * ratioY + result2 * (1-ratioY))))
#                 result3_image[x,y]=int(intensity)

for x in range(2,570):
    for y in range(5,975):
        if(y <= x +100):
            newPoint=np.matrix([[x],[y],[1]])
            oldPoint=AffineMatrix*newPoint
        else:
            newPoint = np.matrix([[x], [y], [1]])
            oldPoint = AffineMatrix2 * newPoint

        if (oldPoint.item(0,0)%1 == 0.0) is (oldPoint.item(1,0)%1 == 0.0) :
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




cv2.imshow('L3 Front view', result3_image)


cv2.waitKey(0)
cv2.destroyAllWindows()