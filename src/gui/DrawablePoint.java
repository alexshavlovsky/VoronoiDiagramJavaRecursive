package gui;

import geometry.Point;

import java.awt.*;

class DrawablePoint extends Point implements Drawable {
    private Color color;
    private int size;

    DrawablePoint(Point p, Color color, int size) {
        super(p.x, p.y);
        this.color = color;
        this.size = size;
    }

    @Override
    public void Draw(Graphics g, Canvas c) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(Canvas.stroke);
        g.drawOval(c.transformX(x) - size / 2, c.transformY(y) - size / 2, size, size);
    }
}
