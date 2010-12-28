import java.util.*;
public class Test2 {
	

	public static void main(String[] args) throws Exception{
		MiniREScanner scan = new MiniREScanner("file1.in");
		
		int x = 10;
		while (x>0){
			System.out.println("Peek: "+scan.peekToken());
			System.out.println(scan.getToken());
			System.out.println();
			x--;
		}
		
		
		
		
		
		
		
		
	}
	
//	public static void main(String[] args) {
//		String str = "hello world";
//		for (byte b : str.getBytes()){
//			System.out.println((char)b);
//		}
//	}
}
