# Everything in here is pretty straightforward; This file contains
# definitions for all the data types defined in the pdf describing M+
# Nodes which have lists of things for children have had special
# display() methods defined which create "fake" nodes for display
# purposes only. (For example, the node [Declarations] does not actually
# exist in the tree, but is shown simply to make the tree more readable.

#All the nodes in the tree have to inherit from this!
class Node(object):
	def display(self, depth):
		print (depth)*"    " + "+---" + self.type
		for child in self.children:
			child.display(depth+1)
	def __init__(self):
		self.children = []
		self.value = None


class M_prog(Node):
	def __init__(self,decls,stmts):
		super(M_prog,self).__init__()
		self.type = "M_prog"
		self.decls = decls
		self.stmts = stmts
	def display(self, depth):
		print (depth)*"    " + "+---" + self.type
		if len(self.decls) > 0:
			print (depth+1)*"    " + "+---[Declarations]"
		for decl in self.decls:
			decl.display(depth+2)
		if len(self.stmts) > 0:
			print (depth+1)*"    " + "+---[Statements]"
		for stmt in self.stmts:
			stmt.display(depth+2)

class M_var(Node):
	def __init__(self,name,exprs,internalType):
		super(M_var,self).__init__()
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
	def __init__(self,name,params,returnType,declarations,stmts):
		super(M_fun,self).__init__()
		self.type = "M_fun"
		self.name = name
		self.params = params
		self.returnType = returnType
		self.declarations = declarations
		self.stmts = stmts

	def display(self,depth):
		print (depth)*"    " + "+---" + self.type
		print (depth+1)*"    " + "+---\"" + self.name + "\""
		
		if len(self.params) > 0:
			print (depth+1)*"    " + "+---[Parameters]"
		for param in self.params:
			print(depth+2)*"    " + "+---" + param[0] + ", [], " + param[2].type
		self.returnType.display(depth+1)

		if len(self.declarations) > 0:
			print (depth+1)*"    " + "+---[Declarations]"
		for declaration in self.declarations:
			declaration.display(depth+2)
		if len(self.stmts) > 0: #Why would this ever not be true?
			print (depth+1)*"    " + "+---[Statements]"	
		for stmt in self.stmts:
			stmt.display(depth+2)
	
class M_ass(Node):
	def __init__(self,id,expressions,assignment):
		super(M_ass,self).__init__()
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
		
	#children: String, Expr_list, M_expr

class M_while(Node):
	def __init__(self,expr,stmt):
		super(M_while,self).__init__()
		self.expr = expr
		self.stmt = stmt
		self.type = "M_while"

	def display(self, depth):
		print (depth)*"    " + "+---" + self.type
		self.expr.display(depth+1)
		self.stmt.display(depth+1)

class M_cond(Node):
	def __init__(self,expr,truestmt,falsestmt):
		super(M_cond,self).__init__()
		self.expr = expr
		self.truestmt = truestmt
		self.falsestmt = falsestmt
		self.type = "M_cond"

	def display(self, depth):
		print (depth)*"    " + "+---" + self.type
		self.expr.display(depth+1)
		self.truestmt.display(depth+1)
		self.falsestmt.display(depth+1)

class M_read(Node):
	def __init__(self,id,expressions):
		super(M_read,self).__init__()
		self.id = id
		self.expressions = expressions
		self.type = "M_read"

	def display(self,depth):	
		print ("    "*depth) + "+---" + self.type + " (" + self.id + ")"
		# print "    "*(depth+1) + "+---" + self.oper.type
		for expression in self.expressions:
			expression.display(depth+1)
		
	
class M_print(Node):
	def __init__(self,expr):
		super(M_print,self).__init__()
		self.type = "M_print"
		self.children = [expr]

class M_return(Node):
	def __init__(self,expr):
		super(M_return,self).__init__()
		self.type = "M_return"
		self.children = [expr]

class M_block(Node):
	def __init__(self,decls,stmts):
		super(M_block,self).__init__()
		self.decls = decls
		self.stmts = stmts
		self.type = "M_block"

	def display(self,depth):
		print (depth)*"    " + "+---" + self.type
		if len(self.decls) > 0:
			print (depth+1)*"    " + "+---[Declarations]"
			for decl in self.decls:
				decl.display(depth+2)
		if len(self.stmts) > 0:
			print (depth+1)*"    " + "+---[Statements]"
			for stmts in self.stmts:
				stmts.display(depth+2)

class M_int(Node):
	def __init__(self):
		super(M_int,self).__init__()
		self.type = "M_int"

class M_bool(Node):
	def __init__(self):
		super(M_bool,self).__init__()
		self.type = "M_bool"

class M_stringtype(Node):#Not sure what this guy is for... arrays/datatypes??
	def __init__(self,value):
		super(M_stringtype,self).__init__()
		self.value = None

class M_ival(Node):
	def __init__(self,value):
		super(M_ival,self).__init__()
		self.value = value
		self.type = "M_ival"
	def display(self, depth):

		print ("    "*(depth)) + "+---" + self.type + " (" + str(self.value) + ")"


class M_bval(Node):
	def __init__(self,value):
		super(M_bval,self).__init__()
		self.value = value
		self.type = "M_bval"
	def display(self, depth):
		print "    "*depth + "+---" + self.type + " (" + str(self.value) + ")"

class M_rval(Node):
	def __init__(self,value):
		super(M_rval,self).__init__()
		self.value = value
		self.type = "M_rval"
	def display(self, depth):
		print "    "*depth + "+---" + self.type + " (" + str(self.value) + ")"

class M_cval(Node):
	def __init__(self,value):
		super(M_cval,self).__init__()
		self.value = value
		self.type = "M_cval"
	def display(self, depth):
		print "    "*depth + "+---" + self.type + " (" + str(self.value) + ")"

class M_id(Node):
	def __init__(self,name,expressions):
		super(M_id,self).__init__()
		self.type = "M_id"
		self.name = name
		self.expressions = expressions
	def display(self,depth):
		print("    "*depth)+"+---" + self.type
		print("    "*(depth+1))+"+---\"" + self.name + "\""
		for expression in self.expressions:
			expression.display(depth+1)


class M_app(Node):
	#M_app is the workhorse node, responsible for the application of all
	#the different operations, as well as function application.
	def __init__(self,operator,expressions):
		super(M_app,self).__init__()
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

class M_fn(Node):
	def __init__(self,name):
		super(M_fn,self).__init__()
		self.type = "M_fn"
		self.name = name
	def display(self,depth):
		print ("    "*depth) + "+---" + self.type + " (" + self.name + ")"

class M_add(Node):
	def __init__(self):
		super(M_add,self).__init__()
		self.type = "M_add"

class M_mul(Node):
	def __init__(self):
		super(M_mul,self).__init__()
		self.type = "M_mul"

class M_sub(Node):
	def __init__(self):
		super(M_sub,self).__init__()
		self.type = "M_sub"

class M_div(Node):
	def __init__(self):
		super(M_div,self).__init__()
		self.type = "M_div"

class M_neg(Node):
	def __init__(self):
		super(M_neg,self).__init__()
		self.type = "M_neg"

class M_lt(Node):
	def __init__(self):
		super(M_lt,self).__init__()
		self.type = "M_lt"

class M_le(Node):
	def __init__(self):
		super(M_le,self).__init__()
		self.type = "M_le"

class M_gt(Node):
	def __init__(self):
		super(M_gt,self).__init__()
		self.type = "M_gt"

class M_ge(Node):
	def __init__(self):
		super(M_ge,self).__init__()
		self.type = "M_ge"

class M_eq(Node):
	def __init__(self):
		super(M_eq,self).__init__()
		self.type = "M_eq"

class M_not(Node):
	def __init__(self):
		super(M_not,self).__init__()
		self.type = "M_not"

class M_and(Node):
	def __init__(self):
		super(M_and,self).__init__()
		self.type = "M_and"

class M_or(Node):
	def __init__(self):
		super(M_or,self).__init__()
		self.type = "M_or"
