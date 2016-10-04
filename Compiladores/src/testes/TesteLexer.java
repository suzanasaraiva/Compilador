package testes;

import java.io.FileReader;
import va1.Lexer;
import va1.Token;
import va1.TokenType;

/**
 * Classe de testes do lexer. 
 */
public class TesteLexer {

	public static void main(String[] args) throws Exception {
		Lexer lexer = null;
		Token token = null;
		
		System.out.println("\n\n\n");
		System.out.println(" == TESTE DO LEXER ==\n");
		System.out.println(" Digite alguma coisa e tecle ENTER:\n\n");
		System.out.print(" ");


		lexer = new Lexer(System.in);
		 
		

		do {
			token = lexer.nextToken();
			System.out.println("\t" + token.toString());
		
		} while (token.getType() != TokenType.EOF);
		
		System.out.println("\n == FIM ==");
		
	}

}