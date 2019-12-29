import java.util.*;

public class median{

	static minHeap min;
	static maxHeap max;
	
	public median(){
		min = new minHeap();
		max = new maxHeap();
	}
	
	public static int calculateMedian(int x){
		if(x < max.peek()){
			max.insert(x);
		}
		else{
			min.insert(x);
		}
		while(max.size() - min.size() > 1){
			min.insert(max.removeMax());
		}
		while(max.size() - min.size() < 1){
			max.insert(min.removeMin());
		}
		return max.peek();
	}
	
	public static void main(String[] args){
		int[][] test = {
			{45,65,55,1,0,52,98,65,32,54,23,-1},//45,55,45,52,52,52
			{4,2,6,0,0,0,0,9,5,6,8,8,8,8,8,8,8,8,8,-1},//4,4,2,0,2,4,5,6,6,8
			{5,6,9,99,8569,45,12,9999,9877,65893,98456,895623,986565,-1},//5,6,9,12,45,99,8569
		};
		for(int a = 0; a < test.length; a++){
			median m = new median();
			System.out.println("Test "+(a+1));
			for(int b = 0; test[a][b] >= 0; b++){
				System.out.println("\tcurrent median: " + m.calculateMedian(test[a][b]));
				b++;
				if(test[a][b] < 0){
					break;
				}
				m.calculateMedian(test[a][b]);
			}
		}
	}
}

class minHeap{

	private int[] heap;
	private int size;
	
	public minHeap(){
		heap = new int[10000];
		size = 0;
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
	
	public int size(){
		return size;
	}
	
	public void insert(int x){
		heap[size] = x;
		bubbleup(size);
		size++;
	}
	
	public void bubbleup(int k){
		if(heap[k] < heap[(k-1)/2]){
			exchange(k, (k-1)/2);
			bubbleup((k-1)/2);
		}
	}

	public void exchange(int i,int j){
		int temp = heap[i];
		heap[i] = heap[j];
		heap[j] = temp;
	}

	public void bubbledown(int k){
		if(2*k+1 < size && heap[k] > heap[2*k+1]){
			exchange(k, 2*k+1);
			bubbledown(2*k+1);
		}
		if(2*k+2 < size && heap[k] > heap[2*k+2]){
			exchange(k, 2*k+2);
			bubbledown(2*k+2);
		}
	}

	public int peek(){
		return heap[0];
	}
	
	public int removeMin(){
		size--;
		int min = heap[0];
		exchange(0, size);
		heap[size] = 0;
		bubbledown(0);
		return min;
	}
}

class maxHeap{

	private int[] heap;
	private int size;
	
	public maxHeap(){
		heap = new int[10000];
		size = 0;
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
	
	public int size(){
		return size;
	}
	
	public void insert(int x){
		heap[size] = x;
		bubbleup(size);
		size++;
	}
	
	public void bubbleup(int k){
		if(heap[k] > heap[(k-1)/2]){
			exchange(k, (k-1)/2);
			bubbleup((k-1)/2);
		}
	}

	public void exchange(int i,int j){
		int temp = heap[i];
		heap[i] = heap[j];
		heap[j] = temp;
	}

	public void bubbledown(int k){
		if(2*k+1 < size && heap[k] < heap[2*k+1]){
			exchange(k, 2*k+1);
			bubbledown(2*k+1);
		}	
		if(2*k+2 < size && heap[k] < heap[2*k+2]){
			exchange(k, 2*k+2);
			bubbledown(2*k+2);
		}
	}

	public int peek(){
		return heap[0];
	}
	
	public int removeMax(){
		size--;
		int max = heap[0];
		exchange(0, size);
		heap[size] = 0;
		bubbledown(0);
		return max;
	}
}