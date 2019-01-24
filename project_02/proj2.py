'''
WRITTEN FOR PYTHON 2.7
Class: CPSC 427-01

Team Member #1: Robert Brajcich
Team Member #2: Damon George
Submitted By: Robert Brajcich
GU username: rbrajcich

Project 2: A Python Exercise
Due: 1/25/2019
To run: python proj2.py
'''

def get_file_name(is_first_attempt = True):
	'''
	Prompts the user to enter the name of a file and returns the string
	'''
	if is_first_attempt:
		return raw_input("Enter name of file to parse: ")
	else:
		return raw_input("Invalid Entry. Enter name of file to parse: ")

def get_file_contents(filename):
	'''
	Returns a string with the contents of a file, or None if error
	'''
	try:
		with open(filename, 'r') as f:
			return f.read()
	except:
		return None

def count_words(file_contents):
	'''
	Returns a dict containing the number of valid words starting with
	each letter given the file contents. The format of the dict is
	{'A': 2341, 'B': 23521 ... 'Z': 643}
	'''
	
	# prepare dict with all letters at 0
	word_counts = {}
	for x in range(ord('A'), ord('Z') + 1):
		word_counts[chr(x)] = 0

	# get list of potential words (strings at beginning, end, 
	# and those in the middle with spaces on both sides)
	potential_words = file_contents.split(' ')

	for potential_word in potential_words:
		if is_word(potential_word):
			word_counts[potential_word[0].upper()] += 1

	return word_counts

def is_word(potential_word):

	# test main word cases
	if potential_word == 'I':
		return True
	elif potential_word == 'A' or potential_word == 'a':
		return True
	elif len(potential_word) > 1 and potential_word.isalpha():
		return True

	# test apostrophe case
	apostrophe_split = potential_word.split("'")
	if len(apostrophe_split) == 2 and \
				apostrophe_split[0].isalpha() and \
				apostrophe_split[1].isalpha():
		return True

	# return false if no criteria were met
	return False

def print_word_counts(word_counts):
	'''
	Prints the word counts for each letter nicely and in alphabetical order
	'''
	for letter in sorted(word_counts.keys()):
		print(letter + ": " + str(word_counts[letter]))

def main():
	# attempt to get file contents
	file_contents = get_file_contents(get_file_name())

	# keep trying to get contents if they weren't loaded successfully
	while file_contents == None:
		file_contents = get_file_contents(get_file_name(False))

	# tokenize and count the words starting with each letter
	word_counts = count_words(file_contents)

	# print out the word counts
	print_word_counts(word_counts)


# entry point for the program
if __name__ == "__main__":
	main()