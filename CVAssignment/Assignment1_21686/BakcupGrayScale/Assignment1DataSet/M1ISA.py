import cv2
import numpy as np
import math

L3=cv2.imread('L3.jpg',0)

matrixtmp=np.matrix([[2,5,1],[694,900,1],[467,68,1]])#2nd point was [980,127,1]
OriginalPointMAtrix=np.linalg.inv(matrixtmp)
destinationYs=np.matrix([[0],[1000],[0]])
destinationXs=np.matrix([[0],[690],[690]])# 2nd point was 1000,2
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

rows3,cols3 = L3.shape
result3_image = np.zeros((rows3,cols3,3), np.uint8)
##################################################################################
# matrixtmp=np.matrix([[5,2,1],[970,130,1],[900,694,1]])#2nd point was [980,127,1]
# OriginalPointMAtrix=np.linalg.inv(matrixtmp)
# destinationXs=np.matrix([[0],[1000],[1000]])
# destinationYs=np.matrix([[0],[0],[690]])# 2nd point was 1000,2
# ones=np.matrix([[1],[1],[1]])
# firstRaw=OriginalPointMAtrix * destinationXs
# firstRaw=firstRaw.transpose()
# secondRaw=OriginalPointMAtrix  * destinationYs
# secondRaw=secondRaw.transpose()
# thirdRaw=OriginalPointMAtrix *ones
# thirdRaw=thirdRaw.transpose()
#
# AffineMatrix2=np.concatenate((firstRaw, secondRaw), axis=0)
# AffineMatrix2=np.concatenate((AffineMatrix2, thirdRaw), axis=0)
# AffineMatrix2=np.linalg.inv(AffineMatrix2)4
matrixtmp=np.matrix([[10,5,1],[121,970,1],[694,900,1]])#2nd point was [980,127,1]
OriginalPointMAtrix=np.linalg.inv(matrixtmp)
destinationYs=np.matrix([[0],[1000],[1000]])
destinationXs=np.matrix([[0],[0],[690]])# 2nd point was 1000,2
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
print (AffineMatrix2)

##################################3
for x in range(0,690):
    limit=(500*x)/345
    for y in range(0,1000):
        if(y <= limit ):
            newPoint=np.matrix([[x],[y],[1]])
            oldPoint=AffineMatrix*newPoint
        else:
            newPoint = np.matrix([[x], [y], [1]])
            oldPoint = AffineMatrix2 * newPoint

        if (oldPoint.item(0,0)%1 == 0.0) is (oldPoint.item(1,0)%1 == 0.0) :
            oldx=int((oldPoint.item(0,0)))
            oldy=int((oldPoint.item(1,0)))
            result3_image[x,y]=L3[oldx,oldy]
        else:
            intensity=0
            exactCellX=int(round(oldPoint.item(0,0)))
            exactCellY=int(round(oldPoint.item(1,0)))
            ratioX=oldPoint.item(0,0)%1
            ratioY=oldPoint.item(1,0)%1
            result1=0
            result2=0
            #if exactCellX <= rows & exactCellY <= cols:
            result1+=L3[exactCellX,exactCellY]*ratioX
            if(exactCellX-1 > -1):
                result1+=(1-ratioX)*L3[exactCellX-1,exactCellY]
            if(exactCellY-1 > -1):
                result2=ratioX*L3[exactCellX,exactCellY-1]
            if(exactCellY-1 > -1 is exactCellX-1 > -1):
                result2+=(1-ratioX) * L3[exactCellX-1][exactCellY-1]
            intensity=int(math.ceil((result1 * ratioY + result2 * (1-ratioY))))
            result3_image[x,y]=int(intensity)


cv2.imshow('Affine L3 other', result3_image)


cv2.waitKey(0)
cv2.destroyAllWindows()