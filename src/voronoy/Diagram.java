package voronoy;

import gui.Canvas;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Diagram{
    final private int Y_EXPORT=675;
    private ConvexHull hull;
    private HashMap<Point2D,Set<Point2D>> points = new HashMap<>();
    private HashMap<Point2D,Set<Point2D>> nodes = new HashMap<>();

    @Override
    public String toString() {
        return "---=== Diagram ===---\n" +
                hull +
                "\npoints=" + points +
                "\nnodes=" + nodes;
    }

    public Diagram(Point2D p1) {
        hull = new ConvexHull(Arrays.asList(p1));
        points.put(p1, new HashSet<>());
    }

    public Diagram(Point2D p1, Point2D p2) {
        hull = new ConvexHull(Arrays.asList(p1,p2));
        points.put(p1, new HashSet<>(Collections.singletonList(p2)));
        points.put(p2, new HashSet<>(Collections.singletonList(p1)));
    }

    public Diagram(int x1, int y1) {
        Point2D p1=new Point2D(x1,Y_EXPORT-y1);
        hull = new ConvexHull(Arrays.asList(p1));
        points.put(p1, new HashSet<>());
    }

    public Diagram(int x1, int y1, int x2, int y2) {
        Point2D p1=new Point2D(x1,Y_EXPORT-y1);
        Point2D p2=new Point2D(x2,Y_EXPORT-y2);
        hull = new ConvexHull(Arrays.asList(p1,p2));
        points.put(p1, new HashSet<>(Collections.singletonList(p2)));
        points.put(p2, new HashSet<>(Collections.singletonList(p1)));
    }

    private static final double ZERO = 0.00001;

    public Point2D[] getFirstIntersect(Point2D p1, Point2D p2, double ym) {
        LineCommon ray = new Edge(p1, p2);
        return Stream.of(
                points.get(p1).stream().map(p -> new Point2D[]{ray.getIntersectionPoint(new Edge(p1, p)), p, p1}),
                points.get(p2).stream().map(p -> new Point2D[]{ray.getIntersectionPoint(new Edge(p2, p)), p, p2})
        ).flatMap(x -> x).filter(p -> p[0].y + ZERO < ym).max((a, b) -> (int) (a[0].y - b[0].y)).orElse(null);
    }

    public void consolidate(Diagram d2) {
        hull.consolidate(d2.hull);
        points.putAll(d2.points);
        nodes.putAll(d2.nodes);

        Point2D p1 = hull.pivotU.p1;
        Point2D p2 = hull.pivotU.p2;
        double ym = Double.POSITIVE_INFINITY;
        while (true) {
            Point2D[] intersect = getFirstIntersect(p1, p2, ym);
            if (intersect == null) break;
            nodes.put(intersect[0], new HashSet<>(Arrays.asList(p1, p2, intersect[1])));
            Point2D[] pts = {p1, p2, intersect[1]};
            for (int i = 0; i < 3; i++) {
                points.get(pts[i]).add(pts[(i + 1) % 3]);
                points.get(pts[i]).add(pts[(i + 2) % 3]);
            }
            if (p1 == intersect[2]) p1 = intersect[1];
            if (p2 == intersect[2]) p2 = intersect[1];
            ym = intersect[0].y;
        }
    }

    void draw(Canvas pap) {
        for (Point2D p : points.keySet()) pap.addPoint(p, Color.RED, 20);
        for (Point2D n : nodes.keySet()) {
            pap.addPoint(n, Color.BLUE, 10);
            Point2D[] pts = nodes.get(n).toArray(new Point2D[3]);
            for (int i = 0; i < 3; i++) pap.addLine(new Line2D(pts[i], pts[(i + 1) % 3]), Color.RED);
            for (int i = 0; i < 3; i++) pap.addLine(new Edge(pts[i], pts[(i + 1) % 3]).LineCircleIntersect(n,50), Color.BLUE);
        }
    }

    String exportSites(){
        return points.keySet().stream().map(p->(int)p.x+","+(Y_EXPORT-(int)p.y)).collect(Collectors.joining(",","{\"sites\":[","],\"queries\":[]}"));
    }

}
