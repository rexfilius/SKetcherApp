
package sketcherapp;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import static sketcherapp.SketcherConstants.*;

public class SketcherView extends JComponent implements Observer, Printable, Pageable {
    
    public SketcherView(Sketcher theApp) {
        this.theApp = theApp;
        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
        // add the pop-up menu items
        JMenuItem moveItem = elementPopup.add(new JMenuItem("Move"));
        JMenuItem deleteItem = elementPopup.add(new JMenuItem("Delete"));
        JMenuItem rotateItem = elementPopup.add(new JMenuItem("Rotate"));
        JMenuItem sendToBackItem = elementPopup.add(new JMenuItem("Send-to-back"));
        // create the menu item listeners
        moveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mode = MOVE;
                selectedElement = highlightElement;
            }
        });
        deleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteElement();
            }
        });
        rotateItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mode = ROTATE;
                selectedElement = highlightElement;
            }
        });
        sendToBackItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendToBack();
            }
        });
    }
    
    // Method to return page count - always two pages
    public int getNumberOfPages() {
        return 2;
    }
    
    // Method to return the Printable object that will render the page
    public Printable getPrintable(int pageIndex) {
        if(pageIndex == 0) {
            return new SketchCoverPage(theApp.getWindow().getSketchName());
        } else {
            return this;
        }
    }
    
    public PageFormat getPageFormat(int pageIndex) {
        if(pageIndex == 0) {
            PageFormat pageFormat = 
                    (PageFormat)(theApp.getWindow().getPageFormat().clone());
            Paper paper = pageFormat.getPaper();
            
            double leftMargin = paper.getImageableX();
            double topMargin = paper.getImageableY();
            double rightMargin = paper.getWidth()-paper.getImageableWidth()-leftMargin;
            double bottomMargin = paper.getHeight()-paper.getImageableHeight()-topMargin;
            leftMargin *= 2.0;
            rightMargin *= 2.0;
            topMargin *= 2.0;
            bottomMargin *= 2.0;
            
            paper.setImageableArea(leftMargin, topMargin,
                                   paper.getWidth() - leftMargin - rightMargin, 
                                   paper.getHeight() - topMargin - bottomMargin);
            pageFormat.setPaper(paper);
            pageFormat.setOrientation(PageFormat.LANDSCAPE);
            return pageFormat;
        }
        return theApp.getWindow().getPageFormat();
    }
    
    // Print the sketch on a local printer
    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex)
                            throws PrinterException {
        //if(pageIndex > 0) {
         //   return NO_SUCH_PAGE;
        //}
        Graphics2D g2D = (Graphics2D) g;
        Rectangle rect = theApp.getModel().getModelExtent();
        double scaleX = pageFormat.getImageableWidth()/rect.width;
        double scaleY = pageFormat.getImageableHeight()/rect.height;
        double scale = Math.min(scaleX, scaleY);
        g2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2D.scale(scale, scale);
        g2D.translate(-rect.x, -rect.y);
        paint(g2D);
        return PAGE_EXISTS;
    }
    
    // Method called by observable object when it changes
    public void update(Observable o, Object rectangle) {
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
    
    private void sendToBack() {
        if(highlightElement != null) {
            SketcherModel sketch = theApp.getModel();
            if(sketch.remove(highlightElement)) {
                sketch.add(highlightElement);
            } else {
                JOptionPane.showMessageDialog(SketcherView.this, "Element not found to remove.",
                            "Remove Element from Sketch", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteElement() {
        if(highlightElement != null) {
            if(!theApp.getModel().remove(highlightElement)) {
                JOptionPane.showMessageDialog(SketcherView.this, "Element not found to remove.",
                            "Remove Element from Sketch", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    class MouseHandler extends MouseInputAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            start = e.getPoint();
            buttonState = e.getButton();
            if(showContextMenu(e)) {
                start = null;
                buttonState = MouseEvent.NOBUTTON;
                return;
            }
            if(theApp.getWindow().getElementType() == TEXT && mode == NORMAL)
                return;
            if(mode == ROTATE && selectedElement != null) {
                oldAngle = selectedElement.getRotation();
                angle = 0.0;
            }
            if(buttonState == MouseEvent.BUTTON1 && mode == NORMAL) {
                g2D = (Graphics2D)getGraphics();
                g2D.setXORMode(getBackground());
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            last = e.getPoint();
            if(theApp.getWindow().getElementType() == TEXT && mode == NORMAL) 
                return;
            switch(mode) {
                case NORMAL:
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
                break;
                case MOVE:
                    if(buttonState == MouseEvent.BUTTON1 && selectedElement != null) {
                        selectedElement.move(last.x-start.x, last.y-start.y);
                        repaint();
                        start = last;
                    }
                break;
                case ROTATE:
                    if(buttonState == MouseEvent.BUTTON1 && selectedElement != null) {
                        angle += getAngle(selectedElement.getPosition(), start, last);
                        if(angle != 0.0) {
                            selectedElement.setRotation(oldAngle + angle);
                            repaint();
                            start = last;
                        }
                    }
                break;
            }   
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if(mode == MOVE || mode == ROTATE) {
                selectedElement = null;
                start = last = null;
                mode = NORMAL;
                return;
            }
            if(showContextMenu(e)) {
                start = last = null;
                return;
            }
            if(theApp.getWindow().getElementType() == TEXT) {
                if(last != null) {
                    start = last = null;
                }
                return;
            }
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
        public void mouseClicked(MouseEvent e) {
            // only if it's TEXT and button1 was clicked
            if(theApp.getWindow().getElementType() == TEXT && buttonState == MouseEvent.BUTTON1) {
                String text = JOptionPane.showInputDialog(theApp.getWindow(), "Enter Input:", 
                         "Create Text Element", JOptionPane.PLAIN_MESSAGE);
                if(text != null && !text.isEmpty()) {
                    g2D = (Graphics2D)getGraphics();
                    tempElement = new Element.Text(text, start, 
                            theApp.getWindow().getElementColor(),
                            g2D.getFontMetrics(theApp.getWindow().getFont()));
                    g2D.dispose();
                    g2D = null;
                    if(tempElement != null) {
                        theApp.getModel().add(tempElement);
                    }                            
                }
                tempElement = null;
                start = null;
            }   
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            Point cursor = e.getPoint();
            for(Element element : theApp.getModel()) {
                if(element.getBounds().contains(cursor)) {
                    if(element == highlightElement) {
                        return;
                    }
                    if(highlightElement != null) {
                        highlightElement.setHighlighted(false);
                        repaint(highlightElement.getBounds());
                    }
                    element.setHighlighted(true);
                    highlightElement = element;
                    repaint(highlightElement.getBounds());
                    return;
                }
            }
            if(highlightElement != null) {
                highlightElement.setHighlighted(false);
                repaint(highlightElement.getBounds());
                highlightElement = null;
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
        
        private boolean showContextMenu(MouseEvent e) {
            if(e.isPopupTrigger()) {
                if(last != null) {
                    start = last;
                }
                if(highlightElement == null) {
                    theApp.getWindow().getPopup().show(SketcherView.this, start.x, start.y);
                } else {
                    elementPopup.show(SketcherView.this, start.x, start.y);
                }
                return true;
            }
            return false;
        }
        
        double getAngle(Point position, Point start, Point last) {
            double perp = Line2D.ptLineDist(position.x, position.y, last.x, last.y, start.x, start.y);
            double hypotenuse = position.distance(start);
            if(perp < 1.0 || hypotenuse < 1.0) return 0.0;
            return -Line2D.relativeCCW(position.x, position.y, start.x, start.y,
                    last.x, last.y)*Math.asin(perp/hypotenuse);
        }
        
        private Point start;
        private Point last;
        private Element tempElement;
        private int buttonState = MouseEvent.NOBUTTON;
        private Graphics2D g2D = null;
    }
    
    private double oldAngle = 0.0;
    private double angle = 0.0;
    private String mode = NORMAL;
    private Sketcher theApp;        // the application object
    private Element highlightElement;
    private Element selectedElement;    // Element for move or rotate
    private JPopupMenu elementPopup = new JPopupMenu("Element Operations");
}
