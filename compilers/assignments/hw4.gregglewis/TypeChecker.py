from parser import *
import sys

data = ""
for line in sys.stdin.readlines():
	data += line

a = parseData(data)
# a.collect().display()

# a.display(0)

# a.decls[2].collect(a.collect()).display() #make sure that the symbol table stack is working

try:
	result = a.check()
	result.display()
	# a.display(0)
except (TypeError, LookupError) as error:
	print >> sys.stderr, "\033[1;31mThere was a typing error.\033[1;m"
	print >> sys.stderr, error.args[0]
	sys.exit(1) #indicate an error