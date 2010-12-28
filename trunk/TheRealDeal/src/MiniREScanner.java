/** 
 * @author Arthur Oysgelt 
 * GTID 901857654
 * aoysgelt@gatech.edu
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class MiniREScanner {
	/*
	 * Error Exit codes: 1 - standard library exception, 2 - Scanner found an
	 * unprintable char 3 - Scanner found a bad tokenI wasn't sure how to
	 * enumerate exit codes.
	 * 
	 * /**
	 * 
	 * @param args
	 */
	String[] words = { "if", "while", "begin", "end", "print", "replace",
			"union", "with", "in", "find", "else", "inters" };
	ArrayList<String> reservedWords = new ArrayList<String>();
	BufferedReader scriptFile;
	int position = 0, pChar, tokenArrayIndex = 0;
	boolean tokenWasPeeked = false;
	final char EOF = (char) -1, ESCAPE = '\\';
	char cha;
	List<Token> tokens = new ArrayList<Token>();
	PrintStream ps;

	public List<Token> getTokens() {
		return tokens;
	}

//	public static void main(String[] args) throws Exception {
//	
//		String testFilePath = "file1.in";
////		testFilePath += "arthur.txt";
//		MiniREScanner x = new MiniREScanner(testFilePath);
//		x.Scan(testFilePath);
//		List<Token> to = x.getTokens();
//		for (Iterator<Token> iterator = to.iterator(); iterator.hasNext();) {
//			Token object = iterator.next();
//			System.out.println(object);
//		}
//	}

	public MiniREScanner(String fname) throws FileNotFoundException {
		ps = new PrintStream(new File("Scanner.tok"));
		try {
			scriptFile = new BufferedReader(new FileReader(fname));
			if(scriptFile == null){
				System.out.println("No file found.");
				System.exit(1);
			}	
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		cha = getChar();
	}

	public void Scan(String fname) throws IOException, ScannerException {
		try {
			while (getToken().TT != null)
				;
			scriptFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		scriptFile.close();
	}
	


	// untested function!!!
	public Token peekToken() throws ScannerException {
//		System.out.println("peekToken-tokenWasPeeked: "+tokenWasPeeked);
		if (!tokenWasPeeked) {
			getToken();
			tokenWasPeeked = true;
		}
		return tokens.get(tokens.size() - 1);
	}

	Token getToken() throws ScannerException {
		if (tokenWasPeeked) {
			tokenWasPeeked = false;
		} else {
			if (cha == EOF) {
				if (tokens.get(tokens.size() - 1).TT != null)
					addTokenToList("" + EOF, null);
			} else if (Character.isWhitespace(cha)) {
				cha = processWhileSpace(cha);
				tokenWasPeeked = false;
				getToken();
			} else if ('"' == cha) {
				cha = processASCIISTR();
			} else if (Character.isLetter(cha)) {
				cha = processIndetifier(cha);
			} else if ('\'' == cha) {
				cha = processREGEX();
			} else if (Character.isDigit(cha)) {
				cha = processINTNUM(cha);
			} else if ("+;*/#(){},".indexOf(cha) > -1) {
				cha = processJUNK(cha);
			} else if ('-' == cha) {
				cha = processMinus();
			} else if ("!=:".indexOf(cha) > -1) {
				cha = processColonOrEqualityOrExclaim(cha);
			} else if ((cha == '<') || (cha == '>')) {
				cha = processLesserOrGreater(cha);
			} else {
				throw new ScannerException("Lexical Error:" + position
						+ ": unidentified token found.");
			}
		}
		
		ps.println(tokens.get(tokens.size() - 1));
		
		return tokens.get(tokens.size() - 1);
	}

	private char processMinus() {

		String id = "" + '-';
		char ch = getChar();
		if (ch == '>') {
			id += ch;
			ch = getChar();
		}
		addTokenToList(id, TokenType.RESERVED);
		return ch;
	}

	private char processLesserOrGreater(char ch) {
		String id = "" + ch;
		ch = getChar();
		if (ch == '=') {
			id += ch;
			ch = getChar();
		}
		addTokenToList(id, TokenType.RESERVED);
		return ch;
	}

	private char processColonOrEqualityOrExclaim(char ch)
			throws ScannerException {
		String id = "" + ch;
		ch = getChar();
		if (ch != '=') {
			throw new ScannerException("Lexical Error: " + (position - 1)
					+ ":found " + ch + "instead of =");
		}
		addTokenToList(id + ch, TokenType.RESERVED);
		return getChar();
	}

	private char processJUNK(char ch) {
		
		addTokenToList("" + ch, TokenType.RESERVED);
		return getChar();
	}

	private char processINTNUM(char ch) throws ScannerException {
		TokenType tokType = TokenType.INTNUM;
		String errorDesc;
		String out = "";
		if (ch == '0') {
			out += ch;
			addTokenToList(out, tokType);
			ch = getChar();
			if (Character.isDigit(ch)) {
				errorDesc = "Lexical Error:" + position + "leading 0 found.";
				throw new ScannerException(errorDesc);
			}
		} else {
			do {
				out += ch;
				ch = getChar();
			} while (Character.isDigit(ch));
			addTokenToList(out, tokType);

		}
		return ch;
	}

	
	private char processREGEX2() throws ScannerException {
		final char inch = '\'';
		String out = "", errorDesc;
		char ch = getChar();
		TokenType tokType = TokenType.REGEX;
		;
		while (ch != inch) {
			if (ch == EOF) {
				errorDesc = "Lexical Error: EOF reached before a " + inch
						+ " to match.";
				throw new ScannerException(errorDesc);
			}
			if (ch == ESCAPE) {
				ch = getChar();
			}
			out += ch;
			ch = getChar();
		}
		if (ch == inch) {
			ch = getChar();
		}
		addTokenToList(out, tokType);
		return ch;
	}
	// This function isn't finished yet!!!  
	private char processREGEX() throws ScannerException {
//		RegularExpresionValidator rgx = new RegularExpresionValidator();
		final char inch = '\'';
		String out = "", errorDesc;
		char ch = getChar();
		TokenType tokType = TokenType.REGEX;
		boolean outsideRange=true;
		while (ch != inch) {
			switch(ch){ 
				case EOF:
				errorDesc = "Lexical Error: EOF reached before a " + inch
						+ " to match.";
				throw new ScannerException(errorDesc);
				
				case ESCAPE://need to check this more - untested functionality
					ch = getChar();
					if(outsideRange){ 
						if(" \\*+?|[]()".indexOf(ch) == -1 )
							errorDesc = "Lexical Error:"+position+
							"REGEX: invalid use of escape character outside of brackets.";
					}else{
						//if("-\^[]".indexOf(ch) == -1 ){
							errorDesc = "Lexical Error:"+position+
							"REGEX: invalid use of escape character in brackets.";
						//}
					}
			}
			out += ch;
			ch = getChar();
		}
		ch = getChar();
		addTokenToList(out, tokType);
		return ch;
	}

	// --------------------------------------------------

	char processIndetifier(char ch) throws ScannerException {
		String id = "";
		String errorDesc = "Lexical Error: ";
		TokenType tokType = null;
		int i = 0;
		tokType = TokenType.ID;
		do {
			id += ch;
			if ((i > 9) || (ch == EOF)) {
				errorDesc += "Lexical Error: " + (position - 1) + ":Token "
						+ id + " is too long";
				throw new ScannerException(errorDesc);
			}
			ch = getChar();
			i++;
		} while (Character.isLetterOrDigit(ch) || (ch == '_'));
		for (int j = 0; j < words.length; j++) {
			if (words[j].equals(id)) {
				tokType = TokenType.RESERVED;
				break;
			}
		}
		addTokenToList(id, tokType);
		return ch;
	}

	// -------------------------------------------
	char processWhileSpace(char ch) throws ScannerException {
		while (Character.isWhitespace(ch)) {
			ch = getChar();
		}
		// ch = proccessCh(ch);
		return ch;
	}

	// ----------------------------------------------
	char processASCIISTR() throws ScannerException {
		final char inch = '"';
		String out = "", errorDesc;
		char ch = getChar();
		TokenType tokType = TokenType.ASCIISTR;
		;
		while (ch != inch) {
			if (ch == EOF) {
				errorDesc = "Lexical Error: EOF reached before a " + inch
						+ " to match.";
				throw new ScannerException(errorDesc);
			}
			if (ch == ESCAPE) {
				ch = getChar();
			}
			out += ch;
			ch = getChar();
		}
		if (ch == inch) {
			ch = getChar();
		}
		addTokenToList(out, tokType);
		return ch;
	}

	// ---------------------------------------------------------
	void addTokenToList(String value, TokenType tokType) {
		tokens.add(new Token(value, tokType));

	}

	// ----------------------------------------------------------------------

	char getChar() {
		try {
			int tokenPiece;
			tokenPiece = scriptFile.read();
			position++;
			return (char) tokenPiece;

		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return EOF;
		// Why is this return statement necessary?
	}
}
