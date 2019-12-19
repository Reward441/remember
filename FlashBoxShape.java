package remember;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.awt.geom.*;
import java.util.ArrayList;

import static java.lang.Integer.max;
import static java.lang.Integer.min;


public enum FlashBoxShape {

    // NB: enum instances are implicitly static and final;
    // y-axis increases downwards here.
    Rectangle("R", new Rectangle(0, 0, w(), h())),
    Oval("O", new Ellipse2D.Double(0, 0, w(), h())),
    Trapezium("T", new Polygon(new int[]{0, qw(), tqw(), w()},
            new int[]{h(), 0, 0, h()}, 4)),
    Pentagon("P", new Polygon(new int[]{0, 0, hw(), w(), w()},
            new int[]{h(), hh(), 0, hh(), h(),}, 5)),
    Hexagon("HX", new Polygon(new int[]{0, 0, qw(), tqw(), w(), w()},
            new int[]{h(), hh(), 0, 0, hh(), h()}, 6)),
    Heptagon("HP", new Polygon(new int[]{0, 0, hw(), w(), w(), tqw(), qw()},
            new int[]{tqh(), hh(), 0, hh(), tqh(), h(), h()}, 7)),
    Octagon("O", new Polygon(new int[]{0, 0, qw(), tqw(), w(), w(), tqw(), qw()},
            new int[]{tqh(), hh(), qh(), qh(), hh(), tqh(), h(), h()}, 8)),
    ZigRect("ZR", new Polygon(new int[]{0, 0, qw(), tqw(), w(), w(), tqw(), qw()},
            new int[]{tqh(), hh(), 10, 10, hh(), tqh(), h(), h()}, 8)),
    ZigRectLeft("ZRL", new Polygon(new int[]{0, qw(), 0, qw(), 0, qw(), 0, qw(), w(), w(), qw(),},
            new int[]{70, 60, 50, 40, 30, 20, 10, 0, 0, h(), h()}, 11)),
    ZigRectTop("ZRT", new Polygon(new int[]{0, 0, w() / 8, qw(), 3 * ew(), hw(), 5 * ew(), tqw(), w() * 7 / 8, w(), w(),},
            new int[]{h(), 20, 0, 20, 0, 20, 0, 20, 0, 20, h()}, 11)),
    ZigRectRight("ZRR", new Polygon(new int[]{0, 0, tqw(), w(), tqw(), w(), tqw(), w(), tqw(), w(), tqw()},
            new int[]{h(), 0, 0, 10, 20, 30, 40, 50, 60, 70, h()}, 11)),
    ZigRectBottom("ZRB", new Polygon(new int[]{0, 0, w(), w(), 7 * ew(), tqw(), 5 * ew(), hw(), 3 * ew(), qw(), ew(),},
            new int[]{60, 0, 0, 60, h(), 60, h(), 60, h(), 60, h()}, 11)),
    ZigRectLeftTop("ZRLT", zigzag(coords("lt", "x"), coords("lt", "y"))),
    ZigRectLeftRight("ZRLR", zigzag(coords("lr", "x"), coords("lr", "y"))),
    ZigRectLeftBottom("ZRLB", zigzag(coords("lb", "x"), coords("lb", "y"))),
    ZigRectTopRight("ZRTR", zigzag(coords("tr", "x"), coords("tr", "y"))),
    ZigRectTopBottom("ZRTB", zigzag(coords("tb", "x"), coords("tb", "y"))),
    ZigRectRightBottom("ZRRB", zigzag(coords("rb", "x"), coords("rb", "y"))),
    ZigRectLeftTopRight("ZRLTR", zigzag(coords("ltr", "x"), coords("ltr", "y"))),
    ZigRectLeftTopBottom("ZRLTB", zigzag(coords("ltb", "x"), coords("ltb", "y"))),
    ZigRectTopRightBottom("ZRTRB", zigzag(coords("trb", "x"), coords("trb", "y")));

    public String getSymbol() {
        return symbol;
    }

    public Shape getShape() {
        return shape;
    }

    public final String symbol;
    public final Shape shape;

    FlashBoxShape(String symbol, Shape shape) {
        this.symbol = symbol;
        this.shape = shape;
    }

    public static List<Double> coords(String zigzagSides, String axis) {
        Double w = (double) w();
        Double qw = (double) qw();
        Double tqw = (double) tqw();
        Double h = (double) h();
        Double qh = (double) qh();
        Double tqh = (double) tqh();
        List<Double> xpoints = new ArrayList<>();
        List<Double> ypoints = new ArrayList<>();
        switch (zigzagSides) {
            case "lt":
                xpoints = new ArrayList<>() {{
                    addAll(dup(0.0, qw, 4));
                    addAll(squeezeRange(1, 7, "sw_qw"));
                    addAll(Arrays.asList(w, w, qw));
                }};
                ypoints = new ArrayList<>() {{
                    addAll(squeezeRange(7, 1, "sh_qh"));
                    addAll(dup(qh, 0.0, 4));
                    addAll(Arrays.asList(qh, h, h));
                }};
                break;
            case "lr":
                xpoints = new ArrayList<>() {{
                    addAll(dup(0.0, qw, 4));
                    addAll(dup(tqw, w, 4));
                    addAll(Arrays.asList(tqw, qw));
                }};
                ypoints = new ArrayList<>() {{
                    addAll(squeezeRange(7, 1, "eh"));
                    addAll(Arrays.asList(0.0, 0.0));
                    addAll(squeezeRange(1, 7, "eh"));
                    addAll(Arrays.asList(h, h));
                }};
                break;
            case "lb":
                xpoints = new ArrayList<>() {{
                    addAll(dup(0.0, qw, 4));
                    addAll(Arrays.asList(w, w));
                    addAll(squeezeRange(7, 1, "sw_qw"));
                    add(qw);
                }};
                ypoints = new ArrayList<>() {{
                    addAll(squeezeRange(7, 1, "sh"));
                    addAll(Arrays.asList(0.0, 0.0, tqh));
                    addAll(dup(h, tqh, 4));
                }};
                break;
            case "tr":
                xpoints = new ArrayList<>() {{
                    addAll(Arrays.asList(0.0, 0.0));
                    addAll(squeezeRange(1, 7, "sw"));
                    addAll(dup(tqw, w, 4));
                    add(tqw);
                }};
                ypoints = new ArrayList<>() {{
                    add(h);
                    addAll(dup(qh, 0.0, 4));
                    add(qh);
                    addAll(squeezeRange(1, 7, "sh_qh"));
                    add(h);
                }};
                break;
            case "tb":
                xpoints = new ArrayList<>() {{
                    addAll(Arrays.asList(0.0, 0.0));
                    addAll(squeezeRange(1, 7, "ew"));
                    addAll(Arrays.asList(w, w));
                    addAll(squeezeRange(7, 1, "ew"));
                }};
                ypoints = new ArrayList<>() {{
                    add(tqh);
                    addAll(dup(qh, 0.0, 4));
                    add(qh);
                    addAll(dup(tqh, h, 4));
                }};
                break;
            case "rb":
                xpoints = new ArrayList<>() {{
                    addAll(Arrays.asList(0.0, 0.0));
                    addAll(dup(tqw, w, 4));
                    add(tqw);
                    addAll(squeezeRange(7, 1, "sw"));
                }};
                ypoints = new ArrayList<>() {{
                    addAll(Arrays.asList(tqh, 0.0,0.0));
                    addAll(squeezeRange(1, 7, "sh"));
                    addAll(dup(tqh, h, 4));
                }};
                break;
            case "ltr":
                xpoints = new ArrayList<>() {{
                    addAll(dup(0.0, qw, 4));
                    addAll(squeezeRange(1, 7, "sw_qw88"));
                    addAll(dup(tqw, w, 4));
                    addAll(Arrays.asList(tqw, qw));
                }};
                ypoints = new ArrayList<>() {{
                    addAll(squeezeRange(7, 1, "sh_qh"));
                    addAll(dup(qh, 0.0, 4));
                    add(qh);
                    addAll(squeezeRange(1, 7, "sh_qh"));
                    addAll(Arrays.asList(h, h));
                }};
                break;
            case "ltb":
                xpoints = new ArrayList<>() {{
                    addAll(dup(0.0, qw, 4));
                    addAll(squeezeRange(1, 7, "sw_qw"));
                    addAll(Arrays.asList(w,w));
                    addAll(squeezeRange(7, 1, "sw_qw"));
                    add(qw);
                }};
                ypoints = new ArrayList<>() {{
                    addAll(squeezeRange(7, 1, "sh_qh88"));
                    addAll(dup(qh, 0.0, 4));
                    add(qh);
                    addAll(dup(tqh, h, 4));
                    add(tqh);
                }};
                break;
            case "trb":
                xpoints = new ArrayList<>() {{
                    addAll(Arrays.asList(0.0,0.0));
                    addAll(squeezeRange(1, 7, "sw"));
                    addAll(dup(tqw, w, 4));
                    add(tqw);
                    addAll(squeezeRange(7, 1, "sw"));
                }};
                ypoints = new ArrayList<>() {{
                    add(tqh);
                    addAll(dup(qh, 0.0, 4));
                    add(tqh);
                    addAll(squeezeRange(1, 7, "sh_qh88"));
                    addAll(dup(tqh, h, 4));
                }};
                break;
        }
        if (axis.equals("x")) return xpoints;
        if (axis.equals("y")) return ypoints;
        else throw new RuntimeException("Invalid axis argument.");
    }

    public static List<Double> dup(Double first, Double second, int quantity) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            result.add(first);
            result.add(second);
        }
        return result;
    }

    public static List<Double> squeezeRange(int firstMultiplier, int secondMultiplier, String squeezeType) {
        int maxi = max(firstMultiplier, secondMultiplier), mini = min(firstMultiplier, secondMultiplier);
        List<Double> result = new ArrayList<>();
        for (int i = mini; i <= maxi; i++)
            switch (squeezeType) {
                case "sw":
                    result.add(i * w() * 3.0 / 32);
                    break;
                case "sw_qw":
                    result.add(i * w() * 3.0 / 32 + qw());
                    break;
                case "sh":
                    result.add(i * h() * 3.0 / 32);
                    break;
                case "sh_qh":
                    result.add(i * h() * 3.0 / 32 + qh());
                    break;
                case "sw_qw88":
                    result.add(i * w() * 1.0 / 16 + qw());
                    break;
                case "sh_qh88":
                    result.add(i * h() * 1.0 / 16 + qh());
                    break;
                case "ew":
                    result.add(i*w() / 8.0);
                    break;
                case "eh":
                    result.add(i*h() / 8.0);
                    break;
            }
        if (firstMultiplier > secondMultiplier) Collections.reverse(result);
        return result;
    }

    /* Must use methods for width (w()) and height (h()) instead of fields
     * to prevent illegal forward reference error.
     * q denotes quarter, tq third quarter, h half (if succeeded by another letter), and s squeeze.
     */
    public static int w() {
        // This must be divisible by eight, else rounding would occur
        // during computation of qw() and ew().
        return 584;
    }

    public static int qw() {
        return w() / 4;
    }

    public static int hw() {
        return w() / 2;
    }

    public static int tqw() {
        return w() * 3 / 4;
    }

    public static int ew() {
        return w() / 8;
    }

    public static int h() {
        return 80;
    }

    public static int qh() {
        return h() / 4;
    }

    public static int hh() {
        return h() / 2;
    }

    public static int tqh() {
        return h() * 3 / 4;
    }

    public static Shape  zigzag(List<Double> xpoints, List<Double> ypoints) {
        Path2D path = new Path2D.Double();
        // Top-bottom.
//        double [] tbxp={};
//        double [] tbyp={};
        path.moveTo(xpoints.get(0), ypoints.get(0));
        for (int i = 1; i < xpoints.size(); i++) {
            path.lineTo(xpoints.get(i), ypoints.get(i));
        }
        return path;
    }
}