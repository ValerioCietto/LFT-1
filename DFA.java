
import java.util.HashSet;
import java.util.HashMap;

/**
 * Un oggetto della classe DFA rappresenta un automa a stati finiti
 * deterministico
 */
public class DFA
{
    /** 
     * Numero degli stati dell'automa. Ogni stato e` rappresentato da
     * un numero interno non negativo, lo stato con indice 0 e` lo
     * stato iniziale.
     */
    private int numberOfStates;

    /** Insieme degli stati finali dell'automa. */
    private HashSet<Integer> finalStates;

    /**
     * Funzione di transizione dell'automa, rappresentata come una
     * mappa da mosse a stati di arrivo.
     */
    private HashMap<Move, Integer> transitions;

    /**
     * Crea un DFA con un dato numero di stati.
     * @param  n Il numero di stati dell'automa.
     */
    public DFA(int n) {
	numberOfStates = n;
	finalStates = new HashSet<Integer>();
	transitions = new HashMap<Move, Integer>();
    }

    /**
     * Aggiunge uno stato all'automa.
     * @return L'indice del nuovo stato creato
     */
    public int newState() {
	return numberOfStates++;
    }

    /**
     * Aggiunge una transizione all'automa.
     * @param  p  Lo stato di partenza della transizione.
     * @param  ch Il simbolo che etichetta la transizione.
     * @param  q  Lo stato di arrivo della transizione.
     * @return <code>true</code> se lo stato di partenza e lo stato di
     *         arrivo sono validi, <code>false</code> altrimenti.
     */
    public boolean setMove(int p, char ch, int q) {
	if (!validState(p) || !validState(q))
	    return false;

	transitions.put(new Move(p, ch), q);
	return true;
    }

    /**
     * Aggiunge uno stato finale.
     * @param  p Lo stato che si vuole aggiungere a quelli finali.
     * @return <code>true</code> se lo stato e` valido,
     *         <code>false</code> altrimenti.
     */
    public boolean addFinalState(int p) {
	if (validState(p)) {
	    finalStates.add(p);
	    return true;
	} else
	    return false;
    }

    /**
     * Determina se uno stato e` valido oppure no.
     * @param  p Lo stato da controllare.
     * @return <code>true</code> se lo stato e` valido,
     *         <code>false</code> altrimenti.
     * @see #numberOfStates
     */
    public boolean validState(int p) {
	return (p >= 0 && p < numberOfStates);
    }

    /**
     * Determina se uno stato e` finale oppure no.
     * @param  p Lo stato da controllare.
     * @return <code>true</code> se lo stato e` finale,
     *         <code>false</code> altrimenti.
     * @see #finalStates
     */
    public boolean finalState(int p) {
	return finalStates.contains(p);
    }

    /**
     * Restituisce il numero di stati dell'automa.
     * @return Numero di stati.
     */
    public int numberOfStates() {
	return numberOfStates;
    }

    /**
     * Restituisce l'alfabeto dell'automa, ovvero l'insieme di simboli
     * che compaiono come etichette delle transizioni dell'automa.
     * @return L'alfabeto dell'automa.
     */
    public HashSet<Character> alphabet() {
	HashSet<Character> alphabet = new HashSet<Character>();
	for (Move m : transitions.keySet())
	    alphabet.add(m.ch);
	return alphabet;
    }

    /**
     * Esegue una mossa dell'automa.
     * @param p  Stato di partenza prima della transizione.
     * @param ch Simbolo da riconoscere.
     * @return Stato di arrivo dopo la transizione, oppure
     *         <code>-1</code> se l'automa non ha una transizione
     *         etichettata con <code>ch</code> dallo stato
     *         <code>p</code>.
     */
    public int move(int p, char ch) {
	Move move = new Move(p, ch);
	if (transitions.containsKey(move))
	    return transitions.get(move);
	else
	    return -1;
    }

    /**
     * Verifica se una stringa e` riconosciuta dall'automa.
     * @param  s  Stringa da riconoscere.
     * @return <code>true</code> se la stringa e` stata riconosciuta,
     *         <code>false</code> altrimenti.
     */
    public boolean scan(String s) {//2.2
		int state = 0;
		int i = 0;
		while(validState(state) && i < s.length())
			state=move(state,s.charAt( i++));
		return finalState(state);
    }
    
    /**
     * Verifica se un'automa è completo
     * @return true se ogni stato e ogni termine, genera un produttore
     * 			false se Move(q,c)=-1 per un qualsiasi q o c 
     */
    public boolean complete(){//2.4
		HashSet<Character> alfabeto=alphabet();
		for (int s=0;s<numberOfStates;s++)
			for (Character i : alfabeto)
				if(!validState(move(s,i)))
					return false;
		return true;
	}

    /**
     * Stampa una rappresentazione testuale dell'automa da
     * visualizzare con <a href="http://www.graphviz.org">GraphViz</a>.
     * @param name Nome dell'automa.
     */
    public void toDOT(String name) {//2.5
		String input="digraph "+name+" {\n";
		input+="\trankdir=LR;\n";
		input+="\tnode [shape = doublecircle];\n";
		for(int i: finalStates)
			input+="\tq"+i+";\n";
		input+="\tnode [shape = circle];\n";
		HashSet<Character> alfabeto=alphabet();
		for (int s=0;s<numberOfStates;s++)
			for (Character c : alfabeto)
				if(validState(move(s,c)))
					input+="\tq"+s+" -> q"+move(s,c)+" [ label = \""+c+"\" ];\n";
		
		input+="}";
		System.out.println(input);
    }

    /**
     * Stampa una classe Java con un metodo <code>scan</code> che implementa 
     * l'automa.
     * @param name Nome della classe da generare.
     */
    public String toJava(String name) { //2.7 opzionale
		String out="public class "+ name+"{\n";
		out+="\tpublic static boolean scan(String s){\n";
		out+="\t\tint state = 0;\n";
		out+="\t\tint i = 0;\n";
		out+="\t\twhile (state >= 0 && i < s.length()) {\n";
		out+="\t\t\tfinal char ch = s.charAt(i++);\n";
		out+="\t\t\tswitch (state) {\n";
		HashSet<Character> alfabeto=alphabet();
		for (int s=0;s<numberOfStates;s++){
			out+="\t\t\t\tcase "+s+":\n";
			out+="\t\t\t\t\t";
			for (Character c : alfabeto){
				if(validState(move(s,c))){
					out+="if(ch=="+c+") state = "+move(s,c)+";\n";
					out+="\t\t\t\t\telse";
				}
			}
			out+=" state = -1;\n";
			out+="\t\t\t\t\tbreak;\n";
		}
		out+="\t\t\t}\n";
		out+="\t\t}\n";
		String t="";
		for(int s=0;s<numberOfStates;s++)
			if(finalState(s)){
				if(!t.equals(""))
					t+=" || ";
				t+="state=="+s;
			}
		out+="\t\treturn "+t+";\n";
		out+="\t}\n";
		out+="}\n";
		return out;
    }

	/**
	 * restituisce gli stati raggiungibili
	 */
	public HashSet<Integer> reach(){//3.1
		return reach(0);
	}
	public HashSet<Integer> reach(int q){//3.1
		boolean  r []=new boolean[numberOfStates];
		boolean  v []=new boolean[numberOfStates];
		for(boolean i:r)
			i=false;//raggiunto
		for(boolean i:v)
			i=false;//raggiunto
		//se stesso è raggiungibile
		r[q]=true;
		//fintanto che raggiunge nuovi stati
		boolean flag=true;
		while (flag){
			flag=false;
			//per ogni stato raggiungibile
			for(int i=0;i<numberOfStates;i++)
				if(r[i] && !v[i]){
					v[i]=true;
					HashSet <Integer> ragg=qConnect(i);
					for(Integer j:ragg)
						if(!r[j]){
							r[j]=true;
							flag=true;
						}
				}
		}
		//add true=raggiunti
		HashSet<Integer> reach=new HashSet<Integer>();
		for(int i=0;i<numberOfStates;i++)
			if(r[i])
				reach.add(i);
		return reach;
	}
	public HashSet<Integer> qConnect(int q0){
		HashSet <Integer> t =new HashSet <Integer>();
		for(Character c:alphabet()){
			int q1=move(q0,c);
			if(validState(q1))
				t.add(q1);			
		}
		return t;
	}
	public HashSet<Character> cConnect(int q0){
		HashSet <Character> t =new HashSet <Character>();
		for(Character c:alphabet()){
			if(validState(move(q0,c)))
				t.add(c);			
		}
		return t;
	}
	
	/**
	 * automa riconosce il linguaggio vuoto
	 * @return true automa riconosce il linguaggio vuoto
	 * 			false vuoto non riconosciuto
	 */
	public boolean empty(){//3.1
		for(Integer s:reach())
			if(finalState(s))
				return false;
		return true;
	}
	
	/**
	 * restituisce gli stati raggiungibili non hanno mosse in uscita
	 * @return HashSet<Integer> stati pozzo 
	 */
	public HashSet<Integer> sink(){//3.1
		return sink(0);
	}
	public HashSet<Integer> sink(int q){//3.1
		boolean  r []=new boolean[numberOfStates];
		//setto true i possibili pozzi, false i non raggiungibili
		for(boolean i:r)
			i=false;
		for(Integer i:reach(q))
			r[i]=true;
		//per ogni stato
		for(Integer i:reach(q))	
			for(Character c:alphabet())
				//se considerato possibile pozzo ma ha una mossa valida
				if(r[i] && validState(move(i,c)) && move(i,c)!=i)
					r[i]=false;//non pozzo
		//add true=pozzi
		HashSet<Integer> pozzi=new HashSet<Integer>();
		for(int i=0;i<numberOfStates;i++)
			if(r[i])
				pozzi.add(i);
		return pozzi;
	}

	public HashSet<String> sample(int q,int max){
		HashSet<String> sample=new HashSet<String>();
		
		Character []c=cConnect(0).toArray();
			
		
	}
}
