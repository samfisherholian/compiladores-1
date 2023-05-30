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
			throw new SyntaxException("Expected ':', found " + token.getType());
		}
		
		this.nextoken();
		if (!this.token.getContent().equals("DECLARACOES")) {
			throw new SyntaxException(this.erroSyntax() + "" +"Expected 'DECLARACOES', found " + token.getType());
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
		this.nextoken();       //vendo se ta pegando agr essa branch aqui
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
		//tipoVar();
	
		this.nextoken();
		this.ponVirgula();

	}

	public void tipoVar() {
			if (!this.token.getContent().equals("INTEIRO") && !this.token.getContent().equals("REAL")) {
				throw new SyntaxException(this.erroSyntax() + " " +"Expected 'INTEIRO' OR 'REAL' but, found " + token.getContent());
			}
		}

	public void listaComandos() {
		if(!this.token.getContent().equals("IF")){
			this.nextoken();
		}
		if (this.token != null) {
			comandos();
			if(this.token != null){
				listaComandos();
			}
			//listaComandos();
		}
	}

	public void comandos() {

		if(this.token != null){

			if(this.myEqualswithnull("WHILE")){
				this.comandoRepeticao();
			}
			if(this.myEqualswithnull("IF")){
				comandoCondicaoIF();
				//this.nextoken();
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
		//removi o else por enquanto
	//	else {
			//tipoComando();
			//!this.token.getContent().equals("ASSING")  && !this.token.getContent().equals("IF")
			//&& !this.token.getContent().equals("ELSE") && !this.token.getContent().equals("WHILE")
			//throw new SyntaxException("Expected 'Command' but, found " + token.getContent());
		//}
		
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
			throw new SyntaxException("Expected 'TO' but, found " + token.getContent());
		}

		this.nextoken();

		if(this.token.getType() != TokenType.IDENTYFIER){
			throw new SyntaxException("Expected 'IDENTYFIER' but, found " + token.getContent());
		}
		this.nextoken();
		this.ponVirgula();
		//this.nextoken();
		//this.comandos();

		//this.comandos(); toda vez q terminar o comando tem chamar a funcao comando pq o comando pode ser chamado mais de uma vez

	}

	public void experssaoAritimetica(){
		//this.nextoken();
		termoAritimetico();
		//experssaoAritimetica2();

	//	if(this.token != null && !this.token.getContent().equals("TO") && this.token.getType() != TokenType.RIGHTPAR 
	//	&& this.token.getType() != TokenType.REL_OP && this.token.getType() != TokenType.KEYWORD
	//	&& !this.token.getContent().equals("AND") && !this.token.getContent().equals("OR")
	//	&& !this.token.getContent().equals("=")){
	//		this.nextoken();
	//	}
	}

	public void termoAritimetico(){
		fatorAritimetico();
		termoAritimetico2();
	}

	public void fatorAritimetico(){

		if(this.token.getType() != TokenType.NUMBER && this.token.getType() != TokenType.REALNUMBER && this.token.getType() != TokenType.IDENTYFIER && this.token.getType() != TokenType.MATH_OP){

			if(this.token.getType() != TokenType.LEFTPAR){
				throw new SyntaxException("Expected '(' but, found " + token.getContent());
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
				throw new SyntaxException("Expected ')' but, found " + token.getContent());
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

		//this.nextoken();
		if(this.token != null && !this.token.getContent().equals("TO") && this.token.getType() != TokenType.RIGHTPAR 
		&& this.token.getType() != TokenType.REL_OP && this.token.getType() != TokenType.KEYWORD
		&& !this.token.getContent().equals("AND") && !this.token.getContent().equals("OR")
		&& !this.token.getContent().equals("=")){
			termoAritimetico3();
			termoAritimetico2();
		}
	}

	public void termoAritimetico3(){

		//this.nextoken();
		if(!this.token.getContent().equals("*") && !this.token.getContent().equals("/")){

			if(!this.token.getContent().equals("+") && !this.token.getContent().equals("-")){
				throw new SyntaxException("Expected '*' or '/' but, found " + token.getContent());
			}
			
		}
		//this.expressaoAritimetica3();
		this.nextoken();
		this.termoAritimetico();

		//this.nextoken();
		/*if(!this.token.getContent().equals("*") && !this.token.getContent().equals("/")){

			if(!this.token.getContent().equals("+") && !this.token.getContent().equals("-")){
				throw new SyntaxException("Expected '*' or '/' but, found " + token.getContent());
			}
			

			this.nextoken();
			fatorAritimetico();
		}
		this.nextoken();
		fatorAritimetico();
*/
		
	}

	public void experssaoAritimetica2(){
		this.nextoken();
		if(this.token.getType() != TokenType.PONTOVIRGULA){
			expressaoAritimetica3();
			experssaoAritimetica2();
		}
	}

	public void expressaoAritimetica3(){
		if(!this.token.getContent().equals("+") && !this.token.getContent().equals("-")){
			throw new SyntaxException("Expected '+' or '-' but, found " + token.getContent());
		}

		termoAritimetico();
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
		/*    desse jeito aqui  */	
		if(this.token.getType() == TokenType.STRING){
			this.nextoken();
		}

		if (this.token.getType() != TokenType.IDENTYFIER && this.token.getType() != TokenType.STRING) {
			throw new SyntaxException("Identyfier or Number expected, found " + token.getType());
		}
		
		this.nextoken();

		if(this.token.getType() == TokenType.STRING){
			this.nextoken();
		}

		if (this.token.getType() != TokenType.RIGHTPAR) {
			throw new SyntaxException("Right Parenthesis expected, found " + token.getType());
		}

		this.nextoken();
		this.ponVirgula();
		//this.nextoken(); //no futuro chama isso quando tiver mais comandos
		//this.nextoken();
		//this.comandos();
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
	//	if(this.token.getType() != TokenType.IDENTYFIER){
			//this.experssaoAritimetica();
		//}
		//this.nextoken();

		//this.operadorRelacional();
		//this.nextoken();
	}

	public void operadorRelacional(){
		if(this.token.getType() != TokenType.REL_OP){
			throw new SyntaxException("Relational operator expected, but found " + token.getContent());
		}
	}

	public void expressaoRelacional2(){
		//this.nextoken();
		if(this.token != null && this.token.getType() != TokenType.KEYWORD && this.token.getType() != TokenType.RIGHTPAR
		&& !this.token.getContent().equals("=")){
			this.operadorBooleano();
			this.expressaoRelacional();
		}

	}

	public void operadorBooleano(){
		
		if(!this.token.getContent().equals("AND") && !this.token.getContent().equals("OR")){
			throw new SyntaxException("Relational operator expected, but found " + token.getContent());
		}

		this.nextoken();

		//this.expressaoRelacional();

	}

	public void comandoCondicaoIF() {
		// implementar expressaoRelacional 
		this.nextoken();
		//if(this.token.getType() != TokenType.IDENTYFIER){
		this.expressaoRelacional();
		//}
		if(!this.token.getContent().equals("THEN")){
			throw new SyntaxException("expected 'THEN', but found " + token.getContent());
		}

		

		//this.nextoken();
		
		this.listaComandos();
		
		if(this.token != null){
			this.comandoCondicao2();
		}
//this.comandoCondicao2();
		
	}

	public void comandoCondicao2() {
		// implementar
		this.nextoken();

		if(this.token != null){
			//this.comandos();
			this.listaComandos();
		}
	}

	public void comandoRepeticao() {
		// implementar expressaoRelacional
		this.nextoken();
		this.expressaoRelacional();

		//if(this.token = this.scanner.back(this.token.getContent()))

		//this.comandos();
		this.listaComandos();

	}

	public void nextoken() {
		//if(this.token != null){
			this.token = this.scanner.nextToken();
			
	}

	public String erroSyntax(){
		return this.scanner.error();
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
