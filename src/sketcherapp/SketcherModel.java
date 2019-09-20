
package sketcherapp;
import java.io.Serializable;
import java.util.*;

public class SketcherModel extends Observable implements Serializable, Iterable<Element> {
    
    // remove an element from the sketch
    public boolean remove(Element element) {
        boolean removed = elements.remove(element);
        if(removed) {
            setChanged();
            notifyObservers(element.getBounds());
        }
        return removed;
    }
    
    // add an element to the sketch
    public void add(Element element) {
        elements.add(element);
        setChanged();
        notifyObservers(element.getBounds());
    }
    
    // get iterator for sketch elements
    @Override
    public Iterator<Element> iterator() {
        return elements.iterator();
    }
    
    protected LinkedList<Element> elements = new LinkedList<>();
    private final static long serialVersionUID = 1001L;
}
