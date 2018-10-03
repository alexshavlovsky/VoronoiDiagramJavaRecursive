package voronoy;

import static geometry.Utils.LinesIntersect;

public class LineCommon {
    public double A, B, C;

    LineCommon(Point2D p1, Point2D p2) {
        double dx = p2.x - p1.x;
        double dy = p1.y - p2.y;
        double n = Math.sqrt(dx * dx + dy * dy);
        this.A = dy / n;
        this.B = dx / n;
        this.C = (p1.x * p2.y - p2.x * p1.y) / n;
    }

    LineCommon(Point2D p1, Point2D p2, boolean edge) {
        double mx = (p1.x + p2.x) / 2;
        double my = (p1.y + p2.y) / 2;
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double n = Math.sqrt(dx * dx + dy * dy);
        this.A = dx / n;
        this.B = dy / n;
        this.C = -(mx * A + my * B);
    }

    LineCommon(Line2D l) {
        this(l.p1,l.p2);
    }

    LineCommon(double a, double b, double c) {
        A = a;
        B = b;
        C = c;
    }

    @Override
    public String toString() {
        return "{" +
                "A=" + A +
                ", B=" + B +
                ", C=" + C +
                '}';
    }

    LineCommon translate(double dx, double dy) {
        return new LineCommon(A, B, C - dx * A - dy * B);
    }

    LineCommon getOrthogonal(Point2D p0) {
        return new LineCommon(-B, A, p0.x * B - p0.y * A);
    }

    LineCommon getParallel(Point2D p0) {
        return new LineCommon(A, B, -p0.x * A - p0.y * B);
    }

    Point2D getIntersectionPoint(LineCommon l2) {
        return LinesIntersect(this, l2);
    }

}
