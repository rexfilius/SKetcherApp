
package sketcherapp;
import java.io.Serializable;
import java.util.*;
import java.awt.Rectangle;

public class SketcherModel extends Observable implements Serializable, Iterable<Element> {
    
    // Remove an element from the sketch
    public boolean remove(Element element) {
        boolean removed = elements.remove(element);
        if(removed) {
            setChanged();
            notifyObservers(element.getBounds());
        }
        return removed;
    }
    
    // Add an element to the sketch
    public void add(Element element) {
        elements.add(element);
        setChanged();
        notifyObservers(element.getBounds());
    }
    
    // Get iterator for sketch elements
    @Override
    public Iterator<Element> iterator() {
        return elements.iterator();
    }
    
    // Get the rectangle enclosing an entire sketch
    Rectangle getModelExtent() {
        Rectangle rect = new Rectangle();
        for(Element element : elements) {
            rect.add(element.getBounds());
        }
        if(rect.width == 0) {
            rect.width = 2;
        }
        if(rect.height == 0) {
            rect.height = 2;
        }
        return rect;
    }
    
    protected LinkedList<Element> elements = new LinkedList<>();
    private final static long serialVersionUID = 1001L;
}