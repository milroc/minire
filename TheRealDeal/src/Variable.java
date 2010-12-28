
/**
 * 
 * @author Irfan Somani
 *
 */
public class Variable {

	public StringMatchList r;
	public int i;
	public VariableType type;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return (this.r.equals(((Variable) obj).getR()) && this.i == ((Variable) obj).getI() && this.type == ((Variable) obj).getType());
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String ret = "{";
		ret += this.getType();
		ret += ": ";
		if(this.getType() == VariableType.INTNUM)
			ret += this.getI();
		else if(this.getType() == VariableType.STRING_MATCH_LIST)
			ret += this.getR();
		ret += "}";
		return ret;
	}

	public Variable(StringMatchList r, VariableType type) {
		super();
		this.r = r;
		this.type = type;
	}

	
	public Variable(VariableType type) {
		super();
		this.type = type;
	}



	public Variable(int i, VariableType type) {
		super();
		this.i = i;
		this.type = type;
	}


	/**
	 * @return the r
	 */
	public StringMatchList getR() {
		return r;
	}


	/**
	 * @param r the r to set
	 */
	public void setR(StringMatchList r) {
		this.r = r;
	}


	/**
	 * @return the i
	 */
	public int getI() {
		return i;
	}


	/**
	 * @param i the i to set
	 */
	public void setI(int i) {
		this.i = i;
	}


	/**
	 * @return the type
	 */
	public VariableType getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(VariableType type) {
		this.type = type;
	}

}
