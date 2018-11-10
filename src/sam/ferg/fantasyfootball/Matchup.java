package sam.ferg.fantasyfootball;

import java.util.Random;

public class Matchup {
	private Player team1;
	private Player team2;
	private Player winner;
	private Player loser;
	private double team1MatchupStrength;
	private double team2MatchupStrength;
	private int team1Wins;
	private int team2Wins;
	private int team1SpecialWins;
	private int team2SpecialWins;
	
	Matchup() {
		team1Wins = 0;
		team2Wins = 0;
		team1SpecialWins = 0;
		team2SpecialWins = 0;
	}
	
	Matchup(Player t1, Player t2) {
		team1 = t1;
		team2 = t2;
		team1Wins = 0;
		team2Wins = 0;
		team1SpecialWins = 0;
		team2SpecialWins = 0;
		team1MatchupStrength = 1.0;
		team2MatchupStrength = 1.0;
	}
	
	Matchup(Player t1, Player t2, double strength1, double strength2) {
		team1 = t1;
		team2 = t2;
		team1Wins = 0;
		team2Wins = 0;
		team1SpecialWins = 0;
		team2SpecialWins = 0;
		team1MatchupStrength = strength1;
		team2MatchupStrength = strength2;
	}
	
	
	public Player getTeam1() {
		return team1;
	}
	
	public Player getTeam2() {
		return team2;
	}
	
	public Player chooseRandomWinner() {
		Random random = new Random();
		
		switch (random.nextInt(2)) {
		case 0:
			return team1;
		case 1:
			return team2;
		default:
			return null;
		}
	}
	
	public Player getOtherTeam(Player team) {
		if (team.getName() == team1.getName()) {
			return team2;
		}
		if (team.getName() == team2.getName()) {
			return team1;
		}
		return null;
	}
	
	public void setWinner(Player team) {
		// add win to team1?
		if (team.getName() == team1.getName()) {
			team1Wins++;
		}
		// add win to team2?
		if (team.getName() == team2.getName()) {
			team2Wins++;
		}
		winner = team;
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public void setLoser(Player team) {
		loser = team;
	}
	
	public Player getLoser() {
		return loser;
	}
	
	public int getTeam1Wins() {
		return team1Wins;
	}
	
	public int getTeam2Wins() {
		return team2Wins;
	}

	public double getTeam1MatchupStrength() {
		return team1MatchupStrength;
	}

	public void setTeam1MatchupStrength(double team1MatchupStrength) {
		this.team1MatchupStrength = team1MatchupStrength;
	}

	public double getTeam2MatchupStrength() {
		return team2MatchupStrength;
	}

	public void setTeam2MatchupStrength(double team2MatchupStrength) {
		this.team2MatchupStrength = team2MatchupStrength;
	}
	
	public void setMatchupStrengths(double strength1, double strength2) {
		team1MatchupStrength = strength1;
		team2MatchupStrength = strength2;
	}
	
	public void addTeam1SpecialWin() {
		team1SpecialWins++;
	}
	
	public void addTeam2SpecialWin() {
		team2SpecialWins++;
	}
	
	public int getTeam1SpecialWins() {
		return team1SpecialWins;
	}
	
	public int getTeam2SpecialWins() {
		return team2SpecialWins;
	}
	
	public void giveWinnerSpecialWin() {
		Player team = winner;
		if (team.getName() == team1.getName()) {
			team1SpecialWins++;
		}
		if (team.getName() == team2.getName()) {
			team2SpecialWins++;
		}
	}
}
