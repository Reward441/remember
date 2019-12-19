package remember;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import javax.swing.*;

import static remember.Remember.*;

public class JListNest extends JPanel {

    static DefaultListModel<Category> catListModel = new DefaultListModel<>();
    static DefaultListModel<Subcategory> subcatListModel = new DefaultListModel<>();
    static DefaultListModel<Message> messageListModel = new DefaultListModel<>();
    static DefaultListModel<Message> abbrevListModel = new DefaultListModel<>();
    static JList<Category> catList = new JList<>(catListModel);
    static JList<Subcategory> subcatList = new JList<>(subcatListModel);
    static JList<Message> messageList = new JList<>(messageListModel);
    static JList<Message> abbrevList = new JList<>(abbrevListModel);
    static JScrollPane catListPane = new JScrollPane(catList);
    static JScrollPane subcatListPane = new JScrollPane(subcatList);
    static JScrollPane messageListPane = new JScrollPane(messageList);
    static Category currentCat;
    static Subcategory currentSubcat;
    static Message currentMessage;
    static int currentCatID, currentSubcatID;
    static TreeSet<String> mess;
    static boolean emptySubs, emptyMess;
    static boolean subSelected, messSelected;
    static Boolean inCheckbox = false;
    int f = 0;
    int g = 0;

    public JListNest() {
        super(new GridLayout(0, 2));
        add(catListPane);
        add(subcatListPane);
    }

    public void setLists() {
        catList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        catList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        catList.setVisibleRowCount(-1);
        catList.setFixedCellWidth(160);
        catList.setCellRenderer(new Renderer());
        subcatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subcatList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        subcatList.setVisibleRowCount(-1);
        subcatList.setFixedCellWidth(320);
        subcatList.setCellRenderer(new Renderer());
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        messageList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        messageList.setVisibleRowCount(-1);
        messageList.setFixedCellWidth(720);
        messageList.setCellRenderer(new Renderer());

        catList.setSelectionModel(new DefaultListSelectionModel() {
            private static final long serialVersionUID = 1L;
            boolean gestureStarted = false;

            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (!gestureStarted) {
                    subSelected = false;
                    catList.clearSelection();
                    super.addSelectionInterval(index0, index1);
                    subcatList.clearSelection();
                    messageList.clearSelection();
                    subcatListModel.clear();
                    messageListModel.clear();
                    currentCat = catList.getSelectedValue();
                    mouseListener(catList, 1);
                    currentCatID = currentCat.id;
                    if (currentCat.subcategories.size() > 0) {
                        emptySubs = false;
                        for (Subcategory subcat : currentCat.subcategories) {
                            subcatListModel.addElement(subcat);
                            subcatListModel.elements();
                        }
                    } else emptySubs = true;
                    Component[] buttons = {addSubcategory, modify, delete, addMessage, modifyMess, deleteMess, flash, listen};
                    for (int i = 0; i <buttons.length ; i++) {
                        buttons[i].setEnabled(i<3);
                    }
                }
                gestureStarted = true;
            }

            @Override
            public void setValueIsAdjusting(boolean isAdjusting) {
                if (!isAdjusting) {
                    gestureStarted = false;
                }
            }
        });

        subcatList.setSelectionModel(new DefaultListSelectionModel() {
            private static final long serialVersionUID = 1L;

            boolean gestureStarted = false;

            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (!gestureStarted) {
                    subSelected = true;
                    messSelected = false;
                    subcatList.clearSelection();
                    super.addSelectionInterval(index0, index1);
                    messageList.clearSelection();
                    messageListModel.clear();
                    currentSubcat = subcatList.getSelectedValue();
                    mouseListener(subcatList, 2);
                    currentSubcatID = currentSubcat.id;
                    int subcatIndex = currentCat.subcatIDs.indexOf(currentSubcatID);
                    // Set colour according to subcategory's id.
                    currentSubcat.setCol(new Color(Colour.values()[subcatIndex].rgb));
                    if (currentSubcat.messages.size() > 0) {
                        emptyMess = false;
                        for (Message mess : currentSubcat.messages) {
                            messageListModel.addElement(mess);
                        }
                    } else emptyMess = true;
                    delete.setEnabled(true);
                    modify.setEnabled(true);
                    addMessage.setEnabled(true);
                    deleteMess.setEnabled(false);
                }
                gestureStarted = true;
            }

            @Override
            public void setValueIsAdjusting(boolean isAdjusting) {
                if (!isAdjusting) {
                    gestureStarted = false;
                }
            }
        });

        messageList.setSelectionModel(new DefaultListSelectionModel() {
            private static final long serialVersionUID = 1L;

            boolean gestureStarted = false;

            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (!gestureStarted) {
                    subSelected = true;
                    messSelected = true;
                    messageList.clearSelection();
                    super.addSelectionInterval(index0, index1);
                    currentMessage = messageList.getSelectedValue();
                    mouseListener(messageList, 4);
                    modifyMess.setEnabled(true);
                    deleteMess.setEnabled(true);
                    // Must modify to enable flash only if checkedMessages is not empty.
                    flash.setEnabled(true);
                    listen.setEnabled(true);
                    save_mp3.setEnabled(true);
                    delete.setEnabled(false);
                    modify.setEnabled(false);

                }
                gestureStarted = true;
            }

            @Override
            public void setValueIsAdjusting(boolean isAdjusting) {
                if (!isAdjusting) {
                    gestureStarted = false;
                }
            }
        });
    }

    public void mouseListener(JList list, int layer) {
        g = 0;
        // Get the position of the click
        int index = list.getSelectedIndex();
        int column = index % (4 / layer);
        // Subcategory's second column is category's third column, hence adjust.
        if (layer == 2 & column == 1) column++;
        int row = index / (4 / layer);
        int firstX = 2 + column * 160;
        int secondX = firstX + 12;
        int firstY = 4 + row * 20;
        int secondY = firstY + 12;
        list.addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent e) {
                final int x = e.getX();
                final int y = e.getY();
                boolean firstCoord = x >= firstX && y >= firstY;
                boolean secondCoord = x <= secondX && y <= secondY;
                inCheckbox = firstCoord && secondCoord;
                Status status;
                if (inCheckbox && f == g) {
                    if (layer == 1) {
                        status = currentCat.toggleStatus();
                        if (!emptySubs)
                            for (Subcategory subcat : currentCat.subcategories) {
                                subcat.setStatus(status);
                                modCheckedMessages(subcat, status);
                            }
                    } else if (layer == 2) {
                        status = currentSubcat.toggleStatus();
                        for (Subcategory subcat : currentCat.subcategories) {

                        }
                        currentCat.setStatus(status);
                        if (!emptyMess) {
                            modCheckedMessages(currentSubcat, status);
                        }
                        if (currentSubcat.messages.size() > 1)
                            for (Subcategory subcat : currentCat.subcategories) {
                                if (subcat.getStatus() != status) {
                                    currentCat.setStatus(Status.PartiallyChecked);
                                    partiallyCheckedCats.add(currentCat);
                                }
                            }
                    } else {
                        status = currentMessage.toggleStatus();

                        currentSubcat.setStatus(status);
                        currentCat.setStatus(status);
                        if (currentSubcat.messages.size() > 1)
                            for (Message m : currentSubcat.messages) {
                                if (m.getStatus() != status) {
                                    currentSubcat.setStatus(Status.PartiallyChecked);
                                    partiallyCheckedSubcats.add(currentSubcat);
                                    currentCat.setStatus(Status.PartiallyChecked);
                                    partiallyCheckedCats.add(currentCat);
                                }
                            }
                        if (currentMessage.checked()) checkedMessages.put(currentMessage,currentSubcat);
                        else checkedMessages.remove(currentMessage);
                    }
                    g += 8;
                    theApp.repaint();
                }
            }
        });
    }

    void modCheckedMessages(Subcategory sub, Status status) {
        for (Message m : sub.messages) {
            m.setStatus(status);
            if (m.checked()) checkedMessages.put(m,sub);
            else checkedMessages.remove(m);
        }
    }

}