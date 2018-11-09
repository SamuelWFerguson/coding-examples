package sam.ferg.fantasyfootball;

import java.util.Comparator;

public class TotalPlayoffsComparator implements Comparator<Player>{

	@Override
	public int compare(Player p1, Player p2) {
		return p2.getNumberOfPlayoffs() - p1.getNumberOfPlayoffs();
	}

}
