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

	String[] keyWords = { "PRINT", "INT", "FLOAT", "IF", "ELSE", "THEN", "TO", "DECLARACOES", "ALGORITMO" };

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
					if (this.isLetter(currentChar)) {
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
					else if (this.isMathOp(currentChar)) {
						content += currentChar;
						state = 3;
					}
					else if (this.isAssign(currentChar)) {
						content += currentChar;
						state = 4;
					}
					else if (this.isOperator(currentChar)) {
						content += currentChar;
						state = 5;
					}
					else if (this.isLefParentese(currentChar)) {
						content += currentChar;
						state = 6;
					}
					else if (this.isRightParentese(currentChar)) {
						content += currentChar;
						state = 7;
					}
					else if (this.isDot(currentChar)) {
						pointCounter++;
						content += currentChar;
						state = 2;
					}
					else if (this.isBarorUndescore(currentChar)) {
						content += currentChar;
						state = 1;
					}
					else if (this.isComment(currentChar)) {
						// ignora os caracteres apos ver o '#' ate chegar ao fim da linha '\n'
						do {
							currentChar = this.nextChar();
							contadorLC.countLineAndColum(currentChar);
						} while (currentChar != '\n');
					}
					else if (this.isTwopoints(currentChar)) {
						content += currentChar;
						state = 8;
					}
					else if (this.isPointcolon(currentChar)) {
						content += currentChar;
						state = 9;
					}
					else {
						throw new RuntimeException(error() + " symbol not regonized");
					}
					break;
				
				case 1:
					if (this.isLetter(currentChar) || this.isDigit(currentChar) || this.isBarorUndescore(currentChar)) {
						content += currentChar;
						state = 1;
					}
					else if (invalidCaractere(currentChar) || isDot(currentChar)) {
						throw new RuntimeException(error() + " Indentifyer Malformed!");
					}
					else {
						// verifica se tem palavras reservadas
						for (int i = 0; i < keyWords.length; i++) {
							if (keyWords[i].equals(content)) {
								this.back(currentChar);
								return new Token(TokenType.KEYWORD, content);
							}
						}
						this.back(currentChar);
						return new Token(TokenType.IDENTYFIER, content);
					}
					break;
				
				case 2:
					if (this.isDigit(currentChar)) {
						content += currentChar;
						state = 2;
					}
					else if (this.isLetter(currentChar)) {
						throw new RuntimeException(error() + " Number Malformed!");

						// se for um 'ponto' incrementa o contador de ponteiro e adiciona no conteudo
					}
					else if (this.isDot(currentChar)) {
						pointCounter++;
						content += currentChar;

						// logica dos numeros reais adicionada aqui
						// se tiver um 'ponto' vai verificar se tem mais de 1 ponto
						// e se o conteudo termina com ponto
					}
					else if (content.contains(".")) {
						if (pointCounter > 1 || content.endsWith(".")) {
							throw new RuntimeException(error() + " Real Number Malformed! in ");
						}
						else {
							pointCounter = 0;
							this.back(currentChar);
							return new Token(TokenType.REALNUMBER, content);
						}
					}
					else {
						this.back(currentChar);
						return new Token(TokenType.NUMBER, content);
					}
					break;
			
				// verifica um operador matematico
				case 3:
					if (this.invalidCaractere(currentChar)) {
						throw new RuntimeException(error() + " Operator Malformed!");
					}
					else {
						this.back(currentChar);
						return new Token(TokenType.MATH_OP, content);
					}

				// reconhece o operador '='
				case 4:
					if (this.invalidCaractere(currentChar)) {
						throw new RuntimeException(error() + " Operator Assign Malformed!");
					}
					else {
						this.back(currentChar);
						return new Token(TokenType.ASSINGN, content);
					}

				// indetifica os operadores relacionais
				case 5:
					if (this.invalidCaractere(currentChar)) {
						throw new RuntimeException(error() + " Operator rel Malformed!");
						// se tiver um operador de atribuicao entao adiciona ao operador relacional
						// <= ou >=
					}
					else if (this.isAssign(currentChar)) {
						content += currentChar;
					}
					else if (content.endsWith("!")) {
						throw new RuntimeException(error() + " Operator rel Malformed!");
					}
					else {
						this.back(currentChar);
						return new Token(TokenType.REL_OP, content);
					}
					break;
				
				// verifica se eh um parentese do lado esquerdo
				case 6:
					if (this.invalidCaractere(currentChar)) {
						throw new RuntimeException(error() + " Left Parentese Malformed!");

					}
					this.back(currentChar);
					return new Token(TokenType.LEFTPAR, content);
				
				// verifica se eh um parenteses do lado direito
				case 7:
					if (this.invalidCaractere(currentChar)) {
						throw new RuntimeException(error() + " Right Parentese Malformed!");

					}
					this.back(currentChar);
					return new Token(TokenType.RIGHTPAR, content);

				case 8:
					if (this.invalidCaractere(currentChar)) {
						throw new RuntimeException(error() + " Twopoints Malformed!");
					}

					this.back(currentChar);
					return new Token(TokenType.TWOPOINTS, content);
				
				case 9:
					if (this.invalidCaractere(currentChar)) {
						throw new RuntimeException(error() + " Semicolon Malformed!");
					}

					this.back(currentChar);
					return new Token(TokenType.PONTOVIRGULA, content);
				
				case 10:
					if (this.invalidCaractere(currentChar)) {
						throw new RuntimeException(error() + " String Malformed!");
					}

					this.back(currentChar);
					return new Token(TokenType.STRING, content);
			}
		}
	}

	private char nextChar() {
		return this.contentTXT[this.pos++];
	}

	private void back(char c) {
		// this.pos--;
		if (c != '\n') {
			contadorLC.decrementeColumn();
		}
		else {
			contadorLC.decrementLine();
		}

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

	private boolean isMathOp(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	private boolean isAssign(char c) {
		return c == '=';
	}

	// verifica se sao invalidos
	// se todos forem invalidos
	private boolean invalidCaractere(char c) {
		return !this.isLetter(c) && !this.isDigit(c) && !this.isOperator(c) && !this.isMathOp(c)
				&& !this.isAssign(c) && !this.isSpace(c) &&
				!this.isLefParentese(c) && !this.isRightParentese(c) && !this.isDot(c) && !this.isTwopoints(c)
				&& !this.isPointcolon(c);
	}

	private boolean isLefParentese(char c) {
		return c == '(';
	}

	private boolean isRightParentese(char c) {
		return c == ')';
	}

	private boolean isDot(char c) {
		return c == '.';
	}

	private boolean isComment(char c) {
		return c == '#';
	}

	private boolean isBarorUndescore(char c) {

		return c == '-' || c == '_';
	}

	private boolean isTwopoints(char c) {
		return c == ':';
	}

	private boolean isPointcolon(char c) {
		return c == ';';
	}

	private String error() {
		return "erro in line " + contadorLC.getLine() + " column " + contadorLC.getColum();
	}

	private boolean isEOF() {
		if (this.pos >= this.contentTXT.length) {
			return true;
		}
		return false;
	}
}
