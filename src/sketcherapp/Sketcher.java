
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
    
    // method to create the application GUI
    private void createGUI() {
        window = new SketcherFrame("Sketcher", this);
        Toolkit theKit = window.getToolkit();
        Dimension wndSize = theKit.getScreenSize();
        
        window.setSize(wndSize.width/2, wndSize.height/2);
        window.setLocationRelativeTo(null);
        window.addWindowListener(new WindowHandler());
        
        sketch = new SketcherModel();
        view = new SketcherView(this);
        sketch.addObserver(view);
        
        window.getContentPane().add(view, BorderLayout.CENTER);
        window.setVisible(true);
    }
    
    // return a reference to the application window
    public SketcherFrame getWindow() {
        return window;
    }
    
    // return a reference to the model
    public SketcherModel getModel() {
        return sketch;
    }
    
    // return a reference to the view
    public SketcherView getView() {
        return view;
    }
    
    // Handler class for window events
    class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            window.dispose();
            System.exit(0);
        }
    }
    
    private SketcherModel sketch;
    private SketcherView view;
    private static SketcherFrame window;
    private static Sketcher theApp;
    
}
