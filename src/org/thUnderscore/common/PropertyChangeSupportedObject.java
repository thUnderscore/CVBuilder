package org.thUnderscore.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.thUnderscore.common.mvc.intf.PropertiesContainer;

/**
 * PropertiesContainer implementation
 * @author thUnderscore
 */
public class PropertyChangeSupportedObject implements PropertiesContainer{
    
    protected PropertyChangeSupport changeSupport;

    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            return;
        }
        changeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if ((oldValue != null && newValue != null && oldValue.equals(newValue)) || (oldValue == null && newValue == null)) {
            return;
        }
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
    
}
