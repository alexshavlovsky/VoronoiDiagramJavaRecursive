package geometry;

import gui.Canvas;
import voronoi.Edge;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static Canvas paper = new Canvas(900, 600, 50);

    private Utils() {
        throw new AssertionError();
    }

    private static final double EPS = 10e-6;

    public static boolean cmpE(double d1, double d2) {
        return (Math.abs(d1 - d2) < EPS);
    }

    public static boolean cmpGE(double d1, double d2) {
        return cmpE(d1, d2) || (d1 > d2);
    }

    public static boolean cmpLE(double d1, double d2) {
        return cmpE(d1, d2) || (d1 < d2);
    }

    private static int compareX(double x1, double x2) {
        if (cmpE(x1, x2)) return 0;
        return (x1 < x2) ? -1 : 1;
    }

    private static int compareY(double y1, double y2) {
        if (cmpE(y1, y2)) return 0;
        return (y1 < y2) ? -1 : 1;
    }

    public static int comparePointXY(Point p1, Point p2) {
        int cx = compareX(p1.x, p2.x);
        return cx != 0 ? cx : compareY(p1.y, p2.y);
    }

    public static double getArea(Point p1, Point p2, Point p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public static Point getMidPoint(Point p1, Point p2) {
        return new Point((p1.x + p2.x) / 2.0, (p1.y + p2.y) / 2.0);
    }

    public static Point getLinesIntersectionPoint(LineCommon l1, LineCommon l2) {
        double den = l1.A * l2.B - l1.B * l2.A;
        return new Point((-l2.B * l1.C + l1.B * l2.C) / den, (l2.A * l1.C - l1.A * l2.C) / den);
    }

    private static Point translatePoint(Point p, double dx, double dy) {
        return new Point(p.x + dx, p.y + dy);
    }

    public static Point shiftPointToPoint(Point p, Point p0, double k) {
        double dx = p.x - p0.x;
        double dy = p.y - p0.y;
        return translatePoint(p0, dx * k, dy * k);
    }

    public static Point getInfiniteOrigin(Point p, Edge e) {
        return translatePoint(p, e.B / EPS, -e.A / EPS);
    }

    public static Point getNegInfiniteOrigin(Point p, Edge e) {
        return translatePoint(p, -e.B / EPS, e.A / EPS);
    }

    private static Point rotatePoint(Point p, Point p0, double a) {
        return new Point(
                p0.x + (p.x - p0.x) * Math.cos(a) - (p.y - p0.y) * Math.sin(a),
                p0.y + (p.y - p0.y) * Math.cos(a) + (p.x - p0.x) * Math.sin(a)
        );
    }

    static public List<Point> rotatePoints(List<Point> list, Point p0, double a) {
        return list.stream().map(p -> rotatePoint(p, p0, a)).collect(Collectors.toList());
    }

    // line segments intersection
    private static Line2D getBox(Point p1, Point p2) {
        return new Line2D(
                new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y)),
                new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y)));
    }

    private static boolean isBoxesIntersect(Point p1, Point p2, Point p3, Point p4) {
        Line2D a = getBox(p1, p2);
        Line2D b = getBox(p3, p4);
        return a.p1.x <= b.p2.x && a.p2.x >= b.p1.x && a.p1.y <= b.p2.y && a.p2.y >= b.p1.y;
    }

    public static double crossDen(double a, double b, double c, double d) {
        return a * d - b * c;
    }

    private static boolean lineSegmentIntersectsLine(Line2D a, Line2D b) {
        double dx = a.p2.x - a.p1.x;
        double dy = a.p2.y - a.p1.y;
        double cd1 = crossDen(dx, dy, b.p1.x - a.p1.x, b.p1.y - a.p1.y);
        double cd2 = crossDen(dx, dy, b.p2.x - a.p1.x, b.p2.y - a.p1.y);
        return cmpE(cd1, 0) || cmpE(cd2, 0) || (cd1 < EPS ^ cd2 < EPS);
    }

    public static boolean doLinesIntersect(Point p1, Point p2, Point p3, Point p4) {
        Line2D a = new Line2D(p1, p2);
        Line2D b = new Line2D(p3, p4);
        return isBoxesIntersect(p1, p2, p3, p4) && lineSegmentIntersectsLine(a, b) && lineSegmentIntersectsLine(b, a);
    }

}