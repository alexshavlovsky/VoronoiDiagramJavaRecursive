package voronoy;

import geometry.Line2D;
import geometry.LineCommon;
import geometry.Point;

import java.awt.*;

import static geometry.Utils.*;


public class Edge extends LineCommon {
    private static boolean visitedFlag = true;
    private boolean isVisited;
    Point p1, p2;
    public Point o1, o2;
    public Point i1, i2;

    Edge(Point p1, Point p2) {
        super(p1.x - p2.x, p1.y - p2.y);
        isVisited = !visitedFlag;
        this.p1 = p1;
        this.p2 = p2;
        Point mp = getMidPoint(p1, p2);
        double n = Math.sqrt(A * A + B * B);
        A = A / n;
        B = B / n;
        C = -(mp.x * A + mp.y * B);
        o1 = getInfiniteOrigin(mp, this);
        o2 = getNegInfiniteOrigin(mp, this);
        i1 = o1;
        i2 = o2;
    }

    Edge(Point p1, Point p2, Point o1) {
        this(p1, p2);
        this.o1 = o1;
    }

    Edge() {
        super(0,0);
    }

    double getDistToOrigin(Point p) {
        if (p == null) return Double.POSITIVE_INFINITY;
        double dx = p.x - o1.x;
        double dy = p.y - o1.y;
        return -B * dx + A * dy;
    }

    Point getOpposite(Point p) {
        return p1 == p ? p2 : p1;
    }

    Line2D toLine2D(double r0) {
        return toLine2D(getMidPoint(p1, p2), r0);
    }

    Line2D toLine2D(Point p0, double r0) {
        return LineCircleIntersect(this, p0, r0);
    }

    static Point aim(Point p, Point p0) {
        double dx = p.x - p0.x;
        double dy = p.y - p0.y;
        return new Point(p0.x + dx * 0.95, p0.y + dy * 0.95);
    }

    Line2D toLine2DAim(Point p0) {
        return new Line2D(aim(o1, p0), aim(o2, p0));
    }

    boolean visited() {
        return isVisited == visitedFlag;
    }

    void markVisited() {
        isVisited = visitedFlag;
    }

    static void parsed() {
        visitedFlag = !visitedFlag;
    }

    public void drawVector() {
        Point mp = getMidPoint(p1, p2);
        paper.addLine(new Line2D(mp, new Point(mp.x - B, mp.y + A)), Color.BLACK);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", o1=" + o1 +
                ", o2=" + o2 +
                ", i1=" + i1 +
                ", i2=" + i2 +
                '}';
    }
}
