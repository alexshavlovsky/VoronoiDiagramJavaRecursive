package voronoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ConvexHull {
    ArrayList<Point2D>[] halves;
    Line2D[] pivot = new Line2D[2];

    @Override
    public String toString() {
        return "ConvexHull{" +
                "halves=" + Arrays.toString(halves) +
                ", pivot=" + Arrays.toString(pivot) +
                '}';
    }

    ConvexHull(List<Point2D> s) {
        halves = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
        //s.sort((p1, p2) -> p1.x != p2.x ? Double.compare(p1.x, p2.x) : Double.compare(p1.y, p2.y));
        for (Point2D p : s) for (int i = 0; i < 2; i++) putPointHalf(p, null, i);
    }

    ConvexHull(ConvexHull ch) {
        halves = new ArrayList[]{new ArrayList<>(ch.halves[0]), new ArrayList<>(ch.halves[1])};
    }

/*    ConvexHull(List<Point2D> s, int i1, int i2) {
        u = new ArrayList<>();
        d = new ArrayList<>();
        IntStream.range(i1, i2).mapToObj(s::get).forEach(this::putPoint);
    }*/


    static ConvexHull mergeHulls(ConvexHull ch1, ConvexHull ch2) {
        ConvexHull ch = new ConvexHull(ch1);
        int[] m = new int[]{ch.halves[0].size(), ch.halves[1].size()};
        for (int i = 0; i < 2; i++) {
            for (Point2D p : ch2.halves[i]) ch.putPointHalf(p, m, i);
            if (m[i] > 0 && m[i] < ch.halves[i].size())
                ch.pivot[i] = new Line2D(ch.halves[i].get(m[i] - 1), ch.halves[i].get(m[i]));
        }
        return ch;
    }

    static private double getArea(Point2D p1, Point2D p2, Point2D p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    static boolean cmpArea(double d, int half) {
        return half == 0 ? d >= 0 : d <= 0;
    }

    private void putPointHalf(Point2D p, int[] m, int i) {
        while (halves[i].size() > 1 && cmpArea(getArea(halves[i].get(halves[i].size() - 2), halves[i].get(halves[i].size() - 1), p), i))
            halves[i].remove(halves[i].size() - 1);
        if (m != null && halves[i].size() < m[i]) m[i] = halves[i].size();
        halves[i].add(p);
    }

  /*  List<Point2D> asList() {
        List<Point2D> r = new ArrayList<>();
        u.stream().limit(u.size() - 1).forEach(r::add);
        int s = d.size();
        IntStream.range(1, s).map(i -> s - i).mapToObj(d::get).forEach(r::add);
        return r;
    }*/

}
