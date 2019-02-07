'''
WRITTEN FOR PYTHON 2.7
Class: CPSC 427-01

Team Member #1: Robert Brajcich
Team Member #2: Damon George
Submitted By: Damon George
GU username: dgeorge2

Project 3: Practice with K-Nearest Neighbors
Due: 2/8/2019
To run: python proj3.py
'''
from kNN import *

def classify_person(norm_data_matrix, range_vals, min_vals, labels_vector):
    labels = ['not at all', 'in small doses', 'in large doses']
    k = 3

    #get user input
    percent_video = float(raw_input("percentage of time spent playing video games " +
                                    "over the past year?\n"))
    freq_flier_miles = float(raw_input("Number of frequent flyer miles earned in " +
                                       "the past year?\n"))
    liters_ice_cream = float(raw_input("Number of liters of ice cream eaten in " +
                                       "the past year?\n"))

    #get user input as normalized vector
    in_pt = array([freq_flier_miles, percent_video, liters_ice_cream])
    in_pt_norm = normalize_point(in_pt, min_vals, range_vals)
    
    #classify
    result = classify(in_pt_norm, norm_data_matrix, labels_vector, k)

    #print result
    potential = labels[int(result) - 1]                 
    print ("You will probably like this person: " + potential)

def main():
    data_matrix, labels_vector = file2matrix("datingTestSet2.txt")
    #plot_data(data_matrix)
    norm_data_matrix, range_vals, min_vals = normalize(data_matrix)
    #test_classifier(norm_data_matrix, labels_vector)
    classify_person(norm_data_matrix, range_vals, min_vals, labels_vector)
    
    
main()
