package eightPuzzleSolver;

public class Main {

	public static void main(String[] args) {
		
		int[] easyConfig = {1, 3, 4, 8, 6, 2, 7, 0, 5};
		int[] medConfig = {2, 8, 1, 0, 4, 3, 7, 6, 5};
		int[] hardConfig = {5, 6, 7, 4, 0, 8, 3, 2, 1};
		EightPuzzle easy, med, hard;
		
		try {
			easy = new EightPuzzle(easyConfig);
			med = new EightPuzzle(medConfig);
			hard = new EightPuzzle(hardConfig);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			return;
		}
		
		// these two searches are succesful
		Search.BreadthFirst(easy);
		Search.BreadthFirst(med);
		
		// this one fails.
		// Search.BreadthFirst(hard);
		
	}

}
