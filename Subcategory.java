package remember;

import java.awt.*;
import java.util.List;

import static remember.FlashBoxShape.*;

public class Subcategory extends Layer {

    int catID;
    List<Message> messages;
    Shape shape = Rectangle.shape;
    Color col;
    Color textCol;
    int size;
    Font font;

    public Color getTextCol() {
        return textCol;
    }

    public void setTextCol(Color textCol) {
        this.textCol = textCol;
    }

    public Subcategory(String name, int id, int catID, List<Message> messages) {
        this.name = name;
        this.id = id;
        this.catID = catID;
        col = new Color(Colour.Red.rgb);
        textCol = new Color(Colour.Red.rgb);
        font=MessageFont.Perpetua.font;
        this.messages=messages;
    }

    public int getCatID() {
        return catID;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Shape getShape() {
        return shape;
    }


    public void setShape(Shape shape) {
        this.shape = shape;
    }
    public Color getCol() {
        return col;
    }

    public void setCol(Color col) {
        this.col = col;
    }
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }


}
