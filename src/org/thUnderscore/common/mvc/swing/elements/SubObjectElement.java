package org.thUnderscore.common.mvc.swing.elements;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.mvc.swing.intf.SwingObjectView;

/**
 * Element which can contain view
 *
 * @author thUnderscore
 */
public class SubObjectElement extends BaseElement {

    /**
     * subview, which is diplayed in element's component
     */
    SwingObjectView subView;
    /**
     * Subview class
     */
    Class<? extends SwingObjectView> subViewClass;

    /**
     *
     * @param name Element name
     * @param subViewClass subview, which is diplayed in element's component
     */
    public SubObjectElement(String name, Class<? extends SwingObjectView> subViewClass) {
        super(JPanel.class, name);
        this.subViewClass = subViewClass;
    }

    /**
     *
     * @param name Element name
     * @param subView subview, which is diplayed in element's component
     */
    public SubObjectElement(String name, SwingObjectView subView) {
        this(name, subView.getClass());
        this.subView = subView;
    }

    /**
     * Gets subview.If property is null - try to create subview by subview class
     *
     * @return
     */
    public SwingObjectView getSubView() {
        if (subView == null) {
            createSubView();
        }
        return subView;
    }

    @Override
    public void setObject(Object object) {
        Object subObject = null;
        if (object != null) {
            try {
                subObject = CommonUtils.getPropertyValue(object, name);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }

        }
        getSubView().getModel().setObject(subObject);

    }

    @Override
    public void createComponent() {
        super.createComponent();
        getSubView().build(component);
    }

    /**
     * Create subview
     */
    protected void createSubView() {
        try {
            subView = subViewClass.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

}
