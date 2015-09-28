package org.thUnderscore.common.ui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;
import org.thUnderscore.common.utils.CommonUtils;

public class ButtonTabComponent extends JPanel {

    public static final String TABBED_PANE_IS_NULL_ERROR = "ButtonTabComponent.TABBED_PANE_IS_NULL";
    public static final String HELP_BUTTON_HINT_RES = "ButtonTabComponent.HELP_BUTTON_HINT";

    public class TabButton extends JButton implements ActionListener {

        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText(CommonUtils.i18n(HELP_BUTTON_HINT_RES));
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                pane.setSelectedIndex(i);
                buttonPressed(i, this);
            }
        }

        @Override
        public void updateUI() {
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i > -1) {
                drawTabButton(g2, getModel().isPressed(), i, this);
            }
        }
    }

    private final JTabbedPane pane;
    private TabButton button;

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

    public ButtonTabComponent(JTabbedPane pane, boolean showButton) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {

            throw new RuntimeException(CommonUtils.i18n(TABBED_PANE_IS_NULL_ERROR));
        }
        this.pane = pane;
        setOpaque(false);

        // read titles from JTabbedPane
        JLabel label = new JLabel() {
            @Override
            public String getText() {
                int i = ButtonTabComponent.this.pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return ButtonTabComponent.this.pane.getTitleAt(i);
                }
                return null;
            }
        };

        add(label);

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        if (showButton) {
            button = new TabButton();
            add(button);
        }
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    /**
     *
     * @param g2
     * @param pressed
     * @param indexOfTabComponent
     * @param button
     */
    protected void drawTabButton(Graphics2D g2, boolean pressed, int indexOfTabComponent, TabButton button) {
    }

    /**
     *
     * @param indexOfTabComponent
     * @param button
     */
    protected void buttonPressed(int indexOfTabComponent, TabButton button) {
    }

}
