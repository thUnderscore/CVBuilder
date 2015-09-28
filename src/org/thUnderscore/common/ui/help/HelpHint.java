package org.thUnderscore.common.ui.help;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.thUnderscore.common.utils.UIUtils;

/**
 * Help hint content description
 *
 * @author thUnderscore
 */
public class HelpHint {

    
    /**
     * Default color for component indication
     */
    public static final Color DEFAULT_INDICATION_COLOR = new Color(82, 146, 235);
    /**
     * Hint icon
     */
    Icon icon;
    /**
     * Hint text
     */
    String text;
    /**
     * The component to which the description
     */
    JComponent component;
    /**
     * Indication effect color
     */
    Color color;

    public HelpHint(String text, ImageIcon icon, JComponent ripplesComponent, Color indicationColor) {

        
        if (icon != null){
            BufferedImage scaledImage = UIUtils.scaleImageToFit(icon, 
                    HelpTooltipWindow.ICON_SIZE, HelpTooltipWindow.ICON_SIZE,
                    BufferedImage.TYPE_INT_ARGB);
            this.icon = new ImageIcon(scaledImage);
        }
        
        this.text = text;
        this.component = ripplesComponent;
        this.color = indicationColor;
        
    }

    public HelpHint(String text, ImageIcon icon) {
        this(text, icon, null, null);
    }
    
    public HelpHint(String text, ImageIcon icon, Color indicationColor) {
        this(text, icon, null, indicationColor);
    }
    
    public HelpHint(String text, ImageIcon icon, JComponent ripplesComponent) {
        this(text, icon, ripplesComponent, null);
    }

    public HelpHint(String text) {
        this(text, null);
    }

    public HelpHint() {
        
    }

    /**
     * Gets hint icon
     * @return Icon instance
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Gets hint text
     * @return hint text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the component to which the description
     * @return component
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * Gets indication effect color
     * @return 
     */
    public Color getColor() {
        return color == null ? DEFAULT_INDICATION_COLOR : color;
    }
    
    

}
