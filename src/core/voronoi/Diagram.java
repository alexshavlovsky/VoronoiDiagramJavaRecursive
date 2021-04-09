package core.voronoi;

import core.geometry.Line2D;
import core.geometry.Point;
import core.geometry.Utils;
import gui.DCircle;
import gui.DLine;
import gui.Drawable;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;

import static core.voronoi.ConvexHull.mergeHulls;

public class Diagram {
    private ConvexHull hull;
    private HashMap<Point, CellIterator> cell = new HashMap<>();
    private static List<Line2D> tr = null;
    private static boolean traceRay = false;

    private Diagram(Diagram d1, Diagram d2) {
        this.hull = mergeHulls(d1.hull, d2.hull);
        this.cell.putAll(d1.cell);
        this.cell.putAll(d2.cell);
    }

    private Diagram(Point p0) {
        hull = new ConvexHull(Arrays.asList(p0));
        cell.put(p0, new CellIterator(DirectedEdge.getEmptyCell(), true));
    }

    private Diagram(Point p1, Point p2) {
        hull = new ConvexHull(Arrays.asList(p1, p2));
        Edge e = new Edge(p1, p2);
        DirectedEdge[] pair = DirectedEdge.getJoinedEdges(e);
        CellIterator itr1 = new CellIterator(DirectedEdge.getEmptyCell(), false);
        CellIterator itr2 = new CellIterator(DirectedEdge.getEmptyCell(), true);
        itr1.insert(pair[0]);
        itr2.insert(pair[1]);
        cell.put(p1, itr1);
        cell.put(p2, itr2);
    }

    public Diagram(List<Point> s) {
        s.sort(Utils::comparePointXY);
        Diagram d = Recursive(s, 0, s.size(), s.size());
        this.hull = d.hull;
        this.cell = d.cell;
    }

    private Diagram Recursive(List<Point> s, int i1, int i2, int sz) {
        int n = i2 - i1;
        if (n == 1) return new Diagram(s.get(i1));
        if (n == 2) return new Diagram(s.get(i1), s.get(i1 + 1));
        Diagram d1 = Recursive(s, i1, i1 + n / 2, sz);
        Diagram d2 = Recursive(s, i1 + n / 2, i2, sz);
        if (n == sz) traceRay = true;
        return mergeDiagrams(d1, d2);
    }

    static private Diagram mergeDiagrams(Diagram d1, Diagram d2) {
        boolean traceRay = Diagram.traceRay;
        Diagram.traceRay = false;
        if (traceRay) tr = new ArrayList<>();
        Diagram d = new Diagram(d1, d2);
        Point pivot[] = new Point[]{d.hull.pivot[0].p1, d.hull.pivot[0].p2};
        CellIterator itr[] = new CellIterator[]{d1.cell.get(pivot[0]).init(true), d2.cell.get(pivot[1]).init(false)};
        Edge ray = null;
        for (int i = 0; i < 5000; i++) {
            ray = new Edge(pivot[0], pivot[1], ray);
            DirectedEdge[] pair = DirectedEdge.getJoinedEdges(ray);
            DirectedEdge edges[] = new DirectedEdge[]{itr[0].cropCell(ray, pair[0]), itr[1].cropCell(ray, pair[1])};
            if (pivot[0] == d.hull.pivot[1].p1 && pivot[1] == d.hull.pivot[1].p2) {
                if (traceRay) tr.add(new Line2D(ray.o1, ray.o2));
                return d;
            }
            Point[] intPoint = new Point[]{ray.getIntersection(edges[0]), ray.getIntersection(edges[1])};
            if (intPoint[0] == null && intPoint[1] == null) throw new RuntimeException("No intersection");
            int l = ray.getDistToOrigin(intPoint[0]) < ray.getDistToOrigin(intPoint[1]) ? 0 : 1;
            ray.o2 = intPoint[l];
            if (Utils.comparePointXY(ray.o1, ray.o2) == 0) {
                itr[0].removeLast();
                itr[1].removeLast();
            }
            if (traceRay) tr.add(new Line2D(ray.o1, ray.o2));
            if (edges[l].e == null) return d;
            if (l == 1 ^ edges[l].fwd) edges[l].e.o1 = ray.o2;
            else edges[l].e.o2 = ray.o2;
            pivot[l] = edges[l].e.getOpposite(pivot[l]);
            itr[l] = d.cell.get(pivot[l]).setDirAndResetToEdge(l == 0, edges[l].opposite);
        }
        throw new RuntimeException("Merging overflow");
    }

    private static BasicStroke defStroke = new BasicStroke(0.005f);
    private static BasicStroke boldStroke = new BasicStroke(0.010f);
    private static double siteSize = 0.03;

    private static Point2D.Double toPoint2D(Point p){
        return new Point2D.Double(p.x, p.y);
    }

    public List<Drawable> toDrawable(boolean drawTraceRay, boolean drawOpenedCells) {
        List<Drawable> drawables = new ArrayList<>();
        for (Map.Entry<Point, CellIterator> siteCell : cell.entrySet()) {
            CellIterator itr = siteCell.getValue();
            DirectedEdge n0 = itr.getAnyOpenedEdge();
            if (!drawOpenedCells && n0.inf != 0) continue;
            Point p0 = siteCell.getKey();
            drawables.add(new DCircle(p0.x, p0.y, siteSize, Color.BLACK, defStroke));
            DirectedEdge n = n0;
            int c = 0;
            do {
                if (c++ == 1000) throw new RuntimeException("Drawing overflow");
                if (n.inf == 0) {
                    Line2D z = n.e.toShiftedLine(p0, 0.8);
                    drawables.add(new DLine(toPoint2D(z.p1), toPoint2D(z.p2), n.fwd ? Color.BLUE : Color.RED, defStroke));
                }
                n = n.next;
            } while (n != n0);
        }
        if (drawTraceRay && tr != null) for (Line2D line : tr)
            drawables.add(new DLine(toPoint2D(line.p1), toPoint2D(line.p2), Color.RED, boldStroke));
        return drawables;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int pn = 0;
        for (Map.Entry<Point, CellIterator> siteCell : cell.entrySet()) {
            s.append(++pn).append(". ").append(siteCell.getKey()).append(siteCell.getValue());
        }
        return s.toString();
    }

}
