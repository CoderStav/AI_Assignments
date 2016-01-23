package eightPuzzleSolver;
import java.util.Queue;
import java.util.Stack;
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
	};
	
	public void breadthFirst(EightPuzzle puzzle){
		
		Node puzzleParent, puzzleChild;
		int[] possibleMoves;
		
		puzzleParent = new Node(new EightPuzzle(puzzle), null);
		possibleMoves = puzzleParent.puzzle.possibleMoves();
		
		Queue<Node> moveQueue = new LinkedList<Node>();
		moveQueue.add(puzzleParent);
		
		while(true){
			puzzleParent = moveQueue.remove();
			
			if(puzzleParent.puzzle.isSolved()){
				Stack<Node> WinningMoves = new Stack<Node>();
				
				System.out.println("======== Puzzle Solved! ========");
				WinningMoves.push(puzzleParent);
				while(puzzleParent.parent != null){
					WinningMoves.push(puzzleParent.parent);
					puzzleParent = puzzleParent.parent;
				}
				
				while(!WinningMoves.isEmpty())
					WinningMoves.pop().puzzle.display();
				
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
	
	public void depthFirst(){
		// TODO
	}
	
	public void uniformCost(){
		// TODO
	}
	
	public void bestFirst(){
		// TODO
	}
	
	public void A1(){
		// TODO
	}
	
	public void A2(){
		// TODO
	}
	
}
