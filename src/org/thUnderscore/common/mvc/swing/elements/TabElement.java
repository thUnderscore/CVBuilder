package org.thUnderscore.common.mvc.swing.elements;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Element which creates tab in TabbedPane. Can display specified view element
 * defined in view
 *
 * @author thUnderscore
 */
public class TabElement extends PanelElement {

    /**
     * @see #getTitle()
     */
    String title;
    /**
     * @see #getElementName()
     */
    String elementName;

    /**
     * 
     * @param name Element name
     * @param title tab title
     * @param elementName Element name. If is specified, tab display this element. Element
     * should be defined in view
     */
    public TabElement(String name, String title, String elementName) {
        super(name);
        this.title = title;
        this.elementName = elementName;
    }

    /**
     * Gets tab title
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets element name. If is specified, tab display this element. Element
     * should be defined in view
     *
     * @return elemtn name
     */
    public String getElementName() {
        return elementName;
    }

    @Override
    public void createComponent() {
        super.createComponent();
        if (elementName != null) {
            JComponent elementComponent = view.getComponentForPlacementByName(elementName);
            JPanel panel = getPanel();
            panel.setLayout(new BorderLayout());
            panel.add(elementComponent, BorderLayout.CENTER);
        }
    }
}
