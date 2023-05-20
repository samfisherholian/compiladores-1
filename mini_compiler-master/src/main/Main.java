package main;

import lexical.Scanner;
import lexical.Token;
import syntax.*;
import exceptions.LexicalException;
import exceptions.SyntaxException;

public class Main {
	
	public static void main(String[] args) {


 /* 
		try {
			Scanner scanner = new Scanner("codigo_fonte_do_AS_feito_em_sala.mc");
			Parser parser = new Parser(scanner);
			parser.E();
			System.out.println("Compilation successful!");
		} catch (LexicalException e) {
			System.out.println("Lexical error: " + e.getMessage());
		} catch (SyntaxException e) {
			System.out.println("Syntax error: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}	
*/

 

		Scanner scanner = new Scanner("source_code.mc");
		Token token = null;	
		do {
			token = scanner.nextToken();
			System.out.println(token);
		} while (token != null);
		
	}

}
