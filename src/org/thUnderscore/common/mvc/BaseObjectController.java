package org.thUnderscore.common.mvc;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import org.jdesktop.beansbinding.BindingGroup;
import org.thUnderscore.common.mvc.intf.ObjectController;
import org.thUnderscore.common.mvc.intf.ObjectModel;
import org.thUnderscore.common.mvc.intf.ObjectView;

/**
 *
 * @author thUnderscore
 */
public class BaseObjectController implements ObjectController {

    protected ObjectModel model;
    protected ObjectView view;
    protected BindingGroup propertiesBindingGroup;
    protected boolean dontCheckModified = true;

    /**
     * bind model and view properties if required (and if possible, of course)
     */
    protected void bind() {
        if ((model != null) && (view != null)) {
            bindProperties();

        }
    }

    /**
     * Reverse operation to bind
     * @see #bind() 
     */
    protected void unbind() {
        unbindProperties();
    }

    /**
     * Create properties bindings/ Oveeride in inheritor if need
     */
    protected void createPropertiesBindings() {
    }

    /**
     * bind model and view properties if required (and if possible, of course)
     */
    protected void bindProperties() {
        propertiesBindingGroup = new BindingGroup();
        createPropertiesBindings();
        propertiesBindingGroup.bind();
    }

    /**
     * Reverse operation to bindProperties
     * @see #bindProperties() 
     */
    protected void unbindProperties() {
        if (propertiesBindingGroup != null) {
            propertiesBindingGroup.unbind();
            propertiesBindingGroup = null;
        }
    }

    /**
     * Prcessing model property changes
     * @param evt PropertyChangeEvent
     */
    protected void modelPropertyChange(PropertyChangeEvent evt) {
        if (BaseObjectModel.OBJECT_PROP.equals(evt.getPropertyName())){
            view.refresh();
        }
    }

    /**
     * Prcessing view property changes
     * @param evt PropertyChangeEvent
     */
    protected void viewPropertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void setView(ObjectView view) {
        unbind();
        if (this.view != null) {
            this.view.removePropertyChangeListener(this);
        }
        this.view = view;
        if (this.view != null) {
            this.view.addPropertyChangeListener(this);
        }
        bind();
    }

    @Override
    public void setModel(ObjectModel model) {
        unbind();
        if (this.model != null) {
            this.model.removePropertyChangeListener(this);
        }
        this.model = model;
        if (this.model != null) {
            this.model.addPropertyChangeListener(this);
        }
        bind();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == model) {
            modelPropertyChange(evt);
        } else if (evt.getSource() == view) {
            viewPropertyChange(evt);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
