package voronoy;

import geometry.Line2D;
import geometry.Point2D;
import geometry.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static geometry.Utils.*;

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
        s.sort(Utils::comparePointXY);
        s.forEach(this::putPoint);
    }

    ConvexHull(ConvexHull ch) {
        halves = new ArrayList[]{new ArrayList<>(ch.halves[0]), new ArrayList<>(ch.halves[1])};
    }

    ConvexHull(List<Point2D> s, int i1, int i2) {
        halves = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
        IntStream.range(i1, i2).mapToObj(s::get).forEach(this::putPoint);
    }

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

    private static boolean cmpArea(double d, int half) {
        return half == 0 ? cmpGE(d, 0) : cmpLE(d, 0);
    }

    private void putPointHalf(Point2D p, int[] m, int i) {
        while (halves[i].size() > 1 && cmpArea(getArea(halves[i].get(halves[i].size() - 2), halves[i].get(halves[i].size() - 1), p), i))
            halves[i].remove(halves[i].size() - 1);
        if (m != null && halves[i].size() < m[i]) m[i] = halves[i].size();
        halves[i].add(p);
    }

    private void putPoint(Point2D p) {
        for (int i = 0; i < 2; i++) putPointHalf(p, null, i);
    }

    List<Point2D> asList() {
        List<Point2D> r = new ArrayList<>();
        int s0 = halves[0].size();
        int s1 = halves[1].size();
        Stream.of(
                halves[0].stream().limit(s0 - 1),
                IntStream.range(1, s1).map(i -> s1 - i).mapToObj(halves[1]::get)
        ).flatMap(p -> p).forEach(r::add);
        return r;
    }

}
