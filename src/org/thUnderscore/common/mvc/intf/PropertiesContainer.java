package org.thUnderscore.common.mvc.intf;

import java.beans.PropertyChangeListener;

/**
 * This is interface for object, which has PropertyChangeSupport as it's internal field
 * and provide it;s methods
 * @author thUnderscore
 */
public interface PropertiesContainer {

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     * If <code>listener</code> was added more than once to the same event
     * source, it will be notified one less time after being removed.
     * If <code>listener</code> is null, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param listener  The PropertyChangeListener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Add a PropertyChangeListener to the listener list. The listener is
     * registered for all properties. The same listener object may be added more
     * than once, and will be called as many times as it is added. If
     * <code>listener</code> is null, no exception is thrown and no action is
     * taken.
     *
     * @param listener The PropertyChangeListener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Reports a bound property update to listeners
     * that have been registered to track updates of
     * all properties or a property with the specified name.
     *
     * @param propertyName  the programmatic name of the property that was changed
     * @param oldValue      the old value of the property
     * @param newValue      the new value of the property
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue);
}
