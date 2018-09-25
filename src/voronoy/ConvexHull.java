package voronoy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class ConvexHull {
    List<Point2D> u,d;
    Line2D pivotU, pivotD;
    int mu,md;

    @Override
    public String toString() {
        return "Hull:\nUp = " + u +"\n"+pivotU+ "\nDn = " + d+"\n"+pivotD;
    }

    ConvexHull(List<Point2D> s) {
        u = new ArrayList<>();
        d = new ArrayList<>();
        //s.sort((p1, p2) -> p1.x != p2.x ? Double.compare(p1.x, p2.x) : Double.compare(p1.y, p2.y));
        s.forEach(this::putPoint);
    }

    ConvexHull(List<Point2D> s, int i1, int i2) {
        u = new ArrayList<>();
        d = new ArrayList<>();
        IntStream.range(i1, i2).mapToObj(s::get).forEach(this::putPoint);
    }

    void consolidate(ConvexHull ch2) {
        mu = u.size();
        md = d.size();
        ch2.u.forEach(this::putPointU);
        ch2.d.forEach(this::putPointD);
        if (mu>0&&mu<u.size()) pivotU = new Line2D(u.get(mu - 1), u.get(mu));
        if (md>0&&md<d.size()) pivotD = new Line2D(d.get(md - 1), d.get(md));
    }

    static private double getArea(Point2D p1, Point2D p2, Point2D p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    private void putPointU(Point2D p) {
        while (u.size() > 1 && getArea(u.get(u.size() - 2), u.get(u.size() - 1), p)>=0) u.remove(u.size() - 1);
        if (u.size()<mu) mu=u.size();
        u.add(p);
    }

    private void putPointD(Point2D p) {
        while (d.size() > 1 && getArea(d.get(d.size() - 2), d.get(d.size() - 1), p)<=0) d.remove(d.size() - 1);
        if (d.size()<md) md=d.size();
        d.add(p);
    }

    private void putPoint(Point2D p) {
        putPointU(p);
        putPointD(p);
    }

    List<Point2D> asList() {
        List<Point2D> r = new ArrayList<>();
        u.stream().limit(u.size() - 1).forEach(r::add);
        int s = d.size();
        IntStream.range(1, s).map(i -> s - i).mapToObj(d::get).forEach(r::add);
        return r;
    }

}
