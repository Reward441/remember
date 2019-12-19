package remember;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static remember.JListNest.*;

public class Remember extends JFrame {

    static Remember theApp;
    static List<Category> categories;
    private static JLabel interval, duration;
    static List<Category> partiallyCheckedCats;
    static List<Subcategory> partiallyCheckedSubcats;
    // Text fields for duration and interval.
    static JTextField fd = new JTextField("2", 4);
    static JTextField fi = new JTextField("2", 4);
    private JListNest jln = new JListNest();
    static Map<Message, Subcategory> checkedMessages;
    List<FlashBox> textboxes;
    private static List<JButton> jb = new ArrayList<>();
    static JButton addSubcategory, addMessage;
    static JButton delete, deleteMess, modify, modifyMess;
    static JButton flash, listen, save_mp3;
    private static JTabbedPane tabs = new JTabbedPane();
    static JComboBox catBox, subcatBox, colours, shapes, fonts, sizes, textColours;
    static DefaultComboBoxModel catBoxModel, subcatBoxModel;
    static Map<String, String> abbreviations, flashables;
    static List<String> abbrevCols;
    static final Vector<String> cols=new Vector<>(Arrays.asList("Abbreviation", "Meaning"));;
    static Vector<Vector<String>> rows = new Vector<>();
//    rows={{"SW","spatial world"}};
    JTable abbrevTable;
    JLabel abbrevs;

    public Remember() throws HeadlessException {
        createComponents();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        Remember.categories = categories;
    }

    public static JLabel getDuration() {
        return duration;
    }

    public static JLabel getInterval() {
        return interval;
    }

    private void createComponents() {
        abbrevTable=new JTable(rows,cols);
        abbrevs=new JLabel("Abbreviations");
        partiallyCheckedCats = new ArrayList<>();
        partiallyCheckedSubcats = new ArrayList<>();
        abbreviations = new TreeMap<>();
        theApp = this;
        jln.setLists();
        categories = new ArrayList<>();
        checkedMessages = new TreeMap<>();
        JLabel cats, subcats, messLabel;
        cats = new JLabel("Categories");
        subcats = new JLabel("Subcategories");
        messLabel = new JLabel("Messages");
        duration = new JLabel("Flash duration: ");
        interval = new JLabel("Flash interval:  ");
//        open = new JButton("Open");
//        save = new JButton("Save");
//        save_mp3 = new JButton("Save as .mp3");
        JButton incDuration, incInterval, decDuration, decInterval;
        incDuration = new JButton("+ 0.5");
        decDuration = new JButton("- 0.5");
        incInterval = new JButton("+ 0.5");
        decInterval = new JButton("- 0.5");
        incDuration.addActionListener(new ButtonHandler("Increment Duration"));
        decDuration.addActionListener(new ButtonHandler("Decrement Duration"));
        incInterval.addActionListener(new ButtonHandler("Increment Interval"));
        decInterval.addActionListener(new ButtonHandler("Decrement Interval"));
        colours = new JComboBox<>(Colour.values());
        colours.addActionListener(e -> {
                    int rgb = ((Colour) colours.getSelectedItem()).rgb;
                    Subcategory subcat = (Subcategory) subcatBox.getSelectedItem();
                    subcat.setCol(new Color(rgb));
                }
        );

        JLabel selectCategory, selectSubcategory, selectColour, selectShape,
                selectTextFont, selectTextSize, selectTextColour;
        selectColour = new JLabel("Select flashbox colour");
        shapes = new JComboBox<>(FlashBoxShape.values());
        shapes.addActionListener(e -> {
                    Shape shape = ((FlashBoxShape) shapes.getSelectedItem()).shape;
                    Subcategory subcat = (Subcategory) subcatBox.getSelectedItem();
                    subcat.setShape(shape);
                }
        );
        selectShape = new JLabel("Select flashbox shape");
        fonts = new JComboBox<>(MessageFont.values());
        fonts.addActionListener(e -> {
                    Font font = ((MessageFont) fonts.getSelectedItem()).font;
                    Subcategory subcat = (Subcategory) subcatBox.getSelectedItem();
                    subcat.setFont(font);
                }
        );
        selectTextFont = new JLabel("Select font");
        sizes = new JComboBox<>(TextSize.SIZES);
        sizes.addActionListener(e -> {
                    int size = (Integer) sizes.getSelectedItem();
                    Subcategory subcat = (Subcategory) subcatBox.getSelectedItem();
                    subcat.setSize(size);
                }
        );
        selectTextSize = new JLabel("Select text size");
        textColours = new JComboBox<>(Colour.values());
        textColours.addActionListener(e -> {
                    int rgb = ((Colour) textColours.getSelectedItem()).rgb;
                    Subcategory subcat = (Subcategory) subcatBox.getSelectedItem();
                    subcat.setTextCol(new Color(rgb));
                }
        );
        selectTextColour = new JLabel("Select text colour");
        catBoxModel = new DefaultComboBoxModel();
        subcatBoxModel = new DefaultComboBoxModel();
        catBox = new JComboBox<>(catBoxModel);
        catBox.addActionListener(e -> {
                    Category c = (Category) catBox.getSelectedItem();
                    subcatBoxModel.removeAllElements();
                    for (Subcategory s : c.subcategories) subcatBoxModel.addElement(s);
                }
        );
        selectCategory = new JLabel("Select category");
        subcatBox = new JComboBox<>(subcatBoxModel);
        subcatBox.addActionListener(e -> {
                    Subcategory subcat = (Subcategory) subcatBox.getSelectedItem();
                    colours.setSelectedItem(subcat.getCol());
                    shapes.setSelectedItem(subcat.getShape());
                    fonts.setSelectedItem(subcat.getFont());
                    sizes.setSelectedItem(subcat.getSize());
                    textColours.setSelectedItem(subcat.getTextCol());

                    int rgb = ((Colour) colours.getSelectedItem()).rgb;
                    subcat.setCol(new Color(rgb));
                }
        );
        selectSubcategory = new JLabel("Select subcategory");
        List<JComboBox> boxes = new ArrayList<>(Arrays.asList(catBox, subcatBox, shapes, colours, fonts, sizes, textColours));
        for (JComboBox box : boxes) box.setEnabled(false);
        String d = "Delete";
        String m = "Modify";
        List<String> buttonNames = new ArrayList<>(Arrays.asList("Category", "Subcategory", "Message", "Abbreviation", "Delete Abbreviation",
                m, m, d, d, "Flash", "Listen", "Open", "Save", "Save As .mp3"));
        for (int i = 0; i < buttonNames.size(); i++) {
            String s = buttonNames.get(i);
            jb.add(new JButton(i > 3 ? s : "New " + s));
            JButton button = jb.get(i);
            if (!(i + "").matches("0|3|4|11|12")) button.setEnabled(false);
            button.addActionListener(new ButtonHandler(s));
        }
        JButton open, addCategory, addAbbrev, deleteAbbrev, save;
        addCategory = jb.get(0);
        addSubcategory = jb.get(1);
        addMessage = jb.get(2);
        addAbbrev = jb.get(3);
        deleteAbbrev = jb.get(4);
        modify = jb.get(5);
        modifyMess = jb.get(6);
        delete = jb.get(7);
        deleteMess = jb.get(8);
        flash = jb.get(9);
        listen = jb.get(10);
        open = jb.get(11);
        save = jb.get(12);
        save_mp3 = jb.get(13);
        JPanel home, settings, flashDuration, flashInterval;
        home = new JPanel();
        home.setLayout(new BorderLayout());
        JPanel buttonPanel1, buttonPanel2, settingsCentre, settingsRight;
        buttonPanel1 = new JPanel();
        buttonPanel2 = new JPanel();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel shape_col = new JPanel();
        settings = new JPanel();
        flashDuration = new JPanel();
        flashInterval = new JPanel();
        settingsCentre = new JPanel();
        settingsRight = new JPanel();
//        fd.addActionListener(e -> {
//                    int size = (Integer) sizes.getSelectedItem();
//                    Subcategory subcat = (Subcategory) subcatBox.getSelectedItem();
//                    subcat.setSize(size);
//                }
//        );
//        fi.addActionListener(e -> {
//                    int interv = Integer.parseInt( fi.getText());
//                    Subcategory subcat = (Subcategory) subcatBox.getSelectedItem();
//                    subcat.setSize(size);
//                }
//        );
        flashDuration.add(duration);
        flashDuration.add(fd);
        flashDuration.add(incDuration);
        flashDuration.add(decDuration);
        flashInterval.add(interval);
        flashInterval.add(fi);
        flashInterval.add(incInterval);
        flashInterval.add(decInterval);
        settingsCentre.add(emptySpace());
        settingsCentre.add(flashDuration);
        settingsCentre.add(flashInterval);
        settingsRight.add(emptySpace());
        settingsRight.add(abbrevs);
        settingsRight.add(abbrevTable);
        settingsRight.add(emptySpace());
        settingsRight.add(addAbbrev);
        settingsRight.add(emptySpace());
        settingsRight.add(deleteAbbrev);
        flashDuration.setMaximumSize(flashDuration.getPreferredSize());
        flashInterval.setMaximumSize(flashInterval.getPreferredSize());
        settings.setLayout(new BorderLayout());
        jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));
        jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS));
        shape_col.setLayout(new BoxLayout(shape_col, BoxLayout.Y_AXIS));
        settingsCentre.setLayout(new BoxLayout(settingsCentre, BoxLayout.Y_AXIS));
        settingsRight.setLayout(new BoxLayout(settingsRight, BoxLayout.Y_AXIS));
        Component[] buttonPanelCompos = {addCategory, addSubcategory, modify, delete, save, addMessage, modifyMess, deleteMess, flash, listen, save_mp3};
        for (int i = 0; i < buttonPanelCompos.length; i++) {
            if (i < 5) buttonPanel1.add(buttonPanelCompos[i]);
            else buttonPanel2.add(buttonPanelCompos[i]);
        }
        catListPane.setPreferredSize(new Dimension(664, 312));
        subcatListPane.setPreferredSize(new Dimension(664, 312));
        messageListPane.setPreferredSize(new Dimension(800, 664));
        Component[] jp1Compos = {emptySpace(), emptySpace(), cats, catListPane, emptySpace(), subcats, subcatListPane, emptySpace(), buttonPanel1};
        for (Component compo : jp1Compos) jp1.add(compo);
        jp2.add(emptySpace());
        jp2.add(emptySpace());
        jp2.add(messLabel);
        jp2.add(messageListPane);
        jp2.add(emptySpace());
        jp2.add(buttonPanel2);
        JComponent[] leftAlign = {subcats, catListPane, subcatListPane, buttonPanel1, buttonPanel2, subcats, messLabel, messageListPane,
                selectCategory, catBox, selectSubcategory, subcatBox, selectColour, colours, selectShape, shapes, selectTextFont, fonts, selectTextSize, sizes,
                selectTextColour, textColours, duration, interval, abbrevs, abbrevTable};
        for (JComponent compo : leftAlign) compo.setAlignmentX(Component.LEFT_ALIGNMENT);
        Component[] shapeCols = {emptySpace(), selectCategory, catBox, emptySpace(), selectSubcategory, subcatBox, emptySpace(),
                selectColour, colours, emptySpace(), selectShape, shapes, emptySpace(), selectTextFont, fonts, emptySpace(), selectTextSize, sizes, emptySpace(),
                selectTextColour, textColours, emptySpace(), bigSpace()};
        for (Component compo : shapeCols) shape_col.add(compo);
        JPanel op = new JPanel();
        op.add(open);
        home.add(op, BorderLayout.CENTER);
        home.add(jp1, BorderLayout.WEST);
        home.add(jp2, BorderLayout.EAST);
        settings.add(shape_col, BorderLayout.WEST);
        settings.add(settingsCentre, BorderLayout.CENTER);
        settings.add(settingsRight, BorderLayout.EAST);
        tabs.add("Home", home);
        tabs.add("Settings", settings);
        add(tabs);
        repaint();
    }

    public Component emptySpace() {
        return Box.createRigidArea(new Dimension(0, 16));
    }

    public Component bigSpace() {
        return Box.createRigidArea(new Dimension(0, 560));
    }

//    @Override
//    public String toString() {
//        for (Category cat : categories.values());
//        return null;
//    }

    public static void main(String[] args) {
        Remember app = new Remember();
        app.setTitle("Remember");
        app.setVisible(true);
        app.setSize(888, 800);
        app.pack();
        app.repaint();
        app.revalidate();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}