package org.thUnderscore.common.mvc.swing.intf;

import javax.swing.JComponent;
import org.thUnderscore.common.mvc.intf.ObjectView;
import org.thUnderscore.common.mvc.swing.elements.BaseElement;

/**
 * Interface for view, which use Swing for UI realization
 * 
 * thUnderscore
 */
public interface SwingObjectView extends ObjectView {

    /**
     * Gets JComponent by object property name
     * @param name object property  name
     * @return appropriated component, which represent propertie  or null if not found
     */
    public JComponent getComponentByName(String name);

    /**
     * Gets componet for placemetn in view by object property name. 
     * Can return a result different from the result of the getComponentByName
     * e.g. if component is wrapped with ScrollPane
     * @param name object property  name
     * @return 
     */
    public JComponent getComponentForPlacementByName(String name);

    /**
     * Create and place components
     * @param component content pane
     */
    public void build(JComponent component);

    /**
     * Add part element of view structure to view
     * @param element element to be added
     * @return same view
     */
    public SwingObjectView addElement(BaseElement element);

    /**
     * Get element of view's structure by ir's name
     * @param name element name
     * @return found element or null
     */
    public BaseElement getElementByName(String name);

}
