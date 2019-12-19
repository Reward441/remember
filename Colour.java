package remember;

public enum Colour {
    // rgb = 65536 * r + 256 * g + b.
    Red(16711680), Darkred(9109504), Coral(16744272), Darkorange(16747520),
    Lemonchiffon(16775885), Yellow(16776960),Palegreen(10025880), Chartreuse(8388352),
    Green(32768), Skyblue(8900331), Blue(255), Indigo(4915330),
    Burlywood(14596231), Saddlebrown(9127187), Silver(12632256), Dimgrey(6908265);

    public final int rgb;

    Colour(int rgb) {
        this.rgb = rgb;
    }
}