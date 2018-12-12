package voronoi;

import geometry.Point;
import geometry.Utils;
import gui.Canvas;

import java.util.List;

public class App {

    public static void main(String[] args) {

        Canvas paper = new Canvas(1024, 800, 120, true);
        List<Point> pts = SceneProducer.getScene(0);

        double angle = 0;
        int errorCounter = 0;
        int frameCounter = 0;
        int sceneId = 0;
        double calcT = 0;
        while (!paper.closed) {
            if (++frameCounter % 200 == 0) {
                sceneId = (sceneId + 1) % SceneProducer.scenesNum;
                pts = SceneProducer.getScene(sceneId);
            }
            try {
                long t0 = System.nanoTime();
                Diagram dd = new Diagram(Utils.rotatePoints(pts, new Point(0, 0), angle));
                long t1 = System.nanoTime();
                calcT = calcT * 0.98 + (t1 - t0) * 0.02;
                dd.draw(paper, true, true,String.format("points: %d, time: %.2f ms", pts.size(), calcT / 10e6));
            } catch (Exception e) {
                System.out.println(++errorCounter + ": " + e);
                e.printStackTrace();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            angle = angle + 0.02 * Math.sin(-0.2 + 0.005 * frameCounter);
            paper.setZ(200 + 100 * Math.cos(0.003 * frameCounter));
        }

    }

}
