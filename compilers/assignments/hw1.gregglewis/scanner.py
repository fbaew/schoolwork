'''
Author: Gregg Thomas Lewis
Title: Simple Minisculus Scanner
Course: CPSC 411
Term: Winter 2011

TODO: This is all working, all that's left is to figure out EXACTLY how
comments need to work.

Oh also figure out how input should be given. Maybe a command line argument
indicating a file to scan, or maybe just scan from stdin? We're doing the
latter right now.

BEFORE SUBMITTING:

-Turn off output when dealing with comments, so we REALLY do ignore them.
-Maybe get rid of the doubly-defined t_SLCOMMENT()... Maybe not though. Ask
 in tutorial. Surely there's a more elegant way.

'''


import ply.lex as lex
import sys


states = (('comment','exclusive'),)
	
	
t_ignore = (" \t\n\r") #ignore spaces, tabs, and newlines
t_comment_ignore = (".\r\n")

# Basic token handling ---------------------------------
'''
	This approach to handling reserved words and identifiers is taken
	more or less directly from the PLY documentation, 
	Section 4.3: Specification of tokens. 
'''	
	
reserved = {
	"if":"IF",
	"then":"THEN",
	"while":"WHILE",
	"do":"DO",
	"input":"INPUT",
	"else":"ELSE",
	"begin":"BEGIN",
	"end":"END",
	"write":"WRITE"
}

tokens = [
	"NEWLINE",
	"ID", #look at 4.4, token values to do these!
	"NUM",
	"ADD",
	"ASSIGN",
	"SUB",
	"MUL",
	"DIV",
	"LPAR",
	"RPAR",
	"SEMICOLON"
] + list(reserved.values())

t_ADD = (r"\+")
t_ASSIGN = (r"\:\=")
t_SUB = (r"\-")
t_MUL = (r"\*")
t_DIV = (r"\/")
t_LPAR = (r"\(")
t_RPAR = (r"\)")
t_SEMICOLON = (r"\;")
t_NUM = (r"[0-9]+")

def t_NEWLINE(t):
	r"\n"
	pass

def t_ID(t):
	r"[a-zA-Z][a-zA-Z_0-9]*"
	t.type = reserved.get(t.value,'ID')
	return t

'''
	Do not re-order the comment token definitions, as their precedence is
	determined by the order of their declaration.
'''

# Single line comment handling -------------------

def t_comment_INITIAL_SLCOMMENT(t):
	r'\%.*'
	#We don't need to say this...
	# print "Single line comment detected and removed..."
	pass


	
# Multiline Comment Handling ---------------------
'''
	Note that while these function definitions are separated into
	multi-line and single-line here, it is important to remember that there
	are two distinct states to consider. In the comment state (which is
	pushed onto the state stack whenever we encounter the start of a
	comment block), the order is as follows:
		1- Look for single-line comments
			-Note this takes care of lines like % this */
		2- Look for comment block "open" symbol (/*)
			-Push another comment state onto the stack if we see
			 one.
		3- Look for the comment block "close" symbol (*/)
			-Pop off the top of the stack if we see one.
		4- Look for any other character (Comment body)
			-Ignore it.
	Observe that handling comments in this way means that multi-line
	comments must have their open and close symbols matched like brackets.
	
	When we're not in the comment state, we look first for single-line
	comments and then for multi-line ones. The latter pushes a comment
	state onto the stack, as above.
'''

def t_STARTCOMMENT(t):
	r"\/\*"
	#We don't actually need to say this
	# print "Starting a comment..."
	t.lexer.push_state('comment')
	
def t_comment_STARTCOMMENT(t):
	r"\/\*"
	#We don't actually need to say this
	# print "Starting a comment... from within a comment!"
	t.lexer.push_state('comment')
	
def t_comment_FINCOMMENT(t):
	r"\*\/"
	# We don't actually need to say this
	# print "Finished a comment..."
	t.lexer.pop_state()
	
def t_comment_MLCOMMENT(t):
	r"."
	pass # ignore contents of comments




# Error handling ------------------------------
def t_comment_error(t):
	print "Some kind of horrible error occurred! (while commenting)"
	#this should actually NEVER happen

def t_error(t):
	'''For right now, we just skip unrecognized characters (which I think
	are always indicative of syntax errors...). In the future, maybe we
	should build up a list of the locations of the characters so we could
	give helpful output that told the user where all the misplaced
	characters are. For example:
	
	Errors Encountered:
		line 3: illegal symbol '!'
		line 7: illegal symbol '|'
		
	This way, when we do the later steps of compiling, we can helpfully
	report things like unexpected identifiers and what not. For now, we
	will keep things simple with an example borrowed from the PLY docs.
	(4.9, Error handling)
	
	'''
	print "Illegal character '%s' encountered. <say where>" % t.value[0]
	sys.exit(1)
	# t.lexer.skip(1) #skip the offending character
	
	
def doScan(source):
	mLexer = lex.lex() #Build the lexer from the above specification
	mLexer.input(source)
	# mLexer.input("/* /*nested*/ %single line\ncomment */")
	return mLexer