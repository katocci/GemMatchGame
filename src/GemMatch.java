import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class simply contains a main method to create and run an instance
 * of GemGUI for a working Gem Match Game
 * @param args command line arguments
 */
public class GemMatch{
    public static void main(String[] args){
        GemGUI graphics = new GemGUI();
        graphics.setVisible(true);
    }
}