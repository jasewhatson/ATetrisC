/*******************************************************************************
|	Copyright 2006	Jason Whatson. All rights reserved.                    |
*******************************************************************************/

package tetrisjava;

import java.awt.Color; //For the diffrent coloured pices 

/**
 * A representation of a Tetris game Piece called a tetrominoe.
 * @author Jason Whatson
 * @version 1.1
 */
public class TetrisPiece {
    
    /**
     * The lenght in pixils of a individual Tetris block
     */
    public static final int PIECELENGTH = 16;
    
    /*------------------------------------------------
     *Tetris Pieces - The diferent shapes
     *-----------------------------------------------*/
    private int[][] TetrisPieceI = {{1},
                                    {1},
                                    {1},
                                    {1}};      /*I*/
            
    private int[][] TetrisPieceJ = {{2,0,0},
                                    {2,2,2}};  /*J*/
            
    private int[][] TetrisPieceL = {{0,0,3},
                                    {3,3,3}};  /*L*/    
            
    private int[][] TetrisPieceO =  {{4,4},    
                                     {4,4}};   /*O*/
            
    private int[][] TetrisPieceS = {{0,5,5},
                                    {5,5,0}};  /*S*/
            
    private int[][] TetrisPieceT = {{0,6,0},
                                    {6,6,6}};  /*T*/
            
    private int[][] TetrisPieceZ = {{7,7,0},
                                    {0,7,7}};  /*Z*/ 
    /*------------------------------------------------*/
    
    //Holds a 2d array refelcting the current piece in play and its rotation pos
    private int[][] CurrentPieceState;
    
    //Starting piece positions
    private int PieceRLocation = 0;
    private int PieceCLocation = 5;
   
    /** Creates a new instance of TetrisPiece */
    public TetrisPiece() {
    }
    
    
    /**
     * Gets a Tetris blocks corresponding colour
     * @param ColourCode The number that corresponds to the colour of the piece block
     * @return The colour of the block in a Tetris piece
     */
    public static Color getPieceColour(int ColourCode){
        switch (ColourCode){
            case 1: 
                return Color.ORANGE;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.RED;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.yellow;
            case 6: 
                return Color.MAGENTA;
            case 7:
                return Color.CYAN;
        }
        return null;
    }
    
    /**
     * A Piece 2d array
     * @return A 2d array representation of the Tetris Piece
     */
    public int[][] getCurrentPiece(){
        return CurrentPieceState;
    }
    
    /**
     * Selects a Tetris piece at random
     * @return A random Tetris piece
     */
    public int[][] getRandomPiece(){
        
        int RandomNo = (int) (0 + Math.random() * 7);
        
        switch(RandomNo){
            case 0: 
                this.CurrentPieceState = TetrisPieceI;
                return TetrisPieceI;
            case 1:
                this.CurrentPieceState = TetrisPieceJ;
                return TetrisPieceJ;
            case 2:
                this.CurrentPieceState = TetrisPieceL;
                return TetrisPieceL;
            case 3:
                this.CurrentPieceState = TetrisPieceO;
                return TetrisPieceO;
            case 4:
                this.CurrentPieceState = TetrisPieceS;
                return TetrisPieceS;
            case 5:
                this.CurrentPieceState = TetrisPieceT;
                return TetrisPieceT;
            case 6:
                this.CurrentPieceState = TetrisPieceZ;
                return TetrisPieceZ;
            default:
                return null;
        }
    }
    
    /**
     * Get the column location of the Piece (the location on the board)
     * @return The current column location
     */
    public int getColumnLocation(){
        return PieceCLocation;
    }
    
    /**
     * Get the row location of the Piece (the location on the board)
     * @return The current row location of the piece
     */
    public int getRowLocation(){
        return PieceRLocation;
    }
    
    /**
     * Rotates the piece to the right
     * @param test Only tests to see if a Tetris Piece can be rotated right dosnt actually roate it.
     */
    public int[][] rotateRight(boolean test){
        
        //Switchs the hight with width of the piece in its current state
        int NewArrayWidth = this.CurrentPieceState.length;
	int NewArrayHeight = this.CurrentPieceState[0].length;
        
        //Create a new array with the inverted dimensions 
        int[][] rotate90Array = new int[NewArrayHeight][NewArrayWidth];
        
        /*Loops though the old 2d array and places the value to the position
        where it would be if roatated*/
        for (int r = 0; r < NewArrayHeight;r++){
            for (int c =0; c < NewArrayWidth;c++){
                rotate90Array[r][c] = this.CurrentPieceState[NewArrayWidth-c-1][r];
            }
        }
        
        //If when the piece is rotated it will go over the edge of the board
        if (this.PieceCLocation + NewArrayWidth > Board.BoardWidth){
            //Send it back
            this.PieceCLocation = Board.BoardWidth - NewArrayWidth;
        }
        //If we are not testing and actually want to rotate a piece, asign the rotated instance
        if (test == false){
          this.CurrentPieceState = rotate90Array;  
        }
        
        return rotate90Array;
    }
    
    /**
     * Rotates the piece to the left
     * @param test Only tests to see if a Tetris Piece can be rotated to the left dosnt actually
     */
    public int[][] rotateLeft(boolean test){
        
        //See rotateLeft() ^^^ for comments!
        
        int NewArrayWidth = this.CurrentPieceState.length;
	int NewArrayHeight = this.CurrentPieceState[0].length;
        
        int[][] rotate270Array = new int[NewArrayHeight][NewArrayWidth];
        
        for (int r =0; r < NewArrayHeight;r++){
            for (int c =0; c < NewArrayWidth;c++){
                rotate270Array[r][c] = this.CurrentPieceState[c][NewArrayHeight-r-1];
            }
        }
        
         if (this.PieceCLocation + NewArrayWidth > Board.BoardWidth){
            this.PieceCLocation = Board.BoardWidth - NewArrayWidth;
        }
        if (test == false){
          this.CurrentPieceState = rotate270Array;  
        }
        
        return rotate270Array;
    }
    
    /**
     * Moves a piece down
     * @return 1 if the piece could be moved down. 0 if it couldnt
     */
    public int moveDown(){
        if (CurrentPieceState.length + this.PieceRLocation < Board.BoardHeight){
            this.PieceRLocation++;
            return 1;
        }
        return 0;
    }
    
    /**
     * Movies a piece to the left or right on the board
     * @param left True if you want to move the piece left
     * @param right True if you want to move the piece right
     */
    public void moveLeftRight(boolean left, boolean right){
        /*Checks piece isnt going under the edge 
         - This may have already been done with  b1.placePiece() in right key 
         event in Tetris game*/
        if(left == true && right == false){
            if (this.PieceCLocation > 0){
                this.PieceCLocation--;
            }
        }else if(left == false && right == true){
            //Checks piece isnt going over the edge - Same as above
            if (this.PieceCLocation + CurrentPieceState[0].length < Board.BoardWidth){    
                this.PieceCLocation++;
            }
        }
    }
}
