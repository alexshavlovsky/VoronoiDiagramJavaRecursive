import core.geometry.Point;
import core.geometry.Utils;
import core.voronoi.Diagram;
import gui.AnimationWindow;
import gui.Drawable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class AppFacade {

    static class SceneState {
        double angle = 0;
        double zoom = 1;
        int errorCounter = 0;
        int frameCounter = 0;
        int sceneId = 0;
        double calcT = 0;
        List<Point> pts = SceneProducer.getScene(0);
    }

    private static SceneState state = new SceneState();

    private static void handleSceneSwitch() {
        if (++state.frameCounter % 200 == 0) {
            state.sceneId = (state.sceneId + 1) % SceneProducer.scenesNum;
            state.pts = SceneProducer.getScene(state.sceneId);
        }
    }

    private static void benchUpdate(long t0) {
        state.calcT = state.calcT * 0.98 + (System.nanoTime() - t0) * 0.02;
    }

    public static void main(String[] args) {
        int xs = 800;
        int ys = 800;
        Dimension dim = new Dimension(xs, ys);
        AffineTransform transform = new AffineTransform();
        transform.translate(xs * 3 / 5, ys * 3 / 5);
        transform.scale(200, 200);

        AnimationWindow.newInstance(w -> {
            handleSceneSwitch();
            List<Drawable> drawables;
            state.zoom = 1.2 + 0.4 * Math.cos(0.003 * state.frameCounter);
            state.angle = state.angle + 0.02 * Math.sin(-0.2 + 0.005 * state.frameCounter);
            try {
                long t0 = System.nanoTime();
                Diagram dd = new Diagram(Utils.zoomPoints(
                        Utils.rotatePoints(state.pts, new Point(0, 0), state.angle),
                        state.zoom)
                );
                benchUpdate(t0);
                drawables = dd.toDrawable(w.isShowTraceRay(), w.isDrawOpenedCells());
            } catch (Exception e) {
                System.out.println(++state.errorCounter + ": " + e);
                e.printStackTrace();
                drawables = new ArrayList<>();
            }
            return new AnimationWindow.DrawableModel(drawables, List.of(String.format("points: %d, time: %.2f ms", state.pts.size(), state.calcT / 10e6)));
        }, dim, transform);
    }

}
