
package sketcherapp;
import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.awt.font.TextLayout;

public class SketchCoverPage implements Printable {
    
    public SketchCoverPage(String sketchName) {
        this.sketchName = sketchName;
    }
    
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) 
                        throws PrinterException {
        if(pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2D = (Graphics2D) g;
        float x = (float)pageFormat.getImageableX();
        float y = (float)pageFormat.getImageableY();
        
        GeneralPath path = new GeneralPath();
        path.moveTo(x+1, y+1);
        path.lineTo(x+(float)pageFormat.getImageableWidth()-1, y+1);
        path.lineTo(x+(float)pageFormat.getImageableWidth()-1,
                    y+(float)pageFormat.getImageableHeight()-1);
        path.lineTo(x+1, y+(float)pageFormat.getImageableHeight()-1);
        path.closePath();
        
        g2D.setPaint(Color.red);
        g2D.draw(path);
        
        Font font = g2D.getFont().deriveFont(12.f).deriveFont(Font.BOLD);
        g2D.setFont(font);
        Rectangle2D textRect = 
                new TextLayout(sketchName, font, g2D.getFontRenderContext()).getBounds();
        double centerX = pageFormat.getWidth()/2;
        double centerY = pageFormat.getHeight()/2;
        Rectangle2D.Double surround = new Rectangle2D.Double(centerX-textRect.getWidth(),
                                                             centerY-textRect.getHeight(),
                                                             2*textRect.getWidth(),
                                                             2*textRect.getHeight());
        g2D.draw(surround);
        
        // draw text in the middle of the printable area
        g2D.setPaint(Color.blue);
        g2D.drawString(sketchName, (float)(centerX-textRect.getWidth()/2),
                                   (float)(centerY+textRect.getHeight()/2));
        return PAGE_EXISTS;
    }
    
    private String sketchName;
}
