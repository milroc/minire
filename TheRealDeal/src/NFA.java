import java.util.*;
public class NFA {
	
	private ArrayList<NFAState> states;
	public NFA(){
		super();
		states = new ArrayList<NFAState>();
	}
	
	public void addState(NFAState s){
		this.states.add(s);
	}
	public DFA generateDFA(){
		DFA dfa = new DFA();
		DFAState startState = new DFAState("S:"+dfa.startStatesCount,StateType.START);
		ArrayList<NFAState> startStates = getStartStates();
		for (NFAState s : getStartStates()){
			startState.addToCorrespondingNFAState(s);
			addUniqueStates(startStates, s.getNextStates(NFAState.epsilonTransition));
			if (s.type == StateType.FINAL || s.type == StateType.START_FINAL){
				startState.name = "S_F:"+dfa.finalStatesCount;
				startState.type = StateType.START_FINAL;
			}
		}
		for (NFAState s : startStates){
			startState.addToCorrespondingNFAState(s);
			if (s.type == StateType.FINAL || s.type == StateType.START_FINAL){
				startState.name = "S_F:"+dfa.finalStatesCount;
				startState.type = StateType.START_FINAL;
			}
		}
		dfa.addState(startState);
		
		for (int x=0; x<dfa.states.size(); x++){
			DFAState currentDFAState = dfa.states.get(x);
			
			for (char c=32; c<=126; c++){
				ArrayList<NFAState> nextStates = new ArrayList<NFAState>();
				for (int y=0; y<currentDFAState.correspondingNFAStates.size(); y++){
					NFAState currentNFAState = currentDFAState.correspondingNFAStates.get(y);
					addUniqueStates(nextStates,currentNFAState.getNextStates(c));
				}
				if (nextStates.size()==0) continue;
				
				DFAState preExistState = dfa.stateExists(nextStates);
				if (preExistState==null){
					DFAState newCurrent = new DFAState("R:"+dfa.regularStatesCount,StateType.REGULAR);
					for (NFAState s : nextStates){
						newCurrent.addToCorrespondingNFAState(s);
						if (s.type == StateType.FINAL){
							newCurrent.name = "F:"+dfa.finalStatesCount;
							newCurrent.type = StateType.FINAL;
						}
					}
					dfa.addState(newCurrent);
					currentDFAState.addTransition(c, newCurrent);
				}
				else {
					currentDFAState.addTransition(c, preExistState);
				}
			}	
		}
		return dfa;
	}


	public void addUniqueStates(ArrayList<NFAState> to, ArrayList<NFAState> from){
		for (NFAState state: from){
			if (!to.contains(state)){
				to.add(state);
			}
		}
	}
	
	//I REALIZED LATER THAT THERE COULD ONLY BE ONE START STATE
	public ArrayList<NFAState> getStartStates(){
		ArrayList<NFAState> states = new ArrayList<NFAState>();
		for (NFAState s : this.states){
			if (s.type == StateType.START || s.type == StateType.START_FINAL){
				states.add(s);
			}
		}
		return states;
	}
	
	
	public void Union(NFA other){
		NFAState startOne = this.getStartStates().get(0);
		if (startOne.type == StateType.START){
			startOne.type = StateType.REGULAR;
		}
		else { //START_FINAL
			startOne.type = StateType.FINAL;
		}
		NFAState startTwo = other.getStartStates().get(0);
		if (startTwo.type == StateType.START){
			startTwo.type = StateType.REGULAR;
		}
		else { //START_FINAL
			startTwo.type = StateType.FINAL;
		}
		
		NFAState newStart = new NFAState("NewStart",StateType.START);
		newStart.addTransition(NFAState.epsilonTransition, startOne);
		newStart.addTransition(NFAState.epsilonTransition, startTwo);
		this.states.add(newStart);
		
		this.states.addAll(other.states);
	}
	public void Concatination(NFA other){
		NFAState startTwo = other.getStartStates().get(0);
		for (NFAState s : this.states){
			if (s.type == StateType.FINAL){
				s.type = StateType.REGULAR;
				s.addTransition(NFAState.epsilonTransition, startTwo);
			}
			if (s.type == StateType.START_FINAL){
				s.type = StateType.START;
				s.addTransition(NFAState.epsilonTransition, startTwo);
			}
		}
		
		for (NFAState s : other.states){
			if (s.type == StateType.START){
				s.type = StateType.REGULAR;
			}
			if (s.type == StateType.START_FINAL){
				s.type = StateType.FINAL;
			}
		}
		
		this.states.addAll(other.states);		
	}
	public void Star(){
		NFAState startOne = this.getStartStates().get(0);
		NFAState newStart = new NFAState("NewStart",StateType.START_FINAL);
		newStart.addTransition(NFAState.epsilonTransition, startOne);
		
		for (NFAState s : this.states){
			if (s.type == StateType.FINAL || s.type == StateType.START_FINAL){
				s.addTransition(NFAState.epsilonTransition, startOne);
			}
			if (s.type == StateType.START){
				s.type = StateType.REGULAR;
			}
			if (s.type == StateType.START_FINAL){
				s.type = StateType.FINAL;
			}
		}
		this.states.add(newStart);
	}
	public void Plus(){
		NFAState startOne = this.getStartStates().get(0);
		for (NFAState s : this.states){
			//IF THE STATE TYPE IS START_FINAL YOU ARE MAKING AN EPSILON TRANITION TO YOURSELF...I DONT THINK THIS MAKES A DIFFERENT
			if (s.type == StateType.FINAL || s.type == StateType.START_FINAL){
				s.addTransition(NFAState.epsilonTransition, startOne);
			}
		}
	}
	public void Question(){
		NFAState startOne = this.getStartStates().get(0);
		NFAState newStart = new NFAState("NewStart",StateType.START_FINAL);
		newStart.addTransition(NFAState.epsilonTransition, startOne);
		
		if (startOne.type == StateType.START){
			startOne.type = StateType.REGULAR;
		}
		if (startOne.type == StateType.START_FINAL){
			startOne.type = StateType.FINAL;
		}
		
		this.states.add(newStart);	
	}
	
	public void makeNFAWhichAcceptsThisCharacter(char c, int num){
		Random gen = new Random();
		this.states = new ArrayList<NFAState>();
//		NFAState startState = new NFAState("S"+num,StateType.START);
//		NFAState finalState = new NFAState("F"+num,StateType.FINAL);
		NFAState startState = new NFAState(""+gen.nextInt(1000),StateType.START);
		NFAState finalState = new NFAState(""+gen.nextInt(1000),StateType.FINAL);
		startState.addTransition(c, finalState);
		this.states.add(startState);
		this.states.add(finalState);
	}

	
	public String toString(){
		String ret = "";
		for (NFAState s : this.states){
			ret+=s.toString();
		}
		return ret;
	}
}
