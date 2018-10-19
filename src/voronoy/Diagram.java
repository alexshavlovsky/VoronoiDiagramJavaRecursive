package voronoy;

import geometry.Line2D;
import geometry.Point;
import geometry.Utils;
import gui.Canvas;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static geometry.Utils.*;
import static java.util.Comparator.comparingDouble;
import static voronoy.ConvexHull.mergeHulls;

class Diagram {
    final static private int MAX_RAY_LENGTH = 1000;
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

    public Diagram(Point p1, Point p2) {
        hull = new ConvexHull(Arrays.asList(p1, p2));
        Edge e = new Edge(p1, p2);
        siteEdge.put(p1, new ArrayList<>(Arrays.asList(e)));
        siteEdge.put(p2, new ArrayList<>(Arrays.asList(e)));
    }

    public Diagram (List<Point> s,int z) {
        s.sort(Utils::comparePointXY);
        Diagram d = Recursive(s, 0,s.size(), z);
        this.hull=d.hull;
        this.siteEdge=d.siteEdge;
    }

    private Diagram Recursive(List<Point> s, int i1, int i2, int z) {
        int n = i2 - i1;
        if (n == 1) return new Diagram(s.get(i1));
        if (n == 2) return new Diagram(s.get(i1), s.get(i1 + 1));
        return mergeDiagrams(Recursive(s, i1, i1 + n / 2,z), Recursive(s, i1 + n / 2, i2,z),""+z+":"+i1+"/"+i2);
    }

    public List<EdgesIntersection> getFirstIntersect(Edge inRay, Diagram d1, Diagram d2) {
        List<EdgesIntersection> il = d1.siteEdge.get(inRay.p1).stream().map(e -> new EdgesIntersection(inRay.p1, inRay, e,true)).
                filter(i -> i.node != null).filter(i -> cmpG(inRay.getDistToOrigin(i.node),0)).collect(Collectors.toList());
        List<EdgesIntersection> ir = d2.siteEdge.get(inRay.p2).stream().map(e -> new EdgesIntersection(inRay.p2, inRay, e,false)).
                filter(i -> i.node != null).filter(i -> cmpG(inRay.getDistToOrigin(i.node),0)).collect(Collectors.toList());
        if (il.size()==0 && ir.size()==0) return null;
        Point mn = Stream.of(il.stream(),ir.stream()).flatMap(x->x).min(Comparator.comparingDouble(a -> inRay.getDistToOrigin(a.node))).get().node;
        EdgesIntersection l = il.stream().filter(i->pointsEqual(i.node,mn)).max(comparingDouble(a -> a.angle)).orElse(null);
        EdgesIntersection r = ir.stream().filter(i->pointsEqual(i.node,mn)).max(comparingDouble(a -> a.angle)).orElse(null);
        return Stream.of(l,r).filter(Objects::nonNull).collect(Collectors.toList());
    }

    void putEdge(Edge edge) {
        siteEdge.get(edge.p1).add(edge);
        siteEdge.get(edge.p2).add(edge);
    }


    static Diagram mergeDiagrams(Diagram d1, Diagram d2, String msg) {
        ConvexHull h = mergeHulls(d1.hull, d2.hull);
        Diagram d = new Diagram(h);
        d.siteEdge.putAll(d1.siteEdge);
        d.siteEdge.putAll(d2.siteEdge);
        Edge inRay = new Edge(h.pivot[0].p1, h.pivot[0].p2);
        List<EdgesIntersection> nodes = new ArrayList<>();
  //      int maxc=30;
        outer:
        while (true) {
       //     if (maxc==0) break ;
      //      maxc--;
       //     inRay.drawVector();
            List<EdgesIntersection> ei = d.getFirstIntersect(inRay, d1, d2);
      //      System.out.println(inRay.getDistToOrigin(ei.get(0).node));
            if (ei==null) throw new AssertionError(msg);
    //        if (ei==null) break ;
            for (EdgesIntersection i : ei) {
                if (i.isLeft) {
                    i.next = i.edge.getOpposite(i.inRay.p1);
                    i.outRay = new Edge(i.next, i.inRay.p2,i.node);
                } else {
                    i.next = i.edge.getOpposite(i.inRay.p2);
                    i.outRay = new Edge(i.inRay.p1, i.next,i.node);
                }
                nodes.add(i);
                inRay = i.outRay;
                if (inRay.p1 == h.pivot[1].p1 && inRay.p2 == h.pivot[1].p2) break outer;
            }
        }
        if (nodes.size() > 0) d.putEdge(nodes.get(0).inRay);
        else d.putEdge(inRay);
        for (EdgesIntersection i : nodes) {
        //    System.out.println(i);
            d.putEdge(i.outRay);
            new Node(i.node, i.inRay.p1, i.inRay.p2, i.next, i.inRay, i.edge, i.outRay, i.isLeft);
        }
        return d;
    }


    void draw(Canvas pap) {
        for (Map.Entry<Point, List<Edge>> point2DSetEntry : siteEdge.entrySet()) {
            pap.addPoint(point2DSetEntry.getKey(), Color.RED, 8);
        }
        if (siteEdge.get(hull.halves[0].get(0)).size() > 0) {
            Edge origin = siteEdge.get(hull.halves[0].get(0)).get(0);
            if (origin.n2 == null) pap.addLine(origin.toLine2D(MAX_RAY_LENGTH), Color.BLUE);
            parse(origin.n2, pap);
            Edge.parsed();
            Node.parsed();
        }
        for (Map.Entry<Point, List<Edge>> pointListEntry : siteEdge.entrySet()) {
            pointListEntry.setValue(pointListEntry.getValue().stream().filter(Edge::visited).collect(Collectors.toList()));
        }
        pap.repaint();
    }

    Node drawEdge(Edge e, Point p, Canvas pap, Color c, boolean inEdge) {
        if (e.visited()) return null;
        e.markVisited();
        Line2D l = e.toLine2D(p, MAX_RAY_LENGTH);
        Node res = null;
        if (e.n1 != null && e.n2 != null) {
            l = new Line2D(e.n1.p, e.n2.p);
            if (!e.n1.visited()) res = e.n1;
            if (!e.n2.visited()) res = e.n2;
            pap.addLine(l, c);
        } else if (inEdge) {
            l.p1 = p;
            res = e.n1;
   //         pap.addLine(l, Color.BLUE);
        } else {
            l.p2 = p;
            res = e.n2;
    //        pap.addLine(l, Color.RED);
        }
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

}
