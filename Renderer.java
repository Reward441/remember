package remember;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

import static remember.JListNest.*;
import static remember.Remember.*;

public class Renderer extends JCheckBox implements ListCellRenderer<Layer> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Layer> list, Layer value, int index, boolean isSelected, boolean cellHasFocus) {
        setSelected(value.checked());
        setComponentOrientation(list.getComponentOrientation());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setEnabled(list.isEnabled());
        setText(value.toString());
        setBackground(Color.WHITE);
        if(partiallyCheckedSubcats.contains(value))
            setForeground(Color.ORANGE);
//        if(value instanceof Subcategory)
        setBorder(new EtchedBorder());
        if (isSelected)
            setBorderPainted(true);
        else
            setBorderPainted(false);
        return this;
    }

}