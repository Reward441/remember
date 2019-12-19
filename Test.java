package remember;

import java.awt.*;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Test {

    public static void main(String[] args) {
        GraphicsEnvironment envmt = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice device = envmt.getDefaultScreenDevice();

        if (!device
                .isWindowTranslucencySupported(WindowTranslucency.PERPIXEL_TRANSLUCENT)) {
            System.out
                    .println("Shaped windows are not supported on your system.");
            System.exit(0);
        }

        ApplicationWindow window = new ApplicationWindow();
        window.setVisible(true);
    }
}

class ApplicationWindow extends JFrame {
    public ApplicationWindow() {
        this.setSize(100, 40);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(new JButton("asdf"));
        final Polygon myShape = getPolygon();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(myShape);
                ((JFrame) e.getSource()).setForeground(Color.red);
                ((JFrame) e.getSource()).setBackground(Color.red);
            }
        });

        this.setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
//        int w = this.getSize().width;
//        int h = this.getSize().height;
//        int x = (dim.width-w)/2;
//        int y = (dim.height-h)/2;

        // in the order 1,2,4 and 3 (of precedence top-left-right-bottom)
//        this.setLocation(x, y);
//        setShape(new Polygon(new int[]{240*2,300*2,320*2,220*2},new int[]{120*2,120*2,160*2,160*2},4));
//        setShape(myShape);
    }
    private Polygon getPolygon() {
//        int x1Points[] = { 0, 20, 100, 200, 330, 300, 200, 100 };
//        int y1Points[] = { 50, 100, 130, 130, 100, 50, 10, 10 };
        int x1Points[] = {20,80,100,0};
        int y1Points[] = {0,0,40,40};
        Polygon polygon = new Polygon();
        for (int i = 0; i < y1Points.length; i++) {
            polygon.addPoint(x1Points[i], y1Points[i]);
        }
        return polygon;
    }
}
