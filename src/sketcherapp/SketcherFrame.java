
package sketcherapp;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.border.*;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import static java.awt.Color.*;
import static java.awt.event.InputEvent.*;
import static javax.swing.Action.*;
import static sketcherapp.SketcherConstants.*;

public class SketcherFrame extends JFrame implements ActionListener, Observer, Printable {
    
    @SuppressWarnings("serial")
    public SketcherFrame(String title, Sketcher theApp) {
        printJob = PrinterJob.getPrinterJob();
        pageFormat = printJob.defaultPage();
        printer = printJob.getPrintService();
        
        checkDirectory(DEFAULT_DIRECTORY);
        fileChooser = new JFileChooser(DEFAULT_DIRECTORY.toString());
        setTitle(title);
        frameTitle = title;
        this.theApp = theApp;
        setJMenuBar(menuBar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setCustomIconColor(CUSTOM16, customColor);
        setCustomIconColor(CUSTOM24, customColor);
        
        createFileMenu();
        createElementMenu();
        createColorMenu();
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic('O');
        menuBar.add(optionsMenu);
        createPopupMenu();
        // add the font choice item to the options menu
        fontItem = new JMenuItem("Choose font...");
        fontItem.addActionListener(this);
        optionsMenu.add(fontItem);
        fontDialog = new FontDialog(this);
        
        createToolBar();
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        // Add the About item to the Help Menu
        aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }
    
    public Color getElementColor() {
        return elementColor;
    }
    
    public int getElementType() {
        return elementType;
    }
    
    public Font getFont() {
        return textFont;
    }
    
    public void setFont(Font font) {
        textFont = font;
    }
    
    public JPopupMenu getPopup() {
        return popup;
    }
    
    public String getSketchName() {
       return currentSketchFile == null ? DEFAULT_FILENAME.toString() : 
                                          currentSketchFile.getFileName().toString();
    }
    
    public PageFormat getPageFormat() {
        return pageFormat;
    }
    
    // Method called by SketcherModel object when it changes
    @Override
    public void update(Observable o, Object obj) {
        sketchChanged = true;
    }
    
    // Method for opening a sketch file and creating the model
    public boolean openSketch(Path file) {
        try(ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(file)))) {
            theApp.insertModel((SketcherModel)in.readObject());
            currentSketchFile = file;
            setTitle(frameTitle + " - " + currentSketchFile);
            sketchChanged = false;
        } catch(Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(this, "Error reading a sketch file.",
                                          "File Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    // Prompt for save operation when necessary for File Open
    public void checkForSave() {
        if(sketchChanged && JOptionPane.YES_OPTION == 
                            JOptionPane.showConfirmDialog(this,
                                                          "Current file has changed. Save current file?",
                                                          "Confirm Save Current File",
                                                          JOptionPane.YES_NO_OPTION,
                                                          JOptionPane.WARNING_MESSAGE)) {
            saveOperation();
        }
    }
    
    // Print the window
    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex)
                            throws PrinterException {
        if(pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2D = (Graphics2D) g;
        double scaleX = pageFormat.getImageableWidth()/getWidth();
        double scaleY = pageFormat.getImageableHeight()/getHeight();
        double scale = Math.min(scaleX, scaleY);
        g2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2D.scale(scale, scale);
        printAll(g2D);
        return PAGE_EXISTS;
    }
    
    // Handle About menu events
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == aboutItem) {
            JOptionPane.showMessageDialog(this, 
                    "Sketcher Application \nDeveloper: Ifeakachukwu Osakwe \nVersion 1.0", 
                    "About Sketcher", JOptionPane.INFORMATION_MESSAGE);
        } else if(e.getSource() == fontItem) {
            Rectangle bounds = getBounds();
            fontDialog.setLocationRelativeTo(null);
            fontDialog.setVisible(true);
        }
    }
    
    private void createFileMenuActions() {
        newAction = new FileAction("New", 'N', CTRL_DOWN_MASK);
        openAction = new FileAction("Open", 'O', CTRL_DOWN_MASK);
        closeAction = new FileAction("Close");
        saveAction = new FileAction("Save", 'S', CTRL_DOWN_MASK);
        saveAsAction = new FileAction("Save As..");
        printAction = new FileAction("Print", 'P', CTRL_DOWN_MASK);
        exitAction = new FileAction("Exit", 'X', CTRL_DOWN_MASK);
        FileAction[] actions = {openAction, closeAction, saveAction, 
                                saveAsAction, printAction, exitAction};
        fileActions = actions;
        // add toolbar icons
        newAction.putValue(LARGE_ICON_KEY, NEW24);
        openAction.putValue(LARGE_ICON_KEY, OPEN24);
        saveAction.putValue(LARGE_ICON_KEY, SAVE24);
        saveAsAction.putValue(LARGE_ICON_KEY, SAVEAS24);
        printAction.putValue(LARGE_ICON_KEY, PRINT24);
        // add menu icons
        newAction.putValue(SMALL_ICON, NEW16);
        openAction.putValue(SMALL_ICON, OPEN16);
        saveAction.putValue(SMALL_ICON, SAVE16);
        saveAsAction.putValue(SMALL_ICON, SAVEAS16);
        printAction.putValue(SMALL_ICON, PRINT16);
        // add tooltip text
        newAction.putValue(SHORT_DESCRIPTION, "Create a New Sketch");
        openAction.putValue(SHORT_DESCRIPTION, "Read a sketch from a file");
        saveAction.putValue(SHORT_DESCRIPTION, "Save the current sketch to file");
        saveAsAction.putValue(SHORT_DESCRIPTION, "Save the current sketch to a new file");
        printAction.putValue(SHORT_DESCRIPTION, "Print the current sketch"); 
    }
    
    private void createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        createFileMenuActions();
        // create print setup menu item
        JMenuItem printSetupItem = new JMenuItem("Print setup...");
        printSetupItem.setToolTipText("Setup the page for the printer");
        printSetupItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PageFormat pf = printJob.pageDialog(printAttr);
                if(pf != null) {
                    pageFormat = pf;
                }
            }
        });
        // Menu item to print the application window
        JMenuItem printWindowItem = new JMenuItem("Print Window");
        printWindowItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(printer == null) {
                    JOptionPane.showMessageDialog(SketcherFrame.this,
                                                  "No default printer available.",
                                                  "Printer Error",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
                printJob.setPrintable(SketcherFrame.this, pageFormat);
                try {
                    printJob.print();
                } catch(PrinterException pe) {
                    System.out.println(pe);
                    JOptionPane.showMessageDialog(SketcherFrame.this,
                                                  "Error printing the application window.",
                                                  "Printer Error",
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // file drop-down menu
        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.add(closeAction);
        fileMenu.addSeparator();
        fileMenu.add(saveAction);
        fileMenu.add(saveAsAction);
        fileMenu.addSeparator();
        fileMenu.add(printAction);
        fileMenu.add(printSetupItem);
        fileMenu.add(printWindowItem);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        menuBar.add(fileMenu);
    }
    
    private void createElementTypeActions() {
        lineAction = new TypeAction("Line", LINE, 'L', CTRL_DOWN_MASK);
        rectangleAction = new TypeAction("Rectangle", RECTANGLE, 'R', CTRL_DOWN_MASK);
        circleAction = new TypeAction("Circle", CIRCLE, 'C', CTRL_DOWN_MASK);
        curveAction = new TypeAction("Curve", CURVE, 'U', CTRL_DOWN_MASK);
        textAction = new TypeAction("Text", TEXT, 'T', CTRL_DOWN_MASK);
        TypeAction[] actions = {lineAction, rectangleAction, circleAction, curveAction, textAction};
        typeActions = actions;
        // add toolbar icons
        lineAction.putValue(LARGE_ICON_KEY, LINE24);
        rectangleAction.putValue(LARGE_ICON_KEY, RECTANGLE24);
        circleAction.putValue(LARGE_ICON_KEY, CIRCLE24);
        curveAction.putValue(LARGE_ICON_KEY, CURVE24);
        textAction.putValue(LARGE_ICON_KEY, TEXT24);
        // add menu bar icons
        lineAction.putValue(SMALL_ICON, LINE16);
        rectangleAction.putValue(SMALL_ICON, RECTANGLE16);
        circleAction.putValue(SMALL_ICON, CIRCLE16);
        curveAction.putValue(SMALL_ICON, CURVE16);
        textAction.putValue(SMALL_ICON, TEXT16);
        // add tooltip text
        lineAction.putValue(SHORT_DESCRIPTION, "Draw lines");
        rectangleAction.putValue(SHORT_DESCRIPTION, "Draw rectangles");
        circleAction.putValue(SHORT_DESCRIPTION, "Draw circles");
        curveAction.putValue(SHORT_DESCRIPTION, "Draw curves");
        textAction.putValue(SHORT_DESCRIPTION, "Draw Text");
    }
    
    private void createElementMenu() {
        createElementTypeActions();
        elementMenu = new JMenu("Elements");
        elementMenu.setMnemonic('E');
        createRadioButtonDropDown(elementMenu, typeActions, lineAction);
        menuBar.add(elementMenu);
    }
    
    private void createElementColorActions() {
        redAction = new ColorAction("Red", RED, 'R', CTRL_DOWN_MASK|ALT_DOWN_MASK);
        yellowAction = new ColorAction("Yellow", YELLOW, 'Y', CTRL_DOWN_MASK|ALT_DOWN_MASK);
        greenAction = new ColorAction("Green", GREEN, 'G', CTRL_DOWN_MASK|ALT_DOWN_MASK);
        blueAction = new ColorAction("Blue", BLUE, 'B', CTRL_DOWN_MASK|ALT_DOWN_MASK);
        customAction = new ColorAction("Custom...", BLUE, 'C', CTRL_DOWN_MASK|ALT_DOWN_MASK);
        ColorAction[] actions = {redAction, yellowAction, greenAction, blueAction, customAction};
        colorActions = actions;
        // add toolbar icons
        redAction.putValue(LARGE_ICON_KEY, RED24);
        greenAction.putValue(LARGE_ICON_KEY, GREEN24);
        blueAction.putValue(LARGE_ICON_KEY, BLUE24);
        yellowAction.putValue(LARGE_ICON_KEY, YELLOW24);
        customAction.putValue(LARGE_ICON_KEY, CUSTOM24);
        /// add menu bar icons
        redAction.putValue(SMALL_ICON, RED16);
        greenAction.putValue(SMALL_ICON, GREEN16);
        blueAction.putValue(SMALL_ICON, BLUE16);
        yellowAction.putValue(SMALL_ICON, YELLOW16);
        customAction.putValue(SMALL_ICON, CUSTOM16);
        // add tooltip text
        redAction.putValue(SHORT_DESCRIPTION, "Draw in red");
        greenAction.putValue(SHORT_DESCRIPTION, "Draw in green");
        blueAction.putValue(SHORT_DESCRIPTION, "Draw in blue");
        yellowAction.putValue(SHORT_DESCRIPTION, "Draw in yellow");
        customAction.putValue(SHORT_DESCRIPTION, "Draw in custom color");
    }
    
    private void createColorMenu() {
        createElementColorActions();
        colorMenu = new JMenu("Color");
        colorMenu.setMnemonic('C');
        createRadioButtonDropDown(colorMenu, colorActions, blueAction);
        menuBar.add(colorMenu);
    }
    
    // Radio button menu items for Element Menu and Color Menu
    private void createRadioButtonDropDown(JMenu menu, Action[] actions, Action selected) {
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem item = null;
        for(Action action : actions) {
            group.add(menu.add(item = new JRadioButtonMenuItem(action)));
            if(action == selected) {
                item.setSelected(true);
            }
        }
    }
    
    private void createToolBar() {
        for(FileAction action : fileActions) {
            if(action != exitAction && action != closeAction)
                addToolbarButton(action);
        }
        toolBar.addSeparator();
        for(ColorAction action : colorActions) {
            addToolbarButton(action);
        }
        toolBar.addSeparator();
        for(TypeAction action : typeActions) {
            addToolbarButton(action);
        }
    }
    
    private void addToolbarButton(Action action) {
        JButton button = new JButton(action);
        button.setBorder(BorderFactory.createCompoundBorder( 
                           new EmptyBorder(2,5,5,2), BorderFactory.createRaisedBevelBorder()));
        button.setHideActionText(true);
        toolBar.add(button);
    }
    
    // Setting menu item checks when a toolbar button is clicked for Elements and Color menu
    private void setChecks(JMenu menu, Object eventSource) {
        if(eventSource instanceof JButton) {
            JButton button = (JButton)eventSource;
            Action action = button.getAction();
            for(int i = 0; i < menu.getItemCount(); ++i) {
                JMenuItem item = menu.getItem(i);
                item.setSelected(item.getAction() == action);
            }
        }
    }
    
    // Create pop-up menu
    private void createPopupMenu() {
        popup.add(new JMenuItem(lineAction));
        popup.add(new JMenuItem(rectangleAction));
        popup.add(new JMenuItem(circleAction));
        popup.add(new JMenuItem(curveAction));
        popup.add(new JMenuItem(textAction));
        popup.addSeparator();
        popup.add(new JMenuItem(redAction));
        popup.add(new JMenuItem(yellowAction));
        popup.add(new JMenuItem(greenAction));
        popup.add(new JMenuItem(blueAction));
    }
    
    private void setCustomIconColor(ImageIcon icon, Color color) {
        BufferedImage image = (BufferedImage)(icon.getImage());
        int width = image.getWidth();
        int indent = width == 16 ? 3 : 2;
        int rectSize = width - 2*indent;
        Graphics2D g2D = image.createGraphics();
        g2D.setPaint(color);
        g2D.fillRect(indent, indent, rectSize, rectSize);
        if(width == 24) {
            TextLayout textLayout = new TextLayout("C", g2D.getFont(), g2D.getFontRenderContext());
            Rectangle2D.Float rect = (Rectangle2D.Float)textLayout.getBounds();
            g2D.setPaint(new Color(255-color.getRed(), 255-color.getGreen(), 255-color.getBlue()));
            g2D.drawString("C", (width-rect.width)/2, (width+rect.height)/2);
        }
        g2D.dispose();
    }
    
    private void checkDirectory(Path directory) {
        if(Files.notExists(directory)) {
            JOptionPane.showMessageDialog(null, "Creating directory: " + directory,
                                          "Directory Not Found", JOptionPane.INFORMATION_MESSAGE);
            try {
                Files.createDirectories(directory);
            } catch(IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Cannot create: " + directory +
                ". Terminating Sketcher.", "Directory Creation Failed",JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
    
    private Path showDialog(String dialogTitle, String approveButtonText,
                            String approveButtonTooltip, ExtensionFilter filter, Path file) {
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setApproveButtonText(approveButtonText);
        fileChooser.setApproveButtonToolTipText(approveButtonTooltip);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
        
        fileChooser.rescanCurrentDirectory();
        Path selectedFile = null;
        if(file == null) {
            selectedFile = Paths.get(
                        fileChooser.getCurrentDirectory().toString(), DEFAULT_FILENAME);
        } else {
            selectedFile = file;
        }
        fileChooser.setSelectedFile(selectedFile.toFile());
        // show the file save dialog
        int result = fileChooser.showDialog(this, null);
        return (result == JFileChooser.APPROVE_OPTION) ? 
                Paths.get(fileChooser.getSelectedFile().getPath()) : null;
    }
    
    private void saveOperation() {
        if(!sketchChanged) {
            return;
        }
        if(currentSketchFile != null) {
            if(saveSketch(currentSketchFile)) {
                sketchChanged = false;
            }
            return;
        }
        // here the sketch was never saved i.e currentSketchFile = null
        Path file = showDialog("Save Sketch", "Save", "Save the sketch", sketchFilter,
                                    Paths.get(DEFAULT_FILENAME));
        if(file == null) {
            return;
        }
        file = setFileExtension(file, "ske");
        if(Files.exists(file) && 
                JOptionPane.NO_OPTION == 
                    JOptionPane.showConfirmDialog(this, file.getFileName() + " exists. Overwrite?",
                                                  "Confirm Save As", JOptionPane.YES_NO_OPTION,
                                                  JOptionPane.WARNING_MESSAGE)) {
            return;
        }
        if(saveSketch(file)) {
            currentSketchFile = file;
            setTitle(frameTitle + " - " + currentSketchFile);
            sketchChanged = false;
        }
    }
    
    private void saveAsOperation() {
        Path file = showDialog("Save Sketch As", "Save", "Save the sketch", sketchFilter,
                    currentSketchFile == null ? Paths.get(DEFAULT_FILENAME) : currentSketchFile);
        if(file == null) {
            return;
        }
        file = setFileExtension(file, "ske");
        if(Files.exists(file) && !file.equals(currentSketchFile) && 
                JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this,
                                                      file.getFileName() + " exists. Overwrite?",
                                                      "Confirm Save As",
                                                      JOptionPane.NO_OPTION,
                                                      JOptionPane.WARNING_MESSAGE)) {
            return;
        }
        if(saveSketch(file)) {
            currentSketchFile = file;
            setTitle(frameTitle + " - " + currentSketchFile);
            sketchChanged = false;
        }
    }
    
    // set the extension for a file path
    private Path setFileExtension(Path file, String extension) {
        StringBuffer fileName = new StringBuffer(file.getFileName().toString());
        if(fileName.indexOf(extension) >= 0) {
            return file;
        }
        int index = fileName.lastIndexOf(".");
        if(index < 0) {
            fileName.append(".").append(extension);
        }
        return file.getParent().resolve(fileName.toString());
    }
    
    // write a sketch
    private boolean saveSketch(Path file) {
        try(ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(file)))) {
            out.writeObject(theApp.getModel());
        } catch(IOException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(this, "Error writing a sketch to " + file,
                                    "File Output Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    // Inner class defining Action objects for File Menu items
    class FileAction extends AbstractAction {
        private FileAction(String name) {
            super(name);
        }
        private FileAction(String name, char ch, int modifiers) {
            super(name);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, modifiers));
            int index = name.toUpperCase().indexOf(ch);
            if(index != -1) {
                putValue(DISPLAYED_MNEMONIC_INDEX_KEY, index);
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(this == saveAction) {
                saveOperation();
                return;
            } else if(this == saveAsAction) {
                saveAsOperation();
                return;
            } else if(this == openAction) {
                checkForSave();
                Path file = showDialog("Open Sketch File", "Open", "Read a sketch from file",
                                        sketchFilter, null);
                if(file != null) {
                    if(openSketch(file)) {
                        currentSketchFile = file;
                        setTitle(frameTitle + " - " + currentSketchFile);
                        sketchChanged = false;
                    }
                }   
            } else if(this == newAction || this == closeAction) {
                checkForSave();
                theApp.insertModel(new SketcherModel());
                currentSketchFile = null;
                setTitle(frameTitle);
                sketchChanged = false;
                return;
            } else if(this == printAction) {
                if(printer == null) {
                    JOptionPane.showMessageDialog(SketcherFrame.this,
                                                  "No default printer available.",
                                                  "Printer Error",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
                printJob.setPageable(theApp.getView());
                boolean printIt = true;
                if(!(e.getSource() instanceof JButton)) {
                    printIt = printJob.printDialog();
                }
                if(printIt) {
                    try {
                        printJob.print(printAttr);
                    } catch(PrinterException pe) {
                        System.out.println(pe);
                        JOptionPane.showMessageDialog(SketcherFrame.this,
                                                      "Error printing a sketch.",
                                                      "Printer Error",
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if(this == exitAction) {
                checkForSave();
                System.exit(0);
            }
        }
    }
    
    // Inner class defining Action objects for Element Type menu items
    class TypeAction extends AbstractAction {
        private TypeAction(String name, int typeID) {
            super(name);
            this.typeID = typeID;
        }
        private TypeAction(String name, int typeID, char ch, int modifiers) {
            this(name,typeID);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, modifiers));
            int index = name.toUpperCase().indexOf(ch);
            if(index != -1) {
                putValue(DISPLAYED_MNEMONIC_INDEX_KEY, index);
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            elementType = typeID;
            setChecks(colorMenu, e.getSource());
            statusBar.setTypePane(typeID);
        }
        private int typeID;
    }
    
    // Inner class defining Action objects for Element Color menu items
    class ColorAction extends AbstractAction {
        private ColorAction(String name, Color color) {
            super(name);
            this.color = color;
        }
        private ColorAction(String name, Color color, char ch, int modifiers) {
            this(name, color);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, modifiers));
            int index = name.toUpperCase().indexOf(ch);
            if(index != -1) {
                putValue(DISPLAYED_MNEMONIC_INDEX_KEY, index);
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(this == customAction) {
                Color newColor = JColorChooser.showDialog(SketcherFrame.this,
                                                "Select Custom Color", customColor);
                if(newColor != null) {
                    elementColor = customColor = newColor;
                    setCustomIconColor(CUSTOM16, customColor);
                    setCustomIconColor(CUSTOM24, customColor);
                    toolBar.repaint();
                }
            } else {
                elementColor = color;
            }
            statusBar.setColorPane(elementColor);
            setChecks(colorMenu, e.getSource());
        }
        private Color color;
    }
     
    private Sketcher theApp;
    private JMenu elementMenu, colorMenu;
    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private StatusBar statusBar = new StatusBar();
    private JMenuItem aboutItem;
    //private JMenu optionsMenu;
    private FontDialog fontDialog;
    private JMenuItem fontItem;
    private JPopupMenu popup = new JPopupMenu("General");
    
    private String frameTitle;
    private Path currentSketchFile;
    private boolean sketchChanged = false;
    private JFileChooser fileChooser;
    private ExtensionFilter sketchFilter = new ExtensionFilter(".ske", "Sketch files(*.ske)");
    
    private PrinterJob printJob;
    private PageFormat pageFormat;
    private PrintService printer;
    private HashPrintRequestAttributeSet printAttr = new HashPrintRequestAttributeSet();
    
    private Color customColor = DEFAULT_ELEMENT_COLOR;
    private Color elementColor = DEFAULT_ELEMENT_COLOR;
    private int elementType = DEFAULT_ELEMENT_TYPE;
    private Font textFont = DEFAULT_FONT;
    
    private FileAction newAction, openAction, closeAction, saveAction, saveAsAction,
                       printAction, exitAction;
    private FileAction[] fileActions;
    
    private TypeAction lineAction, rectangleAction, circleAction, curveAction, textAction;
    private TypeAction[] typeActions;
    
    private ColorAction redAction, greenAction, blueAction, yellowAction, customAction;
    private ColorAction[] colorActions;  
}