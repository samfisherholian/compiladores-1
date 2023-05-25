package syntax;

import exceptions.SyntaxException;
import lexical.Scanner;
import lexical.Token;
import utils.TokenType;

public class Parser {
	private Scanner scanner;
	private Token token;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}

	// programa
	public void program() {
		
		this.nextoken();
		if (this.token.getType() != TokenType.TWOPOINTS) {
			throw new SyntaxException("Expected ':', found " + token.getType());
		}
		
		this.nextoken();
		if (!this.token.getContent().equals("DECLARACOES")) {
			throw new SyntaxException("Expected 'DECLARACOES', found " + token.getType());
		}
		listaDeclaracoes();

		// colocar esse if aqui enquanto n implemento algoritmo
		if (this.token != null) {
			if (this.token.getType() != TokenType.TWOPOINTS) {
				throw new SyntaxException("Expected ':', found " + token.getType());
			}
			this.nextoken();
		}

		// quando sai de listaDeclaracoes o token sai como null entao tem que verificar
		// aqui
		if (this.token != null) {
			if (!this.token.getContent().equals("ALGORITMO")) {
				throw new SyntaxException("Expected 'ALGORITMO', found " + token.getType());
			}
		}
		listaComandos();
		
	}

	public void listaDeclaracoes() {
		this.nextoken();
		if (this.token.getType() != TokenType.TWOPOINTS) {
			declaracao();
			listaDeclaracoes();
		}
	}

	/*
	 *	public void listaDeclaracoes(){
	 *		this.token = this.scanner.nextToken();
	 *		this.nextoken();		
	 *		if(this.token != null){		
	 *			declaracao();
	 *			listaDeclaracoes();
	 *		}
	 *	}
	 */

	public void declaracao() {
		tipoVar();
		
		this.nextoken();
		// reconhce os dois pontos
		if (this.token.getType() != TokenType.TWOPOINTS) {
			throw new SyntaxException("Expected ':', found " + token.getType());
		}

		this.nextoken();
		if (this.token.getType() != TokenType.IDENTYFIER) {
			throw new SyntaxException("Identyfier or Number expected, found " + token.getType());
		}
		this.nextoken();
		this.ponVirgula();

	}

	public void tipoVar() {
			if (!this.token.getContent().equals("INTEIRO") && !this.token.getContent().equals("REAL")) {
				throw new SyntaxException("Expected 'INTEIRO' OR 'REAL' but, found " + token.getContent());
			}
		}

	public void listaComandos() {
		this.nextoken();
		if (this.token != null) {
			comandos();
			listaComandos();
		}
	}

	public void comandos() {
		if (this.token.getContent().equals("INPUT")){
			comandoEntrada();
		}
		else if (this.token.getContent().equals("PRINT")){
			comandoSaida();
		}
		else {
			//tipoComando();
			//!this.token.getContent().equals("ASSING")  && !this.token.getContent().equals("IF")
			//&& !this.token.getContent().equals("ELSE") && !this.token.getContent().equals("WHILE")
			throw new SyntaxException("Expected 'Command' but, found " + token.getContent());
		}
	}

	public void comandoEntrada(){
		this.nextoken();
		if (this.token.getType() != TokenType.IDENTYFIER) {
			throw new SyntaxException("Identyfier or Number expected, found " + token.getType());
		}

		this.nextoken();
		this.ponVirgula();
	}

	public void comandoSaida() {
		this.nextoken();
		if (this.token.getType() != TokenType.LEFTPAR) {
			throw new SyntaxException("Left Parenthesis expected, found " + token.getType());
		}

		this.nextoken();
		/*    NAO SEI SE "CADEIA" EH ASSIM MSM  */	
		if (this.token.getType() != TokenType.IDENTYFIER && this.token.getType() != TokenType.STRING) {
			throw new SyntaxException("Identyfier or Number expected, found " + token.getType());
		}

		this.nextoken();
		if (this.token.getType() != TokenType.RIGHTPAR) {
			throw new SyntaxException("Right Parenthesis expected, found " + token.getType());
		}

		this.nextoken();
		this.ponVirgula();
	}

	public void comandoCondicaoIF() {
		// implementar expressaoRelacional 
	}

	public void comandoCondicaoELSE() {
		// implementar
	}

	public void comandoRepeticao() {
		// implementar expressaoRelacional
	}

	public void nextoken() {
		this.token = this.scanner.nextToken();
	}

	public void ponVirgula() {
		if (this.token.getType() != TokenType.PONTOVIRGULA) {
			throw new SyntaxException("Expected ';' at end of ocurrency but, found " + token.getContent());
		}
	}
 /* 
	private void El() {
		this.token = this.scanner.nextToken();
		if (this.token != null) {
			OP();
			T();
			El();
		}
	}

	private void OP() {// tive que por mais uma concicao "ASSINGN" pra poder rodar
		if (this.token.getType() != TokenType.MATH_OP && this.token.getType() != TokenType.REL_OP && this.token.getType() != TokenType.ASSINGN) {
			throw new SyntaxException("Math or Relational operator expected, found " + token.getType());
		}
	}

	private void T() {
		this.token = this.scanner.nextToken();
		if (this.token.getType() != TokenType.IDENTYFIER && this.token.getType() != TokenType.NUMBER) {
			throw new SyntaxException("Identyfier or Number expected, found " + token.getType());
		}

	}
*/
}
