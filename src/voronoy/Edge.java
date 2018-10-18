package voronoy;

import geometry.Line2D;
import geometry.LineCommon;
import geometry.Point;

import static geometry.Utils.LineCircleIntersect;
import static geometry.Utils.getMidPoint;

public class Edge extends LineCommon {

    Point p1, p2;
    Node n1, n2;

    Edge(Point p1, Point p2) {
        super(p1.x - p2.x, p1.y - p2.y);
        this.p1 = p1;
        this.p2 = p2;
        Point mp = getMidPoint(p1, p2);
        double n = Math.sqrt(A * A + B * B);
        A = A / n;
        B = B / n;
        C = -(mp.x * A + mp.y * B);
    }

    Point getOpposite(Point p) {
        return p1 == p ? p2 : p1;
    }

    public boolean isBasePoint(Point p) {
        return p1 == p;
    }

    Line2D toLine2D(double r0) {
        return toLine2D(getMidPoint(p1, p2), r0);
    }

    Line2D toLine2D(Point p0, double r0) {
        return LineCircleIntersect(this, p0, r0);
    }


    @Override
    public String toString() {
        return "Edge{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", n1=" + n1 +
                ", n2=" + n2 +
                '}';
    }
}
