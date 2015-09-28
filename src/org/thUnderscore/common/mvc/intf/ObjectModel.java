package org.thUnderscore.common.mvc.intf;

/**
 * Interface for model of MVC bunch
 * @author thUnderscore
 */
public interface ObjectModel extends PropertiesContainer {

    /**
     * Gets object, which is represented by model
     * @return represented object
     */
    public Object getObject();

    /**
     * Sets object, which is represented by model 
     * @param object represented object
     */
    public void setObject(Object object);

}
