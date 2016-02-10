from tree import *

#here is the sole type belonging to I_prog

class I_PROG:
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
		

#here is the sole type belonging to I_fbody

class I_FUN:
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

# Here are the types belonging to I_stmt:

class I_ASS :
	def __init__(self,level,offset,exprs):
		self.level = level
		self.offset = offset
		self.exprs = exprs #this is not a list, don't be fooled!
	def display(self, depth):
		print ("    " * depth) + "----I_ASS"
		print ("    " * (depth+1)) + "----Level: " + str(self.level)
		print ("    " * (depth+1)) + "----Offset: " + str(self.offset)
		print ("    " * (depth+1)) + "----Assignment:"
		self.exprs.display(depth+1)


class I_WHILE:
	def __init__(self,expr,stmt):
		self.expr = expr
		self.stmt = stmt
	def display(self, depth):
		print ("    " * (depth)) + "----I_WHILE "
		self.expr.display(depth+1)
		self.stmt.display(depth+1)
		


class I_COND:
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
		
		
class I_READ_B:
	def __init__(self,level,offset):
		self.level = level
		self.offset = offset
	def display(self, depth):
		print (("    " * (depth)) + "----I_READ_B")
		print ("    " * (depth+1)) + "Level: " +  str(self.level)
		print ("    " * (depth+1)) + "Offset: " +  str(self.offset)
		
class I_PRINT_I:
	def __init__(self,expr):
		self.expr = expr
	def display(self, depth):
		print ("    " * depth) + "----I_PRINT_I"
		self.expr.display(depth+1)
		
class I_PRINT_B:
	def __init__(self,expr):
		self.expr = expr
	def display(self, depth):
		print ("    " * depth) + "----I_PRINT_B"
		self.expr.display(depth+1)
				
class I_READ_I:
	def __init__(self,level,offset):
		self.level = level
		self.offset = offset
	def display(self, depth):
		print (("    " * (depth)) + "----I_READ_I")
		print ("    " * (depth+1)) + "Level: " +  str(self.level)
		print ("    " * (depth+1)) + "Offset: " +  str(self.offset)
class I_RETURN:
	def __init__(self,expr):
		self.expr = expr
	def display(self, depth):
		print ("    " * (depth)) + "----I_RETURN"
		self.expr.display(depth+1)
class I_BLOCK:
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
		
# Here are the types belonging to I_expr

class I_IVAL:
	def __init__(self,value):
		self.value = value

	def display(self,depth):
		print ("    " * depth) + "----I_IVAL = " + str(self.value)

class I_BVAL:
	def __init__(self,value):
		self.value = value
	def display(self,depth):
		print ("    " * depth) + "----I_BVAL = " + str(self.value)

class I_ID:
	def __init__(self,level,offset):
		self.level = level
		self.offset = offset
	def display(self,depth):
		print ("    "  * depth) + "----I_ID"
		print ("    " * (depth+1)) + "Level: " + str(self.level)
		print ("    " * (depth+1)) + "Offset: " + str(self.offset)

class I_APP:
	def __init__(self,operand,exprs):
		self.oper = operand
		self.exprs = exprs
	def display(self,depth):
		print ("    " * depth) + "----I_APP"
		self.oper.display(depth+1)
		print ("    " * (depth+1)) + "[Operands]"
		for expr in self.exprs:
			expr.display(depth+2)


# Here are the types belonging to I_opn

class I_CALL:
	def __init__(self,name,level):
		self.name = name
		self.level = level
	def display(self,depth):
		print ("    " * depth) + "----I_CALL"
		print ("    " * (depth+1)) + "Name: " + self.name
		print ("    " * (depth+1)) + "Level: " + str(self.level)

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