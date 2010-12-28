import java.util.*;


public class Test {
	
	public static void main(String[] args) throws Exception {
		NFA nfaOne = new NFA();
		NFA nfaTwo = new NFA();
		
		nfaOne.makeNFAWhichAcceptsThisCharacter('a',1);
		nfaTwo.makeNFAWhichAcceptsThisCharacter('b',2);
		nfaOne.Concatination(nfaTwo);
	
		nfaOne.Plus();
		
		

		DFA dfa = nfaOne.generateDFA();
		
//		RegexResult rr = new RegexResult("text.in", dfa);
//		System.out.println(rr);

//		String str = "ababababbbbbbbabaaaaaaaa";
//		int[]array = dfa.maxMatchStringIndexAndLength(str,0);
//
//		while (array[0]!=-1){
//			System.out.println("index: "+array[0]);
//			System.out.println("length: "+array[1]);
//			if (array[1] >= 0)System.out.println("string: "+str.substring(array[0],array[0]+array[1]));
//			array = dfa.maxMatchStringIndexAndLength(str,array[0]+array[1]+1);
//
//			if(array[0]==0){
//				break;
//			}
//		}
//		System.out.println("index: "+array[0]);
//		System.out.println("length: "+array[1]);
//		if (array[1] >= 0)System.out.println("string: "+str.substring(array[0],array[0]+array[1]));
		
		
		
		
		
	}
	
	public static void main2(String[] args) {
		NFA nfaOne = new NFA();
		NFA nfaTwo = new NFA();
		
		nfaOne.makeNFAWhichAcceptsThisCharacter('a',1);
		nfaTwo.makeNFAWhichAcceptsThisCharacter('b',2);
		

		System.out.println("*****************************NFA BEFORE NFA ONE********************************");
		System.out.println(nfaOne);
		System.out.println("*****************************NFA BEFORE NFA ONE********************************");
		
		System.out.println("*****************************NFA BEFORE NFA TWO********************************");
		System.out.println(nfaTwo);
		System.out.println("*****************************NFA BEFORE NFA TWO********************************");
		
		
		
		nfaOne.Concatination(nfaTwo);
		nfaOne.Plus();
		
		System.out.println("*****************************NFA AFTER********************************");
		System.out.println(nfaOne);
		System.out.println("*****************************NFA AFTER********************************");
		
		DFA dfa = nfaOne.generateDFA();
		System.out.println("*****************************DFA********************************");
		System.out.println(dfa);
		System.out.println("*****************************DFA********************************");
		
		
		String str = "aaaaabababababbbbbbbbbbbb";
		int[]array = dfa.maxMatchStringIndexAndLength(str);
		System.out.println("index: "+array[0]);
		System.out.println("length: "+array[1]);
		if (array[1] >= 0)System.out.println("string: "+str.substring(array[0],array[0]+array[1]));
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
