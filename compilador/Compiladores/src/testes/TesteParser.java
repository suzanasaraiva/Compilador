package testes;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import va1.Lexer;
import va1.Parser;


public class TesteParser {

	public static void main(String args[]) throws IOException {
		Lexer lexer;
		Parser parser;
		
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String arquivo = "C:/Users/Suzana/Desktop/teste.txt";
        System.out.println();
		
		try {
			FileInputStream input = new FileInputStream(arquivo);
			parser = new Parser(input);
			parser.parse(input);

			System.out.println("\nSintaxe OK!");
		
		} catch (Exception e) {
			System.out.println("\nErro !");
			e.printStackTrace();
		}
	}

}


