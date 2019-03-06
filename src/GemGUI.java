import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.image.*;

/**
 * This class displays and controls the GUI for a simple Gem Matching Game.
 */
public class GemGUI extends JFrame{
    // Instance Variables
    private static final int RECT_SIZE = 75;
    private static final int OVAL_SIZE = 71;
    private static final int MAX_BOARD_SIZE = 12;
    
    private int numRows, numCols, stage, totalScore;
    private boolean isVisible,isImages;
    
    private GemManager game;
    private GameBoard board;
    private BufferedImage[] images;
    
    // Array of Colors provides reference for type integer when called to paint.
    private Color colors[] = {Color.WHITE,Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW,Color.BLACK,Color.CYAN,Color.MAGENTA,Color.PINK};
    // Invoke ImageArrays to get file loading out of the way.
    private ImageArrays array = new ImageArrays("sprite_sheet.jpg");
    
    /**
     * Constructor for GemGUI, builds the playing area and information panel in a BorderLayout. 
     */
    public GemGUI(){
        super("Poor Man's Bejeweled");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        isVisible = false; // Do not paint board until start button is clicked.
        stage = -1; // board not clickable until start.
        board = new GameBoard();
        JPanel panel = new JPanel(new BorderLayout(0,30));
        
        panel.add(board, BorderLayout.CENTER);
        panel.add(new InfoPanel(), BorderLayout.LINE_END);
        
        setContentPane(panel);
        pack();
    }
    
    /**
     * Constructs a new board with given parameters.  Defaults score to zero.  
     * @param row number of rows for board
     * @param col number of columns for board
     * @param symb number of symbols to be used for gems
     */
    public void buildBoard(int row, int col, int symb){
        game = new GemManager(row,col,symb,16);
        totalScore = 0;
        
        runGame();
    }
    
    /**
     * Changes type of Gems and removes and shifts them in the board while there are Gems to be removed
     */
    public void runGame(){
        int numToRemove = 0;
        
        while(true){
            numToRemove = game.checkBoard();
            if(numToRemove == 0){
                break;
            }
            game.changeType();
            game.shiftGems();
        }
    }
    
    /**
     * The GameBoard class contains a custom JPanel that displays and interacts with the game board.
     */
    public class GameBoard extends JPanel implements ActionListener{
        // Instance Variables
        private int mx1,mx2,my1,my2,x1,y1,x2,y2,multiplier;
        private Timer timer = new Timer(1000,this);
        
        /**
         * Constructor for GameBoard.  Instantiates all x and y coordinates and holders to -1 so they are not represented
         * on the board initially.  Sets timer delay for animations, board size, color and adds a mouseListener for the entire panel.
         */
        public GameBoard(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            setPreferredSize(new Dimension(MAX_BOARD_SIZE * RECT_SIZE, MAX_BOARD_SIZE * RECT_SIZE));
            setBackground(Color.WHITE);
            setOpaque(true);
            
            mx1 = my1 = mx2 = my2 = -1;
            x1 = y1 = x2 = y2 = -1;
            
            // Anonymous class to handle mouse clicks.
            addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    clickAction(me);    
                }
            });
        }
        
        /**
         * this method takes in mouse clicks and determines if there is a valid swap.  
         * Once that valid swap is found it activates the timer and advances the stage.
         * @param me mouse click.
         */
        public void clickAction(MouseEvent me){
            if(stage == 0){
                // First click
                if(mx1 == -1 && my1 == -1){
                    mx1 = me.getX();
                    my1 = me.getY();
                    x1 = mx1/RECT_SIZE;
                    y1 = my1/RECT_SIZE;
                    
                    if(x1 < numCols && y1 < numRows){
                        repaint();
                    } else {
                        mx1 = my1 = -1;
                        x1 = y1 = -1;
                    }
                }
                // Second click
                else {
                    mx2 = me.getX();
                    my2 = me.getY();
                    x2 = mx2/RECT_SIZE;
                    y2 = my2/RECT_SIZE;
                    // Check if in bounds
                    if(x2 >= numCols || y2 >= numRows){
                        mx2 = my2 = -1;
                        x2 = y2 = -1;
                    }
                    // Same square deselection
                    else if(x1 == x2 && y1 == y2){
                        mx1 = my1 = mx2 = my2 = -1;
                        x1 = y1 = x2 = y2 = -1;
                        repaint();
                    }
                    // Adjacent square swap 
                    else if(x1 == x2 && Math.abs(y1 - y2) == 1 || 
                            y1 == y2 && Math.abs(x1 - x2) == 1){
                        stage = 1;
                        timer.start();
                    }
                    // Non-adajacent square new selection
                    else {
                        mx1 = mx2;
                        my1 = my2;
                        x1 = x2;
                        y1 = y2;
                        mx2 = my2 = x2 = y2 = -1;
                        repaint();
                    }
                }
            }
        }
        
        /**
         * This method uses the ticks of the timer to repaint a simple animation of the board
         * once a valid swap has been found. It first swaps the gem types and repaints. Then
         * any 3+ matches that exists are flagged for removal and animated to match the background and
         * thus "disappear." Finally, it shifts the gems down the board and fills the new empty spaces with
         * random gem types
         * @param e each tick of the timer
         */    
        public void actionPerformed(ActionEvent e){
            int numDel;
            
            // 1: swapGems
            if(stage == 1){ 
                multiplier = 1;
                game.swapGems(x1,y1,x2,y2);
                mx1 = my1 = mx2 = my2 = -1;
                x1 = y1 = x2 = y2 = -1;
                repaint();
                stage = 2;
            }
            // 2: checkBoard and changeType
            else if(stage == 2){
                numDel = game.checkBoard();
                
                if(numDel > 0){
                    game.changeType();
                    totalScore += game.getTurnScore() * multiplier;
                    multiplier++;
                    repaint();
                    stage = 3;
                } else {
                    stage = 0;
                    timer.stop();
                }
            }
            // 3: shiftGems
            else if(stage == 3){
                game.shiftGems();
                repaint();
                stage = 2;
            }
        }
        
        /**
         * This paints the board with the proper gem representations, but only when the game has
         * been initially started, or when it is not paused.  It also paints a highlighting square 
         * around the initially selected gem for swaps
         * @param g graphics to paint
         */        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if(isVisible){
                for(int y = 0; y < numRows; y++){    
                    for(int x = 0; x < numCols; x++){
                        if(!isImages){
                            g.setColor(colors[game.getGem(x,y).getType()]);    
                            g.fillOval(x * RECT_SIZE + (RECT_SIZE - OVAL_SIZE)/2,
                                       y * RECT_SIZE + (RECT_SIZE - OVAL_SIZE)/2,
                                       OVAL_SIZE, OVAL_SIZE);
                        } else {
                            g.drawImage(images[game.getGem(x,y).getType()],
                                x * RECT_SIZE,y * RECT_SIZE,RECT_SIZE,RECT_SIZE,null);
                        }
                    }
                }
                
                if(x1 >= 0 && y1 >= 0){
                    g.setColor(Color.BLACK);
                    g.drawRect(x1 * RECT_SIZE, y1 * RECT_SIZE, RECT_SIZE, RECT_SIZE);
                }
            }
        }
    }
    
    /**
     * This class creates a custom Panel to display all user needed information.  It contains a Start and Pause/Continue button,
     * displays the time remaining in the round, and the score the player has earned.
     */
    public class InfoPanel extends JPanel implements ActionListener{
        // Instance Variables
        private int secs;
        private boolean paused, isStarted;
        private SelectPanel selection;
        private Timer gameTimer;
        
        private JLabel countdown = new JLabel("<html><br>&nbsp Time: </html>");
        private JLabel scoreBoard = new JLabel("<html><br>&nbsp Score: </html>");
        private JButton startButton = new JButton("Start Game");
        private JButton pauseButton = new JButton("Pause");
        private JLabel time,score;
        
        
        
        
        /**
         * Constructor for the InfoPanel.  Organizes data using BorderLayout and listens for mouse clicks on implemented buttons.
         */
        public InfoPanel(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(250, MAX_BOARD_SIZE * RECT_SIZE));
            
            secs = 100;
            paused = false;
            isStarted = false;
            
            selection = new SelectPanel();
            gameTimer = new Timer(750,this);
            JPanel buttonPanel = new JPanel();
            
            countdown.setFont(new Font("Monospaced", Font.PLAIN, 20));
            scoreBoard.setFont(new Font("Monospaced", Font.PLAIN, 20));
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            
            time = new JLabel(" "+secs+" ",SwingConstants.CENTER);
            time.setFont(new Font("Monospaced", Font.PLAIN, 20));
            time.setMaximumSize(new Dimension(200,20));
            
            score = new JLabel(" "+totalScore+" ",SwingConstants.CENTER);
            score.setFont(new Font("Monospaced", Font.PLAIN, 20));
            score.setMaximumSize(new Dimension(200,20));
            
            panel.add(countdown);
            panel.add(time);
            panel.add(scoreBoard);
            panel.add(score);
            
            buttonPanel.add(startButton);
            buttonPanel.add(pauseButton);
            
            add(panel, BorderLayout.PAGE_START);
            add(buttonPanel, BorderLayout.PAGE_END);
            add(selection,BorderLayout.CENTER);
            
            // Anonymous class too listen to mouse clicks on the start button.            
            startButton.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    startGame();
                }
            });
            
            // Anonymous class to listen for mouse clicks on the pause button.
            pauseButton.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    pauseGame();
                }
            });
        }
        
        /**
         * Initializes GUI.  Allows board to be painted, starts the gameTimer,
         * activates mouseListener on the board and instantiates the parameters of the board.
         */
        public void startGame(){
            secs = 100;
            totalScore = 0;
            isVisible = true;
            isStarted = true;
            
            buildBoard(selection.getRows(),selection.getCols(),selection.getSymb());
            
            if(!selection.getImg().equals("circles")){
                images = array.getArray(selection.getImg());
                isImages = true;
            } else {
                isImages = false;
            }
            
            numRows = game.getNumRows();
            numCols = game.getNumCols();
            stage = 0;
            
            gameTimer.start();
            board.repaint();
        }
        
        /**
         * When game is active, pauses game play.  Stops the timer, removes visibility of the board to prevent cheating.
         * It also disallows clicking on the game board. When the game is already paused, reinstates game play by 
         * repainting board and restarting the game timer. Allows clicking on board for normal play to resume.
         */
        public void pauseGame(){
            if(!paused && isStarted){
                gameTimer.stop();
                pauseButton.setText("Continue");
                paused = true;
                isVisible = false;
                stage = -1;
                board.repaint();
            } 
            else if(isStarted) {
                gameTimer.start();
                pauseButton.setText("Pause");
                paused = false;
                isVisible = true;
                stage = 0;
                board.repaint();
            }
        }
        
        /**
         * Event listener for the game timer.  Decrements time, updates display of time remaining and 
         * ends game and displays message when time runs out.
         * @param e each tick of the timer.
         */
        public void actionPerformed(ActionEvent e){
            if(secs >= 0){
                countdown.setText("<html><br>&nbsp Time: </html>");
                time.setText(" "+secs);
                secs--;
            }
            
            scoreBoard.setText("<html><br>&nbsp Score: </html>");
            score.setText(" "+totalScore+" ");
            
            if(secs < 0 && stage == 0){
                gameTimer.stop();
                secs = 100;
                JOptionPane.showMessageDialog(this, "Game Over!");
                stage = -1;
            }
        }
    }
}

