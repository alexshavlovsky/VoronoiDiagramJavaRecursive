package gui;

import geometry.Point2D;

import java.awt.*;

class DrawablePoint extends Point2D implements Drawable {
    Color color;
    int size;

    DrawablePoint(Point2D p, Color color, int size) {
        super(p.x, p.y);
        this.color = color;
        this.size = size;
    }

    @Override
    public void Draw(Graphics g, Canvas c) {
        g.setColor(color);
        g.drawOval((int) (x * c.scale) - size / 2, c.yMax - (int) (y * c.scale) - size / 2, size, size);
    }
}
