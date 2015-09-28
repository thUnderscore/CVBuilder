package org.thUnderscore.common.mvc;

import org.thUnderscore.common.PropertyChangeSupportedObject;
import org.thUnderscore.common.mvc.intf.ObjectModel;

/**
 *
 * @author thUnderscore
 */
public class BaseObjectModel extends PropertyChangeSupportedObject implements ObjectModel{
    
    /**
     * Object property name
     * @see #getObject() 
     */
    public static final String OBJECT_PROP = "object";    
    
    /**
     * @see #getObject() 
     */
    protected Object object;

    public BaseObjectModel() {
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public void setObject(Object object) {
        if (this.object == object) {
            return;
        }
        firePropertyChange(OBJECT_PROP, this.object, this.object = object);
    }
    
}
