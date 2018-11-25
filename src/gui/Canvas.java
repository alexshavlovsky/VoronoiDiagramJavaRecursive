package gui;

import geometry.Line2D;
import geometry.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


public class Canvas extends JComponent {

    public boolean closed = false;
    private final ArrayList<Drawable> figures = new ArrayList<>();
    private double x0, y0, z;


    public void addLine(Line2D l, Color c) {
        if (l == null) return;
        figures.add(new DrawableLine(l.p1, l.p2, c));
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
        for (Drawable f : figures) f.Draw(g, this);

    }

    public Canvas(int x, int y, double z) {
        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                closed = true;
            }
        };
        this.x0 = x / 2;
        this.y0 = y / 2;
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