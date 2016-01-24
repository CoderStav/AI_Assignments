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
	
	private void displayWinMoves(Node winPuzzle){
		Stack<Node> WinningMoves = new Stack<Node>();
		Node winMove = winPuzzle;
		
		System.out.println("======== Puzzle Solved! ========");
		WinningMoves.push(winMove);
		while(winMove.parent != null){
			WinningMoves.push(winMove.parent);
			winMove = winMove.parent;
		}
		
		while(!WinningMoves.isEmpty())
			WinningMoves.pop().puzzle.display();
	}
	
	public Search(){
	}
	
	public void breadthFirst(EightPuzzle puzzle){
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int statesChecked;
		
		Queue<Node> moveQueue = new LinkedList<Node>();
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		statesChecked = 0;
		while(true){
			currentPuzzle = moveQueue.remove();
			
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle);
				System.out.println(statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			
			for(int i = 0; i < possibleMoves.length; ++i){
				puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
				if(possibleMoves[i] > 0){
					puzzleChild.puzzle.move(possibleMoves[i]);
					
					if(puzzleChild.parent == null || 
							puzzleChild.parent.parent == null || 
							!puzzleChild.puzzle.equals(puzzleChild.parent.parent.puzzle))
						moveQueue.add(puzzleChild);
				}
			}
			
			statesChecked++;
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
