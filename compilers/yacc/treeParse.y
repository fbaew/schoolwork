%{

#include <stdio.h>
#include <stdlib.h>
#include "TreeNode.h"


treeNode * numNode(int val);
treeNode * binNode(NodeKind type, treeNode * e1, treeNode * e2);


void printTree(treeNode * t);

int yylex(void);

void yyerror(char *s);
%}

%union {
  int iVal;
  struct treeNode * tn;
};

%start expr

%token PLUS MINUS TIMES DIV LPAREN RPAREN

%token <iVal> NUMBER
%type <tn> expr term factor

%%

expr : expr PLUS  term { $$ = binNode(Add, $1, $3); }
     | expr MINUS term { $$ = binNode(Sub, $1, $3); }
     | term            { $$ = $1; }
     ;

term : term TIMES factor { $$ = binNode(Mul, $1, $3); }
     | term DIV   factor { $$ = binNode(Div, $1, $3); }
     | factor            { $$ = $1; }
     ;

factor : NUMBER      { $$ = numNode($1); }
       | LPAREN expr RPAREN { $$ = $2; }
       ;

%%

  int yywrap()
  {
    return 1;
  } 

  int main(void)
  {
    treeNode * t = (treeNode*)yyparse();
    fprintf(stdout, "completed parse\n");
    //printTree(t);
    return 0;
  } 


  treeNode * numNode(int val){
    treeNode *t = malloc(sizeof(treeNode));
    t->nodekind = Num;
    t->val = val;
    return t;
  }

  treeNode * binNode(NodeKind type, treeNode * e1, treeNode * e2){
    treeNode *t = malloc(sizeof(treeNode));
    t->nodekind = type;
    t->child[0] = e1;
    t->child[1] = e2;
    return t;
  }


void yyerror(char *s) {
      fprintf(stdout, "%s\n", s);
}


void printTree(treeNode * t){
  switch( t->nodekind ) {
    case Add:
      fprintf(stdout, "Add(");
      printTree(t->child[0]);
      fprintf(stdout,",");
      printTree(t->child[1]);
      fprintf(stdout,")");
      break;
    case Sub:
      fprintf(stdout, "Sub(");
      printTree(t->child[0]);
      fprintf(stdout,",");
      printTree(t->child[1]);
      fprintf(stdout,")");
      break;
    case Div:
      fprintf(stdout, "Div(");
      printTree(t->child[0]);
      fprintf(stdout,",");
      printTree(t->child[1]);
      fprintf(stdout,")");
      break;
    case Mul:
      fprintf(stdout, "Mul(");
      printTree(t->child[0]);
      fprintf(stdout,",");
      printTree(t->child[1]);
      fprintf(stdout,")");
      break;
    case Num:
      fprintf(stdout, "Num(%d)", t->val);
      break;
  }
}
