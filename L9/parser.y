%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
extern int yylex();
extern int yylineno;
extern char* yytext;
void yyerror(const char *s);
%}

%token CARDS_ARE
%token YO
%token ITS_DONE
%token INT
%token BOOL
%token FLOAT
%token CHAR
%token STRING
%token CONST
%token IF
%token ELSE
%token WHILE
%token FOR
%token UNO
%token READ_RULES_FOR
%token HAS_RULE
%token PACK
%token LPAREN
%token RPAREN
%token COLON
%token SEMICOLON
%token EQ
%token LEQ
%token LT
%token GEQ
%token GT
%token NEQ
%token AND
%token OR
%token PLUS
%token MINUS
%token MULTIPLY
%token DIVIDE
%token MODULO
%token ASSIGN
%token IDENTIFIER
%token NUMBER_CONST
%token STRING_CONST
%token CHAR_CONST

%left '+' '-'
%left '*' '/'
%left UNARYMINUS

%%

program : CARDS_ARE decllist compound_stmt

decllist : declaration | declaration decllist

declaration : type IDENTIFIER

type : type1 | array_decl

type1 : BOOL | CHAR | STRING | INT | FLOAT

array_decl : type1 PACK LPAREN NUMBER_CONST RPAREN

compound_stmt : YO stmt_list ITS_DONE

stmt_list : stmt | stmt stmt_list

stmt : assign_stmt | iostmt | compound_stmt | if_stmt | while_stmt | for_stmt

assign_stmt : IDENTIFIER HAS_RULE expression

iostmt : READ_RULES_FOR | UNO LPAREN IDENTIFIER RPAREN

if_stmt : IF LPAREN logical_condition RPAREN stmt [ELSE stmt]

while_stmt : WHILE LPAREN logical_condition RPAREN stmt

for_stmt : FOR for_header compound_stmt

for_header : LPAREN INT assign_stmt SEMICOLON condition SEMICOLON assign_stmt RPAREN

expression : expression PLUS term
           | expression MINUS term
           | term

term : term MULTIPLY factor
     | term DIVIDE factor
     | term MODULO factor
     | factor

factor : LPAREN expression RPAREN
       | IDENTIFIER

logical_condition : condition [AND | OR] condition
                 | condition

condition : expression EQ expression
          | expression LEQ expression
          | expression LT expression
          | expression GEQ expression
          | expression GT expression
          | expression NEQ expression

%%

void yyerror(const char *s) {
    fprintf(stderr, "Parse error at line %d: %s\n", yylineno, s);
}

int main() {
    yyparse();
    return 0;
}
