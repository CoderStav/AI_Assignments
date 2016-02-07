package simacogo;
import java.util.Arrays;

public class Simacogo {
	
	private char blankSpace = '-'; // character to be displayed as "blank" space
	
	private char[][] board = {{blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace},
							  {blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace},
							  {blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace},
							  {blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace},
							  {blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace},
							  {blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace},
							  {blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace},
							  {blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace},
							  {blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace, blankSpace}};
	
	private int[] filledSpaces = {0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	/**
	 * Simacogo
	 * Constructor - Instantiates a new game of Simacogo with an empty game board
	 */
	public Simacogo(){
		
	}
	
	/**
	 * Simacogo
	 * Copy constructor - Forks a 
	 * @param c target object to be copied
	 */
	public Simacogo(Simacogo c){
		for(int i = 0; i < this.board.length; ++i)
			this.board[i] = Arrays.copyOf(c.board[i], this.board[i].length);
	}
	
	/**
	 * scoreSpace TODO
	 * @param x X coordinate of scored space
	 * @param y Y coordinate of scored space
	 * @param color Player color that is being scored ("black", "white")
	 * @return score value of space (x,y) or -1 if invalid x,y space coordinates
	 */
	private int scoreSpace(int x, int y, String color){
		
		if(x < 0 || x >= this.board.length)
			return -1;
		
		char playerColor;
		int score = 0;
		
		if(color.equals("black"))
			playerColor = 'B';
		else if(color.equals("white"))
			playerColor = 'W';
		else
			return 0;
		
		if(this.board[y][x] != playerColor)
			return 0;
		
		for(int i = y-1, j = x-1; i <= y+1; ++j){
			
			if(j > x+1){
				j = x-1;
				++i;
			}
			
			try{
				if((i != y || j != x)
					&& this.board[i][j] == playerColor){
					
					if(i == y || j == x)
						score += 2;
					else
						score += 1;
					
				}
			}catch(Exception e){
				// do nothing
			}
			
		}
		
		return score;
	}
	
	/**
	 * playerScore
	 * @param color Color of the player whose score is being retrieved ("black", "white")
	 * @return The current total score of the specified player
	 */
	public int score(String color){
		
		int score = 0;
		for(int i = 0; i < this.board.length; ++i)
			for(int j = 0; j < this.board[i].length; ++j)
				score += scoreSpace(i, j, color);
		
		/*
		 * every adjacent and diagonal piece is scored twice
		 * because each piece is individually scored. So the
		 * cumulative score must be divided by two
		 */
		return score/2;
		
	}
	
	/**
	 * move
	 * Executes a move on the board for specified column and player color
	 * 
	 * @param col board column the piece is to be put down
	 * @param color of the player's piece ("black", "white")
	 */
	public void move(int col, String color){
		
		if(col < 0 || col > 9)
			return;
		
		if(!color.equals("black") && !color.equals("white"))
			return;
		
		for(int i = 0; i < this.board.length; ++i){
			
			if(board[i][col] != this.blankSpace){
				
				if(color.equals("white"))
					this.board[i-1][col] = 'W';
				else if(color.equals("black"))
					this.board[i-1][col] = 'B';
				
				this.filledSpaces[i-1]++;
				
				return;
				
			}else if(i == this.board.length-1){
				
				if(color.equals("white"))
					this.board[i][col] = 'W';
				else if(color.equals("black"))
					this.board[i][col] = 'B';
				
				this.filledSpaces[i]++;
				
				return;
			
			}
		}
		
	}
	
	/**
	 * display
	 * prints the game board grid to the console
	 */
	public void display(){
		
		System.out.print("\n");
		for(int i = 0; i < this.board.length; ++i){
			for(int j = 0; j < this.board[i].length; ++j)
				System.out.printf("%s ", this.board[i][j]);
		
			System.out.print("\n");
		}
		System.out.print("\n");
		
	}
	
}
