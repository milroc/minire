import java.util.*;


public class RegexScanner {
	Queue<Character> queue;
	
	public RegexScanner(String str)
	{
		queue = new LinkedList<Character>();
		for (byte b : str.getBytes()){
			queue.add((char)b);
		}
	}
	public char getChar(){
		if (queue.size()==0){
			return '\0';
		}
		return queue.poll();
	}
	public char peekChar(){
		if (queue.size()==0){
			return '\0';
		}
		return queue.peek();
	}
	
}
