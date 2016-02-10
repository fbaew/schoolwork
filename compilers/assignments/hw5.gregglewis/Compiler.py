from parser import *
import sys

data = ""
for line in sys.stdin.readlines():
	data += line

a = parseData(data)

try:
	result = a.check()
	# result.display()
	result.code()
	# a.display(0)
except (TypeError, LookupError), (error):
	print >> sys.stderr, "\033[1;31mThere was a typing error.\033[1;m"
	print >> sys.stderr, error.args[0]
	sys.exit(1) #indicate an error