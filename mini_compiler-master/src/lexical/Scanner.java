package lexical;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.management.RuntimeErrorException;
import utils.TokenType;

public class Scanner {

	int pos;
	char[] contentTXT;
	int state;
	int pointCounter;
	String[] keyWords = {"print", "int", "float", "if", "else"};
	LineColum contadorLC = new LineColum();

	public Scanner(String filename) {
		try {
			String contentBuffer = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
			this.contentTXT = contentBuffer.toCharArray();
			this.pos = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Token nextToken() {
		this.state = 0;
		String content = "";
		char currentChar;

		while (true) {
			if (isEOF()) {
				return null;
			}
			currentChar = this.nextChar();
			contadorLC.countLineAndColum(currentChar);

			switch (state) {
			case 0:
				if(this.isLetter(currentChar)) {
					content += currentChar;
					state = 1;
				}
				else if (this.isSpace(currentChar)) {
					state = 0;
				}
				else if (this.isDigit(currentChar)) {
					content += currentChar;
					state = 2;
				}
				else if(this.isMathOp(currentChar)){
					content += currentChar;
					state = 3;
				}
				else if(this.isAssign(currentChar)){
					content += currentChar;
					state = 4;
				}
				else if(this.isOperator(currentChar)){
					content += currentChar;
					state = 5;
				}
				else if(this.isLefParentese(currentChar)){
					content += currentChar;
					state = 6;
				}
				else if(this.isRightParentese(currentChar)){
					content += currentChar;
					state = 7;
				}
				else if(this.isDot(currentChar)){
					pointCounter++;
					content += currentChar;
					state = 2;
				}
				else if(this.isBarorUndescore(currentChar)){
					content+=currentChar;
					state = 1;
				}
				else if(this.isComment(currentChar)){
					
					//ignora os caracteres apos ver o '#' ate chegar ao fim da linha '\n'
					do{
						currentChar = this.nextChar();
						contadorLC.countLineAndColum(currentChar);
					}while(currentChar != '\n');
				}
				else{
					throw new RuntimeException(error() +"symbol not regonized");
				}
				break;
			
			case 1:
				if(this.isLetter(currentChar) || this.isDigit(currentChar) || this.isBarorUndescore(currentChar)) {
					content += currentChar;
					state = 1;
				}
				else{
				
					//verifica se tem palavras reservadas
					for(int i = 0;i < keyWords.length; i++){
						if(keyWords[i].equals(content)){
							this.back(currentChar);
							return new Token(TokenType.KEYWORD, content);	
						}
					}
					this.back(currentChar);
					return new Token(TokenType.IDENTYFIER, content);
				}
				break;
			
			case 2:
				if(this.isDigit(currentChar)) {
					content += currentChar;
					state = 2;
				}
				else if(this.isLetter(currentChar)) {
					throw new RuntimeException(error() +"Number Malformed!");
				}

				//se for um '.' incrementa o contador de ponteiro e adiciona no conteudo
				else if(this.isDot(currentChar)){
					pointCounter++;
					content += currentChar;
				}

				// logica dos numeros reais adicionada aqui
				//se tiver um '.' vai verificar se tem mais de 1 ponto
				// e se o conteudo termina com ponto
				else if(content.contains(".")){
					if(pointCounter > 1 || content.endsWith(".")){
						throw new RuntimeException(error() +"Real Number Malformed! in ");

					}
					else{
						pointCounter = 0;
						this.back(currentChar);
						return new Token(TokenType.REALNUMBER, content);
					}
				}
				else{
					this.back(currentChar);
					return new Token(TokenType.NUMBER, content);
				}
				break;
			
			//verifica um operador matematico	
			case 3:
				if(this.invalidCaractere(currentChar)){
					throw new RuntimeException(error() +"Operator Malformed!");
				}
				else{
					this.back(currentChar);
					return new Token(TokenType.MATH_OP, content);
				}

			//reconhece o operador '='
			case 4:
				if(this.invalidCaractere(currentChar)){
					throw new RuntimeException(error() +"Operator Assign Malformed!");
				}
				else{
					this.back(currentChar);
					return new Token(TokenType.ASSINGN, content);
				}	

				//indetifica os operadores relacionais
			case 5:
				if(this.invalidCaractere(currentChar)){
					throw new RuntimeException(error() +"Operator rel Malformed!");
				//se tiver um operador de atribuicao entao adiciona ao operador relacional
				// <= ou >=	
				}
				else if (this.isAssign(currentChar)){
					content += currentChar;
				}
				else{
						this.back(currentChar);
						return new Token(TokenType.REL_OP, content);
						
					}
				break;
				
			//verifica se eh um parentese do lado esquerdo
			case 6:
				if(this.invalidCaractere(currentChar)){
					throw new RuntimeException(error() +"Left Parentese Malformed!");
				}
					return new Token(TokenType.LEFTPAR, content);
			
			//verifica se eh um parenteses do lado direito
			case 7:
				if(this.invalidCaractere(currentChar)){
					throw new RuntimeException(error() +"Right Parentese Malformed!");

				}
					return new Token(TokenType.RIGHTPAR, content);

			}
		}
	}

	private char nextChar() {
		return this.contentTXT[this.pos++];
	}

	private void back(char symbol) {
		if(symbol != '\n') {
			contadorLC.decrementeColumn();
		}
		else{
			contadorLC.decrementLine();
		}
		this.pos--;
	}

	private boolean isLetter(char symbol) {
		return (symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z');
	}

	private boolean isDigit(char symbol) {
		return symbol >= '0' && symbol <= '9';
	}

	private boolean isOperator(char symbol) {
		return symbol == '>' || symbol == '=' || symbol == '<' || symbol == '!';
	}

	private boolean isSpace(char symbol) {
		return symbol == ' ' || symbol == '\n' || symbol == '\t' || symbol == '\r';
	}

	private boolean isMathOp(char symbol){

		return symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/';
	}

	private boolean isAssign(char symbol){
		return symbol == '=';
	}

	//verifica se sao invalidos
	//se todos forem invalidos
	private boolean invalidCaractere(char symbol){
		return !this.isLetter(symbol) && !this.isDigit(symbol) && !this.isOperator(symbol) && !this.isMathOp(symbol)
		&& !this.isAssign(symbol) && !this.isSpace(symbol) &&
		!this.isLefParentese(symbol) && !this.isRightParentese(symbol) && !this.isDot(symbol);
	}

	private boolean isLefParentese(char symbol){
		return symbol == '(';
	}

	private boolean isRightParentese(char symbol){
		return symbol == ')';
	}

	private boolean isDot(char symbol){
		return symbol == '.';
	}
	
	private boolean isComment(char symbol){
		return symbol == '#';
	}

	private boolean isBarorUndescore(char symbol){
		return symbol == '-' || symbol == '_';
	}

	private String error(){
		return "//Erro! Line "+ contadorLC.getLine() +" Column "+ contadorLC.getColum() +" //";
	}

	private boolean isEOF() {
		if (this.pos >= this.contentTXT.length) {
			return true;
		}
		return false;
	}
}
