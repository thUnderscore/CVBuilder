package org.thUnderscore.common.mvc.swing.elements;

import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.ui.components.TextArea;

/**
 * Element which creates TextArea
 * @author thUnderscore
 */
public class TextAreaElement extends PropertyElement {

    
    
    /**
     * hint for empty TextArea
     */
    String hintText = null;

    /**
     * 
     * @param name Element name
     */
    public TextAreaElement(String name) {
        this(name, null, false);
    }
    
    /**
     *
     * @param name Element name
     * @param hintText Hint for empty component
     * @param required Is object property required
     */
    public TextAreaElement(String name, String hintText, boolean required) {
        this(name, name, hintText);
        this.hintText = hintText;
        this.required = required;
    }

    /**
     *
     * @param name Element name
     * @param objectPropertyName Object property, which binded with component
     * @param hintText Hint for empty component
     */
    public TextAreaElement(String name, String objectPropertyName, String hintText) {
        super(TextArea.class, name, objectPropertyName, TextArea.TEXT_PROP);
        setWrapScrollPane(true);
    }

    /**
     * Gets created by element component as TextArea
     * @return 
     */
    public TextArea getTextArea(){
        return (TextArea) component;                
    }
    
    @Override
    public void createComponent()  {
        super.createComponent();
        TextArea textArea = getTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setHintText(hintText);
        textArea.setRequired(required);        
    }

    @Override
    public void clear() {
        if (component != null) {
            getTextArea().setText(null);
        }
    }
    
    @Override
    protected void setControlReadOnly(boolean readOnly){
        getTextArea().setEditable(readOnly);
    }

    @Override
    public boolean isEmpty() {
        return CommonUtils.isEmpty(getTextArea().getText());
    }
}
