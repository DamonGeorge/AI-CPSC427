'''
Class: CPSC 427 
PYTHON 3
Team Member 1: Robert Brajcich
Team Member 2: Damon George
Submitted By Damon George
GU Username: dgeorge2
File Name: proj10-linearRegression.py
Vectorized Linear Regression
Usage: python3 proj10-linearRegression.py
Date: 5 April 2019
'''
import numpy
import csv
import matplotlib.pyplot as plt


'''
Reads csv file of vectors into numpy float array
'''
def get_csv_data(filename):
	#read file into list of x,y coordinates, one set of coords per line
	reader = list(csv.reader(open(filename, "rt"), delimiter=','))
	return numpy.array(reader).astype('float')

'''
Creates the vectors necessary for the vectorized linear regression from the points array.
This returns the X, X_T, Y, and Theta matrices.
X is NxM where N is the number of Features, and M is the number of samples. 
The first column is just 1s and the rest of the columns 
are cols 0 through M-1 of the points matrix
X_T is X transposed
Y is Mx1 and is the last column of the points matrix
Theta is Nx1 of zeros
'''
def get_data_vectors(points):
	Y = points[:,-1]
	X_T = numpy.insert(points[:, :-1], 0, 1, axis=1)
	X = numpy.transpose(X_T)
	Theta = numpy.zeros(points.shape[1]) #num of features = #num cols - 1 + 0th feature

	return X, X_T, Y, Theta

'''
The vectorized linear regression. This returns the Theta vector optimized for the given points
using the provided learning rate and the number of iterations
'''
def gradient_descent(points, learning_rate, num_iterations):
	#get necessary vectors
	X, X_T, Y, Theta = get_data_vectors(points)

	#number of samples
	M = points.shape[0]

	#The gradient descent vectorized algorithm:
	#Loop: Theta = Theta - (alpha / M) * (X * (X_T * Theta - Y))
	for i in range(num_iterations):
		Theta = Theta - (learning_rate/M) * numpy.matmul(X, numpy.matmul(X_T, Theta) - Y)

	return Theta

'''
Uses matplotlib to plot 2d points and the optimized line
'''
def plot_results(points,m,b):
	x_training = [point[0] for point in points]
	y_training = [point[1] for point in points]
	y_prediction = [m*x + b for x in x_training]
	plt.plot(x_training,y_prediction,color='r')
	plt.scatter(x_training,y_training,color='g')
	plt.title("Vectorized Linear Regression")
	plt.show()


def main():
	#Get the csv points data
	points = get_csv_data("input.csv")

	#set the initial parameters
	learning_rate = 0.0001 #step size
	num_iter = 1000 #number of iterations of gradient across all points

	#vectorized linear regression!
	Theta = gradient_descent(points, learning_rate, num_iter)
	print("Theta = ", end='')
	print(Theta)
	
	#if 2 features (i.e. y = mx+b), plot the results
	if(Theta.shape[0] == 2):
		plot_results(points,Theta[1],Theta[0])


if __name__ == "__main__":
	main()
