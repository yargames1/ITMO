import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NicePlot extends JPanel {

    private static class Pt {
        final double x, y;
        Pt(double x, double y) { this.x = x; this.y = y; }
    }

    private final List<Pt> points = new ArrayList<>();

    public NicePlot() {
        points.add(new Pt(0.9, 56));
        points.add(new Pt(0.9, 68));
        points.add(new Pt(0.9, 80));
        points.add(new Pt(1.3, 68));
        points.add(new Pt(1.3, 80));
        points.add(new Pt(1.3, 92));
        points.add(new Pt(1.7, 80));
        points.add(new Pt(1.7, 92));
        points.add(new Pt(1.7, 104));
        points.add(new Pt(2.1, 92));
        points.add(new Pt(2.1, 104));
        points.add(new Pt(2.1, 116));
        points.add(new Pt(2.5, 104));
        points.add(new Pt(2.5, 116));
        points.add(new Pt(2.5, 128));
        points.add(new Pt(2.9, 116));
        points.add(new Pt(2.9, 128));
        points.add(new Pt(2.9, 140));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        int w = getWidth();
        int h = getHeight();
        int margin = 60;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);

        int x0 = margin;
        int y0 = h - margin;
        int x1 = w - margin;
        int y1 = margin;

        // оси
        g2.setColor(Color.BLACK);
        g2.drawLine(x0, y0, x1, y0); // X
        g2.drawLine(x0, y0, x0, y1); // Y

        double minX = 0.0;
        double maxX = 3.2;
        double minY = 0.0;
        double maxY = 160.0;

        double scaleX = (x1 - x0) / (maxX - minX);
        double scaleY = (y0 - y1) / (maxY - minY);

        java.util.function.Function<Double, Integer> toScreenX =
                x -> (int) (x0 + (x - minX) * scaleX);
        java.util.function.Function<Double, Integer> toScreenY =
                y -> (int) (y0 - (y - minY) * scaleY);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // деления X
        for (double xv = 0; xv <= 3.3; xv += 0.2) {
            int sx = toScreenX.apply(xv);
            g2.drawLine(sx, y0, sx, y0 + 5);
            String label = String.format("%.1f", xv);
            int sw = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, sx - sw / 2, y0 + 20);
        }

        // деления Y
        for (int yv = 0; yv <= 160; yv += 10) {
            int sy = toScreenY.apply((double) yv);
            g2.drawLine(x0 - 5, sy, x0, sy);
            String label = Integer.toString(yv);
            int sh = g2.getFontMetrics().getAscent();
            int sw = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, x0 - 10 - sw, sy + sh / 2 - 2);
        }

        // подпись оси X (горизонтально)
        String xLabel = "x";
        int xLabelWidth = g2.getFontMetrics().stringWidth(xLabel);
        g2.drawString(xLabel, (x0 + x1) / 2 - xLabelWidth / 2, h - 5);

        // подпись оси Y
        String yLabel = "y";
        Graphics2D g2y = (Graphics2D) g2.create();
        g2y.rotate(-Math.PI / 2, 15, (y0 + y1) / 2.0);
        int yLabelWidth = g2.getFontMetrics().stringWidth(yLabel);
        g2y.drawString(yLabel, 15 - yLabelWidth / 2, (y0 + y1) / 2);
        g2y.dispose(); // вернул старую систему координат после переворота

        // прямая синим
        double k = 0.865 * (0.555 / 19.01);
        g2.setColor(new Color(0, 120, 220));
        g2.setStroke(new BasicStroke(2f));

        double xLeft = minX;
        double xRight = maxX;
        double yLeft = 99.92 + k * (xLeft - 1.88);
        double yRight = 99.92 + k * (xRight - 1.88);

        g2.drawLine(
                toScreenX.apply(xLeft),  toScreenY.apply(yLeft),
                toScreenX.apply(xRight), toScreenY.apply(yRight)
        );

        // точки красным
        int r = 5;
        g2.setColor(new Color(220, 50, 47));
        for (Pt p : points) {
            int sx = toScreenX.apply(p.x);
            int sy = toScreenY.apply(p.y);
            g2.fillOval(sx - r, sy - r, 2 * r, 2 * r);
        }

        g2.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("График X-Y");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new NicePlot());
            f.setSize(800, 800);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
