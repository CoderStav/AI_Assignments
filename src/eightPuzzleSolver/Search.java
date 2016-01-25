package eightPuzzleSolver;
import java.util.Comparator;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;

public class Search {
	
	private class Node{
		
		public EightPuzzle puzzle;
		public Node parent;
		public int lastMove = 0;
		public int level = 0;
		public int score = 0;
		
		public Node(EightPuzzle puzzle, Node parent){
			this.puzzle = new EightPuzzle(puzzle);
			this.parent = parent;
		}
		
	}
	
	public Search(){
	}
	
	private void displayWinMoves(Node winPuzzle, int numExpand){
		Stack<Node> WinningMoves = new Stack<Node>();
		Node winMove = winPuzzle;
		
		System.out.println("======== Puzzle Solved! ========");
		WinningMoves.push(winMove);
		while(winMove.parent != null){
			WinningMoves.push(winMove.parent);
			winMove = winMove.parent;
		}
		
		while(!WinningMoves.isEmpty()){
			winMove = WinningMoves.pop();
			winMove.puzzle.display();
		}
		System.out.println();
		System.out.println("~~ Search Metrics ~~");
		System.out.printf("Solution Score: %d\n", winPuzzle.score);
		System.out.printf("Nodes Expanded: %d", numExpand);
			
	}
	
	public void breadthFirst(EightPuzzle puzzle){
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		Queue<Node> moveQueue = new LinkedList<Node>();
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			if(++statesChecked % 1000000 == 0)
				System.out.println(statesChecked);
			
			currentPuzzle = moveQueue.remove();
			
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0 && childMove != currentPuzzle.lastMove){
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					
					puzzleChild.puzzle.move(childMove);
					puzzleChild.lastMove = childMove;
					
					// TODO figure out how to record level for BFS
					
					puzzleChild.score = currentPuzzle.score + childMove;
					
					moveQueue.add(puzzleChild);
				}
			}
			
		}while(!moveQueue.isEmpty());
	}
	
	public void depthFirst(EightPuzzle puzzle, int maxDepth){
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		Stack<Node> moveStack = new Stack<Node>();
		moveStack.push(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			if(++statesChecked % 1000000 == 0)
				System.out.println(statesChecked);
			
			currentPuzzle = moveStack.pop();
			
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			
			if(currentPuzzle.level < maxDepth){
				for(int i = 0; i < possibleMoves.length; ++i){
					
					childMove = possibleMoves[i];
					
					if(childMove > 0 && childMove != currentPuzzle.lastMove){
						puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
						
						puzzleChild.puzzle.move(childMove);
						puzzleChild.lastMove = childMove;
						puzzleChild.level = currentPuzzle.level + 1;
						puzzleChild.score = currentPuzzle.score + childMove;
						
						moveStack.push(puzzleChild);
					}
				}
			}
			
		}while(!moveStack.isEmpty());
		
	}
	
	public void uniformCost(EightPuzzle puzzle){
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			public int compare(Node n1, Node n2){
				return n1.score - n2.score;
			}
		});
		
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			if(++statesChecked % 1000000 == 0)
				System.out.println(statesChecked);
			
			currentPuzzle = moveQueue.poll();
			
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0 && childMove != currentPuzzle.lastMove){
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					
					puzzleChild.puzzle.move(childMove);
					puzzleChild.lastMove = childMove;
					
					// TODO figure out how to record level for UCS
					
					puzzleChild.score = currentPuzzle.score + childMove;
					
					moveQueue.add(puzzleChild);
				}
			}
			
		}while(!moveQueue.isEmpty());
	}
	
	public void bestFirst(EightPuzzle puzzle){
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			
			public int compare(Node n1, Node n2){
				return n1.lastMove - n2.lastMove;
			}
			
		});
		
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			if(++statesChecked % 1000000 == 0)
				System.out.println(statesChecked);
			
			currentPuzzle = moveQueue.poll();
			
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0 && childMove != currentPuzzle.lastMove){
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					
					puzzleChild.puzzle.move(childMove);
					puzzleChild.lastMove = childMove;
					
					// TODO figure out how to record level for GBFS
					
					puzzleChild.score = currentPuzzle.score + childMove;
					
					moveQueue.add(puzzleChild);
				}
			}
			
		}while(!moveQueue.isEmpty());
	}
	
	public void A1(){
		// TODO
	}
	
	public void A2(){
		// TODO
	}
	
}
