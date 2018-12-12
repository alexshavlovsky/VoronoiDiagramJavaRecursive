package gui;

import geometry.Line2D;
import geometry.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


public class Canvas extends JComponent {
    final static BasicStroke stroke = new BasicStroke(0.7f);
    final static BasicStroke boldStroke = new BasicStroke(2.0f);
    boolean antialiasing;
    public boolean closed = false;
    final public ArrayList<Drawable> figures = new ArrayList<>();
    private double x0, y0, z;

    public void setZ(double z) {
        this.z = z;
    }

    public void addText(String s, int x, int y, Color color) {
        figures.add(new DrawableText(s, x, y, color));
    }

    public void addLine(Line2D l, Color c, boolean bold) {
        if (l == null) return;
        figures.add(new DrawableLine(l.p1, l.p2, c, bold));
    }

    public void addPoint(Point p, Color c, int s) {
        if (p == null) return;
        figures.add(new DrawablePoint(p, c, s));
    }

    public void clear() {
        figures.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        synchronized (figures) {
            for (Drawable f : figures) f.Draw(g, this);
        }
    }

    public Canvas(int x, int y, double z, boolean antialiasing) {
        this.antialiasing = antialiasing;
        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                closed = true;
            }
        };
        this.x0 = x / 2.0;
        this.y0 = y / 2.0;
        this.z = z;
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(x, y));
        testFrame.getContentPane().add(this, BorderLayout.CENTER);
        testFrame.pack();
        testFrame.addWindowListener(windowAdapter);
        testFrame.setVisible(true);
    }

    int transformX(double x) {
        return (int) (x0 + x * z);
    }

    int transformY(double y) {
        return (int) (y0 - y * z);
    }

}
