package org.thUnderscore.common.mvc.swing.intf;

import javax.swing.JComponent;
import org.thUnderscore.common.mvc.intf.ListView;
/**
 * Interface for vew, which can display object contained children list.
 * Implementation should display oject properties, children list and current
 * child properties thUnderscore
 */
public interface SwingListView extends ListView, SwingObjectView {

    /**
     * Get view for current child
     *
     * @return SwingObjectView displayed current child
     */
    public SwingObjectView getItemView();

    /**
     * Gets current child component by child property name
     *
     * @param name child property name
     * @return component which represent propertie
     * @see SwingObjectView#getComponentByName(java.lang.String)
     */
    public JComponent getItemViewComponentByName(String name);

    /**
     * Gets current child component for placement by child property name
     *
     * @param name child property name
     * @return component which represent propertie or it's wrapper (e.g. ScrollPane)
     * @see SwingObjectView#getComponentForPlacementByName(java.lang.String) 
     */
    public JComponent getItemViewComponentForPlacementByName(String name);

    /**
     * Set size for component which represent children list
     * @param width
     * @param height
     * @return same view
     */
    public SwingListView setListPanelSize(int width, int height);

    /**
     * Add description for column, which should be displayed in children list
     * @param name child property name
     * @param title column title
     * @param clazz class of property
     * @return 
     */
    public SwingListView addColumnDescription(String name, String title, Class clazz);

    /**
     * Set axis for default placement of list, object properties and child view
     * 
     * @param axis
     * @return same view
     */
    public SwingListView setAxis(int axis);

}
