/*******************************************************************************
|	Copyright 2006	Jason Whatson. All rights reserved.                    |
*******************************************************************************/

/*
 * Main.java
 *
 * Created on January 1, 2007, 5:10 PM
 */

package tetrisjava;

import java.applet.Applet;
import java.awt.event.WindowAdapter;

/**
 * Creates a new Tetris game and starts a new game
 * @author Jason Whatson
 */
public class Main{
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Create a new game
        TetrisGame f = new TetrisGame();
        
        //On exit event close the window
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing() {
                System.exit(0);
            }
        });
        
        //Size of the window
        f.setSize(TetrisPiece.PIECELENGTH * Board.BoardWidth + 
        140,TetrisPiece.PIECELENGTH * Board.BoardHeight + 40);
        
        f.setVisible(true);
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
    }
}
