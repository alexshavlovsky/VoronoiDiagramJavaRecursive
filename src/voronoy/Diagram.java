package voronoy;

import geometry.Point2D;
import gui.Canvas;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static geometry.Utils.comparePointXY;
import static geometry.Utils.comparePointYX;
import static voronoy.ConvexHull.mergeHulls;

class Diagram {
    final static private int Y_EXPORT = 675;
    private ConvexHull hull;
    private HashMap<Point2D, Set<Edge>> siteEdge = new HashMap<>();      //0-...
    private HashMap<Point2D, Set<Node>> siteNode = new HashMap<>();      //0-...
    private HashMap<Edge, Set<Point2D>> edgeNode = new HashMap<>();      //0-2

    @Override
    public String toString() {
        return "Diagram{" +
                "\n" + hull +
                "\nSite-Edge=" + siteEdge +
                "\nSite-Node=" + siteNode +
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

/*    public Diagram(Point2D p1, Point2D p2) {
        hull = new ConvexHull(Arrays.asList(p1, p2));
        Edge e = new Edge(p1, p2);
        edgeNode.put(e, new HashSet<>());
        siteEdge.put(p1, new HashSet<>(Arrays.asList(e)));
        siteEdge.put(p2, new HashSet<>(Arrays.asList(e)));
        siteNode.put(p1, new HashSet<>());
        siteNode.put(p2, new HashSet<>());
    }*/

    public Diagram(int x1, int y1) {
        this(new Point2D(x1, Y_EXPORT - y1));
    }

/*    public Diagram(int x1, int y1, int x2, int y2) {
        this(new Point2D(x1, Y_EXPORT - y1), new Point2D(x2, Y_EXPORT - y2));
    }*/

    public EdgesIntersection getFirstIntersect(Edge inRay, Point2D pm) {
        return Stream.of(
                siteEdge.get(inRay.p1).stream().map(e -> new EdgesIntersection(inRay.p1, inRay, e)),
                siteEdge.get(inRay.p2).stream().map(e -> new EdgesIntersection(inRay.p2, inRay, e))
        ).flatMap(x -> x).
                filter(i -> comparePointYX(i.node, pm) < 0).
                max((a, b) -> comparePointXY(a.node, b.node)).
                orElse(null);
    }

    static ArrayList<EdgesIntersection> chain = new ArrayList<>();

    static Color getColorFromInt(int i) {
        return Color.getHSBColor((i % 12) / 12.0f, 0.8f, 0.8f);
    }

    void putEdge(Edge edge){
        siteEdge.get(edge.p1).add(edge);
        siteEdge.get(edge.p2).add(edge);
    }

    void putNode(Node node) {
        siteNode.get(node.p1).add(node);
        siteNode.get(node.p2).add(node);
        siteNode.get(node.p3).add(node);
    }

    static Diagram mergeDiagrams(Diagram d1, Diagram d2) {
        ConvexHull h = mergeHulls(d1.hull, d2.hull);
        Diagram d = new Diagram(h);
        d.siteEdge.putAll(d1.siteEdge);
        d.siteEdge.putAll(d2.siteEdge);
        d.siteNode.putAll(d1.siteNode);
        d.siteNode.putAll(d2.siteNode);
        Point2D pm = new Point2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Edge inRay = new Edge(h.pivot[0].p1,h.pivot[0].p2);
        while (true) {
            d.putEdge(inRay);
            EdgesIntersection intersect = d.getFirstIntersect(inRay, pm);
//            chain.add(intersect);
            if (intersect == null) break;
            Point2D p3 = intersect.edge.getOpposite(intersect.site);
            Edge outRay = (intersect.site == inRay.p1) ? new Edge(p3, inRay.p2) : new Edge(inRay.p1, p3);
            d.putNode(new Node(inRay.p1, inRay.p2, p3, inRay, intersect.edge, outRay));
            pm = intersect.node;
            inRay = outRay;
        }
        return d;
    }

    void draw(Canvas pap) {
/*        for (int i = 0; i < chain.size(); i++) {
            pap.addPoint(chain.get(i).node, getColorFromInt(i), 8);
            pap.addPoint(chain.get(i).site, getColorFromInt(i), 32);
        }
        for (int i = 0; i < chain.size() - 1; i++)
            pap.addLine(new Line2D(chain.get(i).node, chain.get(i + 1).node), getColorFromInt(i));*/
        for (Map.Entry<Point2D, Set<Edge>> point2DSetEntry : siteEdge.entrySet()) {
            pap.addPoint(point2DSetEntry.getKey(), Color.RED, 16);
            for (Edge edge : point2DSetEntry.getValue())
                pap.addLine(edge.toLine2D(1000), Color.BLUE);
        }
        pap.repaint();
    }

    String exportSites() {
        return siteEdge.keySet().stream().map(p -> (int) p.x + "," + (Y_EXPORT - (int) p.y)).collect(Collectors.joining(",", "{\"sites\":[", "],\"queries\":[]}"));
    }

}
