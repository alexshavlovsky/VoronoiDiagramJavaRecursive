package core.geometry;

import core.voronoi.Edge;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private Utils() {
        throw new AssertionError();
    }

    private static final double INFINITY = 10e6;
    private static final double EPS = 10e-9;
    private static final double INTERSECTION_EPS = 10e-2;

    private static boolean cmpZ(double d) {
        return (Math.abs(d) < EPS);
    }

    public static boolean cmpGZ(double d) {
        return d > EPS;
    }

    public static boolean cmpLZ(double d) {
        return d < -EPS;
    }

    private static int compareX(double x1, double x2) {
        if (Math.abs(x1 - x2) < EPS) return 0;
        return (x1 < x2) ? -1 : 1;
    }

    private static int compareY(double y1, double y2) {
        if (Math.abs(y1 - y2) < EPS) return 0;
        return (y1 < y2) ? -1 : 1;
    }

    public static int comparePointXY(Point p1, Point p2) {
        int cx = compareX(p1.x, p2.x);
        return cx != 0 ? cx : compareY(p1.y, p2.y);
    }

    public static double getArea(Point p1, Point p2, Point p3) {
        return cross(p1.x - p3.x, p1.y - p3.y, p2.x - p3.x, p2.y - p3.y);
    }

    public static Point getMidPoint(Point p1, Point p2) {
        return new Point((p1.x + p2.x) / 2.0, (p1.y + p2.y) / 2.0);
    }

    public static Point getLinesIntersectionPoint(LineCommon l1, LineCommon l2) {
        double den = cross(l1.A, l1.B, l2.A, l2.B);
        return cmpZ(den) ? null : new Point(cross(l1.B, l2.B, l1.C, l2.C) / den, -cross(l1.A, l2.A, l1.C, l2.C) / den);
    }

    private static Point translatePoint(Point p, double dx, double dy) {
        return new Point(p.x + dx, p.y + dy);
    }

    public static Point transformPointToPoint(Point p, Point p0, double k) {
        return translatePoint(p0, (p.x - p0.x) * k, (p.y - p0.y) * k);
    }

    public static Point[] getInfiniteOrigins(Point p, Edge e) {
        double dx = e.B * INFINITY;
        double dy = -e.A * INFINITY;
        return new Point[]{translatePoint(p, dx, dy), translatePoint(p, -dx, -dy)};
    }

    private static double cross(double a, double b, double c, double d) {
        return a * d - b * c;
    }

    private static boolean lineSegmentIntersectsLine(Line2D a, Line2D b) {
        double dx = a.p2.x - a.p1.x;
        double dy = a.p2.y - a.p1.y;
        double cd1 = cross(dx, dy, b.p1.x - a.p1.x, b.p1.y - a.p1.y);
        double cd2 = cross(dx, dy, b.p2.x - a.p1.x, b.p2.y - a.p1.y);
        return Math.abs(cd1) < INTERSECTION_EPS || Math.abs(cd2) < INTERSECTION_EPS || (cd1 < 0 ^ cd2 < 0);
    }

    public static boolean doLinesIntersect(Point p1, Point p2, Point p3, Point p4) {
        Line2D a = new Line2D(p1, p2);
        Line2D b = new Line2D(p3, p4);
        return lineSegmentIntersectsLine(a, b) && lineSegmentIntersectsLine(b, a);
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

    static public List<Point> zoomPoints(List<Point> list, double z) {
        return list.stream().map(p -> new Point(p.x * z, p.y * z)).collect(Collectors.toList());
    }

}
