import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NewtonBasins extends JFrame {

    int width = 1024;
    int height = 1024;
    int maxIter = 100;
    double zoom = 1  ;
    // Если точка приблизится к одному из корней меньше чем на это число, считаем, что оно стремится к этому корню
    double tolerance = 1e-6;
    // Центр изображения (можно сдвигать)
    double centerX = 0;
    double centerY = 0;

    BufferedImage image;

    public NewtonBasins() {
        super("Бассейны Ньютона (f(z) = z³ - 1)");
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawNewtonBasins();
        setLayout(new BorderLayout());
        add(new JLabel(new ImageIcon(image)), BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void drawNewtonBasins() {

        Complex[] roots = {
                new Complex(1, 0),
                new Complex(-0.5, Math.sqrt(3) / 2),
                new Complex(-0.5, -Math.sqrt(3) / 2)
        };

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                // Преобразуем координаты пикселя в комплексную плоскость
                double zx = (x - (double) width / 2) / (0.5 * width * zoom) + centerX;
                double zy = (y - (double) height / 2) / (0.5 * height * zoom) + centerY;
                Complex z = new Complex(zx, zy);

                // Проходим по итерациям, смотрим, к какому из корней приближается точка
                int iteration = 0;
                int rootIndex = -1;
                boolean converged = false;

                while (iteration < maxIter) {
                    Complex f = z.pow(3).minus(new Complex(1, 0));
                    Complex df = z.pow(2).times(new Complex(3, 0));
                    if (df.abs() == 0) break;
                    // Считаем точку в комплексных числах
                    z = z.minus(f.divide(df));
                    // проверяем на стремление к корню
                    for (int i = 0; i < roots.length; i++) {
                        if (z.minus(roots[i]).abs() < tolerance) {
                            converged = true;
                            rootIndex = i;
                            break;
                        }
                    }
                    if (converged) break;
                    iteration++;
                }
                // Красим точку в зависимости от корня и итерации
                int color;
                if (converged) {
                    float hue = (rootIndex * 0.33f + iteration / (float) maxIter / 3f) % 1.0f;
                    color = Color.HSBtoRGB(hue, 1f, 1f - iteration / (float) maxIter);
                } else {
                    color = Color.BLACK.getRGB();
                }

                image.setRGB(x, y, color);
            }
        }
    }

    public static void main(String[] args) {
        new NewtonBasins();
    }
}