package voronoy;

import geometry.Point;

import static geometry.Utils.LinesIntersect;
import static geometry.Utils.cmpGL;
import static geometry.Utils.getRayToEdgeAngle;

class EdgesIntersection {
    double angle;
    Edge edge;
    Point site, node;

    public EdgesIntersection(Point site, Edge inRay, Edge edge) {
        this.angle = getRayToEdgeAngle(inRay, edge);
        if (cmpGL(angle, -1, 1)) {
            this.node = LinesIntersect(inRay, edge);
            this.edge = edge;
            this.site = site;
        }
    }

    @Override
    public String toString() {
        return "EdgesIntersection{" +
                "angle=" + angle +
                ", edge=" + edge +
                ", site=" + site +
                ", node=" + node +
                '}';
    }
}
