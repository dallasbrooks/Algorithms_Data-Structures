import edu.princeton.cs.algs4.*;
import java.util.*;
import java.io.File;

public class BaseballElimination{

	/*
	compile: 
		javac -cp .:algs4.jar BaseballElimination.java
	run:
		java -cp .:algs4.jar BaseballElimination <file>.txt
	*/
	
	public ArrayList<String> eliminated = new ArrayList<String>();
	private ST<Integer, SET<String>> holder = new ST<>();
	private int max;
	private ST<String, Integer> teams;
	private int[] wins;
	private int[] remaining;
	private int[][] against;

	/* BaseballElimination(s)
		Given an input stream connected to a collection of baseball division
		standings we determine for each division which teams have been eliminated 
		from the playoffs. For each team in each division we create a flow network
		and determine the maxflow in that network. If the maxflow exceeds the number
		of inter-divisional games between all other teams in the division, the current
		team is eliminated.
	*/
	public BaseballElimination(Scanner s){
		max = s.nextInt();
		teams = new ST<>();
		wins = new int[max];
		remaining = new int[max];
		against = new int[max][max];
		for(int a = 0; a < max; a++){
			teams.put(s.next(), a);
			wins[a] = s.nextInt();
			remaining[a] = s.nextInt();
			for(int b = 0; b < max && s.hasNextInt(); b++){
				against[a][b] = s.nextInt();
			}
		}	
		for(String team : teams()){
			if(isEliminated(team)){
				eliminated.add(team);
			}
		}
		Collections.sort(eliminated);
	}

	public static void main(String[] args){
		Scanner s;
		try{
			s = new Scanner(new File(args[0]));
		}
		catch(java.io.FileNotFoundException e){
			System.out.printf("Unable to open %s\n",args[0]);
			return;
		}
		System.out.printf("Reading input values from %s.\n", args[0]);
		BaseballElimination be = new BaseballElimination(s);		
		if(be.eliminated.size() == 0){
			System.out.println("No teams have been eliminated.");
		}
		else{
			System.out.println("Teams eliminated: " + be.eliminated);
		}
	}

	public Iterable<String> teams(){
		return teams.keys();
	}
	
	public boolean isEliminated(String team){
		computeElimination(teams.get(team));
		return !holder.get(teams.get(team)).isEmpty();
	}

	private void computeElimination(int team){
		if(holder.contains(team)){
			return;
		}
		int best = wins[team] + remaining[team];
		for(String s : teams()){
			if(teams.get(s) != team && best < wins[teams.get(s)]){
				SET<String> hold = new SET<>();
				hold.add(s);
				holder.put(team, hold);
				return;
			}
		}
		minCut(team);
	}

	private void minCut(int team){
		int games = max*(max-1)/2;
		int V = games + max + 2;
		FlowNetwork network = new FlowNetwork(V);
		int count = 1;
		int source = games + 1;
		for(int a = 0; a < max; a++){
			if(a == team){
				continue;
			}
			for(int b = a; b < max; b++){
				if(b == team){
					continue;
				}
				network.addEdge(new FlowEdge(0, count, against[a][b]));
				network.addEdge(new FlowEdge(count, source+a, Integer.MAX_VALUE));
				network.addEdge(new FlowEdge(count, source+b, Integer.MAX_VALUE));
				count++;
			}
		}
		int best = wins[team] + remaining[team];
		for(int a = 0; a < max; a++){
			if(a == team){
				continue;
			}
			network.addEdge(new FlowEdge(source+a, V-1, best-wins[a]));
		}
		FordFulkerson ford = new FordFulkerson(network, 0, V-1);
		SET<String> hold = new SET<>();
		for(String s : teams()){
			if(teams.get(s) == team){
				continue;
			}
			if(ford.inCut(source + teams.get(s))){
				hold.add(s);
			}
		}
		holder.put(team, hold);
	}
}