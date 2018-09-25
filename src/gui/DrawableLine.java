package gui;

import voronoy.Line2D;
import voronoy.Point2D;

import java.awt.*;

class DrawableLine extends Line2D implements Drawable{
    Color color;

    DrawableLine(Point2D p1, Point2D p2, Color color) {
        super(p1,p2);
        this.color = color;
    }

    @Override
    public void Draw(Graphics g, Canvas c) {
        g.setColor(color);
        g.drawLine((int) (p1.x * c.scale), c.yMax - (int) (p1.y * c.scale), (int) (p2.x * c.scale), c.yMax - (int) (p2.y * c.scale));
    }
}
