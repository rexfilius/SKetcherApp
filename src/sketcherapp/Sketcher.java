
package sketcherapp;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sketcher {
    
    public static void main(String[] args) {
        theApp = new Sketcher();
        SwingUtilities.invokeLater(() -> {
            theApp.createGUI();
        });
    }
    
    // Insert a new sketch model
    public void insertModel(SketcherModel newSketch) {
        sketch = newSketch;
        sketch.addObserver(view);
        sketch.addObserver(window);
        view.repaint();
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
        sketch.addObserver(window);
        
        window.getContentPane().add(view, BorderLayout.CENTER);
        window.setVisible(true);
    }
    
    // Handler class for window events
    class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            window.checkForSave();
        }
    }
    
    private SketcherModel sketch;
    private SketcherView view;
    private static SketcherFrame window;
    private static Sketcher theApp;    
}
