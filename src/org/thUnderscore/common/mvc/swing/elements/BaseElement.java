package org.thUnderscore.common.mvc.swing.elements;

import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import org.thUnderscore.common.mvc.swing.BaseSwingObjectView;

/**
 * Base element of View. Element create JComponent s part of Swing UI.
 *
 * @author thUnderscore
 */
public abstract class BaseElement {

    /**
     * @see #getComponent()
     */
    JComponent component;
    /**
     * Class, whose instance will be created and became component
     *
     * @see #getComponent
     */
    Class<? extends JComponent> componentClass;
    /**
     * Gets has component TitledBorder or not
     */
    protected boolean hasBorder;
    /**
     * TitledBorder title
     */
    protected String borderTitle;

    /**
     * @see #getName()
     */
    String name;
    /**
     * @see #setView(org.thUnderscore.common.mvc.swing.BaseSwingObjectView)
     */
    protected BaseSwingObjectView view;
    /**
     * If component was wrapped vy other component (e.g. ScrollPane) contain
     * this wraping component
     */
    protected JComponent wrapComponent;
    /**
     * @see #setWrapScrollPane(boolean)
     */
    protected boolean wrapScrollPane;
    /**
     * @see #setMaximumSize(java.awt.Dimension)
     */
    protected Dimension maximumSize;
    /**
     * @see #setMinimumSize(java.awt.Dimension)
     */
    protected Dimension minimumSize;
    /**
     * @see #setPreferredSize(java.awt.Dimension)
     */
    protected Dimension preferredSize;

    /**
     * 
     * @param componentClass Class, whose instance will be created and became component
     * @param name Element name
     */
    public BaseElement(Class<? extends JComponent> componentClass, String name) {
        this.componentClass = componentClass;
        this.name = name;
    }

    /**
     * Gets element's name
     *
     * @return element's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets maximum component size
     *
     * @return size
     */
    public Dimension getMaximumSize() {
        return maximumSize;
    }

    /**
     * Sets maximum component size
     *
     * @param maximumSize
     * @return same element
     */
    public BaseElement setMaximumSize(Dimension maximumSize) {
        this.maximumSize = (maximumSize == null) ? null : new Dimension(maximumSize);
        return this;
    }

    /**
     * Sets maximum component size
     *
     * @param width
     * @param height
     * @return same element
     */
    public BaseElement setMaximumSize(int width, int height) {
        this.maximumSize = new Dimension(width, height);
        return this;
    }

    /**
     * Gets minimum component size
     *
     * @return size
     */
    public Dimension getMinimumSize() {
        return minimumSize;
    }

    /**
     * Sets minimum component size
     *
     * @param minimumSize
     * @return same element
     */
    public BaseElement setMinimumSize(Dimension minimumSize) {
        this.minimumSize = (minimumSize == null) ? null : new Dimension(minimumSize);
        return this;
    }

    /**
     * Sets minimum component size
     *
     * @param width
     * @param height
     * @return same element
     */
    public BaseElement setMinimumSize(int width, int height) {
        this.minimumSize = new Dimension(width, height);
        return this;
    }

    /**
     * Gets preferred component size
     *
     * @return size
     */
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    /**
     * Sets preferred component size
     *
     * @param preferredSize
     * @return same element
     */
    public BaseElement setPreferredSize(Dimension preferredSize) {
        this.preferredSize = (preferredSize == null) ? null : new Dimension(preferredSize);
        return this;
    }

    /**
     * Sets preferred component size
     *
     * @param width
     * @param height
     * @return same element
     */
    public BaseElement setPreferredSize(int width, int height) {
        this.preferredSize = new Dimension(width, height);
        return this;
    }

    /**
     * Set maximum, minimum and preferred size
     * @param size
     * @return same element
     */
    public BaseElement setSize(Dimension size) {
        setMaximumSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        return this;
    }

    /**
     * Set maximum, minimum and preferred size
     * @param width
     * @param height
     * @return same element
     */
    public BaseElement setSize(int width, int height) {
        return setSize(new Dimension(width, height));
    }

    /**
     * Set border for component
     * @param hasBorder - if true, component  border will be setted to TitledBorder
     * @param title border title
     * @return same element
     */
    public BaseElement setBorder(boolean hasBorder, String title) {
        this.hasBorder = hasBorder;
        this.borderTitle = title;
        return this;
    }

    /**
     * Sets should component be wrapped with scrollPane
     *
     * @param wrapScrollPane if true - component will be wrapped
     * @return same element
     */
    public BaseElement setWrapScrollPane(boolean wrapScrollPane) {
        this.wrapScrollPane = wrapScrollPane;
        return this;
    }

    /**
     * Create component,  wrapComponent and set sizes, border etc
     */
    public void createComponent() {
        component = internalCreateComponent();
        component.addPropertyChangeListener(view);
        component.setPreferredSize(null);
        if (hasBorder) {
            JComponent borderComponent;
            if (wrapScrollPane) {
                borderComponent = getComponentForPlacement();
            } else {
                borderComponent = component;
            }
            borderComponent.setBorder(new TitledBorder(borderTitle));
        }
        if (maximumSize != null) {
            getComponentForPlacement().setMaximumSize(maximumSize);
        }
        if (minimumSize != null) {
            getComponentForPlacement().setMinimumSize(minimumSize);
        }
        if (preferredSize != null) {
            getComponentForPlacement().setPreferredSize(preferredSize);
        }

    }

    /**
     * Gets UI component, created by this element
     *
     * @return
     */
    public JComponent getComponent() {
        if (component == null) {
            createComponent();
        }
        return component;
    }

    /**
     * Create wrapComponent
     * @return 
     */
    protected JComponent createWrapComponentForPlacement() {
        return new JScrollPane(getComponent());
    }

    /**
     * Gets component for placement - wrapComponent if it is not null all component
     * in other case
     * @return Component which will be placed in view container
     */
    public JComponent getComponentForPlacement() {
        return wrapScrollPane ? (wrapComponent == null
                ? wrapComponent = createWrapComponentForPlacement() : wrapComponent) : getComponent();
    }

    /**
     * Set object, which is displayed in element's 
     * @param object 
     */
    public void setObject(Object object) {

    }

    /**
     * Set view, contained this element
     * @param view 
     */
    public void setView(BaseSwingObjectView view) {
        this.view = view;
    }

    /**
     * Place elements component in container
     * @param container 
     */
    public void placeComponents(JComponent container) {

    }

    /**
     * Create instace of component. Override if need in inheritor
     * @return 
     */
    protected JComponent internalCreateComponent() {
        try {
            return componentClass.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

}
