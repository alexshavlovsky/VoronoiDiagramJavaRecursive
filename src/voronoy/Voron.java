package voronoy;

import geometry.Point;
import geometry.Utils;
import gui.Canvas;

import java.util.Arrays;
import java.util.stream.Collectors;


public class Voron {

    public static void main(String[] args) {
        Canvas paper = new Canvas(900, 675, 50);

        Diagram d= null;
        Point[] p = { new Point(0.0, -2.0), new Point(-2.0, 0.0), new Point(0.0, 2.00001),new Point(2.0, 0.0)};
//       Point[] p = { new Point(0.0, -2.0), new Point(2.0, 0.0), new Point(-2.0, 0.0), new Point(0.0, 2.0)};
//        Point[] p = { new Point(0.0, 0.0), new Point(2.0, 0.0), new Point(-2.0, 0.0), new Point(0.0, 2.0), new Point(0.0, -2.0) };
//        Point[] p = { new Point(2.0, 1.0), new Point(2.0, -1.0), new Point(4.4, 2.2), new Point(4.4, -2.2), new Point(-0.4, 2.2), new Point(-0.4, -2.2) };

        for (Point point : Arrays.stream(p).sorted(Utils::comparePointXY).collect(Collectors.toList())) {
            if (d == null) d = new Diagram(point);
            else d = Diagram.mergeDiagrams(d, new Diagram(point), null);
        }

        System.out.println(d);
        d.draw(paper);


/*        Diagram d = new Diagram(145, 475);
        Diagram d2 = new Diagram(248, 209);
        Diagram d3 = new Diagram(400, 300);

        Diagram d4 = new Diagram(500, 500);
        Diagram d5 = new Diagram(540, 100);
        Diagram d6 = new Diagram(543,140);
        Diagram d7 = new Diagram(550,170);
        Diagram d8 = new Diagram(585,189);
        Diagram d9 = new Diagram(590,195);
        Diagram d10 = new Diagram(610,200);

        d = mergeDiagrams(d, d2, null);
        d = mergeDiagrams(d, d3, null);

       d4 = mergeDiagrams(d4, d5, null);
        d4 = mergeDiagrams(d4, d6, null);
        d4 = mergeDiagrams(d4, d7, null);
        d4 = mergeDiagrams(d4, d8, null);
        d4 = mergeDiagrams(d4, d9, null);
        d4 = mergeDiagrams(d4, d10, null);
        d4 = mergeDiagrams(d4, new Diagram(615,140), null);

        d=mergeDiagrams(d,d4,null);
        System.out.println(d);
        d.draw(paper);
        System.out.println(d.exportSites());*/
    }

}