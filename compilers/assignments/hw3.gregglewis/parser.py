# Each function in this file corresponds to a production (or several
# productions) in the M grammar. Each of them sets p[0] to a subtree of the
# overall syntax tree. There are some exceptions, for things like fun_body
# which is actually a list of statements. In this case, a list is placed in
# p[0] and something further along the line will pass this list as an argument
# in the initializer for a tree node which expects a list.

import ply.yacc as yacc
from scanner import tokens
from tree import *
import sys

start = "prog"

def p_expr_or(p):
	'''expr : expr OR bint_term
	        | bint_term'''
	if len(p) == 2:
		p[0]  = p[1]
	elif len(p) == 4:
		p[0] = M_app(M_or(), [p[1],p[3]])

def p_addop(p):
	'''addop : ADD 
	| SUB'''
	if p[1] == "+":
		p[0] = M_add()
	elif p[1] == "-":
		p[0] = M_sub()


def p_int_expr(p):
	'''int_expr : int_expr addop int_term
	            | int_term'''
	if len(p) == 2:
		p[0] = p[1]
	elif len(p) == 4:
		p[0] = M_app(p[2],[p[1],p[3]])

def p_int_term(p):
	'''int_term : int_term mulop int_factor
	            | int_factor'''
	if len(p) == 2:
		p[0] = p[1]
	elif len(p) == 4:
		p[0] = M_app(p[2],[p[1],p[3]])
	pass

def p_mulop(p):
	'''mulop : MUL 
	         | DIV'''
	if p[1] == "*":
		p[0] = M_mul()
	elif p[1] == "/":
		p[0] = M_div()

def p_int_factor_expr(p):
	'''int_factor : LPAR expr RPAR'''
	p[0] = p[2]

def p_int_factor_ival(p):
	'''int_factor : IVAL'''
	p[0] = M_ival(p[1])
	
def p_int_factor_bval(p):
	'''int_factor : BVAL'''
	p[0] = M_bval(p[1])

def p_int_factor_sub(p):
	'''int_factor : SUB int_factor'''
	p[0] = M_app(M_neg(), [p[2]])

def p_bintterm_and(p):
	'''bint_term : bint_term AND bint_factor'''
	p[0] = M_app(M_and(), [p[1],p[3]])

def p_bintterm_bintfactor(p):
	'''bint_term : bint_factor'''
	p[0] = p[1]
	
def p_bint_factor_not(p):
	'''bint_factor : NOT bint_factor'''
	p[0] = M_app(M_not(),[p[2]])

def p_bint_factor_compare(p):
	'''bint_factor : int_expr compare_op int_expr'''
	p[0] = M_app(p[2],[p[1],p[3]])

def p_bint_factor_intexpr(p):
	'''bint_factor : int_expr'''
	p[0] = p[1]

def p_compare_op(p):
	'''compare_op : EQUAL
			| LT
			| GT
			| LE
			| GE'''
	operations = {
		"=":M_eq(),
		"<":M_lt(),
		">":M_gt(),
		"=<":M_le(),
		">=":M_ge()
	}
	p[0] = operations.get(p[1])

def p_int_factor_id(p):
	'''int_factor : ID argument_list'''
	if p[2] == None:
		p[0] = M_id(p[1],[])
	else:
		p[0] = M_app(M_fn(p[1]),p[2])

def p_argument_list(p):
	'''argument_list : LPAR arguments RPAR'''
	p[0] = p[2]

def p_arguments(p):
	'''arguments : arguments1'''
	p[0] = p[1]

def p_arguments_empty(p):
	'''arguments : empty'''
	p[0] = []

def p_argument_list_empty(p):
	'''argument_list : empty'''
	p[0] = None

def p_arguments1(p):
	'''arguments1 : arguments1 COMMA expr
		      | expr'''
	if len(p) == 2:
		p[0] = [p[1]]
		
	elif len(p) == 4:
		p[0] = p[1] + [p[3]]

def p_empty(p):
	'empty :'
	pass

def p_program_body(p):
	'''program_body : BEGIN prog_stmts END
			| prog_stmts'''
	if len(p) == 4:
		p[0] = p[2]
	elif len(p) == 2:
		p[0] = p[1]

def p_prog_stmts(p):
	'''prog_stmts : prog_stmt SEMICOLON prog_stmts'''
	p[0] = [p[1]] + p[3]

def p_prog_stmts_empty(p):
	'''prog_stmts : empty'''
	p[0] = []

def p_prog_stmt(p):
	'''prog_stmt : IF expr THEN prog_stmt ELSE prog_stmt
		     | WHILE expr DO prog_stmt
		     | READ ID
		     | ID ASSIGN expr
		     | PRINT expr'''

	if p[1] == "if":
		p[0] = M_cond(p[2],p[4],p[6])

	elif p[1] == "while":
		p[0] = M_while(p[2],p[4])
	
	elif p[1] == "read":
		p[0] = M_read(p[2],[]) #if we add arrays, this has to change.

	elif len(p) == 4: #this is filthy, but ASSIGN is the only one that fits
		p[0] = M_ass(p[1],[],p[3]) #will have to change for arrays

	elif p[1] == "print":
		p[0] = M_print(p[2])

def p_fun_body(p):
	'''fun_body : BEGIN prog_stmts RETURN expr SEMICOLON END'''
	p[0] = p[2] + [M_return(p[4])]

def p_fun_body_nobegin(p):
	'''fun_body : prog_stmts RETURN expr SEMICOLON'''
	p[0] = p[1] + [M_return(p[3])]

def p_type(p):
	'''type : INT
		| BOOL'''
	if p[1] == "bool":
		p[0] = M_bool()
	elif p[1] == "int":
		p[0] = M_int()

def p_identifier(p):
	'''identifier : ID'''
	p[0] = p[1]

def p_basic_var_declaration(p):
	'''basic_var_declaration : identifier COLON type'''
	p[0] = (p[1],[],p[3])
	
def p_var_declaration(p):
	'''var_declaration : VAR basic_var_declaration'''
	p[0] = M_var(p[2][0],p[2][1],p[2][2])

def p_parameters1(p):
	'''parameters1 : parameters1 COMMA basic_var_declaration'''
	p[0] = p[1] + [p[3]]

def p_parameters1_basic(p):
	'''parameters1 : basic_var_declaration'''
	p[0] = [p[1]]

def p_parameters(p):
	'''parameters : parameters1'''
	p[0] = p[1]

def p_parameters_empty(p):
	'''parameters : empty'''
	p[0] = []

def p_param_list(p):
	'''param_list : LPAR parameters RPAR'''
	p[0] = p[2]

def p_fun_declaration(p):
	'''fun_declaration : FUN identifier param_list COLON type CLPAR fun_block CRPAR'''
	p[0] = M_fun(p[2], p[3], p[5], p[7][0], p[7][1])


def p_fun_block(p):
	'''fun_block : declarations fun_body'''
	p[0] = (p[1], p[2])

def p_declarations(p):
	'''declarations : declaration SEMICOLON declarations'''
	p[0] = [p[1]] + p[3]

def p_declarations_empty(p):
	'''declarations : empty'''
	p[0] = []

def p_declaration_var(p):
	'''declaration : var_declaration'''
	p[0] = p[1]

def p_declaration_fun(p):
	'''declaration : fun_declaration'''
	p[0] = p[1]

def p_prog(p):
	'''prog : block'''
	p[0] = M_prog(p[1].decls,p[1].stmts)

def p_block(p):
	'''block : declarations program_body'''
	p[0] = M_block(p[1],p[2])

def p_prog_stmt_block(p):
	'''prog_stmt : CLPAR block CRPAR'''
	p[0] = p[2]

def p_error(p): #We raise a SyntaxError on the first error encountered. 
		# We'll catch it and exit after reporting where it was seen.
	if (p != None):
		print >> sys.stderr, "\033[1;31mUnexpected", p.type ,"on line", p.lineno,"\033[1;m"
	else:
		print >> sys.stderr, "\033[1;31mUnexpected EOF\033[1;m"
	raise SyntaxError

parser = yacc.yacc()

data = ""
for line in sys.stdin.readlines():
	data += line

try:
	result = parser.parse(data)
except SyntaxError:
	print >> sys.stderr, "\033[1;31mCould not parse the provided M code.\033[1;m"
	sys.exit(1) #indicate an error
result.display(0)