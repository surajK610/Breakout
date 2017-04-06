import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Recursion1 {
		
	public static void main (String [] args) throws Exception	
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter String");
        String input = br.readLine();
		int x = Integer.parseInt(input);
		System.out.println(getFactorial(x));
		
	}
	
	private static int getFactorial(int x){
		if (x == 1){
			return 1;
		}
		else if (x == 2){
			return 2;
		}
		else {
			int result = x * getFactorial (x-1);
			System.out.println(String.format("Factorial of %d is %d", x, result));
			return result;
		}
		
	}		
}
	