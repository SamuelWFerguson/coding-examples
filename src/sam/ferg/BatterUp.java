package sam.ferg;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BatterUp {

	/* Determine a players slugging percentage based on the input given
	 * -1 walk, 0 strike-out, 1 single, 2 double, 3 triple, 4 homerun  
	 * input is two lines, the first is how many at-bats. the second is the result of each at-bat.
	 */
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to slugging percentage calculator!");
		System.out.println("Input the number of at-bats"); 
		int atbats = input.nextInt();
		if (atbats <= 0) {
			System.out.println("Expected more than zero at-bats. Exiting program.");
			return;
		}
		
		System.out.println("Input the results of each, seperated by a space");
		List<Integer> playerHits = new ArrayList<Integer>();
		
		//getting the hits input from the user as a string
		String hitsAsString = input.next();
		hitsAsString += input.nextLine();
		
		int hitAsInt = 0;
		for (String hitAsString: hitsAsString.split(" ")) {
			try {
				hitAsInt = Integer.parseInt(hitAsString);
			} catch(NumberFormatException exception) {
				System.out.println("You have entered an invalid integer. Exiting program.");
				return;
			}
			if (hitAsInt < -1 || hitAsInt > 4) {
				System.out.println("One of the at-bats you have entered is outside of the range of -1 to 4. Exiting program");
				return;
			}
			
			playerHits.add(hitAsInt);
		}
		
		if (playerHits.size() > atbats) {
			System.out.println("You have entered more at-bats than expected. Exiting program.");
			return;
		}
		if (playerHits.size() < atbats) {
			System.out.println("You have entered less at-bats than expected Exiting program.");
			return;
		}
	
		int numerator = 0;
		int denominator = atbats;
		for (Integer playerHit: playerHits) {
			if (playerHit >= 0) {
				numerator = numerator + playerHit;
			} else {
				denominator = denominator - 1;
			}
			
		}
		float sluggingPercentage = (float) numerator / (float) denominator;
		
		System.out.println("slugging percentage: " + numerator + "/" + denominator + " = " + sluggingPercentage);
		return;
		}
}
