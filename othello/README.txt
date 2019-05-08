'''
Class: CPSC 427 
Team Member 1: Robert Brajcich
Team Member 2: Damon George
Submitted By Damon George
GU Username: dgeorge2
Date: 9 May 2019
'''

Othello final project for Gonzaga University CPSC 427 - Artificial Intelligence

Written in Java

Usage: 
	javac Othello.java
	java Othello

Files:
- AIThread.java 	The AI used by the program.
					This runs as a background thread and attempts to calculate a move in under 10 seconds.

- AITimer.java 		The timer task used to prevent the AI task from exceeding 10 seconds

- BoardCell.java 	A cell component of the Board GUI. 
					This handles drawing tiles and allowing the player to select moves.

- BoardPanel.java 	The Board Section of the Main Window.
					This holds all the BoardCells and updates them when the game state changes.

- ControlPanel.java The Contorl Section of the Main Window. 
					This shows the score, current turn information, 
					and holds any buttons used to advance through states of the game

- MainWindow.java 	The Main GUI of the program. 
					This just holds the BoardPanel and the ControlPanel

- Othello.java 		The main program. This is a singleton. 
					This handles starting the program, creating the windows,
					and also handles the game state machine, which is updated in the Event Dispatch Thread.


- OthelloGame.java	The othello game class holding functionality regarding playing othello, such as making moves,
					and also holding the current state of the game. 

- OthelloNode.java 	A node of the Othello State space.
					This is used by the AI to to alpha beta pruning.
					This class uses a tree structure, and also holds functionality
					for generating children and for calculating heuristics.

- StartDialog.java 	The start dialog for initializing the program


