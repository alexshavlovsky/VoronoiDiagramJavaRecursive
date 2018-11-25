package voronoi;

import geometry.Point;
import geometry.Utils;
import gui.Canvas;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static geometry.Utils.cmpE;
import static voronoi.ConvexHull.mergeHulls;

class Diagram {
    private ConvexHull hull;
    private HashMap<Point, CellIterator> cell = new HashMap<>();

    private Diagram(Diagram d1, Diagram d2) {
        this.hull = mergeHulls(d1.hull, d2.hull);
        this.cell.putAll(d1.cell);
        this.cell.putAll(d2.cell);
    }

    private Diagram(Point p0) {
        hull = new ConvexHull(Arrays.asList(p0));
        cell.put(p0, new CellIterator());
    }

    private Diagram(Point p1, Point p2) {
        hull = new ConvexHull(Arrays.asList(p1, p2));
        Edge e = new Edge(p1, p2);
        DirectedEdge[] pair = DirectedEdge.getJoinedEdges(e);
        cell.put(p1, new CellIterator(pair[0]));
        cell.put(p2, new CellIterator(pair[1]));
    }

    Diagram(List<Point> s) {
        s.sort(Utils::comparePointXY);
        Diagram d = Recursive(s, 0, s.size());
        this.hull = d.hull;
        this.cell = d.cell;
    }

    private Diagram Recursive(List<Point> s, int i1, int i2) {
        int n = i2 - i1;
        if (n == 1) return new Diagram(s.get(i1));
        if (n == 2) return new Diagram(s.get(i1), s.get(i1 + 1));
        return mergeDiagrams(Recursive(s, i1, i1 + n / 2), Recursive(s, i1 + n / 2, i2));
    }

    static private Diagram mergeDiagrams(Diagram d1, Diagram d2) {
        Diagram d = new Diagram(d1, d2);
        Point pivot[] = new Point[]{d.hull.pivot[0].p1, d.hull.pivot[0].p2};
        CellIterator itr[] = new CellIterator[]{d1.cell.get(pivot[0]).init(true), d2.cell.get(pivot[1]).init(false)};
        Edge ray = null;
        while (true) {
            ray = new Edge(pivot[0], pivot[1], ray);
            DirectedEdge[] pair = DirectedEdge.getJoinedEdges(ray);
            DirectedEdge intEdge[] = new DirectedEdge[]{itr[0].cropCell(ray, pair[0]), itr[1].cropCell(ray, pair[1])};
            if (pivot[0] == d.hull.pivot[1].p1 && pivot[1] == d.hull.pivot[1].p2) return d;
            Point[] intPoint = new Point[]{ray.getIntersection(intEdge[0].e), ray.getIntersection(intEdge[1].e)};
            if (intPoint[0] == intPoint[1]) throw new RuntimeException("Can't determine an intersection side");
            double i1 = ray.getDistToOrigin(intPoint[0]);
            double i2 = ray.getDistToOrigin(intPoint[1]);
            if (cmpE(i1,0)||cmpE(i2,0)) throw new RuntimeException("Too short distance between intersections");
            int l = ray.getDistToOrigin(intPoint[0]) < ray.getDistToOrigin(intPoint[1]) ? 0 : 1;
            ray.o2 = intPoint[l];
            if (l == 0 ^ ray.getCrossDenWithEdge(intEdge[l].e) > 0) intEdge[l].e.o1 = ray.o2;
            else intEdge[l].e.o2 = ray.o2;
            pivot[l] = intEdge[l].e.getOpposite(pivot[l]);
            itr[l] = (l == 0 ? d1 : d2).cell.get(pivot[l]).setDirAndResetToEdge(l == 0, intEdge[l].opposite);
        }
    }

    void draw(Canvas pap) {
        for (Map.Entry<Point, CellIterator> siteCell : cell.entrySet()) {
            CellIterator itr = siteCell.getValue();
            //if (itr.trySearchOpenedEdge()) continue;
            Point p0 = siteCell.getKey();
            pap.addPoint(p0, Color.RED, 4);
            DirectedEdge n0 = itr.de, n = n0;
            do {
                if (n.e != null) pap.addLine(n.e.toShiftedLine(p0, 1), Color.BLACK);
                n = n.next;
            } while (n != n0);
        }
        pap.repaint();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int pn = 0;
        for (Map.Entry<Point, CellIterator> siteCell : cell.entrySet()) {
            s.append(++pn).append(". ").append(siteCell.getKey()).append(siteCell.getValue().print());
        }
        return s.toString();
    }

}
