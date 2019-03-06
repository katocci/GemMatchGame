import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * This class reads in a JPEG file that is a sprite sheet for all of the 
 * imported images necessary to create a full set of Gem types to use
 * with the GemGUI.  The JPEG itself is preplanned so as to know the exact
 * dimensions needed to pull each sub-image for each type.  Once the image 
 * array is selected by the player, the matching image array is sent to the GUI.
 */
public class ImageArrays{
    // Instance Variables
    private BufferedImage image;
    private BufferedImage[] animals,gems,mario,gems2;
    
    /**
     * Constructor for ImageArrays. Loads the JPEG from file, creates arrays
     * of the proper length for the amount of types, then fills the arrays
     * with sub-images of predetermined size.
     * @param imageName String of file to be loaded
     */
    public ImageArrays(String imageName){
        //ClassLoader so it'll work in my .jar
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream(imageName);
        try{
            image = ImageIO.read(in);
            } catch (IOException ex){
                System.err.println("Error loading: "+ imageName);
        }
        
        animals = new BufferedImage[9];
        gems = new BufferedImage[9];
        mario = new BufferedImage[9];
        gems2 = new BufferedImage[9];
        
        for(int i = 0; i <= 8; i++){
            animals[i] = image.getSubimage(i * 80,0,75,75);
            gems[i] = image.getSubimage(i * 80,80,75,75);
            mario[i] = image.getSubimage(i * 80, 160, 75, 75);
            gems2[i] = image.getSubimage(i * 80, 240, 75, 75);
        }
    }
    
    /**
     * This method returns the array that matches the String sent by the caller.
     * @param name String of array name
     * @return BufferedImage array that matches String name
     */
    public BufferedImage[] getArray(String name){
        if(name.equals("animals")){
            return animals;
        } else if (name.equals("gems")){
            return gems;
        } else if(name.equals("mario")){
            return mario;
        } else {
            return gems2;
        }
    } 
}
