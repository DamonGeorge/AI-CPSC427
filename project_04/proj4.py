'''
WRITTEN FOR PYTHON 2.7
Class: CPSC 427-01

Team Member #1: Robert Brajcich
Team Member #2: Damon George
Submitted By: Damon George
GU username: dgeorge2

Project 4: K-Nearest Neighbors for Handwriting Recognition
Due: 2/8/2019
To run: python proj4.py
'''
from kNN import *
from os import listdir
import re

'''
file_lst is the list of file names containing training or test data.  path is
the path from the code to where the data is stored.  train_matrix is an N X 1024
matrix of training/test data where each row is the contents of one of the
training files
'''
def make_matrix(path):
    #get list of files and length
    file_lst = listdir(path)
    m = len(file_lst)

    #initialize matrix
    train_matrix = zeros((m,1024))

    #create vector from file for each row of matrix
    for index in range(m):
        train_matrix[index] = img2vector(file_lst[index], path)

    return train_matrix
    
'''
Each training, test file is a 32X32 matrix of 0's and 1's.  The goal is to
transform the matrix into a vector, where 32nd position of the vector begins
with the 0th item in the 1st row the matrix. This function and the following
one unpacks a 2-D array into a 1-D array.

A simpler example:
ex = matrix([[1,2],[3,4],[5,6]])
vect = zeros((1,6))
for i in range(3):
    for j in range(2):
        vect[0,2*i+j] = sample[i,j]
vect now contains: [1,2,3,4,5,6]
'''
def img2vector(file_name,path):
    file_name = path+file_name

    #read entire file, remove whitespace, and convert to array of ints
    with open(file_name, 'r') as fin:
        vect = array(list(re.sub(r'\s', '', fin.read())), dtype=int)

    return vect
    
'''
file_lst is the list of file names containing training or test data.
Every file name begins with a digit, as in 1_160.txt.  This function and
the following one extracts the initial digit and stores it in a list
'''
def make_labels(path):
    files = listdir(path) 
    #create list of labels from each file name
    labels = [class_number(file_name) for file_name in files]
    return labels

'''
extract the class number from the file name and return it to make_labels
'''
def class_number(file_name):
    #class number is simply the first character of the file name
    return file_name[0];  

'''
test all of the files in the test directory
'''
def test_classifier(test_path, train_matrix, train_labels, k):
    #get test files and create the test matrix and labels
    file_lst = listdir(test_path)
    test_matrix = make_matrix(test_path)
    test_labels = make_labels(test_path)

    m = test_matrix.shape[0] # rows
    errors = 0 # to track errors

    for i in range(m):
        # classify the current image
        result = classify(test_matrix[i,:], train_matrix, train_labels, k)
    
        # if the classification does not correspond to the class number of the file,
        # and error has occured
        if (result != test_labels[i]):
            errors += 1.0
            print("Error on test file " + str(file_lst[i]))
            print("  classifier says: " + str(result) + ", real answer: " + str(test_labels[i]))

    print("\nTotal Errors: " + str(errors))
    print("Error Rate: " + str(float(errors)/float(m)))
    print(" ")
         
         
def main():
    k = 3
    train_path = 'trainingDigits/'
    test_path = 'testDigits/'

    #get training data
    print("Parsing Training Data...")
    train_labels = make_labels(train_path)
    train_matrix = make_matrix(train_path)   
    
    #classify the test data
    print("Classifying Test Data...\n")
    test_classifier(test_path,train_matrix,train_labels,k)
        
    
main()
