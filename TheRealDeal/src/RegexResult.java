import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class RegexResult {
	ArrayList<String> matches;
	ArrayList<Integer> indexes;
	ArrayList<Integer> lines;
	ArrayList<String> fileNames;
	

	
	public RegexResult(ArrayList<String> m, ArrayList<Integer> i, ArrayList<Integer> l) throws Exception{
		matches = m;
		indexes = i;
		lines = l;
	}
	
	public RegexResult() throws Exception{
		matches = new ArrayList<String>();
		indexes = new ArrayList<Integer>();
		lines = new ArrayList<Integer>();
		fileNames = new ArrayList<String>();
	}
	
	public void add(String m, Integer i, Integer l, String f){
		matches.add(m);
		indexes.add(i);
		lines.add(l);
		fileNames.add(f);
	}
	
	public RegexResult union(Var var) throws Exception {
		 RegexResult result = new RegexResult();
		 RegexResult o = var.r;
		 result = this;
		 
			for(int i = 0; i < o.fileNames.size(); i++)
			{
//				int idx = -1;
				if(!(result.fileNames.contains(o.fileNames.get(i)) &&
						result.indexes.contains(o.indexes.get(i)) &&
						result.lines.contains(o.lines.get(i)) &&
						result.matches.contains(o.matches.get(i))) )
				{
					result.add(o.matches.get(i), o.indexes.get(i), o.lines.get(i), o.fileNames.get(i));
				}
//				if(!(result.fileNames.contains(this.fileNames.get(i)) &&
//						result.indexes.contains(this.indexes.get(i)) &&
//						result.lines.contains(this.lines.get(i)) &&
//						result.matches.contains(this.matches.get(i))) )
//				{
//					result.add(matches.get(i), indexes.get(i), lines.get(i), fileNames.get(i));
//				}
			}
			return result;

	 }

	 //TODO TEST
	 public RegexResult inters(Var var) throws Exception {

		 RegexResult result = new RegexResult();
		 RegexResult o = var.r;
			for(int i = 0; i < o.fileNames.size(); i++)
			{				
				if((this.fileNames.contains(o.fileNames.get(i)) &&
						this.indexes.contains(o.indexes.get(i)) &&
						this.lines.contains(o.lines.get(i)) &&
						this.matches.contains(o.matches.get(i))) )
				{
					result.add(o.matches.get(i), o.indexes.get(i), o.lines.get(i), o.fileNames.get(i));
				}
				
			}
			return result;
	 }
	 
	 public boolean equals(Var var) throws Exception {
		 boolean result = true;
		 RegexResult o = var.r;
		 if(this.indexes.size() != o.indexes.size()) result = false;
		 for(int i = 0; i < o.fileNames.size(); i++)
		 {				
			 if(!(this.fileNames.contains(o.fileNames.get(i)) &&
					 this.indexes.contains(o.indexes.get(i)) &&
					 this.lines.contains(o.lines.get(i)) &&
					 this.matches.contains(o.matches.get(i))) )
			{
				result = false;
			}
			
		}
			return result;
	 }
	
	public String toString(){
		String ret = "";
		for (int x=0; x<this.indexes.size(); x++){
			ret+="{";
			ret+=this.matches.get(x)+",";
			ret+=this.fileNames.get(x)+",";
			ret+=this.lines.get(x)+",";
			ret+=this.indexes.get(x);
			ret+="}";
		}
		
		
		return ret;
	}
	
	public boolean equals(RegexResult o)
	{
		boolean val = true;
		for(int i = 0; i < this.fileNames.size(); i++)
		{
			if(!(this.fileNames.get(i).equals(o.fileNames.get(i)) &&
					this.indexes.get(i) == o.indexes.get(i) &&
					this.lines.get(i) == o.lines.get(i) &&
					this.matches.get(i).equals(o.matches.get(i))))
				val = false;
		}
		return val;
	}
	
//	public DFA interpret(ParseTree tree) throws Exception
//	{
//		if(tree == null || Êtree.head == null) {
//			NFA emptyString = new DFA();
//			NFAState emptyState = new NFAState("", DFAState.StateType.START_FINAL);
//			emptyString.addState(emptyState);
//			return emptyString;
//			}
//		NFA d = this.rexp(tree.head);
//		if(d != null)
//			return d.generateDFA();
//		else{
//			DFA empty = new DFA();
//		}
//		DFAState mt = new DFAState("Empty DFA Acceptance State", DFAState.StateType.START_FINAL);
//		empty.addState(mt);
//		return empty;
//	}
	
	
	
	
	
	
}
