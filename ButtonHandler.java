package remember;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import marytts.exceptions.SynthesisException;
import marytts.signalproc.effects.StadiumEffect;

import static remember.JListNest.*;
import static remember.Remember.*;

public class ButtonHandler implements ActionListener {

    static int cid, sid, mid;
    private int flashCount, mesCount, listenCount;
    private String level;
    private List<JList> jlists = new ArrayList<>(Arrays.asList(catList, subcatList, messageList));
    static JWindow jw = new JWindow();
    private final static int JWINDOW_WIDTH = 584;
    private final static int JWINDOW__HEIGHT = 80;
    // Determine centre location of screen for the JWindow.
    private final static Dimension DIM = Toolkit.getDefaultToolkit().getScreenSize();
    private final static int CENTRE_X = (DIM.width - JWINDOW_WIDTH) / 2;
    private final static int CENTRE_Y = (DIM.height - JWINDOW__HEIGHT) / 2;
    JLabel jl = new JLabel();
    private double flashDuration, flashInterval;
    private Timer visibleTimer;
    private boolean on = true;
    static int once;
    static String storageJson;
    boolean opened;

    public ButtonHandler(String level) {
        this.level = level;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (level.matches("Category|Subcategory|Message")) {
            String input = JOptionPane.showInputDialog(level + " name: ");
            if ((input != null) && !input.equals("")) {
                if (level.equals("Category")) {
                    Category c = new Category(input, cid, new ArrayList<>());
                    categories.add(c);
                    catListModel.addElement(c);
                    catBoxModel.addElement(c);
                    cid++;
                }
                if (level.equals("Subcategory")) {
                    Subcategory sc = new Subcategory(input, sid, currentCatID, new ArrayList<>());
                    currentCat.subcategories.add(sc);
                    currentCat.subcatIDs.add(sid);
                    subcatListModel.addElement(sc);
                    sid++;
                }
                if (level.equals("Message")) {
                    boolean duplicate = false;
                    for (Message m : currentSubcat.messages)
                        if (m.name.equals(input)) duplicate = true;
                    if (!duplicate) {
                        Message mes = new Message(input, mid);
//                        mes.setSubcat(currentSubcat);
                        currentSubcat.messages.add(mes);
                        messageListModel.addElement(mes);
//                        if (catBoxModel.getIndexOf(currentCat) == -1)
//                            catBox.addItem(currentCat);
//                        catBox.setSelectedItem(currentCat);
//                        if (subcatBoxModel.getIndexOf(currentSubcat) == -1) {
//                            activeSubs.add(currentSubcat);
//                            subcatBox.addItem(currentSubcat);
//                        }
                        mid++;
                    }
                }
            }
        }
        if (level.equals("Abbreviation")) {
            String input = JOptionPane.showInputDialog("Abbreviation and definition (separated with =): ");
            int boundary = input.indexOf('=');
            String abb = input.substring(0, boundary);
            String def = input.substring(boundary + 1);
            rows.add(new Vector<>(Arrays.asList(abb, def)));
//            model.fireTableDataChanged();
        }
        if (level.equals("Modify")) {
            String input;
            String name;
            if (!subSelected) {
                name = currentCat.getName();
                input = JOptionPane.showInputDialog(level + " category name: ", currentCat.getName());
                if (input == null)
                    currentCat.setName(name);
                else currentCat.setName(input);
            } else if (!messSelected) {
                name = currentSubcat.getName();
                input = JOptionPane.showInputDialog(level + " subcategory name: ", currentSubcat.getName());
                if (input == null)
                    currentSubcat.setName(name);
                else currentSubcat.setName(input);
            } else {
                name = currentMessage.getName();
                input = JOptionPane.showInputDialog(level + " message name: ", currentMessage.getName());
                if (input == null)
                    currentMessage.setName(name);
                else currentMessage.setName(input);
            }
        }
        if (level.equals("Delete")) {
            int i;
            if (!subSelected) {
                // Delete category.
                System.out.println("Holmium 533");
                for (Subcategory s : currentCat.subcategories) {
                    i = subcatListModel.indexOf(s);
                    subcatListModel.remove(i);
                }
                i = catListModel.indexOf(currentCat);
                catListModel.remove(i);
                categories.remove(currentCat);
                catBoxModel.removeElement(currentCat);
                currentCat = null;
            } else if (!messSelected) {
                // Delete subcategory.
                for (Message m : currentSubcat.messages) {
                    i = messageListModel.indexOf(m);
                    messageListModel.remove(i);
                }
                i = subcatListModel.indexOf(currentSubcat);
                subcatListModel.remove(i);
                currentCat.subcategories.remove(currentSubcat);
                subcatBoxModel.removeElement(currentSubcat);
                currentSubcat = null;
            } else {
                // Delete message.
                i = messageListModel.indexOf(currentMessage);
                messageListModel.remove(i);
                currentSubcat.messages.remove(currentMessage);
            }
        }


        double durationValue = Double.parseDouble(fd.getText());
        double intervalValue = Double.parseDouble(fi.getText());
        if (level.equals("Flash") && !checkedMessages.isEmpty()) {
            mesCount = 0;
            jw.setSize(JWINDOW_WIDTH, JWINDOW__HEIGHT);
            jw.setLocation(CENTRE_X, CENTRE_Y);
            Colour col = (Colour) colours.getSelectedItem();
            jw.getContentPane().setBackground(new Color(col.rgb));
            FlashBoxShape flashShape = (FlashBoxShape) shapes.getSelectedItem();
            jw.setShape(flashShape.shape);
            jl.setHorizontalAlignment(JLabel.CENTER);
            // NB: text size is integrated in font object.
//            jl.setBackground(Color.white);
            if (flashCount % 2 == 0) {
                flash.setText("Stop");
                jw.add(jl);
                flashDuration = durationValue;
//                List<Message> checkedMess=new ArrayList<>(checkedMessages.keySet());
                // Get an entrySet to later iterate over elements of CheckedMessages.
                Set<Map.Entry<Message,Subcategory>> sme = checkedMessages.entrySet();
                // Timer with delay of 1s so minimum interval increase is 1s.
                visibleTimer = new Timer(1000, h -> {
                    // flashDuration (1s) and flashInterval (2s) will be made dynamic later.
                    if (on) {
                        if (flashDuration == durationValue) {
                            // Iterate over elements of CheckedMessages.
                            Iterator<Map.Entry<Message,Subcategory>> it=null;
                            Map.Entry<Message,Subcategory> me=null;
                            if (mesCount==0) it = sme.iterator();
                            if (mesCount < checkedMessages.size()) {
                                me=it.next();
                            } else{
                                mesCount = 0;
                                it = sme.iterator();
                                me=it.next();
                            }
                            jl.setText(me.getKey().getName());
                            Subcategory sub =me.getValue();
                            jl.setFont(sub.getFont());
                            jl.setForeground(sub.getTextCol());
                            jw.getContentPane().setBackground(sub.getCol());
                            jw.setAlwaysOnTop(true);
//                            jw.getContentPane().setBackground(Color.white);
                            jw.setVisible(true);
                            mesCount++;
                        }
                        if (flashDuration > 0) {
                            flashDuration--;
                        } else {
                            flashInterval = intervalValue;
                            jw.setVisible(false);
                            on = false;
                        }
                    }
                    if (!on) {
                        if (flashInterval > 0) {
                            flashInterval--;
                        }
                        if (flashInterval == 0) {
                            flashDuration = durationValue;
                            on = true;
                        }
                    }
                });
                visibleTimer.start();
            } else {
                visibleTimer.stop();
                flash.setText("Flash");
                jw.setVisible(false);
                jw.remove(jl);
            }
            flashCount++;
        }

        if (level.equals("Listen") && !checkedMessages.isEmpty()) {
            if (listenCount % 2 == 0) {
                TTS tts = new TTS();
                StadiumEffect stadiumEffect = new StadiumEffect();
                stadiumEffect.setParams("amount:0");
                tts.getMarytts().setAudioEffects(stadiumEffect.getFullEffectAsString());
                tts.setVoice("dfki-poppy-hsmm");
                System.out.println(tts.getAudioEffects());
//                tts.setAudioEffects("Rate(durScale:2.0)");

                listen.setText("Quiet");
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        for (int i = 0; i <= checkedMessages.size(); i++) {
                            if (i == checkedMessages.size()) i = 0;
                            if (listen.getText().equals("Quiet")) {
                                tts.speak(checkedMessages.get(i).getName(), 2.0f, false, true);
                            }
                        }
                        return null;
                    }
                };
                worker.execute();
            } else {
                listen.setText("Listen");
            }
            listenCount++;
        }

        switch (level) {
            case "Increment Duration":
                fd.setText(String.valueOf(durationValue + 0.5));
                break;
            case "Decrement Duration":
                fd.setText(String.valueOf(durationValue - 0.5));
                break;
            case "Increment Interval":
                fi.setText(String.valueOf(intervalValue + 0.5));
                break;
            case "Decrement Interval":
                fi.setText(String.valueOf(intervalValue - 0.5));
                break;
        }
        if (level.equals("Save")) {
            Storage storage = new Storage();
            Gson gson = new GsonBuilder().registerTypeAdapter(Shape.class, new InterfaceAdapter<Shape>())
                    .create();
            storageJson = gson.toJson(storage, Storage.class);
            JFileChooser fileChooser = new JFileChooser();
            System.out.println(storageJson);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    BufferedWriter output = new BufferedWriter(new FileWriter(file));
                    output.write(storageJson);
                    output.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        if (level.equals("Open")) {
            JFileChooser fileChooser = new JFileChooser();
            Storage storage = null;
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    FileInputStream fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);
                    fis.close();
                    String deserialise = new String(data, "UTF-8");
                    System.out.println(deserialise);
                    Gson gson = new GsonBuilder().registerTypeAdapter(Shape.class, new InterfaceAdapter<Shape>())
                            .create();
                    storage = gson.fromJson(deserialise, Storage.class);
                    opened = true;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (opened) {
                cid = storage.cats.length;
                sid = mid = 0;
                catListModel.clear();
                subcatListModel.clear();
                messageListModel.clear();
//                catBoxModel.removeAllElements();
//                subcatBoxModel.removeAllElements();
                for (Category c : storage.cats) {
                    currentCat = c;
                    categories.add(c);
                    catListModel.addElement(c);
                    catBox.addItem(c);
                    for (Subcategory sub : c.subcategories) {
                        subcatBox.addItem(sub);
                        if (sid < sub.getId()) sid = sub.getId();
                        for (Message mes : sub.messages)
                            if (mid < mes.getId()) mid = mes.getId();
                    }
                    opened = false;
                }
            }
        }
        if (level.equals("Save as .mp3")) {
            TTS tts = new TTS();
            StadiumEffect stadiumEffect = new StadiumEffect();
            stadiumEffect.setParams("amount:0");
            tts.getMarytts().setAudioEffects(stadiumEffect.getFullEffectAsString());
            tts.setVoice("dfki-poppy-hsmm");
            String text = "";
            for (int i = 0; i < checkedMessages.size(); i++) {
                text += checkedMessages.get(i).getName();
            }

            try {
                AudioInputStream audio = tts.getMarytts().generateAudio(text); //generate audio from text
                AudioSystem.write(audio, AudioFileFormat.Type.WAVE, new File("F:\\temp\\" + "filename" + ".wav"));//save audio as .wav to the static location with filename
            } catch (SynthesisException | IOException si) {
                si.printStackTrace();
            }
        }
        if (categories.size() > 0) {
            catBox.setEnabled(categories.size() > 0);
            Component[] shapeCols = {subcatBox, colours, shapes, fonts, sizes, textColours};
            for (Category c : categories)
                if (c.subcategories != null && c.subcategories.size() > 0) {
                    for (Component compo : shapeCols)
                        compo.setEnabled(true);
                    break;
                }

        }
        theApp.repaint();
    }
}