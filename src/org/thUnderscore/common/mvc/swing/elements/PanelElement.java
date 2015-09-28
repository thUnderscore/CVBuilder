package org.thUnderscore.common.mvc.swing.elements;

import javax.swing.JPanel;

/**
 * Element which creates JPanel
 * @author thUnderscore
 */
public class PanelElement extends BaseElement {

    /**
     * 
     * @param name Element name
     */
    public PanelElement(String name) {
        super(JPanel.class, name);
    }

    public JPanel getPanel() {
        return (JPanel) getComponent();
    }

}
