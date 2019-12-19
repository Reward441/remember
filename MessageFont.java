package remember;

import java.awt.*;

public enum MessageFont {

    Algerian("A",fontType("Algerian")), Broadway("B",fontType("Broadway")),
    Consolas("CO",fontType("Consolas")),Elephant("E",fontType("Elephant")),
    Gabriola("G",fontType("Gabriola")), Garamond("GA",fontType("Garamond")),
    Gigi("GI",fontType("Gigi")),Jokerman("J",fontType("Jokerman")),
    JuiceITC("JU",fontType("JuiceITC")),MSGothic("M",fontType("MSGothic")),
    Onyx("O",fontType("Onyx")),OpenSans("OP",fontType("OpenSans")),
    Perpetua("P",fontType("Perpetua")),Pristina("PR",fontType("Pristina")),
    Rockwell("R",fontType("Rockwell")),Webdings("W",fontType("Webdings"));

    public final String symbol;
    public final Font font;

    MessageFont(String symbol, Font font) {
        this.symbol = symbol;
        this.font=font;
    }

    public static Font fontType(String fontName){
        return new Font(fontName,Font.BOLD,12);
    }
}