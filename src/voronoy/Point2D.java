package voronoy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    static List<Point2D> getSortedListFrom2DArray(double[][] a) {
        return Arrays.stream(a).map(e -> new Point2D(e[0], e[1])).
                sorted((p1, p2) -> p1.x != p2.x ? Double.compare(p1.x, p2.x) : Double.compare(p1.y, p2.y)).
                collect(Collectors.toList());
    }

    static Point2D getMidPoint(Point2D p1, Point2D p2) {
        return new Point2D((p1.x + p2.x)/2, (p1.y + p2.y)/2);
    }
}
