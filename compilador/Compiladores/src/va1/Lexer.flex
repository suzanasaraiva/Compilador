package va1;

%%

%class Lexer
%public

%function nextToken
%type Token
%line
%column

whiteSpace=([ \n\t\f\r]+)
letramin=[a-z]
letra=[a-zA-Z]
numero=[0-9]+

%%

{whiteSpace} { 
	// Caracteres ignorados.
	// Nenhum token é retornado.
}

">>" [^<] ~"<<" | ">>" "<"+ "<" {
	// Comentários de bloco.
	// Nenhum token é retornado.
}
"**"[^]*[\r|\n|\n|\r] {
	//Comentários de bloco.
	// Nenhum token é retornado.
}

";" { return new Token(TokenType.PT_VIRG,yyline, yycolumn); }
"+" { return new Token(TokenType.MAIS, yyline, yycolumn); }
"-" { return new Token(TokenType.MENOS, yyline, yycolumn); }
"*" { return new Token(TokenType.VEZES, yyline, yycolumn); }
"/" { return new Token(TokenType.DIVIDE, yyline, yycolumn); }
"(" { return new Token(TokenType.ABRE_PAR, yyline, yycolumn); }
")" { return new Token(TokenType.FECHA_PAR, yyline, yycolumn); }
"=" { return new Token(TokenType.IGUAL_ATRIBUIR, yyline, yycolumn); }
"," { return new Token(TokenType.VIRGULA, yyline, yycolumn); }
":" { return new Token(TokenType.DOIS_PONTOS, yyline, yycolumn); }
"{" { return new Token(TokenType.ABRE_CHAVES, yyline, yycolumn); }
"}" { return new Token(TokenType.FECHA_CHAVES, yyline, yycolumn); }
":=" {return new Token(TokenType.ATRIBUICAO, yyline, yycolumn); }
">"  { return new Token(TokenType.MAIOR_QUE,yyline, yycolumn); }
"<"  { return new Token(TokenType.MENOR_QUE,yyline, yycolumn); }
">=" { return new Token(TokenType.MAIOR_OU_IGUAL,yyline, yycolumn); }
"<=" { return new Token(TokenType.MENOR_OU_IGUAL,yyline, yycolumn); }
"<>" { return new Token(TokenType.DIFERENTE,yyline, yycolumn); }
"%"  { return new Token(TokenType.RESTO_DIVISAO,yyline, yycolumn); }

"if"  { return new Token(TokenType.IF,yyline, yycolumn); }
"then"  { return new Token(TokenType.THEN,yyline, yycolumn); }
"else" { return new Token(TokenType.ELSE,yyline, yycolumn); }
"while" { return new Token(TokenType.WHILE,yyline, yycolumn); }
"return" { return new Token(TokenType.RETURN,yyline, yycolumn); }
"float" { return new Token(TokenType.FLOAT,yyline, yycolumn); }
"char" { return new Token(TokenType.CHAR,yyline, yycolumn); }
"and" { return new Token(TokenType.AND,yyline, yycolumn); }
"or"  { return new Token(TokenType.OR,yyline, yycolumn); }
"not" { return new Token(TokenType.NOT,yyline, yycolumn); }
"prnt" { return new Token(TokenType.PRNT,yyline, yycolumn); }
"int" { return new Token(TokenType.INT,yyline, yycolumn); }
"void" { return new Token(TokenType.VOID,yyline, yycolumn); }
"proc" {return new Token(TokenType.PROC,yyline, yycolumn); }
"var" {return new Token(TokenType.VAR, yyline, yycolumn); }


{letramin}({letra}|{numero})*   { return new Token(TokenType.IDENTIFICADOR, yytext()); }

//Inteiro_Literal
{numero}+  { return new Token(TokenType.INTEIRO_LITERAL, yytext(),yyline, yycolumn); }

//Ponto_Flutuante
{numero}*.{numero}+ { return new Token(TokenType.FLOAT_LITERAL, yytext(),yyline, yycolumn); }

//Caracter_Literal
"'"({numero}|{letra}| \\n | \\t |" "|":"|"("|")"|",")"'" { return new Token(TokenType.CARACTER_LITERAL, yytext()); }

//Se não atender as expressoes acima
. {  System.out.println("Illegal character at line "+ yyline+" column "+yycolumn+": " + yytext());
}

//Fim do arquivo apenas
<<EOF>> {
	return new Token(TokenType.EOF);
}
