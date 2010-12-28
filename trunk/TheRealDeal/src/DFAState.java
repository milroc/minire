import java.util.*;

/**
 * 
 * @author Irfan Somani
 *
 */
public class DFAState {
	public String name;
	public StateType type; 
	private HashMap<Character,DFAState> validTransitions;
	public ArrayList<NFAState> correspondingNFAStates;
	
	public DFAState(String name, StateType type) {
		super();
		this.name = name;
		this.type = type;
		this.validTransitions = new HashMap<Character, DFAState>();
		this.correspondingNFAStates = new ArrayList<NFAState>();
	}
	
	public void addToCorrespondingNFAState(NFAState s){
		if (!this.correspondingNFAStates.contains(s)){
			this.correspondingNFAStates.add(s);
		}
	}
	
	public String getNames(){
		String ret = "";
		for (NFAState s : this.correspondingNFAStates){
			ret = ret + s.name + ",";
		}
		if (ret.length() > 0) ret = ret.substring(0,ret.length()-1);
		return ret;
	}
	
	public DFAState getNextState(char c){
		return this.validTransitions.get(c);
	}
	public void addTransition(char c, DFAState s){
		this.validTransitions.put(c, s);
	}
	public void removeTransition(char c){
		this.validTransitions.remove(c);
	}

	public String toString(){
		String ret = "";
		ret+="\n************************************************";
		ret+="\nName: "+getNames();
		ret+="\nType: "+this.type;
		for (char c : this.validTransitions.keySet()){
			ret+="\nAt character: "+c+" transition to: "+this.validTransitions.get(c).getNames();
		}
		ret+="\n************************************************";
		return ret;
	}
	
}
