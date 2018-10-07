package voronoy;

import geometry.Line2D;
import geometry.LineCommon;
import geometry.Point2D;

import static geometry.Utils.LineCircleIntersect;
import static geometry.Utils.getMidPoint;

class Edge extends LineCommon {

    Point2D p1, p2;
    Node n1, n2;

    Edge(Point2D p1, Point2D p2) {
        super(p1.x - p2.x, p1.y - p2.y);
        this.p1 = p1;
        this.p2 = p2;
        Point2D mp = getMidPoint(p1, p2);
        double n = Math.sqrt(A * A + B * B);
        A = A / n;
        B = B / n;
        C = -(mp.x * A + mp.y * B);
    }

    Point2D getOpposite(Point2D p) {
        return p1 == p ? p2 : p1;
    }

    Line2D toLine2D(double r0) {
        return toLine2D(getMidPoint(p1, p2), r0);
    }

    Line2D toLine2D(Point2D p0, double r0) {
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
