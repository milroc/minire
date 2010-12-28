//import java.util.ArrayList;
//import java.util.Collection;
//
//
//
//public class RegexInterpreter {
//	RegexParser parser;
//	
//	
//	public DFA interpret(ParseTree tree) throws Exception
//	{
//		
////		if(tree == null || 	tree.head == null) {
////			NFA emptyString = new DFA();
////			NFAState emptyState = new NFAState("", DFAState.StateType.START_FINAL);
////			emptyString.addState(emptyState);
////			return emptyString;
////		}
//		NFA d = this.rexp(tree.head);
//		if(d != null)
//			return d.generateDFA();
//		else
//			return null;
//	}
//	
//	public NFA rexp(ParseTreeNode root) throws Exception
//	{
//		if (root == null)
//			return null;
//		else if(root.getChildrens().size() > 0) //<rexp'>
//			return this.rexp_prime(root.getChildrens().get(0));
//		else {
//			NFA d=new NFA();
//			if(root.value.id.equals("")){ //SIGMA
//				//TODO Arthur
////				d.addState(s);
//			}
//			else //SOMETHING IS WRONG
//				d=null;
//			return d;
//		}
//	}
//	
//	//<rexp'> --> <rexp''> | <rexp''>UNION<rexp'>
//	public NFA rexp_prime(ParseTreeNode root) throws Exception
//	{
//		NFA left = this.rexp_double_prime(root.getChildrens().get(0));
//		if(root.getChildrens().size() > 1)
//		{
//			NFA right = this.rexp_prime(root.getChildrens().get(2));
//			left.Union(right);
//		}
//		return left;
//	}
//	
//	public NFA rexp_double_prime(ParseTreeNode root) throws Exception
//	{
//		NFA left = this.rexp_triple_prime(root.getChildrens().get(0));
//		if(root.getChildrens().size() > 1) // if(not null)
//		{
//			NFA right = this.rexp_double_prime(root.getChildrens().get(1));
//			left.Concatination(right);
//		}
//		return left;
//	}
//	
//	public NFA rexp_triple_prime(ParseTreeNode root) throws Exception
//	{
//		NFA left = this.rexp_quadruple_prime(root.getChildrens().get(0));
//		if (root.getChildrens().size()>1){
//			ArrayList<Token> right = rexp_triple_prime_squigly(root.getChildrens().get(1));
//			for(int i = 0; i < right.size(); i++) 
//			{
//				if(right.get(i).id.equals("*"))
//				{
//					System.out.println("LEFT STAR...");
//					left.Star();
//				}
//				else if(right.get(i).id.equals("+"))
//				{
//					left.Plus();
//				}
//				else if(right.get(i).id.equals("?"))
//				{
//					left.Question();
//				}
//			}
//		}
//		return left;
//	}
//	
//	public ArrayList<Token> rexp_triple_prime_squigly(ParseTreeNode root) throws Exception
//	{
//		ArrayList<Token> temp = new ArrayList<Token>();
//		if (root==null){
//			return temp;
//		}
//		temp.add(root.getChildrens().get(0).value);
//		if(root.getChildrens().size() > 1)
//			temp.addAll(this.rexp_triple_prime_squigly(root.getChildrens().get(1)));
//		return temp;
//	}
//	
//	public NFA rexp_quadruple_prime(ParseTreeNode root) throws Exception
//	{
//		Token temp = root.getChildrens().get(0).value;
//		NFA left;
//		if(temp.id.equals("("))
//			left = this.rexp(root.getChildrens().get(1));
//		else
//			left = this.char_class(root.getChildrens().get(0));
//		
//		return left;
//	}
//	
//	public NFA char_class(ParseTreeNode root) throws Exception
//	{
//		Token temp = root.getChildrens().get(0).value;
//		NFA left = new NFA();
//		ArrayList<Token> list = new ArrayList<Token>(); 
//		boolean foo = false;
//		if(temp.id.equals(".")) //.
//		{
//			list = new ArrayList<Token>();
//			for(char i = 32; i <= 126; i++)
//			{
//				list.add(new Token(""+i));
//			}
//			left.makeNFAWhichAcceptsThisCharacter(list.get(0).id.charAt(0), 0);
//			for(int i = 1; i < list.size(); i++){
//				NFA temp1 = new NFA();
//				temp1.makeNFAWhichAcceptsThisCharacter(list.get(i).id.charAt(0), 0);
//				left.Union(temp1);
//			}
//		}
//		else if(temp.id.equals("[")) //[^<>]
//		{
//			if(root.getChildrens().get(1).value.id.equals("^"))
//			{
//				
//				foo = true; //TODO FLIP NFA
//				ArrayList<Token> tok = this.char_set(root.getChildrens().get(2));
//				ArrayList<String> s = new ArrayList<String>();
//				for(Token t : tok)
//					s.add(t.id);
//				
//				for(char i = 32; i <= 126; i++)
//				{
//					if(!s.contains(""+i))
//						list.add(new Token(""+i));
//				}
//			}
//			else
//				list = this.char_set(root.getChildrens().get(1));
//			left.makeNFAWhichAcceptsThisCharacter(list.get(0).id.charAt(0), 0);
//			for(int i = 1; i < list.size(); i++){
//				NFA temp1 = new NFA();
//				temp1.makeNFAWhichAcceptsThisCharacter(list.get(i).id.charAt(0), 0);
//				left.Union(temp1);
//			}
//			
//		}
//		else if(RE_Char_comp(root.getChildrens().get(0).value.id.charAt(0)))
//		{
//			left.makeNFAWhichAcceptsThisCharacter(root.getChildrens().get(0).value.id.charAt(0), 0);
//		}
//		return left;
//	}
//	
//	public ArrayList<Token> char_set(ParseTreeNode root) throws Exception
//	{
//		ArrayList<Token> ret = new ArrayList<Token>();
//		
//		ret.addAll(this.char_set_prime(root.getChildrens().get(0)));
//		if(root.getChildrens().size() > 1)
//			ret.addAll(this.char_set(root.getChildrens().get(1)));
//		return ret;
//		
//	}
//	
//	public ArrayList<Token> char_set_prime(ParseTreeNode root) throws Exception
//	{
//		ArrayList<Token> ret = new ArrayList<Token>();
//		
//		ret.add(root.getChildrens().get(0).value);
//		if(root.getChildrens().size() > 1){ //RANGE
//			Token t = root.getChildrens().get(2).value;
//			char begin = ret.get(0).id.charAt(0);
//			char end = t.id.charAt(0);
//			for (char c=(char) (begin+1); c<=end; c++){
//				ret.add(new Token(""+c,TokenType.ASCIISTR));
//			}
//			
//		}
//			//TODO EMPTY RANGE
//		return ret;
//	}
//	
//	
//	public boolean RE_Char_comp(char c) throws Exception //TODO WTF
//	{
//		boolean ret = (c>=32 && c<=126);
//		ret = ret && c!= '(' && c != ')'; //(a)
//		ret = ret && c!='|'; //a|b
//		ret = ret && c!= '*' && c!='+' && c!= '?'; //*a ?a +a
////		ret = ret && c!= '[' && c!=']'; //[a
//		return ret;
//	}
//	
//}


import java.util.ArrayList;
import java.util.Collection;



public class RegexInterpreter {
	RegexParser parser;
	
	
	public DFA interpret(ParseTree tree) throws Exception
	{
		
//		if(tree == null || 	tree.head == null) {
//			NFA emptyString = new DFA();
//			NFAState emptyState = new NFAState("", DFAState.StateType.START_FINAL);
//			emptyString.addState(emptyState);
//			return emptyString;
//		}
		NFA d = this.rexp(tree.head);
		if(d != null)
			return d.generateDFA();
		else{
			DFA empty = new DFA();
			DFAState mt = new DFAState("Empty DFA Acceptance State", StateType.START_FINAL);
			empty.addState(mt);
			return empty;
		}	
			
	}
	
	public NFA rexp(ParseTreeNode root) throws Exception
	{
		if (root == null)
			return null;
		else if(root.getChildrens().size() > 0) //<rexp'>
			return this.rexp_prime(root.getChildrens().get(0));
		else {
			NFA d=new NFA();
			return d;
		}
	}
	
	//<rexp'> --> <rexp''> | <rexp''>UNION<rexp'>
	public NFA rexp_prime(ParseTreeNode root) throws Exception
	{
		NFA left = this.rexp_double_prime(root.getChildrens().get(0));
		if(root.getChildrens().size() > 1)
		{
			NFA right = this.rexp_prime(root.getChildrens().get(2));
			left.Union(right);
		}
		return left;
	}
	
	public NFA rexp_double_prime(ParseTreeNode root) throws Exception
	{
		NFA left = this.rexp_triple_prime(root.getChildrens().get(0));
		if(root.getChildrens().size() > 1) // if(not null)
		{
			NFA right = this.rexp_double_prime(root.getChildrens().get(1));
			left.Concatination(right);
		}
		return left;
	}
	
	public NFA rexp_triple_prime(ParseTreeNode root) throws Exception
	{
		NFA left = this.rexp_quadruple_prime(root.getChildrens().get(0));
		if (root.getChildrens().size()>1){
			ArrayList<Token> right = rexp_triple_prime_squigly(root.getChildrens().get(1));
			for(int i = 0; i < right.size(); i++) 
			{
				if(right.get(i).id.equals("*"))
				{
					System.out.println("LEFT STAR...");
					left.Star();
				}
				else if(right.get(i).id.equals("+"))
				{
					left.Plus();
				}
				else if(right.get(i).id.equals("?"))
				{
					left.Question();
				}
			}
		}
		return left;
	}
	
	public ArrayList<Token> rexp_triple_prime_squigly(ParseTreeNode root) throws Exception
	{
		ArrayList<Token> temp = new ArrayList<Token>();
		if (root==null){
			return temp;
		}
		temp.add(root.getChildrens().get(0).value);
		if(root.getChildrens().size() > 1)
			temp.addAll(this.rexp_triple_prime_squigly(root.getChildrens().get(1)));
		return temp;
	}
	
	public NFA rexp_quadruple_prime(ParseTreeNode root) throws Exception
	{
		Token temp = root.getChildrens().get(0).value;
		NFA left;
		if(temp.id.equals("("))
			left = this.rexp(root.getChildrens().get(1));
		else
			left = this.char_class(root.getChildrens().get(0));
		
		return left;
	}
	
	public NFA char_class(ParseTreeNode root) throws Exception
	{
		Token temp = root.getChildrens().get(0).value;
		NFA left = new NFA();
		ArrayList<Token> list = new ArrayList<Token>(); 
		boolean foo = false;
		if(temp.id.equals(".")) //.
		{
			list = new ArrayList<Token>();
			for(char i = 32; i <= 126; i++)
			{
				list.add(new Token(""+i));
			}
			left.makeNFAWhichAcceptsThisCharacter(list.get(0).id.charAt(0), 0);
			for(int i = 1; i < list.size(); i++){
				NFA temp1 = new NFA();
				temp1.makeNFAWhichAcceptsThisCharacter(list.get(i).id.charAt(0), 0);
				left.Union(temp1);
			}
		}
		else if(temp.id.equals("[")) //[^<>]
		{
			if(root.getChildrens().get(1).value.id.equals("^"))
			{
				
				foo = true; //TODO FLIP NFA
				ArrayList<Token> tok = this.char_set(root.getChildrens().get(2));
				ArrayList<String> s = new ArrayList<String>();
				for(Token t : tok)
					s.add(t.id);
				
				for(char i = 32; i <= 126; i++)
				{
					if(!s.contains(""+i))
						list.add(new Token(""+i));
				}
			}
			else
				list = this.char_set(root.getChildrens().get(1));
			left.makeNFAWhichAcceptsThisCharacter(list.get(0).id.charAt(0), 0);
			for(int i = 1; i < list.size(); i++){
				NFA temp1 = new NFA();
				temp1.makeNFAWhichAcceptsThisCharacter(list.get(i).id.charAt(0), 0);
				left.Union(temp1);
			}
			
		}
		else if(RE_Char_comp(root.getChildrens().get(0).value.id.charAt(0)))
		{
			left.makeNFAWhichAcceptsThisCharacter(root.getChildrens().get(0).value.id.charAt(0), 0);
		}
		return left;
	}
	
	public ArrayList<Token> char_set(ParseTreeNode root) throws Exception
	{
		ArrayList<Token> ret = new ArrayList<Token>();
		
		ret.addAll(this.char_set_prime(root.getChildrens().get(0)));
		if(root.getChildrens().size() > 1)
			ret.addAll(this.char_set(root.getChildrens().get(1)));
		return ret;
		
	}
	
	public ArrayList<Token> char_set_prime(ParseTreeNode root) throws Exception
	{
		ArrayList<Token> ret = new ArrayList<Token>();
		
		ret.add(root.getChildrens().get(0).value);
		if(root.getChildrens().size() > 1){ //RANGE
			Token t = root.getChildrens().get(2).value;
			char begin = ret.get(0).id.charAt(0);
			char end = t.id.charAt(0);
			for (char c=(char) (begin+1); c<=end; c++){
				ret.add(new Token(""+c,TokenType.ASCIISTR));
			}
			
		}
			//TODO EMPTY RANGE
		return ret;
	}
	
	
	public boolean RE_Char_comp(char c) throws Exception //TODO WTF
	{
		boolean ret = (c>=32 && c<=126);
		ret = ret && c!= '(' && c != ')'; //(a)
		ret = ret && c!='|'; //a|b
		ret = ret && c!= '*' && c!='+' && c!= '?'; //*a ?a +a
//		ret = ret && c!= '[' && c!=']'; //[a
		return ret;
	}
	
}


