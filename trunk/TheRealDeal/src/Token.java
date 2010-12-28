
public class Token {
	
	String id;
	TokenType TT;
	
	public String getId() {
		return id;
	}

	public TokenType getTT() {
		return TT;
	}

	public Token(String i){
		id = i;
	}
	public Token(TokenType T){
		this.TT = T;
	}
	

	public Token(String i, TokenType T) {
		id=i;
		TT=T;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Token [id={" + id + "}, TT=" + TT + "]";
	}
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	public boolean isType(TokenType t)
	{
		return this.getTT() == t;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o){
		Token t = (Token)o;
		return (this.getId().equals(t.getId()) && this.getTT() == t.getTT()); 
	}
	
}
