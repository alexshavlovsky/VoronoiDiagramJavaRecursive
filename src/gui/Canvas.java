package gui;

import geometry.Line2D;
import geometry.Point2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;


public class Canvas extends JComponent {

    private final ArrayList<Drawable> figures = new ArrayList<>();
    double scale;
    int yMax;

    public void addListNotClosed(java.util.List<Point2D> l, Color color) {
        IntStream.range(0, l.size() - 1).forEach(i -> addLine(new Line2D(l.get(i), l.get(i + 1)), color));
    }

    public void addListClosed(java.util.List<Point2D> l, Color color) {
        IntStream.range(0, l.size()).forEach(i -> addLine(new Line2D(l.get(i), l.get((i + 1) % l.size())), color));
    }

    public void addLine(Line2D l, Color c) {
        if (l == null) return;
        figures.add(new DrawableLine(l.p1, l.p2, c));
        repaint();
    }

    public void addPoint(Point2D p, Color c, int s) {
        if (p == null) return;
        figures.add(new DrawablePoint(p, c, s));
        repaint();
    }

    public void clear() {
        figures.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Drawable f : figures) f.Draw(g, this);
    }

    public Canvas(int x, int y, double scale) {
        this.scale = scale;
        this.yMax = y - 1;
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(x, y));
        testFrame.getContentPane().add(this, BorderLayout.CENTER);
        testFrame.pack();
        testFrame.setVisible(true);
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
            public void actionPerformed(ActionEvent e) {
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
            public void actionPerformed(ActionEvent e) {
                comp.clearLines();
            }
        });

        testFrame.pack();
        testFrame.setVisible(true);
    }*/

}