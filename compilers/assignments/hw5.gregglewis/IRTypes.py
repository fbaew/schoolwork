from tree import *

generalcount = 0
def nextLabel():
	global generalcount;
	generalcount += 1
	return "lb" + str(generalcount)

#here is the sole type belonging to I_prog

class I_PROG(object):
	def __init__(self,functions,lvars,stmts):
		self.functions = functions
		self.lvars = lvars
		self.stmts = stmts
	def display(self):
		print "I_PROG"
		print "    Functions:"
		for func in self.functions:
			func.display(3)
		print "    Local Variables:", self.lvars
		print "    [Body]"
		for stmt in self.stmts:
			stmt.display(3)
		if len(self.stmts) == 0:
			print ("        EMPTY")
	def code(self):
		print "ALLOC 4" #No args, allocate a void cell each for return value, static link, dynamic link, and return addr
		#we can do the above because we don't care about returning to another function... this is the main function!
		print "LOAD_R %sp"
		print "STORE_R %fp" #set the frame pointer
		print "ALLOC", self.lvars
		print
		print "JUMP progstart"
		for fun in self.functions:
			fun.code()

		print "progstart:"
		for stmt in self.stmts:
			stmt.code()
		
		print "ALLOC", -(self.lvars + 4) #clean up!
		print "HALT"
		

#here is the sole type belonging to I_fbody

class I_FUN(object):
	def __init__(self,name,functions,lvars,numargs,stmts):
		self.name = name
		self.functions = functions
		self.lvars = lvars
		self.numargs = numargs
		self.stmts = stmts
	def display(self, depth):
		print ("    " * (depth-1)) + "----" + self.name + ": (" + str(self.numargs) + " arguments)"
		print ("    " * depth) + "----" + str(self.lvars) + " local variables"
		print ("    " * depth) + "----[Functions]"
		for func in self.functions:
			func.display(depth+1)
		if len(self.functions) == 0:
			print ("    " * (depth+1) + "----NONE")
		print ("    " * depth) + "----[Body]"
		for stmt in self.stmts:
			stmt.display(depth+1)
		if len(self.stmts) == 0:
			print ("    " * (depth+1)) + "EMPTY"
	def code(self):
		print
		print self.name + ":"
		print "%finishing the call"
		print "LOAD_R %sp"
		print "STORE_R %fp"
		print "ALLOC", self.lvars, "%allocating space for local vars"
		print "LOAD_I", self.lvars+2 #deallocation counter (+2 takes care of counter and return addr?)
		print "%function body:"
		print "JUMP", self.name+"start"
		for fun in self.functions:
			fun.code()
		print self.name + "start:"
		for stmt in self.stmts:
			stmt.code()

		print "%returning code"
		print "LOAD_R %fp"
		print "STORE_O", str(-(self.numargs+3)) #stick the return value in the first argument slot
		print "LOAD_R %fp"
		print "LOAD_O 0"
		print "LOAD_R %fp"
		print "STORE_O", str(-(self.numargs+2)) #stick return pointer in second arg slot
		print "LOAD_R %fp"
		print "LOAD_O", str(self.lvars+1)
		print "APP NEG"
		print "ALLOC_S" #deallocate local variables
		print "STORE_R %fp" #puts the dynamic link back into %fp
		print "ALLOC", str(-self.numargs)
		print "JUMP_S"

# Here are the types belonging to I_stmt:

class I_ASS(object):
	def __init__(self,level,offset,exprs):
		self.level = level
		self.offset = offset
		self.exprs = exprs #this is not a list, don't be fooled!
		#	Thanks, past Gregg! ---^
	def display(self, depth):
		print ("    " * depth) + "----I_ASS"
		print ("    " * (depth+1)) + "----Level: " + str(self.level)
		print ("    " * (depth+1)) + "----Offset: " + str(self.offset)
		print ("    " * (depth+1)) + "----Assignment:"
		self.exprs.display(depth+1)
	def code(self):
		self.exprs.code()
		#now we have to find the memory location of our variable!
		print "LOAD_R %fp"
		for i in range(self.level):
			print "LOAD_O -2" #follow the static pointer up as many levels as we need to get to the var declaration.
		print "STORE_O", self.offset #This should put the value of the expression into memory at the right spot! I hope!

class I_WHILE(object):
	def __init__(self,expr,stmt):
		self.expr = expr
		self.stmt = stmt
	def display(self, depth):
		print ("    " * (depth)) + "----I_WHILE "
		self.expr.display(depth+1)
		self.stmt.display(depth+1)
	def code(self):
		#get some labels
		testLabel = nextLabel()
		loopEndLabel = nextLabel()
		
		print testLabel + ":"
		self.expr.code()
		print "JUMP_C", loopEndLabel
		self.stmt.code()
		print "JUMP", testLabel
		print loopEndLabel + ":"
		


class I_COND(object):
	def __init__(self,expr, truestmt,falsestmt):
		self.expr = expr
		self.truestmt = truestmt
		self.falsestmt = falsestmt
	def display(self, depth):
		print ("    " * (depth)) + "----I_COND"
		self.expr.display(depth+1)
		print ("    " * (depth+1)) + "True stmt:"
		self.truestmt.display(depth+2)
		print ("    " * (depth+1)) + "False stmt:"
		self.falsestmt.display(depth+2)
	def code(self):
		trueLabel = nextLabel()
		falseLabel = nextLabel()
		endLabel = nextLabel()
		
		self.expr.code()
		print "JUMP_C", falseLabel
		print trueLabel + ":"
		self.truestmt.code()
		print "JUMP", endLabel
		print falseLabel + ":"
		self.falsestmt.code()
		print endLabel + ":"
	
class I_READ_B(object):
	def __init__(self,level,offset):
		self.level = level
		self.offset = offset
	def display(self, depth):
		print (("    " * (depth)) + "----I_READ_B")
		print ("    " * (depth+1)) + "Level: " +  str(self.level)
		print ("    " * (depth+1)) + "Offset: " +  str(self.offset)
	def code(self):
		print "READ_B"
		#now we have to find the memory location of our variable!
		print "LOAD_R %fp"
		for i in range(self.level):
			print "LOAD_O -2" #follow the static pointer up as many levels as we need to get to the var declaration.
		print "STORE_O", self.offset #This should put the value of the expression into memory at the right spot! I hope!
		
class I_PRINT_I(object):
	def __init__(self,expr):
		self.expr = expr
	def display(self, depth):
		print ("    " * depth) + "----I_PRINT_I"
		self.expr.display(depth+1)
	def code(self):
		self.expr.code()
		print "PRINT_I"
		
class I_PRINT_B(object):
	def __init__(self,expr):
		self.expr = expr
	def display(self, depth):
		print ("    " * depth) + "----I_PRINT_B"
		self.expr.display(depth+1)
	def code(self):
		self.expr.code()
		print "PRINT_B"
				
class I_READ_I(object):
	def __init__(self,level,offset):
		self.level = level
		self.offset = offset
	def display(self, depth):
		print (("    " * (depth)) + "----I_READ_I")
		print ("    " * (depth+1)) + "Level: " +  str(self.level)
		print ("    " * (depth+1)) + "Offset: " +  str(self.offset)
	def code(self):
		print "READ_I"
		#now we have to find the memory location of our variable!
		print "LOAD_R %fp"
		for i in range(self.level):
			print "LOAD_O -2" #follow the static pointer up as many levels as we need to get to the var declaration.
		print "STORE_O", self.offset #This should put the value of the expression into memory at the right spot! I hope!		
class I_RETURN(object):
	def __init__(self,expr):
		self.expr = expr
	def display(self, depth):
		print ("    " * (depth)) + "----I_RETURN"
		self.expr.display(depth+1)
	def code(self):
		self.expr.code()

class I_BLOCK(object):
	def __init__(self,functions,lvars,stmts):
		self.functions = functions
		self.lvars = lvars
		self.stmts = stmts
	def display(self, depth):
		print ("    " * (depth)) + "----I_BLOCK"
		print ("    " * (depth+1)) + "----" + str(self.lvars) + " local variables"
		print ("    " * (depth+1)) + "----[Functions]"
		for func in self.functions:
			func.display(depth+2)
		if len(self.functions) == 0:
			print ("    " * (depth+2) + "----NONE")
		print ("    " * (depth+1)) + "----[Body]"
		for stmt in self.stmts:
			stmt.display(depth+2)
		if len(self.stmts) == 0:
			print ("    " * (depth+2)) + "EMPTY"
	def code(self):
		print
		print "LOAD_R %fp" #save the old frame pointer
		print "ALLOC 2" #allocate two void stack cells
		print "LOAD_R %sp"
		print "STORE_R %fp" #set the frame pointer to the last void cell
		print "ALLOC", self.lvars
		print "LOAD_I", self.lvars+3
		
		print "%block code"
		blockLabel = nextLabel()
		print "JUMP", blockLabel
		
		for fun in self.functions:
			fun.code()
		
		print blockLabel +":"
		for stmt in self.stmts:
			stmt.code()


		print "%block cleanup"
		print "LOAD_R %fp"
		print "LOAD_O",self.lvars+1
		print "APP NEG"
		print "ALLOC_S" #deallocate static local storage
		print "STORE_R %fp" #restore the old frame pointer
		print "%end of block"
		
# Here are the types belonging to I_expr

class I_IVAL(object):
	def __init__(self,value):
		self.value = value

	def display(self,depth):
		print ("    " * depth) + "----I_IVAL = " + str(self.value)
	def code(self):
		print "LOAD_I",self.value

class I_BVAL(object):
	def __init__(self,value):
		self.value = value
	def display(self,depth):
		print ("    " * depth) + "----I_BVAL = " + str(self.value)
	def code(self):
		print "LOAD_B",str(self.value).lower()

class I_ID(object):
	def __init__(self,level,offset):
		self.level = level
		self.offset = offset
	def display(self,depth):
		print ("    "  * depth) + "----I_ID"
		print ("    " * (depth+1)) + "Level: " + str(self.level)
		print ("    " * (depth+1)) + "Offset: " + str(self.offset)
	def code(self):
		print "LOAD_R %fp"
		for i in range(self.level):
			print "LOAD_O -2" #follow the static pointer up as many levels as we need to get to the var declaration.
		print "LOAD_O", self.offset #This should get the value from the stack...

class I_APP(object):
	def __init__(self,operand,exprs):
		self.oper = operand
		self.exprs = exprs
	def display(self,depth):
		print ("    " * depth) + "----I_APP"
		self.oper.display(depth+1)
		print ("    " * (depth+1)) + "[Operands]"
		for expr in self.exprs:
			expr.display(depth+2)
	def code(self):
		operands = self.exprs
		if type(self.oper) == I_CALL: #gotta load the arguments in reverse order if it's a function call
			operands.reverse()

		for expr in operands:	#compute all the operands
			expr.code()
		if type(self.oper) == I_ADD_I:
			print "APP ADD"
		elif type(self.oper) == I_SUB_I:
			print "APP SUB"
		elif type(self.oper) == I_MUL_I:
			print "APP MUL"
		elif type(self.oper) == I_DIV_I:
			print "APP DIV"
		elif type(self.oper) == I_NEG_I:
			print "APP NEG"
		elif type(self.oper) == I_LT_I:
			print "APP LT"
		elif type(self.oper) == I_LE_I:
			print "APP LE"
		elif type(self.oper) == I_GT_I:
			print "APP GT"
		elif type(self.oper) == I_GE_I:
			print "APP GE"
		elif type(self.oper) == I_EQ_I:
			print "APP EQ"
		elif type(self.oper) == I_NOT:
			print "APP NOT"
		elif type(self.oper) == I_AND:
			print "APP AND"
		elif type(self.oper) == I_OR:
			print "APP OR"
		elif type(self.oper) == I_CALL:
			print
			print "%calling function",self.oper.name
			print "ALLOC 1"
			self.oper.code()






			# print "We're doing integer addition!"


# Here are the types belonging to I_opn

class I_CALL(object):
	def __init__(self,name,level):
		self.name = name
		self.level = level
	def display(self,depth):
		print ("    " * depth) + "----I_CALL"
		print ("    " * (depth+1)) + "Name: " + self.name
		print ("    " * (depth+1)) + "Level: " + str(self.level)
	def code(self):
		print "LOAD_R %fp"
		for i in range(self.level):
			print "LOAD_O -2" #follow the static pointer up as many levels as we need to get to the function declaration.
		print "LOAD_R %fp"
		print "LOAD_R %cp" #put the return address on the stack
		print "JUMP", self.name
class primativeOp(object):
	def display(self,depth):
		types = {
			I_ADD_I:"I_ADD_I"
			, I_MUL_I:"I_MUL_I"
			, I_SUB_I:"I_SUB_I"
			, I_DIV_I:"I_DIV_I"
			, I_NEG_I:"I_NEG_I"
			, I_LT_I:"I_LT_I"
			, I_LE_I:"I_LE_I"
			, I_GT_I:"I_GT_I"
			, I_GE_I:"I_GE_I"
			, I_EQ_I:"I_EQ_I"
			, I_NOT:"I_NOT"
			, I_AND:"I_AND"
			, I_OR:"I_OR"
		}
		print ("    " * depth) + "----" + types[type(self)]

class I_ADD_I(primativeOp):
	pass

class I_MUL_I(primativeOp):
	pass

class I_SUB_I(primativeOp):
	pass

class I_DIV_I(primativeOp):
	pass

class I_NEG_I(primativeOp):
	pass

class I_LT_I(primativeOp):
	pass

class I_LE_I(primativeOp):
	pass

class I_GT_I(primativeOp):
	pass

class I_GE_I(primativeOp):
	pass

class I_EQ_I(primativeOp):
	pass

class I_NOT(primativeOp):
	pass

class I_AND(primativeOp):
	pass

class I_OR(primativeOp):
	pass