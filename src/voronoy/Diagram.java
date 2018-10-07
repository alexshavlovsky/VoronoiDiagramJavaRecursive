package voronoy;

import geometry.Line2D;
import geometry.Point2D;
import gui.Canvas;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static geometry.Utils.comparePointYX;
import static voronoy.ConvexHull.mergeHulls;

class Diagram {
    final static private int Y_EXPORT = 675;
    private ConvexHull hull;
    private HashMap<Point2D, List<Edge>> siteEdge = new HashMap<>();
    private HashMap<Point2D, List<Node>> siteNode = new HashMap<>();

    @Override
    public String toString() {
        return "Diagram{" +
                "\n" + hull +
                "\nSite-Edge=" + siteEdge +
                "\nSite-Node=" + siteNode +
                '}';
    }

    public Diagram(ConvexHull h) {
        hull = h;
    }

    public Diagram(Point2D p1) {
        hull = new ConvexHull(Arrays.asList(p1));

        siteEdge.put(p1, new ArrayList<>());
        siteNode.put(p1, new ArrayList<>());
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
                max((a, b) -> comparePointYX(a.node, b.node)).
                orElse(null);
    }

    static Color getColorFromInt(int i) {
        return Color.getHSBColor((i % 12) / 12.0f, 0.8f, 0.8f);
    }

    void putEdge(Edge edge) {
        siteEdge.get(edge.p1).add(edge);
        siteEdge.get(edge.p2).add(edge);
    }

    void putNode(Node node) {
        siteNode.get(node.p1).add(node);
        siteNode.get(node.p2).add(node);
        siteNode.get(node.p3).add(node);
    }

    static Diagram mergeDiagrams(Diagram d1, Diagram d2, Canvas pap) {
        ConvexHull h = mergeHulls(d1.hull, d2.hull);
        Diagram d = new Diagram(h);
        d.siteEdge.putAll(d1.siteEdge);
        d.siteEdge.putAll(d2.siteEdge);
        d.siteNode.putAll(d1.siteNode);
        d.siteNode.putAll(d2.siteNode);
        Point2D pm = new Point2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Edge inRay = new Edge(h.pivot[0].p1, h.pivot[0].p2);
        while (true) {
            d.putEdge(inRay);
            EdgesIntersection intersect = d.getFirstIntersect(inRay, pm);
            if (intersect == null) break;
            Point2D p3 = intersect.edge.getOpposite(intersect.site);
            boolean isLeft = intersect.site == inRay.p1;
            Edge outRay = (isLeft) ? new Edge(p3, inRay.p2) : new Edge(inRay.p1, p3);
            if (pap!=null) {
                System.out.println(isLeft);
                pap.addLine(inRay.toLine2D(1000), Color.BLACK);
                pap.addPoint(intersect.node, Color.BLACK, 16);
            }
            d.putNode(new Node(intersect.node, inRay.p1, inRay.p2, p3, inRay, intersect.edge, outRay, isLeft));
            pm = intersect.node;
            inRay = outRay;
        }
        return d;
    }


    void draw(Canvas pap) {
        for (Map.Entry<Point2D, List<Edge>> point2DSetEntry : siteEdge.entrySet()) {
            pap.addPoint(point2DSetEntry.getKey(), Color.RED, 16);
        }
        Point2D origin = hull.halves[0].get(0);
        if (siteNode.get(origin).size() == 0) {
            if (siteEdge.get(origin).size() > 0) pap.addLine(siteEdge.get(origin).get(0).toLine2D(1000), Color.BLUE);
        } else {
            parse(siteNode.get(origin).get(0), pap);
            Node.parsed();
        }

        pap.repaint();
    }

    Node drawEdge(Edge e, Point2D p, Canvas pap, Color c, boolean inEdge) {
        Line2D l = e.toLine2D(p, 100);
        Node res = null;
        if (e.n1 != null && e.n2 != null) {
            l = new Line2D(e.n1.p, e.n2.p);
            if (!e.n1.visited()) res = e.n1;
            if (!e.n2.visited()) res = e.n2;
        } else if (inEdge) {
            l.p1 = p;
            if (e.n1 != null) l.p2 = e.n1.p;
            res = e.n1;
        } else {
            l.p2 = p;
            if (e.n2 != null) l.p1 = e.n2.p;
            res = e.n2;
        }
        pap.addLine(l, c);
        return res;
    }

    void parse(Node node, Canvas pap) {
        if (node == null || node.visited()) return;
        else node.markVisited();
        pap.addPoint(node.p, Color.blue, 8);

        parse(drawEdge(node.in, node.p, pap, Color.ORANGE, true),pap);
        parse(drawEdge(node.out, node.p, pap, Color.GREEN, false),pap);
        parse(drawEdge(node.edge, node.p, pap, Color.CYAN, !node.isLeft),pap);
    }

    String exportSites() {
        return siteEdge.keySet().stream().map(p -> (int) p.x + "," + (Y_EXPORT - (int) p.y)).collect(Collectors.joining(",", "{\"sites\":[", "],\"queries\":[]}"));
    }

}
