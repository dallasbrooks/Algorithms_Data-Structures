import java.util.*;
import java.io.File;

public class Stock{

    public static int[] CalculateSpan(int[] p){
        int[] span = new int[p.length];
        Stack<Integer> stack = new Stack<Integer>();
        span[0] = 1;
        stack.push(0);
        for(int a = 1; a < p.length; a++){
            while(!stack.isEmpty() && p[stack.peek()] <= p[a]){
                stack.pop();
            }
            if(stack.isEmpty()){
                span[a] = a + 1;
            }
            else{
                span[a] = a - stack.peek();
            }
            stack.push(a);
        }
        return span;
    }

    public static void main(String[] args){
        int[][] test = {
            {1,5,6,8,7,9},//1,2,3,4,1,6
            {25,26,75,14,25,65,32,85,25,65,9,45},//1,2,3,1,2,3,1,8,1,2,1,2
            {78,52,21,14,1,2,9,0},//1,1,1,1,1,2,3,1
        };
        for(int a = 0; a < test.length; a++){
            Stock m = new Stock();
            int[] span = m.CalculateSpan(test[a]);
            System.out.println("Test "+(a+1));
            System.out.println("The span is:");
            System.out.print("\t");
            for(int b = 0; b < span.length; b++){
                System.out.print(span[b]+(((b+1) == span.length)? ".\n": ", "));
            }
        }
    }
}