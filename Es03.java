/* Contiene
 * es 3.1 ok DFA.reach DFA.sink DFA.empty
 * es 3.2
 * */
import java.util.HashSet;

public class Es03{
	public static void main ( String [] args ){//3.1
		printDFA("DFA1",DFA1());
		printDFA("DFA2",DFA2());
		//printDFA("3 Zeri Non Consecutivi",DFATreZeriNonConsecutivi());		
	}
	
	//Utility
	public static void printDFA(String s,DFA dfa){
		System.out.println(s);
		System.out.println( "complete: "+dfa.complete());//3.1
		dfa.toDOT(s);
		System.out.println( "reach(q0): "	+dfa.reach());//3.1
		System.out.println( "empty: "		+dfa.empty());//3.1
		System.out.println( "sink(q0): "	+dfa.sink());//3.1
		//System.out.println( "sample(q0): "	+dfa.sample(0,5));//3.1
	}
	public static DFA DFA1 (){
		DFA tz = new DFA(2);
		tz.setMove(0, '0', 0);
		tz.setMove(0, '1', 1);
		tz.setMove(1, '1', 1);
		tz.setMove(1, '0', 1);
		tz.addFinalState(1);
		return tz;
	}
	public static DFA DFA2 (){
		DFA tz = new DFA(3);
		tz.setMove(0, '0', 0);
		tz.setMove(0, '1', 1);
		tz.setMove(1, '1', 1);
		tz.setMove(1, '0', 0);
		tz.setMove(2, '1', 1);
		tz.setMove(2, '0', 0);
		tz.addFinalState(2);
		return tz;
	}
}
