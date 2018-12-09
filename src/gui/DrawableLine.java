package gui;

import geometry.Line2D;
import geometry.Point;

import java.awt.*;

class DrawableLine extends Line2D implements Drawable {
    private Color color;
    boolean bold;

    DrawableLine(Point p1, Point p2, Color color, boolean bold) {
        super(p1, p2);
        this.color = color;
        this.bold = bold;
    }

    @Override
    public void Draw(Graphics g, Canvas c) {
        g.setColor(color);
//        g.drawLine(c.transformX(p1.x), c.transformY(p1.y), c.transformX(p2.x), c.transformY(p2.y));
        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(bold ? Canvas.boldStroke : Canvas.stroke);
        g2.drawLine(c.transformX(p1.x), c.transformY(p1.y), c.transformX(p2.x), c.transformY(p2.y));
    }
}
