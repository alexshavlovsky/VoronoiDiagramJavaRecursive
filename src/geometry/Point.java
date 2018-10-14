package geometry;

public class Point {

    public double x, y;

    public Point(double x0, double y0) {
        x = x0;
        y = y0;
    }

    @Override
    public String toString() {
        return "(" + (int)x + ", " + (int)y + ')';
    }

}
