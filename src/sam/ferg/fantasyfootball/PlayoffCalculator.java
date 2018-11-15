package sam.ferg.fantasyfootball;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayoffCalculator {
	
	// amount of runs for simulation
	public static final int RUNS = 1000000;
	
	// choose which player to analyze, the PTA
	public static final String PLAYER_TO_ANALYZE = "ferg";

	public static void main(String args[]) {
		
		// create roster
		List<Player> roster = new ArrayList<Player>();
		
		// create schedule
		List<Week> schedule = new ArrayList<Week>();
		
		// populate both with players and league schedule
		populateScheduleAndRoster(schedule, roster);
		
		// set player stats by parsing schedule
		setPlayerStats(schedule, roster);
		
		// run simulations for the rest of the league
		runPlayoffPathAnalysis(schedule, roster);
	}
	
	/**
	 * While giving 50/50 chances to players in matchups and in their tiebreakers, how often does a team make the playoffs?
	 * This tells us something of how hard a players 'paths' are for getting into the playoffs.
	 * 
	 * @param roster All teams in the league
	 * @param schedule All matchups in the league
	 */
	public static void runPlayoffPathAnalysis(List<Week> schedule, List<Player> roster) {
		Random random = new Random();
		
		// simulate season however many times
		for (int i = 0; i < RUNS; i++) {
			
			// reset players
			for (Player p : roster) {
				p.resetPlayerWinsAndLosses();
			}
			
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
					
					// determine winner of matchup either by random or if winner has been preset
					if (team1IsTheWinner) {
						m.setWinner(team1);
						m.setLoser(team2);
						
						// add to matchup records
						m.addWinToTeam(team1);
						
						// add to player records
						team1.addWin();
						team2.addLoss();
					} else {
						m.setWinner(team2);
						m.setLoser(team1);
						
						// add to matchup records
						m.addWinToTeam(team2);
						
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
					// when a player makes the playoffs, how did each matchup turn out? get the win ratio for team1 and team2 
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
	
	public static void setPlayerStats(List<Week> schedule, List<Player> roster) {
		
		int wins;
		int losses;
		List<Double> points;
		
		for (Player p : roster) {
			
			wins = 0;
			losses = 0;
			points = new ArrayList<Double>();
			
			for (Week w : schedule) {
				
				// skip games that haven't happened yet
				if (!w.isWeekOver()) {
					continue;
				}
				
				for (Matchup m : w.getMatchups()) {
					
					// move on if Player p was not in this matchup
					if (m.getTeam1().getName() != p.getName() && m.getTeam2().getName() != p.getName()) {
						continue;
					}
					
					// add a win if our player won
					if (m.getWinner().getName() == p.getName()) {
						wins++;
					}
					
					// add a loss if our player lost
					if (m.getLoser().getName() == p.getName()) {
						losses++;
					}
					
					// add points to point list
					points.add(m.getPointsForPlayer(p));
					
				}
			}
			
			// set player stats in roster
			p.setPlayer(wins, losses, points);
			
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
		week5.add(new Matchup(mitchell, coire, 109.9, 65.1));
		week5.add(new Matchup(ryan, josh, 102.0, 83.1));
		week5.add(new Matchup(matt, honschopp, 81.8, 115.2));
		week5.add(new Matchup(will, eric, 97.0, 80.1));
		
		Week week6 = new Week("Week 6", true);
		week6.add(new Matchup(ferg, mitchell, 138.6, 73.3));
		week6.add(new Matchup(matt, ryan, 116.2, 105.1));
		week6.add(new Matchup(honschopp, will, 163.4, 72.6));
		week6.add(new Matchup(shane, eric, 97.2, 95.0));
		week6.add(new Matchup(coire, josh, 95.8, 95.2));
		
		Week week7 = new Week("Week 7", true);
		week7.add(new Matchup(josh, ferg, 77.1, 85.1));
		week7.add(new Matchup(ryan, honschopp, 91.1, 130.8));
		week7.add(new Matchup(will, shane, 99.1, 111.6));
		week7.add(new Matchup(eric, coire, 80.6, 81.4));
		week7.add(new Matchup(mitchell, matt, 79.2, 142.2));
		
		Week week8 = new Week("Week 8", true);
		week8.add(new Matchup(ferg, eric, 84.6, 138.3));
		week8.add(new Matchup(shane, ryan, 95.7, 84.5));
		week8.add(new Matchup(coire, will, 98.2, 66.6));
		week8.add(new Matchup(matt, josh, 114.1, 126.6));
		week8.add(new Matchup(honschopp, mitchell, 135.4, 97.5));
		
		Week week9 = new Week("Week 9", true);
		week9.add(new Matchup(ferg, ryan, 91.7, 111.0));
		week9.add(new Matchup(matt, will, 124.8, 107.0));
		week9.add(new Matchup(honschopp, eric, 128.9, 117.5));
		week9.add(new Matchup(shane, josh, 80.3, 107.6));
		week9.add(new Matchup(coire, mitchell, 113.2, 79.1));
		
		// broncos, vikings, ravens, texans on BYE
		Week week10 = new Week("Week 10", true);
		week10.add(new Matchup(ferg, mitchell, 174.3, 96.4));
		week10.add(new Matchup(honschopp, will, 111.6, 106.2));
		week10.add(new Matchup(shane, eric, 44.0, 105.0));
		week10.add(new Matchup(coire, josh, 75.8, 145.9));
		week10.add(new Matchup(ryan, matt, 107.7, 66.9));
		
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
