package voronoi;

import geometry.Line2D;
import geometry.Point;

import java.util.ArrayList;
import java.util.List;

import static geometry.Utils.*;

class ConvexHull {
    private ArrayList<Point>[] halves;
    Line2D[] pivot = new Line2D[2];

    ConvexHull(List<Point> s) {
        halves = new ArrayList[]{new ArrayList(), new ArrayList()};
        s.forEach(this::putPoint);
    }

    static ConvexHull mergeHulls(ConvexHull ch, ConvexHull ch2) {
        int[] m = new int[]{ch.halves[0].size(), ch.halves[1].size()};
        for (int i = 0; i < 2; i++) {
            for (Point p : ch2.halves[i]) ch.putPointHalf(p, m, i);
            if (m[i] > 0 && m[i] < ch.halves[i].size())
                ch.pivot[i] = new Line2D(ch.halves[i].get(m[i] - 1), ch.halves[i].get(m[i]));
        }
        return ch;
    }

    private static boolean cmpArea(double d, int half) {
        return half == 0 ? cmpGE(d, 0) : cmpLE(d, 0);
    }

    private void putPointHalf(Point p, int[] m, int i) {
        while (halves[i].size() > 1 && cmpArea(getArea(halves[i].get(halves[i].size() - 2), halves[i].get(halves[i].size() - 1), p), i))
            halves[i].remove(halves[i].size() - 1);
        if (m != null && halves[i].size() < m[i]) m[i] = halves[i].size();
        halves[i].add(p);
    }

    private void putPoint(Point p) {
        for (int i = 0; i < 2; i++) putPointHalf(p, null, i);
    }

}
