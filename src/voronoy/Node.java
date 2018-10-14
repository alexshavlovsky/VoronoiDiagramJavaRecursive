package voronoy;

import geometry.Point2D;

import static geometry.Utils.comparePointXY;
import static geometry.Utils.getDistToPoint;

public class Node {
    private static boolean visitedFlag = true;
    private boolean isVisited;
    Point2D p, p1, p2, p3;
    Edge in, edge, out;
    boolean isLeft;

    public Node(Point2D p, Point2D p1, Point2D p2, Point2D p3, Edge in, Edge edge, Edge out, boolean isLeft, Diagram d) {
        isVisited = !visitedFlag;
        this.p = p;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.in = in;
        this.edge = edge;
        this.out = out;
        if (edge.n1 == null && edge.n2 == null) {
            edge.n2 = this;
            d.siteNode.get(p1).add(this);
            d.siteNode.get(p2).add(this);
            d.siteNode.get(p3).add(this);
        }
        else {
            int n1 = 0, n2 = 0;
            double d1 = 0, d2 = 0;
            if (edge.n1 != null) n1 = comparePointXY(edge.n1.p, p);
            if (edge.n2 != null) n2 = comparePointXY(edge.n2.p, p);
            if (edge.n1 != null) d1 = getDistToPoint(in, edge.n1.p);
            if (edge.n2 != null) d2 = getDistToPoint(in, edge.n2.p);

            System.out.println(n1 + "//" + n2 + ">>" + d1 + "::" + d2);
            //if (n1==-1 || n2 ==1) edge.n2 = this; else edge.n1 = this;
            if (d1 > 0 || d2 < 0) {
                d.siteNode.get(p1).add(this);
                d.siteNode.get(p2).add(this);
                d.siteNode.get(p3).add(this);
                if (edge.n2!=null) {
                    d.siteNode.get(p1).remove(edge.n2);
                    d.siteNode.get(p2).remove(edge.n2);
                    d.siteNode.get(p3).remove(edge.n2);
                }
                edge.n2 = this;
            } else {
                d.siteNode.get(p1).add(this);
                d.siteNode.get(p2).add(this);
                d.siteNode.get(p3).add(this);
                if (edge.n1!=null){
                    d.siteNode.get(p1).remove(edge.n1);
                    d.siteNode.get(p2).remove(edge.n1);
                    d.siteNode.get(p3).remove(edge.n1);
                }
                edge.n1 = this;
            }
        }
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
        return "Node{" +
                "p=" + p +
                '}';
    }
}
