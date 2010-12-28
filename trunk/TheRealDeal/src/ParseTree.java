import java.util.ArrayList;

public class ParseTree {
	ParseTreeNode head;
	ArrayList<ParseTreeNode> nodes;
	public ParseTree(ParseTreeNode head){
		this.head = head;
		nodes = new ArrayList<ParseTreeNode>();
		nodes.add(this.head);
	}
	public void addNode(ParseTreeNode parent, ParseTreeNode child){
		nodes.add(child);
		parent.addChildren(child);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String ret = "";
		ret+=head.toString()+'\n';	
		return ret;		
	}
}
