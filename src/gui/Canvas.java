package gui;

import geometry.Line2D;
import geometry.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.stream.IntStream;


public class Canvas extends JComponent {

    public boolean closed=false;
    private final ArrayList<Drawable> figures = new ArrayList<>();
    double x0,y0,z;

    public void addListNotClosed(java.util.List<geometry.Point> l, Color color) {
        IntStream.range(0, l.size() - 1).forEach(i -> addLine(new Line2D(l.get(i), l.get(i + 1)), color));
    }

    public void addListClosed(java.util.List<Point> l, Color color) {
        IntStream.range(0, l.size()).forEach(i -> addLine(new Line2D(l.get(i), l.get((i + 1) % l.size())), color));
    }

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

    public int transformX(double x){
        return (int)(x0+x*z);
    }

    public int transformY(double y){
        return (int)(y0-y*z);
    }

    /*public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final gui.Canvas comp = new gui.Canvas();
        comp.setPreferredSize(new Dimension(320, 200));
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        JButton newLineButton = new JButton("New DrawableLine");
        JButton clearButton = new JButton("Clear");
        buttonsPanel.add(newLineButton);
        buttonsPanel.add(clearButton);
        testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        newLineButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent de) {
                int x1 = (int) (Math.random()*320);
                int x2 = (int) (Math.random()*320);
                int y1 = (int) (Math.random()*200);
                int y2 = (int) (Math.random()*200);
                Color randomColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
                comp.addLine(x1, y1, x2, y2, randomColor);
            }
        });
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent de) {
                comp.clearLines();
            }
        });

        testFrame.pack();
        testFrame.setVisible(true);
    }*/

}