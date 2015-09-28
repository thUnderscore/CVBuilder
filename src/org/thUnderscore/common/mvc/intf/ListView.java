package org.thUnderscore.common.mvc.intf;

/**
 * Interface for view of MVC bunch, which mean object with child objects list
 *
 * @author thUnderscore
 */
public interface ListView extends ObjectView {

    public static final int YES = 0;
    public static final int NO = 1;

    /**
     * Gets should changes in current child be saved, canceled or action should
     * be canceled. Decision can be made by user or by other means
     *
     * @return YES - if object should be saved and action should be executed; NO
     * - if changes should be canceled (if child is new - it should be deleted)
     * and action should be executed; all ather cases - action should be
     * canceled
     *
     * @see ListModel#isCurrentModified()
     * @see ListModel#isCurrentNewObject()
     * @see ListController#checkItemModified()
     * @see ListView#YES
     * @see ListView#NO
     */
    public int getSaveChangedOnChangeCurrentDecision();

    /**
     * Checks whether the object should be removed. Decision can be made by user
     * or by other means
     *
     * @return true if object should be deleted, false if removing should be
     * canceled
     */
    public boolean checkDeleteCurrent();

    /**
     * It is called when view should bind children list getted from model to UI
     * and display this list
     */
    public void bindList();

    /**
     * It is called when view should unbind children list getted from model from
     * UI and stop displaying of list
     */
    public void unbindList();

    /**
     * Gets child, which is current in UI. This property should be synchronized
     * to appropriated model property
     *
     * @return current object
     * @see ListModel#getCurrent()
     */
    public Object getCurrent();

    /**
     * Set new current child in UI. This property should be synchronized to
     * appropriated model property
     *
     * @param current
     * @see ListModel#getCurrent()
     */
    public void setCurrent(Object current);

    /**
     * Gets model of MVC bunch
     *
     * @return ListModel instance
     */
    public ListModel getListModel();

    /**
     * Gets controller of MVC bunch
     *
     * @return ListController instance
     */
    public ListController getListController();

    /**
     * Gets should be control for children swapping visible or not
     * @return true if control are required
     */
    public boolean isShowMoveControl();

    /**
     * Sets should be control for children swapping visible or not
     * @param showMoveControls
     * @return same list view
     */
    public ListView setShowMoveControl(boolean showMoveControls);

    /**
     * Lets view actualize object's isModified property
     */
    public void beforeCheckItemModified();

    /**
     * Lets view restore itself after actualization object's isModified property
     */
    public void afterCheckItemModified(boolean result);
}
