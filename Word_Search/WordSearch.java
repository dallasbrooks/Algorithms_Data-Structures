import java.util.*;
import java.io.File;

public class WordSearch{

	public int startRow;//Starting row of word in puzzle
	public int startCol;//Starting column of word in puzzle
	public int endRow;//Ending row of word in puzzle
	public int endCol;//Ending column of word in puzzle
	public char[] pattern;//word to search for
	public int[][] dfa;//dfa for KMP algorithm

	/* WordSearch(word)
		Use the WordSearch construcutor to do any preprocessing
		of the search word that may be needed. Examples, the dfa[][]
		for KMP, the right[] for Boyer-Moore, the patHash for Rabin-
		Karp.
	*/
    public WordSearch(char[] word){
    	pattern = new char[word.length];
    	dfa = new int[256][word.length];
    	dfa[word[0]][0] = 1;
    	int x = 0;
    	for(int a = 0; a < word.length; a++){
    		pattern[a] = word[a];
    		if(a == 0){
    			continue;
    		}
    		for(int b = 0; b < 256; b++){
    			dfa[b][a] = dfa[b][x];
    		}
    		dfa[word[a]][a] = a+1;
    		x = dfa[word[a]][x];
    	}
    }

    public boolean check(char[][] puzzle, Coordinates n, String dir){
    	int r = n.row;
    	int c = n.col;
    	int a = 0;
    	while(r >= 0 && r < puzzle.length && c >= 0 && c < puzzle[0].length && a < pattern.length){
    		a = dfa[puzzle[r][c]][a];
    		if(a == 1){
    			startRow = r;
    			startCol = c;
    		}
    		if(a == pattern.length){
    			endRow = r;
    			endCol = c;
    			return true;
    		}
    		if(dir == "N"){
    			c--;
    		}
    		else if(dir == "NE"){
    			r++;
    			c--;
    		}
    		else if(dir == "E"){
    			r++;
    		}
    		else if(dir == "SE"){
    			r++;
    			c++;
    		}
    		else if(dir == "S"){
    			c++;
    		}
    		else if(dir == "SW"){
    			r--;
    			c++;
    		}
    		else if(dir == "W"){
    			r--;
    		}
    		else if(dir == "NW"){
    			r--;
    			c--;
    		}
    	}
  		return false;
    }

	/* search(puzzle)
		Once you have preprocessed the word you need to search the
		puzzle for the word. That happens here. You will assign the
		given global variables here once you find the word and return
		the boolean value "true". If you can't find the word, return 
		"false"
	*/
    public boolean search(char[][] puzzle){
		LinkedList<Coordinates> ll = new LinkedList<Coordinates>();
		for(int a = 0; a < puzzle.length; a++){	
			for(int b = 0; b < puzzle[0].length; b++){
				if(puzzle[a][b] == pattern[0]){
					ll.add(new Coordinates(a, b));
				}
			}
		}
		String[] dir = new String[]{"N","NE","E","SE","S","SW","W","NW"};
		while(!ll.isEmpty()){
			Coordinates n = ll.remove();
			for(int a = 0; a < 8; a++){
				if(check(puzzle, n, dir[a])){
					return true;
				}
			}
		}
		return false;
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
		int n = s.nextInt();
		String wordAsString = s.next();
		char[] word = wordAsString.toCharArray();
		char[][] puzzle = new char[n][n];
		for(int a = 0; a < n; a++){
			String line = s.next();
			for(int b = 0; b < n; b++){
				puzzle[a][b] = line.charAt(b);
			}
		}
		WordSearch searcher = new WordSearch(word);//Preprocess word
        boolean result = searcher.search(puzzle);//Search for word in puzzle
		System.out.println("Word: " + wordAsString);
		System.out.printf("\nPuzzle:\t\n");
		for(int a = 0; a < n; a++){
			for(int b = 0; b < n; b++){
				 System.out.print(puzzle[a][b]);
			}
			System.out.println();
		}
		if(!result){
			System.out.printf("\nSolution: Search word not found\n");
		}
		else{
			int x1 = searcher.startRow;
			int y1 = searcher.startCol;
			int x2 = searcher.endRow;
			int y2 = searcher.endCol;
			System.out.printf("\nSolution: Search word starts at coordinate ");
			System.out.print("("+x1+","+y1+")");
			System.out.print(" and ends at coordinate ");
			System.out.print("("+x2+","+y2+")\n");
		}
    }
}

class Coordinates{

	int row;
	int col;

	public Coordinates(int r, int c){
		this.row = r;
		this.col = c;
	}
}