package voronoy;

import geometry.Line2D;
import geometry.LineCommon;
import geometry.Point2D;
import gui.Canvas;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static geometry.Utils.LinesIntersect;
import static geometry.Utils.comparePointXY;
import static geometry.Utils.comparePointYX;
import static voronoy.ConvexHull.mergeHulls;

class Diagram {
    final static private int Y_EXPORT = 675;
    private ConvexHull hull;
    private HashMap<Point2D, Set<Edge>> siteEdge = new HashMap<>();      //0-...
    private HashMap<Point2D, Set<Point2D>> siteNode = new HashMap<>();   //0-...
    private HashMap<Point2D, Set<Point2D>> nodeSite = new HashMap<>();   //3
    private HashMap<Point2D, Set<Edge>> nodeEdge = new HashMap<>();      //3
    private HashMap<Edge, Point2D[]> edgeSite = new HashMap<>();         //2
    private HashMap<Edge, Set<Point2D>> edgeNode = new HashMap<>();      //0-2

    @Override
    public String toString() {
        return "Diagram{" +
                "\n" + hull +
                "\nSite-Edge=" + siteEdge +
                "\nSite-Node=" + siteNode +
                "\nNode-Site=" + nodeSite +
                "\nNode-Edge=" + nodeEdge +
                "\nEdge-Site=" + edgeSite +
                "\nEdge-Node=" + edgeNode +
                '}';
    }

    public Diagram(ConvexHull h) {
        hull = h;
    }

    public Diagram(Point2D p1) {
        hull = new ConvexHull(Arrays.asList(p1));
        siteEdge.put(p1, new HashSet<>());
        siteNode.put(p1, new HashSet<>());
    }

    public Diagram(Point2D p1, Point2D p2) {
        hull = new ConvexHull(Arrays.asList(p1, p2));
        Edge e = new Edge(p1, p2);
        edgeSite.put(e, new Point2D[]{p1, p2});
        edgeNode.put(e, new HashSet<>());
        siteEdge.put(p1, new HashSet<>(Arrays.asList(e)));
        siteEdge.put(p2, new HashSet<>(Arrays.asList(e)));
        siteNode.put(p1, new HashSet<>());
        siteNode.put(p2, new HashSet<>());
    }

    public Diagram(int x1, int y1) {
        this(new Point2D(x1, Y_EXPORT - y1));
    }

    public Diagram(int x1, int y1, int x2, int y2) {
        this(new Point2D(x1, Y_EXPORT - y1), new Point2D(x2, Y_EXPORT - y2));
    }

    public Object[] getFirstIntersect(Point2D p1, Point2D p2, Point2D pm) {
        LineCommon ray = new Edge(p1, p2);
        return Stream.of(
                siteEdge.get(p1).stream().map(e -> new Object[]{LinesIntersect(ray, e), p1, e}),
                siteEdge.get(p2).stream().map(e -> new Object[]{LinesIntersect(ray, e), p2, e})
        ).flatMap(x -> x).filter(p -> comparePointYX((Point2D) p[0], pm) < 0).max((a, b) -> comparePointXY((Point2D) a[0], (Point2D) b[0])).orElse(null);
    }

    Point2D getOpposite(Edge e, Point2D p) {
        Point2D[] pair = edgeSite.get(e);
        return pair[0] == p ? pair[1] : pair[0];
    }

    static ArrayList<Object[]> nodes = new ArrayList<>();

    static Color getColorFromInt(int i) {
        return Color.getHSBColor((i % 12) / 12.0f, 0.8f, 0.8f);
    }

    static Diagram mergeDiagrams(Diagram d1, Diagram d2) {
        ConvexHull h = mergeHulls(d1.hull, d2.hull);
        Diagram d = new Diagram(h);
        d.siteEdge.putAll(d1.siteEdge);
        d.siteEdge.putAll(d2.siteEdge);
        Point2D p1 = h.pivot[0].p1;
        Point2D p2 = h.pivot[0].p2;
        Point2D pm = new Point2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        while (true) {
            Object[] intersect = d.getFirstIntersect(p1, p2, pm);
            if (intersect == null) break;
            nodes.add(intersect);
            if (intersect[1] == p1) p1 = d1.getOpposite((Edge) intersect[2], p1);
            if (intersect[1] == p2) p2 = d2.getOpposite((Edge) intersect[2], p2);
            pm = (Point2D) intersect[0];
        }
        return d;
    }

    void draw(Canvas pap) {
        for (int i = 0; i < nodes.size(); i++) {
            pap.addPoint((Point2D) nodes.get(i)[0], getColorFromInt(i), 8);
            pap.addPoint((Point2D) nodes.get(i)[1], getColorFromInt(i), 32);
        }
        for (int i = 0; i < nodes.size() - 1; i++) {
            pap.addLine(new Line2D((Point2D) nodes.get(i)[0], (Point2D) nodes.get(i + 1)[0]), getColorFromInt(i));
        }
        for (Map.Entry<Point2D, Set<Edge>> point2DSetEntry : siteEdge.entrySet()) {
            pap.addPoint(point2DSetEntry.getKey(), Color.RED, 16);
            for (Edge edge : point2DSetEntry.getValue())
                pap.addLine(edge.toLine2D(1000), Color.BLUE);
        }
    }

    String exportSites() {
        return siteEdge.keySet().stream().map(p -> (int) p.x + "," + (Y_EXPORT - (int) p.y)).collect(Collectors.joining(",", "{\"sites\":[", "],\"queries\":[]}"));
    }

}
