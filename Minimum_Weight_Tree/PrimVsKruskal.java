import java.util.*;
import java.io.File;

public class PrimVsKruskal{

	private static double[][] prim;
	private static double[][] kruskal;
	
	/* PrimVsKruskal(G)
		Given an adjacency matrix for connected graph G, with no self-loops or parallel edges,
		determine if the minimum spanning tree of G found by Prim's algorithm is equal to 
		the minimum spanning tree of G found by Kruskal's algorithm.
		
		If G[i][j] == 0.0, there is no edge between vertex i and vertex j
		If G[i][j] > 0.0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static boolean PrimVsKruskal(double[][] G){
		int n = G.length;
		prim = new double[n][n];
		kruskal = new double[n][n];
		primMST(G, n);	
		kruskalMST(G, n);
		for(int a = 0; a < n; a++){
			for(int b = a; b < n; b++){
				if(prim[a][b] != kruskal[a][b]){
					return false;
				}
			}
		}
		return true;	
	}
	
	//Prim's algorithm
	static void primMST(double[][] G, int n){
		int[] parent = new int[n];
		double[] weight = new double[n];
		boolean[] visited = new boolean[n];
		for(int a = 0; a < n; a++){
			weight[a] = Double.MAX_VALUE;
		}
		weight[0] = 0;
		parent[0] = 0;
		for(int a = 0; a < n; a++){
			int u = minWeight(weight, visited, n);
			visited[u] = true;
			for(int v = 0; v < n; v++){
				if(G[u][v] != 0 && !visited[v] && G[u][v] < weight[v]){
					parent[v] = u;
					weight[v] = G[u][v];
				}
			}
		}
		for(int u = 0; u < n; u++){
			prim[u][parent[u]] = G[parent[u]][u];
			prim[parent[u]][u] = G[u][parent[u]];
		}
	}

	//used to find minimun weight in the visited nodes
	static int minWeight(double[] weight, boolean[] visited, int n){
		double min = Double.MAX_VALUE;
		int minIndex = -1;
		for(int a = 0; a < n; a++){
			if(!visited[a] && weight[a] < min){
				min = weight[a];
				minIndex = a;
			}
		}
		return minIndex;
	}

	//Kruskal's algorithm
	static void kruskalMST(double[][] G, int n){
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>(n, new EdgeComparator());
		UnionFind uf = new UnionFind(n);
		Edge e;
		for(int a = 0; a < n; a++){
			for(int b = a; b < n; b++){
				if(G[a][b] != 0){
					e = new Edge();
					e.weight = G[a][b];
					e.u = a;
					e.v = b;
					pq.add(e);
				}
			}
		}
		int u;
		int v;
		double weight;
		while(!pq.isEmpty()){
			e = pq.poll();
			weight = e.weight;
			u = e.u;
			v = e.v;
			if(uf.find(u) == uf.find(v)){
				continue;
			}
			uf.union(u, v);
			kruskal[u][v] = weight;
			kruskal[v][u] = weight;
		}
	}
		
   	public static void main(String[] args){
		Scanner s;
		try{
			s = new Scanner(new File(args[0]));
		}
		catch(java.io.FileNotFoundException e){
			System.out.printf("Unable to open %s\n", args[0]);
			return;
		}
		System.out.printf("Reading input values from %s.\n", args[0]);
		int n = s.nextInt();
		double[][] G = new double[n][n];
		int valuesRead = 0;
		for(int a = 0; a < n && s.hasNextDouble(); a++){
			for(int b = 0; b < n && s.hasNextDouble(); b++){
				G[a][b] = s.nextDouble();
				if(a == b && G[a][b] != 0.0) {
					System.out.printf("Adjacency matrix contains self-loops.\n");
					return;
				}
				if(G[a][b] < 0.0){
					System.out.printf("Adjacency matrix contains negative values.\n");
					return;
				}
				if(b < a && G[a][b] != G[b][a]){
					System.out.printf("Adjacency matrix is not symmetric.\n");
					return;
				}
				valuesRead++;
			}
		}
		if(valuesRead < n*n){
			System.out.printf("Adjacency matrix for the graph contains too few values.\n");
			return;
		}
        boolean pvk = PrimVsKruskal(G);
        System.out.printf("Does Prim MST = Kruskal MST? %b\n", pvk);
    }
}

class Edge{

	double weight;
	int u;
	int v;
}


class EdgeComparator implements Comparator<Edge>{

	public int compare(Edge u, Edge v){
		if(u.weight < v.weight){
			return -1;
		}
		if(u.weight > v.weight){
			return 1;
		}
		return 0;
	}
}

class UnionFind{

	private int[] parent;
	private int[] rank;

	public UnionFind(int n){

		parent = new int[n];
		rank = new int[n];
		for(int a = 0; a < n; a++){
			parent[a] = a;
		}
	}

	public int find(int u){
		int v = parent[u];
		if(u == v){
			return u;
		}
		return parent[u] = find(v);
	}

	public void union(int u, int v){
		int r1 = find(u);
		int r2 = find(v);
		if(r1 == r2){
			return;
		}
		if(rank[r1] > rank[r2]){
			parent[r2] = r1;
		}
		else if(rank[r1] < rank[r2]){
			parent[r1] = r2;
		}
		else{
			parent[r2] = r1;
			rank[r1]++;
		}
	}
}