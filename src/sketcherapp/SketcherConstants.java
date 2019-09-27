
package sketcherapp;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import javax.swing.*;

public class SketcherConstants {
    
    // Path for images
    public static final String IMAGE_PATH = "C:\\Users\\HP\\Documents\\NetBeansProjects\\SketcherApp\\images\\";
    
    // ToolBar icons for File Menu
    public static final Icon NEW24 = new ImageIcon(IMAGE_PATH + "New24.gif");
    public static final Icon OPEN24 = new ImageIcon(IMAGE_PATH + "Open24.gif");
    public static final Icon SAVE24 = new ImageIcon(IMAGE_PATH + "Save24.gif");
    public static final Icon SAVEAS24 = new ImageIcon(IMAGE_PATH + "SaveAs24.gif");
    public static final Icon PRINT24 = new ImageIcon(IMAGE_PATH + "Print24.gif");
    
    // ToolBar icons for Element Menu
    public static final Icon LINE24 =  new ImageIcon(IMAGE_PATH + "Line24.gif");
    public static final Icon RECTANGLE24 =  new ImageIcon(IMAGE_PATH + "Rectangle24.gif");
    public static final Icon CIRCLE24 =  new ImageIcon(IMAGE_PATH + "Circle24.gif");
    public static final Icon CURVE24 =  new ImageIcon(IMAGE_PATH + "Curve24.gif");
    public static final Icon TEXT24 =  new ImageIcon(IMAGE_PATH + "Text24.gif");

    // ToolBar icons for Color Menu
    public static final Icon RED24 =  new ImageIcon(IMAGE_PATH + "Red24.gif");
    public static final Icon GREEN24 =  new ImageIcon(IMAGE_PATH + "Green24.gif");
    public static final Icon BLUE24 =  new ImageIcon(IMAGE_PATH + "Blue24.gif");
    public static final Icon YELLOW24 =  new ImageIcon(IMAGE_PATH + "Yellow24.gif");
    public static final ImageIcon CUSTOM24 = new ImageIcon(
                                                new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB));
    
    // MenuBar icons for File Menu
    public static final Icon NEW16 = new ImageIcon(IMAGE_PATH + "New16.gif");
    public static final Icon OPEN16 = new ImageIcon(IMAGE_PATH + "Open16.gif");
    public static final Icon SAVE16 = new ImageIcon(IMAGE_PATH + "Save16.gif");
    public static final Icon SAVEAS16 = new ImageIcon(IMAGE_PATH + "SaveAs16.gif");
    public static final Icon PRINT16 = new ImageIcon(IMAGE_PATH + "Print16.gif");
    
    // MenuBar icons for Element Menu
    public static final Icon LINE16 = new ImageIcon(IMAGE_PATH + "Line16.gif");
    public static final Icon RECTANGLE16 = new ImageIcon(IMAGE_PATH + "Rectangle16.gif");
    public static final Icon CIRCLE16 = new ImageIcon(IMAGE_PATH + "Circle16.gif");
    public static final Icon CURVE16 = new ImageIcon(IMAGE_PATH + "Curve16.gif");
    public static final Icon TEXT16 = new ImageIcon(IMAGE_PATH + "Text16.gif");

    // MenuBar icons for Color Menu
    public static final Icon RED16 = new ImageIcon(IMAGE_PATH + "Red16.gif");
    public static final Icon GREEN16 = new ImageIcon(IMAGE_PATH + "Green16.gif");
    public static final Icon BLUE16 = new ImageIcon(IMAGE_PATH + "Blue16.gif");
    public static final Icon YELLOW16 = new ImageIcon(IMAGE_PATH + "Yellow16.gif");
    public static final ImageIcon CUSTOM16 = new ImageIcon(
                                                new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
    
    // Element type definitions
    public static final int LINE = 101;
    public static final int RECTANGLE = 102;
    public static final int CIRCLE = 103;
    public static final int CURVE = 104;
    public static final int TEXT = 105;
    
    // Initial conditions
    public static final int DEFAULT_ELEMENT_TYPE = LINE;
    public static final Color DEFAULT_ELEMENT_COLOR =  Color.BLUE;
    public static final Color HIGHLIGHT_COLOR = Color.MAGENTA;
    public static final Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 12);
    
    // Font sizes
    public static final int POINT_SIZE_MIN = 8;
    public static final int POINT_SIZE_MAX = 24;
    public static final int POINT_SIZE_STEP = 2;
    
    // Operating modes
    public static final String NORMAL = "Normal";
    public static final String MOVE = "Move";
    public static final String ROTATE = "Rotate";
    
    // Default directory and file name for a sketch
    public static final Path DEFAULT_DIRECTORY = 
            Paths.get(System.getProperty("user.home")).resolve("Sketches");
    public static final String DEFAULT_FILENAME = "Sketch.ske";
}
