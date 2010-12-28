
public class RegexParser {
	RegexScanner scan;
	ParseTree tree;
	
	
	/*
	 * TERMINALS
	 * CLS_CHAR
	 * .
	 * [
	 * ^
	 * ]
	 * UNION
	 * +
	 * *
	 */
	
	
	public RegexParser(Token r)
	{
		scan = new RegexScanner(r.id);
		this.tree = null;
	}
	
	
	//<rexp> --> <rexp'> | SIGMA
	public ParseTreeNode rexp() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("rexp"));
		if (this.tree == null){
			this.tree = new ParseTree(root);
		}
		if(scan.queue.size() == 0) //SIGMA 
			root.value = new Token("");
		else //<rexp'>
			root.addChildren(this.rexp_prime());
		return root;
	}
	
		
		//<rexp'> --> <rexp''> | <rexp''>UNION<rexp'>
	public ParseTreeNode rexp_prime() throws Exception
	{	
		ParseTreeNode root = new ParseTreeNode(new Token("rexp_prime"));
		root.addChildren(this.rexp_double_prime());
		if(scan.peekChar() == '|')
		{
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar('|')))));
			root.addChildren(this.rexp_prime());
		}
		return root;
			
	}
	
	public ParseTreeNode rexp_double_prime() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("rexp_double_prime"));
		
		root.addChildren(this.rexp_triple_prime());
		if(scan.peekChar() == '(' || scan.peekChar() == '.' || scan.peekChar() == '[' || RE_Char_comp(scan.peekChar()))
		{
				root.addChildren(this.rexp_double_prime());
		}
		return root;
		
	}
	
	public ParseTreeNode rexp_triple_prime() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("rexp_triple_prime"));
		root.addChildren(this.rexp_quadruple_prime());
		if((scan.peekChar() == '+')|| (scan.peekChar() == '*') || (scan.peekChar() == '?'))
		{
			root.addChildren(this.rexp_triple_prime_squigly());
		}
		return root;
	}
	
	
	public ParseTreeNode rexp_triple_prime_squigly() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("rexp_triple_prime_squigly"));
		if(scan.peekChar() == '+')
		{
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar('+')))));
			root.addChildren(this.rexp_triple_prime_squigly());
		}
		else if(scan.peekChar() == '*')
		{
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar('*')))));
			root.addChildren(this.rexp_triple_prime_squigly());
		}
		else if(scan.peekChar() == '?')
		{
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar('?')))));
			root.addChildren(this.rexp_triple_prime_squigly());
		}
		else
		{
			root = null;
		}
		return root;
	}
	
	public ParseTreeNode rexp_quadruple_prime() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("rexp_quadruple_prime"));
		
		if(scan.peekChar() == '(')
		{
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar('(')))));
			root.addChildren(this.rexp());
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar(')')))));
			return root;
		}
		else if(scan.peekChar() == '.' || scan.peekChar() == '[' || RE_Char_comp(scan.peekChar()))
		{
			root.addChildren(this.char_class());
			return root;
		}
		else
			throw new Exception("Expected one of the following tokens: (, ., [, REChar but found: "+scan.peekChar()); 
	}
	
	public ParseTreeNode char_class() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("char_class"));
		
		if(scan.peekChar() == '.')
		{
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar('.')))));
		}
		else if (scan.peekChar() == '[')
		{
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar('[')))));
			if(scan.peekChar() == '^')
			{
				root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar('^')))));
			}
			root.addChildren(this.char_set());
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar(']')))));
		}
		else if (RE_Char_comp(scan.peekChar()))
		{
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar(scan.peekChar())))));
		}
		else
		{
			root = null;
		}
		return root;
	}
	
	public ParseTreeNode char_set() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("char_set"));
		root.addChildren(this.char_set_prime());
		if(CLS_Char_Comp(scan.peekChar()))
			root.addChildren(this.char_set());
		
		return root;
	}
	
	//<char-set-prime> --> 
	public ParseTreeNode char_set_prime() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("char_set_prime"));
		
		root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar(this.get_RE_Char_comp(scan.peekChar()))))));
		if(scan.peekChar() == '-')
		{
			root.addChildren(new ParseTreeNode(new Token(""+this.matchChar('-'))));
			root.addChildren(new ParseTreeNode(new Token(""+(this.matchChar(this.get_RE_Char_comp(scan.peekChar()))))));
		}
		
		
		return root;
	}

	//TODO FIX THIS	
	public boolean CLS_Char_Comp(char c) throws Exception
	{
		return RE_Char_comp(c);
	}
	

	//TODO FIX THIS	
	public boolean RE_Char_comp(char c) throws Exception
	{
		boolean ret = (c>=32 && c<=126);
		ret = ret && c!= '(' && c != ')'; //(a)
		ret = ret && c!='|'; //a|b
		ret = ret && c!= '*' && c!='+' && c!= '?'; //*a ?a +a
		ret = ret && c!= '[' && c!=']'; //[a
		return ret;
	}
	
	public char get_RE_Char_comp(char c) throws Exception
	{
		if (RE_Char_comp(c))
			return c;
		else 
			return (char) (c+1);
	}	

	
//		static boolean slash = false;
//		public boolean RE_Char_comp(char c)
//		{
//			if(!slash){
//				if(c == '\\'){
//					slash=true;
//					return RE_Char_comp(scan.peekChar());
//				}else{
//					if("\\*+?|[]().\'\"".indexOf(c)>-1){
//						return false;
//					}
//					return true;
//				}
//			}else
//				slash=false;
//			return true; 
//		}
//		if (c>=32 && c<=126){
//			if ("*+?|[]().'\"".indexOf(c)>=0){
//				throw new Exception("RE_CHAR INVALID");
//			}
//			else if(c=='\\'){
//				char nextChar = scan.peekChar();
//				if ("*+?|[]().'\"".indexOf(nextChar)>=0){
//					return true;
//				}
//				else {
//					throw new Exception("RE_CHAR INVALID");
//				}
//			}
//			else {
//				return true;
//			}
//		}
//		else {
//			throw new Exception("RE_CHAR INVALID");
//		}
	
	
	
	
	public char matchChar(char c) throws Exception {
		char temp = scan.getChar(); 
		if(c == temp)
			return c;
		else
				throw new Exception("Expected: "+c+" but received: "+temp);
	}
	
	public static void main(String[] args) throws Exception {
		//RegexParser parser = new RegexParser(new Token("(a\\\\+[0-9]?)[5]*.\\."));
		RegexParser parser = new RegexParser(new Token("[^5]"));
		try {
			parser.rexp();
//			System.out.println(parser.tree);
		}
		catch (Exception e){
//			System.out.println(parser.tree);
			throw e;
		}
		finally{
			System.out.println(parser.tree);
			RegexInterpreter ri = new RegexInterpreter();
			DFA dfa = ri.interpret(parser.tree);
			System.out.println(dfa);
		}
	}
	

}
