import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;

/**
 * This class creates the drop down menus for choosing paramaters for board
 * customization for the GameBoard in GemGUI.  It consists of arrays of the
 * proper type for the selections that are being made. The JComboBox and
 * JLabel of its description are added to a JPanel to easily create one
 * single object in GameBoard.
 */
public class SelectPanel extends JPanel{
    // Instance Variables
    private Integer[] rowSelect = {8,9,10,11,12};
    private Integer[] colSelect = {8,9,10,11,12};        
    private Integer[] symbSelect = {3,4,5,6,7,8};
    private String[] imgSelect = {"circles","animals","gems","mario","gems 2"};
    
    private JComboBox<Integer> rowBox = new JComboBox<Integer>(rowSelect);
    private JComboBox<Integer> colBox = new JComboBox<Integer>(colSelect);
    private JComboBox<Integer> symbBox = new JComboBox<Integer>(symbSelect);
    private JComboBox<String> imgBox = new JComboBox<String>(imgSelect);
    
    /**
     * Constructor creates all necessary JPanels and JLabels and sets a
     * common font for all JLabels.
     */
    public SelectPanel(){
        JPanel rowPanel = new JPanel();
        JPanel colPanel = new JPanel();
        JPanel symbPanel = new JPanel();
        JPanel imgPanel = new JPanel();
        
        JLabel label = new JLabel("Customize the Game!");
        JLabel rowLabel = new JLabel("Number of rows: ");
        JLabel colLabel = new JLabel("Number of columns: ");
        JLabel symbLabel = new JLabel("Number of symbols: ");
        JLabel imgLabel = new JLabel("Type of pieces: ");
        
        label.setFont(new Font("Monospaced", Font.PLAIN,18));
        rowLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        colLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        symbLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        imgLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        
        rowPanel.add(rowLabel);
        rowPanel.add(rowBox);
        
        colPanel.add(colLabel);
        colPanel.add(colBox);
        
        symbPanel.add(symbLabel);
        symbPanel.add(symbBox);
        
        imgPanel.add(imgLabel);
        imgPanel.add(imgBox);
        
        add(label);
        add(rowPanel);
        add(colPanel);
        add(symbPanel);
        add(imgPanel);
    }
    
    /**
     * Get method that returns the value chosen in the Rows JComboBox
     * @return int value of Integer object selected
     */
    public int getRows(){
        return (int) rowBox.getSelectedItem();
    }
    
    /**
     * Get method that returns the value chosen in the Cols JComboBox
     * @return int value of Integer object selected
     */
    public int getCols(){
        return (int) colBox.getSelectedItem();
    }
    
    /**
     * Get method that returns the value chosen in the Symb JComboBox
     * @return int value of Integer object selected
     */
    public int getSymb(){
        return (int) symbBox.getSelectedItem();
    }
    
    /**
     * Get method that returns the value chosen in the Img JComboBox
     * @return String value of Object selected
     */
    public String getImg(){
        return (String) imgBox.getSelectedItem();
    }
}