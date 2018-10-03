package voronoy;

import static geometry.Utils.LineCircleIntersect;
import static geometry.Utils.getMidPoint;

public class Edge extends LineCommon{

    Point2D midPoint;

    public Edge(Point2D p1, Point2D p2) {
        super(p1,p2,true);
        midPoint = getMidPoint(p1, p2);
    }

    public Line2D getRays(double r0){
        return LineCircleIntersect(this, midPoint,r0);
    }

    public Edge(Line2D l) {
        this(l.p1,l.p2);
    }

}
