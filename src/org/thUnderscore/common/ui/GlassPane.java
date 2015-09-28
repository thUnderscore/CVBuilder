package org.thUnderscore.common.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;
import org.thUnderscore.common.utils.UIUtils;

/**
 * Glass pane with additional effects
 *
 * @author thUnderscore
 */
public class GlassPane extends JComponent {

    /**
     * Max ripples radius
     */
    private static final int MAX_RIPPLES_EFFECT_RADIUS = 200;
    /**
     * Ripples speed
     */
    private static final int RIPPLES_EFFECT_SPEED = 3;

    /**
     * Draw ripples on glass pane
     */
    protected class RipplesEffect {

        private final Point p;
        private final Paint color;
        private int radius;

        public RipplesEffect(Point p, Paint color) {
            this.p = p;
            this.radius = 0;
            this.color = color;
        }

        /**
         * Draw active rippless
         */
        protected void paint(Graphics g) {

            radius += Math.max(1, RIPPLES_EFFECT_SPEED
                    - RIPPLES_EFFECT_SPEED * radius / MAX_RIPPLES_EFFECT_RADIUS);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    (1 - (float) radius / MAX_RIPPLES_EFFECT_RADIUS)));
            g2d.setStroke(ripplesStroke);
            g2d.setPaint(color);
            g2d.drawOval(p.x - radius / 2 - 1,
                    p.y - radius / 2 - 1,
                    radius, radius);

            if (radius >= MAX_RIPPLES_EFFECT_RADIUS) {
                listRipplesEffect.remove(this);
            }
        }
    }

    /**
     * Ripples stroke
     */
    private final Stroke ripplesStroke = new BasicStroke(2.5f);

    /**
     * List of active ripples effects
     */
    private final List<RipplesEffect> listRipplesEffect = Collections.synchronizedList(new ArrayList<RipplesEffect>());
    /**
     * Current ripples effect to be painted
     */
    //private RipplesEffect currentEffect;
    /**
     * Timer for ripples reapainting
     */
    private final Timer timerRipplesEffect = new Timer(20,
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    synchronized (listRipplesEffect) {
                        repaint();
                        if (listRipplesEffect.isEmpty()) {
                            timerRipplesEffect.stop();
                        }
                    }

                }
            }
            );

    public GlassPane() {
        setOpaque(false);
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        synchronized (listRipplesEffect) {
            for (int i = listRipplesEffect.size() - 1; i >= 0; i--) {
                listRipplesEffect.get(i).paint(g);
            }
        }
    }

    /**
     * Start ripples under mouse position
     *
     * @param color ripples color
     */
    public void startRipplesEffect(Color color) {
        Point m = MouseInfo.getPointerInfo().getLocation();
        startRipplesEffect(m.x, m.y, color);
    }

    /**
     * Start ripples
     *
     * @param x ripples x coordinate
     * @param y ripples y coordinate
     * @param color ripples color
     */
    public void startRipplesEffect(int x, int y, Color color) {
        /*if (timerRipplesEffect.isRunning()){
         return;
         }*/
        synchronized (listRipplesEffect) {
            JFrame frame = UIUtils.getMainFrame();
            if (frame != null) {
                Point rp = frame.getRootPane().getLocationOnScreen();
                listRipplesEffect.add(new RipplesEffect(new Point(x - rp.x, y - rp.y), color));
                repaint();
                timerRipplesEffect.start();
            }

        }

    }
}
