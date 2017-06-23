import cv2
import numpy as np
import math

L3=cv2.imread('L3.jpg',0)

rows3,cols3 = L3.shape

result4_image = np.zeros((rows3,cols3,3), np.uint8)
matrixtmp=np.matrix([[985,129,1],[898,1006,1],[68,467,1]])
OriginalPointMAtrix=np.linalg.inv(matrixtmp)
destinationXs=np.matrix([[0],[600],[600]])
destinationYs=np.matrix([[1000],[1000],[0]])
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
k=0;
rows3,cols3 = L3.shape
print(rows3)
result3_image = np.zeros((rows3,cols3,3), np.uint8)
for x in range(600,0,-1):
    for y in range(0,1000):
        newPoint=np.matrix([[x],[y],[1]])
        oldPoint=AffineMatrix*newPoint
        #print(oldPoint)
        if (oldPoint.item(0,0)%1 == 0.0) is (oldPoint.item(1,0)%1 == 0.0) :
            oldx=int((oldPoint.item(0,0)))
            oldy=int((oldPoint.item(1,0)))
            result4_image[x,y]=L3[oldx,oldy]
        else:
            intensity=0
            exactCellX=int(round(oldPoint.item(0,0)))
            exactCellY=int(round(oldPoint.item(1,0)))
            # if(exactCellY >= 1007):
            #     break
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
            result4_image[x,y]=int(intensity)
    # k=k+1
    # if(k == 0):
    #     break


cv2.imshow('Affine L3 other', result4_image)


cv2.waitKey(0)
cv2.destroyAllWindows()