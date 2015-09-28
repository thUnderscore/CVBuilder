package org.thUnderscore.common.ui.help;

import com.sun.awt.AWTUtilities;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.GeneralPath;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.thUnderscore.common.utils.UIUtils;

/**
 * Page help tooltip window
 */
public class HelpTooltipWindow extends JDialog {

    /**
     * Icon size;
     */
    public static final int ICON_SIZE = 25;

    /**
     * Panel for TooltipButton
     */
    protected static class TooltipButtonsPanel extends JPanel {

        {
            setOpaque(false);
        }

        public TooltipButtonsPanel() {
            super(new BorderLayout(0, 0));
        }

    }

    /**
     * Component in tootip which can process mouse click
     */
    protected static class TooltipButton extends JComponent {

        /**
         * Default shape stroke
         */
        private static Stroke stroke = new BasicStroke(2f);

        private boolean mouseOver = false;

        {
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mouseOver = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mouseOver = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    TooltipButton.this.mousePressed(e);
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Отрисовка кнопки для закрытия окна
            g2d.setStroke(stroke);
            g2d.setPaint(mouseOver ? UIUtils.OVER_COLOR : UIUtils.OUT_COLOR);
            draw(g2d);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(11, 11);
        }

        /**
         * Draw button content
         *
         * @param g2d
         */
        protected void draw(Graphics2D g2d) {
        }

        /**
         * Process mouse event
         *
         * @param e MouseEvent object
         */
        protected void mousePressed(MouseEvent e) {
        }
    }

    /**
     * Current displayed help window
     */
    private static HelpTooltipWindow dialog = null;
    /**
     * Timer for hiding\showing
     */
    private static Timer showTimer;
    /**
     * Timer for current hint component indication
     */
    private static Timer indicationTimer;
    /**
     * Current window opacity
     */
    private static float opacity;

    /**
     * Component for displaying tooltip text and icon
     */
    protected JLabel label;

    /**
     * Main panel, where background is drawed
     */
    protected JPanel contentPanel;

    /**
     * x coordinate of tooltip
     */
    protected int x;
    /**
     * y coordinate of tooltip
     */
    protected int y;

    /**
     * Hitms array
     */
    protected HelpHint[] hints;

    /**
     * Hint index to be displayed
     */
    int currentHint = -1;

    // Go to next button
    TooltipButton next;

    // Go to previuos button
    TooltipButton prev;

    /**
     * Window border shape
     */
    GeneralPath borderShape = null;

    public HelpTooltipWindow(int x, int y, HelpHint[] hints) {
        this.x = x;
        this.y = y;
        this.hints = hints == null ? new HelpHint[0] : Arrays.copyOf(hints, hints.length);
        getContentPane().setLayout(new BorderLayout(0, 0));

        label = new JLabel();
        createContentPanel();

        // Close window on focus lost
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                closeTooltipWindow(HelpTooltipWindow.this);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Use timer for smooth showing
                if (showTimer != null && showTimer.isRunning()) {
                    showTimer.stop();
                }
                opacity = 0.1f;
                showTimer = new Timer(1000 / 24, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        opacity += 0.1f;
                        if (opacity >= 1f) {
                            opacity = 1f;
                            showTimer.stop();
                        }
                        repaint();
                    }
                });
                showTimer.start();
            }
        });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent evt) {
                int keyCode = evt.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_ESCAPE:
                        closeTooltipWindow(HelpTooltipWindow.this);
                        break;
                    case KeyEvent.VK_RIGHT:
                        goToNextHint();
                        break;
                    case KeyEvent.VK_LEFT:
                        goToPrevHint();
                        break;
                }
            }
        });

        setUndecorated(true);
        

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * Create main panel, where background is drawed
     */
    protected void createContentPanel() {
        contentPanel = new JPanel() {
            /**
             * Color of background border
             */
            private final Color borderColor = new Color(157, 157, 157);
            /**
             * Top gradient point color
             */
            private final Color topColor = new Color(250, 250, 250);
            /**
             * Bottom gradient point color
             */
            private final Color bottomColor = new Color(238, 238, 238);

            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setComposite(
                        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

                if (borderShape != null) {
                    //fill background
                    g2d.setPaint(new GradientPaint(0, 10, topColor, 0, getHeight() - 1, bottomColor));
                    g2d.fill(borderShape);

                    //draw border
                    g2d.setPaint(borderColor);
                    g2d.setStroke(new BasicStroke(1.0f));
                    g2d.draw(borderShape);
                }

                super.paint(g);
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout(4, 4));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(16, 10, 5, 10));

        getContentPane().add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(label, BorderLayout.CENTER);
        contentPanel.add(
                new TooltipButtonsPanel() {
                    {
                        // Close button
                        add(new TooltipButton() {
                            @Override
                            protected void draw(Graphics2D g2d) {
                                g2d.drawLine(1, 1, getWidth() - 2, getHeight() - 2);
                                g2d.drawLine(getWidth() - 2, 1, 1, getHeight() - 2);
                            }

                            @Override
                            protected void mousePressed(MouseEvent e) {
                                closeTooltipWindow(HelpTooltipWindow.this);
                            }

                        },
                        BorderLayout.NORTH);
                    }
                },
                BorderLayout.EAST);

        contentPanel.add(
                new TooltipButtonsPanel() {
                    {
                        prev = new TooltipButton() {
                            @Override
                            protected void draw(Graphics2D g2d) {
                                g2d.drawLine(1, 5, 9, 5);
                                g2d.drawLine(5, 1, 1, 5);
                                g2d.drawLine(5, 9, 1, 5);
                            }

                            @Override
                            protected void mousePressed(MouseEvent e) {
                                goToPrevHint();
                            }
                        };

                        next = new TooltipButton() {
                            @Override
                            protected void draw(Graphics2D g2d) {
                                g2d.drawLine(1, 5, 9, 5);
                                g2d.drawLine(5, 1, 9, 5);
                                g2d.drawLine(5, 9, 9, 5);
                            }

                            @Override
                            protected void mousePressed(MouseEvent e) {
                                goToNextHint();

                            }
                        };

                        add(next, BorderLayout.EAST);

                        add(prev, BorderLayout.WEST);
                    }
                },
                BorderLayout.SOUTH);
    }

    /**
     * Display next hint if possible
     */
    private void goToNextHint() {
        if (currentHint < (hints.length - 1)) {
            setCurrentHint(currentHint + 1);
        }
    }

    /**
     * Display previous hint if possible
     */
    private void goToPrevHint() {
        if (currentHint > 0) {
            setCurrentHint(currentHint - 1);
        }
    }

    /**
     * Set current hint index
     *
     * @param currentHint hint index
     */
    protected void setCurrentHint(int currentHint) {
        this.currentHint = currentHint;
        actualize();
    }

    /**
     * Display current hint
     */
    protected void actualize() {
        boolean canDisplay = (currentHint >= 0) && (currentHint < hints.length);
        // Use timer for start ripples
        if (indicationTimer != null && indicationTimer.isRunning()) {
            indicationTimer.stop();
        }
        if (canDisplay) {
            HelpHint hint = hints[currentHint];
            Icon icon = hint.getIcon();
            label.setText(hint.getText());
            label.setIcon(icon);
            label.setBorder(BorderFactory.createEmptyBorder(0,
                    icon == null ? ICON_SIZE + 4 : 0, 0, ICON_SIZE - 10));
            final JComponent component = hint.getComponent();
            final Color color = hint.getColor();
            if (component != null) {
                indicationTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        UIUtils.startRipplesEffect(component, color);
                    }
                });
                UIUtils.startRipplesEffect(component, color);
                indicationTimer.start();

            }
        }
        next.setVisible(canDisplay && (currentHint + 1 < hints.length));
        prev.setVisible(canDisplay && (currentHint > 0));
        pack();
        borderShape = new GeneralPath();

        borderShape.moveTo(5, 15);
        borderShape.quadTo(5, 10, 10, 10);
        borderShape.lineTo(getWidth() / 2 - 10, 10);
        borderShape.lineTo(getWidth() / 2, 0);
        borderShape.lineTo(getWidth() / 2 + 10, 10);
        borderShape.lineTo(getWidth() - 11, 10);
        borderShape.quadTo(getWidth() - 6, 10, getWidth() - 6, 15);
        borderShape.lineTo(getWidth() - 6, getHeight() - 5);
        borderShape.quadTo(getWidth() - 6, getHeight() - 1, getWidth() - 11,
                getHeight() - 1);
        borderShape.lineTo(10, getHeight() - 1);
        borderShape.quadTo(5, getHeight() - 1, 5, getHeight() - 6);
        borderShape.lineTo(5, 15);

        //7
        //setShape(borderShape);
        //6
        AWTUtilities.setWindowOpaque ( this, false );
        
        //???
        //setBackground(new Color(0, 0, 0, 0));

        setLocation(x - (dialog.getWidth() / 2), y);
    }

    /**
     * Show window under conponent, centered by horizontal
     *
     * @param source component for location
     * @param hints hints description
     * @param minimumSize minimum window size
     */
    public static void showTooltipWindow(JComponent source, HelpHint[] hints,
            Dimension minimumSize) {
        showTooltipWindow(source.getLocationOnScreen().x + source.getWidth() / 2,
                source.getLocationOnScreen().y + source.getHeight(), hints, minimumSize);

    }

    /**
     * Show window under y coordinate, centered by x coordinate
     *
     * @param x
     * @param y
     * @param hints hints description
     * @param minimumSize minimum window size
     *
     */
    public static void showTooltipWindow(int x, int y, HelpHint[] hints, Dimension minimumSize) {
        if (dialog != null) {
            dialog.setVisible(false);
        }

        dialog = new HelpTooltipWindow(x, y, hints);
        dialog.setMinimumSize(minimumSize);
        dialog.setCurrentHint(0);
        dialog.setVisible(true);
    }

    /**
     * Close tooltip window
     *
     * @param dialog window to be closed
     */
    private static void closeTooltipWindow(final HelpTooltipWindow dialog) {
        // Smoothly hide window
        if (showTimer != null && showTimer.isRunning()) {
            showTimer.stop();
        }
        if (indicationTimer != null && indicationTimer.isRunning()) {
            indicationTimer.stop();
        }
        opacity = 1f;
        showTimer = new Timer(1000 / 24, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.1f;
                if (opacity <= 0.1f) {
                    opacity = 0.1f;
                    dialog.setVisible(false);
                    showTimer.stop();
                }
                dialog.contentPanel.repaint();
            }
        });
        showTimer.start();
    }
}
