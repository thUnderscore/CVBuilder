package org.thUnderscore.common.mvc.swing;

import org.thUnderscore.common.mvc.swing.elements.BaseElement;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.mvc.BaseObjectView;
import org.thUnderscore.common.mvc.intf.ObjectController;
import org.thUnderscore.common.mvc.intf.ObjectModel;
import org.thUnderscore.common.mvc.swing.intf.SwingObjectView;

/**
 *
 * @author tosh
 */
public class BaseSwingObjectView extends BaseObjectView implements SwingObjectView {

    /**
     * In default realization of placement if such element exists in view, view
     * container layout will be setted to BorderLayout and element named ROOT
     * would be placed in rootPosition
     *
     * @see #rootPosition
     */
    public static final String ROOT = "rootComponent";
    /**
     * Predefinde name of element
     */
    public static final String GENERAL_INFO = "generalInfo";
    /**
     * Predefinde name of element
     */
    public static final String ADDITIONAL_INFO = "additionalInfo";
    /**
     * Key in bunde for GENERAL_INFO element title
     */
    public static final String GENERAL_INFO_CAPTION_RES = "BaseObjectView.GENERAL_INFO_CAPTION";
    /**
     * Key in bunde for ADDITIONAL_INFO element title
     */
    public static final String ADDITIONAL_INFO_CAPTION_RES = "BaseObjectView.ADDITIONAL_INFO_CAPTION";

    /**
     * true if view was already built
     */
    boolean isBuilt;
    /**
     * Elements of this view
     */
    List<BaseElement> elementList = new ArrayList<BaseElement>();
    /**
     * Map of elements of view, element name is key
     */
    Map<String, BaseElement> elementMap = new HashMap<String, BaseElement>();
    /**
     * Default position of ROOT element
     *
     * @see #ROOT
     */
    protected String rootPosition = BorderLayout.CENTER;

    /**
     * As model used BaseObjectModel instance, as controller used
     * BaseObjectController
     */
    public BaseSwingObjectView() {
        super();
    }

    /**
     *
     * @param model model of MVC bunch
     * @param controller controller of MVC bunch
     */
    public BaseSwingObjectView(ObjectModel model, ObjectController controller) {
        super(model, controller);
    }

    /**
     * Create components for all elements of view
     */
    protected void createComponents() {
        for (int i = 0; i < elementList.size(); i++) {
            elementList.get(i).createComponent();
        }
    }

    /**
     * Add component to container as child
     *
     * @param container containder
     * @param componentName component name
     * @return true if component was added
     */
    protected boolean addComponentByNameToContainer(JComponent container, String componentName) {
        JComponent component = getComponentForPlacementByName(componentName);
        boolean result = (component != null);
        if (result) {
            container.add(component);
        }
        return result;
    }

    /**
     * Add component to container as child
     *
     * @param containerName containder name
     * @param componentName component name
     * @return true if component was added
     */
    protected boolean addComponentByNameToContainer(String containerName, String componentName) {
        JComponent container = getComponentByName(containerName);
        return (container != null)
                && addComponentByNameToContainer(container, componentName);
    }

    /**
     * Add component to container as child
     *
     * @param containerName containder name
     * @param component
     * @return true if component was added
     */
    protected boolean addComponentToContainer(String containerName, JComponent component) {
        return addComponentToContainer(getComponentByName(containerName), component);
    }

    /**
     * Add component to container as child
     *
     * @param container
     * @param component
     * @return true if component was added
     */
    protected boolean addComponentToContainer(JComponent container, JComponent component) {
        boolean result = (component != null) && (container != null);
        if (result) {
            container.add(component);
        }
        return result;
    }

    /**
     * Place componets in container and set container layout. By default if
     * exists view element named rootComponent, layout is BorderLayout and this
     * element is placed to position from rootPosition field
     *
     * @param container Container for components
     */
    protected void placeComponents(JComponent container) {
        for (int i = 0; i < elementList.size(); i++) {
            elementList.get(i).placeComponents(container);
        }
        if (getElementByName(ROOT) != null) {
            container.setLayout(new BorderLayout());
            container.add(getComponentForPlacementByName(ROOT), rootPosition);
        }
    }

    @Override
    public void refresh() {
        Object object = getObject();
        for (BaseElement baseElement : elementList) {
            baseElement.setObject(object);
        }
    }

    /**
     * Create and place components by view elements
     *
     * @param container container for components
     */
    @Override
    public void build(JComponent container) {
        if (isBuilt) {
            return;
        }
        createComponents();
        placeComponents(container);
        controller.setModel(model);
        controller.setView(this);
        isBuilt = true;
    }

    @Override
    public BaseSwingObjectView addElement(BaseElement element) {
        element.setView(this);
        elementList.add(element);
        String name = element.getName();
        if (CommonUtils.isNotEmpty(name)) {
            elementMap.put(name, element);
        }
        return this;
    }

    @Override
    public BaseElement getElementByName(String name) {
        return elementMap.get(name);
    }

    @Override
    public JComponent getComponentByName(String name) {
        BaseElement viewElement = elementMap.get(name);
        return viewElement == null ? null : viewElement.getComponent();
    }

    @Override
    public JComponent getComponentForPlacementByName(String name) {
        BaseElement viewElement = elementMap.get(name);
        return viewElement == null ? null : viewElement.getComponentForPlacement();
    }

}
