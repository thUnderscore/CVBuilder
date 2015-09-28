package org.thUnderscore.common.mvc.intf;

/**
 * Interface for object which can be edited
 * 
 * @author thUnderscore
 */
public interface EditableObject {

    /**
     * Gets was object changed since last saving
     * @return true if object was changed
     */
    public boolean isModified();

    /**
     * Sets object was changed since last saving
     * @param isModified was changed
     */
    public void setModified(boolean isModified);

    /**
     * Saves object changes
     */
    public void save();

    /**
     * Cancels object changes
     */
    public void cancel();

    /**
     * Gets is object created but not saved
     * @return true if object is new
     */
    public boolean isNewObject();

    /**
     * Sets is object new (created but not saved)
     * @param newObject is object new
     */
    public void setNewObject(boolean newObject);

}
