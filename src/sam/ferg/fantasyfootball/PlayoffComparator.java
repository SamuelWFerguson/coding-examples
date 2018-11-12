package sam.ferg.fantasyfootball;

import java.util.Comparator;
import java.util.Random;

public class PlayoffComparator implements Comparator<Player>{
	
	private Random random = new Random();

	@Override
	public int compare(Player p1, Player p2) {
		// compare ratios
		if (p1.getRatio() > p2.getRatio()) {
			return -1;
		}
		if (p1.getRatio() < p2.getRatio()) {
			return 1;
		}
		// break ratio tie by comparing points for
		if (p1.getRatio() == p2.getRatio()) {
			boolean randomBool = random.nextBoolean();
			if (randomBool) {
				return -1;
			} else {
				return 1;
			}
		}
		// points for is a tie as well, these are players equal, so randomly choose who is better
		RandomPlayerComparator c = new RandomPlayerComparator();
		return c.compare(p1, p2);
	}

}
