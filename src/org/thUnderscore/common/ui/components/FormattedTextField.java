package org.thUnderscore.common.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import org.thUnderscore.common.ui.intf.EditorComponent;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.utils.UIUtils;

/**
 * Enhanced text field
 *
 * @author thUnderscore
 */
public class FormattedTextField extends JFormattedTextField implements EditorComponent {

    public static final String TEXT_PROP = "text";

    /**
     * show hint if is empty
     */
    protected boolean showHint = true;
    /**
     * is required
     */
    protected boolean required = false;
    /**
     * hint text
     */
    protected String hintText = null;
    {
        setSelectionColor(UIUtils.TEXT_SELECTED_COLOR);
        setSelectedTextColor(getForeground());

            }

            @Override
    public void setText(String t) {
        super.setText(t);
                calcShowHint();
                repaint();
            }

                    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue); 
        if ("value".equals(propertyName)){
            fireTextChanged();        
                }
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        calcShowHint();
        repaint();
        if (e.getID() == FocusEvent.FOCUS_LOST){            
            fireTextChanged();
    }
    }

    @Override
    public void setEditable(boolean b) {
        super.setEditable(b);
        calcShowHint();
        repaint();

    }

    /**
     * Gets required
     * @return required
     * @see #required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets required
     * @param required required
     * @see #required
     */
    public void setRequired(boolean required) {
        this.required = required;
        calcShowHint();
        repaint();
    }
    
    /**
     * Gets hintText
     * @return hint text
     * @see #hintText
     */
    public String getHintText() {
        return hintText;
    }

    /**
     * Sets hintText
     * @param hintText hint text
     * @see #hintText
     */
    public void setHintText(String hintText) {
        this.hintText = hintText;
        calcShowHint();
        repaint();
    }    
    
    /**
     * Calculate is hint required
     */
    protected void calcShowHint() {
        showHint = (getDocument() != null)
                && CommonUtils.isEmpty(getText())
                && (!hasFocus() || !isEditable())
                && CommonUtils.isNotEmpty(hintText);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showHint) {
            /*
            Show hint if needed
            */
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            Insets insets = UIUtils.getComponentInsets(this);
            g2d.setColor(required
                    ? UIUtils.REQUIRED_COMPONENT_HINT_COLOR : UIUtils.COMPONENT_HINT_COLOR);
            g2d.drawString(hintText, insets.left, g2d.getFontMetrics().getHeight());
        }
    }

    @Override
    public void flush() {    
        fireTextChanged();        
    }
    
    private void fireTextChanged() {
        super.firePropertyChange(TEXT_PROP, null, getText()); 
    }    

}
