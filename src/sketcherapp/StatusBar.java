
package sketcherapp;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import static sketcherapp.SketcherConstants.*;

public class StatusBar extends JPanel {
    
    public StatusBar() {
       setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
       setBackground(Color.LIGHT_GRAY);
       setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
       colorPane = new StatusPane("BLUE", BLUE16);
       setColorPane(DEFAULT_ELEMENT_COLOR);
       setTypePane(DEFAULT_ELEMENT_TYPE);
       add(colorPane);
       add(typePane);
    }
    
    public void setColorPane(Color color) {
        // code to set the color pane contents...
        String text = null;
        Icon icon = null;
        if(color.equals(Color.RED)) {
            text = "RED";
            icon = RED16;
        } else if(color.equals(Color.YELLOW)) {
            text = "YELLOW";
            icon = YELLOW16;
        } else if(color.equals(Color.GREEN)) {
            text = "GREEN";
            icon = GREEN16;
        } else if(color.equals(Color.BLUE)) {
            text = "BLUE";
            icon = BLUE16;
        } else {
            text = "CUSTOM COLOR";
        }
        colorPane.setIcon(icon);
        colorPane.setText(text);
    }
    
    public void setTypePane(int elementType) {
        // code to set the type pane contents...
        String text = null;
        switch(elementType) {
            case LINE:
                text = "LINE";
                break;
            case RECTANGLE:
                text = "RECTANGLE";
                break;
            case CIRCLE:
                text = "CIRCLE";
                break;
            case CURVE:
                text = "CURVE";
                break;
            case TEXT:
                text = "TEXT";
            default:
                assert false;
        }
        typePane.setText(text);
    }
    
    // Panes in the status bar
    private StatusPane colorPane = new StatusPane("BLUE", BLUE16);
    private StatusPane typePane = new StatusPane("LINE");
    
    class StatusPane extends JLabel {
        public StatusPane(String text) {
            super(text, LEFT);
            setupPane();
        }
        public StatusPane(String text, Icon icon) {
            super(text, icon, LEFT);
            setupPane();
        }
        private void setupPane() {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.BLACK);
            setFont(paneFont);
            setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                        BorderFactory.createEmptyBorder(0, 5, 0, 3)));
            setPreferredSize(new Dimension(80,20));
        }
        private Font paneFont = new Font("Serif", Font.PLAIN, 10);
    }
}
