package org.thUnderscore.common.mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.thUnderscore.common.PropertyChangeSupportedObject;
import org.thUnderscore.common.mvc.intf.ObjectController;
import org.thUnderscore.common.mvc.intf.ObjectModel;
import org.thUnderscore.common.mvc.intf.ObjectView;

/**
 *
 * @author thUnderscore
 */
public class BaseObjectView extends PropertyChangeSupportedObject
        implements ActionListener, PropertyChangeListener, ObjectView {

    /**
     * @see #getModel()
     */
    protected ObjectModel model;
    /**
     * @see #getController() ()
     */
    protected ObjectController controller;

    /**
     * As model used BaseObjectModel instance, as controller used BaseObjectController
     */
    public BaseObjectView() {
        this(new BaseObjectModel(), new BaseObjectController());
    }

    /**
     *
     * @param model model of MVC bunch
     * @param controller controller of MVC bunch
     */
    public BaseObjectView(ObjectModel model, ObjectController controller) {
        this.model = model;
        this.controller = controller;
    }

    @Override
    public ObjectModel getModel() {
        return model;
    }

    @Override
    public ObjectController getController() {
        return controller;
    }

    @Override
    public void refresh() {

    }

    @Override
    public Object getObject() {
        return model == null ? null : model.getObject();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
