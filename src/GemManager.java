import java.util.*;
import java.util.List;

/**
 * This class contains the bookkeeping for Gems in a simple Match 3 game.  It checks
 * for 3 or more in a row in the horizontal and vertical.  Removes those pieces, condenses
 * the board and refills any open spots at the tops of columns with new random pieces.
 * @version date(in_CS_251_003 format: 2016 - 04 - 22)
 * @author Kathryn Tocci
 * File: GemManager.java
 */
public class GemManager{
    
    //Instance Variables
    private int numRows, numCols, numSymbols, turnScore;
    private int[] numDeleteCol;
    private Gem[][] board; // 2D Array to represent board
    private List<Gem> gemsToRemove; // Collection of Gems flagged for removal
    private List<Gem> gemsToCheck; // Collection of Gems to check for matches after board shift
    private Random rand; 
    private enum Direction{N, S, E, W};  // Used for match checking purposes
    
    /* Character representations for non-GUI game to correspond index to type.
       Used for testing purposes.*/
    private char[] charSymbol = {'_','*','#','&','o','x','$','+'}; 
    
    /**
     * Constructor for testing because of inclusion of seed for random num generator
     * @param rows num rows for board
     * @param cols num cols for board
     * @param symb num symbols to be used on board
     * @param randSeed constant for random num generator
     */
    public GemManager(int rows, int cols, int symb, long randSeed){
        this.numRows = rows;
        this.numCols = cols;
        this.numSymbols = symb; 
        numDeleteCol = new int[numCols]; // Keeps track of num to remove per col
        rand = new Random(randSeed);
        board = new Gem[numCols][numRows];
        gemsToRemove = new LinkedList<Gem>();
        gemsToCheck = new LinkedList<Gem>();
        turnScore = 0;
        
        initBoard();
    }
    
    /**
     * Constructor for live game that uses the current system time to select
     * the random seed, thus creating a new board for every game.  Calls the initial
     * constructor.
     * @param rows num rows for board
     * @param cols num cols for board
     * @param symb num symbols to be used on board
     */
    public GemManager(int rows, int cols, int symb){
        this(rows, cols, symb, System.nanoTime());
    }
    
    /**
     * Get method for numRows.
     * @return number of rows in board
     */
    public int getNumRows(){
        return numRows;
    }
    
    /**
     * Get method for numCols.
     * @return number of columns in board
     */
    public int getNumCols(){
        return numCols;
    }
    
    /**
     * Get method for specific Gem.
     * @param x x-coordinate of Gem
     * @param y y-coordinate of Gem
     * @return Gem at (x,y) location in board
     */
    public Gem getGem(int x, int y){
        return board[x][y];
    }
    
    /**
     * Get method for turnScore.
     * @return score gained during each cascade
     */
    public int getTurnScore(){
        return turnScore;
    }
    
    /**
     * Initialize the board with random Gems to fill the rows and cols.
     */
    public void initBoard(){
        for(int y = 0; y < numRows; y++){
            for(int x = 0; x < numCols; x++){
                board[x][y] = new Gem(x,y,randType());
                gemsToCheck.add(board[x][y]);
            }
        }
    }
    
    /**
     * Selects random number based upon how many symbols are in game
     * @return random number
     */
    public int randType(){
        int randNum = rand.nextInt(numSymbols) + 1; // +1 because zeero is reserved type
        return randNum;
    }

    /**
     * Find matches of 3 or more.
     * @return Size of Collection of Gems flagged for removal
     */
    public int checkBoard(){
        int x, y;
        
        turnScore = 0;
        
        for(Gem gem: gemsToCheck){
            x = gem.getX();
            y = gem.getY();
            horizontalCheck(x,y);
            verticalCheck(x,y);
        } 
        gemsToCheck.clear();
        
        return gemsToRemove.size();
    }

    /**
     * Checks to the left and right for horizontal combos of 3 or more on board around
     * swapped piece. Flags pieces for removal in the event of chains of 3 or more.
     * @param x0 x coordinate for starting point
     * @param y0 y coordinate for starting point
     */
    public void horizontalCheck(int x0, int y0){
        int n1, n2, totalInRow;
        boolean scoreFlag = false; // Ensures proper scoring with gems that are in 2 matching strings.
                
        n1 = numInRow(x0, y0, Direction.E);
        n2 = numInRow(x0, y0, Direction.W);
        totalInRow = n1 + n2 + 1;
        
        if(totalInRow >= 3){
            for(int x = x0 - n2; x <= x0 + n1; x++){
                if(!gemsToRemove.contains(board[x][y0])){
                    if(!scoreFlag){
                        scoreFlag = true;
                        chainScore(totalInRow);
                    }
                    gemsToRemove.add(board[x][y0]);
                }
            }
        }
    }
    
    /**
     * Checks up and down for vertical combos of 3 or more on board around
     * swapped piece. Flags pieces for removal in the event of chains of 3 or more.
     * @param x0 x coordinate for starting point
     * @param y0 y coordinate for starting point
     */
    public void verticalCheck(int x0, int y0){
        int n1, n2, totalInRow;
        boolean scoreFlag = false; // Ensures proper scoring with gems that are in 2 matching strings.
        
        n1 = numInRow(x0, y0, Direction.N);
        n2 = numInRow(x0, y0, Direction.S);
        totalInRow = n1 + n2 + 1;
        
        if(totalInRow >= 3){
            for(int y = y0 - n2; y <= y0 + n1; y++){
                if(!gemsToRemove.contains(board[x0][y])){
                    if(!scoreFlag){
                        scoreFlag = true;
                        chainScore(totalInRow);
                    }
                    gemsToRemove.add(board[x0][y]);
                }
            }
        }
    }   
    
    /**
     * Searches in indicated Direction for matching Gems.  Keeps count. Ensures
     * search is conducted within the bounds of the board.
     * @param x0 x coord of starting point
     * @param y0 y coord of starting point
     * @param dir Direction to search
     * @return num of consecutive symbols
     */
    public int numInRow(int x0, int y0, Direction dir){
        int[] newPos = new int[2];
        int type = board[x0][y0].getType();
        int x = x0;
        int y = y0;
        int count = 0;
        
        while(true){
            newPos = move(x, y, dir);
            x = newPos[0];
            y = newPos[1];
            if(!isInBoard(x,y)){
                break;
            }
            if(type == board[x][y].getType()){
                count += 1;
            }
            else{
                break;
            }
        }
        return count;
    }
    
    /**
     * Determines if the row and column parameter are in the board.
     * @param x0 x coordinate to check
     * @param y0 y coordinate to check
     * @return false if coordinate is not in the board.
     */
    public boolean isInBoard(int x0, int y0){
        if( (x0 >= 0 && x0 < numCols) && (y0 >= 0 && y0 < numRows)){
            return true;
        } else {
            return false; 
        }
    }
    
     /**
     * Changes coordinates in proper direction for searching for match purposes.
     * @param x0 x coord of starting point
     * @param y0 y coord of starting point
     * @param dir Direction to search
     * @return int array of new coordinates
     */
    public int[] move(int x0, int y0, Direction dir){
        int newPos[] = {x0, y0};
        
        switch(dir){
            case N: 
                newPos[1] += 1;
                break;
            case S: 
                newPos[1] -= 1;
                break;
            case W: 
                newPos[0] -= 1;
                break;
            case E: 
                newPos[0] += 1;
        }
        return newPos;
    }
    
    /**
     * Calculates score for each horizontal and vertical matching string of Gems.
     * @param n number of Gems matching string.
     */
    public void chainScore(int n){
        turnScore += 100 * n + (n - 3) * 100; 
    }
    
    /**
     * Removes Gems that are flagged for removal and shift Gems down while refilling 
     * the tops of columns with new random Gems.
     */
    public void changeType(){
        for(int x = 0; x < numCols; x++){
            numDeleteCol[x] = 0;
        }
        // Change type of all Gems in  gemsToRemove to 0
        for(Gem gemDelete : gemsToRemove){
            gemDelete.setType(0);
            numDeleteCol[gemDelete.getX()]++;
        }
        
        gemsToRemove.clear(); // Empty after flags are changed on current play
    }
    
    /**
     * Loop through each column starting at the top, shifting and replacing
     * Gems as necessary
     */
    public void shiftGems() {
        LinkedList<Integer> types = new LinkedList<Integer>(); // Holds shifting Gems
        int numDelete, type;
        Gem gem;
        
        for(int x = 0; x < numCols; x++){
            numDelete = numDeleteCol[x];
            types.clear();
            for(int i = 0; i < numDelete; i++){
                types.add(randType());
            }
            for(int y = 0; y < numRows; y++){
                if(types.size() == 0){
                    break;
                }
                gem = board[x][y];
                type = gem.getType();
                if(type != 0){
                    types.add(type);
                }
                gem.setType(types.pop());
                gemsToCheck.add(gem);
            }
        }
    }
    
    /**
     * Swaps Gem types of the two board positions selected. Ensures the two are
     * adjacent to each other.
     * @param x1 x coordinate of first Gem
     * @param y1 y coordinate of first Gem
     * @param x2 x coordinate of second Gem
     * @param y2 y coordinate of second Gem
     */
    public void swapGems(int x1, int y1, int x2, int y2){
        int tempType = board[x1][y1].getType();
        
        gemsToCheck.add(board[x1][y1]);
        gemsToCheck.add(board[x2][y2]);
        
        board[x1][y1].setType(board[x2][y2].getType());
        board[x2][y2].setType(tempType);
    }
        
    /**
     * Overrides toString method to display the board in characters.
     * @return String representation of board
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        for(int y = 0; y < numRows; y++){
            for(int x = 0; x < numCols; x++){
                sb.append(charSymbol[board[x][y].getType()]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}