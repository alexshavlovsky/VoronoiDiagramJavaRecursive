package voronoy;

import gui.Canvas;

import static voronoy.Diagram.mergeDiagrams;


public class Voron {

    private static boolean isFinite(double d) {
        return Math.abs(d) < Double.POSITIVE_INFINITY;
    }

    private static Point2D getCircle(Point2D p1, Point2D p2, Point2D p3) {
        double x12 = p1.x - p2.x, x31 = p3.x - p1.x, x23 = p2.x - p3.x, xm = x31 * x12 * x23;
        double y12 = p1.y - p2.y, y31 = p3.y - p1.y, y23 = p2.y - p3.y, ym = y31 * y12 * y23;
        y12 = y12 * p3.x;
        y31 = y31 * p2.x;
        y23 = y23 * p1.x;
        double d = 2.0 * (y12 + y31 + y23);
        return new Point2D((y12 * p3.x + y31 * p2.x + y23 * p1.x - ym) / d,
                (xm - x12 * p3.y * p3.y - x31 * p2.y * p2.y - x23 * p1.y * p1.y) / d);
    }

    static double getPolarSqr(Point2D p0, Point2D p) {
        double dx = p.x - p0.x;
        double dy = p.y - p0.y;
        return dx / Math.sqrt(dx * dx + dy * dy);
    }

    public static void main(String[] args) {
        Canvas paper = new Canvas(900, 675, 1);

        Diagram d1 = new Diagram(145, 475, 248, 209);
        Diagram d2 = new Diagram(289, 397, 442, 468);
//        Diagram d3 = new Diagram(490,380);

        Diagram d = mergeDiagrams(d1, d2);
        System.out.println(d);
        d.draw(paper);
//        System.out.println(d1.exportSites());
    }

}