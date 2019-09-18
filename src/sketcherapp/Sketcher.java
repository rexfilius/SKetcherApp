
package sketcherapp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sketcher {
    
    public static void main(String[] args) {
        theApp = new Sketcher();
        SwingUtilities.invokeLater(() -> {
            theApp.createGUI();
        });
    }
    
    private void createGUI() {
        window = new SketcherFrame("Sketcher");
        Toolkit theKit = window.getToolkit();
        Dimension wndSize = theKit.getScreenSize();
        
        window.setSize(wndSize.width/2, wndSize.height/2);
        window.setLocationRelativeTo(null);
        window.addWindowListener(new WindowHandler());
        window.setVisible(true);
        
    }
    
    class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            window.dispose();
            System.exit(0);
        }
    }
    
    private static SketcherFrame window;
    private static Sketcher theApp;
    
}
