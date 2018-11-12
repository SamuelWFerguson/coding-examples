package sam.ferg.fantasyfootball;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private int wins;
	private int losses;
	private int numberOfPlayoffs;
	private double pointsFor;
	private List<Double> points;
	
	Player(String n) {
		name = n;
		wins = 0;
		losses = 0;
		pointsFor = 0;
		numberOfPlayoffs = 0;
		points = new ArrayList<Double>();
	}
	
	public void setPlayer(int w, int l, double pf, List<Double> points) {
		wins = w;
		losses = l;
		pointsFor = pf;
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
}
