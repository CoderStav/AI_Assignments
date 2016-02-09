package simacogo;
import java.util.Scanner;

public class AI {

	Simacogo gameBoard;
	int turnsLeft;
	
	/*
	 * AI
	 * Constructor - Instantiates a new AI object with an empty game board
	 */
	public AI(){
		this.gameBoard = new Simacogo();
	}
	
	public void newGame(int numTurns){
		this.gameBoard = new Simacogo();
		this.turnsLeft = numTurns;
	}
	
	public void executeMove(int move, int ai_moveply){
		
		if(this.checkGameOver())
			return;
		
		if(this.gameBoard.move(move, "black")){
			// AI executed move
			this.gameBoard.move(this.bestMove(ai_moveply), "white");
			this.turnsLeft--;
		}else
			System.out.println("Invalid move");
		
		this.gameBoard.display();
		System.out.printf("Black: %d\n", this.gameBoard.score("black"));
		System.out.printf("White: %d\n", this.gameBoard.score("white"));
		System.out.printf("Turns Remaining: %d\n", this.turnsLeft);
		
		this.checkGameOver();
		
	}
	
	private boolean checkGameOver(){
		if(this.turnsLeft > 0)
			return false;
		
		int blackScore = this.gameBoard.score("black");
		int whiteScore = this.gameBoard.score("white");
		
		System.out.println("Game Over!");
		if(blackScore > whiteScore)
			System.out.println("Player Wins!");
		else if(blackScore < whiteScore)
			System.out.println("Computer Wins!");
		else
			System.out.println("Tie Game!");
		
		return true;
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
		
		int moveScore = Integer.MIN_VALUE;
		int bestMove = 0;
		int bestMoveScore = Integer.MIN_VALUE+1;
		
		boolean validMove = false;
		
		Simacogo forkedBoard;
		
		for(int i = 0; i < 9; ++i){
			
			forkedBoard = new Simacogo(board);
			
			if(AIMove){
				validMove = forkedBoard.move(i, "white");
				if(validMove)
					moveScore = this.bestMove(forkedBoard, ply - 1, false);
			}else{
				validMove = forkedBoard.move(i, "black");
				if(validMove)
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
