import java.util.ArrayList;


public class ParseTreeNode {
	Token value;
	DFA dfa;
	ArrayList<ParseTreeNode> childrens;
	NodeType type;
	
	public ParseTreeNode(Token value) {
		super();
		this.value = value;
		this.childrens = new ArrayList<ParseTreeNode>();
	}
	
	public ParseTreeNode(Token value, NodeType t) {
		super();
		this.value = value;
		this.childrens = new ArrayList<ParseTreeNode>();
		this.type =t;
	}
	
	public ParseTreeNode(DFA dfa) {
		super();
		this.dfa = dfa;
		this.childrens = new ArrayList<ParseTreeNode>();
	}
	
	public void addChildren(ParseTreeNode child){
		childrens.add(child);
	}
	public ArrayList<ParseTreeNode> getChildrens(){
		return childrens;
	}

	
	/**
	 * @return the value
	 */
	public Token getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Token value) {
		this.value = value;
	}

	/**
	 * @return the dfa
	 */
	public DFA getDfa() {
		return dfa;
	}

	/**
	 * @param dfa the dfa to set
	 */
	public void setDfa(DFA dfa) {
		this.dfa = dfa;
	}

	/**
	 * @return the type
	 */
	public NodeType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(NodeType type) {
		this.type = type;
	}

	/**
	 * @param childrens the childrens to set
	 */
	public void setChildrens(ArrayList<ParseTreeNode> childrens) {
		this.childrens = childrens;
	}

	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String ret = "";
		ret+=this.value+"\n";
		for (ParseTreeNode ptn : childrens){
			if (ptn!=null)
				ret+=ptn.toString();
			else 
				ret+="NULL";
		}
		return ret;		
	}
	
	
}


