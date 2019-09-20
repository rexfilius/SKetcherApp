
package sketcherapp;
import javax.swing.JComponent;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import java.awt.geom.*;

public class SketcherView extends JComponent implements Observer {
    
    public SketcherView(Sketcher theApp) {
        this.theApp = theApp;
        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
    }
    
    // method called by observable object when it changes
    public void update(Observable o, Object rectangle) {
        // code to respond to changes in the model
        if(rectangle != null) {
            repaint((Rectangle)rectangle); // this rectangle is the area occupied by the new element
        } else {
            repaint();
        }
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;
        for(Element element : theApp.getModel()) {
            element.draw(g2D);
        }
    }
    
    class MouseHandler extends MouseInputAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            start = e.getPoint();
            buttonState = e.getButton();
            if(buttonState == MouseEvent.BUTTON1) {
                g2D = (Graphics2D)getGraphics();
                g2D.setXORMode(getBackground());
            }
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            last = e.getPoint();
            if(buttonState == MouseEvent.BUTTON1) {
                if(tempElement == null) {
                    tempElement = Element.createElement(theApp.getWindow().getElementType(),
                                                        theApp.getWindow().getElementColor(),
                                                        start, last);
                }else {
                    tempElement.draw(g2D);
                    tempElement.modify(start, last);
                }
                tempElement.draw(g2D);
            }
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                buttonState = MouseEvent.NOBUTTON;
                if(tempElement != null) {
                    theApp.getModel().add(tempElement);
                    tempElement = null;
                }
                if(g2D != null) {
                    g2D.dispose();
                    g2D = null;
                }
                start = last = null;
            }
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getDefaultCursor());
        }
        private Point start;
        private Point last;
        private Element tempElement;
        private int buttonState = MouseEvent.NOBUTTON;
        private Graphics2D g2D = null;
    }
    
    private Sketcher theApp;        // the application object
}
