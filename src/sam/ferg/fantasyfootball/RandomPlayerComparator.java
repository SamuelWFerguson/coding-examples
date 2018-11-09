package sam.ferg.fantasyfootball;

import java.util.Comparator;
import java.util.Random;

public class RandomPlayerComparator implements Comparator<Player>{

	@Override
	public int compare(Player o1, Player o2) {
		Random random = new Random();
		// randomly pick -1 or 1
		int num = 0;
		while (num == 0) {
			// can be -1, 0, 1
			num = random.nextInt(3) - 1;
		}
		return num;
	}

}
