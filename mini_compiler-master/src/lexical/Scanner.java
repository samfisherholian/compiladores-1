package lexical;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.management.RuntimeErrorException;

import utils.TokenType;

public class Scanner {

	int pos;
	char[] contentTXT;
	int state;
	int pointCounter;

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

			switch (state) {
			case 0:
				if (this.isLetter(currentChar)) {
					content += currentChar;
					state = 1;
				} else if (this.isSpace(currentChar)) {
					state = 0;
				} else if (this.isDigit(currentChar)) {
					content += currentChar;
					state = 2;
				}else if(this.isMathOp(currentChar)){
					content += currentChar;
					state = 3;
				}else if(this.isAssign(currentChar)){
					content += currentChar;
					state = 4;
				}else if(this.isOperator(currentChar)){
					content += currentChar;
					state = 5;
				}else if(this.isLefParentese(currentChar)){
					content += currentChar;
					state = 6;
				}else if(this.isRightParentese(currentChar)){
					content += currentChar;
					state = 7;
				}else if (this.isDot(currentChar)){
					pointCounter++;
					content += currentChar;
					state = 2;
				}
				break;
			case 1:
				if (this.isLetter(currentChar) || this.isDigit(currentChar)) {
					content += currentChar;
					state = 1;
				} else {
					this.back();
					return new Token(TokenType.IDENTYFIER, content);
				}
				break;
			case 2:
				if(this.isDigit(currentChar)) {
					content += currentChar;
					state = 2;
				
				}else if(this.isLetter(currentChar)) {
					throw new RuntimeException("Number Malformed!");

				//se for um '.' incrementa o contador de ponteiro e vai para o proximo caractere
				} else if(this.isDot(currentChar)){
					pointCounter++;
					content += currentChar;

				// logica dos numeros reais adicionada aqui
				//se tiver um '.' vai verificar se tem mais de 1 ponto
				// e se o conteudo termina com ponto		
				} else if(content.contains(".")){
					
					if(pointCounter > 1 || content.endsWith(".")){
						throw new RuntimeException("Real Number Malformed! in " + content);

					}else{
						pointCounter = 0;
						this.back();
						return new Token(TokenType.REALNUMBER, content);
					}

					
				}else{

					
					this.back();
					return new Token(TokenType.NUMBER, content);
				}
				//pointCounter = 0;
				break;
			//verifica um operador matematico	
			case 3:
				if(this.invalidCaractere(currentChar)){
					throw new RuntimeException("Operator Malformed!");
				}else{
					this.back();
					return new Token(TokenType.MATH_OP, content);
				}

			//reconhece o operador '='
			case 4:
					// vou ter que criar um metodo pra isso
				if(!this.isLetter(currentChar) && !this.isDigit(currentChar) && !this.isOperator(currentChar) && !this.isMathOp(currentChar) && !this.isAssign(currentChar) && !this.isSpace(currentChar)){
					throw new RuntimeException("Operator Assign Malformed!");
				}else{

					this.back();
					return new Token(TokenType.ASSINGN, content);
					
				}	

				/*
									if(isLetter(currentChar) && isDigit(currentChar) && isOperator(currentChar) &&isMathOp(currentChar) && isSpace(currentChar)){
					throw new RuntimeException("Operator Malformed!");
				}else{

					content += currentChar;
					state = 4;
					if(isLetter(currentChar) || isDigit(currentChar) || isSpace(currentChar)){
						this.back();
						return new Token(TokenType.ASSINGN, content);
					}else{
						throw new RuntimeException("Operator Malformed!");
					}

				}	
				 */
				//indetifica os operadores relacionais
				case 5:
					if(this.invalidCaractere(currentChar)){
						throw new RuntimeException("Operator rel Malformed!");
					}else if (this.isAssign(currentChar)){
						content += currentChar;
					}else{
						
						this.back();
						return new Token(TokenType.REL_OP, content);
						
					}
				break;
				//verifica se eh um parentese do lado esquerdo
				case 6:
					
					if(this.invalidCaractere(currentChar)){
						throw new RuntimeException("Left Parentese Malformed!");
					//se o carcetere n tiver atingido o fim do arquivo entao
					//volta	
					}else if(!this.isEOF()){
						this.back();
					}

						return new Token(TokenType.LEFTPAR, content);
				//verifica se eh um parenteses do lado direito
				case 7:

					if(this.invalidCaractere(currentChar)){
						throw new RuntimeException("Right Parentese Malformed!");

					}else if(!this.isEOF()){
						this.back();
					}

						return new Token(TokenType.RIGHTPAR, content);
			}
		}
	}

	private char nextChar() {
		return this.contentTXT[this.pos++];
	}

	private void back() {
		this.pos--;
	}

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isOperator(char c) {
		return c == '>' || c == '=' || c == '<' || c == '!';
	}

	private boolean isSpace(char c) {
		return c == ' ' || c == '\n' || c == '\t' || c == '\r';
	}

	private boolean isMathOp(char c){

		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	private boolean isAssign(char c){
		return c == '=';
	}

	//verifica se sao invalidos
	//se todos forem invalidos
	private boolean invalidCaractere(char c){
		return !this.isLetter(c) && !this.isDigit(c) && !this.isOperator(c) && !this.isMathOp(c) && !this.isAssign(c) && !this.isSpace(c) &&
		!this.isLefParentese(c) && !this.isRightParentese(c) && !this.isDot(c);
	}

	private boolean isLefParentese(char c){

		return c == '(';

	}

	private boolean isRightParentese(char c){


		return c == ')';

	}

	private boolean isDot(char c){
		return c == '.';
	}

	private boolean isEOF() {
		if (this.pos >= this.contentTXT.length) {
			return true;
		}
		return false;
	}

}
