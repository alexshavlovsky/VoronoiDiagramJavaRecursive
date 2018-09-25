package voronoy;

class LineCommon {
    double A, B, C;

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

    static Point2D LinesIntersect(LineCommon l1, LineCommon l2) {
        double den = l1.A * l2.B - l1.B * l2.A;
        return new Point2D((-l2.B * l1.C + l1.B * l2.C) / den, (l2.A * l1.C - l1.A * l2.C) / den);
    }

    static double getDistToPoint(LineCommon l1, Point2D p) {
        return l1.A * p.x + l1.B * p.y + l1.C / Math.sqrt(l1.A * l1.A + l1.B * l1.B);
    }

    Line2D LineCircleIntersect(Point2D c0, double r0) {
        double T = C + c0.x * A + c0.y * B;
        double AB2 = A * A + B * B;
        double d2 = r0 * r0 - T * T / AB2;
        double m = Math.sqrt(d2 / AB2);
        double x0 = c0.x - A * T / AB2;
        double y0 = c0.y - B * T / AB2;
        return new Line2D(
                new Point2D(x0 - B * m, y0 + A * m),
                new Point2D(x0 + B * m, y0 - A * m));
    }

    Point2D getIntersectionPoint(LineCommon l2) {
        return LinesIntersect(this, l2);
    }

}
