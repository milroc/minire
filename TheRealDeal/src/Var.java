import java.util.ArrayList;



public class Var {
	public enum VarType{INTNUM, STRING_MATCH_LIST};
	public RegexResult r;
	public int i;
//	public ArrayList<String> s;
	public VarType type;
	
	public Var(VarType type) {
		super();
		this.type = type;
	}

	public RegexResult union(Var right) {
		// TODO Auto-generated method stub
		return null;
	}

	public RegexResult inters(Var right) {
		// TODO Auto-generated method stub
		return null;
	}

}
