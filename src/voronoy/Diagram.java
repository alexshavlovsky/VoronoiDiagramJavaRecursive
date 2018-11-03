package voronoy;

import geometry.Line2D;
import geometry.Point;
import geometry.Utils;
import gui.Canvas;

import java.awt.*;
import java.util.*;
import java.util.List;

import static geometry.Utils.*;
import static voronoy.ConvexHull.mergeHulls;

class Diagram {
    private ConvexHull hull;
    private HashMap<Point, CellIterator> siteEdge = new HashMap<>();

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
        siteEdge.put(p1, new CellIterator());
    }

    public Diagram(Point p1, Point p2) {
        hull = new ConvexHull(Arrays.asList(p1, p2));
        Edge e = new Edge(p1, p2);
        DirectedEdge[] pair = DirectedEdge.getPairFromEdge(e);
        siteEdge.put(p1, new CellIterator(pair[0]));
        siteEdge.put(p2, new CellIterator(pair[1]));
    }

    public Diagram(List<Point> s) throws Exception {
        s.sort(Utils::comparePointXY);
        Diagram d = Recursive(s, 0, s.size());
        this.hull = d.hull;
        this.siteEdge = d.siteEdge;
    }

    private Diagram Recursive(List<Point> s, int i1, int i2) throws Exception {
        int n = i2 - i1;
        if (n == 1) return new Diagram(s.get(i1));
        if (n == 2) return new Diagram(s.get(i1), s.get(i1 + 1));
        return mergeDiagrams(Recursive(s, i1, i1 + n / 2), Recursive(s, i1 + n / 2, i2));
    }

    static Diagram mergeDiagrams(Diagram d1, Diagram d2) throws Exception {
        ConvexHull h = mergeHulls(d1.hull, d2.hull);
        Diagram d = new Diagram(h);
        d.siteEdge.putAll(d1.siteEdge);
        d.siteEdge.putAll(d2.siteEdge);
        Point PL = h.pivot[0].p1;
        Point PR = h.pivot[0].p2;
        if (Utils.debugFlag)
            System.out.println("dbg");
        Edge e = new Edge(PL, PR);
        CellIterator Litr = d1.siteEdge.get(PL);
        CellIterator Ritr = d2.siteEdge.get(PR);
        Litr.ResetToInfiniteEdge(true);
        Ritr.ResetToInfiniteEdge(false);
        int maxx = 20;
        while (!(PL == h.pivot[1].p1 && PR == h.pivot[1].p2)) {
            if (Utils.debugFlag && maxx-- == 0) break;
            if (Utils.debugFlag && maxx == 2)
                System.out.println("dbg");
            Litr.reduceCell(e);
            Ritr.reduceCell(e);
            DirectedEdge EL = Litr.de;
            DirectedEdge ER = Ritr.de;
            Point IL = EL.infinite ? (e.o2) : getLinesIntersectionPoint(e, EL.e);
            Point IR = ER.infinite ? (e.o2) : getLinesIntersectionPoint(e, ER.e);
            if (Utils.debugFlag) paper.addLine(new Line2D(e.o1, e.o2), Color.orange);
            if (Utils.debugFlag && IL != null)
                paper.addPoint(IL, Color.ORANGE, 4);
            if (Utils.debugFlag && IR != null)
                paper.addPoint(IR, Color.BLACK, 4);
            if (Utils.debugFlag && maxx == 0) return d;
            double dd1 = e.getDistToOrigin(IL);
            double dd2 = e.getDistToOrigin(IR);
            if (e.getDistToOrigin(IL) < e.getDistToOrigin(IR)) {
                e.o2 = IL;
                if (crossDen(-e.B, e.A, -EL.e.B, EL.e.A) > 0) EL.e.o2 = IL;
                else EL.e.o1 = IL;
                DirectedEdge[] pair = DirectedEdge.getPairFromEdge(e);
                Litr.insert(pair[0], true, false);
                Ritr.insert(pair[1], true, true);
                PL = EL.e.getOpposite(PL);
                Litr = d1.siteEdge.get(PL);
                Litr.setDirAndResetToEdge(true, EL.opposite);
                Ritr.setOriginEdge(Ritr.de);
                e = new Edge(PL, PR, IL);
            } else {
                e.o2 = IR;
                if (crossDen(-e.B, e.A, -ER.e.B, ER.e.A) > 0) ER.e.o1 = IR;
                else ER.e.o2 = IR;
                DirectedEdge[] pair = DirectedEdge.getPairFromEdge(e);
                Litr.insert(pair[0], true, true);
                Ritr.insert(pair[1], true, false);
                PR = ER.e.getOpposite(PR);
                Ritr = d2.siteEdge.get(PR);
                Ritr.setDirAndResetToEdge(false, ER.opposite);
                Litr.setOriginEdge(Litr.de);
                e = new Edge(PL, PR, IR);
            }
        }
        DirectedEdge[] pair = DirectedEdge.getPairFromEdge(e);
        if (Utils.debugFlag) paper.addLine(new Line2D(e.o1, e.o2), Color.orange);
        if (Utils.debugFlag)
            System.out.println("dbg");
        Litr.reduceCell(e);
        Ritr.reduceCell(e);
        Litr.insert(pair[0], true, true);
        Ritr.insert(pair[1], true, true);
        return d;
    }

    void draw(Canvas pap) {
        int pn=0;
        for (Map.Entry<Point, CellIterator> siteCell : siteEdge.entrySet()) {
            pn++;
            System.out.print(pn+". "+siteCell.getKey()+siteCell.getValue().print());
//            if (pn++==4) continue;
        //    if (pn++==4)
          //      System.out.println("zz");
            // TODO debug case when contour does not removed
            pap.addPoint(siteCell.getKey(), Color.RED, 8);
            CellIterator itr = siteCell.getValue();
            int num = 4;
            if (itr.tryFindInfiniteEdge()) num++;
//            System.out.println(num);
            DirectedEdge n0 = itr.de;
            DirectedEdge n = n0;
            do {
                if (!n.infinite)
                    pap.addLine(n.e.toLine2DAim(siteCell.getKey()), n.fwd ? Color.blue : Color.red);
                n = n.next;
            } while (n != n0 && --num > 0);
        }
        pap.repaint();
    }

}
