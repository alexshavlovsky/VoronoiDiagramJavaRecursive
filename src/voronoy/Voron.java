package voronoy;

import geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Voron {

    public static void main(String[] args) {
        /*Point org=new Point(2,0);
        Point p1=new Point(3,-1);
        Point p2=new Point(3,1);
        Point p=new Point(1,0);

        Edge e = new Edge(p1,p2,org);
        e.drawVector();
        paper.addPoint(org, Color.BLUE,5);
        paper.addPoint(p1, Color.RED,5);
        paper.addPoint(p2, Color.RED,5);
        System.out.println(e.getDistToOrigin(p));*/
        int z=2200;
        while (true) {
            Random r = new Random(z);
            int n = 5;
            List<Point> pts = new ArrayList<>();

            for (int i = 0; i < n; i++) pts.add(new Point(-4 + r.nextFloat() * 8, -4 + r.nextFloat() * 8));

   //     System.out.println(
//                pts.stream().map(p -> 100+(int) (p.x*10) + "," + (600 - (int) (p.y*10))).collect(Collectors.joining(",", "{\"sites\":[", "],\"queries\":[]}")));



     //   pts.forEach(t->paper.addPoint(t, Color.RED, 8));
            Diagram d = new Diagram(pts,z);
            z++;
        }
      //  d.draw(paper);
    }

}