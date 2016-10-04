package va1;

import java.io.IOException;
import java.io.InputStream;

import exception.CompilerException;


public class Parser {
	private Lexer lexer;
	private Token currentToken;

	public Parser(InputStream input) {
		lexer = new Lexer(input);
	}

	
	public String parse(InputStream input) throws CompilerException, IOException {

		// reinicia o lexer e lê o primeiro token
		lexer = new Lexer(input);
		currentToken = lexer.nextToken();

		// tenta reconhecer algo que case com o símbolo "program", símbolo inicial da gramática
		parsePrograma();
		acceptToken(TokenType.EOF);

		// se nao der exceção antes de chegar aqui, então o programa
		// está sintaticamente correto
		return "Sintaxe OK";
	}

	//////////// METODOS PARA MANIPULAR OS TOKENS /////////////

	
	private void acceptToken() throws CompilerException, IOException {
		currentToken = lexer.nextToken();		
	}

	/**
	 * Verifica se o token é do tipo esperado. Se for, aceita (avança para o próximo). 
	 * Se não for, lança exceção.
	 * @throws IOException 
	 */
	private void acceptToken(TokenType tp) throws CompilerException, IOException {
		if (currentToken.getType() == tp) {
			currentToken = lexer.nextToken();

		} else {
			throw new CompilerException("Token inesperado: "
					+ "foi lido um \"" + currentToken.getType() 
					+ "\", quando o esperado era \"" + tp + "\".");
		}

	}

	// METODOS PARA OS NÃO-TERMINAIS 

	/**
	 * programa = decl_global*
	 *				
	 * 
	 */
	private void parsePrograma() throws CompilerException, IOException {
		if (currentToken.getType() ==  TokenType.PROC||currentToken.getType() == TokenType.INT || currentToken.getType() == TokenType.CHAR || currentToken.getType() == TokenType.FLOAT ||  currentToken.getType() == TokenType.VOID ) {
			parseDecl_global();
			parsePrograma();
		} else {	
			//Não faz nada 
		}
	}
	/**
	 * 
	 * decl_global = decl_variavel
	 *				| decl_funcao
	 * 
	 */
	private void parseDecl_global() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.INT || currentToken.getType() == TokenType.CHAR || currentToken.getType() == TokenType.FLOAT ||  currentToken.getType() == TokenType.VOID ) {
			acceptToken();
			if (currentToken.getType() == TokenType.IDENTIFICADOR) { 
				acceptToken();
				 if(currentToken.getType() == TokenType.VIRGULA) {
					 do {
						 acceptToken();
						 acceptToken(TokenType.IDENTIFICADOR);	
					 } while (currentToken.getType() == TokenType.VIRGULA);
					 acceptToken(TokenType.PT_VIRG);
				 } else if (currentToken.getType() == TokenType.ABRE_PAR) {
					 acceptToken(TokenType.ABRE_PAR);
					 parseParam_formais();
					 acceptToken(TokenType.FECHA_PAR);
					 parseBloco();
				 }
			} 
			parseDecl_funcao();
			parseDecl_variavel();
			
		} else {
			throw new CompilerException("Invalid expression: " + currentToken);
			//Não aceita Vazio!!
		}
	}	
	/**
	 * 
	 * decl_variavel = "var" Lista_idents "-" tipo  ";"
	 * 
	 */
	private void parseDecl_variavel() throws CompilerException, IOException {
		acceptToken(TokenType.VAR);
		parseLista_idents();
		acceptToken(TokenType.MENOS);
		parseTipo();
		acceptToken(TokenType.PT_VIRG);
		
	}	
	/**
	 * 
	 *Lista_idents = IDENTIFICADOR ("," IDENTIFICADOR )*
	 *				
	 * 
	 */
	private void parseLista_idents() throws CompilerException, IOException {

		acceptToken(TokenType.IDENTIFICADOR);
		while (currentToken.getType() == TokenType.VIRGULA) {
			acceptToken();
			acceptToken(TokenType.IDENTIFICADOR);}
	}
	/**
	 * 
	 * tipo = "int" | "char" | "float" 
	 * 
	 */
	private void parseTipo() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.INT) {
			acceptToken(TokenType.INT);
		} else if(currentToken.getType() == TokenType.CHAR){
			acceptToken(TokenType.CHAR);
		}else if(currentToken.getType() == TokenType.FLOAT){
			acceptToken(TokenType.FLOAT);
		} else {
			throw new CompilerException("Invalid expression: " + currentToken);
		}
	}	
	/**
	 * 
	 * decl_funcao =  = "proc" nome_args "-" tipo bloco
 					  | "proc" nome_args bloco 
	 */
	private void parseDecl_funcao() throws CompilerException, IOException {
		acceptToken(TokenType.PROC);
		parseNome_args();
		if (currentToken.getType() == TokenType.MENOS){	
			acceptToken(TokenType.MENOS);
			parseTipo();
			parseBloco();
		}else{
				parseBloco();
			}
	}

	
	/**
	 * 
	 * nome_args = (IDENTIFICADOR "(" param_formais ")" )+
	 *					
	 * 
	 */
	private void parseNome_args() throws CompilerException, IOException {
			acceptToken(TokenType.IDENTIFICADOR);
			acceptToken(TokenType.ABRE_PAR);
			parseParam_formais();
			acceptToken(TokenType.FECHA_PAR);
		}
		
	

	/**
	 * param_formais = IDENTIFICADOR "-" tipo "(""," IDENTIFICADOR "-" tipo")""*"
	 *						| Vazio
	 * 
	 * 
	 */

	private void parseParam_formais() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.IDENTIFICADOR) {
			acceptToken(TokenType.IDENTIFICADOR);
			acceptToken(TokenType.MENOS);
			parseTipo();
			while (currentToken.getType() == TokenType.VIRGULA) {
				acceptToken(TokenType.VIRGULA);
				acceptToken(TokenType.IDENTIFICADOR);
				acceptToken(TokenType.MENOS);
				parseTipo(); }

		}else{
			//Aceita vazio!!!
		}
	}
	/**
	 * 
	 * bloco = "{" lista_comandos "}"
	 * 
	 */
	private void parseBloco() throws CompilerException, IOException {
		//if (currentToken.getType() == TokenType.ABRE_CHAVES){
		acceptToken(TokenType.ABRE_CHAVES);
		parseLista_comandos();
		acceptToken(TokenType.FECHA_CHAVES);

	} 	
	/**
	 * 
	 * lista_comandos = (comando)* 							  
	 */
	private void parseLista_comandos() throws CompilerException, IOException {

		if ((currentToken.getType() == TokenType.IDENTIFICADOR)||
				(currentToken.getType() == TokenType.WHILE) ||
				(currentToken.getType() == TokenType.IF) ||
				(currentToken.getType() == TokenType.PRNT) ||
				(currentToken.getType() == TokenType.RETURN) ||
				(currentToken.getType() == TokenType.ABRE_CHAVES)){
			parseComando();
			parseLista_comandos();
		}else{
			throw new CompilerException("Invalid expression: " + currentToken);
		}
	}

	/**
	 * 
	 * comando = 	decl_variavel
	 * 				|atribuicao
	 *				| iteracao
	 *				| decisao
	 *				| escrita
	 *				| retorno
	 *				| bloco
	 *				| chamada_func_cmd
	 * 
	 */
	private void parseComando() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.INT || currentToken.getType() == TokenType.CHAR || currentToken.getType() == TokenType.FLOAT){ 			
			parseDecl_variavel();
		}else if (currentToken.getType() == TokenType.WHILE) {
			parseIteracao();	
		}else if (currentToken.getType() == TokenType.IF) {
			parseDecisao();
		}else if (currentToken.getType() == TokenType.PRNT) {
			parseEscrita();
		}else if (currentToken.getType() == TokenType.RETURN) {
			parseRetorno();
		}else if (currentToken.getType() == TokenType.ABRE_CHAVES) {
			parseBloco();
		}else if (currentToken.getType() == TokenType.IDENTIFICADOR) {
			parseChamada_func_cmd_ou_atribuicao();	
		}
		else{
			throw new CompilerException("Invalid expression: " + currentToken);

		}
	}	
	/**
	 * 
	 * atribuicao = IDENTIFICADOR ":=" expressao ";" 

	 * 
	 */
	private void parseAtribuicao()throws CompilerException, IOException {
			acceptToken(TokenType.IDENTIFICADOR);
			acceptToken(TokenType.ATRIBUICAO);
			parseExpressao();
			acceptToken(TokenType.PT_VIRG);
		}
	
	/**
	 * 
	 * iteracao = "while" "(" expressao ")" comando
	 * 
	 */
	private void parseIteracao() throws CompilerException, IOException {
		acceptToken(TokenType.WHILE);
		acceptToken(TokenType.ABRE_PAR);
		parseExpressao();
		acceptToken(TokenType.FECHA_PAR);
		parseComando();
	}

	/**
	 * 
	 *  decisão = "if" expressao "then" comando "else" comando
	 *           |"if" expressao "then" comando 
	 *				
	 * 
	 */
	private void parseDecisao() throws CompilerException, IOException {
		acceptToken(TokenType.IF);
		parseExpressao();
		acceptToken(TokenType.THEN);
		parseComando();
		if (currentToken.getType() == TokenType.ELSE){	
			acceptToken(TokenType.ELSE);
			parseComando();	
		}
	}
	/**
	 * 
	 * escrita = "print" "(" expressao ")" ";"
	 * 
	 */
	private void parseEscrita() throws CompilerException, IOException {
		acceptToken(TokenType.PRNT);
		acceptToken(TokenType.ABRE_PAR);
		parseExpressao();
		acceptToken(TokenType.FECHA_PAR);
		acceptToken(TokenType.PT_VIRG);
	}

	/**
	 * Chamada_func_cmd_ou_atribuicao = chamada_func ";"
	 * 
	 */
	private void parseChamada_func_cmd_ou_atribuicao() throws CompilerException, IOException {
			parseChamada_func();
			acceptToken(TokenType.PT_VIRG);
	}

	/**
	 * 
	 * retorno = "return" expressao ";"
	 * 
	 */
	private void parseRetorno() throws CompilerException, IOException {
		acceptToken(TokenType.RETURN);
		parseExpressao();
		acceptToken(TokenType.PT_VIRG);
	}
	/**
	 * 
	 * chamada_func = (IDENTIFICADOR "(" lista_exprs ")")+
	 * 
	 */
	private void parseChamada_func() throws CompilerException, IOException {
			while (currentToken.getType() == TokenType.IDENTIFICADOR){
		acceptToken(TokenType.ABRE_PAR);
		parseLista_exprs();
		acceptToken(TokenType.FECHA_PAR); 
			}
	}
	/**
	 * 
	 * lista_exprs = vazio 
	 *				| expressao (","expressao)*
	 * 
	 */
	private void parseLista_exprs() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.IDENTIFICADOR||currentToken.getType() == TokenType.FLOAT_LITERAL ||currentToken.getType() == TokenType.CARACTER_LITERAL ||currentToken.getType() == TokenType.INTEIRO_LITERAL){
			parseExpressao();
			while (currentToken.getType() == TokenType.VIRGULA){
				acceptToken(TokenType.VIRGULA);
				parseExpressao();			
			}
		}else{
			// Aceita vazio!!!
		}
	}

	/**
	 * 
	 * expressao = expressaoA
	 * 
	 */
	private void parseExpressao() throws CompilerException, IOException {
		parseExpressaoA();
	}

	/**
	 * expressaoA = expressaoB restoExpressaoA
	 *				
	 * 
	 */
	private void parseExpressaoA() throws CompilerException, IOException {
		parseExpressaoB();
		parseRestoExpressaoA();
	}

	/**
	 * restoExpressaoA = "or" expressaoA
	 *			| "and" expressaoA
	 *									
	 *				
	 * 
	 */
	private void parseRestoExpressaoA() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.OR) {
			acceptToken(TokenType.OR);
			parseExpressaoA();

		}else if (currentToken.getType() == TokenType.AND) {
			acceptToken(TokenType.AND);
			parseExpressaoA();
		}
		
	}

	/**
	 * 
	 * expressaoB = expressaoC restoExpressaoB
	 *				
	 * 
	 */
	private void parseExpressaoB() throws CompilerException, IOException {
		parseExpressaoC();
		parseRestoExpressaoB();
	}

	/**
	 * restoExpressaoB = "==" expressaoB
	 *			| "<>"<expressaoB>
	 *			| "<"<expressaoB>
	 *			| ">"<expressaoB>
	 *			| "<="<expressaoB>
	 *			| ">=" <expressaoB>
	 *			| Vazio
	 *								
	 *				
	 * 
	 */
	private void parseRestoExpressaoB() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.IGUAL_COMPARAR) {
			acceptToken(TokenType.IGUAL_COMPARAR);
			parseExpressaoB();	
		}else if (currentToken.getType() == TokenType.DIFERENTE) {
			acceptToken(TokenType.DIFERENTE);
			parseExpressaoB();
		}else if (currentToken.getType() == TokenType.MENOR_QUE) {
			acceptToken(TokenType.MENOR_QUE);
			parseExpressaoB();
		}else if (currentToken.getType() == TokenType.MAIOR_QUE) {
			acceptToken(TokenType.MAIOR_QUE);
			parseExpressaoB();
		}else if (currentToken.getType() == TokenType.MENOR_OU_IGUAL) {
			acceptToken(TokenType.MENOR_OU_IGUAL);
			parseExpressaoB();
		}else if (currentToken.getType() == TokenType.MAIOR_OU_IGUAL) {
			acceptToken(TokenType.MAIOR_OU_IGUAL);
			parseExpressaoB();
		}
		else{
			//Aceita vazio!!!
		}
	}

	/**
	 * 
	 * expressaoC = expressaoD restoExpressaoC
	 * 
	 */
	private void parseExpressaoC() throws CompilerException, IOException {
		parseExpressaoD();
		parseRestoExpressaoC();
	}

	/**
	 * restoExpressaoC = "+" expressaoC
	 *			| "-" expressaoC
	 *			| Vazio				
	 *				
	 * 
	 */
	private void parseRestoExpressaoC() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.MAIS) {
			acceptToken(TokenType.MAIS);
			parseExpressaoC();

		}else if (currentToken.getType() == TokenType.MENOS) {
			acceptToken(TokenType.MENOS);
			parseExpressaoC();
		}
		else{
			//Aceita vazio!!!
		}
	}

	/**
	 * expressaoD = expressaoE restoExpressaoD	
	 * 
	 * 
	 */
	private void parseExpressaoD() throws CompilerException, IOException {
		parseExpressaoE();
		parseRestoExpressaoD();
	}

	/**
	 * restoExpressaoD = "*" expressaoD
	 *						| "/" expressaoD
	 *						|"%" expressaoD
	 *						| Vazio					
	 *				
	 * 
	 */
	private void parseRestoExpressaoD() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.VEZES) {
			acceptToken(TokenType.VEZES);
			parseExpressaoD();

		}else if (currentToken.getType() == TokenType.DIVIDE) {
			acceptToken(TokenType.DIVIDE);
			parseExpressaoD();
		}else if (currentToken.getType() == TokenType.RESTO_DIVISAO) {
			acceptToken(TokenType.RESTO_DIVISAO);
			parseExpressaoD();
		}
		else{
			//Aceita vazio!!!
		}
	}
	/**
	 * 
	 * expressaoE = expr_basica
	 * 
	 */
	private void parseExpressaoE() throws CompilerException, IOException {
		parseExpr_basica();
	}

	/**
	 * 
	 * expr_basica = expr_basicaA
	 * 
	 * 
	 */
	private void parseExpr_basica() throws CompilerException, IOException {
		parseExpr_basicaA();
	}

	/**
	 * 
	 * expr_basicaA  = "not" expr_basicaA
	 *					| "-" expr_basicaA
	 *					| expr_basicaB
	 * 
	 * 
	 */

	private void parseExpr_basicaA() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.NOT) {
			acceptToken(TokenType.NOT);
			parseExpr_basicaA();
		}else if (currentToken.getType() == TokenType.MENOS) {
			acceptToken(TokenType.MENOS);
			parseExpr_basicaA();
		}else if (currentToken.getType() == TokenType.RESTO_DIVISAO) {
			acceptToken(TokenType.RESTO_DIVISAO);
			parseExpressaoD();
		}else if ((currentToken.getType() == TokenType.IDENTIFICADOR)||
				(currentToken.getType() == TokenType.FLOAT_LITERAL) ||
				(currentToken.getType() == TokenType.CARACTER_LITERAL) ||
				(currentToken.getType() == TokenType.INTEIRO_LITERAL) ||
				(currentToken.getType() == TokenType.ABRE_PAR)){
			parseExpr_basicaB();
		}
		else{
			throw new CompilerException("Invalid expression: " + currentToken);
		}
	}

	/**
	 * 
	 * expr_basicaB =   "(" expressao ")"
	 * 					 | "not" expr_basica
						 | "-" expr_basica 
	 *					 |INT_LITERAL
	 *					 | CARACTER_LITERAL
	 *					 | FLOAT_LITERAL
	 *					 | STRING_LITERAL
	 *					 | IDENTIFICADOR
	 *					 | chamada_func
	 * 
	 * 
	 */
	private void parseExpr_basicaB() throws CompilerException, IOException {
		if (currentToken.getType() == TokenType.ABRE_PAR) {
			acceptToken(TokenType.ABRE_PAR);
			parseExpressao();
			acceptToken(TokenType.FECHA_PAR);
		}else if(currentToken.getType() == TokenType.NOT){
			
		}else if (currentToken.getType() == TokenType.INTEIRO_LITERAL) {
			acceptToken(TokenType.INTEIRO_LITERAL);
		}else if (currentToken.getType() == TokenType.CARACTER_LITERAL) {
			acceptToken(TokenType.CARACTER_LITERAL);
		}else if (currentToken.getType() == TokenType.FLOAT_LITERAL) {
			acceptToken(TokenType.FLOAT_LITERAL);
		}else if (currentToken.getType() == TokenType.IDENTIFICADOR) {
			parseChamada_func();
		}else{
			throw new CompilerException("Invalid expression: " + currentToken);
		}
	}
}

// FIM 


