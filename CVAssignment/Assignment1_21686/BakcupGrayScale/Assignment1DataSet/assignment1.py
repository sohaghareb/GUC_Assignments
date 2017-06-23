import cv2
import numpy as np
import math
#Q1
L1 = cv2.imread('L1.jpg',0)
copy=cv2.imread('L1.jpg',0)
rows,cols = L1.shape
for x in xrange(rows):
    for y in xrange(cols):
	if copy[x,y] > 127:
		copy[x,y]=255
	else:
 	   copy[x,y]=0

#cv2.imshow('dst_rt', copy)
##############################################################
#Q2
#here i will use L1 object again
#i will scale down so i will scale L1 to be like logo
logo=cv2.imread('logo.jpg',0)#load logo image
rows1,cols1 = logo.shape
result_image = np.zeros((rows,cols,3), np.uint8)#create new image with the same size
result_image=cv2.cvtColor(result_image, cv2.COLOR_BGR2GRAY)
xScale=rows/float(rows1)
yScale=cols/float(cols1)
print(rows)
print(cols)
print(rows1)
print(cols1)
scalingMtrix=np.matrix([[xScale,0],[0,yScale]])
ScalingMatrivInv=np.linalg.inv(scalingMtrix)
print("matrix is ")
print(ScalingMatrivInv)
#loop from the destination to the source so loop on logo
#scaling L1
for x in xrange(rows):
    for y in xrange(cols):
	newPoint=np.matrix([[x],[y]])
	oldPoint=ScalingMatrivInv*newPoint
	if (oldPoint.item(0,0)%1 == 0.0) & (oldPoint.item(1,0)%1 == 0.0) :
		oldx=int((oldPoint.item(0,0)))
		oldy=int((oldPoint.item(1,0)))
		result_image[x,y]=logo[oldx,oldy]
	else:
		intensity=0;
		exactCellX=int(math.floor(oldPoint.item(0,0)))
		exactCellY=int(math.floor(oldPoint.item(1,0)))
		ratioX=oldPoint.item(0,0)%1
		ratioY=oldPoint.item(1,0)%1
		result1=0
		result2=0
		#if exactCellX <= rows & exactCellY <= cols:
		result1+=logo[exactCellX,exactCellY]*ratioX
		if(exactCellX-1 > -1):
			result1+=(1-ratioX)*logo[exactCellX-1,exactCellY]
		if(exactCellY-1 > -1):
			result2=ratioX*logo[exactCellX,exactCellY-1]
		if(exactCellY-1 > -1 & exactCellX-1 > -1):
			result2+=(1-ratioX) * logo[exactCellX-1][exactCellY-1]
		intensity=int(math.ceil((result1 * ratioY + result2 * (1-ratioY))))
		result_image[x,y]=int(intensity)
#blending L1 and logo
#cv2.imshow('dst_rt', result_image)
blended_image = np.zeros((rows,cols,3), np.uint8)#create new image with the same size
for x in xrange(rows):
    for y in xrange(cols):
        i=result_image[x,y]* (float(1)/5) + L1[x,y] * (float(4)/5)
        blended_image[x,y]=round(i)

#cv2.imshow('blended image', blended_image)
############################################################################3333
#Q3
L2 = cv2.imread('L2.jpg',0)
LResult = cv2.imread('L2.jpg',0)
L2rows,L2cols = L2.shape
for x in xrange(L2rows):
    for y in xrange(L2cols):
        LResult[x,y]=(L2[x,y]+50)%255

#cv2.imshow('dst_rt', LResult)
##################################################################################33
#Q4
#to get the coordinates of the image
right_clicks = list()
#this function will be called whenever the mouse is right-clicked
def mouse_callback(event, x, y, flags, params):

    #right-click event value is 2
    if event == 2:
        global right_clicks

        #store the coordinates of the right-click event
        right_clicks.append([x, y])

        #this just verifies that the mouse data is being collected
        #you probably want to remove this later
        print right_clicks

#cv2.namedWindow('image')
cv2.setMouseCallback('image', mouse_callback)
L3=cv2.imread('L3.jpg',0)
cv2.imshow('image', L3)
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
rows3,cols3 = L3.shape
print(rows3)
result3_image = np.zeros((rows3,cols3,3), np.uint8)
for x in range(2,570):
    for y in range(5,k):
        newPoint=np.matrix([[x],[y],[1]])
        oldPoint=AffineMatrix*newPoint
        if (oldPoint.item(0,0)%1 == 0.0) & (oldPoint.item(1,0)%1 == 0.0) :
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
            if(exactCellY-1 > -1 & exactCellX-1 > -1):
                result2+=(1-ratioX) * L3[exactCellX-1][exactCellY-1]
            intensity=int(math.ceil((result1 * ratioY + result2 * (1-ratioY))))
            result3_image[x,y]=int(intensity)
    k=k-1
##################################################Affine matrix2





#cv2.imshow('Affine L3', result3_image)



################################################################################333333
#Q5 homograohy
pts_src=np.matrix([[5.0,2.0],[1000.0,140.0],[894.0,694.0],[68.0,467.0]])
pts_dst=np.matrix([[5.0,2.0],[1000.0,2.0],[1000.0,569.0],[5.0,569.0]])
h,status = cv2.findHomography(pts_src, pts_dst)
im_dst = cv2.warpPerspective(L3, h, (1500,1500))
cv2.imshow('image', im_dst)
#cv2.imshow('i', L3)


cv2.waitKey(0)
cv2.destroyAllWindows()
