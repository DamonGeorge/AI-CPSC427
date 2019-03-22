Class: CPSC 427 
Team Member 1: Robert Brajcich
Team Member 2: Damon George
Submitted By Damon George
GU Username: dgeorge2
Project 8: Genetic Algorithm for the Traveling Salesman Problem
Date: 22 March 2019

To configure parameters for GA:
	Edit the config.properties file appropriately

To Compile:
	javac TspTest.java

To Run:
	java TspTest <configFilename> <inputCostDataFilename> <pair/mating_combo_type>



Java Files:
- Chromosome: Defines chromosomes for the GA
- GA: defines the genetic algorithm
- Mate: holds static methods for mating pairs of chromosomes
- Pair: defines a pair of chromosome parents and has static methods for pairing the GA population
- Params: holds function for loading GA parameters from properties file
- TSP: extension of the GA for the traveling salesman problem
- TspTest: Holds the main method of the TSP GA program

Other Files:
- config.properties: holds parameters for the genetic algorithm
- tsp_input_data.csv: holds cost data for the TSP data