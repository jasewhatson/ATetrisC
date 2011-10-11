/*******************************************************************************
|	Copyright 2006	Jason Whatson. All rights reserved.                    |
*******************************************************************************/

package tetrisjava;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import timer.*;

/**
 * Handles how the game plays, user key presses and the 
 * frame for the game canavas.
 *
 *Runnable - For threads
 *KeyListner - key events
 *Alarmable - Timer events
 *
 * @author Jason Whatson
 * @version 1.0
 */
public class TetrisGame extends javax.swing.JFrame implements Runnable,KeyListener,Alarmable{
    
    //The points recived by the player correspoinding with how many rows cleared at once
    private static final int ONEPOINT = 100;
    private static final int TWOPOINT = 300;
    private static final int THREEPOINT = 600;
    private static final int FOURPOINT = 900;
    
    //The initial time between a piece drops a row
    private static final int STARTINGDROPWAIT = 500;
    
    //After every the level the drop will get quicker by this much
    private static final int DROPTIMEREDUCTION = 20;
    
    //Minutes until graduation to next level
    private static final int MINUTESTILLSPEEDUP = 1;
    
    //Main game loop thread
    private Thread t1;
    //Current and Next Pieces
    private TetrisPiece cp,np;
    //Instance of the canvas where we draw our game
    private GameCanvas gc;
    //The game board
    private Board b1 = new Board();
    //If the game is over or not
    private static boolean GameOver = false;
    //If the game is paused or not
    private boolean Paused = false;
    //Total points scored by the player
    private long TotalPoints = 0;
    //Total number of rows the player has completed
    private int RowsCleared = 0;
    //The current level the player is on
    private int Level = 0;
    //The ammount of time until the piece drops a row
    private int DropWait = STARTINGDROPWAIT;
    //Holds the result of tempory tests such as piece placement
    private int TempRes = 0;
    //Roatate dirrection
    private char RotateDirection = 'R';
    //Time until player moves onto next level
    private static int MinutesUntilLevelChange = 1;
    //Creates a new timer to count time until next level
    private CountDown countdown = new timer.CountDown(0,MINUTESTILLSPEEDUP,0,0);

    /** Creates a new instance of TetrisGame */
    public TetrisGame() {
        
        //Register ourself as wanting to be notified on Alarmable events
        countdown.addTimesUpListener(this);
        
        //Creates a new canvas which draws the game, then adds it to us (the JFrame)
        gc = new GameCanvas();
        this.add(gc);
               
        //Creates a new random tetris piece as the current piece
        cp = new TetrisPiece();
        cp.getRandomPiece();
        
        //Creates a tetris peice which will be the next piece in play after
        //the current piece is placed
        np = new TetrisPiece();
        np.getRandomPiece();
        
        //We dont want the game canvas to get focus
        gc.setFocusable(false);
                
        //Set the direction to right enitially 
        gc.setRotationDirection("Right");

        //Registers ourself as wanting to be noticed when key events arise.
        this.addKeyListener(this);
        
        //Gets the game rolling
        this.start();  
    }

    /**
     * The game loop
     */
    public void run() {
        
        //Loop the game until game over has occured
        while (GameOver == false){
            
            //If the game is paused do nothing, else...
            if (Paused == false){           
                
                //Get the time to go until next level, show it to the user
                gc.setSeconds(Integer.parseInt(countdown.formatAsDate(
                countdown.getTimeToGo()).split(":")[2]));
                
                //Checks to see if its possible for the current piece to move down a row
                TempRes = b1.placePiece(cp.getCurrentPiece(),
                          cp.getRowLocation() + 1,cp.getColumnLocation(),true);

                //If its not posible to move down a row
                if (TempRes == 1){
                    //Place the piece in its current possition
                    this.piecePlaced();
                //If it is posible to move the piece down
                }else{
                    //Move the piece down a row
                    cp.moveDown();
                }

                //Draw the game
                this.drawGame();

            }
                //Sleep the game for the length of the drop wait
                try { 
                    t1.sleep(DropWait);
                } catch (InterruptedException ex) {ex.printStackTrace();}
            
        }
        
        //When the game ends clean up memory
        System.gc();
    }
    
    //Places a tetris piece on the board and works out if any rows have been
    //completed
    private void piecePlaced(){
        int Rows = 0;
        
        //Place the piece at its current place on the board
        //(it will now become part of the board)
        b1.placePiece(cp.getCurrentPiece(),
        cp.getRowLocation(),cp.getColumnLocation(),false);
        
        //Test to see if the game is over (no room to place another piece)
        if(b1.gameOver() == true){
            //If so stop the game loop and set game over for drawing on the canavs
            this.GameOver = true;
            gc.setGameOver(true);
        //Else, the games not over and we new need a new tetris piece
        }else{
            //The next piece now becomes our current piece
            cp = np;
            //Our next piece is now assigned a new random piece
            np = new TetrisPiece();
            np.getRandomPiece();
            
            //Work out if the placed piece has finished any rows and how many its finished
            Rows = b1.calculatePoints();
            //Total the number of rows completed
            this.RowsCleared += Rows;
            //Calculate the points earned
            this.tallyPoints(Rows);
        }
    }
    
    
    /**
     * Starts the game loop
     */
    public void start(){
        this.GameOver = false;        
        t1 = new Thread(this);
        t1.start();
    }
    
    /**
     *Restarts the game
     */
    private void restart() {
        //Create a new timer
        countdown = new timer.CountDown(0,MINUTESTILLSPEEDUP,0,0);
        countdown.addTimesUpListener(this);
        
        //Creates a new random tetris piece as the current piece
        cp = new TetrisPiece();
        cp.getRandomPiece();
        
        //Creates a tetris peice which will be the next piece in play after
        //the current piece is placed
        np = new TetrisPiece();
        np.getRandomPiece();
        
        b1 = new Board();

        this.DropWait = STARTINGDROPWAIT;

        Paused = false;
        gc.setPaused(false);
        gc.setGameOver(false);
        gc.setLevel(0);
        TotalPoints = 0;
        RowsCleared = 0;
        Level = 0;
            
        this.GameOver = true;
        
        //This needs to be here to give time for the current game to end (a bit hacky)
        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        this.start(); //Gets the game rolling again
    }
    
    //Works out points earned. More points for more rows completed at once
    private void tallyPoints(int Rows){
        switch(Rows){
            case 0:
                this.TotalPoints += 0;
                break;
            case 1:
                this.TotalPoints += this.ONEPOINT;
                break;
            case 2:
                this.TotalPoints += this.TWOPOINT;
                break;
            case 3: 
                this.TotalPoints += this.THREEPOINT;
                break;
            case 4: 
                this.TotalPoints += this.FOURPOINT;
                break;
        }
    }
    
    //Sets values to the game canvas, and redraws the canavs
    /*Maybe some things in here dont need to be set on all redraws, 
    but they are esentially just pointers. Considure changing this for proformamce boosts*/
    private void drawGame(){
        gc.setScore(this.TotalPoints); //Sets score value
        gc.setRowsComplete(this.RowsCleared); //Sets rows cleared value
        gc.setNextPiece(np.getCurrentPiece()); //Sets the next piece
        gc.setArrayBoard(b1.getBoardArray()); //Sets the board 
        //Sets the location to draw the current piece
        gc.setPaintLocation(cp.getColumnLocation(),cp.getRowLocation()); 
        gc.setArrayPiece(cp.getCurrentPiece()); //Sets the current piece
        gc.repaint();
      }


    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        
        //Down key is pressed
        if (e.getKeyCode() == e.VK_DOWN){
            
            //Check if the current piece can be moved a row down
            TempRes = b1.placePiece(cp.getCurrentPiece(),
                      cp.getRowLocation() + 1,cp.getColumnLocation(),true);

            //If it cant be moved down place the piece in the current position
            if (TempRes == 1){
                this.piecePlaced(); //Place the piece on the board
            //Else if it can be moved down
            }else{
                this.TotalPoints++;
                cp.moveDown();
                this.drawGame();
            }

        //Else if the P Key is pressed
        }else if (e.getKeyCode() == e.VK_P){
            this.Paused = !this.Paused; //Toggle the game paused / Unpaused
            gc.setPaused(this.Paused);
            countdown.pause(this.Paused);
            this.drawGame(); //So pause shows up to user
            
        //Else if the up key is pressed
        }else if (e.getKeyCode() == e.VK_UP){
            
            //Maybe make the below a switch instead of if/else
            if (this.RotateDirection == 'R'){
            
                //Check to see if the piece can be rotated right
                TempRes = b1.placePiece(cp.rotateRight(true),
                          cp.getRowLocation(),cp.getColumnLocation(),true);
                
                //If its possible to rotate right
                if (TempRes != 1){
                    cp.rotateRight(false);
                    this.drawGame(); 
                }
                
            }else{
                //Check to see if the piece can be rotated left
                TempRes = b1.placePiece(cp.rotateLeft(true),
                          cp.getRowLocation(),cp.getColumnLocation(),true);
                 //If its possible to rotate right
                if (TempRes != 1){
                    cp.rotateLeft(false);
                    this.drawGame(); 
                }
            }

        //Else if the right key is pressed    
        } else if (e.getKeyCode() == e.VK_RIGHT){
            
            //Check to see if the piece can be moved right
            TempRes = b1.placePiece(cp.getCurrentPiece(),
                      cp.getRowLocation(),cp.getColumnLocation() + 1,true);
            
            //If the piece can be moved right
            if (TempRes != 1){
                //Move the piece up a column
                cp.moveLeftRight(false,true);
                this.drawGame(); 
            }
        //Else if the left key is pressed
        }else if (e.getKeyCode() == e.VK_LEFT){
            
            //Check to see if the piece can be moved left
            TempRes = b1.placePiece(cp.getCurrentPiece(),
                      cp.getRowLocation(),cp.getColumnLocation() - 1,true);
            
            //If the piece can be moved left
            if (TempRes != 1){
                //Move the piece down a column
                cp.moveLeftRight(true,false);
                this.drawGame(); 
            }   
        //Change rotation direction
        }else if (e.getKeyCode() == e.VK_R){
            //If its currently set to rotate right, make it go left
            if (this.RotateDirection == 'R'){
                this.RotateDirection = 'L';
                gc.setRotationDirection("Left");
            //Else make it go right
            }else{
                this.RotateDirection = 'R';
                gc.setRotationDirection("Right");
            }
            this.drawGame();
        } else if (e.getKeyCode() == e.VK_N){
           this.restart(); //Restart game
        }    
    }

    //Called when its time to move onto next level
    public void timesUp(TimerEvent Event) {
        
        this.DropWait -= this.DROPTIMEREDUCTION; //Make the game drop faster
        this.gc.setLevel(++this.Level); //Incress level
        countdown = new timer.CountDown(0,MINUTESTILLSPEEDUP,0,0); //New timer to time next level
        countdown.addTimesUpListener(this);
    }
    
}
