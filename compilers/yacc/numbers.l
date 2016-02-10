/* numbers.l
 * Recognizes integers, operators, and parenthesis
 * Author: Thomas Burt
 * Date: Mar 14th, 2011
 */

/* The first section is for includes and options */

%{

  #include <stdio.h>
  /* we are using yacc, so we need this for yyval and others to be in scope */
  #include "y.tab.h"

%}

%%

[0-9]+        { yylval.iVal = atoi(yytext); return NUMBER; }
"("           return LPAREN;
")"           return RPAREN;
"+"           return PLUS;
"-"           return MINUS;
"*"           return TIMES;
"/"           return DIV;

\n            ;
[ \t]+        ;

%%
