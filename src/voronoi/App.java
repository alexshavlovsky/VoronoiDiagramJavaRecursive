package voronoi;

import geometry.Point;
import geometry.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static geometry.Utils.paper;

public class App {

    public static void main(String[] args) {


//        List<Point> pts = new ArrayList<>();
//        int i0 = new Random().nextInt(100000);
//        System.out.println("Seed: " + i0);
//        Random r = new Random(i0);
//        int n = 50;
//        for (int i = 0; i < n; i++) pts.add(new Point(-2 + r.nextFloat() * 4, -2 + r.nextFloat() * 4));

//        List<Point> pts = new ArrayList<>();
//        int i0 = new Random().nextInt(100000);
//        System.out.println("Seed: " + i0);
//        Random r = new Random(i0);
//        int n = 10;
//        for (int i = 0; i < n*n; i++) if (r.nextInt(100)>30)pts.add(new Point(5.0*(-n/2+i%n)/n, 5.0*(-n/2+i/n)/n));


        List<Point> pts = new ArrayList<>();
        int i0 = new Random().nextInt(100000);
        System.out.println("Seed: " + i0);
        Random r = new Random(i0);
        int n = 3;
        for (int i = 0; i < n*n; i++) /*if (r.nextInt(100)>30)*/pts.add(new Point((i%n), (i/n)-1));

        pts.sort(Utils::comparePointXY);
        List<Point> pts2 = new ArrayList<>();
        pts2.add(pts.get(4));
        pts2.add(pts.get(7));

        pts.remove(8);
        pts.remove(7);//
        pts.remove(5);
        pts.remove(4);//
        pts.remove(2);
        pts.remove(1);


        List<Point> pts1 = new ArrayList<>(Utils.rotatePoints(pts, new Point(0, 0), 1.57));
        List<Point> pts3 = new ArrayList<>(Utils.rotatePoints(pts2, new Point(0, 0), 1.57));
        Diagram d2=new Diagram(pts1);
        Diagram d3=new Diagram(pts3);
        Diagram.traceRay=true;
//        Diagram.dbg=true;
        Diagram d4=Diagram.mergeDiagrams(d3,d2);
        System.out.println(d4);
        d4.draw(paper,true);




        //List<Point> pts = Arrays.asList(new Point(0.0,0.0), new Point(0.0,1.0), new Point(0.0,2.0), new Point(0.0,3.0), new Point(0.0,4.0), new Point(0.0,5.0), new Point(0.0,6.0), new Point(0.0,7.0), new Point(0.0,8.0), new Point(1.0,0.0), new Point(1.0,1.0), new Point(1.0,2.0), new Point(1.0,3.0), new Point(1.0,4.0), new Point(1.0,5.0), new Point(1.0,6.0), new Point(1.0,7.0), new Point(1.0,8.0), new Point(2.0,0.0), new Point(2.0,1.0), new Point(2.0,2.0), new Point(2.0,3.0), new Point(2.0,4.0), new Point(2.0,5.0), new Point(2.0,6.0), new Point(2.0,7.0), new Point(2.0,8.0), new Point(3.0,0.0), new Point(3.0,1.0), new Point(3.0,2.0), new Point(3.0,3.0), new Point(3.0,4.0), new Point(3.0,5.0), new Point(3.0,6.0), new Point(3.0,7.0), new Point(3.0,8.0), new Point(4.0,0.0), new Point(4.0,1.0), new Point(4.0,2.0), new Point(4.0,3.0), new Point(4.0,4.0), new Point(4.0,5.0), new Point(4.0,6.0), new Point(4.0,7.0), new Point(4.0,8.0));

//        List<Point> pts = new ArrayList<>();
//        int n = 600;
//        double r0=2.8;
//        double an=0;
//        for (int i = 0; i < n; i++) {
//            pts.add(new Point(r0*Math.cos(an), r0*Math.sin(an)));
//            an=an+0.09;
//            r0=r0-0.003;
//        }
//        pts.add(new Point(0,0));


//        List<Point> pts = new ArrayList<>();
//        int i0 = new Random().nextInt(100000);
//        System.out.println("Seed: " + i0);
//        Random r = new Random(i0);
//        int n = 2;
//        for (int i = 0; i < n*n+1; i++) /*if (r.nextInt(100)>40)*/pts.add(new Point(5.0*(-n/2+i%n)/n, 5.0*(-n/2+i/n)/n));

        //                                                                                    0.91 norm
//        List<Point> pts1 = new ArrayList<>(Utils.rotatePoints(pts, new Point(0, 0), 0.9));
//        pts1.sort(Utils::comparePointXY);
//        Diagram d2=new Diagram(pts1.get(pts1.size()-1));
//        pts1.remove(pts1.size()-1);
//        Diagram dd = new Diagram(pts1);
//        Diagram.dbg=true;
//        dd = Diagram.mergeDiagrams(dd,d2);
//        dd.draw(paper);
//        System.out.println(dd);




//        double a = 0;
//        int m = 0;
//        while (!paper.closed) {
//            try {
//                Diagram dd = new Diagram(Utils.rotatePoints(pts, new Point(0, 0), a));
//                paper.clear();
//                dd.draw(paper);
//            } catch (Exception e) {
//                System.out.println(++m + ": " + e);
//                e.printStackTrace();
//            }
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            a = a + 0.02;
//            paper.z*=1.001;
//        }

    }

}