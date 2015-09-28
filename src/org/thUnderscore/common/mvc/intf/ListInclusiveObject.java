package org.thUnderscore.common.mvc.intf;

import java.util.List;

/**
 * Interface for object which has items list
 * 
 * @author thUnderscore
 */
public interface ListInclusiveObject {
    
    /**
     * Gets items list
     * @return List - items list
     */
    public List getList();
    
    /**
     * Create new item suitable for list/ Not add to list? just create
     * @return new item
     */
    public Object createItem();
    
    /**
     * Executes after removing item from list. Notifies object that the item was removed
     * @param item removed item
     */
    public void itemRemoved(Object item);

    /**
     * Executes after adding item to list. Notifies object that the item was added
     * @param item added item
     */
    public void itemAdded(Object item);    

    /**
     * Executes after swapping items in list. Notifies object that the items were swapped
     * @param i index of first item
     * @param j index of second item
     */
    public void itemSwaped(int i, int j);
           
}
