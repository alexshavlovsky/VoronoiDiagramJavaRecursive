package voronoy;

import geometry.Point2D;

public class Node {
    Point2D p, p1, p2, p3;
    Edge in, edge, out;
    boolean isLeft;

    public Node(Point2D p, Point2D p1, Point2D p2, Point2D p3, Edge in, Edge edge, Edge out, boolean isLeft) {
        this.p = p;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.in = in;
        this.edge = edge;
        this.out = out;
        in.n2 = this;
        if (isLeft) edge.n2 = this;
        else edge.n1 = this;
        out.n1 = this;
    }

    @Override
    public String toString() {
        return "Node{" +
                "p=" + p +
                '}';
    }
}
