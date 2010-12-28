import java.util.*;

/**
 * @author Irfan Somani
 */
public class DFA {
	public ArrayList<DFAState> states;
	public int regularStatesCount;
	public int finalStatesCount;
	public int startStatesCount;
	
	public DFA(){
		super();
		states = new ArrayList<DFAState>();
		regularStatesCount = 0;
		finalStatesCount = 0;
		startStatesCount = 0;
	}
	public void addState(DFAState s){
		states.add(s);
		if (s.type == StateType.FINAL) finalStatesCount++;
		if (s.type == StateType.REGULAR) regularStatesCount++;
		if (s.type == StateType.START) startStatesCount++;
		if (s.type == StateType.START_FINAL){
			startStatesCount++;
			finalStatesCount++;
		}
	}
	public DFAState stateExists(ArrayList<NFAState> correspondingNFAStates){
		for (DFAState dfaState : this.states){
			if (dfaState.correspondingNFAStates.size() != correspondingNFAStates.size()) continue;
			if (dfaState.correspondingNFAStates.containsAll(correspondingNFAStates)) return dfaState;
		}
		return null;
	}
	
	
	public int maxMatchStringLength(String str){
		int maxMatchLength = -1;
		DFAState current = getStartState();
		if (current.type == StateType.START_FINAL){
			maxMatchLength = 0;
		}
		for (int x=0; x<str.length(); x++){
			char currentChar = str.charAt(x);
			current = current.getNextState(currentChar);
			if (current==null) break;
			if (current.type == StateType.FINAL || current.type == StateType.START_FINAL){
				maxMatchLength = x + 1;
			}
		}
		return maxMatchLength;
	}
	
	//int[0] = index
	//int[1] = length
	public int[] maxMatchStringIndexAndLength(String str){
		int maxMatchIndex = -1;
		int maxMatchLength = -1;
		
		if (maxMatchStringLength("") == 0){
			maxMatchIndex = 0;
			maxMatchLength = 0;
		}
		
		for (int x=0; x<str.length(); x++){
			int iMaxMatchStringLength = maxMatchStringLength(str.substring(x));
			if (iMaxMatchStringLength > maxMatchLength){
				maxMatchLength = iMaxMatchStringLength;
				maxMatchIndex = x;
			}
		}
		int[]array = {maxMatchIndex,maxMatchLength};
		return (array);
	}
	
	//int[0] = index
	//int[1] = length
	public int[] maxMatchStringIndexAndLength(String str, int start){
		int maxMatchIndex = -1;
		int maxMatchLength = -1;
		
		if (maxMatchStringLength("") == 0){
			maxMatchIndex = 0;
			maxMatchLength = 0;
		}
		
		for (int x=start; x<str.length(); x++){
			int iMaxMatchStringLength = this.maxMatchStringLength(str.substring(x));
			if (iMaxMatchStringLength > maxMatchLength){
				maxMatchLength = iMaxMatchStringLength;
				maxMatchIndex = x;
			}
		}
		int[]array = {maxMatchIndex,maxMatchLength};
		return (array);
	}
	
	
	
	public DFAState getStartState(){
		for (DFAState s : this.states){
			if (s.type == StateType.START || s.type == StateType.START_FINAL){
				return s;
			}
		}
		return null;
	}
	
	public String toString(){
		String ret = "";
		for (DFAState s : this.states){
			ret+=s.toString();
		}
		return ret;
	}
	
	
}
