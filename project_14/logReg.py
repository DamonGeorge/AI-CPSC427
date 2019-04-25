'''
WRITTEN FOR PYTHON 3
Class: CPSC 427 
Team Member 1: Robert Brajcich
Team Member 2: Damon George
Submitted By Damon George
GU Username: dgeorge2
File Name: logReg.py
Logistic Regression
Usage: python logReg.py
Date: 29 April 2019
'''

import numpy as np
import matplotlib.pyplot as plt

'''
Read a float csv into a numpy array.
This returns the array of feature columns X and the result column Y, 
both as numpy ndarrays
'''
def load_data(file_name):
    #get nested list of floats
    with open(file_name) as fin:
        rows = (line.strip().split(',') for line in fin)
        dataMat = [list(map(float,row)) for row in rows]

    #convert nested list of floats to numpy array
    data = np.array(dataMat)

    X = data[:,:-1] #get all columns except the last one
    Y = data[:,-1] #the last column

    return X, Y

'''
Get the data points from X that have a corresponding value in Y
that equals result. 
'''
def get_data_by_result(X, Y, result):
    indices = np.where(Y == result) #get the indices in Y where the value equals result
    return np.take(X, indices, axis = 0)[0] #get the data from X using the list of indices

'''
Create the x and y points for the line using the theta vector.
The x values return are a numpy array from the min value in 
the first column X to the max value in the first column of X.
The y values are calculated using the theta vector.
'''
def create_line(X, theta):
    #get max and min values of first column of X
    min = np.amin(X[:, 0])
    max = np.amax(X[:, 0])

    x_vals = np.arange(min, max) #create x values as range from min to max
    y_vals = -1/theta[2] * (theta[1] * x_vals + theta[0]) #calculate y values using theta

    return x_vals, y_vals

'''
Plot the X data and the line corresponding to the theta vector.
'''
def plot_results(X, Y, theta):
    #get the data points for the scatter plot
    #by separating the data with a Y of 1 and a Y of 0
    acceptedPoints = get_data_by_result(X, Y, 1)
    rejectedPoints = get_data_by_result(X, Y, 0)

    #scatter the data, using different markers and colors to differentiate the two sets
    plt.scatter(acceptedPoints[:, 0], acceptedPoints[:, 1], marker="+", color="k", label="Accept")
    plt.scatter(rejectedPoints[:, 0], rejectedPoints[:, 1], marker=".", color="g", label="Reject")

    #create the points for the line and plot
    x_line, y_line = create_line(X, theta)
    plt.plot(x_line, y_line)

    #make the plot and show
    plt.legend(loc="upper right")
    plt.xlabel("Exam 1 Score")
    plt.ylabel("Exam 2 Score")
    plt.title("Admitted/Not Admitted")
    plt.show()

'''
Sigmoid of x where x is a numpy array: 1/(1+e^-x)
'''
def sigmoid(x):
    return 1 / (1 + np.exp(-x))

'''
The vectorized logistic regression.
This return the theta vector optimized for the given x and y data
using the given learning rate and the number of iterations.
'''
def logistic_regression(x, y, learning_rate, num_iterations):
    #insert 1's into the first column of X to handle the theta bias
    X = np.insert(x, 0, 1, axis=1)

    #get X transposed
    X_T = np.transpose(X)

    #create theta as vector of zeros 
    Theta = np.zeros(X.shape[1]) #num of features + 1
   
    #get number of data points
    M = X.shape[0]

    #loop for the number of iterations
    #The gradient descent calculation is:  
    #Theta = Theta - alpha/m * (X_T * (g(X*Theta) - Y))
    for i in range(num_iterations):
        G = sigmoid(np.matmul(X, Theta))
        Theta = Theta - (learning_rate/M) * np.matmul(X_T, G - y)

    return Theta


def main():
    #get data from file
    X, Y = load_data("data1.txt")

    #set the initial parameters for the gradient descent
    learning_rate = 0.001 #step size
    num_iter = 100000 #number of iterations of gradient across all points

    # calculate the theta vector
    theta = logistic_regression(X, Y, learning_rate, num_iter)

    #print the calculated theta values
    print("Calculated Theta Vector:")
    print(theta)

    #plot the data and the resulting line separating accepted and rejected points
    plot_results(X, Y, theta)


# main entry point for program
main()