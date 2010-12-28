import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * @authors Miles McCrocklin, Irfan Somanni, Arthur Oysgelt
 *
 */
public class MiniREInterpreter {
	MiniREParser parser;
	HashMap<String,Variable> Variables;
	
	public void interpret(ParseTree tree) throws Exception
	{
		Variables = new HashMap<String, Variable>();
		this.miniRE_pr(tree.head);
	}
		
	//<miniRE-pr>
	public void miniRE_pr(ParseTreeNode root) throws Exception 
	{
		this.statement_list(root.getChildrens().get(1)); 
	}
	
	//<statement-list>
	public void statement_list(ParseTreeNode root) throws Exception
	{
		if (root==null) return;
		this.statement(root.getChildrens().get(0)); //<statement> eval
		this.statement_list(root.getChildrens().get(1)); //<statement-list>

	}
	
	//<statement>
	public void statement(ParseTreeNode root) throws Exception 
	{
		ParseTreeNode temp = root.getChildrens().get(0);
		Variable NodeVal;
		//ID := <exp>;
		if(temp.value.equals(new Token(TokenType.ID)))
		{
			NodeVal tok = this.exp(root.getChildrens().get(2));
			
			if (tok.type==NodeType.INTNUM) {
				NodeVal = new Variable(VariableType.INTNUM);
				NodeVal.i = tok.i;
			}
			else {
				NodeVal = new Variable(VariableType.STRING_MATCH_LIST);
				NodeVal.r = tok.r;
			}
			
			Variables.put(temp.value.id, NodeVal);
		}
		//replace REGEX with ASCII_STR in <filenames>;
		else if(temp.value.equals(new Token("replace", TokenType.RESERVED)))
		{
			/*NOTE the node has 7 children
			 * 0:replace
			 * 1:REGEX 
			 * 2:<------DFA for REGEX
			 * 3:with
			 * 4:ASCIISTR
			 * 5:in
			 * 6:filenames
			 * 7:;
			*/
			DFA dfa = root.getChildrens().get(2).dfa;
			String[] fileNames = this.filenames(root.getChildrens().get(6));
			String ascii = root.getChildrens().get(4).value.id;
			
			Scanner sc = new Scanner(new File(fileNames[0]));
			String newFile = "";
			StringMatchList rr = new StringMatchList();
			rr = find(fileNames[0], dfa);
			int lineCounter = 0;
			while(sc.hasNextLine() && rr.indexes.size() > 0){
				String currLine = sc.nextLine();
				String newLine = "";
				int s = 0;
				int e = rr.indexes.get(0);
				int i = 0;
				boolean look = false;
				while(i < rr.indexes.size() )
				{
					if(rr.lines.get(i) == lineCounter) {
						
						newLine += currLine.substring(s, e);
						s = rr.indexes.get(i)+rr.matches.get(i).length();
						if(!(rr.indexes.size()-1 == i))
							e = rr.indexes.get(i+1);// + rr.lines.get(i);
						look = true;
						newLine += ascii;
					}
					i++;
				}
				if(!look)
					newLine += currLine;
				lineCounter++;
				newFile += newLine+"\n";
			}
			
			
			PrintStream ps = new PrintStream(new File(fileNames[1]));
			ps.println(newFile);
		}
		//print(<exp-list>); 
		else if(temp.value.equals(new Token("print", TokenType.RESERVED)))
		{ //0: print 1: ( 2: exp_list
			System.out.println(this.exp_list(root.getChildrens().get(2)));
		}
		//if(<condition>){<stmt-list>}else{<stmt-list>}
		else if(temp.value.equals(new Token("if", TokenType.RESERVED)))
		{
			if(this.condition(root.getChildrens().get(2))) //0: if 1: ( 2: <conditions>
			{
				this.statement_list(root.getChildrens().get(5)); //3: ) 4: { 5: <stmt-list>
			}
			else if(root.getChildrens().size() > 6) //else if there is an else, use else
			{
				this.statement_list(root.getChildrens().get(9)); //6: } 7: else 8: { 9: <stmt-list> 
			}
		}
		//while(<condition>){<stmt-list>}
		else if(temp.value.equals(new Token("while", TokenType.RESERVED)))
		{
			while(this.condition(root.childrens.get(2)))//0:while 1:( //2:<condition>
			{
				this.statement_list(root.childrens.get(5)); //3:) 4:{ 5:<stmt-list>
			}
		}	
	}
	
	//<filenames>
	public String[] filenames(ParseTreeNode root)
	{
		String[] ret = new String[2];
		ret[0] = this.filename(root.getChildrens().get(0)); 	//0: file1
																//1: ->
		ret[1] = this.filename(root.getChildrens().get(2));		//2: file2
		return ret;
	}
	
	//<filename>
	public String filename(ParseTreeNode root)
	{
		return root.getChildrens().get(0).value.id; //0: filename
	}

	public String exp_list(ParseTreeNode root) throws Exception
	{
		String str = "";
		
		NodeVal v = (this.exp(root.getChildrens().get(0)));
		if(v.type == NodeType.INTNUM)
			str += v.i;
//		else if(v.type == NodeType.REGEX_RESULT)
//			str += v.r + ",";
		else if(v.type == NodeType.STRING_MATCH_LIST)
			str += v.r;
		if(root.getChildrens().size() > 1){
			String t = (this.exp_list_prime(root.getChildrens().get(2)));		
//			if(!t.equals("")) {
				str += ","+ t;
//			}
		}
		return str; 
	}
	
	//<exp-list'> --> <exp> | <exp><exp-list'>
	public String exp_list_prime(ParseTreeNode root) throws Exception
	{
		String str = "";
		NodeVal v = (this.exp(root.getChildrens().get(0)));
		if(v.type == NodeType.INTNUM)
			str += v.i ;
//		else if(v.type == NodeType.REGEX_RESULT)
//			str += v.r + ",";
		else if(v.type == NodeType.STRING_MATCH_LIST)
			str += v.r;
		if(root.getChildrens().size() > 1){
			String t = (this.exp_list_prime(root.getChildrens().get(2)));		
			str += "," +  t;
		}
		return str; 
	}
	
	//<condition>
	public boolean condition(ParseTreeNode root) throws Exception
	{
		boolean ret = false;
		NodeVal left = this.exp(root.getChildrens().get(0)); //0: <exp'>
		Token mid = this.bool_op(root.getChildrens().get(1));
		NodeVal right = this.exp(root.getChildrens().get(2));
		
		if((left.type == right.type) && (right.type == NodeType.INTNUM) )
		{
			if(mid.getId().equals("=="))
				ret = left.i == right.i;
			else if(mid.getId().equals("!="))
				ret = left.i != right.i;
			else if(mid.getId().equals(">="))
				ret = left.i >= right.i;
			else if(mid.getId().equals("<="))
				ret = left.i <= right.i;
			else if(mid.getId().equals("<"))
				ret = left.i < right.i;
			else if(mid.getId().equals(">"))
				ret = left.i > right.i;
			else
				throw new Exception("SEMANTIC ERROR: Expected Integer Operator but found" + mid.getId());	
			
		}
		else if((left.type == right.type) && (right.type == NodeType.INTNUM) )
		{
			if(mid.getId().equals("=="))
				ret = left.r.equals(right.r);
			else if(mid.getId().equals("!="))
				ret = !left.r.equals(right.r);
			else
				throw new Exception("SEMANTIC ERROR: Expected String Operator but found" + mid.getId());
		}
		else
			throw new Exception("SEMANTIC ERROR: Expected INTNUM or STRING_MATCH_LIST but found" + left.type + " and " + right.type);
		
		return ret;
	}
	
	//<bool-op>
	public Token bool_op(ParseTreeNode root)
	{
		return root.getChildrens().get(0).value;
	}
	//<bin-op>
	public Token bin_op(ParseTreeNode root) 
	{
		return root.getChildrens().get(0).value;
	}
	//<bin-op'>
	public Token bin_op_prime(ParseTreeNode root) 
	{
		return root.getChildrens().get(0).value;
	}
	
	//<exp>
	public NodeVal exp(ParseTreeNode root) throws Exception 
	{
		NodeVal ret = null;
		NodeVal left = this.exp_prime(root.getChildrens().get(0)); //0: <exp'>
		if(root.getChildrens().size() > 1)
		{
			Token bin_op = this.bin_op(root.getChildrens().get(1));
			NodeVal right = this.exp(root.getChildrens().get(2));
			
			if((left.type == right.type) && (right.type == NodeType.INTNUM) )
			{
				int i = 0;
				if(bin_op.getId().equals("+"))
					i = left.i + right.i;
				else if(bin_op.getId().equals("-"))
					i = left.i - right.i;
				
				ret = new NodeVal(NodeType.INTNUM);
				ret.i = i;
			}
			else if((left.type == right.type) && (right.type == NodeType.STRING_MATCH_LIST))
			{
				StringMatchList s;
				ret = new NodeVal(NodeType.STRING_MATCH_LIST);
				if(bin_op.getId().equals("inters"))
				{
					s = left.r.inters(right);
					ret.r = s;
				}
				else if(bin_op.getId().equals("union"))
				{
					s = left.r.union(right);
					ret.r = s;
				}
				
			}
			else
				throw new Exception("SEMANTIC ERROR: Expected both INTNUM or both STRING_MATCH_LIST but found" + left.type + " and " + right.type);
		}
		else if(root.getChildrens().size() == 1)
			ret = left;
		return ret;
	}
	
	//<exp'>
	public NodeVal exp_prime(ParseTreeNode root) throws Exception
	{
		NodeVal ret = null;
		NodeVal left = this.exp_double_prime(root.getChildrens().get(0));
		if(root.getChildrens().size() > 1)
		{
			Token bin_op = this.bin_op_prime(root.getChildrens().get(1));
			NodeVal right = this.exp_prime(root.getChildrens().get(2));
			if((left.type == right.type) && (right.type == NodeType.INTNUM) )
			{
				int i = 0;
				if(bin_op.getId().equals("*"))
					i = left.i * right.i;
				else if(bin_op.getId().equals("/"))
					i = left.i / right.i;
				
				ret = new NodeVal(NodeType.INTNUM);
				ret.i =i;
			}
			else
				throw new Exception("SEMANTIC ERROR: Expected both INTNUM but found" + left.type + " and " + right.type);
		}
		else if(root.getChildrens().size() == 1)
			ret = left;
		return ret;
	}
	//<exp''>
	public NodeVal exp_double_prime(ParseTreeNode root) throws Exception 
	{
		ParseTreeNode temp = root.getChildrens().get(0);
		NodeVal ret = null;
		//<exp''> --> ID | INTNUM 
		if(temp.value.equals(new Token(TokenType.ID)))
		{
			if (Variables.containsKey(temp.value.id)){
				Variable NodeVal = Variables.get(temp.value.id);
				if(NodeVal.type == VariableType.INTNUM) {
					ret = new NodeVal(NodeType.INTNUM);
					ret.i = NodeVal.i;
				}
				else if(NodeVal.type == VariableType.STRING_MATCH_LIST) {
					ret = new NodeVal(NodeType.STRING_MATCH_LIST);
					ret.r = NodeVal.r;
				}
			}
			else {
				throw new Exception("SEMANTIC ERROR: Variable "+temp.value.id+" not found.");
			}
		}
		else if(temp.value.equals(new Token(TokenType.INTNUM)) )
		{
			ret = new NodeVal(NodeType.INTNUM);
			ret.i = Integer.valueOf((temp.value.id));
		}
		//<exp''> --> (<exp>)
		else if(temp.value.equals(new Token("(", TokenType.RESERVED)))
		{
			ret = this.exp(root.getChildrens().get(1)); //0:( 1:<exp> 2:)
		}
		//<exp''> --> #<exp>
		else if(temp.value.equals(new Token("#", TokenType.RESERVED)))
		{
			NodeVal v = this.exp(root.getChildrens().get(1));
			if(v.type == NodeType.STRING_MATCH_LIST) {
				ret = new NodeVal(NodeType.INTNUM);
				ret.i = v.r.matches.size();
			}
			else
				throw new Exception("SEMANTIC ERROR: Expected STRING_MATCH_LIST but got: " + v.type);
		}
		else if(temp.value.equals(new Token("find", TokenType.RESERVED)))
		{
			DFA dfa = root.getChildrens().get(1).dfa;
			String fileName = root.getChildrens().get(3).getChildrens().get(0).value.id;
			
			StringMatchList rr = new StringMatchList();
			rr = find(fileName, dfa);
			ret = new NodeVal(NodeType.STRING_MATCH_LIST);
			ret.r = rr;
			
		}
		return ret;
	}
	
	public StringMatchList find(String fileName, DFA dfa) throws Exception{
		StringMatchList r = new StringMatchList();
		Scanner scan = new Scanner(new File(fileName));
		int lineNum = 0;
		while(scan.hasNextLine()){
			String str = scan.nextLine();
			int[]array = dfa.maxMatchStringIndexAndLength(str,0);
			while (array[0]!=-1){
				if (array[1] > 0){
					r.matches.add(str.substring(array[0],array[0]+array[1]));
					r.indexes.add(array[0]);
					r.lines.add(lineNum);
					r.fileNames.add(fileName);
				}
				array = dfa.maxMatchStringIndexAndLength(str,array[0]+array[1]);

				if(array[0]==0){
					break;
				}
			}
			if (array[1] >= 0){
				r.matches.add(str.substring(array[0],array[0]+array[1]));
				r.indexes.add(array[0]);
				r.lines.add(lineNum);
				r.fileNames.add(fileName);
			}
			lineNum++;
		}
		return r;
		
	}
	
//	public void replace(String fileName, DFA dfa) throws Exception {
//		RegexResult r = this.find(fileName, dfa);
//		
//		for(int i = 0; i<r.matches.size(); i++)
//		{
//			for(i)
//		}
//	
//	}
	
	
	
	public static void main(String[] args) throws Exception{
		MiniREScanner scan = new MiniREScanner("bonus1.mre");
		MiniREParser parser = new MiniREParser(scan);
		try {
			parser.MiniRE_pr();
//			System.out.println(parser.tree);
			MiniREInterpreter mri = new MiniREInterpreter();
			mri.interpret(parser.tree);
		}
		catch (Exception e){
			throw e;
		}
		

	}
	
}
