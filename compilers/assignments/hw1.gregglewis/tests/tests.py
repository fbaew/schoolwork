'''
Author: Gregg Thomas Lewis
Title: Simple Minisculus Scanner
Course: CPSC 411
Term: Winter 2011

A small suite of tests to make sure that our scanner is working like we
think it is.
'''
import sys
sys.path.append("..")
import scanner

output = [
	# This is documented using single-line comments because lots of editors
	# color strings and '''-style comments the same color, making this much
	# harder to read.

	[], #empty so the index in output matches the number of the test file
	
	# Test File 1
	#
	# Test simple single-line comments, as well as basic parsing.
	
	["NUM","ID","SEMICOLON","ID","ID","SEMICOLON","ID","BEGIN","END","ID",
	"BEGIN","NUM","ID","END","NUM","NUM","ADD","NUM","MUL","NUM","ASSIGN",
	"ASSIGN","SEMICOLON","DO","ID","ID","NUM","ID","ID","NUM","ID",
	"LPAR","ID","RPAR","SUB","DIV","MUL","ADD","LPAR","ID","ADD","ID",
	"ADD","ID","MUL","ID","MUL","ID","MUL","ID","DIV","ID","DIV","ID",
	"DIV","ID","SUB","ID","SUB","ID","SUB","ID","RPAR","SEMICOLON",
	"SEMICOLON"],
	
	# Test File 2
	#
	# Make sure we handle unrecognized characters properly. (For now, this
	# means we ignore them.)
	
	["ID","MUL","ID","ADD","LPAR","ID","MUL","MUL","ID","ID","ID","RPAR"],

	# Test File 3
	#
	# Basic test of multi-line comments
	
	["ID","ASSIGN","ID","ADD","ID","MUL","ID","BEGIN","ID","ASSIGN","NUM",
	"SEMICOLON","ID","ASSIGN","NUM","SEMICOLON","ID","ASSIGN","NUM",
	"SEMICOLON","END","WRITE","ID","SEMICOLON",
	"ID","ID","SEMICOLON"],
	
	# Test File 4
	#
	# Basic test of nested multi-line comments.
	
	["ID","ASSIGN","ID","ADD","ID","MUL","ID","WRITE","ID","SEMICOLON"],

	# Test File 5
	#
	# More advanced test of the interaction of single and multi-line
	# comments. Specifically the case where a multi-line comment is
	# ended on a single line comment line. The expectation is that the
	# multi-line block will continue, as the close symbol is commented out.
	
	["ID","ASSIGN","ID","ADD","ID","MUL","ID","BEGIN","ID","ASSIGN",
	"NUM","SEMICOLON","ID","ASSIGN","NUM","SEMICOLON","WRITE","ID",
	"SEMICOLON"],
	
	# Test File 6
	#
	# More simple parse testing
	
	["BEGIN","ID","ASSIGN","NUM","SEMICOLON","ID","ID","SEMICOLON", "END"]
	
	#
]

def doTest(testInput, expectedOutput):
	'''
	Takes as input a string containing the name of the test file to be
	scanned, and a list containing the expected tokens (in order, of
	course.) Returns a boolean value indicating whether the test passed.
	'''
	data = ""
	testCaseFile = open(testInput)
	for line in testCaseFile.readlines():
		data += line
 	mLexer = scanner.doScan(data)
	tokens = []
	for tok in mLexer: #iter(mLexer.token, None):
	    tokens += [repr(tok.type).strip("'")]

	if (tokens != expectedOutput):
		print ""
		print "What we expected:", expectedOutput
		print "   What we found:", tokens
		
	return tokens == expectedOutput

if (int(sys.argv[1]) <= 6):
	print "Running test", int(sys.argv[1]), "...", ("Failed","Passed")[doTest("test/test"+sys.argv[1]+".m-.txt",output[int(sys.argv[1])])]
else:
	print "No such test, sorry mate!"