package geometry;

import voronoy.Line2D;
import voronoy.LineCommon;
import voronoy.Point2D;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static int comparePointXY(Point2D p1, Point2D p2) {
        int cx = compareX(p1.x, p2.x);
        return cx != 0 ? cx : compareY(p1.y, p2.y);
    }

    public static int comparePointYX(Point2D p1, Point2D p2) {
        int cy = compareY(p1.y, p2.y);
        return cy != 0 ? cy : compareX(p1.x, p2.x);
    }

    public static double getArea(Point2D p1, Point2D p2, Point2D p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public static Point2D getCircle(Point2D p1, Point2D p2, Point2D p3) {
        double x12 = p1.x - p2.x, x31 = p3.x - p1.x, x23 = p2.x - p3.x, xm = x31 * x12 * x23;
        double y12 = p1.y - p2.y, y31 = p3.y - p1.y, y23 = p2.y - p3.y, ym = y31 * y12 * y23;
        y12 = y12 * p3.x;
        y31 = y31 * p2.x;
        y23 = y23 * p1.x;
        double d = 2.0 * (y12 + y31 + y23);
        return new Point2D((y12 * p3.x + y31 * p2.x + y23 * p1.x - ym) / d,
                (xm - x12 * p3.y * p3.y - x31 * p2.y * p2.y - x23 * p1.y * p1.y) / d);
    }

    public static double getPolarSqr(Point2D p0, Point2D p) {
        double dx = p.x - p0.x;
        double dy = p.y - p0.y;
        return dx / Math.sqrt(dx * dx + dy * dy);
    }

    public static List<Point2D> getSortedListFrom2DArray(double[][] a) {
        return Arrays.stream(a).map(e -> new Point2D(e[0], e[1])).sorted(Utils::comparePointXY).collect(Collectors.toList());
    }

    public static Point2D getMidPoint(Point2D p1, Point2D p2) {
        return new Point2D((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    public static Point2D LinesIntersect(LineCommon l1, LineCommon l2) {
        double den = l1.A * l2.B - l1.B * l2.A;
        return new Point2D((-l2.B * l1.C + l1.B * l2.C) / den, (l2.A * l1.C - l1.A * l2.C) / den);
    }

    public static double getDistToPoint(LineCommon l1, Point2D p) {
        return l1.A * p.x + l1.B * p.y + l1.C / Math.sqrt(l1.A * l1.A + l1.B * l1.B);
    }

    public static Line2D LineCircleIntersect(LineCommon line, Point2D c0, double r0) {
        double T = line.C + c0.x * line.A + c0.y * line.B;
        double AB2 = line.A * line.A + line.B * line.B;
        double d2 = r0 * r0 - T * T / AB2;
        double m = Math.sqrt(d2 / AB2);
        double x0 = c0.x - line.A * T / AB2;
        double y0 = c0.y - line.B * T / AB2;
        return new Line2D(
                new Point2D(x0 - line.B * m, y0 + line.A * m),
                new Point2D(x0 + line.B * m, y0 - line.A * m));
    }

}