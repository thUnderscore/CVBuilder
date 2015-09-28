package org.thUnderscore.common.mvc.swing.elements;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

/**
 * Element which creates sequence of element
 *
 * @author thUnderscore
 */
public class SequentialPanelElement extends PanelElement {

    /**
     * The axis to lay out components Sequence along. Can be one of:
     * BoxLayout.X_AXIS, BoxLayout.Y_AXIS, BoxLayout.LINE_AXIS or
     * BoxLayout.PAGE_AXIS
     *
     */
    protected int axis;

    /**
     * Element of the sequence. Can define new subsequence or define name of
     * view elemnt which which is part of sequence
     */
    protected static class SequnatialItem {

        /**
         * @see #getElement()
         */
        protected final SequentialPanelElement element;
        /**
         * @see #getElementName()
         */
        protected final String elementName;
        /**
         * @see #isIsNewSequential()
         */
        protected final boolean newSequential;

        /**
         *
         * @param elementName View element name which should be displayed by
         * this sequence item
         */
        public SequnatialItem(String elementName) {
            this.element = null;
            this.elementName = elementName;
            this.newSequential = false;
        }

        /**
         *
         * @param element subsequence element
         */
        private SequnatialItem(SequentialPanelElement element) {
            this.element = element;
            this.elementName = null;
            this.newSequential = true;
        }

        /**
         * Gets view element name which should be displayed by this item
         *
         * @return view element name if newSequential == false and null
         * otherwise
         */
        public String getElementName() {
            return elementName;
        }

        /**
         * Gets subsequence element
         *
         * @return subsequence element if newSequential == true and null
         * otherwise
         */
        public SequentialPanelElement getElement() {
            return element;
        }

        /**
         * Define is SequnatialItem subsequence or other view element
         *
         * @return
         */
        public boolean isIsNewSequential() {
            return newSequential;
        }

    }

    /**
     * Elements of sequence
     */
    protected List<SequnatialItem> list = new ArrayList<SequnatialItem>();

    public SequentialPanelElement(String name, int axis) {
        this(name, axis, false, null);
    }

    public SequentialPanelElement(int axis) {
        this(null, axis);
    }

    /**
     *
     * @param name Element name
     * @param axis The axis to lay out components Sequence along. Can be one of:
     * BoxLayout.X_AXIS, BoxLayout.Y_AXIS, BoxLayout.LINE_AXIS or
     * BoxLayout.PAGE_AXIS
     * @param hasBorder if true, component  border will be setted to TitledBorder
     * @param borderTitle border title
     */
    public SequentialPanelElement(String name, int axis,
            boolean hasBorder, String borderTitle) {
        super(name);
        this.axis = axis;
        this.setBorder(hasBorder, borderTitle);
    }

    /**
     * Add subsequence
     *
     * @param axis subsequence axis
     * @param borderTitle subsequence border title
     * @return same SequentialPanelElement
     */
    public SequentialPanelElement addSubsequentiall(int axis, String borderTitle) {
        return addSubsequential(null, axis, borderTitle);
    }

    /**
     * Add subsequence
     *
     * @param name subsequence element name
     * @param axis subsequence axis
     * @param borderTitle subsequence border title
     * @return same SequentialPanelElement
     */
    public SequentialPanelElement addSubsequential(String name, int axis, String borderTitle) {
        return addSubsequential(name, axis, true, borderTitle);
    }

    /**
     * Add subsequence
     *
     * @param axis subsequence axis
     * @return same SequentialPanelElement
     */
    public SequentialPanelElement addSubsequential(int axis) {
        return addSubsequential(null, axis, false, null);
    }

    /**
     * Add subsequence
     *
     * @param name subsequence element name
     * @param axis subsequence axis
     * @param hasBorder has subsequence border
     * @param borderTitle subsequence border title
     * @return same SequentialPanelElement
     */
    public SequentialPanelElement addSubsequential(String name, int axis,
            boolean hasBorder, String borderTitle) {
        addSubsequential(new SequentialPanelElement(name, axis, hasBorder, borderTitle));
        return this;

    }

    /**
     * Add subsequence
     *
     * @param element should be instance of SequentialPanelElement
     * @return same SequentialPanelElement
     */
    public SequentialPanelElement addSubsequential(BaseElement element) {
        if (element instanceof SequentialPanelElement) {
            list.add(new SequnatialItem((SequentialPanelElement) element));
        }
        return this;
    }

    /**
     * Add element which is already defined in view
     *
     * @param elementName view element name
     * @return SequentialPanelElement
     */
    public SequentialPanelElement addElement(String elementName) {
        list.add(new SequnatialItem(elementName));
        return this;
    }

    @Override
    public void createComponent() {
        super.createComponent();
        for (int i = 0; i < list.size(); i++) {
            SequnatialItem item = list.get(i);
            if (item.newSequential) {
                view.addElement(item.getElement());
            }
        }
    }

    @Override
    public void placeComponents(JComponent container) {
        super.placeComponents(container);
        component.setLayout(new BoxLayout(component, axis));
        for (int i = 0; i < list.size(); i++) {
            SequnatialItem item = list.get(i);
            if (i > 0) {
                //TODO
                component.add(Box.createRigidArea(new Dimension(5, 5)));
            }
            BaseElement element;
            if (item.newSequential) {
                element = item.element;
            } else {
                element = view.getElementByName(item.getElementName());
            }
            component.add(element.getComponentForPlacement());
        }
    }

}
