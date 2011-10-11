/*******************************************************************************
|	Copyright 2006	Jason Whatson. All rights reserved.                    |
*******************************************************************************/

package tetrisjava;

/**
 * The Tetris game board
 * @author Jason Whatson
 * @version 1.1
 */
public class Board {
    
    /**
     * The height of the board
     */
    public final static int BoardHeight = 20;
    /**
     * The width of the board
     */
    public final static int BoardWidth = 11;
    
    private int[][] BoardArray = new int[20][11];
    /**
     * Creates a new board instance
     */
    public Board() {
       
    }
    
    /**
     * Returns an array represnetion of the board, a blank space on the board is 0.
     * @return An array represnetion of the board
     */
    public int[][] getBoardArray(){
        return BoardArray;
    }
    
    /**
     * Places a Tetris game piece on the game board
     * @param Piece An 2d array (usally a representation of a piece) to place on the board
     * @param R The Row the piece will be placed
     * @param C The Column the piece will be placed
     * @param test True, if you only want to test if a piece can be placed 
     * on the board in the definded row/column position. 
     * False if you would actually like to place the piece there 
     * (will become part of the board)
     * @return 
     */
    public int placePiece(int[][] Piece, int R, int C,boolean test){
        int OrigonalC = C;
        for (int r = 0;r < Piece.length;r++){   
            for (int c = 0;c < Piece[0].length;c++){ 
                try{
                if (this.BoardArray[R][C] != 0 && Piece[r][c] != 0){
                    return 1;
                }else{
                  if (test == false){
                      this.BoardArray[R][C] |= Piece[r][c]; 
                  }
                  C++;
                }
                }catch (java.lang.ArrayIndexOutOfBoundsException ex){return 1;}
                    
            }
            R++;
            C = OrigonalC;
        }
        return 0;
    }
    
    private void removeLine(int y) {
        if (y < 0 || y >= BoardHeight) {
            return;
        }
        for (; y > 0; y--) {
            for (int x = 0; x < BoardWidth; x++) {
                BoardArray[y][x] = BoardArray[y - 1][x];
            }
        }
        for (int x = 0; x < BoardWidth; x++) {
            BoardArray[0][x] = 0;
        }
    }
    
    /**
     * Checks to see if the games over (a placed piece is on the top row) 
     * I may change this
     * @return True if game is over, false if it is not
     */
    public boolean gameOver(){
        for (int i = 0; i < BoardArray[0].length; i++){
            if (BoardArray[0][i] != 0){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Calculates the number of rows completed, and removes these rows.
     * Also, adds new blank rows to the top of the board.
     * @return The number of rows completed after the last call to the 
     */
    public int calculatePoints(){
        
        int RowsComplete = 0;
        int CellsInRow = 0;
        
        for (int r = 0;r < BoardArray.length;r++){   
            for (int c = 0;c < BoardArray[0].length;c++){ 
                if (BoardArray[r][c] != 0){
                    CellsInRow++;
                }
            }
            
           if (CellsInRow >= BoardArray[0].length){
                removeLine(r);
                RowsComplete++;
           }
            
           CellsInRow = 0;
        }
        
        return RowsComplete;
    }
    
}
