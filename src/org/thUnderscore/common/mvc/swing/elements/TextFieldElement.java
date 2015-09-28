package org.thUnderscore.common.mvc.swing.elements;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.thUnderscore.common.ui.components.FormattedTextField;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Element which creates TextField
 *
 * @author thUnderscore
 */
public class TextFieldElement extends PropertyElement {


    /**
     * Hint for empty component
     */
    protected String hintText;

    /**
     *
     * @param name Element name
     */
    public TextFieldElement(String name) {
        this(name, null, false);
    }

    /**
     *
     * @param name Element name
     * @param hintText Hint for empty component
     * @param required Is object property required
     */
    public TextFieldElement(String name, String hintText, boolean required) {
        this(name, name, hintText, required);
    }

    /**
     *
     * @param name Element name
     * @param objectPropertyName Object property, which binded with component
     * @param hintText Hint for empty component
     * @param required
     */
    public TextFieldElement(String name, String objectPropertyName,
            String hintText, boolean required) {
        super(FormattedTextField.class, name, objectPropertyName, 
                FormattedTextField.TEXT_PROP);
        this.hintText = hintText;
        this.required = required;
    }

    /**
     * Gets created by element component as FormattedTextField
     *
     * @return
     */
    public FormattedTextField getTextField() {
        return (FormattedTextField) component;
    }

    @Override
    public void clear() {
        if (component != null) {
            getTextField().setText(null);
        }
    }

    @Override
    protected void setControlReadOnly(boolean readOnly) {
        getTextField().setEditable(readOnly);
    }

    @Override
    public void createComponent() {
        super.createComponent();
        final FormattedTextField textField = getTextField();
        textField.setHintText(hintText);
        textField.setRequired(required);      
    };    

    @Override
    public boolean isEmpty() {
        return CommonUtils.isEmpty(getTextField().getText());
    }

}
