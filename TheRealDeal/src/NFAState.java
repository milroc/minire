import java.util.*;

public class NFAState {
	public static final char epsilonTransition = 0;
	
	public String name;
	
	public StateType type; 
	public HashMap<Character,ArrayList<NFAState>> validTransitions;
	
	public NFAState(String name, StateType type) {
		super();
		this.name = name;
		this.type = type;
		this.validTransitions = new HashMap<Character, ArrayList<NFAState>>();
	}

	public ArrayList<NFAState> getNextStates(char c){
		
		ArrayList<NFAState> nextStates = new ArrayList<NFAState>();
		addUniqueStates(nextStates,this.validTransitions.get(c));

		ArrayList<NFAState> epsilonTransitions = new ArrayList<NFAState>();
		boolean anyStateAdded = false;
		do {
			epsilonTransitions = getEpsilonTransitions(nextStates);
			anyStateAdded = addUniqueStates(nextStates, epsilonTransitions);
		}
		while (epsilonTransitions.size() > 0 && anyStateAdded);		
		
		return nextStates;
	}
	
	public ArrayList<NFAState> getEpsilonTransitions(ArrayList<NFAState> state){
		ArrayList<NFAState> transitions = new ArrayList<NFAState>();
		for (NFAState s : state){
			if (s.validTransitions.containsKey(epsilonTransition)){
				addUniqueStates(transitions, s.validTransitions.get(epsilonTransition));
			}
		}
		return transitions;
	}
	
	public boolean addUniqueStates(ArrayList<NFAState> to, ArrayList<NFAState> from){
		boolean anyStateAdded = false;
		if (from == null) return false;
		for (NFAState state: from){
			if (!to.contains(state)){
				to.add(state);
				anyStateAdded = true;
			}
		}
		return anyStateAdded;
	}

	public void addTransition(char c, NFAState s){
		if (!this.validTransitions.containsKey(c)){
			this.validTransitions.put(c, new ArrayList<NFAState>());
		}
		this.validTransitions.get(c).add(s);
		
	}
	public void removeTransition(char c, NFAState s){
		this.validTransitions.get(c).remove(s);
	}
	
	public String getNames(ArrayList<NFAState> nfaStates){
		String ret = "";
		for (NFAState s : nfaStates){
			ret+=s.name+",";
		}
		if (ret.length()>0) ret = ret.substring(0,ret.length()-1);
		return ret;
	}

	public String toString(){
		String ret = "";
		ret+="\n************************************************";
		ret+="\nName: "+this.name;
		ret+="\nType: "+this.type;
		for (char c : this.validTransitions.keySet()){
			String transitions = "";
			for (NFAState s : getNextStates(c)){
				transitions = transitions + s.name + ",";
			}
			ret+="\nAt character: "+c+" transition to: "+transitions.substring(0,transitions.length()-1);
		}
		ret+="\n************************************************";
		return ret;
	}
	
}
