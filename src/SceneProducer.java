import core.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SceneProducer {

    static final int scenesNum = 5;

    static List<Point> getScene(int scene) {
        List<Point> pts = new ArrayList<>();
        int i0 = new Random().nextInt(100000);
        //System.out.println("Seed: " + i0);
        Random r = new Random(i0);
        switch (scene) {

            //square grid
            case 1: {
                int n = 40;
                for (int i = 0; i < n * n; i++)
                    pts.add(new Point(5.0 * (-n / 2 + i % n) / n, 5.0 * (-n / 2 + i / n) / n));
                break;
            }

            //randomized square grid
            case 2: {
                int n = 50;
                for (int i = 0; i < n * n; i++)
                    if (r.nextInt(100) > 30)
                        pts.add(new Point(5.0 * (-n / 2 + i % n) / n, 5.0 * (-n / 2 + i / n) / n));
                break;
            }

            //whirlpool
            case 3: {
                int n = 800;
                double r0 = 2.6;
                double an = 0;
                for (int i = 0; i < n; i++) {
                    pts.add(new Point(r0 * Math.cos(an), r0 * Math.sin(an)));
                    an = an + 0.1 * r0;
                    r0 = r0 * 0.9985;
                }
                pts.add(new Point(0, 0));
                break;
            }

            // large randomized square grid
            case 4: {
                int n = 100;
                for (int i = 0; i < n * n; i++)
                    if (r.nextInt(100) > 50)
                        pts.add(new Point(5.0 * (-n / 2 + i % n) / n, 5.0 * (-n / 2 + i / n) / n));
                break;
            }

            //random points
            case 5: {
                int n = 500;
                for (int i = 0; i < n; i++) pts.add(new Point(-2 + r.nextFloat() * 4, -2 + r.nextFloat() * 4));
                break;
            }

            default: {
                int n = 2;
                double r0 = 0.24;
                for (int z = 0; z < 10; z++) {
                    r0 *= 1.2;
                    double an = 0;
                    for (int i = 0; i < n; i++) {
                        an += Math.PI * 2 / n;
                        if (z < 6 || (r.nextInt(100) > 60+(z-6)*10)) pts.add(new Point(r0 * Math.cos(an), r0 * Math.sin(an)));
                    }
                    n = n * 2;
                }
                pts.add(new Point(0, 0));
                break;
            }

        }
        return pts;
    }

}
