
package sketcherapp;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import static sketcherapp.SketcherConstants.*;

public class FontDialog extends JDialog implements ActionListener, ListSelectionListener, ChangeListener {
    
    public FontDialog(SketcherFrame window) {
        super(window, "Font Selection", true);
        font = window.getFont();
        // create the dialog button panel
        JPanel buttonPane = new JPanel();
        buttonPane.add(ok = createButton("OK"));
        buttonPane.add(cancel = createButton("Cancel"));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        // create the data input panel
        JPanel dataPane = new JPanel();
        dataPane.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createLineBorder(Color.black),
                           BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        GridBagLayout gbLayout = new GridBagLayout();
        dataPane.setLayout(gbLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        // code to create font choice and add it to the input panel
        JLabel label = new JLabel("Choose a Font");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gbLayout.setConstraints(label, constraints);
        dataPane.add(label);
        // code to set up font list choice component
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = e.getAvailableFontFamilyNames();
        fontList = new JList<>(fontNames);
        fontList.setValueIsAdjusting(true);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontList.setSelectedValue(font.getFontName(), true);
        fontList.addListSelectionListener(this);
        fontList.setToolTipText("Choose a font");
        JScrollPane chooseFont = new JScrollPane(fontList);
        chooseFont.setMinimumSize(new Dimension(400,100));
        chooseFont.setWheelScrollingEnabled(true);
        // Panel to display font sample
        JPanel display = new JPanel(true);
        fontDisplay = new JLabel("Sample Size: x X y Y z Z");
        fontDisplay.setFont(font);
        fontDisplay.setPreferredSize(new Dimension(350,100));
        display.add(fontDisplay);
        // create split pane with font choice at top and font display at bottom
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, chooseFont, display);
        gbLayout.setConstraints(splitPane, constraints);
        dataPane.add(splitPane);
        // set up font size choice using a spinner
        JPanel sizePane = new JPanel(true);
        label = new JLabel("Choose point size: ");
        sizePane.add(label);
        chooseSize = new JSpinner(new SpinnerNumberModel(font.getSize(), POINT_SIZE_MIN,
                                        POINT_SIZE_MAX, POINT_SIZE_STEP));
        chooseSize.setValue(font.getSize());
        chooseSize.addChangeListener(this);
        sizePane.add(chooseSize);
        gbLayout.setConstraints(sizePane, constraints);
        dataPane.add(sizePane);
        // set up style options using radio buttons
        bold = new JRadioButton("Bold", (font.getStyle() & Font.BOLD) > 0);
        italic = new JRadioButton("Italic", (font.getStyle() & Font.ITALIC) > 0);
        bold.addItemListener(new StyleListener(Font.BOLD));
        italic.addItemListener(new StyleListener(Font.ITALIC));
        JPanel stylePane = new JPanel(true);
        stylePane.add(bold);
        stylePane.add(italic);
        gbLayout.setConstraints(stylePane, constraints);
        dataPane.add(stylePane);
        getContentPane().add(dataPane, BorderLayout.CENTER);
        pack();
        setVisible(false); 
    }
    
    JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(80,20));
        button.addActionListener(this);
        return button;
    }
    
    // ActionListener method
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == ok) {
            ((SketcherFrame)getOwner()).setFont(font);
        } else {
            font = ((SketcherFrame)getOwner()).getFont();
            fontDisplay.setFont(font);
            chooseSize.setValue(font.getSize());
            fontList.setSelectedValue(font.getName(),true);
            int style = font.getStyle();
            bold.setSelected((style & Font.BOLD) > 0);
            italic.setSelected((style & Font.ITALIC) > 0);
        }
        setVisible(false); // hide the dialog for ok or cancel
    }
    
    // ListSelectionListener method
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            font = new Font(fontList.getSelectedValue(), font.getStyle(), font.getSize());
            fontDisplay.setFont(font);
            fontDisplay.repaint();
        }
    }
    
    // ChangeListener method
    @Override
    public void stateChanged(ChangeEvent e) {
        int fontSize = ((Number)(((JSpinner)e.getSource()).getValue())).intValue();
        font = font.deriveFont((float)fontSize);
        fontDisplay.setFont(font);
        fontDisplay.repaint();
    }
    
    class StyleListener implements ItemListener {
        public StyleListener(int style) {
            this.style = style;
        }
        public void itemStateChanged(ItemEvent e) {
            int fontStyle = font.getStyle();
            if(e.getStateChange() == ItemEvent.SELECTED) {
                fontStyle |= style;
            } else {
                fontStyle &= ~style;
            }
            font = font.deriveFont(fontStyle);
            fontDisplay.setFont(font);
            fontDisplay.repaint();
        }
        private int style;
    }
    
    private Font font;
    private JButton ok;
    private JButton cancel;
    private JList<String> fontList;
    private JSpinner chooseSize;
    private JLabel fontDisplay;
    private JRadioButton bold;
    private JRadioButton italic; 
}