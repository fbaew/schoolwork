'''
A dirty hack of a test suite... tries to compile all the files in tests/,
compares results against a handmade list indicating whether each file should
compile or not.

The tests in /tests/mine/ indicate the expected results in the comments. (that
is to say, they specify what the stack machine code should do when executed,
allowing for manual testing.)

This script is provided simply as a tool for quick and dirty automatic testing.

More verbose output (that is, the actual generated stack machine code, where
applicable) is sent to the file /testresults
'''

import os

#expected exit codes for 1.m- through 6.m- in mine/
myTestExpected = [0,0,1,0,1,1]
myTestResults = []

#expected exit codes for Mtest08-1.m- through Mtest08-7.m- in parser/
parserTestExpected = [0,0,1,1,0,1,1,0,0,0]
parserTestResults = []

#expected exit codes for test1.m- through test10.m- in scanner/
scannerTestExpected = [1,1,1,1,1,0,0,1,0,1]
scannerTestResults = []
os.system("echo \"\" > testresults")

for i in range(1,7):
	print "Trying to compile %s.m- ..." % i
	test = r"cat tests/mine/" + str(i) + r".m- | python compile.py >> testresults"
	if(os.system(test) == 0):
		myTestResults += [0]
	else:
		myTestResults += [1]

for i in range(1,8):
	print "Trying to compile %s.m- ..." % i
	test = r"cat tests/parser/Mtest08-" + str(i) + r".m- | python compile.py >> testresults"
	if(os.system(test) == 0):
		parserTestResults += [0]
	else:
		parserTestResults += [1]
for i in [3,6,7]:
	print "Trying to compile %s.m- ..." % i
	test = r"cat tests/parser/Mtest08-" + str(i) + r"-fixed.m- | python compile.py >> testresults"
	if(os.system(test) == 0):
		parserTestResults += [0]
	else:
		parserTestResults += [1]

for i in range(1,11):
	print "Trying to compile test%s.m- ..." % i
	test = r"cat tests/scanner/test" + str(i) + r".m- | python compile.py >> testresults"
	if(os.system(test) == 0):
		scannerTestResults += [0]
	else:
		scannerTestResults += [1]

mine = myTestResults == myTestExpected
parser = parserTestResults == parserTestExpected
scanner = scannerTestResults == scannerTestExpected

print
print
print "Finished parsing all test files..."
if (mine and parser and scanner):
	print "All tests passed!"