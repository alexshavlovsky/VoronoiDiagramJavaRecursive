package voronoy;

import geometry.*;
import geometry.Point;
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
    private HashMap<Point, List<Edge>> siteEdge = new HashMap<>();

    @Override
    public String toString() {
        return "Diagram{" +
                "\n" + hull +
                "\nSite-Edge=" + siteEdge +
                '}';
    }

    public Diagram(ConvexHull h) {
        hull = h;
    }

    public Diagram(Point p1) {
        hull = new ConvexHull(Arrays.asList(p1));

        siteEdge.put(p1, new ArrayList<>());
    }

/*    public Diagram(Point p1, Point p2) {
        hull = new ConvexHull(Arrays.asList(p1, p2));
        Edge e = new Edge(p1, p2);
        edgeNode.put(e, new HashSet<>());
        siteEdge.put(p1, new HashSet<>(Arrays.asList(e)));
        siteEdge.put(p2, new HashSet<>(Arrays.asList(e)));
        siteNode.put(p1, new HashSet<>());
        siteNode.put(p2, new HashSet<>());
    }*/

    public Diagram(int x1, int y1) {
        this(new Point(x1, Y_EXPORT - y1));
    }

/*    public Diagram(int x1, int y1, int x2, int y2) {
        this(new Point(x1, Y_EXPORT - y1), new Point(x2, Y_EXPORT - y2));
    }*/

    public EdgesIntersection getFirstIntersect(Edge inRay, geometry.Point pm) {
        List<EdgesIntersection> left = siteEdge.get(inRay.p1).stream().map(e -> new EdgesIntersection(inRay.p1, inRay, e)).filter(i -> comparePointYX(i.node, pm) < 0).collect(Collectors.toList());
        List<EdgesIntersection> right = siteEdge.get(inRay.p2).stream().map(e -> new EdgesIntersection(inRay.p2, inRay, e)).filter(i -> comparePointYX(i.node, pm) < 0).collect(Collectors.toList());
        System.out.println(left+":"+right);
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



    static Diagram mergeDiagrams(Diagram d1, Diagram d2, Canvas pap) {
        ConvexHull h = mergeHulls(d1.hull, d2.hull);
        Diagram d = new Diagram(h);
        d.siteEdge.putAll(d1.siteEdge);
        d.siteEdge.putAll(d2.siteEdge);
        Point pm = new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Edge inRay = new Edge(h.pivot[0].p1, h.pivot[0].p2);
        while (true) {
            d.putEdge(inRay);
            EdgesIntersection intersect = d.getFirstIntersect(inRay, pm);
            if (intersect == null) break;
            Point p3 = intersect.edge.getOpposite(intersect.site);
            boolean isLeft = intersect.site == inRay.p1;
            Edge outRay = (isLeft) ? new Edge(p3, inRay.p2) : new Edge(inRay.p1, p3);
            if (pap!=null) {
                System.out.println(isLeft);
                if (isLeft)
                pap.addLine(inRay.toLine2D(1000), Color.BLACK);
                else
                    pap.addLine(inRay.toLine2D(1000), Color.RED);
                pap.addPoint(intersect.node, Color.BLACK, 16);
            }
            Node n = new Node(intersect.node, inRay.p1, inRay.p2, p3, inRay, intersect.edge, outRay, isLeft,d);

            pm = intersect.node;
            inRay = outRay;
        }
        return d;
    }


    void draw(Canvas pap) {
        for (Map.Entry<Point, List<Edge>> point2DSetEntry : siteEdge.entrySet()) {
            pap.addPoint(point2DSetEntry.getKey(), Color.RED, 16);
        }
        if (siteEdge.get(hull.halves[0].get(0)).size()>0) {
            Edge origin = siteEdge.get(hull.halves[0].get(0)).get(0);
            if (origin.n2==null) pap.addLine(origin.toLine2D(1000),Color.BLUE);
            parse(siteEdge.get(hull.halves[0].get(0)).get(0).n2, pap);
        }
        pap.repaint();
    }

    Node drawEdge(Edge e, Point p, Canvas pap, Color c, boolean inEdge) {
        Line2D l = e.toLine2D(p, 1000);
        Node res = null;
        if (e.n1 != null && e.n2 != null) {
            l = new Line2D(e.n1.p, e.n2.p);
            if (!e.n1.visited()) res = e.n1;
            if (!e.n2.visited()) res = e.n2;
        } else if (inEdge) {
            l.p1 = p;
            res = e.n1;
        } else {
            l.p2 = p;
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
