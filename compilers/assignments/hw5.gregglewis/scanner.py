# Author: Gregg Thomas Lewis
# Title: Simple Minisculus Scanner
# Course: CPSC 411
# Term: Winter 2011



import ply.lex as lex
import sys


states = (('comment','exclusive'),)
	
	
t_ignore = (" \t\r") #ignore spaces, tabs, and newlines
t_comment_ignore = (".\r\n")

# Basic token handling
# This approach to handling reserved words and identifiers is taken
# more or less directly from the PLY documentation, 
# Section 4.3: Specification of tokens. 
	
reserved = {
	"if":"IF",
	"then":"THEN",
	"while":"WHILE",
	"do":"DO",
	"read":"READ",
	"else":"ELSE",
	"begin":"BEGIN",
	"end":"END",
	"print":"PRINT",
	"int":"INT",
	"bool":"BOOL",
	"var":"VAR",
	"fun":"FUN",
	"not":"NOT",
	"return":"RETURN"

}

tokens = [
	# "NEWLINE",
	"ID", #look at 4.4, token values to do these!
	"ADD",
	"SUB",
	"MUL",
	"DIV",
	"LPAR",
	"RPAR",
	"ASSIGN",
	"SEMICOLON",
	"AND",
	"OR",
	"EQUAL",
	"LT",
	"GT",
	"LE",
	"GE",
	"CLPAR",
	"CRPAR",
	"COLON",
	"COMMA",
	"BVAL",
	"IVAL"
	# ,
	# "RVAL",
	# "CVAL"
] + list(reserved.values())

t_ADD = (r"\+")
t_ASSIGN = (r"\:\=")
t_SUB = (r"\-")
t_MUL = (r"\*")
t_DIV = (r"\/")
t_LPAR = (r"\(")
t_RPAR = (r"\)")
t_SEMICOLON = (r"\;")


t_AND = (r"\&\&")
t_OR = (r"\|\|")


t_LE = (r"=<")
t_GE = (r">=")
t_LT = (r"<")
t_GT = (r">")
t_EQUAL = (r"\=")


t_CLPAR = (r"\{")
t_CRPAR = (r"\}")

t_COLON = (r"\:")
t_COMMA = (r"\,")



# def t_RVAL(t):
# 	r"[0-9]+\.[0-9]*"
# 	t.value = float(t.value)
# 	return t
# 	
# def t_CVAL(t):
# 	r"\"[A-Za-z0-9]\"" #I omitted the ^_ from this regex. Not sure
# 	#what it was for.
# 	t.value = t.value[1]
# 	return t

def t_IVAL(t):
	r"[0-9]+"
	t.value = int(t.value)
	return t


def t_BVAL(t):
	r"false|true"
	t.value = t.value == "true"
	return t

def t_ID(t):
	r"[a-zA-Z][_a-zA-Z0-9]*"
	t.type = reserved.get(t.value,'ID')
	return t



# Do not re-order the comment token definitions, as their precedence is
# determined by the order of their declaration.


# Single line comment handling -------------------

def t_comment_INITIAL_SLCOMMENT(t):
	r'\%.*'
	pass

	
# Multiline Comment Handling
# Note that while these function definitions are separated into
# multi-line and single-line here, it is important to remember that there
# are two distinct states to consider. In the comment state (which is
# pushed onto the state stack whenever we encounter the start of a
# comment block), the order is as follows:
# 	1- Look for single-line comments
# 		-Note this takes care of lines like % this */
# 	2- Look for comment block "open" symbol (/*)
# 		-Push another comment state onto the stack if we see
# 		 one.
# 	3- Look for the comment block "close" symbol (*/)
# 		-Pop off the top of the stack if we see one.
# 	4- Look for any other character (Comment body)
# 		-Ignore it.
# Observe that handling comments in this way means that multi-line
# comments must have their open and close symbols matched like brackets.
# 
# When we're not in the comment state, we look first for single-line
# comments and then for multi-line ones. The latter pushes a comment
# state onto the stack, as above.

def t_comment_INITIAL_STARTCOMMENT(t):
	r"\/\*"
	t.lexer.push_state('comment')
	
def t_comment_FINCOMMENT(t):
	r"(\*\/)(?!\n)"
	t.lexer.pop_state()

def t_comment_FINCOMMENTNL(t):
	r"(\*\/)(?=\n)"
	t.lexer.lineno += 1
	t.lexer.pop_state()
	
	
def t_NEWLINE(t):
	r"\n+"
	t.lexer.lineno += len(t.value)
	pass

def t_comment_MLCOMMENTNL(t):
	r".\n"
	t.lexer.lineno += 1
	pass # ignore contents of comments

def t_comment_ALL(t):
	r"."
	pass


# Error handling ------------------------------
def t_comment_error(t):
	print "Some kind of horrible error occurred! (while commenting)"
	#this should actually NEVER happen

def t_error(t):
	print "Illegal character '"+t.value[0]+"' encountered on line",t.lineno
	sys.exit(1)
	# t.lexer.skip(1) #skip the offending character
	
lex.lex(errorlog=lex.NullLogger())


def doScan(source):
	mLexer = lex.lex(errorlog=lex.NullLogger()) #Build the lexer from the above specification
	mLexer.input(source)
	return mLexer