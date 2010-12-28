import java.util.ArrayList;


public class FakeScanner {
	int x;
	ArrayList<String> al;
	public FakeScanner(){
		x = 0;
		al = new ArrayList<String>();
	}
	public String peekToken(){
		return al.get(x);
	}
	public void matchToken(String val, boolean real){
//		if (val.eq)
	}
}
