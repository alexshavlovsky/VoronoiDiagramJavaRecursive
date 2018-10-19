package voronoy;

import geometry.Point;

import static geometry.Utils.getDistToPoint;

public class Node {
    private static boolean visitedFlag = true;
    private boolean isVisited;
    Point p, p1, p2, p3;
    Edge in, edge, out;
    boolean isLeft;

    public Node(Point p, Point p1, Point p2, Point p3, Edge in, Edge edge, Edge out, boolean isLeft) {
        isVisited = !visitedFlag;
        this.p = p;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.in = in;
        this.edge = edge;
        this.out = out;
        this.isLeft = isLeft;
        double d1 = 0, d2 = 0;
        if (edge.n1 != null) d1 = getDistToPoint(in, edge.n1.p);
        if (edge.n2 != null) d2 = getDistToPoint(in, edge.n2.p);
        if ((d1 > 0 || d2 < 0 || (d1 == 0 && d2 == 0)) ^ !isLeft) edge.n2 = this;
        else edge.n1 = this;
        in.n2 = this;
        out.n1 = this;
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

    @Override
    public String toString() {
        return "Node" + p;
    }
}
