# Everything in here is pretty straightforward; This file contains
# definitions for all the data types defined in the pdf describing M+
# Nodes which have lists of things for children have had special
# display() methods defined which create "fake" nodes for display
# purposes only. (For example, the node [decls] does not actually
# exist in the tree, but is shown simply to make the tree more readable.

#All the nodes in the tree have to inherit from this!
from SymbolTable import *
from IRTypes import *
import copy

def getNames(types):
	names = ""
	readable = { M_int:"INT",
		     M_bool:"BOOL"}
	for item in types:
		names += ", " + readable[item]
	if len(names) > 2:
		names = names[2:]
	return "[" + names + "]"

def getReadable(aType):
	readable = { M_int:"INT",
		     M_bool:"BOOL"}
	return readable[aType]

class Node(object):
	def display(self, depth):
		print (depth)*"    " + "+---" + self.type
		for child in self.children:
			child.display(depth+1)
	def __init__(self):
		self.children = []		
		self.value = None
		self.position = None



class M_prog(Node):
	def __init__(self,decls,stmts,line):
		super(M_prog,self).__init__()
		self.type = "M_prog"
		self.decls = decls
		self.stmts = stmts
		self.position = line
	def collect(self):
		table = SymbolTable()
		offset = 0;
		label = 0;
		for decl in self.decls:
			if type(decl) == M_var:
				table.insert(decl.name,decl.internalType, "VAR", offset)
				offset += 1
			elif type(decl) == M_fun:
				table.insert(decl.name,decl.returnType, "FUN", ("fn" + str(label),decl.params))
				label += 1
		return table

	def check(self):
		table = self.collect()
		funIR = []
		stmtsIR = []
		varCount = 0
		for decl in self.decls:
			if type(decl) == M_var:
				varCount += 1
			elif type(decl) == M_fun:
				funIR += [decl.check(table)]
		for stmt in self.stmts:
			stmtsIR += [stmt.check(table)]
		return I_PROG(funIR,varCount,stmtsIR)
		
		
		
	def display(self, depth):
		print (depth)*"    " + "+---" + self.type
		if len(self.decls) > 0:
			print (depth+1)*"    " + "+---[decls]"
		for decl in self.decls:
			decl.display(depth+2)
		if len(self.stmts) > 0:
			print (depth+1)*"    " + "+---[Statements]"
		for stmt in self.stmts:
			stmt.display(depth+2)

class M_var(Node):
	'''There's no literal translation for this into an IR node. We handle it in the
	various collect() methods, to add rows to the symbol table.'''
	def __init__(self,name,exprs,internalType,line):
		super(M_var,self).__init__()
		self.position = line
		self.type = "M_var"
		self.name = name
		self.exprs = exprs
		self.internalType = internalType
	def display(self,depth):
		print (depth)*"    " + "+---" + self.type
		print (depth+1)*"    " + "+---\"" + self.name + "\""
		for expression in self.exprs:
			expression.display(depth+1)
		self.internalType.display(depth+1)

class M_fun(Node):
	def __init__(self,name,params,returnType,decls,stmts,line):
		super(M_fun,self).__init__()
		self.position = line
		self.type = "M_fun"
		self.name = name
		self.params = params
		self.returnType = returnType
		self.decls = decls
		self.stmts = stmts
		
	def collect(self,table):
		newTable = copy.deepcopy(table)
		newTable.push()
		offset = 0;
		label = 0;
		for decl in self.decls:
			if type(decl) == M_var:
				newTable.insert(decl.name,decl.internalType, "VAR", offset)
				offset += 1
			elif type(decl) == M_fun:
				newTable.insert(decl.name,decl.returnType, "FUN", ("fn" + str(label),decl.params))
				label += 1

		for param in self.params:
			newTable.insert(param[0],param[2],"VAR",offset)
			offset += 1

		return newTable

	def display(self,depth):
		print (depth)*"    " + "+---" + self.type
		print (depth+1)*"    " + "+---\"" + self.name + "\""
		
		if len(self.params) > 0:
			print (depth+1)*"    " + "+---[Parameters]"
		for param in self.params:
			print(depth+2)*"    " + "+---" + param[0] + ", [], " + param[2].type
		self.returnType.display(depth+1)

		if len(self.decls) > 0:
			print (depth+1)*"    " + "+---[decls]"
		for declaration in self.decls:
			declaration.display(depth+2)
		if len(self.stmts) > 0: #Why would this ever not be true?
			print (depth+1)*"    " + "+---[Statements]"
		for stmt in self.stmts:
			stmt.display(depth+2)
	def check(self,table):
		varCount = 0
		funIR = []
		stmtsIR = []
		deeperTable = self.collect(table)

		for decl in self.decls:
			if type(decl) == M_var:
				varCount += 1
			elif type(decl) == M_fun:
				funIR += [decl.check(deeperTable)]
		for stmt in self.stmts:
			stmtsIR += [stmt.check(deeperTable)]
		
		return I_FUN(self.name,funIR,varCount,len(self.params), stmtsIR)
				
	
class M_ass(Node):
	def __init__(self,id,expressions,assignment,line):
		super(M_ass,self).__init__()
		self.position = line
		self.type = "M_ass"
		self.id = id
		self.expressions = expressions
		self.assignment = assignment
	
	def display(self,depth):
		print (depth)*"    " + "+---" + self.type
		print (depth+1)*"    " + "+---\"" + self.id + "\""
		for expression in self.expressions:
			expression.display(depth+1)
		self.assignment.display(depth+1)
	
	def check(self,table):
		var = table.lookup(self.id)
		if var == None:
			raise LookupError("Line " + str(self.position) +": Symbol '" + self.id + "' was not declared.")
		elif var["info"].symClass != "VAR":
			raise TypeError("Line " + str(self.position) +": Symbol '"+self.id+"' is not a variable, so we can't assign something to it.")
		else:
			if type(var["info"].symType) == type(self.assignment.check(table)["type"]):
				return I_ASS(var["level"],var["info"].symAttrib,self.assignment.check(table)["IR"])
			else:
				raise TypeError("Line " + str(self.position) +": Variable '"+self.id+"' is of type " + getReadable(type(var["info"].symType)) + ", but you supplied type " + getReadable(type(self.assignment.check(table)["type"])))
	#children: String, Expr_list, M_expr

class M_while(Node):
	def __init__(self,expr,stmt,line):
		super(M_while,self).__init__()
		self.position = line
		self.expr = expr
		self.stmt = stmt
		self.type = "M_while"

	def display(self, depth):
		print (depth)*"    " + "+---" + self.type
		self.expr.display(depth+1)
		self.stmt.display(depth+1)
	def check(self,table):
		if type(self.expr.check(table)["type"]) == M_bool:
			return I_WHILE(self.expr.check(table)["IR"], 
					self.stmt.check(table))
		else:
			raise TypeError("Line " + str(self.position) +": Conditional predicate must be a boolean expression.")
		

class M_cond(Node):
	def __init__(self,expr,truestmt,falsestmt,line):
		super(M_cond,self).__init__()
		self.position = line
		self.expr = expr
		self.truestmt = truestmt
		self.falsestmt = falsestmt
		self.type = "M_cond"

	def display(self, depth):
		print (depth)*"    " + "+---" + self.type
		self.expr.display(depth+1)
		self.truestmt.display(depth+1)
		self.falsestmt.display(depth+1)
	def check(self,table):
		if type(self.expr.check(table)["type"]) == M_bool:
			return I_COND(self.expr.check(table)["IR"], 
				self.truestmt.check(table), 
				self.falsestmt.check(table))
		else:
			raise TypeError("Line " + str(self.position) +": Conditional predicate must be a boolean expression.")
		

class M_read(Node):
	def __init__(self,givenID,expressions,line):
		super(M_read,self).__init__()
		self.position = line
		self.id = givenID
		self.expressions = expressions
		self.type = "M_read"

	def display(self,depth):	
		print ("    "*depth) + "+---" + self.type + " (" + self.id + ")"
		# print "    "*(depth+1) + "+---" + self.oper.type
		for expression in self.expressions:
			expression.display(depth+1)

	def check(self,table):
		result = table.lookup(self.id)
		if result == None:
			raise TypeError("Line " + str(self.position) +": Symbol '" + self.id + "' not declared.");
		if result["info"].symClass == "VAR":
			if type(result["info"].symType) == M_bool:
				varInfo = table.lookup(self.id)
				return I_READ_B(varInfo["level"],varInfo["info"].symAttrib)
			elif type(result["info"].symType) == M_int:
				varInfo = table.lookup(self.id)
				return I_READ_I(varInfo["level"],varInfo["info"].symAttrib)

class M_print(Node):
	def __init__(self,expr,line):
		super(M_print,self).__init__()
		self.position = line
		self.type = "M_print"
		self.children = [expr]
	def check(self,table):
		exprType = self.children[0].check(table)["type"]
		if type(exprType) == M_int:
			return I_PRINT_I(self.children[0].check(table)["IR"])
		elif type(exprType) == M_bool:
			return I_PRINT_B(self.children[0].check(table)["IR"])

class M_return(Node):
	def __init__(self,expr,line):
		super(M_return,self).__init__()
		self.position = line
		self.type = "M_return"
		self.children = [expr]
	def check(self,table):
		return I_RETURN(self.children[0].check(table)["IR"])

class M_block(Node):
	def __init__(self,decls,stmts,line):
		super(M_block,self).__init__()
		self.position = line
		self.decls = decls
		self.stmts = stmts
		self.type = "M_block"


	def collect(self,table):
		newTable = copy.deepcopy(table)
		newTable.push()
		offset = 0;
		label = 0;
		for decl in self.decls:
			if type(decl) == M_var:
				newTable.insert(decl.name,decl.internalType, "VAR", offset)
				offset += 1
			elif type(decl) == M_fun:
				newTable.insert(decl.name,decl.returnType, "FUN", ("fn" + str(label),decl.params))
				label += 1
		return newTable

	def display(self,depth):
		print (depth)*"    " + "+---" + self.type
		if len(self.decls) > 0:
			print (depth+1)*"    " + "+---[decls]"
			for decl in self.decls:
				decl.display(depth+2)
		if len(self.stmts) > 0:
			print (depth+1)*"    " + "+---[Statements]"
			for stmts in self.stmts:
				stmts.display(depth+2)
	def check(self,table):
		varCount = 0
		functionsIR = []
		stmtsIR = []
		deeperTable = self.collect(table) #this is the table we'll use when checking children of this node.

		for decl in self.decls:
			if type(decl) == M_var:
				varCount += 1
			elif type(decl) == M_fun:
				functionsIR += [decl.check(deeperTable)]

		for stmt in self.stmts:
			stmtsIR += [stmt.check(deeperTable)]
		return I_BLOCK(functionsIR,varCount,stmtsIR)


class M_int(Node):
	def __init__(self,line):
		super(M_int,self).__init__()
		self.position = line
		self.type = "M_int"
		self.name = "INT"

class M_bool(Node):
	def __init__(self,line):
		super(M_bool,self).__init__()
		self.position = line
		self.type = "M_bool"
		self.name = "BOOL"

class M_ival(Node):
	def __init__(self,value,line):
		super(M_ival,self).__init__()
		self.position = line
		self.value = value
		self.type = "M_ival"
	def display(self, depth):
		print ("    "*(depth)) + "+---" + self.type + " (" + str(self.value) + ")"
	def check(self,tree):
		'''We don't have to do any checking because our tokenizer can
		differentiate between the only two types: int and bool'''
		return {"IR":I_IVAL(self.value), "type":M_int(-1)}

class M_bval(Node):
	def __init__(self,value,line):
		super(M_bval,self).__init__()
		self.position = line
		self.value = value
		self.type = "M_bval"
	def display(self, depth):
		print "    "*depth + "+---" + self.type + " (" + str(self.value) + ")"
	def check(self,table):
		'''We don't have to do any checking because our tokenizer can
		differentiate between the only two types: int and bool'''
		return {"IR":I_BVAL(self.value), "type":M_bool(-1)}



class M_id(Node):
	def __init__(self,name,expressions,line):
		super(M_id,self).__init__()
		self.position = line
		self.type = "M_id"
		self.name = name
		self.expressions = expressions
	def display(self,depth):
		print("    "*depth)+"+---" + self.type
		print("    "*(depth+1))+"+---\"" + self.name + "\""
		for expression in self.expressions:
			expression.display(depth+1)

	def check(self,table):
		result = table.lookup(self.name)
		if result == None:
			raise TypeError("Line " + str(self.position) +": Symbol '" + self.name + "' not declared.");
		if result["info"].symClass == "VAR":
			return {"IR":I_ID(result["level"],result["info"].symAttrib), "type": result["info"].symType}
		raise TypeError("Line " + str(self.position) +": Symbol " + self.name + " is a function, we need a variable!")

class M_app(Node):
	#M_app is the workhorse node, responsible for the application of all
	#the different operations, as well as function application.
	def __init__(self,operator,expressions,line):
		super(M_app,self).__init__()
		self.position = line
		self.oper = operator
		self.expressions = expressions
		self.type = "M_app"

	def display(self, depth):
		print ("    "*depth) + "+---" + self.type
		self.oper.display(depth+1)
		if len(self.expressions) > 0:
			print (depth+2)*"    " + "+---[Expressions]"
		for expression in self.expressions:
			expression.display(depth+3)

	def check(self,table):
		exprType = None
		operandType = None
		typesFound = []
		exprIR = []
		opsOnInts = {
			M_add:I_ADD_I(),
			M_sub:I_SUB_I(),
			M_mul:I_MUL_I(),
			M_div:I_DIV_I(),
			M_eq:I_EQ_I(),
			M_lt:I_LT_I(),
			M_le:I_LE_I(),
			M_ge:I_GE_I(),
			M_gt:I_GT_I(),
			M_neg:I_NEG_I()
		}
		
		opsOnBools = {
			M_and:I_AND(),
			M_or:I_OR(),
			M_not:I_NOT()
		}

		yieldsInts = {
			M_add:I_ADD_I(),
			M_sub:I_SUB_I(),
			M_mul:I_MUL_I(),
			M_div:I_DIV_I(),
			M_neg:I_NEG_I()
		}
		
		yieldsBools = {
			M_eq:I_EQ_I(),
			M_lt:I_LT_I(),
			M_le:I_LE_I(),
			M_ge:I_GE_I(),
			M_gt:I_GT_I(),
			M_and:I_AND(),
			M_or:I_OR(),
			M_not:I_NOT()
		}

		# first, we gather the IR for all the expressions
		# We abandon ship if ever one expression's type is different from what we've encountered.
		
		if yieldsInts.has_key(type(self.oper)):
			exprType = M_int(-1)
		elif yieldsBools.has_key(type(self.oper)):
			exprType = M_bool(-1)
		elif type(self.oper) == M_fn:
			if table.lookup(self.oper.name) == None:
				raise LookupError("Line " + str(self.position) +": Function '" + self.oper.name + "' was not declared in this scope.")
			exprType = table.lookup(self.oper.name)["info"].symType

		if opsOnInts.has_key(type(self.oper)):
			operandType = M_int(-1)
		elif opsOnBools.has_key(type(self.oper)):
			operandType = M_bool(-1)

		# print "The following expression is of type", type(exprType), "beacause of", type(self.oper)
		# self.display(0)

		if exprType == None:
			raise TypeError("Line " + str(self.position) +": Unrecognized type!")

		# whatever happens, we need to populate exprIR all the same
		for expr in self.expressions:
			# print "problem:",expr.name
			exprIR += [expr.check(table)["IR"]]
			typesFound += [type(expr.check(table)["type"])]
		
		# If the operator is a primative, not a function, then we just have to make sure the operands
		# are all the same type as the operator type.
		if (type(self.oper) != M_fn):
			for expr in self.expressions:
				if type(expr.check(table)["type"]) != type(operandType):
					raise TypeError("Line " + str(self.oper.position) +": Type mismatch. Expected " + getReadable(type(operandType)) + ", found " + getReadable(type(expr.check(table)["type"])))

		# If the operator is a funciton, we need to check that the function exists.
		# And that the supplied arguments are each of the right type.
		if type(self.oper) == M_fn:
			if table.lookup(self.oper.name) == None:
				raise LookupError("Line " + str(self.position) +": Symbol '" + self.oper.name + "' was not declared.");
			argTypes = []
			for item in table.lookup(self.oper.name)["info"].symAttrib[1]:
				argTypes += [type(item[2])]

			if argTypes != typesFound:
				raise TypeError("Line " + str(self.position) +": Function '" + self.oper.name + "' expected arguments " + getNames(argTypes) + ", received arguments " + getNames(typesFound))

			return {"IR":I_APP(I_CALL(self.oper.name,table.lookup(self.oper.name)["level"]), exprIR ),
				"type":exprType}
		else:
			ops = {}
			if type(operandType) == M_int:
				ops = opsOnInts
			elif type(operandType) == M_bool:
				ops = opsOnBools
			if not ops.has_key(type(self.oper)):
				raise TypeError("Line " + str(self.position) +": Operator \"" + self.oper.name + "\" does not support arguments of type " + exprType.type)
			return {"IR":I_APP(ops[type(self.oper)],exprIR),
				"type":exprType}

class M_fn(Node):
	def __init__(self,name,line):
		super(M_fn,self).__init__()
		self.position = line
		self.type = "M_fn"
		self.name = name
	def display(self,depth):
		print ("    "*depth) + "+---" + self.type + " (" + self.name + ")"
	def check(self,table):
		return I_CALL(self.name,table.lookup(self.name)["level"]);

class M_add(Node):
	def __init__(self,line):
		super(M_add,self).__init__()
		self.position = line
		self.type = "M_add"
		self.name = "+"


class M_mul(Node):
	def __init__(self,line):
		super(M_mul,self).__init__()
		self.position = line
		self.type = "M_mul"
		self.name = "*"

class M_sub(Node):
	def __init__(self,line):
		super(M_sub,self).__init__()
		self.position = line
		self.type = "M_sub"
		self.name = "-"

class M_div(Node):
	def __init__(self,line):
		super(M_div,self).__init__()
		self.position = line
		self.type = "M_div"
		self.name = "/"

class M_neg(Node):
	def __init__(self,line):
		super(M_neg,self).__init__()
		self.position = line
		self.type = "M_neg"
		self.name = "-"

class M_lt(Node):
	def __init__(self,line):
		super(M_lt,self).__init__()
		self.position = line
		self.type = "M_lt"
		self.name = "<"

class M_le(Node):
	def __init__(self,line):
		super(M_le,self).__init__()
		self.position = line
		self.type = "M_le"
		self.name = "<="

class M_gt(Node):
	def __init__(self,line):
		super(M_gt,self).__init__()
		self.position = line
		self.type = "M_gt"
		self.name = ">"

class M_ge(Node):
	def __init__(self,line):
		super(M_ge,self).__init__()
		self.position = line
		self.type = "M_ge"
		self.name = ">="

class M_eq(Node):
	def __init__(self,line):
		super(M_eq,self).__init__()
		self.position = line
		self.type = "M_eq"
		self.name = "="

class M_not(Node):
	def __init__(self,line):
		super(M_not,self).__init__()
		self.position = line
		self.type = "M_not"
		self.name = "not"

class M_and(Node):
	def __init__(self,line):
		super(M_and,self).__init__()
		self.position = line
		self.type = "M_and"
		self.name = "&&"

class M_or(Node):
	def __init__(self,line):
		super(M_or,self).__init__()
		self.position = line
		self.type = "M_or"
		self.name = "||"
