package voronoy;

public class Point2D {
    public double x, y;

    @Override
    public String toString() {
        return "(" + +x + ", " + y + ')';
    }

    public Point2D(double x0, double y0) {
        x = x0;
        y = y0;
    }

}
