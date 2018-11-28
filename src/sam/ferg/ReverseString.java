package sam.ferg;

import java.util.Scanner;

public class ReverseString {
	
	public static String reverse(String s) {
		String reversedString = new String();
		
		// start from end of string and move to beginning
		for (int i = s.length(); i > 0; i--) {
			reversedString += s.charAt(i - 1);
		}
		
		return reversedString;
	}
	
	public static void main (String[] args) {
		
		Scanner input = new Scanner(System.in);
		String inputString = new String();
		
		System.out.println("Input a string you want reversed. Input \"quit\" to quit.");
		
		while (true) {
			// wait for user to input line, set it to inputString
			inputString = input.nextLine();
			
			if (inputString.compareTo("quit") == 0) {
				System.out.println("Quitting");
				input.close();
				return;
			}
			
			// print reverse
			System.out.println(reverse(inputString));
		}
	}

}
