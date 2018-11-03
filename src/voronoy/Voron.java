package voronoy;

import geometry.Point;
import geometry.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static geometry.Utils.paper;

public class Voron {

    public static void main(String[] args) {


      /*  while (!paper.closed) {
            List<Point> pts = new ArrayList<>();
            Random r0 = new Random();
            int i0=404869835;
            Random r = new Random(i0);
            int n = 5;

            for (int i = 0; i < n; i++) pts.add(new Point(-4 + r.nextFloat() * 8, -4 + r.nextFloat() * 8));

            Diagram d = null;
            try {
                d = new Diagram(Utils.rotatePoints(pts, new Point(0, 0), 0));
            } catch (Exception e) {
                System.out.println("merging error: "+i0);
            }
        }*/

        //     System.out.println(
//                pts.stream().map(p -> 100+(int) (p.x*10) + "," + (600 - (int) (p.y*10))).collect(Collectors.joining(",", "{\"sites\":[", "],\"queries\":[]}")));

        List<Point> pts = new ArrayList<>();
        Random r0 = new Random();
        int i0=404869835;
        Random r = new Random(i0);
        int n = 5;

        for (int i = 0; i < n; i++) pts.add(new Point(-4 + r.nextFloat() * 8, -4 + r.nextFloat() * 8));


       double a=3.30;
        int m=0;
        while (!paper.closed) {

            Diagram d = null;
            try {
                d = new Diagram(Utils.rotatePoints(pts, new Point(0, 0), a));
            } catch (Exception e) {
                m++;
                System.out.println("merging error: "+m+"/"+a+"="+m/a);
            }
            if (d!=null) {
                paper.clear();
                d.draw(paper);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
             a = a + 0.01;

        }
    }

}