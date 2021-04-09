package gui;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class DLine extends Line2D.Double implements Drawable {

    private final Color color;
    private final Stroke stroke;

    public DLine(Point2D.Double p1, Point2D.Double p2, Color color, Stroke stroke) {
        super(p1, p2);
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
