import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class JuliaSet extends JFrame {

    int width = 1024;
    int height = 1024;
    int maxIter = 10000;
    double zoom = 300;

    // Константа c = a + bi (меняя её, получаем разные множества)
    double cx = 0.4;
    double cy = 0.2;

    double centerX = 0.465005;
    double centerY = 0.185;

    // Радиус убегания (оценка R, при котором |z| > R — точка уходит)
    double escapeRadius = 2.0;

    BufferedImage image;

    public JuliaSet() {
        super("Множество Жюлия");
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawJuliaSet();
        setLayout(new BorderLayout());
        add(new JLabel(new ImageIcon(image)), BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void drawJuliaSet() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                // Преобразуем координаты пикселя в комплексную плоскость и берем начальную точку
                double zx = (x - (double) width / 2) / (0.5 * width * zoom) + centerX;
                double zy = (y - (double) height / 2) / (0.5 * height * zoom) + centerY;

                // Проходим по итерациям, проверяем, когда точка убежит
                float iteration = 0;
                while (zx * zx + zy * zy < escapeRadius * escapeRadius && iteration < maxIter) {
                    double tmp = zx * zx - zy * zy + cx; // Реальная часть: x^2 - y^2 + c_x
                    zy = 2.0 * zx * zy + cy; // Мнимая часть: 2*x*y + c_y
                    zx = tmp;
                    iteration++;
                }

                // Цвет точки зависит от количества итераций
                int color = iteration == maxIter ? 0 : Color.HSBtoRGB(iteration / 256f, 1, iteration / (iteration + 8f));
                image.setRGB(x, y, color);
            }
        }
    }

    public static void main(String[] args) {
        new JuliaSet();
    }
}
