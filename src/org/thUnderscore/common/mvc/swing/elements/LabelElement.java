package org.thUnderscore.common.mvc.swing.elements;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * Element which creates JLabel
 *
 * @author thUnderscore
 */
public class LabelElement extends PropertyElement {

    public static final String TEXT_PROP = "text";

    /**
     * label title
     */
    protected String title;
    /**
     * label text color
     */
    protected Color color;

    /**
     * 
     * @param name Element name
     * @param title Label text
     */
    public LabelElement(String name, String title) {
        this(name, title, null);
    }

    /**
     * 
     * @param name Element name
     * @param title Label text
     * @param color Label text color
     */
    public LabelElement(String name, String title, Color color) {
        this(name, null, title, color);
    }

    /**
     * 
     * @param name Element name
     * @param objectPropertyName Object property, which binded with component
     * @param title Label text
     * @param color Label text color
     */
    public LabelElement(String name, String objectPropertyName, String title, Color color) {
        super(JLabel.class, name, objectPropertyName, TEXT_PROP);
        this.title = title;
        this.color = color;
    }

    @Override
    public void clear() {
        if ((component != null) && isBundable()) {
            ((JTextArea) component).setText(null);
        }
    }

    @Override
    public void createComponent() {
        super.createComponent();
        if (!isBundable()) {
            ((JLabel) component).setText(title);
        }
        if (color != null) {
            ((JLabel) component).setForeground(color);
        }
    }

}
