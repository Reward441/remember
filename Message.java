package remember;

import java.awt.*;
import java.util.ArrayList;

import static remember.JListNest.currentCatID;

public class Message extends Layer {

    public Message(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
