package geometry;


public class Line2D {

    public Point2D p1, p2;

    public Line2D(Point2D p1, Point2D p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String toString() {
        return "[" + p1 + ", " + p2 + ']';
    }

}
