import java.util.*;
import java.io.File;

public class NinePuzzle{

	public static final int NUM_BOARDS = 362880;//9!
	private static final int[][] goal = {
		{1, 2, 3},
		{4, 5, 6},
		{7, 8, 0},
	};//index 0

	private static void moveTile(int[][] B, int row, int column, int newRow, int newColumn, int vertices, Graph graph){
		int board[][] = new int[3][3];
		for(int a = 0; a < 3; a++){
			board[a] = B[a].clone();
		}
		board[row][column] = board[newRow][newColumn];
		board[newRow][newColumn] = 0;
		graph.addEdge(getIndexFromBoard(board), vertices);
	}

	public static boolean SolveNinePuzzle(int[][] B){
		Graph graph = new Graph(NUM_BOARDS);
		for(int a = 0; a < graph.getVertices(); a++){
			int[][] board = getBoardFromIndex(a);
			int row = -1;
			int column = -1;
			for(int b = 0; b < 3; b++){
				for(int c = 0; c < 3; c++){
					if(board[b][c] == 0){
						row = b;
						column = c;
					}
				}
			}
			if(row > 0){
				moveTile(board, row, column, row-1, column, a, graph);
			}
			if(row < 2){
				moveTile(board, row, column, row+1, column, a, graph);
			}
			if(column > 0){
				moveTile(board, row, column, row, column-1, a, graph);
			}
			if(column < 2){
				moveTile(board, row, column, row, column+1, a, graph);
			}
		}
		int start = getIndexFromBoard(B);
		BFS bfs = new BFS(graph);
		if(bfs.hasPathTo(start)){
			for(int path : bfs.pathTo(start)){
				printBoard(getBoardFromIndex(path));
			}
			return true;
		}
		return false;
	}

	public static void printBoard(int[][] B){
		for(int a = 0; a < 3; a++){
			for(int b = 0; b < 3; b++){
				System.out.printf("%d ",B[a][b]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static int getIndexFromBoard(int[][] B){
		int[] P = new int[9];
		int[] PI = new int[9];
		for(int a = 0; a < 9; a++){
			P[a] = B[a/3][a%3];
			PI[P[a]] = a;
		}
		int s;
		int tmp;
		int id = 0;
		int multiplier = 1;
		for(int a = 9; a > 1; a--){
			s = P[a-1];
			P[a-1] = P[PI[a-1]];
			P[PI[a-1]] = s;
			tmp = PI[s];
			PI[s] = PI[a-1];
			PI[a-1] = tmp;
			id += multiplier*s;
			multiplier *= a;
		}
		return id;
	}
		
	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int tmp;
		for(int a = 0; a < 9; a++){
			P[a] = a;
		}
		for(int a = 9; a > 0; a--){
			tmp = P[a-1];
			P[a-1] = P[id%a];
			P[id%a] = tmp;
			id /= a;
		}
		int[][] B = new int[3][3];
		for(int a = 0; a < 9; a++){
			B[a/3][a%3] = P[a];
		}
		return B;
	}
	
	public static void main(String[] args){
		Scanner s;
		try{
			s = new Scanner(new File(args[0]));
		}catch(java.io.FileNotFoundException e){
			System.out.printf("Unable to open %s\n",args[0]);
			return;
		}
		System.out.printf("Reading input values from %s.\n",args[0]);
		int graphNum = 0;
		double totalTimeSeconds = 0;
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt()){
				break;
			}
			System.out.printf("Reading board %d\n", graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int a = 0; a < 3 && s.hasNextInt(); a++){
				for (int b = 0; b < 3 && s.hasNextInt(); b++){
					B[a][b] = s.nextInt();
					valuesRead++;
				}
			}
			if(valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n", graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n", graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			if(isSolvable){
				System.out.printf("Board %d: Solvable.\n", graphNum);
			}else{
				System.out.printf("Board %d: Not solvable.\n", graphNum);
			}
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}

}

class BFS{

	private int[] edgeTo;
	private boolean[] marked;

	public BFS(Graph graph){
		edgeTo = new int[graph.getVertices()];
		marked = new boolean[graph.getVertices()];
		Queue<Integer> path = new LinkedList<Integer>();
		marked[0] = true;
		path.add(0);
		while(!path.isEmpty()){
			int vertex = path.remove();
			for(int vertex2 : graph.adjacent(vertex)){
				if(!marked[vertex2]){
					edgeTo[vertex2] = vertex;
					marked[vertex2] = true;
					path.add(vertex2);
				}
			}
		}
	}

	public boolean hasPathTo(int vertex){
		return marked[vertex];
	}

	public Iterable<Integer> pathTo(int vertex){
		Stack<Integer> path = new Stack<Integer>();
		for(int a = vertex; a != 0; a = edgeTo[a]){
			path.push(a);
		}
		path.push(0);
		return path;
	}
}

class Graph{

	private final int vertices;
	private LinkedList<Integer>[] adjacent;

	@SuppressWarnings("unchecked")
	public Graph(int vertices){
		this.vertices = vertices;
		adjacent = new LinkedList[vertices];
		for(int a = 0; a < vertices; a++){
			adjacent[a] = new LinkedList<Integer>();
		}
	}

	public int getVertices(){
		return vertices;
	}

	public void addEdge(int vertex, int vertex2){
		adjacent[vertex].add(vertex2);
		adjacent[vertex2].add(vertex);
	}

	public Iterable<Integer> adjacent(int vertex){
		return adjacent[vertex];
	}
}