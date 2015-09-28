package org.thUnderscore.common.mvc.intf;

/**
 *
 * @author thUnderscore
 */

public interface ObjectView extends PropertiesContainer{

    /**
     * Gets object displayed in view.
     * @return displayed object or null if no object is displayed
     */
    public Object getObject();

    /**
     * Refresh view when object represented by model was substituted
     */
    public void refresh();

    /**
     * Gets model of MVC bunch
     *
     * @return ObjectModel instance
     */
    public ObjectModel getModel();
    
    /**
     * Gets controller of MVC bunch
     *
     * @return ObjectController instance
     */
    public ObjectController getController();
        
}
