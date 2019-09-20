
package sketcherapp;
import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import static sketcherapp.SketcherConstants.*;

public abstract class Element implements Serializable {
    
    protected Element(Color color) {
        this.color = color;
    }
    
    protected Element(Point position, Color color) {
        this.position = position;
        this.color = color;
    }
    
    public static Element createElement(int type, Color color, Point start, Point end) {
        switch(type) {
            case LINE:
                return new Line(start, end, color);
            case RECTANGLE:
                return new Rectangle(start, end, color);
            case CIRCLE:
                return new Circle(start, end, color);
            case CURVE:
                return new Curve(start, end, color);
            default:
                assert false;
        }
        return null;
    }
    public Color getColor() {
        return color;
    }
    public Point getPosition() {
        return position;
    }
    public java.awt.Rectangle getBounds() {
        return bounds;
    }
    
    // nested class defining a line
    public static class Line extends Element {
        public Line(Point start, Point end, Color color) {
            super(start, color);
            line = new Line2D.Double(origin.x, origin.y,
                                    end.x - position.x, end.y - position.y);
            bounds =  new java.awt.Rectangle(
                                    Math.min(start.x, end.x), Math.min(start.y, end.y),
                                    Math.abs(start.x - end.x)+1, Math.abs(start.y - end.y)+1);
        }
        // change the end point for the line
        @Override
        public void modify(Point start, Point last) {
            line.x2 = last.x - position.x;
            line.y2 = last.y - position.y;
            bounds = new java.awt.Rectangle(
                                    Math.min(start.x, last.x), Math.min(start.y, last.y),
                                    Math.abs(start.x - last.x)+1, Math.abs(start.y - last.y)+1);
        }
        // display the line
        @Override
        public void draw(Graphics2D g2D) {
            g2D.setPaint(color);
            g2D.translate(position.x, position.y);
            g2D.draw(line);
            g2D.translate(-position.x, -position.y);
        }
        private Line2D.Double line;
        private final static long serialVersionUID = 1001L;
    }
    
    // nested class defining a rectangle
    public static class Rectangle extends Element {
        public Rectangle(Point start, Point end, Color color) {
            super(new Point(Math.min(start.x, end.x), Math.min(start.y, end.y)), color);
            rectangle = new Rectangle2D.Double(origin.x, origin.y,
                                        Math.abs(start.x - end.x), Math.abs(start.y - end.y));
            bounds = new java.awt.Rectangle(
                                    Math.min(start.x, end.x), Math.min(start.y, end.y),
                                    Math.abs(start.x - end.x)+1, Math.abs(start.y - end.y)+1);
        }
        // display the rectangle
        @Override
        public void draw(Graphics2D g2D) {
            g2D.setPaint(color);
            g2D.translate(position.x, position.y);
            g2D.draw(rectangle);
            g2D.translate(-position.x, -position.y);
        }
        // method to redefine the rectangle
        @Override
        public void modify(Point start, Point last) {
            bounds.x = position.x = Math.min(start.x, last.x);
            bounds.y = position.y = Math.min(start.x, last.y);
            rectangle.width = Math.abs(start.x - last.x);
            rectangle.height = Math.abs(start.y - last.y);
            bounds.width = (int)rectangle.width + 1;
            bounds.height = (int)rectangle.height + 1;
        }
        private Rectangle2D.Double rectangle;
        private final static long serialVersionUID = 1001L;
    }
    
    // Nested class defining a circle
    public static class Circle extends Element {
        public Circle(Point center, Point circum, Color color) {
            super(color);
            double radius = center.distance(circum);
            position = new Point(center.x - (int)radius, center.y - (int)radius);
            circle = new Ellipse2D.Double(origin.x, origin.y, 2.*radius, 2.*radius);
            bounds = new java.awt.Rectangle(position.x, position.y, 1 + (int)circle.width,
                                                                    1 + (int)circle.height);
        }
        // Display the circle
        @Override
        public void draw(Graphics2D g2D) {
            g2D.setPaint(color);
            g2D.translate(position.x, position.y);
            g2D.draw(circle);
            g2D.translate(-position.x, -position.y);
        }
        // Recreate this circle
        @Override
        public void modify(Point center, Point circum) {
            double radius = center.distance(circum);
            circle.width = circle.height = 2*radius;
            position.x = center.x - (int)radius;
            position.y = center.y - (int)radius;
            bounds = new java.awt.Rectangle(position.x, position.y, 1 + (int)circle.width,
                                                                    1 + (int)circle.height);
        }
        private Ellipse2D.Double circle;
        private final static long serialVersionUID = 1001L;
    }
    
    // Nested Class defining a curve
    public static class Curve extends Element {
        public Curve(Point start, Point next, Color color) {
            super(start, color);
            curve = new GeneralPath();
            curve.moveTo(origin.x, origin.y);
            curve.lineTo(next.x - position.x, next.y - position.y);
            bounds = new java.awt.Rectangle(
                            Math.min(start.x, next.x), Math.min(start.y, next.y),
                            Math.abs(next.x - start.x)+1, Math.abs(next.y - start.y)+1);
        }
        // Add another segment
        @Override
        public void modify(Point start, Point next) {
            curve.lineTo(next.x - position.x, next.y - position.y);
            bounds.add(new java.awt.Rectangle(next.x, next.y, 1, 1));
        }
        // Display the curve
        @Override
        public void draw(Graphics2D g2D) {
            g2D.setPaint(color);
            g2D.translate(position.x, position.y);
            g2D.draw(curve);
            g2D.translate(-position.x, -position.y);
        }
        private GeneralPath curve;
        private final static long serialVersionUID = 1001L;
    }
    
    public abstract void draw(Graphics2D g2D);
    public abstract void modify(Point start, Point last);
    
    protected Point position;
    protected Color color;
    protected java.awt.Rectangle bounds;
    private final static long serialVersionUID = 1001L;
    static final Point origin = new Point();
}
