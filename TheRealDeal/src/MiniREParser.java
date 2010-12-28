import java.io.File;
import java.io.PrintStream;



/**
 * 
 * @author Miles McCrocklin
 *
 */
public class MiniREParser {
	MiniREScanner scan;
	ParseTree tree;
	Token begin, end, dec, semicol, replace, with, in, print_0, openpar, closepar, if_0, curlyopen, curlyclose, else_0, arrow, while_0,
			comma, doubleequal, notequal, less, greater, loeq, goeq, union, inters, plus, minus, multiply, divide, numsymbol, find;
	Token ID, INTNUM, REGEX, ASCIISTR;
	//dec: :=
	//arrow: ->
	
	
	public MiniREParser(MiniREScanner scan){
		this.scan = scan;
		this.begin = new Token("begin", TokenType.RESERVED);
		this.end = new Token("end", TokenType.RESERVED);
		this.dec = new Token(":=", TokenType.RESERVED);
		this.semicol = new Token(";", TokenType.RESERVED);
		this.replace = new Token("replace", TokenType.RESERVED);
		this.with = new Token("with", TokenType.RESERVED);
		this.in = new Token("in", TokenType.RESERVED);
		this.print_0 = new Token("print", TokenType.RESERVED);
		this.openpar = new Token("(", TokenType.RESERVED);
		this.closepar = new Token(")", TokenType.RESERVED);
		this.if_0 = new Token("if", TokenType.RESERVED);
		this.curlyopen = new Token("{", TokenType.RESERVED);
		this.curlyclose = new Token("}", TokenType.RESERVED);
		this.else_0 = new Token("else", TokenType.RESERVED);
		this.arrow = new Token("->", TokenType.RESERVED);
		this.while_0 = new Token("while", TokenType.RESERVED);
		this.comma = new Token(",", TokenType.RESERVED);
		this.doubleequal = new Token("==", TokenType.RESERVED);
		this.notequal = new Token("!=", TokenType.RESERVED);
		this.less = new Token("<", TokenType.RESERVED);
		this.greater = new Token(">", TokenType.RESERVED);
		this.loeq = new Token("<=", TokenType.RESERVED);
		this.goeq = new Token(">=", TokenType.RESERVED);
		this.union = new Token("union", TokenType.RESERVED);
		this.inters = new Token("inters", TokenType.RESERVED);
		this.plus = new Token("+", TokenType.RESERVED);
		this.minus = new Token("-", TokenType.RESERVED);
		this.multiply = new Token("*", TokenType.RESERVED);
		this.divide = new Token("/", TokenType.RESERVED);
		this.numsymbol = new Token("#", TokenType.RESERVED);
		this.find = new Token("find", TokenType.RESERVED);
		
		
		this.ID = new Token(TokenType.ID);
		this.INTNUM = new Token(TokenType.INTNUM);
		this.REGEX = new Token(TokenType.REGEX);
		this.ASCIISTR = new Token(TokenType.ASCIISTR);
		
		
	}
	public void MiniRE_pr() throws Exception{
		try{
		ParseTreeNode root = new ParseTreeNode(new Token("MiniRE_pr"));
		tree = new ParseTree(root);
		tree.addNode(root, new ParseTreeNode(this.matchToken(this.begin)));
		tree.addNode(root, this.statement_list());
		tree.addNode(root, new ParseTreeNode(this.matchToken(this.end)));
		}
		catch(Exception e){
			throw e;
		}
		finally {
			
			PrintStream ps = new PrintStream(new File("Tree.tr"));
			ps.println(tree);
		}
	}
	
	//<stmt-list> --> <stmt> <stmt-list> | SIGMA
	public ParseTreeNode statement_list() throws Exception
	{
		if((scan.peekToken().getTT() == TokenType.ID) || 
				(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("replace")) ||
				(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("print"))  ||
				(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("if"))  ||
				(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("while")))
		{
			ParseTreeNode root = new ParseTreeNode(new Token("statement_list"));
			//<STATEMENT>
			tree.addNode(root, this.statement());
			//<STATEMENT-LIST>
			tree.addNode(root, this.statement_list());
			return root;
		}
		else
		{
			return null;
		}
	}
	
	//<stmt> --> 
	public ParseTreeNode statement() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("statement"));
		//<statement> --> ID := <exp> ;
		if(scan.peekToken().getTT() == TokenType.ID)
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.ID)));
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.dec)));
			tree.addNode(root, this.exp());
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.semicol)));
		}
		//<statement> --> replace REGEX with ASCII-STR in  <file-names> ;
		else if(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("replace"))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.replace)));
			
			ParseTreeNode temp = new ParseTreeNode(this.matchToken(this.REGEX));
			tree.addNode(root, temp); 
			RegexParser parser = new RegexParser(new Token(temp.value.id));
			parser.rexp();
			RegexInterpreter ri = new RegexInterpreter();
			DFA dfa = ri.interpret(parser.tree);
			ParseTreeNode temp2 = new ParseTreeNode(dfa);
			tree.addNode(root, temp2);
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.with)));
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.ASCIISTR)));
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.in)));
			tree.addNode(root, this.filenames());
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.semicol)));
		}
		//<statement> --> print ( <exp-list> ) ;
		else if(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("print"))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.print_0)));
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.openpar)));
			tree.addNode(root, this.exp_list());
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.closepar)));			
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.semicol)));
		}
		//<statement> --> if ( <condition> ) { <statement-list> } else { <statement-list> }   // Bonus part
		else if(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("if"))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.if_0)));
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.openpar)));
			tree.addNode(root, this.condition());
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.closepar)));			
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.curlyopen)));
			tree.addNode(root, this.statement_list());
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.curlyclose)));
//			if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("else"))) {
				tree.addNode(root, new ParseTreeNode(this.matchToken(this.else_0)));
				tree.addNode(root, new ParseTreeNode(this.matchToken(this.curlyopen)));
				tree.addNode(root, this.statement_list());
				tree.addNode(root, new ParseTreeNode(this.matchToken(this.curlyclose)));
//			}
		}
		//<statement> --> while ( <condition> ) { <statement-list> }                // Bonus part
		else if(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("while"))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.while_0)));
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.openpar)));
			tree.addNode(root, this.condition());
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.closepar)));			
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.curlyopen)));
			tree.addNode(root, this.statement_list());
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.curlyclose)));
		}
		return root;
	}
	
	public ParseTreeNode filenames() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("filenames"));
		tree.addNode(root, this.filename());
		tree.addNode(root, new ParseTreeNode(this.matchToken(this.arrow)));
		tree.addNode(root, this.filename());
		return root;
	}
	

	//<cond> --> <exp> <bool-op><exp>
	public ParseTreeNode condition() throws Exception
	{
//		ParseTreeNode root = new ParseTreeNode(new Token("condition"));
//		tree.addNode(root, this.exp());
//		tree.addNode(root, this.bool_op());
//		tree.addNode(root, this.exp());
//		return root;
//		
		ParseTreeNode root = new ParseTreeNode(new Token("condition"));
		ParseTreeNode left =this.exp(); 
		
		ParseTreeNode mid =this.bool_op(); 
		ParseTreeNode right = this.exp();
		String op = mid.getChildrens().get(0).value.id;
		if(left.type == NodeType.STRING_MATCH_LIST ||right.type == NodeType.STRING_MATCH_LIST &&
				op.equals("==") && op.equals("!="))
		{
			tree.addNode(root, left);
			tree.addNode(root, mid);
			tree.addNode(root, right);
			root.type = NodeType.STRING_MATCH_LIST;
		}
		else if(left.type == NodeType.INTNUM && right.type == NodeType.INTNUM)
		{
			tree.addNode(root, left);
			tree.addNode(root, mid);
			tree.addNode(root, right);
			root.type = NodeType.INTNUM;
		}
		else if(left.type == NodeType.ID ||right.type == NodeType.ID )
		{
			tree.addNode(root, left);
			tree.addNode(root, mid);
			tree.addNode(root, right);
			root.type = NodeType.ID;
		}
		
		else
			throw new Exception("SEMANTIC ERROR: \n EXPECTED INTNUM and INTNUM was supplied: " + left.type + " " +  right.type);
	
		return root;
	}
	
	public ParseTreeNode filename() throws Exception{
		ParseTreeNode root = new ParseTreeNode(new Token("filename"));
		tree.addNode(root, new ParseTreeNode(this.matchToken(this.ASCIISTR)));
		return root;
	}
	
	public ParseTreeNode exp_list() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("exp_list"));
		
			tree.addNode(root, this.exp());
			if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals(","))) //<exp>, <exp-list-2>
			{
				tree.addNode(root, new ParseTreeNode(this.matchToken(this.comma)));
				tree.addNode(root, this.exp_list());
			}
			return root;
	
	}
	
	public ParseTreeNode exp_list_prime() throws Exception
	{
	
		ParseTreeNode root = new ParseTreeNode(new Token("exp_list_prime"));
		
		if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("find")) || 
				(scan.peekToken().getTT() == TokenType.ID) || (scan.peekToken().getTT() == TokenType.INTNUM) || 
				(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("(")) || 
				(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("#")) ||
				(scan.peekToken().getTT() == TokenType.ASCIISTR))
		{
			tree.addNode(root, this.exp());
			if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals(","))) //<exp>, <exp-list-2>
			{
				tree.addNode(root, new ParseTreeNode(this.matchToken(this.comma)));
				tree.addNode(root, this.exp_list());
			}
			return root;
		}
		else
			return null;
	}
	
	public ParseTreeNode bool_op() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("bool_op"));
		
		if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("==")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.doubleequal)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("!=")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.notequal)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals(">")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.greater)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("<")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.less)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals(">=")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.goeq)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("<=")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.loeq)));
		}
		else
		{
			throw new Exception("EXPECTED A BOOLEAN OPERATOR BUT FOUND" + scan.peekToken());
		}
		return root;
	}
	
	
	public ParseTreeNode bin_op() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("bin_op"));
		
		if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("union")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.union)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("inters")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.inters)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("-")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.minus)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("+")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.plus)));
		}
//		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("*")) ||
//				(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("/")))
//		{
//			tree.addNode(root, this.bin_op_prime());
//		}
		else
		{
			throw new Exception("EXPECTED A BINARY OPERATOR BUT FOUND" + scan.peekToken());
		}
		return root;
	}
	
	public ParseTreeNode bin_op_prime() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("bin_op_prime"));
	
	
		if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("*")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.multiply)));
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("/")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.divide)));
		}
		else
		{
			throw new Exception("EXPECTED A BINARY OPERATOR BUT FOUND" + scan.peekToken());
		}
		return root;
	}
	
	
	public ParseTreeNode exp() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("exp"));
		ParseTreeNode left =this.exp_prime(); 
		if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("+")) ||
			(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("-")) ||
			(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("union")) ||
			(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("inters")))
		{
			ParseTreeNode mid =this.bin_op(); 
			ParseTreeNode right = this.exp();
			String op = mid.getChildrens().get(0).value.id;
			
			if(left.type == NodeType.INTNUM && right.type == NodeType.INTNUM && (op.equals("+") || op.equals("-")))
			{
				
				root.type = NodeType.INTNUM;
			}
			else if(left.type == NodeType.ID ||right.type == NodeType.ID )
			{
				
				root.type = NodeType.ID;
			}
			else if(left.type == NodeType.STRING_MATCH_LIST ||right.type == NodeType.STRING_MATCH_LIST &&
					(op.equals("union") || op.equals("inters")))
			{
				
				root.type = NodeType.STRING_MATCH_LIST;
			}
			else
				throw new Exception("SEMANTIC ERROR was supplied: " + left.type + " " +  right.type);
			tree.addNode(root, left);
			tree.addNode(root, mid);
			tree.addNode(root, right);
		}
		else
		{
			tree.addNode(root, left);
			root.type = left.type;
		}
		return root;
	}
	
	public ParseTreeNode exp_prime() throws Exception
	{
		ParseTreeNode root = new ParseTreeNode(new Token("exp_prime"));
		ParseTreeNode left =this.exp_double_prime(); 

		if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("*")) ||
				(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("/")))
		{
			ParseTreeNode mid =this.bin_op_prime(); 
			ParseTreeNode right = this.exp_prime();
			
			if(left.type == NodeType.INTNUM && right.type == NodeType.INTNUM)
			{
				
				root.type = NodeType.INTNUM;
			}
			else if(left.type == NodeType.ID ||right.type == NodeType.ID )
			{
				root.type = NodeType.ID;
			}
			else
				throw new Exception("SEMANTIC ERROR: was supplied: " + left.type + " " +  right.type);
			tree.addNode(root, left);
			tree.addNode(root, mid);
			tree.addNode(root, right);
			tree.addNode(root, mid);
			tree.addNode(root, right);
		}
		else
		{
			tree.addNode(root, left);
			root.type = left.type;
		}
		return root;
	}
	
	public ParseTreeNode exp_double_prime() throws Exception 
	{
		ParseTreeNode root = new ParseTreeNode(new Token("exp_double_prime"));
		
		if((scan.peekToken().getTT() == TokenType.ID))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.ID)));
			root.type = NodeType.ID;
		}
		else if((scan.peekToken().getTT() == TokenType.INTNUM))
		{
			tree.addNode(root, this.intnum(false)); 
			root.type = NodeType.INTNUM;
		}
		else if(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("-"))
		{
			tree.addNode(root, this.intnum(false));
			root.type = NodeType.INTNUM;
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("(")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.openpar)));
			tree.addNode(root, this.exp());
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.closepar)));
			root.type = root.getChildrens().get(1).type;
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("#")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.numsymbol)));
			tree.addNode(root, this.exp());
			root.type = NodeType.INTNUM;
		}
		else if((scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("find")))
		{
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.find)));
			ParseTreeNode temp = new ParseTreeNode(this.matchToken(this.REGEX));
			RegexParser parser = new RegexParser(new Token(temp.value.id));
			parser.rexp();
			RegexInterpreter ri = new RegexInterpreter();
			DFA dfa = ri.interpret(parser.tree);
			ParseTreeNode temp2 = new ParseTreeNode(dfa);
			tree.addNode(root, temp2);
			tree.addNode(root, new ParseTreeNode(this.matchToken(this.in)));
			tree.addNode(root, this.filename());
			root.type = NodeType.STRING_MATCH_LIST;
		}
		else
			throw new Exception("SYNTACTIC ERROR");
		
		
		return root;
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private ParseTreeNode intnum(boolean i) throws ScannerException, Exception { //if true -
		
		if((scan.peekToken().getTT() == TokenType.INTNUM))
		{
			ParseTreeNode newNode = new ParseTreeNode(this.matchToken(this.INTNUM));
			if(i)
				newNode.value.id = "-" + newNode.value.id;
			return newNode;
		}
		
		else if(scan.peekToken().getTT() == TokenType.RESERVED && scan.peekToken().getId().equals("-"))
		{
			this.matchToken(minus);
			return this.intnum(!(i));
		}
		
		else
			throw new Exception("Excpected INTNUM or - recieved" + scan.peekToken());
	}
	//if the scanner returns a null token - this indicates the token found by the scanner was not as expected 
	public Token matchToken(Token t) throws Exception {
		if (t.getTT() == TokenType.RESERVED) {
			if(t.getId().equals(scan.peekToken().id)){
				
			}else{
				throw new Exception("SYNTACTIC ERROR Token "+t.getId()+" did not match with "+scan.peekToken().id);
			}
		} else {
			if(t.getTT() == scan.peekToken().TT){
				
			}else{
				throw new Exception("SYNTACTIC ERROR Token "+t.getId()+" did not match with "+scan.peekToken().id);
			}
		}

		return scan.getToken();

	}
	
	
	
}
