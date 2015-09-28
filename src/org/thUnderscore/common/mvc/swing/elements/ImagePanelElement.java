package org.thUnderscore.common.mvc.swing.elements;

import org.thUnderscore.common.ui.components.ImagePanel;


/**
 * Element which creates ImagePanel
 * @author tosh
 */
public class ImagePanelElement extends PropertyElement {

    /**
     * 
     * @param name  Element name
     */
    public ImagePanelElement(String name) {
        this(name, name);        
    }
    
    /**
     * 
     * @param name Element name
     * @param objectPropertyName Object property, which binded with component
     */
    public ImagePanelElement(String name, String objectPropertyName) {
        super(ImagePanel.class, name, objectPropertyName, ImagePanel.IMAGE_PROP);
    }

    @Override
    public void clear() {
        if (component != null){
            ((ImagePanel)component).clear();
        }
    }
}
