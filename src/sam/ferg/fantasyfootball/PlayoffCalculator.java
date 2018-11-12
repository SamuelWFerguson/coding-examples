package sam.ferg.fantasyfootball;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayoffCalculator {
	
	// amount of runs for simulation
	public static final int RUNS = 1000000;
	
	// choose which player to analyze, the PTA
	public static final String PLAYER_TO_ANALYZE = "matt";

	public static void main(String args[]) {
		
		// create roster
		List<Player> roster = new ArrayList<Player>();
		
		// create schedule
		List<Week> schedule = new ArrayList<Week>();
		
		// populate both with players and league schedule
		populateScheduleAndRoster(schedule, roster);
		
		// Run simulations for the rest of the league
		runPlayoffPathAnalysis(roster, schedule);
	}
	
	/**
	 * While giving 50/50 chances to players in matchups and in their tiebreakers, how often does a team make the playoffs?
	 * 
	 * @param roster All teams in the league
	 * @param schedule All matchups in the league
	 */
	public static void runPlayoffPathAnalysis(List<Player> roster, List<Week> schedule) {
		Random random = new Random();
		
		// simulate season however many times
		for (int i = 0; i < RUNS; i++) {
			// reset players
			setPlayerStats(roster);
			// simulate the season
			for (Week w: schedule) {
				
				// skip weeks that have already happened
				if (w.isWeekOver()) {
					continue;
				}
				
				for (Matchup m: w.getMatchups()) {
					// get both players in the matchup
					Player team1 = m.getTeam1();
					Player team2 = m.getTeam2();
					
					// randomly pick winner with 50/50 chances
					boolean team1IsTheWinner = random.nextBoolean();
					
					// determine winner of matchup
					if (team1IsTheWinner) {
						m.setWinner(team1);
						m.setLoser(team2);
						
						// add to records
						team1.addWin();
						team2.addLoss();
					} else {
						m.setWinner(team2);
						m.setLoser(team1);
						
						// add to records
						team1.addLoss();
						team2.addWin();
					}
				}
			}
			
			// add a play-off to the top 4 players of the simulated season
			roster.sort(new PlayoffComparator());
			for (int j = 0; j < 4; j++) {
				roster.get(j).addPlayoff();
				
				// record results when PTA gets to playoffs
				if (roster.get(j).getName() == PLAYER_TO_ANALYZE) {
					for (Week w : schedule) {
						if (w.isWeekOver()) {
							continue;
						}
						for (Matchup m : w.getMatchups()) {
							m.giveWinnerSpecialWin();
						}
					}
				}
			}			
		}
			
		// sort based on total play-offs
		roster.sort(new TotalPlayoffsComparator());
		
		// print out result of simulations
		System.out.println("Playoff path statistics for  " + RUNS + " simulations");
		
		int totalPlayoffPaths = 1;
		for (Week w : schedule) {
			if (w.isWeekOver()) {
				continue;
			}
			for (int i = 0; i < w.getMatchups().size(); i++) {
				totalPlayoffPaths *= 2;
			}
		}
		System.out.println("Where there are " + totalPlayoffPaths + " total playoff paths");
		
		for (int i = 0; i < roster.size(); i++) {
			int numOfPlayoffs = roster.get(i).getNumberOfPlayoffs();
			double ratio = (double) numOfPlayoffs / (double) RUNS;
			
			System.out.println((i + 1) + ": " + roster.get(i).getName());
			System.out.printf("\t" + "Approximately " + "%.1f" + "%% of playoff paths\n", (ratio * 100.0));
		}
		System.out.print("\n");
		
		// try to find the total playoffs for our PTA
		int totalRunsForPlayerToAnalyze = 0;
		for (Player p : roster) {
			if (p.getName() == PLAYER_TO_ANALYZE) {
				totalRunsForPlayerToAnalyze = p.getNumberOfPlayoffs();
			}
		}
		
		// print playoff path analysis for only when PTA wins the playoffs
		if (totalRunsForPlayerToAnalyze != 0) {
			for (Week w : schedule) {
				
				// skip weeks that have already happened
				if (w.isWeekOver()) {
					continue;
				}
				
				System.out.println(w.getWeekName() + " for " + PLAYER_TO_ANALYZE + " play-offs");
				for (Matchup m : w.getMatchups()) {
					double team1SpecialRatio = (double) m.getTeam1SpecialWins() / (double) totalRunsForPlayerToAnalyze;
					double team2SpecialRatio = (double) m.getTeam2SpecialWins() / (double) totalRunsForPlayerToAnalyze;
					
					double team1Ratio = (double) m.getTeam1Wins() / (double) RUNS;
					double team2Ratio = (double) m.getTeam2Wins() / (double) RUNS;
					
					double team1RatioDiff = team1SpecialRatio - team1Ratio;
					double team2RatioDiff = team2SpecialRatio - team2Ratio;
					
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
	
	public static Double estimatePointsForPlayer(Player player) {
		Double points = new Double(0.0);
		
		return points;
	}
	
	public static void setPlayerStats(List<Player> roster) {
		for (Player p : roster) {
			List<Double> points = new ArrayList<Double>();
			int wins = 0;
			int losses = 0;
			double pf = 0.0;
			switch(p.getName()) {
				case "honschopp":
					wins = 8;
					losses = 2;
					pf = 1155.1;
					break;
				case "coire":
					wins = 7;
					losses = 3;
					pf = 883.9;
					break;
				case "matt":
					wins = 5;
					losses = 5;
					pf= 956.2;
					break;
				case "ferg":
					wins = 5;
					losses = 5;
					pf = 887.6;
					break;
				case "shane":
					wins = 4;
					losses = 6;
					pf = 852.8;
					break;
				case "ryan":
					wins = 7;
					losses = 3;
					pf = 941.9;
					break;
				case "will":
					wins = 4;
					losses = 6;
					pf = 970.7;
					break;
				case "eric":
					wins = 4;
					losses = 6;
					pf = 950.8;
					break;
				case "josh":
					wins = 4;
					losses = 6;
					pf = 885.6;
					break;
				case "mitchell":
					wins = 2;
					losses = 8;
					pf = 746.2;
					break;
			}
			p.setPlayer(wins, losses, pf, points);
		}
	}
	
	private static void populateScheduleAndRoster(List<Week> schedule, List<Player> roster) {
		
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
		
		
		Week week1 = new Week("Week 1", true);
		week1.add(new Matchup(ryan, ferg, 114.8, 89.6));
		week1.add(new Matchup(matt, coire, 82.3, 119.3));
		week1.add(new Matchup(will, mitchell, 117.6, 81.0));
		week1.add(new Matchup(honschopp, shane, 140.4, 142.9));
		week1.add(new Matchup(eric, josh, 93.1, 78.4));
		
		Week week2 = new Week("Week 2", true);
		week2.add(new Matchup(ferg, honschopp, 86.9, 141.0));
		week2.add(new Matchup(will, matt, 113.0, 74.2));
		week2.add(new Matchup(ryan, eric, 125.8, 88.9));
		week2.add(new Matchup(coire, shane, 74.1, 62.7));
		week2.add(new Matchup(mitchell, josh, 99.5, 95.4));
		
		Week week3 = new Week("Week 3", true);
		week3.add(new Matchup(coire, ferg, 86.7, 97.3));
		week3.add(new Matchup(eric, honschopp, 96.5, 102.2));
		week3.add(new Matchup(shane, matt, 98.5, 115.2));
		week3.add(new Matchup(josh, will, 81.9, 97.3));
		week3.add(new Matchup(mitchell, ryan, 62.6, 97.8));
		
		Week week4 = new Week("Week 4", true);
		week4.add(new Matchup(matt, ferg, 105.3, 99.4));
		week4.add(new Matchup(josh, shane, 140.3, 91.8));
		week4.add(new Matchup(honschopp, coire, 97.8, 150.4));
		week4.add(new Matchup(eric, mitchell, 160.8, 64.2));
		week4.add(new Matchup(will, ryan, 100.4, 109.7));
		
		Week week5 = new Week("Week 5", true);
		week5.add(new Matchup(ferg, shane, 114.3, 72.2));
		week5.add(new Matchup());
		week5.add(new Matchup());
		week5.add(new Matchup());
		week5.add(new Matchup());
		
		Week week6 = new Week("Week 6", true);
		week6.add(new Matchup());
		week6.add(new Matchup());
		week6.add(new Matchup());
		week6.add(new Matchup());
		week6.add(new Matchup());
		
		Week week7 = new Week("Week 7", true);
		week7.add(new Matchup());
		week7.add(new Matchup());
		week7.add(new Matchup());
		week7.add(new Matchup());
		week7.add(new Matchup());
		
		Week week8 = new Week("Week 8", true);
		week8.add(new Matchup());
		week8.add(new Matchup());
		week8.add(new Matchup());
		week8.add(new Matchup());
		week8.add(new Matchup());
		
		Week week9 = new Week("Week 9", true);
		week9.add(new Matchup());
		week9.add(new Matchup());
		week9.add(new Matchup());
		week9.add(new Matchup());
		week9.add(new Matchup());
		
		// broncos, vikings, ravens, texans on BYE
		Week week10 = new Week("Week 10", true);
		week10.add(new Matchup(ferg, mitchell));
		week10.add(new Matchup(honschopp, will));
		week10.add(new Matchup(shane, eric));
		week10.add(new Matchup(coire, josh));
		week10.add(new Matchup(ryan, matt));
		
		// bills, dolphins, patriots, jets, browns, and 49ers on BYE
		Week week11 = new Week("Week 11", false);
		week11.add(new Matchup(ferg, josh));
		week11.add(new Matchup(honschopp, ryan));
		week11.add(new Matchup(shane, will));
		week11.add(new Matchup(coire, eric));
		week11.add(new Matchup(matt, mitchell));
		
		// rams and chiefs on BYE
		Week week12 = new Week("Week 12", false);
		week12.add(new Matchup(ferg, eric));
		week12.add(new Matchup(honschopp, mitchell));
		week12.add(new Matchup(shane, ryan));
		week12.add(new Matchup(coire, will));
		week12.add(new Matchup(matt, josh));
		
		schedule.add(week1);
		schedule.add(week2);
		schedule.add(week3);
		schedule.add(week4);
		schedule.add(week5);
		schedule.add(week6);
		schedule.add(week7);
		schedule.add(week8);
		schedule.add(week9);
		schedule.add(week10);
		schedule.add(week11);
		schedule.add(week12);
		
	}
}
