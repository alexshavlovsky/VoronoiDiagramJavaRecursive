package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class AnimationWindow extends JFrame implements KeyListener {

    public static class DrawableModel {
        final Collection<Drawable> drawables;
        final List<String> text;

        public DrawableModel(Collection<Drawable> drawables, List<String> text) {
            this.drawables = drawables;
            this.text = text;
        }
    }

    private final AffineTransform transform;

    private final DrawingPanel mainPanel = new DrawingPanel();

    private DrawableModel drawableModel = new DrawableModel(new ArrayList<>(), new ArrayList<>());
    private final Function<AnimationWindow, DrawableModel> updatesSupplier;

    public boolean animationPaused = false;
    private boolean showHeadsUpDisplay = true;

    public boolean isShowTraceRay() {
        return showTraceRay;
    }

    public boolean isDrawOpenedCells() {
        return drawOpenedCells;
    }

    private boolean showTraceRay = true;
    private boolean drawOpenedCells = true;

    public double time = 0;
    private int FRAME_RATE = 25;

    public static void newInstance(Function<AnimationWindow, DrawableModel> updatesSupplier, Dimension windowDimension, AffineTransform transform) {
        SwingUtilities.invokeLater(() -> new AnimationWindow(updatesSupplier, windowDimension, transform));
    }

    private AnimationWindow(Function<AnimationWindow, DrawableModel> updatesSupplier, Dimension windowDimension, AffineTransform transform) {
        this.updatesSupplier = updatesSupplier;
        this.transform = transform;

        this.setTitle("Animation window");
        this.setSize(windowDimension);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.addKeyListener(this);
//        mainPanel.setBackground(new Color(64, 128, 192));
        this.add(mainPanel);
        this.setVisible(true);

        Timer timer = new Timer(1000 / FRAME_RATE, new Interval());
        timer.start();
    }

    private class Interval implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (animationPaused) return;
            time = time + 1.0 / FRAME_RATE;
            drawableModel = updatesSupplier.apply(AnimationWindow.this);
            mainPanel.repaint();
        }
    }

    private class DrawingPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            AffineTransform save = g2d.getTransform();
            g2d.setTransform(transform);

            drawableModel.drawables.forEach(drawable -> drawable.draw(g2d));

            g2d.setTransform(save);

            if (showHeadsUpDisplay) {
                g.setColor(Color.BLACK);
                int z = 10;
                g.drawString("'H' - hide this, 'SPACE' - pause", 15, z += 15);
                g.drawString("'R' - show a trace ray, 'O' - draw opened cells", 15, z += 15);
                g.drawString("Time: " + String.format("%.1f", time), 15, z += 15);
                g.drawString("Paused: " + animationPaused, 15, z += 15);
                for (String txt : drawableModel.text) g.drawString(txt, 15, z += 15);
            }

            g2d.dispose();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                animationPaused = !animationPaused;
                break;
            case KeyEvent.VK_H:
                showHeadsUpDisplay = !showHeadsUpDisplay;
                break;
            case KeyEvent.VK_R:
                showTraceRay = !showTraceRay;
                break;
            case KeyEvent.VK_O:
                drawOpenedCells = !drawOpenedCells;
                break;
        }
        mainPanel.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
