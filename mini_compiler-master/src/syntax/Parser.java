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

//	public void E() {
		//T();
		//El();
		//program();
	//}

	//programa
	public void program(){
		//this.token = this.scanner.nextToken();
		this.nextoken();
		if(this.token.getType() != TokenType.TWOPOINTS ){
			throw new SyntaxException("Expected ':', found " + token.getType());
		}
		//this.token = this.scanner.nextToken();
		this.nextoken();
		if(!this.token.getContent().equals("DECLARACOES")){
			throw new SyntaxException("Expected 'DECLARACOES', found " + token.getType());
		}
		listaDeclaracoes();
		//this.nextoken();


		//colocar esse if aqui enquanto n implemento algoritmo
		if(this.token != null){

			if(this.token.getType() != TokenType.TWOPOINTS ){
				throw new SyntaxException("Expected ':', found " + token.getType());
			}
		}			
		//quando sai de listaDeclaracoes o token sai como null entao tem que verificar aqui
		if(this.token != null){

			//this.token = this.scanner.nextToken();
			this.nextoken();
			if(!this.token.getContent().equals("ALGORITMO")){
				throw new SyntaxException("Expected 'ALGORITMO', found " + token.getType());
			}

			
		}	
	}

	public void listaDeclaracoes(){
//		this.token = this.scanner.nextToken();
		this.nextoken();		
		if(this.token != null){		
			declaracao();
			listaDeclaracoes();
		}

	}

	public void declaracao(){
		tipoVar();
		//this.token = this.scanner.nextToken();
		this.nextoken();
		//reconhce os dois pontos
		if(this.token.getType() != TokenType.TWOPOINTS ){
			throw new SyntaxException("Expected ':', found " + token.getType());
		}

		//this.token = this.scanner.nextToken();
		this.nextoken();
		if(this.token.getType() != TokenType.IDENTYFIER){
			throw new SyntaxException("Identyfier or Number expected, found " + token.getType());
		}
		this.nextoken();
		this.ponVirgula();


	}

	public void tipoVar(){
		//this.token = this.scanner.nextToken();
		if(!this.token.getContent().equals("INTEIRO") && !this.token.getContent().equals("REAL")){
			throw new SyntaxException("Expected 'INTEIRO' OR 'REAL' but, found " + token.getContent());
		}

	}

	public void nextoken(){
		
		this.token = this.scanner.nextToken();
	
	}

	public void ponVirgula(){
		if(this.token.getType() != TokenType.PONTOVIRGULA){
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
