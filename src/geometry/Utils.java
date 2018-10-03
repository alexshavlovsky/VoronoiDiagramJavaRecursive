package geometry;

import voronoy.Point2D;

public class Utils {

    private static final double EPS = 10e-9;

    public static boolean cmpE(double d1, double d2) {
        return (Math.abs(d1 - d2) < EPS);
    }

    public static boolean cmpGE(double d1, double d2) {
        return cmpE(d1, d2) || (d1 > d2);
    }

    public static boolean cmpLE(double d1, double d2) {
        return cmpE(d1, d2) || (d1 < d2);
    }

    public static int compareX(double x1, double x2) {
        if (cmpE(x1, x2)) return 0;
        return (x1 < x2) ? -1 : 1;
    }

    public static int compareY(double y1, double y2) {
        if (cmpE(y1, y2)) return 0;
        return (y1 < y2) ? -1 : 1;
    }

    public static int comparePoint(Point2D p1, Point2D p2) {
        int cx = compareX(p1.x, p2.x);
        return cx != 0 ? cx : compareY(p1.y, p2.y);
    }

    public static double getArea(Point2D p1, Point2D p2, Point2D p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }
}
