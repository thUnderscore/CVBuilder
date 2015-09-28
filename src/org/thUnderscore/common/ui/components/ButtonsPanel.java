package org.thUnderscore.common.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.WeakHashMap;
import javax.swing.*;
import org.thUnderscore.common.utils.UIUtils;

/**
 * Buttons bar components
 *
 * @author thUnderscore
 */
public class ButtonsPanel extends JPanel implements ActionListener {

    /**
     * components aligment inside th buttons panel
     */
    public enum Alignment {

        LEADING,
        TRAILING,
        /**
         * use only when trere are no left an right or they are symmetric
         */
        CENTER
    }

    /**
     * default height
     */
    public static final int DEF_HEIGHT = 40;
    /**
     * default widhth
     */
    public static final int DEF_WIDTH = 0;
    /**
     * rigid area between components size
     */
    public static final Dimension rigidAreaDimension = new Dimension(5, 5);
    /**
     * beginUpdates counter
     *
     * @see #beginUpdate()
     */
    private int updatesCount = 0;
    /**
     * plase rigid areas between components
     */
    private boolean useGap = false;
    /**
     * Colors map for action command
     *
     * @see UIUtils#startRipplesEffect(int, int, java.awt.Color)
     */
    private Map<String, Color> commandColorMap;
    /**
     * lead components
     */
    protected ArrayList<JComponent> leftComponents = new ArrayList<JComponent>();
    /**
     * trailcomponents
     */
    protected ArrayList<JComponent> rightComponents = new ArrayList<JComponent>();
    /**
     * centre components
     */
    protected ArrayList<JComponent> centerComponents = new ArrayList<JComponent>();
    /**
     * all components
     */
    protected WeakHashMap<JComponent, String> allComponents = new WeakHashMap<JComponent, String>();

    public ButtonsPanel() {
        Dimension size = new Dimension(DEF_WIDTH, DEF_HEIGHT);
        setSize(size);
        setPreferredSize(size);
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
    }

    /**
     * Gets useGap
     *
     * @return useGap
     * @see #useGap
     */
    public boolean isUseGap() {
        return useGap;
    }

    /**
     * Sets useGap
     *
     * @param useGap
     * @see #useGap
     */
    public void setUseGap(boolean useGap) {
        this.useGap = useGap;
    }

    /**
     * Prevent rebuild after adding each components. Should be used in pair with
     * <code>endUpdate</code>
     *
     * @see #endUpdate
     */
    public void beginUpdate() {
        ++updatesCount;
    }

    /**
     * Last call of this method rebuilds buttons panel. Should be used in pair
     * with <code>beginUpdate</code>
     *
     * @see #beginUpdate
     */
    public void endUpdate() {
        if (updatesCount == 0) {
            return;
        }
        --updatesCount;
        if (updatesCount == 0) {
            rebuildLayout();
        }
    }

    /**
     * Create and add button
     *
     * @param title button's title
     * @param toolTip button's tooTip
     * @param id button's name
     * @param alignment button's aligment on panel
     * @param index button's index in aligment group
     */
    public void addButton(String title, String toolTip, String id, Alignment alignment, int index) {
        addButton(title, toolTip, "", id, alignment, index);
    }

    /**
     * Create and add button
     *
     * @param title button's title
     * @param toolTip button's tooTip
     * @param iconPath base resource name. "Normal.png" "Rollover.png"
     * "Pressed.png" "Disabled.png" will be addded to it for appropriate type of
     * image
     * @param id button's name
     * @param alignment button's aligment on panel
     * @param index button's index in aligment group
     *
     */
    public void addButton(String title, String toolTip, String iconPath, String id,
            Alignment alignment, int index) {
        JButton button = UIUtils.createButton(title, toolTip, iconPath, null);
        addButton(button, id, alignment, index);
    }

    /**
     * Create and add button
     *
     * @param bundle Resource bundle which contains values for title, tooltpi and
     * icons
     * @param titleKey button's title key in bundle
     * @param toolTipKey button's tooTip key in bundle
     * @param iconPathKey icon path key in bundle. value in bundle is base
     * resource name. "Normal.png" "Rollover.png" "Pressed.png" "Disabled.png"
     * will be addded to it for appropriate type of image
     * @param id button's name
     * @param alignment button's aligment on panel
     * @param index button's index in aligment group
     *
     */
    public void addButton(ResourceBundle bundle, String titleKey,
            String toolTipKey, String iconPathKey, String id,
            Alignment alignment, int index) {
        String title = (bundle != null) && bundle.containsKey(titleKey)
                ? bundle.getString(titleKey) : titleKey;
        String toolTip = (bundle != null) && bundle.containsKey(toolTipKey)
                ? bundle.getString(toolTipKey) : toolTipKey;
        String iconPath = (bundle != null) && bundle.containsKey(iconPathKey)
                ? bundle.getString(iconPathKey) : iconPathKey;
        addButton(title, toolTip, iconPath, id, alignment, index);
    }

    /**
     * Add button to panel
     *
     * @param button button to be added
     * @param id button's name
     * @param alignment button's alignment
     * @param index button's index in aligment group
     */
    public void addButton(JButton button, String id, Alignment alignment, int index) {
        button.addActionListener(this);
        addComponent(button, id, alignment, index);
    }

    /**
     * Add button to panel
     *
     * @param component component to be added
     * @param id component's name
     * @param alignment component's alignment
     * @param index component's index in aligment group
     */
    public void addComponent(JComponent component, String id, Alignment alignment, int index) {
        ArrayList<JComponent> components;
        switch (alignment) {
            case TRAILING:
                components = rightComponents;
                break;
            case CENTER:
                components = centerComponents;
                break;
            case LEADING:
            default:
                components = leftComponents;
                break;
        }
        allComponents.put(component, id);
        component.setName(id);
        if (index < 0) {
            components.add(component);
        } else {
            components.add(index, component);
        }
        rebuildLayout();
    }

    /**
     * Remove component by it's name
     *
     * @param id
     */
    public void removeComponent(String id) {
        JComponent component = getComponent(id);
        if (component == null) {
            return;
        }
        leftComponents.remove(component);
        rightComponents.remove(component);
        centerComponents.remove(component);
        allComponents.remove(component);
        rebuildLayout();
    }

    /**
     * gets component by it's name
     *
     * @param id component's name
     * @return component or null if not exists
     */
    public JComponent getComponent(String id) {
        JComponent component = null;
        for (int i = 0; ((null == component) && (i < leftComponents.size())); i++) {
            component = leftComponents.get(i);
            if (!component.getName().equalsIgnoreCase(id)) {
                component = null;
            }
        }
        for (int i = 0; ((null == component) && (i < rightComponents.size())); i++) {
            component = rightComponents.get(i);
            if (!component.getName().equalsIgnoreCase(id)) {
                component = null;
            }
        }
        for (int i = 0; ((null == component) && (i < centerComponents.size())); i++) {
            component = centerComponents.get(i);
            if (!component.getName().equalsIgnoreCase(id)) {
                component = null;
            }
        }
        return component;
    }

    /**
     * Reset layout and place components
     */
    protected void rebuildLayout() {
        if (updatesCount > 0) {
            return;
        }
        this.removeAll();
        BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        this.setLayout(layout);
        if (isUseGap()) {
            this.add(Box.createRigidArea(rigidAreaDimension));
        }
        for (int i = 0; i < leftComponents.size(); i++) {
            this.add(leftComponents.get(i), null);
            if (isUseGap() && (i + 1 < leftComponents.size())) {
                this.add(Box.createRigidArea(rigidAreaDimension));
            }
        }
        this.add(Box.createHorizontalGlue());
        for (int i = 0; i < centerComponents.size(); i++) {
            this.add(centerComponents.get(i), null);
            if (isUseGap() && (i + 1 < centerComponents.size())) {
                this.add(Box.createRigidArea(rigidAreaDimension));
            }
        }
        this.add(Box.createHorizontalGlue());
        for (int i = 0; i < rightComponents.size(); i++) {
            this.add(rightComponents.get(i), null);
            if (isUseGap() && (i + 1 < rightComponents.size())) {
                this.add(Box.createRigidArea(rigidAreaDimension));
            }
        }
        if (isUseGap()) {
            this.add(Box.createRigidArea(new Dimension(5, 0)));
        }
    }

    /**
     * Add action listener
     *
     * @param l listener
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Remove action listener
     *
     * @param l listener
     */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * Get action listeners arra
     *
     * @return
     */
    public ActionListener[] getActionListeners() {
        return (ActionListener[]) (listenerList.getListeners(
                ActionListener.class));
    }

    /**
     * Set color for ripples which appears in center of components when
     * appropriate action executed
     *
     * @param command command name
     * @param color ripples color
     */
    public void setCommandColor(String command, Color color) {
        if (commandColorMap == null) {
            commandColorMap = new HashMap<String, Color>();
        }
        commandColorMap.put(command, color);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            JComponent component;
            for (int i = 0; i < leftComponents.size(); i++) {
                component = leftComponents.get(i);
                if (component instanceof JButton) {
                    ((JButton) component).setAction(null);
                }
            }
            for (int i = 0; i < rightComponents.size(); i++) {
                component = rightComponents.get(i);
                if (component instanceof JButton) {
                    ((JButton) component).setAction(null);
                }
            }
            for (int i = 0; i < centerComponents.size(); i++) {
                component = centerComponents.get(i);
                if (component instanceof JButton) {
                    ((JButton) component).setAction(null);
                }
            }

        } catch (Exception e) {
        } finally {
            super.finalize();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        /*if ((commandColorMap != null) && (commandColorMap.containsKey(command))) {
            JComponent component = getComponent(command);
            UIUtils.startRipplesEffect(component, commandColorMap.get(command));            
        }*/
        
        // Guaranteed to return a non-null array
        ActionListener[] listeners = getActionListeners();
        for (int i = listeners.length - 1; i >= 0; --i) {
            listeners[i].actionPerformed(e);
        }
    }
}
