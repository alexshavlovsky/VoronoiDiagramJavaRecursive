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
    Point origin;
    Node n1, n2;

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
        origin = getInfiniteOrigin(mp,this);
    }

    Edge(Point p1, Point p2, Point origin) {
        this(p1, p2);
        this.origin = origin;
    }

    double getDistToOrigin(Point p) {
        double dx = p.x - origin.x;
        double dy = p.y - origin.y;
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
                ", n1=" + n1 +
                ", n2=" + n2 +
                '}';
    }
}
