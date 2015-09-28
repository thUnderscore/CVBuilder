package org.thUnderscore.common.mvc.swing.elements;

import javax.swing.JComponent;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Element whose component represent object property
 * @author thUnderscore
 */
public class PropertyElement extends BaseElement {

    /**
     * Property of object
     */
    protected String objectPropertyName;
    /**
     * Property of component
     */
    protected String componentPropertyName;
    /**
     * Binding between object and component property
     */
    protected Binding binding;
    /**
     * @see #isRequired()
     */
    protected boolean required;
    
    /**
     * 
     * @param componentClass Class, whose instance will be created and became component
     * @param name Element name
     * @param objectPropertyName Object property, which binded with component
     * @param componentPropertyName Component property, which binded with object
     */
    public PropertyElement(Class<? extends JComponent> componentClass,
            String name, String objectPropertyName, String componentPropertyName) {
        super(componentClass, name);
        this.objectPropertyName = objectPropertyName;
        this.componentPropertyName = componentPropertyName;
    }

    @Override
    public void setObject(Object object) {
        if (!isBundable()){
            return;
        }
        if (binding != null) {
            if (binding.isBound()) {
                binding.unbind();
            }

        } //else {
            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                    object, 
                    //BeanProperty.create(objectPropertyName),
                    ELProperty.create(CommonUtils.getPropertyExpressionByName(objectPropertyName)),
                    getComponent(), BeanProperty.create(componentPropertyName));
        //}
        //binding.setSourceObject(object);
        if (object != null) {
            binding.bind();
        } else {
            clear();
        }
        setControlReadOnly(object != null);
    }

    /**
     * Clear component property
     */
    public void clear() {
    }

    /**
     * Sets control read only
     * @param readOnly 
     */
    protected void setControlReadOnly(boolean readOnly){
        component.setEnabled(readOnly);
    }

    /**
     * Gets can bject property be bound
     * @return 
     */
    protected boolean isBundable() {
        return CommonUtils.isNotEmpty(objectPropertyName);
    }

    /**
     * Gets is object property required
     * @return true if required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets is objec property required
     * @param required 
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    /**
     * Gets is component empty
     * @return true if empty
     */
    public boolean isEmpty(){
        return false;
    }
}
