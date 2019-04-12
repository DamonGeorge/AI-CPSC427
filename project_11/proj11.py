'''
WRITTEN FOR PYTHON 2
Class: CPSC 427 
Team Member 1: Robert Brajcich
Team Member 2: Damon George
Submitted By Robert Brajcich
GU Username: rbrajcich
File Name: proj11.py
Performs k-means on given test set and plots the results
Usage: python proj11.py
Date: 13 April 2019
'''

import numpy as np
import matplotlib.pyplot as plt

'''
Data comes in like this:
0.xxx ...  0.yyyy
where the values are floating point numbers representing points in a space
my example is m x 2, but the code would work for n features
Read these into an m x 2 numpy matrix, where m is the number of points
'''
def loadData(file_name):
    with open(file_name) as fin:
        rows = (line.strip().split('\t') for line in fin)
        dataMat = [map(float,row) for row in rows]
    return np.mat(dataMat)

'''
Construct a k x n matrix of randomly generated points as the
initial centroids. The points have to be in the range of the points
in the data set
'''
def randCent(dataMat,k):
    numCol = np.shape(dataMat)[1]  #notice the number of cols is not fixed.
    centroids = np.mat(np.zeros((k,numCol))) #kxnumCol matrix of zeros
    for col in range(numCol):
        minCol = np.min(dataMat[:,col]) #minimum from each column
        maxCol = np.max(dataMat[:,col]) #maximum from each column
        rangeCol = float(maxCol - minCol)
        centroids[:,col] = minCol + rangeCol * np.random.rand(k,1)
    return centroids

'''
Compute the Euclidean distance between two points
Each point is vector, composed of n values, idicating a point in n space 
'''
def distEucl(vecA,vecB):
    return np.sqrt(np.sum(np.power(vecA - vecB,2)))

def kMeans(dataMat, k, distMeas=distEucl, createCent=randCent):
    m = np.shape(dataMat)[0]  #how many items in data set
    
    #create an mX1 natrix filled with -1's (uninititialized value)
    #each row stores centroid information for a point
    #col 0 stores the centroid index to which the point belongs
    clusterAssment = np.mat(np.ones((m,1)))
    clusterAssment = np.negative(clusterAssment)

    #create k randomly placed centroids
    centroids = createCent(dataMat,k)  

    #perform the actual kMeans calculations
    iterations = 0
    pointShifted = True
    while pointShifted:
        iterations = iterations + 1
        
        # calculate nearest centroid for each point
        distances = np.apply_along_axis(lambda vec1: np.apply_along_axis(distMeas, 1, dataMat, vec1), 1, centroids)
        newClusters = np.argmin(distances, 0)
        pointShifted = not np.array_equal(newClusters, clusterAssment)
        clusterAssment = newClusters

        # calculate mean for each centroid by summing x and y points
        xySums = np.mat(np.zeros((k, 2)))
        totalNums = np.mat(np.zeros((k, 1)))
        for i in range(m):
            xySums[clusterAssment[i]] += dataMat[i]
            totalNums[clusterAssment[i]] += 1

        # move centroids to the mean point for each
        centroids = xySums / totalNums

    return centroids, iterations #is the number of iterations required

def plot_results(dataMat, centroids):
    '''
    On the same scatter plot, plot the points and the centroids
    The centroid points should be a different shape and color than the data
    points
    '''

    dataXCoords = [coord[0, 0] for coord in dataMat]
    dataYCoords = [coord[0, 1] for coord in dataMat]
    centXCoords = [coord[0, 0] for coord in centroids]
    centYCoords = [coord[0, 1] for coord in centroids]

    plt.plot(dataXCoords, dataYCoords, 'go')
    plt.plot(centXCoords, centYCoords, 'r^')
    plt.show()

def main():
    k = 4
    dataMat = loadData("testSet.txt")

    centroids, iterations = kMeans(dataMat, k, distEucl, randCent)

    print("iterations: " + str(iterations))
    plot_results(dataMat, centroids)


# main entry point for program
main()