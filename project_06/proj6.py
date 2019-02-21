'''
Class: CPSC 427 
Team Member 1: Robert Brajcich
Team Member 2: Damon George
Submitted By Damon George
GU Username: dgeorge2
File Name: proj6.py
Uses breadth-first search to list states while finding goal state of given eight puzzle initial state
Usage: python proj6.py
Date: 22 February 2019
'''

from copy import deepcopy
from collections import deque #for implementing fast queues

#displays the given state
def display(state):
    for row in state:
        print row
    print ""

#returns (row,col) of value in state
def find_coord(value, state):
    for row in range(3):
        for col in range(3):
            if state[row][col] == value:
                return (row,col)

#returns list of (row, col) tuples which can be swapped for blank
#these form the legal moves of the given current state
def get_new_moves(cur_state):
    row, col = find_coord(0, cur_state) #get row, col of blank
    
    moves = []

    if col > 0:
        moves.append((row, col - 1))    #go left
    if row > 0:
        moves.append((row - 1, col))    #go up
    if col < 2:
        moves.append((row, col + 1))    #go right
    if row < 2:
        moves.append((row + 1, col))    #go down

    return moves

def gen_child_states(cur_state):
    #get legal moves
    move_lst = get_new_moves(cur_state)
   
    #blank is a tuple, holding coordinates of the blank tile
    blank = find_coord(0, cur_state)

    children = []
    #tile is a tuple, holding coordinates of the tile to be swapped
    #with the blank
    for tile in move_lst:
        #create a new state using deep copy 
        #ensures that matrices are completely independent
        child = deepcopy(cur_state)

        #move tile to position of the blank
        child[blank[0]][blank[1]] = child[tile[0]][tile[1]]

        #set tile position to 0                          
        child[tile[0]][tile[1]] = 0
        
        #append child state to the list of children
        children.append(child)

    return children

# performs breadth-first search on the 8-puzzle given as start
# on its journey towards the 8 puzzle arrangement given in goal.
def breadth_first(start, goal):
    
    # init queues
    open = deque([])
    closed = deque([])
    children = deque([])

    #for tracking number of states we've checked
    cur_state_step_no = 1;

    # add starting state to open queue and begin search
    open.append(start)
    while (len(open) > 0):
        # get next item in open queue
        cur = open.popleft()

        #print step # and state
        print("Step " + str(cur_state_step_no)) 
        cur_state_step_no += 1 
        display(cur)
        
        # if this is the goal state, be done
        if (cur == goal):
            return True
        
        #should be in closed states after analyzing
        closed.append(cur)
        
        # add each child of cur to queue
        for child in gen_child_states(cur): # moving left to right
            children.append(child)

        # dequeue each child of cur from queue, add to open if not already
        # analyzed or waiting to be analyzed
        while(len(children) > 0):
            child = children.popleft()
            if (child not in open and child not in closed):
                open.append(child)
        
    return False


def main():
    #nested list representation of 8 puzzle. 0 is the blank.
    start_state= [[2,8,3],
                  [1,6,4],
                  [7,0,5]]

    # goal state we are trying to obtain
    goal_state = [[1,2,3],
                  [8,0,4],
                  [7,6,5]]

    # perform search and print states along the way
    breadth_first(start_state, goal_state)

main()