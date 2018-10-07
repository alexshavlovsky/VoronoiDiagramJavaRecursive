package voronoy;

import geometry.Point2D;

import static geometry.Utils.LinesIntersect;

class EdgesIntersection {
    Edge edge;
    Point2D site, node;

    public EdgesIntersection(Point2D site, Edge inRay, Edge edge) {
        this.edge = edge;
        this.site = site;
        this.node = LinesIntersect(inRay, edge);
    }

}
