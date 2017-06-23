import cv2
import numpy as np
import math

matrixtmp=np.matrix([[5,2,1],[980,127,1],[68,467,1]])
OriginalPointMAtrix=np.linalg.inv(matrixtmp)
destinationXs=np.matrix([[5],[1000],[5]])
destinationYs=np.matrix([[2],[2],[569]])
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
k=1000;
L3=cv2.imread('L3.jpg',0)
rows3,cols3 = L3.shape
print(rows3)
result3_image = np.zeros((rows3,cols3,3), np.uint8)
for x in range(2,570):
    for y in range(0,1000):
        if(x < y):
            newPoint=np.matrix([[x],[y],[1]])
            oldPoint=AffineMatrix*newPoint
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
        else:
            break


cv2.imshow('image', result3_image)
#cv2.imshow('i', L3)


cv2.waitKey(0)
cv2.destroyAllWindows()