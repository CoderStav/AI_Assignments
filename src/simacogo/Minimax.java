package simacogo;

public class Minimax {

	private Simacogo gameBoard;
	private int movePly;
	private int turnsLeft;
	
	/*
	 * AI
	 * Constructor - Instantiates a new AI object with an empty game board
	 */
	public Minimax(){
		this.gameBoard = new Simacogo();
	}
	
	/**
	 * newGame
	 * begins a new game of Simacogo with minimax
	 * @param numTurns
	 * @param ply
	 */
	public void newGame(int numTurns, int ply){
		this.gameBoard = new Simacogo();
		this.turnsLeft = numTurns;
		this.movePly = ply;
		
		this.showGame();
		
	}
	
	/**
	 * executeMove
	 * Executes a valid player move and subsequent AI move within a running game of Simacogo
	 * @param move Player supplied move value
	 */
	public void executeMove(int move){
		
		if(this.checkGameOver())
			return;
		
		if(this.gameBoard.move(move, "white")){
			// AI executed move
			this.gameBoard.move(this.bestMove(this.movePly), "black");
			this.turnsLeft--;
		}else
			System.out.println("Invalid move");
		
		this.showGame();
		
		this.checkGameOver();
		
	}
	
	/**
	 * showGame
	 * Prints game board, current scores, and turns remaining to console
	 */
	private void showGame(){
		this.gameBoard.display();
		System.out.printf("White (You): %d\n", this.gameBoard.score("white"));
		System.out.printf("Black: %d\n", this.gameBoard.score("black"));
		System.out.printf("Turns Remaining: %d\n", this.turnsLeft);
	}
	
	/**
	 * checkGameOver
	 * Prints game over message to console and returns true if the game is over
	 * @return boolean indicating if the game is over or not
	 */
	private boolean checkGameOver(){
		if(this.turnsLeft > 0)
			return false;
		
		int blackScore = this.gameBoard.score("white");
		int whiteScore = this.gameBoard.score("black");
		
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
	 * Helper method - uses minimax algorithm of a given ply to determine the best move for the AI
	 * @param board The current board configuration
	 * @param ply The number of move pairs the method will examine
	 * @param AIMove Whether it's the AI's "move" in the game tree node being examined
	 * @return Column with the best determined score payoff for the AI
	 */
	private int bestMove(Simacogo board, int ply, boolean AIMove){
		
		if(ply == 0)
			return board.score("black") - board.score("white");
		
		int moveScore = Integer.MIN_VALUE;
		int bestMove = 0;
		int bestMoveScore = Integer.MIN_VALUE+1;
		
		boolean validMove = false;
		
		Simacogo forkedBoard;
		
		for(int i = 0; i < 9; ++i){
			
			forkedBoard = new Simacogo(board);
			
			if(AIMove){
				validMove = forkedBoard.move(i, "black");
				if(validMove)
					moveScore = this.bestMove(forkedBoard, ply - 1, false);
			}else{
				validMove = forkedBoard.move(i, "white");
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
