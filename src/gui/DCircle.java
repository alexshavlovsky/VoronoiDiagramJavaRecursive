package gui;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class DCircle extends Ellipse2D.Double implements Drawable {

    private final Color color;
    private final Stroke stroke;

    public DCircle(double x, double y, double r, Color color, Stroke stroke) {
        super(x - r / 2, y - r / 2, r, r);
        this.color = color;
        this.stroke = stroke;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(stroke);
        g.draw(this);
    }

}
