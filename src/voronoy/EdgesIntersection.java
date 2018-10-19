package voronoy;

import geometry.Point;

import static geometry.Utils.LinesIntersect;
import static geometry.Utils.getRayToEdgeCtg;

class EdgesIntersection {
    Edge inRay, edge, outRay;
    Point site, node, next;
    boolean isLeft;
    double angle;


    public EdgesIntersection(Point site, Edge inRay, Edge edge, boolean isLeft) {
        Double ctg = getRayToEdgeCtg(inRay, edge, isLeft);
        if (ctg != null) {
            this.edge = edge;
            this.site = site;
            this.inRay = inRay;
            this.isLeft = isLeft;
            this.angle = ctg;
            this.node = LinesIntersect(inRay, edge);
        }
    }

    @Override
    public String toString() {
        return "EdgesIntersection{" +
                "inRay=" + inRay +
                ", edge=" + edge +
                ", outRay=" + outRay +
                ", site=" + site +
                ", node=" + node +
                ", next=" + next +
                ", isLeft=" + isLeft +
                ", angle=" + angle +
                '}';
    }
}
