package voronoy;

import geometry.Line2D;
import geometry.LineCommon;
import geometry.Point2D;

import static geometry.Utils.LineCircleIntersect;
import static geometry.Utils.getMidPoint;

class Edge extends LineCommon {

    private Point2D mp;

    Edge(Point2D p1, Point2D p2) {
        super(p1.x - p2.x, p1.y - p2.y);
        mp = getMidPoint(p1, p2);
        double n = Math.sqrt(A * A + B * B);
        A = A / n;
        B = B / n;
        C = -(mp.x * A + mp.y * B);
    }

    Line2D toLine2D(double r0) {
        return LineCircleIntersect(this, mp, r0);
    }

}
