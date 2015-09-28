package org.thUnderscore.common.mvc.swing.elements;

import org.thUnderscore.common.ui.components.ButtonsPanel;

/**
 * Element which create ButtonsPanel
 * 
 * @see ButtonsPanel
 * @author thUnderscore
 */
public class ButtonsPanelElement extends BaseElement {

    /**
     * 
     * @param name lement name
     */
    public ButtonsPanelElement(String name) {
        super(ButtonsPanel.class, name);
    }

    @Override
    public void createComponent() {
        super.createComponent();
        ((ButtonsPanel)component).addActionListener(view);
        
    }

}
