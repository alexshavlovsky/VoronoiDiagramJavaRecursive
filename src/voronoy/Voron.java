package voronoy;

import gui.Canvas;


public class Voron {


    static double[][] a = {
            {70.0, 24.0}, {63.0, 84.0}, {72.0, 84.0}, {60.0, 43.0}, {0.0, 21.0}, {55.0, 98.0}, {77.0, 68.0}, {93.0, 41.0}, {67.0, 85.0}, {47.0, 4.0},
            {26.0, 63.0}, {94.0, 16.0}, {76.0, 71.0}, {89.0, 50.0}, {97.0, 34.0}, {50.0, 41.0}, {72.0, 70.0}, {58.0, 28.0}, {47.0, 14.0}, {29.0, 59.0},
            {83.0, 9.0}, {99.0, 67.0}, {60.0, 65.0}, {95.0, 22.0}, {63.0, 89.0}, {29.0, 83.0}, {6.0, 82.0}, {55.0, 66.0}, {94.0, 34.0}, {95.0, 89.0},
            {68.0, 68.0}, {4.0, 62.0}, {75.0, 52.0}, {71.0, 32.0}, {79.0, 5.0}, {38.0, 15.0}, {63.0, 76.0}, {20.0, 79.0}, {86.0, 34.0}, {82.0, 12.0},
            {34.0, 57.0}, {68.0, 60.0}, {81.0, 1.0}, {34.0, 2.0}, {80.0, 31.0}, {7.0, 88.0}, {100.0, 89.0}, {76.0, 93.0}, {70.0, 62.0}, {5.0, 73.0},
    };

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

        Diagram d1 = new Diagram(145,475,248,209);
        Diagram d2 = new Diagram(289,397,442,468);
        Diagram d3 = new Diagram(500,380);

        d1.consolidate(d2);
        d1.consolidate(d3);
        System.out.println(d1);
        d1.draw(paper);
        System.out.println(d1.exportSites());
    }

}