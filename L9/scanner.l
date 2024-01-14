
%{ 	 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "y.tab.h"
int currentLine = 1;
%} 

%option noyywrap

IDENTIFIER		[a-zA-Z_][a-zA-Z0-9_]*
NUMBER_CONST	0|[+|-]?[1-9][0-9]*([.][0-9]*)?|[+|-]?0[.][0-9]*
STRING_CONST	[\"][a-zA-Z0-9 ]+[\"]
CHAR_CONST		[\'][a-zA-Z0-9 ][\']

%%
"cardsAre"       { printf("CARDS_ARE\n"); return CARDS_ARE; }
"yo"             { printf("YO\n"); return YO; }
"itsDone"        { printf("ITS_DONE\n"); return ITS_DONE; }
"int"            { printf("INT\n"); return INT; }
"bool"           { printf("BOOL\n"); return BOOL; }
"float"          { printf("FLOAT\n"); return FLOAT; }
"char"           { printf("CHAR\n"); return CHAR; }
"string"         { printf("STRING\n"); return STRING; }
"const"          { printf("CONST\n"); return CONST; }
"if"             { printf("IF\n"); return IF; }
"else"           { printf("ELSE\n"); return ELSE; }
"while"          { printf("WHILE\n"); return WHILE; }
"for"            { printf("FOR\n"); return FOR; }
"uno"            { printf("UNO\n"); return UNO; }
"readRulesFor"   { printf("READ_RULES_FOR\n"); return READ_RULES_FOR; }
"hasRule"        { printf("HAS_RULE\n"); return HAS_RULE; }
"pack"           { printf("PACK\n"); return PACK; }

"("             { printf("LPAREN\n"); return LPAREN; }
")"             { printf("RPAREN\n"); return RPAREN; }
":"             { printf("COLON\n"); return COLON; }
";"             { printf("SEMICOLON\n"); return SEMICOLON; }
"=="            { printf("EQ\n"); return EQ; }
"<="            { printf("LEQ\n"); return LEQ; }
"<"             { printf("LT\n"); return LT; }
">="            { printf("GEQ\n"); return GEQ; }
">"             { printf("GT\n"); return GT; }
"!="            { printf("NEQ\n"); return NEQ; }
"&&"            { printf("AND\n"); return AND; }
"||"            { printf("OR\n"); return OR; }
"+"             { printf("PLUS\n"); return PLUS; }
"-"             { printf("MINUS\n"); return MINUS; }
"*"             { printf("MULTIPLY\n"); return MULTIPLY; }
"/"             { printf("DIVIDE\n"); return DIVIDE; }
"%"             { printf("MODULO\n"); return MODULO; }
"="             { printf("ASSIGN\n"); return ASSIGN; }

{IDENTIFIER}		{printf("Identifier: %s\n", yytext); return IDENTIFIER;}
{NUMBER_CONST}		{printf("Number: %s\n", yytext); return NUMBER_CONST;}
{STRING_CONST}		{printf("String: %s\n", yytext); return STRING_CONST;}
{CHAR_CONST}		{printf("Character: %s\n", yytext); return CHAR_CONST;}

[ \t]+		{}
[\n]+	{currentLine++;}

[0-9][a-zA-Z0-9_]*		{printf("Illegal identifier at line %d\n", currentLine);}
[+|-]0		{printf("Illegal numeric constant at line %d\n", currentLine);}
[+|-]?[0][0-9]*([.][0-9]*)?		{printf("Illegal numeric constant at line %d\n", currentLine);}
[\'][a-zA-Z0-9 ]{2,}[\']|[\'][a-zA-Z0-9 ][a-zA-Z0-9 ][\']		{printf("Illegal character constant at line %d\n", currentLine);}
[\"][a-zA-Z0-9_]+|[a-zA-Z0-9_]+[\"]		{printf("Illegal string constant at line %d\n", currentLine);}

%%