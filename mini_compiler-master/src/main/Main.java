package main;

import lexical.Scanner;
import lexical.Token;
import syntax.*;
import exceptions.LexicalException;
import exceptions.SyntaxException;

public class Main {

	public static void main(String[] args) {

		try {
			Scanner scanner = new Scanner("codigo_fonte_que_deve_rodar_apos_a_implementacao.mc");
			Parser parser = new Parser(scanner);
			parser.program();
			System.out.println("Compilation successful!");
		} catch (LexicalException e) {
			System.out.println("Lexical error: " + e.getMessage());
		} catch (SyntaxException e) {
			System.out.println("Syntax error: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
