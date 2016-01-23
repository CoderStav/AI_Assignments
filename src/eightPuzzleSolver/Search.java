package eightPuzzleSolver;
import java.util.Queue;
import java.util.LinkedList;

public class Search {
	
	private class Node{
		
		public EightPuzzle puzzle;
		public Node parent;
		
		public Node(EightPuzzle puzzle, Node parent){
			this.puzzle = new EightPuzzle(puzzle);
			this.parent = parent;
		}
		
	}
	
	public Search(){
		//nothing
	};
	
	public void BreadthFirst(EightPuzzle puzzle){
		
		Node puzzleParent, puzzleChild;
		puzzleParent = new Node(new EightPuzzle(puzzle), null);
		
		int[] possibleMoves = puzzleParent.puzzle.possibleMoves();
		
		Queue<Node> moveQueue = new LinkedList<Node>();
		moveQueue.add(puzzleParent);
		
		while(true){
			puzzleParent = moveQueue.remove();
			
			puzzleParent.puzzle.display();
			
			if(puzzleParent.puzzle.isSolved()){
				System.out.println("======== Puzzle Solved! ========");
				while(puzzleParent.parent != null){
					puzzleParent = puzzleParent.parent;
					puzzleParent.puzzle.display();
				}
				return;
			}
			
			possibleMoves = puzzleParent.puzzle.possibleMoves();
			
			for(int i = 0; i < possibleMoves.length; ++i){
				puzzleChild = new Node(new EightPuzzle(puzzleParent.puzzle), puzzleParent);
				if(possibleMoves[i] > 0){
					puzzleChild.puzzle.move(possibleMoves[i]);
					moveQueue.add(puzzleChild);
				}
			}
		}
		
	}
	
}
