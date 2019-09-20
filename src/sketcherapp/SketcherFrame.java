
package sketcherapp;

import java.awt.event.*;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.*;
import static sketcherapp.SketcherConstants.*;
import static java.awt.Color.*;
import static java.awt.AWTEvent.*;
import static java.awt.event.InputEvent.*;
import static javax.swing.Action.*;

public class SketcherFrame extends JFrame {
    
    @SuppressWarnings("serial")
    public SketcherFrame(String title, Sketcher theApp) {
        setTitle(title);
        this.theApp = theApp;
        setJMenuBar(menuBar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        createFileMenu();
        createElementMenu();
        createColorMenu();
        
        createToolBar();
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        getContentPane().add(toolBar, BorderLayout.NORTH);
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
        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.add(closeAction);
        fileMenu.addSeparator();
        fileMenu.add(saveAction);
        fileMenu.add(saveAsAction);
        fileMenu.addSeparator();
        fileMenu.add(printAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        menuBar.add(fileMenu);
    }
    private void createElementTypeActions() {
        lineAction = new TypeAction("Line", LINE, 'L', CTRL_DOWN_MASK);
        rectangleAction = new TypeAction("Rectangle", RECTANGLE, 'R', CTRL_DOWN_MASK);
        circleAction = new TypeAction("Circle", CIRCLE, 'C', CTRL_DOWN_MASK);
        curveAction = new TypeAction("Curve", CURVE, 'U', CTRL_DOWN_MASK);
        TypeAction[] actions = {lineAction, rectangleAction, circleAction, curveAction};
        typeActions = actions;
        // add toolbar icons
        lineAction.putValue(LARGE_ICON_KEY, LINE24);
        rectangleAction.putValue(LARGE_ICON_KEY, RECTANGLE24);
        circleAction.putValue(LARGE_ICON_KEY, CIRCLE24);
        curveAction.putValue(LARGE_ICON_KEY, CURVE24);
        // add menu bar icons
        lineAction.putValue(SMALL_ICON, LINE16);
        rectangleAction.putValue(SMALL_ICON, RECTANGLE16);
        circleAction.putValue(SMALL_ICON, CIRCLE16);
        curveAction.putValue(SMALL_ICON, CURVE16);
        // add tooltip text
        lineAction.putValue(SHORT_DESCRIPTION, "Draw lines");
        rectangleAction.putValue(SHORT_DESCRIPTION, "Draw rectangles");
        circleAction.putValue(SHORT_DESCRIPTION, "Draw circles");
        curveAction.putValue(SHORT_DESCRIPTION, "Draw curves");
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
        ColorAction[] actions = {redAction, yellowAction, greenAction, blueAction};
        colorActions = actions;
        // add toolbar icons
        redAction.putValue(LARGE_ICON_KEY, RED24);
        greenAction.putValue(LARGE_ICON_KEY, GREEN24);
        blueAction.putValue(LARGE_ICON_KEY, BLUE24);
        yellowAction.putValue(LARGE_ICON_KEY, YELLOW24);
        /// add menu bar icons
        redAction.putValue(SMALL_ICON, RED16);
        greenAction.putValue(SMALL_ICON, GREEN16);
        blueAction.putValue(SMALL_ICON, BLUE16);
        yellowAction.putValue(SMALL_ICON, YELLOW16);
        // add tooltip text
        redAction.putValue(SHORT_DESCRIPTION, "Draw in red");
        greenAction.putValue(SHORT_DESCRIPTION, "Draw in green");
        blueAction.putValue(SHORT_DESCRIPTION, "Draw in blue");
        yellowAction.putValue(SHORT_DESCRIPTION, "Draw in yellow");
    }
    private void createColorMenu() {
        createElementColorActions();
        colorMenu = new JMenu("Color");
        colorMenu.setMnemonic('C');
        createRadioButtonDropDown(colorMenu, colorActions, blueAction);
        menuBar.add(colorMenu);
    }
    // radio button menu items for Element Menu and Color Menu
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
    
    // setting menu item checks when a toolbar button is clicked for Elements and Color menu
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
    
    // inner class defining Action objects for File Menu items
    class FileAction extends AbstractAction {
        FileAction(String name) {
            super(name);
        }
        FileAction(String name, char ch, int modifiers) {
            super(name);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, modifiers));
            int index = name.toUpperCase().indexOf(ch);
            if(index != -1) {
                putValue(DISPLAYED_MNEMONIC_INDEX_KEY, index);
            }
        }
        public void actionPerformed(ActionEvent e) {
            
        }
    }
    // inner class defining Action objects for Element Type menu items
    class TypeAction extends AbstractAction {
        TypeAction(String name, int typeID) {
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
        public void actionPerformed(ActionEvent e) {
            elementType = typeID;
            setChecks(colorMenu, e.getSource());
        }
        private int typeID;
    }
    // inner class defining Action objects for Element Color menu items
    class ColorAction extends AbstractAction {
        public ColorAction(String name, Color color) {
            super(name);
            this.color = color;
        }
        public ColorAction(String name, Color color, char ch, int modifiers) {
            this(name, color);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, modifiers));
            int index = name.toUpperCase().indexOf(ch);
            if(index != -1) {
                putValue(DISPLAYED_MNEMONIC_INDEX_KEY, index);
            }
        }
        public void actionPerformed(ActionEvent e) {
            elementColor = color;
            setChecks(colorMenu, e.getSource());
        }
        private Color color;
    }
    
    // return the current drawing color
    public Color getElementColor() {
        return elementColor;
    }
    // return the current element type
    public int getElementType() {
        return elementType;
    }
    
    private Sketcher theApp;
    private JMenu elementMenu, colorMenu;
    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private Color elementColor = DEFAULT_ELEMENT_COLOR;
    private int elementType = DEFAULT_ELEMENT_TYPE;
    
    private FileAction newAction, openAction, closeAction, saveAction, saveAsAction,
                       printAction, exitAction;
    private FileAction[] fileActions;
    
    private TypeAction lineAction, rectangleAction, circleAction, curveAction;
    private TypeAction[] typeActions;
    
    private ColorAction redAction, greenAction, blueAction, yellowAction;
    private ColorAction[] colorActions;  
}
