package eightPuzzleSolver;
import java.util.Arrays;
import java.lang.System;

public class EightPuzzle extends Object{

	private final int[] winConfiguration = {1, 2, 3, 8, 0, 4, 7, 6, 5};
	
	private int[] configuration = {0, 0, 0, 0, 0, 0, 0, 0, 0};
	private int emptySpace;
	
	/*
	 * EightPuzzle object
	 * holds the int[9] array that contains the configuration
	 * has methods used to modify the configuration and check
	 * the status of both the puzzle as a whole and its 
	 * individual tiles.
	 */
	public EightPuzzle(int[] startingConfig){
		
		if(startingConfig.length != 9) 
			throw new IllegalArgumentException("configuration does not contain exactly 8 elements");
		
		for(int i = 0; i < 9; ++i){
			if(startingConfig[i] > 8 || startingConfig[i] < 0)
				throw new IllegalArgumentException("configuration has element with value not between 0 and 8");
			
			for(int j = i+1; j < 9; ++j)
				if(startingConfig[i] == startingConfig[j])
					throw new IllegalArgumentException("configuration has duplicate elements");
			
			if(startingConfig[i] == 0)
				this.emptySpace = i;
				
		}
		
		this.configuration = startingConfig;
	}
	
	/*
	 * Copy Constructor
	 */
	public EightPuzzle(EightPuzzle c){
		System.arraycopy(c.configuration, 0, this.configuration, 0, c.configuration.length);
		this.emptySpace = c.emptySpace;
	}
	
	@Override
	public int hashCode(){	
	    return Arrays.hashCode(this.configuration);
	}
	
	@Override
	public boolean equals(final Object puzzle){
		return Arrays.equals(this.configuration, ((EightPuzzle) puzzle).configuration);
	}
	
	/*
	 * Determines if two given tiles are in the same row
	 * used internally
	 */
	private boolean sameRow(int x, int y){
		
		if((x < 0 || x > 8) || (y < 0 || y > 8)) return false;
		
		int k = 0;
		boolean xfound, yfound;
		xfound = yfound = false;
		
		for(int i = 0; i < 3; ++i){
			for(int j = 0; j < 3; ++j, ++k){
				if(this.configuration[k] == x)
					xfound = true;
				else if(this.configuration[k] == y)
					yfound = true;
			}
			
			if(yfound || xfound)
				return yfound && xfound;	
		}
		
		return false;
	}
	
	/*
	 * Determines the current position of a given tile
	 * used internally
	 */
	private int tilePosition(int tile){
		for(int i = 0; i < this.configuration.length; ++i)
			if(this.configuration[i] == tile)
				return i;
				
		return -1;
	}
	
	/*
	 * Determines the "correct" position of a given tile
	 * according to winConfiguration
	 * used internally
	 */
	private int correctTilePosition(int tile){
		for(int i = 0; i < this.winConfiguration.length; ++i)
			if(this.winConfiguration[i] == tile)
				return i;
		
		return -1;
	}
	
	/*
	 * Determines if the puzzle's configuration is solved
	 * by cross referencing it with winConfiguration
	 */
	public boolean isSolved(){
		return Arrays.equals(this.configuration, this.winConfiguration);
	}
	
	/*
	 * Moves tile with specified face value
	 */
	public void move(int tile){
		
		if(tile < 1 || tile > 8)
			throw new IllegalArgumentException("Invalid tile");
		
		int[] movablePieces = possibleMoves();
		boolean pieceCanMove = false;
		for(int i = 0; i < movablePieces.length; ++i)
			if(movablePieces[i] == tile)
				pieceCanMove = true;
		
		if(!pieceCanMove)
			throw new IllegalArgumentException("Piece cannot be moved");
		
		int piecePosition = 0;
		for(int i = 0; i < 9; ++i)
			if(this.configuration[i] == tile)
				piecePosition = i;
		
		this.configuration[this.emptySpace] = tile;
		this.configuration[piecePosition] = 0;
		
		this.emptySpace = piecePosition;
		
	}
	
	/*
	 * Return a int[4] array where values that are
	 * greater than zero are the face values of pieces that
	 * can be moved.
	 */
	public int[] possibleMoves(){
		
		int[] validmoves = new int[4];
		
		try{
			if(sameRow(0, this.configuration[this.emptySpace - 1]))
				validmoves[0] = this.configuration[this.emptySpace - 1];
		}catch(Exception e){}
		
		try{
			validmoves[1] = this.configuration[this.emptySpace - 3];
		}catch(Exception e){}
		
		try{
			if(sameRow(0, this.configuration[this.emptySpace + 1]))
				validmoves[2] = this.configuration[this.emptySpace + 1];
		}catch(Exception e){}
		
		try{
			validmoves[3] = this.configuration[this.emptySpace + 3];
		}catch(Exception e){}
		
		return validmoves;
		
	}
	
	/*
	 * Returns the Manhattan Distance of the tile with the given face value
	 */
	public int manhattanDistance(int tile){
		
		if(tile < 0 || tile > 8)
			throw new IllegalArgumentException("Invalid tile");
		
		if(tile > 0 || tile < 8) return -1;
		
		int position = tilePosition(tile);
		int correctPosition = correctTilePosition(tile);
		boolean[] possibleMovements;
		
		int i = 0;
		while(position != correctPosition){
			
			/* {can go left, can go up, can go right, can go down} */
			possibleMovements = new boolean[]{position % 3 >= 1, position >= 3, position % 3 <= 1, position <= 5};
			
			if(position > correctPosition && possibleMovements[0] && position - correctPosition < 3)
				position += -1;
			else if(position > correctPosition && possibleMovements[1] && position - correctPosition >= 3)
				position += -3;
			else if(position < correctPosition && possibleMovements[2] && correctPosition - position < 3)
				position += 1;
			else if(position < correctPosition && possibleMovements[3] && correctPosition - position >= 3)
				position += 3;
			
			possibleMovements = null;
			++i;
			
		}
		
		return i;
	}
	
	/*
	 * Returns the amount of tiles in the puzzle's configuration
	 * that aren't in their correct spots according to
	 * winConfiguration
	 */
	public int misplacedTiles(){
		int misplaced = 0;
		for(int i = 0; i < this.configuration.length; ++i)
			if(this.configuration[i] != this.winConfiguration[i])
				misplaced++;
		
		return misplaced;
	}
	
	/*
	 * Prints the puzzle's current configuration
	 * in a 3 by 3 grid
	 */
	public void display(){
		for(int i = 0; i < 9; ++i){
			if(i % 3 == 0) System.out.print("\n");
			
			if(this.configuration[i] != 0)
				System.out.printf("%d ", this.configuration[i]);
			else
				System.out.print("  ");
		}
		System.out.print("\n");
	}

}
