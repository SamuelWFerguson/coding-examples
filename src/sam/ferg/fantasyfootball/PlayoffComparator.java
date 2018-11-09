package sam.ferg.fantasyfootball;

import java.util.Comparator;

public class PlayoffComparator implements Comparator<Player>{

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
			if (p1.getPointsFor() > p2.getPointsFor()) {
				return -1;
			}
			if (p1.getPointsFor() < p2.getPointsFor()) {
				return 1;
			}
		}
		// points for is a tie as well, these are players equal, so randomly choose who is better
		RandomPlayerComparator c = new RandomPlayerComparator();
		return c.compare(p1, p2);
	}

}
