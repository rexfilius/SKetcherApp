
package sketcherapp;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExtensionFilter extends FileFilter {
    
    public ExtensionFilter(String ext, String descr) {
        extension = ext.toLowerCase();
        description = descr;
    }
    
    @Override
    public boolean accept(File file) {
        return (file.isDirectory() || file.getName().toLowerCase().endsWith(extension));
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    private String description;
    private String extension;
}
