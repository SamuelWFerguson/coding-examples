package sam.ferg.fantasyfootball;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayoffCalculator {
	
	// amount of runs for simulation
	public static final int RUNS = 1000000;
	
	// amount of weeks played so far
	public static final int WEEKS_PLAYED = 9;
	
	// predict chaos (its totally legit)
	public static final int CHAOS_POINTS = 45;
	
	// choose which player to analyze, the PTA
	public static final String PLAYER_TO_ANALYZE = "mitchell";

	public static void main(String args[]) {
		
		// create all our players
		Player honschopp = new Player("honschopp");
		Player coire = new Player("coire");
		Player matt = new Player("matt");
		Player ferg = new Player("ferg");
		Player shane = new Player("shane");
		Player ryan = new Player("ryan");
		Player will = new Player("will");
		Player eric = new Player("eric");
		Player josh = new Player("josh");
		Player mitchell = new Player("mitchell");
		
		// add boom bust
		honschopp.setBoomBust(30, 30);
		coire.setBoomBust(50, 30);
		matt.setBoomBust(40, 30);
		ferg.setBoomBust(45, 10);
		shane.setBoomBust(45, 35);
		ryan.setBoomBust(25, 20);
		will.setBoomBust(10, 40);
		eric.setBoomBust(55, 25);
		josh.setBoomBust(40, 20);
		mitchell.setBoomBust(30, 20);
		
		// create and populate roster
		List<Player> roster = new ArrayList<Player>();
		roster.add(honschopp);
		roster.add(coire);
		roster.add(matt);
		roster.add(ferg);
		roster.add(shane);
		roster.add(ryan);
		roster.add(will);
		roster.add(eric);
		roster.add(josh);
		roster.add(mitchell);
		
		// broncos, vikings, ravens, texans on BYE
		Week week10 = new Week("Week 10");
		week10.add(new Matchup(ferg, mitchell));
		week10.add(new Matchup(honschopp, will));
		week10.add(new Matchup(shane, eric));
		week10.add(new Matchup(coire, josh));
		week10.add(new Matchup(ryan, matt));
		
		// bills, dolphins, patriots, jets, browns, and 49ers on BYE
		Week week11 = new Week("Week 11");
		week11.add(new Matchup(ferg, josh));
		week11.add(new Matchup(honschopp, ryan));
		week11.add(new Matchup(shane, will));
		week11.add(new Matchup(coire, eric));
		week11.add(new Matchup(matt, mitchell));
		
		// rams and chiefs on BYE
		Week week12 = new Week("Week 12");
		week12.add(new Matchup(ferg, eric));
		week12.add(new Matchup(honschopp, mitchell, 0.8, 1.0));
		week12.add(new Matchup(shane, ryan, 0.9, 1.0));
		week12.add(new Matchup(coire, will));
		week12.add(new Matchup(matt, josh));
		
		
		// create schedule
		List<Week> schedule = new ArrayList<Week>();
		schedule.add(week10);
		schedule.add(week11);
		schedule.add(week12);
		
		// Run simulations for the rest of the league
		calculatePlayoffChances(roster, schedule);
	}
	
	public static void calculatePlayoffChances(List<Player> roster, List<Week> schedule) {
		
		Random random = new Random();
		
		for (int i = 0; i < RUNS; i++) {
			
			// reset players
			setPlayerStats(roster);
			
			// simulate the season
			for (Week w: schedule) {
				for (Matchup m: w.getMatchups()) {
					// get both players in the matchup
					Player team1 = m.getTeam1();
					Player team2 = m.getTeam2();
					
					// determine PF averages
					double team1Points = team1.getPointsFor() / (double) WEEKS_PLAYED;
					double team2Points = team2.getPointsFor() / (double) WEEKS_PLAYED;
					
					//give random variability to PF scored
					team1Points += (double) random.nextInt(team1.getBoom()) + random.nextInt(CHAOS_POINTS);
					team1Points -= (double) random.nextInt(team1.getBust()) + random.nextInt(CHAOS_POINTS);
					team2Points += (double) random.nextInt(team2.getBoom()) + random.nextInt(CHAOS_POINTS);
					team2Points -= (double) random.nextInt(team2.getBust()) + random.nextInt(CHAOS_POINTS);
					
					// adjust based on matchup strength
					team1Points = team1Points * m.getTeam1MatchupStrength();
					team2Points = team2Points * m.getTeam2MatchupStrength();
					
					// determine winner of matchup
					if (team1Points > team2Points) {
						m.setWinner(team1);
						m.setLoser(team2);
						
						// add to records
						team1.addWin();
						team2.addLoss();
					}
					
					if (team1Points < team2Points) {
						m.setWinner(team2);
						m.setLoser(team1);
						
						// add to records
						team1.addLoss();
						team2.addWin();
					}
					
					if (team1Points == team2Points) {
						// randomly choose a winner and loser
						Player winner = m.chooseRandomWinner();
						Player loser = m.getOtherTeam(winner);
						
						// add the loss and the win to the players records
						winner.addWin();
						loser.addLoss();	
					}
					
					team1.addPointsFor(team1Points);
					team2.addPointsFor(team2Points);
				}
			}
			
			// add a play-off to the top 4 players of the simulated season
			roster.sort(new PlayoffComparator());
			for (int j = 0; j < 4; j++) {
				roster.get(j).addPlayoff();
				// record results when specific player gets to playoffs
				if (roster.get(j).getName() == PLAYER_TO_ANALYZE) {
					for (Week w : schedule) {
						for (Matchup m : w.getMatchups()) {
							m.giveWinnerSpecialWin();
						}
					}
				}
			}			
		}
			
		// sort based on total play-offs
		roster.sort(new TotalPlayoffsComparator());
		
		// print out matchup results
		for (Week w : schedule) {
			System.out.println(w.getWeekName());
			for (Matchup m : w.getMatchups()) {
				double team1Ratio = (double) m.getTeam1Wins() / (double) RUNS;
				double team2Ratio = (double) m.getTeam2Wins() / (double) RUNS;
				
				// in case we divided by zero or something
				if (team1Ratio > 100) team1Ratio = 1.0;
				if (team2Ratio > 100) team2Ratio = 1.0;
				
				System.out.printf("\t" + m.getTeam1().getName() + " (" + m.getTeam1MatchupStrength() + ") Vs " + m.getTeam2().getName() + " (" + m.getTeam2MatchupStrength() + ")\n");
				System.out.printf("\t" + "Team " + m.getTeam1().getName() + ": " + "%.1f" + "%%\n", team1Ratio * 100.0);
				System.out.printf("\t" + "Team " + m.getTeam2().getName() + ": " + "%.1f" + "%%\n", team2Ratio * 100.0);
				System.out.print("\n");
				
			}
		}
		System.out.printf("\n");
		
		// print out result of simulations
		System.out.println("Player Order for " + RUNS + " simulations");
		for (int i = 0; i < roster.size(); i++) {
			int numOfPlayoffs = roster.get(i).getNumberOfPlayoffs();
			double ratio = (double) numOfPlayoffs / (double) RUNS;
			
			System.out.println((i + 1) + ": " + roster.get(i).getName());
			System.out.println("\t" + numOfPlayoffs + " playoff appearences");
			System.out.printf("\t" + "appearing in " + "%.1f" + "%% of playoffs \n", (ratio * 100.0));
		}
		System.out.print("\n");
		
		// try to find the total playoffs for our PTA
		int totalRunsForPlayerToAnalyze = 0;
		for (Player p : roster) {
			if (p.getName() == PLAYER_TO_ANALYZE) {
				totalRunsForPlayerToAnalyze = p.getNumberOfPlayoffs();
			}
		}
		
		// print matchup analyze for PTA
		if (totalRunsForPlayerToAnalyze != 0) {
			for (Week w : schedule) {
				System.out.println(w.getWeekName() + " for " + PLAYER_TO_ANALYZE + " play-offs");
				for (Matchup m : w.getMatchups()) {
					double team1SpecialRatio = (double) m.getTeam1SpecialWins() / (double) totalRunsForPlayerToAnalyze;
					double team2SpecialRatio = (double) m.getTeam2SpecialWins() / (double) totalRunsForPlayerToAnalyze;
					
					double team1Ratio = (double) m.getTeam1Wins() / (double) RUNS;
					double team2Ratio = (double) m.getTeam2Wins() / (double) RUNS;
					
					double team1RatioDiff = team1SpecialRatio - team1Ratio;
					double team2RatioDiff = team2SpecialRatio - team2Ratio;
					
					// in case we divided by zero or something
					if (team1Ratio > 100) team1Ratio = 1.0;
					if (team2Ratio > 100) team2Ratio = 1.0;
					
					System.out.printf("\t" + m.getTeam1().getName() + " Vs " + m.getTeam2().getName() + "\n");
					
					// show percentages for team 1
					System.out.printf("\t" + "Team " + m.getTeam1().getName() + ": " + "%.1f" + "%% ", team1SpecialRatio * 100.0);
					System.out.print("(");
					if (team1RatioDiff > 0) {
						System.out.print("+");
					}
					System.out.printf("%.1f%%)\n", team1RatioDiff * 100);
					
					// show percentages for team 2
					System.out.printf("\t" + "Team " + m.getTeam2().getName() + ": " + "%.1f" + "%% ", team2SpecialRatio * 100.0);
					System.out.print("(");
					if (team2RatioDiff > 0) {
						System.out.print("+");
					}
					System.out.printf("%.1f%%)\n", team2RatioDiff * 100);
					
					System.out.print("\n");
					
				}
			} 
			System.out.print("\n");
		} else {
			System.out.println(PLAYER_TO_ANALYZE + " does not make it to the playoffs in any simulations");
		}
	}
	
	public static void setPlayerStats(List<Player> roster) {
		for (Player p : roster) {
			switch(p.getName()) {
				case "honschopp":
					p.setPlayer(7, 2, 1155.1);
					break;
				case "coire":
					p.setPlayer(7, 2, 883.9);
					break;
				case "matt":
					p.setPlayer(5, 4, 956.2);
					break;
				case "ferg":
					p.setPlayer(4, 5, 887.6);
					break;
				case "shane":
					p.setPlayer(4, 5, 852.8);
					break;
				case "ryan":
					p.setPlayer(6, 3, 941.9);
					break;
				case "will":
					p.setPlayer(4, 5, 970.7);
					break;
				case "eric":
					p.setPlayer(3, 6, 950.8);
					break;
				case "josh":
					p.setPlayer(3, 6, 885.6);
					break;
				case "mitchell":
					p.setPlayer(2, 7, 746.2);
					break;
			}
		}
	}
}
