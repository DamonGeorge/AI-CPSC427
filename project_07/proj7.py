'''
Class: CPSC 427 
Team Member 1: Robert Brajcich
Team Member 2: Damon George
Submitted By Robert Brajcich
GU Username: rbrajcich
File Name: proj7.py
Uses best-first search to list states while finding goal state of given eight puzzle initial state
Usage: python proj7.py
Date: 8 March 2019
'''

from copy import deepcopy

#set a depth limit for the best-first search.
#set to 0 for unlimited depth
DEPTH_LIMIT = 5

# goal state we are trying to obtain
GOAL_STATE = [[1,2,3],
              [8,0,4],
              [7,6,5]]

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

# heuristic function for eight puzzles. the returned number is the
# number of pieces out of place within the eight puzzle
def heuristic(cur_state):
    out_of_place = 0

    # look at each item in state, and compare it to goal state
    for i in range(3):
        for j in range(3):
            if GOAL_STATE[i][j] != cur_state[i][j]:
                out_of_place += 1

    return out_of_place

# key function for sorting algorithm for open list (priority queue)
# returns g(list_entry) + h(list_entry)
def f(list_entry):
    return list_entry[1] + heuristic(list_entry[0])

# performs best-first search on the 8-puzzle given as start
# on its journey towards the 8 puzzle arrangement given in goal.
# depth is limited by the global constant DEPTH_LIMIT defined earlier
def best_first(start):
    open = [(start, 0)] # second item in tuple is g(state)
    closed = []
    while(len(open) > 0):
        
        cs = open.pop(0)
        display(cs[0])

        if cs[0] == GOAL_STATE:
            return True

        if cs[1] < DEPTH_LIMIT:
            for child in gen_child_states(cs[0]):

                # attempt to find on open and lower existing g value
                if shrink_g_on_open(open, child, cs[1] + 1):
                    continue # if found on open, move to next child

                # attempt to find on closed and reopen if lower g value exists    
                if reopen_state(closed, open, child, cs[1] + 1):
                    continue # if found on closed, move to next child

                # at this point the child was neither on open or closed
                open.append((child, cs[1] + 1)) # append it to open with its proper g value

        closed.append(cs) # done with this state, add it to closed
        open.sort(key=f) # sort open (priority queue) by calculating f and reordering appropriately

    return False

# taking list of "open" states, target state to adjust, and new g value,
# this function attempts to find the target state in the open list,
# and if the new g value is less than the current g value for the state
# in the open list, will replace the current g value with the new lower one.
# @returns True if target state found on "open", otherwise False
def shrink_g_on_open(open_states, target_state, new_g):
    for i in range(len(open_states)):
        if open_states[i][0] == target_state: # state found on open list!
            if open_states[i][1] > new_g:
                open_states[i] = (target_state, new_g) # replace g value in open queue
            return True
    return False

# taking list of "closed" and "open" states, target state to test, and new g value,
# this function looks for target state on closed list. If found, and the g value 
# of the state in the closed list is greater than the new g value, remove the item from
# the closed list, and put it into the open list with the new g value.
# @returns True if target state found on "closed", otherwise False
def reopen_state(closed_states, open_states, target_state, new_g):
    for i in range(len(closed_states)):
        if closed_states[i][0] == target_state: # state found on closed list!
            if closed_states[i][1] > new_g:
                closed_states.pop(i) # remove state from closed states
                open_states.append((target_state, new_g)) # add to open states with new g
            return True
    return False

def main():
    #nested list representation of 8 puzzle. 0 is the blank.
    start_state= [[2,8,3],
                  [1,6,4],
                  [7,0,5]]

    # perform search and print states along the way
    print(best_first(start_state))

main()