package syntax;

import java.security.cert.PolicyNode;

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
			throw new SyntaxException(this.erroSyntax() + "" +"Expected ':', found " + token.getType());
		}
		
		this.nextoken();
		if (!this.token.getContent().equals("DECLARACOES")) {
			throw new SyntaxException(this.erroSyntax() + "" +"Expected 'DECLARACOES', found " + token.getType());
		}
		listaDeclaracoes();

		// colocar esse if aqui enquanto n implemento algoritmo
		if (this.token != null) {
			if (this.token.getType() != TokenType.TWOPOINTS) {
				throw new SyntaxException(this.erroSyntax() + "" +"Expected ':', found " + token.getType());
			}
			this.nextoken();
		}

		// quando sai de listaDeclaracoes o token sai como null entao tem que verificar
		// aqui
		if (this.token != null) {
			if (!this.token.getContent().equals("ALGORITMO")) {
				throw new SyntaxException(this.erroSyntax() + "" +"Expected 'ALGORITMO', found " + token.getType());
			}
		}
		listaComandos();
		
	}

	public void listaDeclaracoes() {
		this.nextoken();       //vendo se ta pegando agr essa branch aqui
		if (this.token.getType() != TokenType.TWOPOINTS) {
			declaracao();
			listaDeclaracoes();
		}
	}


	public void declaracao() {

		this.varLista();
	
		if (this.token.getType() != TokenType.TWOPOINTS) {
			throw new SyntaxException(this.erroSyntax() + "" +"Expected ':', found " + token.getType());
		}

		this.nextoken();

		tipoVar();
	
		this.nextoken();
		this.ponVirgula();

	}

	public void varLista(){

		if (this.token.getType() != TokenType.IDENTYFIER) {
			throw new SyntaxException(this.erroSyntax() + "" +"Identyfier or Number expected, found " + token.getType());
		}
		this.nextoken();
		if(this.token.getType() == TokenType.VIRGULA){
			this.nextoken();
			varLista();
		}

	}

	public void tipoVar() {
			if (!this.token.getContent().equals("INTEIRO") && !this.token.getContent().equals("REAL")) {
				throw new SyntaxException(this.erroSyntax() + " " +"Expected 'INTEIRO' OR 'REAL' but, found " + token.getContent());
			}
		}

	public void listaComandos() {
		if(!this.token.getContent().equals("IF") && !this.token.getContent().equals("ASSIGN") && !this.token.getContent().equals("INPUT") && !this.token.getContent().equals("PRINT") && !this.token.getContent().equals("ELSE")){
			this.nextoken();
		}
		if (this.token != null) {
			comandos();
			if(this.token != null){
				listaComandos();
			}
			
		}
	}

	public void comandos() {

		if(this.token != null){

			if(this.myEqualswithnull("WHILE")){
				this.comandoRepeticao();
			}
			if(this.myEqualswithnull("IF")){
				comandoCondicaoIF();
			
			}
			if(this.myEqualswithnull("ASSIGN")){
				comandoAtribuicao();
			}
			if (this.myEqualswithnull("INPUT")){
				comandoEntrada();
			}
			if (this.myEqualswithnull("PRINT")){
				comandoSaida();
			}
			if(this.myEqualswithnull("ELSE")){
				this.comandoCondicao2();
			}
		}	
		
	}
	
	/* ESSA FUNCAO FOI BASEADA NESTA AQUI DO STACKOVERFLOW: 
	https://stackoverflow.com/questions/11011742/java-null-equalsobject-o */
	public boolean myEqualswithnull(String c){
		if(this.token != null){
			//compara a string
			if(this.token.getContent().equals(c)){
				return true;
			}else{
				return false;
			}
				
		}else{
			return false;
		}
	}

	public void comandoAtribuicao(){
		this.nextoken();
		if(this.token.getType() != TokenType.IDENTYFIER){
			experssaoAritimetica();
		}



		if(this.token.getType() != TokenType.KEYWORD){
			this.nextoken();
			if(this.token.getType() == TokenType.MATH_OP){
				this.nextoken();
				this.experssaoAritimetica();
			}
			
		}
		
		if(!this.token.getContent().equals("TO")){
			throw new SyntaxException(this.erroSyntax() + "" +"Expected 'TO' but, found " + token.getContent());
		}

		this.nextoken();

		if(this.token.getType() != TokenType.IDENTYFIER){
			throw new SyntaxException(this.erroSyntax() + "" +"Expected 'IDENTYFIER' but, found " + token.getContent());
		}
		this.nextoken();
		this.ponVirgula();

	}

	public void experssaoAritimetica(){
		termoAritimetico();

	}

	public void termoAritimetico(){
		fatorAritimetico();
		termoAritimetico2();
	}

	public void fatorAritimetico(){

		if(this.token.getType() != TokenType.NUMBER && this.token.getType() != TokenType.REALNUMBER && this.token.getType() != TokenType.IDENTYFIER && this.token.getType() != TokenType.MATH_OP){

			if(this.token.getType() != TokenType.LEFTPAR){
				throw new SyntaxException(this.erroSyntax() + "" +"Expected '(' but, found " + token.getContent());
			}
				
				if(this.token.getType() != TokenType.IDENTYFIER){
					this.nextoken();
					this.experssaoAritimetica();
				}
				
				if(this.token.getType() == TokenType.REL_OP){
					this.nextoken();
				}

				if(this.token.getType() == TokenType.NUMBER || this.token.getType() == TokenType.REALNUMBER){
					this.nextoken();
				}


			if(this.token.getType() != TokenType.RIGHTPAR){
				throw new SyntaxException(this.erroSyntax() + "" +"Expected ')' but, found " + token.getContent());
			}
		}	
		

		this.nextoken();
		
			
		
		if(this.token.getContent().equals("=")){
			this.nextoken();
			this.nextoken();
			this.nextoken();
		}
	}

	public void termoAritimetico2(){

		
		if(this.token != null && !this.token.getContent().equals("TO") && this.token.getType() != TokenType.RIGHTPAR 
		&& this.token.getType() != TokenType.REL_OP && this.token.getType() != TokenType.KEYWORD
		&& !this.token.getContent().equals("AND") && !this.token.getContent().equals("OR")
		&& !this.token.getContent().equals("=")){
			termoAritimetico3();
			termoAritimetico2();
		}
	}

	public void termoAritimetico3(){

		
		if(!this.token.getContent().equals("*") && !this.token.getContent().equals("/")){

			if(!this.token.getContent().equals("+") && !this.token.getContent().equals("-")){
				throw new SyntaxException(this.erroSyntax() + "" +"Expected '*' or '/' but, found " + token.getContent());
			}
			
		}
		
		this.nextoken();
		this.termoAritimetico();


		
	}

	public void comandoEntrada(){
		this.nextoken();
		if (this.token.getType() != TokenType.IDENTYFIER) {
			throw new SyntaxException(this.erroSyntax() + "" +"Identyfier or Number expected, found " + token.getType());
		}

		this.nextoken();
		this.ponVirgula();
	}

	public void comandoSaida() {
		this.nextoken();
		if (this.token.getType() != TokenType.LEFTPAR) {
			throw new SyntaxException(this.erroSyntax() + "" +"Left Parenthesis expected, found " + token.getType());
		}

		this.nextoken();
		/*    desse jeito aqui  */	
		if(this.token.getType() == TokenType.STRING){
			this.nextoken();
		}

		if (this.token.getType() != TokenType.IDENTYFIER && this.token.getType() != TokenType.STRING) {
			throw new SyntaxException(this.erroSyntax() + "" +"Identyfier or Number expected, found " + token.getType());
		}
		
		this.nextoken();

		if(this.token.getType() == TokenType.STRING){
			this.nextoken();
		}

		if (this.token.getType() != TokenType.RIGHTPAR) {
			throw new SyntaxException(this.erroSyntax() + "" +"Right Parenthesis expected, found " + token.getType());
		}

		this.nextoken();
		this.ponVirgula();
		
	}

	public void expressaoRelacional(){
		this.termoRelacional();
		this.expressaoRelacional2();
	}

	public void termoRelacional(){
/* 
		if(this.token.getType() == TokenType.LEFTPAR){
		
				
				if(this.token.getType() != TokenType.IDENTYFIER){
					this.nextoken();
					this.expressaoRelacional();
				}
				


			if(this.token.getType() != TokenType.RIGHTPAR){
				throw new SyntaxException("Expected ')' but, found " + token.getContent());
			}
			this.nextoken();

			if(this.token.getContent().equals("AND") || this.token.getContent().equals("OR")){
				this.operadorBooleano();
			}else if(this.token.getType() == TokenType.REL_OP){
				this.operadorRelacional();
				this.nextoken();
			}
			//this.nextoken();
			//this.nextoken();
		}
		*/
		this.experssaoAritimetica();
		if(this.token.getType() != TokenType.KEYWORD && !this.token.getContent().equals("=")){
			this.operadorRelacional();
			this.nextoken();
			this.experssaoAritimetica();
		}
	
	}

	public void operadorRelacional(){
		if(this.token.getType() != TokenType.REL_OP){
			throw new SyntaxException(this.erroSyntax() + "" +"Relational operator expected, but found " + token.getContent());
		}
	}

	public void expressaoRelacional2(){
		
		if(this.token != null && this.token.getType() != TokenType.KEYWORD && this.token.getType() != TokenType.RIGHTPAR
		&& !this.token.getContent().equals("=")){
			this.operadorBooleano();
			this.expressaoRelacional();
		}

	}

	public void operadorBooleano(){
		
		if(!this.token.getContent().equals("AND") && !this.token.getContent().equals("OR")){
			throw new SyntaxException(this.erroSyntax() + "" +"Relational operator expected, but found " + token.getContent());
		}

		this.nextoken();

		

	}

	public void comandoCondicaoIF() {
		// implementar expressaoRelacional 
		this.nextoken();
		
		this.expressaoRelacional();
		
		if(!this.token.getContent().equals("THEN")){
			throw new SyntaxException(this.erroSyntax() + "" +"expected 'THEN', but found " + token.getContent());
		}
		
		this.listaComandos();
		
		if(this.token != null){
			this.comandoCondicao2();
		}
		
	}

	public void comandoCondicao2() {
		// implementar
		this.nextoken();

		if(this.token != null){

			this.listaComandos();
		}
	}

	public void comandoRepeticao() {
		// implementar expressaoRelacional
		this.nextoken();
		this.expressaoRelacional();

		this.listaComandos();

	}

	public void nextoken() {
		
		this.token = this.scanner.nextToken();
			
	}

	public String erroSyntax(){
		return this.scanner.error();
	}

	public void ponVirgula() {
		if (this.token.getType() != TokenType.PONTOVIRGULA) {
			throw new SyntaxException(this.erroSyntax() + "" +"Expected ';' at end of ocurrency but, found " + token.getContent());
		}
	}
 
}
