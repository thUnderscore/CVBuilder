package org.thUnderscore.common.mvc.intf;

import java.util.List;

/**
 * Interface for model of MVC bunch, which mean object with child objects list
 * @author thUnderscore
 */
public interface ListModel extends ObjectModel {

    /**
     * Gets represented object children List 
     * @return 
     */
    public List<Object> getList();

    /**
     * Gets "current" child of represented object. It is assumed that only one 
     * child at a time is the current and it should be displayed in view
     * @return current child or null if children list is empty or current child is 
     * not selected
     */
    public Object getCurrent();

    /**
     * Gets "current" child of represented object. It is assumed that only one 
     * child at a time is the current and it should be displayed in view
     * @param object new current child
     */    
    public void setCurrent(Object object);

    /**
     * Gets was current child modified
     * @return true if current is selected and was modified
     * @see EditableObject#isModified() 
     */
    public boolean isCurrentModified();

    /**
     * Gets is current child just added to list and was not saved yet
     * @return true if current is new unsaved 
     * @see EditableObject#isNewObject() 
     */
    public boolean isCurrentNewObject();

    /**
     * Save current child
     * @see EditableObject#save() 
     */
    public void saveCurrent();

    /**
     * Cancel changes in  current child
     * @see EditableObject#cancel() 
     */
    public void cancel();

    public void removeItem(Object item);

    /**
     * Created new child and add it to children list
     * @return 
     */
    public Object addNewItem();

    /**
     * Check can item be added to children list or not
     * @param item checked child
     * @return true if item can be added
     */
    public boolean isAcceptable(Object item);
    
    /**
     * Swap two children in list
     * @param i index of first child
     * @param j index of second child
     */
    public void swap(int i, int j);

}
