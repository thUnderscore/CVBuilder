package org.thUnderscore.common.ui.components;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextArea;
import org.thUnderscore.common.ui.intf.EditorComponent;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.utils.UIUtils;

/**
 * Enhanced text area
 *
 * @author thUnderscore
 */
public class TextArea extends JTextArea implements EditorComponent{

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
        /**
         * add focus listener for hint displaying
         */
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                fireTextChanged();
                calcShowHint();
                repaint();
            }

            @Override
            public void focusGained(FocusEvent e) {
                calcShowHint();
                repaint();
            }

        });
        
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
    public void setRequired(boolean requiered) {
        this.required = requiered;
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
            FontMetrics fontMetrics = getFontMetrics(getFont());
            int maxWidth = getWidth() - insets.left - insets.right;
            int minCharCount = 7 * maxWidth / (2 * fontMetrics.getMaxAdvance());
            int start = 0;
            int lineLength;
            int length = hintText.length();
            int lineCount = 1;
            while (length > 0) {
                if (length <= minCharCount) {
                    lineLength = length;

                } else {
                    lineLength = minCharCount;

                    for (int i = start + minCharCount; i > start; i--) {
                        if (hintText.charAt(i) == ' ') {
                            lineLength = i - start + 1;
                            break;
                        }
                    }
                }

                g2d.drawString(hintText.substring(start, start + lineLength), insets.left,
                        g2d.getFontMetrics().getHeight() * lineCount);
                start += lineLength;
                length -= lineLength;
                lineCount++;
            }
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
