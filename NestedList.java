package remember;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class NestedList extends JPanel implements ListSelectionListener {
    private DefaultListModel model = new DefaultListModel();
    private JList list1 = new JList(model);
    private JList list2 = new JList();
    JScrollPane pane1 = new JScrollPane(list1);
    JScrollPane pane2 = new JScrollPane(list2);

    private NestedList() {
        super(new GridLayout(0, 2));
        add(pane1);
        add(pane2);
        list1.addListSelectionListener(this);
    }

    public void addItem(String item, List<String> subitems) {
        model.addElement(new NestedItem(item, subitems));
    }

    public void valueChanged(ListSelectionEvent e) {
        NestedItem item =
                (NestedItem) list1.getSelectedValue();
        list2.setModel(item.subitems);
    }

    private static class NestedItem {
        private String item = null;
        private DefaultListModel subitems = new DefaultListModel();

        NestedItem(String item, List<String> subitems) {
            this.item = item;
            for (String subitem : subitems)
                this.subitems.addElement(subitem);
        }

        public String toString() {
            return item;
        }
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Nested Lists");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        NestedList list = new NestedList();
        list.addItem("numbers", new ArrayList(Arrays.asList("1", "2", "3")));
        list.addItem("letters", new ArrayList(Arrays.asList("A", "B", "C")));
        frame.add(list);
        frame.pack();
        frame.setVisible(true);
    }
}