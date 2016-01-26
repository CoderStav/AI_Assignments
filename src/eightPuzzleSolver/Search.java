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
	
	private class Node{
		
		public EightPuzzle puzzle;
		public Node parent;
		public int lastMove = 0;
		public int score = 0;
		
		public Node(EightPuzzle puzzle, Node parent){
			this.puzzle = new EightPuzzle(puzzle);
			this.parent = parent;
		}
		
	}
	
	private void reloadPermutations(){
		
		this.puzzlePermutations.clear();
		
		int[] defaultConfig = {1, 2, 3, 4, 5, 6, 7, 8, 0};
		
		List<Integer> startConfig = new ArrayList<Integer>();
		for(int i = 0; i < defaultConfig.length; ++i)
			startConfig.add(defaultConfig[i]);
		
		loadPuzzlePermutations(startConfig, 0);
	}
	
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
			try {
				this.puzzlePermutations.add(new EightPuzzle(perm));
			} catch (InvalidConfigurationException e) {}
		}
		
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
		System.out.printf("Nodes Expanded: %d\n", numExpand);
			
	}
	
	public Search(){
	}
	
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
			
			currentPuzzle = moveQueue.remove();
			
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
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.lastMove = childMove;
						puzzleChild.score = currentPuzzle.score + childMove;
						
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
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
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.lastMove = childMove;
						puzzleChild.score = currentPuzzle.score + childMove;
						moveStack.push(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveStack.isEmpty());
		
		System.out.println("Unsolvable");
		
	}
	
	public void uniformCost(EightPuzzle puzzle){
		
		reloadPermutations();
		
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
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.lastMove = childMove;
						puzzleChild.score = currentPuzzle.score + childMove;
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
	public void bestFirst(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			
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
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.lastMove = childMove;
						puzzleChild.score = currentPuzzle.score + childMove;
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
	public void A1(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			
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
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.lastMove = childMove;
						puzzleChild.score = currentPuzzle.score + childMove;
						moveQueue.add(puzzleChild);
						this.puzzlePermutations.remove(puzzleChild.puzzle);
					}
				}
			}
			
		}while(!this.puzzlePermutations.isEmpty() && !moveQueue.isEmpty());
		
		System.out.println("Unsolvable");
	}
	
	public void A2(EightPuzzle puzzle){
		
		reloadPermutations();
		
		Node currentPuzzle, puzzleChild;
		int[] possibleMoves;
		int childMove;
		
		PriorityQueue<Node> moveQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			
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
					
					if(this.puzzlePermutations.contains(puzzleChild.puzzle)){
						puzzleChild.lastMove = childMove;
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
