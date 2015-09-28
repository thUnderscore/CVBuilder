package org.thUnderscore.common.mvc.swing.elements;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

/**
 * Element which creates JTabbedPane
 *
 * @author thUnderscore
 */
public class TabbedPaneElement extends BaseElement {

    /**
     * Suffix for tab component
     */
    public static final String SUFFIX = "Tab";

    /**
     * Class which define tab of tabbed pane
     */
    protected static class Tab {

        /**
         * @see #getName()
         */
        private final String name;
        /**
         * @see #getTitle()
         */
        private String title;
        /**
         * @see #getElementName() 
         */
        private final String elementName;
        /**
         * Created TabElement
         */
        private TabElement element;

        /**
         * 
         * @param name tab name
         * @param elementName Element name. If is specified, tab display this element. Element
         * @param title tab title
         */
        public Tab(String name, String elementName, String title) {
            this.name = ((name == null) ? elementName + SUFFIX : name);
            this.title = title;
            this.elementName = elementName;
        }

        /**
         * Gets tab name. if not defined in constructor, will be setted as elementName +
         * SUFFIX
         *
         * @return
         */
        public String getName() {
            return name;
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
         * Sets tab title
         * @param title 
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * Gets vame of element which will e placed on tab
         * @return 
         */
        public String getElementName() {
            return elementName;
        }

    }

    /**
     * List of tab
     */
    protected List<Tab> list = new ArrayList<Tab>();

    public TabbedPaneElement(String name) {
        super(JTabbedPane.class, name);
    }

    /**
     * Gets created by element component as JTabbedPane
     * @return 
     */
    public JTabbedPane getTabbedPane() {
        return (JTabbedPane) getComponent();
    }

    /**
     * Add tab definition
     * @param name tab name
     * @param title tab titl 
     * @return same TabbedPaneElement
     */
    public TabbedPaneElement addTabDescription(String name, String title) {
        list.add(new Tab(name, null, title));
        return this;
    }

    /**
     *  Add tab definition
     * @param elementName element which will be placed on tab name 
     * @param title tab title
     * @return 
     */
    public TabbedPaneElement addTabDescriptionByViewElement(String elementName, String title) {
        list.add(new Tab(null, elementName, title));
        return this;
    }

    /**
     * Add JComponent to JTabbedPane
     * @param tabElement 
     */
    protected void addTabToComponent(TabElement tabElement) {
        getTabbedPane().add(tabElement.getTitle(), tabElement.getComponentForPlacement());
    }
    
    @Override
    public void createComponent() {
        super.createComponent();
        for (int i = 0; i < list.size(); i++) {
            Tab tab = list.get(i);
            TabElement tabElement = new TabElement(tab.name, tab.title, tab.elementName);
            view.addElement(tabElement);
            tab.element = tabElement;

        }
    }

    @Override
    public void placeComponents(JComponent container) {
        super.placeComponents(container);
        for (int i = 0; i < list.size(); i++) {
            this.addTabToComponent(list.get(i).element);
        }
    }

}
