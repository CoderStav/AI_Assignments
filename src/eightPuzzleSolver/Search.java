package eightPuzzleSolver;
import java.util.Comparator;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;


public class Search{
	
	private HashSet<EightPuzzle> puzzlePermutations = new HashSet<EightPuzzle>();
	
	
	/*
	 * Node
	 * Subclass
	 * Holds EightPuzzle object and its book keeping data in regards to the search
	 */
	private class Node{
		
		public EightPuzzle puzzle;
		public Node parent;
		public int score = 0;
		public int movedTile = 0; // last moved tile
		public int direction = -1; // movement made to get to the current configuration. 
		// 0 - right, 1 - down, 2 - left, 3 - up, -1 - none
		
		public Node(EightPuzzle puzzle, Node parent){
			this.puzzle = new EightPuzzle(puzzle);
			this.parent = parent;
		}
		
	}
	
	/*
	 * Clears puzzlePermutations and then calls
	 * loadPuzzlePermutation with a layout to start its
	 * recursive activity with
	 */
	private void reloadPermutations(){
		
		this.puzzlePermutations.clear();
		
		int[] startConfigArr = {1, 2, 3, 4, 5, 6, 7, 8, 0};
		
		List<Integer> startConfig = new ArrayList<Integer>();
		for(int i = 0; i < startConfigArr.length; ++i)
			startConfig.add(startConfigArr[i]);
		
		loadPuzzlePermutations(startConfig, 0);
	}
	
	/*
	 * Recursively loads into puzzlePermutations all 9! possible unique EightPuzzle configurations
	 */
	private void loadPuzzlePermutations(List<Integer> lst, int k){
		
		for(int i = k; i < lst.size(); ++i){
			Collections.swap(lst, i, k);
			loadPuzzlePermutations(lst, k+1);
			Collections.swap(lst, k, i);
		}
		
		if(k == lst.size()-1){
			int[] perm = new int[lst.size()];
			for(int i = 0; i < perm.length; ++i)
				perm[i] = lst.get(i);
			this.puzzlePermutations.add(new EightPuzzle(perm));
		}
		
	}
	
	/*
	 * Displays discovered winning move path from start to finish
	 */
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
			
			String[] directions = {"right\n", "down\n", "left\n", "up\n"};
			
			if(winMove.movedTile != 0 && winMove.direction != -1)
				System.out.printf("%d %s", winMove.movedTile, directions[winMove.direction]);
			System.out.printf("Score: %d\n", winMove.score);
		}
		
		System.out.println();
		System.out.println("~~ Search Metrics ~~");
		System.out.printf("Solution Score: %d\n", winPuzzle.score);
		System.out.printf("Nodes Expanded: %d\n", numExpand);
			
	}
	
	/*
	 * Search
	 * Contains methods used to search EightPuzzle for a solution from any given
	 * EightPuzzle object with a configuration that is both valid and solvable.
	 */
	public Search(){
		// constructor
	}
	
	
	/*
	 * Executes a breadth first search for a puzzle solution stating from argument puzzle
	 * breaks the loop and calls displayWinMoves on success
	 * prints "Unsolvable" when a solution cannot be found
	 */
	public void breadthFirst(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		Queue<Node> moveQueue = new LinkedList<Node>();
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			statesChecked++;
			
			currentPuzzle = moveQueue.poll();
			
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0){
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					puzzleChild.puzzle.move(childMove);
					puzzleChild.movedTile = childMove;
					puzzleChild.direction = i;
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.score = currentPuzzle.score + childMove;
						
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
	/*
	 * Executes a depth first search for a puzzle solution stating from argument puzzle
	 * breaks the loop and calls displayWinMoves on success
	 * prints "Unsolvable" when a solution cannot be found
	 */
	public void depthFirst(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		Stack<Node> moveStack = new Stack<Node>();
		moveStack.push(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			++statesChecked;
			
			currentPuzzle = moveStack.pop();
			
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0){
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					puzzleChild.puzzle.move(childMove);
					puzzleChild.movedTile = childMove;
					puzzleChild.direction = i;
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.score = currentPuzzle.score + childMove;
						moveStack.push(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveStack.isEmpty());
		
		System.out.println("Unsolvable");
		
	}
	
	/*
	 * Executes a uniform-cost search for a puzzle solution stating from argument puzzle
	 * uses the cumulative value of the face values of previously moved to determine path cost
	 * breaks the loop and calls displayWinMoves on success
	 * prints "Unsolvable" when a solution cannot be found
	 */
	public void uniformCost(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			/*
			 * This compare implementation allows moveQueue to order is Node contents by 
			 * cumulative score, allowing uniform cost to expand the cheapest
			 * puzzle configuration available to it
			 */
			public int compare(Node n1, Node n2){
				return n1.score - n2.score;
			}
		});
		
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			++statesChecked;
			
			currentPuzzle = moveQueue.poll();
			
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0){
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					puzzleChild.puzzle.move(childMove);
					puzzleChild.movedTile = childMove;
					puzzleChild.direction = i;
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.score = currentPuzzle.score + childMove;
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
	/*
	 * Executes a "greedy" breadth first search for a puzzle solution stating from argument puzzle
	 * uses the number of misplaced tiles in an EightPuzzle configuration as its heuristic
	 * breaks the loop and calls displayWinMoves on success
	 * prints "Unsolvable" when a solution cannot be found
	 */
	public void bestFirst(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			/*
			 * This compare implementation allows moveQueue to order is Node contents by 
			 * number of misplaced tiles (via EightPuzzle's misplaced tiles method).
			 * allowing bestFirst to expand the puzzle available to it with the 
			 * least misplaced tiles first.
			 */
			public int compare(Node n1, Node n2){
				return n1.puzzle.misplacedTiles() - n2.puzzle.misplacedTiles();
			}
			
		});
		
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			++statesChecked;
			
			currentPuzzle = moveQueue.poll();
				
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0){
					
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					puzzleChild.puzzle.move(childMove);
					puzzleChild.movedTile = childMove;
					puzzleChild.direction = i;
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.score = currentPuzzle.score + childMove;
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
	/*
	 * Executes an A* search for a puzzle solution stating from argument puzzle
	 * uses the cumulative value of the face values of previously moved tiles as its path cost
	 * uses the number of misplaced tiles in an EightPuzzle configuration as is heuristic
	 * breaks the loop and calls displayWinMoves on success
	 * prints "Unsolvable" when a solution cannot be found
	 */
	public void A1(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			/*
			 * This compare implementation allows moveQueue to order is Node contents by 
			 * f(n) = g(n)[cumulative moved tile face value sum] + h(n)[number of misplaced tiles].
			 * allowing A* to expand the puzzle available to it with the least cost
			 * according to f(n) first.
			 */
			public int compare(Node n1, Node n2){
				return (n1.puzzle.misplacedTiles() + n1.score) - (n2.puzzle.misplacedTiles() + n2.score);
			}
			
		});
		
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			++statesChecked;
			
			currentPuzzle = moveQueue.poll();
				
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0){
					
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					puzzleChild.puzzle.move(childMove);
					puzzleChild.movedTile = childMove;
					puzzleChild.direction = i;
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.score = currentPuzzle.score + childMove;
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
	/*
	 * Executes an A* search for a puzzle solution stating from argument puzzle
	 * uses the cumulative value of the face values of previously moved tiles as its path cost
	 * uses the cumulative Manhattan Distances from every tile in a given configuration as its heuristic
	 * breaks the loop and calls displayWinMoves on success
	 * prints "Unsolvable" when a solution cannot be found
	 */
	public void A2(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			/*
			 * This compare implementation allows moveQueue to order is Node contents by 
			 * f(n) = g(n)[cumulative moved tile face value sum] + h(n)[Manhattan Distance sum of all tile in a given configuration].
			 * allowing A* to expand the puzzle available to it with the least cost
			 * according to f(n) first.
			 */
			public int compare(Node n1, Node n2){
				
				int n1ManhattanSum, n2ManhattanSum;
				n1ManhattanSum = n2ManhattanSum = 0;
				for(int i = 1; i < 9; ++i){
					n1ManhattanSum += n1.puzzle.manhattanDistance(i);
					n2ManhattanSum += n2.puzzle.manhattanDistance(i);
				}
				
				return (n1ManhattanSum + n1.score) - (n2ManhattanSum + n2.score);
			}
			
		});
		
		moveQueue.add(new Node(new EightPuzzle(puzzle), null));
		
		int statesChecked = 0;
		do{
			++statesChecked;
			
			currentPuzzle = moveQueue.poll();
				
			if(currentPuzzle.puzzle.isSolved()){
				displayWinMoves(currentPuzzle, statesChecked);
				return;
			}
			
			possibleMoves = currentPuzzle.puzzle.possibleMoves();
			for(int i = 0; i < possibleMoves.length; ++i){
				
				childMove = possibleMoves[i];
				
				if(childMove > 0){
					
					puzzleChild = new Node(new EightPuzzle(currentPuzzle.puzzle), currentPuzzle);
					puzzleChild.puzzle.move(childMove);
					puzzleChild.movedTile = childMove;
					puzzleChild.direction = i;
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.score = currentPuzzle.score + childMove;
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
}
