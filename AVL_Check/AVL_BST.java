public class AVL_BST{

	public static boolean checkAVL(BST b){
		if(balance(b.root) >= 0){
			return true;
		}
		return false;
	}

	private static int balance(TreeNode root){
		if(root == null){
			return 0;
		}
		int leftTree = balance(root.left);
		int rightTree = balance(root.right);
		if(leftTree == -1 || rightTree == -1){
			return -1;
		}
		if(Math.abs(leftTree - rightTree) > 1){
			return -1;
		}
		return Math.max(leftTree, rightTree) + 1;
	}
	
	public static BST createBST(int[] a){
		BST node = new BST();
		for(int index = 0; index < a.length; index++){
			node.add(a[index]);
		}
		return node;
	}

	public static void main(String[] args){
		int[][] test = {
			{6,2,1,4,3,5,13,11,12,9,10,8,14,15},//true
			{5,2,8,6,1,9,52,3},//true
			{82,85,153,195,124,66,200,193,185,243,73,153,76},//false
			{5,3,7,1},//true
			{5,1,98,100,-3,-5,55,3,56,50},//true
			{297,619,279,458,324,122,505,549,83,186,131,71},//false
			{78},//true
		};
		for(int a = 0; a < test.length; a++){
			BST tree = createBST(test[a]);
			System.out.println("Tree "+(a+1)+" is AVL: "+checkAVL(tree));
		}
	}
}

class TreeNode{

	int value;
	TreeNode left;
	TreeNode right;

	TreeNode(int value){
		this.value = value;
		this.left = null;
		this.right = null;
	}
}

class BST{

	TreeNode root;

	public BST(){
		this.root = root;
	}

	public void add(int value){
		TreeNode node = root;
		if(node == null){
			root = new TreeNode(value);
			return;
		}
		while(true){
			if(value < node.value){
				if(node.left == null){
					node.left = new TreeNode(value);
					break;
				}
				node = node.left;
			}
			else{
				if(node.right == null){
					node.right = new TreeNode(value);
					break;
				}
				node = node.right;
			}
		}
	}
}