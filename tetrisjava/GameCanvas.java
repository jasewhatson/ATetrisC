/*******************************************************************************
|	Copyright 2006	Jason Whatson. All rights reserved.                    |
*******************************************************************************/

package tetrisjava;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Handles the drawing of the Tetris game to the screen
 * @author jason
 * @version 1.0
 */
public class GameCanvas extends Canvas{

    private final static int PIECEWIDTH = 16; //RENAME to block width
    private int[][] Piece,BoardArray,NextPiece;
    private int ColumnMultiplyer = 0,RowMultiplyer = 0;
    private int R,C;
    private boolean DrawGameOver = false;
    private boolean DrawPaused = false;
    private int CompletedRows = 0;
    private long Score = 0;
    private String RotationDirection = "";
    private int Level = 0;
    private int SecodnsTillNextLevel = 0;
    
    // The image that will contain everything that has been drawn on
    // bufferGraphics.
    private Image offscreen;
    
    // The object we will use to write with instead of the standard screen graphics
    private Graphics bufferGraphics;
    
    private static final int GAMEAREAWIDTH = Board.BoardWidth * PIECEWIDTH + 5;
    private static final int GAMEAREAHEIGHT = Board.BoardHeight * PIECEWIDTH + 3;
    
    private static final int GAMEAREAXLOC = 1;
    private static final int GAMEAREAYLOC = 1;
    
    private static final int NPAREAXLOC = Board.BoardWidth * PIECEWIDTH + 10;
    private static final int NPAREAYLOC = 1;
    
    private static final int NPWIDTH = 120;
    private static final int NPHEIGHT = 80;

    private static final int GAMEINFOWIDTH = 120;
    private static final int GAMEINFOHEIGHT = 239;
        
    private static final int GAMEINFOXLOC = Board.BoardWidth * PIECEWIDTH + 10;
    private static final int GAMEINFOYLOC = 85;
        
    //The size to draw the block, PIECEWIDTH is the block size with space buffering. BLOCKDRASIZE is it without
    private static final int BLOCKDRAWSIZEPIECE = 14;
    
    private static final int SCOREXLOC = Board.BoardWidth * PIECEWIDTH + 12;
    private static final int SCOREYLOC = 105;
    
    private static final int ROWSXLOC = Board.BoardWidth * PIECEWIDTH + 12;
    private static final int ROWSYLOC = 125;
        
    private static final int ROTATIONXLOC = Board.BoardWidth * PIECEWIDTH + 12;
    private static final int ROTATIONYLOC = 145;
    
    private static final int LEVELXLOC = Board.BoardWidth * PIECEWIDTH + 12;
    private static final int LEVELYLOC = 165;
    
    private static final int TIMELEFTXLOC = Board.BoardWidth * PIECEWIDTH + 12;
    private static final int TIMELEFTYLOC = 185;
    
    /**
     * Creates a new instance of Board
     */
    public GameCanvas() {
    }
    
    /**
     * Sets a 2d array to be drawn. Which should be the current piece in play.
     * @param arr The 2d array representing a inplay Tetris piece
     */
    public void setArrayPiece(int[][] arr){
        this.Piece = arr;
    }
    
    /**
     * Sets the next piece to be drawn
     * @param arr An 2d array represting the next piece to appear on the board.
     */
    public void setNextPiece(int[][] arr){
        this.NextPiece = arr;
    }
    
    /**
     * Sets if the game is over or not.
     * @param go If true, game over will drawn to the screen
     */
    public void setGameOver(boolean go){
        this.DrawGameOver = go;
    }
    
    /**
     * Sets the board to be drawn
     * @param arr The Row the piece will be placed
     */
    public void setArrayBoard(int[][] arr){
        this.BoardArray = arr;
    }
    
    /**
     * Sets the location to paint the in play Tetris piece
     * @param ColumnLocation Column which in play game piece will be drawn
     * @param RowLocation Row which in play game piece will be drawn
     */
    public void setPaintLocation(int ColumnLocation,int RowLocation){
        this.ColumnMultiplyer = ColumnLocation;
        this.RowMultiplyer = RowLocation;
    }
    
    /**
     * Sets the number of completed rows to be drawn
     * @param Rows The number of completed rows
     */
    public void setRowsComplete(int Rows){
        this.CompletedRows = Rows;
    }
    
    /**
     * Sets the players score to be drawn
     * @param Score The score 
     */
    public void setScore(long Score){
        this.Score = Score;
    }
    
    /**
     * Shows as game paused
     */
    public void setPaused(boolean Paused){
        this.DrawPaused = Paused;
    }
    
    /**
     * Sets the level # to draw
     */
    public void setLevel(int Level){
        this.Level = Level;
    }
    
    /**
     * sets the seconds left to draw
     */
    public void setSeconds(int secs){
        this.SecodnsTillNextLevel = secs;
    }
    
    /**
     * Sets the rotation direction to draw (word)
     */
    public void setRotationDirection(String Direction){
        RotationDirection = Direction;
    }
    
    /**
     * Overridden from super
     */
    public void update(Graphics g){
       this.paint(g);
    }

    /**
     * Overridden from super
     */
    public void paint(Graphics g) {
       
        //For back-buffer
        if (offscreen == null) {
           offscreen = createImage(this.getWidth(),this.getHeight());
           
        }
        
        //Create a double buffered image - Back-buffer
        bufferGraphics = offscreen.getGraphics();
        
        //Donno if this is needed - May reduce proformace if deleted
        bufferGraphics.clearRect(0, 0,this.getWidth(),this.getHeight());
  
        //Draw a black background
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0,0,this.getWidth(),this.getHeight());
        
        //Draw a white border around the board
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.drawRect(GAMEAREAXLOC,GAMEAREAYLOC,GAMEAREAWIDTH,GAMEAREAHEIGHT);
        
        //Draw a white border to make the 'next piece area'
        bufferGraphics.drawRect(NPAREAXLOC,NPAREAYLOC,NPWIDTH,NPHEIGHT);
        
        //Draw a white border to make a 'game info area'
        bufferGraphics.drawRect(GAMEINFOXLOC,GAMEINFOYLOC,GAMEINFOWIDTH,GAMEINFOHEIGHT);
        
        //Draws the piece
        int r = PIECEWIDTH * RowMultiplyer + 3,c = PIECEWIDTH * ColumnMultiplyer;
        
        for(int i=0; i < Piece.length;i++){
            for (int j=0; j < Piece[0].length;j++){
                if (Piece[i][j] != 0){
                    bufferGraphics.setColor(TetrisPiece.getPieceColour(Piece[i][j]));
                    bufferGraphics.drawRect(c + 5,r,BLOCKDRAWSIZEPIECE,BLOCKDRAWSIZEPIECE);
                }
                c += PIECEWIDTH;
            }
            c = PIECEWIDTH * ColumnMultiplyer;
            r += PIECEWIDTH;
            
        }
        
        //Draws the board
        r = 3; c = 0;
        for (int k=0; k < BoardArray.length;k++){
            for (int l=0; l < BoardArray[0].length;l++){
                 if (BoardArray[k][l] != 0){
                    bufferGraphics.setColor(TetrisPiece.getPieceColour(BoardArray[k][l]));
                    bufferGraphics.fillRect(c + 5,r,BLOCKDRAWSIZEPIECE,BLOCKDRAWSIZEPIECE);
                 }
                c += PIECEWIDTH;
            }
            c = 0;
            r += PIECEWIDTH;
        }
        bufferGraphics.setColor(Color.WHITE);        
        
        //Centers the next piece
        int midr = (NPHEIGHT - (this.NextPiece.length * PIECEWIDTH)) /2;
        int midc = (NPWIDTH - (this.NextPiece[0].length * PIECEWIDTH)) / 2;
        int R = 2 + midr,C = Board.BoardWidth * PIECEWIDTH + 5 + 5 + midc;
        //Draws the next piece
        for(int i=0; i < this.NextPiece.length;i++){
            for (int j=0; j < this.NextPiece[0].length;j++){
                if (this.NextPiece[i][j] != 0){
                    bufferGraphics.setColor(TetrisPiece.getPieceColour(this.NextPiece[i][j]));
                    bufferGraphics.drawRect(C,R,BLOCKDRAWSIZEPIECE,BLOCKDRAWSIZEPIECE);
                }
                C += PIECEWIDTH;
            }
            C = Board.BoardWidth * PIECEWIDTH + 10 + midc;
            R += PIECEWIDTH;
            
        }      
        
        //Draws side info status stuff
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.drawString("Rows: " + this.CompletedRows,ROWSXLOC,ROWSYLOC);
        
        bufferGraphics.drawString("Rotation: " + this.RotationDirection,ROTATIONXLOC,ROTATIONYLOC);

        bufferGraphics.drawString("Level: " + this.Level,LEVELXLOC,LEVELYLOC);
        
        bufferGraphics.drawString("Next level in: " + SecodnsTillNextLevel,TIMELEFTXLOC,TIMELEFTYLOC);
        

        if (Score >= 9999999){
            bufferGraphics.drawString("Total score: God like!",SCOREXLOC,SCOREYLOC);
        } else if (Score == 666){
            bufferGraphics.drawString("Total score: NOTB",SCOREXLOC,SCOREYLOC); //<-Number of the beast 
        }else{
             bufferGraphics.drawString("Total score: " + Score,SCOREXLOC,SCOREYLOC);
        }
        //If game is pause draw pause
        if (this.DrawPaused == true){
             bufferGraphics.setFont(new Font("", Font.BOLD, 13));
             bufferGraphics.setColor(Color.BLUE);
             bufferGraphics.drawString("PAUSED",Board.BoardWidth * PIECEWIDTH + 45,250);
        }
        
        //Draws game over, if it is set to
        if (this.DrawGameOver == true){
            bufferGraphics.setFont(new Font("", Font.BOLD, 13));
            bufferGraphics.setColor(Color.BLUE);
            bufferGraphics.drawString("GAME OVER!",Board.BoardWidth * PIECEWIDTH + 32,250);
        }
        
        //Draw author info
        bufferGraphics.setFont(new Font("", Font.PLAIN, 10));
        bufferGraphics.setColor(Color.BLUE);
        bufferGraphics.drawString("By Jason Whatson",Board.BoardWidth * PIECEWIDTH + 12,320);
        
        //Get rid of our current back buffered image - Else this would rape memory
        bufferGraphics.dispose();
        //Draw the double buffered image to the screen (flips it from memory)
        g.drawImage(offscreen,0,0,this); 
        
    }

}
