package org.thUnderscore.common.mvc.intf;

/**
 * Interface for controller of MVC bunch, which mean object with child objects
 * list
 *
 * @author thUnderscore
 */
public interface ListController extends ObjectController {
    
    /**
     * Sets is child from list delee silence, or user confirmation iss required
     *
     * @param silentDelete true if deletion should be silent
     */
    public void setSilentDelete(boolean silentDelete);

    /**
     * Gets is child from list delee silence, or user confirmation iss required
     *
     * @return true if deletion should be silent
     */
    public boolean isSilentDelete();

    /**
     * Gets model of MVC bunch
     *
     * @return ListModel instance
     */
    public ListModel getListModel();

    /**
     * Gets view of MVC bunch
     *
     * @return ListView instance
     */
    public ListView getListView();

    /**
     * Call before executing some action, which can change current child pointer
     *
     * @return True if caller can continue and False is it must cancel action
     */
    public boolean checkItemModified();
}
