package va1;

import va1.TokenType;
import va1.Lexer;

public class Token {
	private TokenType tipo;
	private String lexema;
	private int yyline;
	private int yycolumn;

	public Token(TokenType tipo, int yyline, int yycolumn) {
		this.tipo = tipo;
		this.yyline= yyline;
		this.yycolumn= yycolumn;
	}
	
	public Token(TokenType tipo, String lexema) {
		this.tipo = tipo;
		this.lexema = lexema;	
	}
	
	public Token(TokenType identificador, String yytext, int yyline2,
			int yycolumn2) {
		this.tipo = identificador;
		this.yyline= yyline;
		this.yycolumn= yycolumn;
		this.lexema = yytext;
	}

	public Token(TokenType eof) {
		// TODO Auto-generated constructor stub
	}
	
	public TokenType getType() {
		return tipo;
	}
	
	public String getLexeme() {
		return lexema;
	}
	
	public String toString() {
		if (lexema == null || lexema.length() == 0) {
			return "[" + tipo + "]";	
		} else {
			return "[" + tipo + "," + lexema + "]";
		}
	}

}

