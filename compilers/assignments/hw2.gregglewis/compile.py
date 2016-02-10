'''
CPSC 411
Assignment 2
Gregg Thomas Lewis
UCID 00323505
A Recursive Descent Parser for the Minisculus language.

I'm sorry, but I'm gonna use a global variable to hold tokens.
Super sorry. (Hey, it's better than how things worked in my first draft!)

There's also a global var for the last label number used, just because it's a
bit tidier than passing the counter around all day.
'''

import scanner
import sys

def getLabel():
	global labelCount
	labelCount += 1
	return "lb" + str(labelCount)

def consume(food,critical):
	'''
	This helper function basically lets us check for a particular token at
	the head of a list. If we think of our RDP inductively, this is like
	the base case.
	
	Critical is a boolean value indicating whether the presence of the
	expected token is critical. If it is, and we don't see what we expect,
	we raise a SyntaxError.
	
	This notion is adapted from the notes of Brett Giles, 2003 TA for CPSC
	411
	http://pages.cpsc.ucalgary.ca/~gilesb/ta.2003/411/pycalc/calcpy.pdf
	'''
	if (tokens == None or len(tokens) == 0):
		# print "Empty token list! Expected:", food
		if (critical == True):
			print >> sys.stderr, "Error: Encountered end of file prematurely. Expected",food,"token."
			raise SyntaxError
		return False
	if (food == tokens[0].type):
		tokens.pop(0)
		return True
	else:
		# print "found:", tokens
		# print "looking for:", food
		if (critical == True):
			print >> sys.stderr, "Error on line", tokens[0].lineno,"-- expected",food,"found",tokens[0].type,"("+tokens[0].value+")"
			raise SyntaxError
		return False

'''
The following class definitions all represent nodes in an abstract syntax tree
for minisculus. Their constructors consume stuff from the front of the global
token list, and depending on what they find, call constructors for their
children.
'''

class prog:
	def __init__(self):
		if (tokens == None or len(tokens) == 0):
			print >> sys.stderr, "You must provide some code!"
			raise SyntaxError
		self.stmt = stmt()
	def getCode(self):
		print "lb0:"
		self.stmt.getCode()

class stmt:
	def __init__(self):
		if (tokens == None):
			pass #not sure what to do if we get no tokens passed in
			#so for now, we do nothing.

		elif (tokens[0].type == "IF"):
			self.type = "IF"
			consume("IF",True)
			self.expr = expr()
			consume("THEN",True)
			self.true = stmt()
			consume("ELSE",True)
			self.false = stmt()

		elif (tokens[0].type == "WHILE"):
			self.type = "WHILE"
			consume("WHILE",True)
			self.expr = expr()
			consume("DO",True)
			self.stmt = stmt()

		elif (tokens[0].type == "INPUT"):
			self.type = "INPUT"
			consume("INPUT",True)
			self.id = tokens[0].value
			consume("ID",True)

		elif (tokens[0].type == "ID"): #this indicates an ASSIGN node
			self.type = "ASSIGN"
			self.id = tokens[0].value
			consume("ID",True)
			consume("ASSIGN",True)
			self.expr = expr()

		elif (tokens[0].type == "WRITE"):
			self.type = "WRITE"
			consume("WRITE",True)
			self.expr = expr()

		elif (tokens[0].type == "BEGIN"):
			self.type = "STMTLIST"
			consume("BEGIN",True)
			self.stmtlist = stmtlist()
			consume("END",True)
	def getCode(self):
		#IF,WHILE,INPUT,ASSIGN,WRITE,STMTLIST
		if (self.type == "IF"):
			self.expr.getCode()
			falseTarget = getLabel()
			endTarget = getLabel()
			print "\tcJUMP", falseTarget
			self.true.getCode()
			print "\tJUMP", endTarget
			print falseTarget + ":"
			self.false.getCode()
			print endTarget + ":"
		elif (self.type == "WHILE"):
			loopTest = getLabel()
			loopEnd = getLabel()
			print loopTest + ":"
			self.expr.getCode()
			print "\tcJUMP", loopEnd
			self.stmt.getCode()
			print "\tJUMP",loopTest
			print loopEnd + ":"
		elif (self.type == "INPUT"):
			print "\tREAD",self.id
		elif (self.type == "ASSIGN"):
			self.expr.getCode()
			print "\tLOAD",self.id
		elif (self.type == "WRITE"):
			self.expr.getCode()
			print "\tPRINT"
		elif (self.type == "STMTLIST"):
			self.stmtlist.getCode()

class stmtlist:
	def __init__(self):
		if (len(tokens) > 0):
			if (tokens[0].type in
			["IF","WHILE","INPUT","ID","WRITE","BEGIN"]):
				self.stmt = stmt()
				consume("SEMICOLON",True)
				self.stmtlist = stmtlist()
			else:
				self.stmt = None
				self.stmtlist = None
		else:
			self.stmt = None
			self.stmtlist = None
	def getCode(self):
		if (self.stmt != None):
			self.stmt.getCode()
			self.stmtlist.getCode()

class expr:
	def __init__(self):
		self.term = term()
		self.expr_ = expr_()
	def getCode(self):
		self.term.getCode()
		self.expr_.getCode()

class expr_:
	'''
	The production to which this method corresponds is nullable, so we've
	got to check if the token list is empty or if it begins with something
	in the first set of expr_. Suddenly the need for first and follow sets
	becomes clear!
	'''
	def __init__(self):
		if (len(tokens) > 0):
			if (tokens[0].type in ["ADD","SUB"]):
				if (consume("ADD",False)): self.type = "ADD"
				if (consume("SUB",False)): self.type = "SUB"
				self.term = term()
				self.expr_ = expr_()
			else:
				self.type = None
		else:
			self.type = None
				
	def getCode(self):
		if (self.type != None):
			self.term.getCode()
			self.expr_.getCode()
		if (self.type == "ADD"):
			print "\tOP2 +"
		elif (self.type == "SUB"):
			print "\tOP2 -"

class term:
	def __init__(self):
		self.factor = factor()
		self.term_ = term_()

	def getCode(self):
		self.factor.getCode()
		self.term_.getCode()

class term_:
	'''
	The production to which this method corresponds is nullable, so we've
	got to check if the token list is empty or if it begins with something
	in the first set of term_. Suddenly the need for first and follow sets
	becomes clear!
	'''
	def __init__(self):
		if (len(tokens) > 0):
			if (tokens[0].type in ["MUL","DIV"]):
				if (consume("MUL",False)): self.type = "MUL"
				if (consume("DIV",False)): self.type = "DIV"
				self.factor = factor()
				self.term_ = term_()
			else:
				self.type = None
		else:
			self.type = None

	def getCode(self):
		if (self.type != None):
			self.factor.getCode()
			self.term_.getCode()
		if (self.type == "MUL"):
			print "\tOP2 *"
		elif (self.type == "DIV"):
			print "\tOP2 /"

class factor:
	def __init__(self):
		if (tokens[0].type == "LPAR"):
			consume("LPAR",True)
			self.type = "EXPR"
			self.expr = expr()
			consume("RPAR",True)
		elif (tokens[0].type == "ID"):
			self.type = "ID"
			self.value = tokens[0].value
			consume("ID",True)
		elif (tokens[0].type == "NUM"):
			self.type = "NUM"
			self.value = tokens[0].value
			consume("NUM",True)
		elif (tokens[0] == "SUB"):
			self.type = "NUM"
			consume("SUB",True)
			self.value = str(-1 * int(tokens[0].value))
			consume("NUM",True)
	def getCode(self):
		if (self.type == "EXPR"):
			self.expr.getCode()
		elif (self.type == "ID"):
			print "\trPUSH", self.value
		elif (self.type == "NUM"):
			print "\tcPUSH", self.value


'''
Now that all those class definitions are taken care of, we'll use the scanner
from assignment 1 to scan for tokens.
'''
data = ""
for line in sys.stdin.readlines():
	data += line
tokens = []
mLexer = scanner.doScan(data)
labelCount = 0
for tok in mLexer:
	tokens += [tok]

'''
And finally, we'll try and create an AST from our scanned tokens. A SyntaxError
exception gets raised if anything goes wrong, so if that happens we'll catch
the exception and tell the user there was a problem. We'll tell them the line
number of the offending token, and what we expected to find.
'''

try:
	hope = prog()
	if (len(tokens) != 0):
		print "Finished parsing, but there was code left over! Did you forget to enclose something in a begin/end block?"
		raise SyntaxError
	hope.getCode()
	sys.exit(0) #exit normally
	
except SyntaxError:
	print >> sys.stderr, "Could not compile the provided m- source to stack machine code."
	sys.exit(1) #indicate an error