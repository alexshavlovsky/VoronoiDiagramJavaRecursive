package voronoi;

import geometry.Point;
import geometry.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static geometry.Utils.paper;

public class App {

    public static void main(String[] args) throws Exception {
        List<Point> pts = new ArrayList<>();
        int i0 = new Random().nextInt(100000);
        System.out.println("Seed: " + i0);
        Random r = new Random(i0);
        int n = 100;
        for (int i = 0; i < n; i++) pts.add(new Point(-4 + r.nextFloat() * 8, -4 + r.nextFloat() * 8));

/*        double r0=8;
        double an=0;
        for (int i = 0; i < n; i++) {
            pts.add(new Point(r0*Math.cos(an), r0*Math.sin(an)));
            an=an+0.082;
            r0=r0-0.004;
        }
        pts.add(new Point(0.2,0));*/

        double a = 0;
        int m = 0;
        while (!paper.closed) {
            try {
                Diagram d = new Diagram(Utils.rotatePoints(pts, new Point(0, 0), a));
                paper.clear();
                d.draw(paper);
            } catch (Exception e) {
                System.out.println(++m + ": " + e);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            a = a + 0.02;

        }

    }

}