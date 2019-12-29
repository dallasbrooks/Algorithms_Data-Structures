import java.util.*;
import java.io.File;

public class RedBlackBST{

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;
    private double redCount;

    private class Node{

        private int key;//key
        private Node left;//link to left subtree
        private Node right;//links to right subtree
        private boolean color;//color of parent link
        private int size;//subtree count

        public Node(int key, boolean color, int size){
            this.key = key;
            this.color = color;
            this.size = size;
        }
    }

	public RedBlackBST(){
        this.redCount = 0;
	}

    private boolean isRed(Node x){
        if(x == null){
            return false;
        }
        return x.color == RED;
    }

    public int size(){
        return size(root);
    }

    private int size(Node x){
        if(x == null){
            return 0;
        }
        return x.size;
    } 

    public boolean isEmpty(){
        return root == null;
    }

    public void put(int key){
        root = put(root, key);
        if(isRed(root)){
            root.color = BLACK;
            redCount--;  
        }
    }

    //insert the key-value pair in the subtree rooted at h
    private Node put(Node h, int key){
        if(h == null){
            redCount++;
            return new Node(key, RED, 1);
        }
        int cmp = key - h.key;
        if(cmp < 0){
            h.left  = put(h.left,  key); 
        }
        else if(cmp > 0){
            h.right = put(h.right, key); 
        }
        else{
            h.key = key;
        }
        //fix-up any right-leaning links
        if(isRed(h.right) && !isRed(h.left)){
            h = rotateLeft(h);
        }
        if(isRed(h.left) && isRed(h.left.left)){
            h = rotateRight(h);
        }
        if(isRed(h.left) && isRed(h.right)){
            flipColors(h);
            redCount--;
        }
        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    //make a right-leaning link lean to the left
    private Node rotateLeft(Node h){
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    //make a left-leaning link lean to the right
    private Node rotateRight(Node h){
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    //switch colours of children and parent
    private void flipColors(Node h){
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    //calculate precentage of red nodes in the tree
    public double percentRed(){
        if(isEmpty()){
            return 0;
        }
        return redCount/root.size*100;
    }

    public static void main(String[] args){ 
        RedBlackBST RBTree = new RedBlackBST();
        if(args.length > 0){
            try{
                Scanner input = new Scanner(new File(args[0]));
                for(int a = 0; input.hasNextInt(); a++){
                    RBTree.put(input.nextInt());
                }
                System.out.println("Reading input values from " + args[0]);
                System.out.printf("Percent of Red Nodes: %.6f\n", RBTree.percentRed());
            }catch(java.io.FileNotFoundException e){
                System.out.printf("Failure: Unable to open %s\n", args[0]);
                return;
            }
        }
        else{
            //Code below is used for no given file
            //100 iterations of 10^4, 10^5, and 10^6 random integers
            Random rand = new Random();
            for(int a = 1; a <= 100; a++){
                System.out.println("\nIteration " + a + ":\n");
                RBTree = new RedBlackBST();
                for(int b = 0; b < 10000; b++){
                    RBTree.put(rand.nextInt(10001));
                }
                System.out.printf("Percent of Red Nodes for 10^4: %.6f\n", RBTree.percentRed());
                RBTree = new RedBlackBST();
                for(int b = 0; b < 100000; b++){
                    RBTree.put(rand.nextInt(100001));
                }
                System.out.printf("Percent of Red Nodes for 10^5: %.6f\n", RBTree.percentRed());
                RBTree = new RedBlackBST();
                for(int b = 0; b < 1000000; b++){
                    RBTree.put(rand.nextInt(1000001));
                }
                System.out.printf("Percent of Red Nodes for 10^6: %.6f\n", RBTree.percentRed());
            }
        }
    }
}