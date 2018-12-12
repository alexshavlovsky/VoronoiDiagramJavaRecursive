package gui;

import java.awt.*;

public class DrawableText implements Drawable {
    private String s;
    private int x, y;
    private Color color;

    public DrawableText(String s, int x, int y, Color color) {
        this.s = s;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void Draw(Graphics g, Canvas c) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        if (c.antialiasing) g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(Canvas.stroke);
        g.drawString(s, x, y);
    }

}
