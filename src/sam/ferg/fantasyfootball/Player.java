package sam.ferg.fantasyfootball;

public class Player {
	private String name;
	private int wins;
	private int losses;
	private int numberOfPlayoffs;
	private int boom;
	private int bust;
	private double pointsFor;
	
	Player(String n) {
		name = n;
		wins = 0;
		losses = 0;
		pointsFor = 0;
		numberOfPlayoffs = 0;
	}
	
	public void setPlayer(int w, int l, double pf) {
		wins = w;
		losses = l;
		pointsFor = pf;
	}
	
	public void setBoomBust(int bm, int bst) {
		boom = bm;
		bust = bst;
	}
	
	public void addPlayoff() {
		numberOfPlayoffs++;
	}
	
	public int getNumberOfPlayoffs() {
		return numberOfPlayoffs;
	}
	
	public double getRatio() {
		return wins/losses;
	}
	
	public void addWin() {
		wins++;
	}
	
	public void addLoss() {
		losses++;
	}
	
	public void setWins(int w) {
		wins = w;
	}
	
	public int getWins() {
		return wins;
	}
	
	public void setLosses(int l) {
		losses = l;
	}
	
	public int getLosses() {
		return losses;
	}
	
	public void setPointsFor(double pf) {
		pointsFor = pf;
	}
	
	public void addPointsFor(double pf) {
		pointsFor += pf;
	}
	
	public double getPointsFor() {
		return pointsFor;
	}
	
	public String getName() {
		return name;
	}
	
	public int getBoom() {
		return boom;
	}
	
	public int getBust() {
		return bust;
	}
}
