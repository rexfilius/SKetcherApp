
package sketcherapp;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
public class SketcherConstants {
    
    // path for images
    public final static String IMAGE_PATH = "C:\\Users\\HP\\Documents\\NetBeansProjects\\SketcherApp\\images\\";
    
    // ToolBar icons for File Menu
    public final static Icon NEW24 = new ImageIcon(IMAGE_PATH + "New24.gif");
    public final static Icon OPEN24 = new ImageIcon(IMAGE_PATH + "Open24.gif");
    public final static Icon SAVE24 = new ImageIcon(IMAGE_PATH + "Save24.gif");
    public final static Icon SAVEAS24 = new ImageIcon(IMAGE_PATH + "SaveAs24.gif");
    public final static Icon PRINT24 = new ImageIcon(IMAGE_PATH + "Print24.gif");
    
    // ToolBar icons for Element Menu
    public final static Icon LINE24 =  new ImageIcon(IMAGE_PATH + "Line24.gif");
    public final static Icon RECTANGLE24 =  new ImageIcon(IMAGE_PATH + "Rectangle24.gif");
    public final static Icon CIRCLE24 =  new ImageIcon(IMAGE_PATH + "Circle24.gif");
    public final static Icon CURVE24 =  new ImageIcon(IMAGE_PATH + "Curve24.gif");
    public final static Icon TEXT24 =  new ImageIcon(IMAGE_PATH + "Text24.gif");

    // ToolBar icons for Color Menu
    public final static Icon RED24 =  new ImageIcon(IMAGE_PATH + "Red24.gif");
    public final static Icon GREEN24 =  new ImageIcon(IMAGE_PATH + "Green24.gif");
    public final static Icon BLUE24 =  new ImageIcon(IMAGE_PATH + "Blue24.gif");
    public final static Icon YELLOW24 =  new ImageIcon(IMAGE_PATH + "Yellow24.gif");
    
    // MenuBar icons for File Menu
    public final static Icon NEW16 = new ImageIcon(IMAGE_PATH + "New16.gif");
    public final static Icon OPEN16 = new ImageIcon(IMAGE_PATH + "Open16.gif");
    public final static Icon SAVE16 = new ImageIcon(IMAGE_PATH + "Save16.gif");
    public final static Icon SAVEAS16 = new ImageIcon(IMAGE_PATH + "SaveAs16.gif");
    public final static Icon PRINT16 = new ImageIcon(IMAGE_PATH + "Print16.gif");
    
    // MenuBar icons for Element Menu
    public final static Icon LINE16 = new ImageIcon(IMAGE_PATH + "Line16.gif");
    public final static Icon RECTANGLE16 = new ImageIcon(IMAGE_PATH + "Rectangle16.gif");
    public final static Icon CIRCLE16 = new ImageIcon(IMAGE_PATH + "Circle16.gif");
    public final static Icon CURVE16 = new ImageIcon(IMAGE_PATH + "Curve16.gif");
    public final static Icon TEXT16 = new ImageIcon(IMAGE_PATH + "Text16.gif");

    // MenuBar icons for Color Menu
    public final static Icon RED16 = new ImageIcon(IMAGE_PATH + "Red16.gif");
    public final static Icon GREEN16 = new ImageIcon(IMAGE_PATH + "Green16.gif");
    public final static Icon BLUE16 = new ImageIcon(IMAGE_PATH + "Blue16.gif");
    public final static Icon YELLOW16 = new ImageIcon(IMAGE_PATH + "Yellow16.gif");
    
    public static final ImageIcon CUSTOM24 = new ImageIcon(
                                                new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB));
    public static final ImageIcon CUSTOM16 = new ImageIcon(
                                                new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
    
    // Element type definitions
    public static final int LINE = 101;
    public static final int RECTANGLE = 102;
    public static final int CIRCLE = 103;
    public static final int CURVE = 104;
    public static final int TEXT = 105;
    
    // Font sizes
    public final static int POINT_SIZE_MIN = 8;
    public final static int POINT_SIZE_MAX = 24;
    public final static int POINT_SIZE_STEP = 2;
    
    public static final Color HIGHLIGHT_COLOR = Color.MAGENTA;
    
    // Operating modes
    public final static String NORMAL = "Normal";
    public final static String MOVE = "Move";
    public final static String ROTATE = "Rotate";
    
    // initial conditions when application starts
    public final static int DEFAULT_ELEMENT_TYPE = LINE;
    public final static Color DEFAULT_ELEMENT_COLOR =  Color.BLUE;
    public final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 12);
}
