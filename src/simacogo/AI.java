package simacogo;
import java.util.Scanner;

public class AI {

	Simacogo gameBoard;
	
	/*
	 * AI
	 * Constructor - Instantiates a new AI object with an empty game board
	 */
	public AI(){
		this.gameBoard = new Simacogo();
	}
	
	/**
	 * playMe
	 * Starts a new game of Simacogo with the AI.
	 */
	public void playMe(){
		
		Scanner userIn = new Scanner(System.in);
		int userMove;
		
		while(true){
			this.gameBoard.display();
			System.out.printf("Black: %d\n", this.gameBoard.score("black"));
			System.out.printf("White: %d\n", this.gameBoard.score("white"));
			
			// player executed move
			System.out.print("Enter your next move: ");
			userMove = userIn.nextInt();
			this.gameBoard.move(userMove, "black");
			
			// AI executed move
			this.gameBoard.move(this.bestMove(4), "white");
		}
	}
	
	/**
	 * bestMove
	 * Uses a minimax algorithm to find the optimal move for the AI
	 * @param ply Number of move pairs the method will examine
	 * @return Column with the best determined score payoff for the AI
	 */
	private int bestMove(int ply){
		return this.bestMove(this.gameBoard, ply, true);
	}
	
	/**
	 * bestMove
	 * Helper method
	 * @param board The current board configuration
	 * @param ply The number of move pairs the method will examine
	 * @param AIMove Whether it's the AI's "move" in the game tree node being examined
	 * @return Column with the best determined score payoff for the AI
	 */
	private int bestMove(Simacogo board, int ply, boolean AIMove){
		
		if(ply == 0)
			return board.score("white") - board.score("black");
		
		int moveScore;
		int bestMove = 0;
		int bestMoveScore = Integer.MIN_VALUE;
		Simacogo forkedBoard;
		
		for(int i = 0; i < 9; ++i){
			
			forkedBoard = new Simacogo(board);
			
			if(AIMove){
				forkedBoard.move(i, "white");
				moveScore = this.bestMove(forkedBoard, ply - 1, false);
			}else{
				forkedBoard.move(i, "black");
				moveScore = this.bestMove(forkedBoard, ply - 1, true);
			}
				
			if(moveScore > bestMoveScore){
				bestMove = i;
				bestMoveScore = moveScore;
			}
			
		}
		
		return bestMove;
		
	}
	
	
}
