package lab1.graphics;

import java.lang.Math;

public class Triangle extends Figure implements RoundAboutAvailable {
    private final Dot dot1;
    private final Dot dot2;
    private final Dot dot3;

    public Triangle(Dot dot1, Dot dot2, Dot dot3) {
        super(() -> (Math.abs((dot2.getX() - dot1.getX()) * (dot3.getY() - dot1.getY()) -
                (dot3.getX() - dot1.getX()) * (dot2.getY() - dot1.getY()))) * 0.5);
        check(dot1, dot2, dot3);
        this.dot1 = dot1;
        this.dot2 = dot2;
        this.dot3 = dot3;
    }

    private void check(Dot d1, Dot d2, Dot d3) {
        Dot a = d2.minus(d1);
        Dot b = d2.minus(d3);
        if (isEqual(b.getX() * a.getY() - b.getY() * a.getX(), 0))
            throw new RuntimeException("Это не треугольник. Пожалуйста задайте другие координаты");
    }

    private boolean isEqual(double a, double b) {
        return Math.abs(a - b) < 1e-5;
    }

    @Override
    public Triangle move(double delta_x, double delta_y) {
        return new Triangle(dot1.move(delta_x, delta_y), dot2.move(delta_x, delta_y), dot3.move(delta_x, delta_y));
    }

    public Ellipse getRoundAbout() {
        // (x1, y1) (x2, y2) (x3, y3) for x^2 + y^2 + Ax + By + C
        double x1 = dot1.getX(), y1 = dot1.getY(), x2 = dot2.getX(), y2 = dot2.getY(),
                x3 = dot3.getX(), y3 = dot3.getY();
        double v = (x2 - x1) * y3 + (x1 - x3) * y2 + (x3 - x2) * y1;
        double A = ((y2 - y1) * y3 * y3 + (-y2 * y2 + y1 * y1 - x2 * x2 + x1 * x1) * y3 + y1 * y2 * y2 + (-y1 * y1 + x3 * x3 - x1 * x1) * y2 + (x2 * x2 - x3 * x3) * y1) / v;
        double B = -((x2 - x1) * y3 * y3 + (x1 - x3) * y2 * y2 + (x3 - x2) * y1 * y1 + (x2 - x1) * x3 * x3 + (x1 * x1 - x2 * x2) * x3 + x1 * x2 * x2 - x1 * x1 * x2) / v;
        double center_x = -A * 0.5;
        double center_y = -B * 0.5;
        double radius = Math.sqrt(Math.pow(x1 - center_x, 2) + Math.pow(y1 - center_y, 2));
        return new Ellipse(new Dot(center_x, center_y), radius, radius, 0);
    }

    @Override
    public Triangle expandTo(double multiplier) {
        Dot mass_center = dot1.plus(dot2).plus(dot3).product(1d / 3d);
        return new Triangle(dot1.minus(mass_center).product(multiplier), dot2.minus(mass_center).product(multiplier),
                dot3.minus(mass_center).product(multiplier));
    }

    @Override
    public String toString() {
        return String.format("""
                Треугольник.
                Координаты точек: %s %s %s
                Площадь: %f
                                
                """, dot1, dot2, dot3, getSquare());
    }

    @Override
    public void draw() {
    }
}
