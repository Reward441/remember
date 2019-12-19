package remember;

import javax.swing.*;
import java.awt.*;

public class Checkbox extends JCheckBox implements ListCellRenderer{

    int id;
    Status status;

    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setSelected(isSelected);
        setEnabled(list.isEnabled());

        setText(value == null ? "" : value.toString());
        if (isSelected) this.setSelected(false);
        return this;
    }

    void check() {
    }
    void partiallyCheck() {
    }
    void uncheck(){

    }

}
