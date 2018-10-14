package voronoy;

import geometry.Point;

import static geometry.Utils.LinesIntersect;

class EdgesIntersection {
    Edge edge;
    Point site, node;

    public EdgesIntersection(Point site, Edge inRay, Edge edge) {
        this.edge = edge;
        this.site = site;
        this.node = LinesIntersect(inRay, edge);
    }

    @Override
    public String toString() {
        return "EdgesIntersection{" +
                "edge=" + edge +
                ", site=" + site +
                ", node=" + node +
                '}';
    }
}
