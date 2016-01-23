package eightPuzzleSolver;
import java.util.Queue;
import java.util.LinkedList;

public class Search {
	
	public static void BreadthFirst(EightPuzzle puzzle){
		
		Queue<EightPuzzle> moveQueue = new LinkedList<EightPuzzle>();
		int[] possibleMoves = puzzle.possibleMoves();
		EightPuzzle puzzleCopy, puzzleMain;
		
		for(int i = 0; i < possibleMoves.length; ++i){
			puzzleCopy = new EightPuzzle(puzzle);
			if(possibleMoves[i] > 0){
				puzzleCopy.move(possibleMoves[i]);
				moveQueue.add(puzzleCopy);
			}
		}
		
		int count = 0;
		while(true){
			
			puzzleMain = moveQueue.remove();
			
			if(puzzleMain.isSolved()){
				System.out.println("Puzzle Solved!");
				return;
			}
			
			possibleMoves = puzzleMain.possibleMoves();
			
			for(int i = 0; i < possibleMoves.length; ++i){
				puzzleCopy = new EightPuzzle(puzzleMain);
				if(possibleMoves[i] > 0){
					puzzleCopy.move(possibleMoves[i]);
					moveQueue.add(puzzleCopy);
				}
			}
			
			count++;
		}
				
		
	}
	
}
