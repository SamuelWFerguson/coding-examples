package sam.ferg;

import java.util.Scanner;

public class FibonacciSeries {
	
	public static void printFibonacciSeries(int n) {
		Integer fib0 = 0;
		Integer fib1 = 1;
		Integer fibNext;
		
		for (int i = 0; i < n; i++) {
			fibNext = fib0 + fib1;
			
			fib0 = fib1;
			fib1 = fibNext;
						
			System.out.println(fib0);
			
		}
	}
	
	public static void main(String[] args) {
		Integer totalNumbers = 0;
		Scanner input = new Scanner(System.in);
		
		System.out.println("Input how many numbers of the fibonacci series you would like to see");
		
		try {
			totalNumbers = input.nextInt();
		} catch(Exception exception) {
			System.out.println("Could not parse input as an integer");
		}
		
		printFibonacciSeries(totalNumbers);
		
		input.close();
		System.out.println("done");
		return;
	}
	

}
