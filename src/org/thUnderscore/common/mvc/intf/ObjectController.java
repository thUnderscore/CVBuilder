package org.thUnderscore.common.mvc.intf;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

/**
 * Interface for controller of MVC bunch
 * @author thUnderscore
 */
public interface ObjectController extends ActionListener, PropertyChangeListener{

    /**
     * Sets model of MVC bunch
     * @param model model whish should be controlled
     */
    public void setModel(ObjectModel model);

    /**
     * Sets view of MVC bunch
     * @param view view whish should be controlled
     */
    public void setView(ObjectView view);
    
}
