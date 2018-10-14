package gui;

import geometry.Line2D;
import geometry.Point;

import java.awt.*;

class DrawableLine extends Line2D implements Drawable{
    Color color;

    DrawableLine(Point p1, Point p2, Color color) {
        super(p1,p2);
        this.color = color;
    }

    @Override
    public void Draw(Graphics g, Canvas c) {
        g.setColor(color);
        g.drawLine(c.transformX(p1.x), c.transformY(p1.y), c.transformX(p2.x), c.transformY(p2.y));
    }
}
