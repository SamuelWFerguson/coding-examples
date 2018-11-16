package sam.ferg.fantasyfootball;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private int wins;
	private int baseWins;
	private int losses;
	private int baseLosses;
	private int numberOfPlayoffs;
	private List<Double> points;
	
	Player(String n) {
		name = n;
		wins = 0;
		losses = 0;
		numberOfPlayoffs = 0;
		points = new ArrayList<Double>();
	}
	
	public void setPlayer(int w, int l, List<Double> p) {
		wins = w;
		baseWins = w;
		losses = l;
		baseLosses = l;
		points = p;
	}
	
	public void resetPlayerWinsAndLosses() {
		wins = baseWins;
		losses = baseLosses;
	}
	
	public void addPlayoff() {
		numberOfPlayoffs++;
	}
	
	public int getNumberOfPlayoffs() {
		return numberOfPlayoffs;
	}
	
	public float getRatio() {
		return ((float) wins/ (float) losses);
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
	
	public String getName() {
		return name;
	}
	
	public List<Double> getPoints() {
		return points;
	}
}
