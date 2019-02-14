'''
Class: CPSC 427 
Team Member 1: Robert Brajcich
Team Member 2: Damon George
Submitted By Robert Brajcich
GU Username: rbrajcich
File Name: proj5.py
Uses depth-first search to list states while finding goal state of given eight puzzle initial state
Usage: python proj5.py
Date: 15 February 2019
'''

from copy import deepcopy

#set a depth limit for the depth-first search.
#set to 0 for unlimited depth
DEPTH_LIMIT = 5

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

def depth_first(start, goal):
    
    # init stacks/queues
    open = []
    closed = []
    children = []

    # stack to hold number of children
    # left to examine on each depth level going up
    depths = [1]
    cur_depth = 0

    cur_state_step_no = 1;

    # add starting state and begin search
    open.append(start)
    while (len(open) > 0):

        while depths[-1] == 0:
            cur_depth -= 1
            depths.pop()

        # get next item in "open" stack
        cur = open.pop()
        print("Step " + str(cur_state_step_no) + " - Depth " + str(cur_depth))
        cur_state_step_no += 1 
        display(cur)
        
        # if this is the goal state, be done
        if (cur == goal):
            return True
        
        #should be in closed states after analyzing
        closed.append(cur)
        depths[-1] -= 1     # mark this state as done in depths stack
        
        if DEPTH_LIMIT == 0 or cur_depth < DEPTH_LIMIT:         
            # add each child of cur to stack
            for child in gen_child_states(cur): # moving left to right
                children.append(child)

            depths.append(0)
            cur_depth += 1

            # popping each child of cur from stack, add to open if not already
            # analyzed or waiting to be analyzed
            while(len(children) > 0):
                child = children.pop()
                if (child not in open and child not in closed):
                    open.append(child)
                    depths[-1] += 1
        
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
    depth_first(start_state, goal_state)

main()