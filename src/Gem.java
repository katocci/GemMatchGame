/**
 * This class holds the x and y board coordinates and holds an integer that indicates
 * the type of char/color/sprite to be outputted when the board is printed.
 * @version date(in_CS_251_003 format: 2016 - 04 - 22)
 * @author Kathryn Tocci
 * File: Gem.java
 */
public class Gem{
    //Instance variables
    private int x, y, type;
    
    /**
     * Basic constructor
     * @param x x coordinate on board
     * @param y y coordinate on board
     * @param type integer representing separate types
     */
    public Gem(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Get method for type.
     * @return type
     */
    public int getType(){
        return type;
    }
    
     /**
     * Get method for x.
     * @return x
     */
    public int getX(){
        return x;
    }
     /**
     * Get method for y.
     * @return y
     */
    public int getY(){
        return y;
    }
    
    /**
     * Set method for type.
     * @param type integer from 0 to however many symbols are being used in game
     */
    public void setType(int type){
        this.type = type;
    }
    
    /**
     * Simple override of the toString method for testing purposes
     * @return String of coordinates and type
     */
    public String toString(){
        return "("+this.x+","+this.y+"," +this.type+")";
    }
}