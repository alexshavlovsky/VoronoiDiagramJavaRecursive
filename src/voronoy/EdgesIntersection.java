package voronoy;

import geometry.Point2D;

import static geometry.Utils.LinesIntersect;

class EdgesIntersection {
    Edge ray, e;
    Point2D site, node;

    public EdgesIntersection(Point2D site, Edge ray, Edge e) {
        this.ray = ray;
        this.e = e;
        this.site = site;
        this.node = LinesIntersect(ray, e);
    }

    public EdgesIntersection(Edge ray){
        this.ray = ray;
        this.e = null;
        this.site = null;
        this.node = null;
    }

}
