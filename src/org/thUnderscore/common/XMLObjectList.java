package org.thUnderscore.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.thUnderscore.common.mvc.intf.ListInclusiveObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Base class for object with sublist, stored in XML node
 *
 * @author thUnderscore
 */
public class XMLObjectList extends XMLObject implements ListInclusiveObject {

    /*
     Nodes and properies names
     */
    public static final String ITEM_NODE_NAME = "item";
    public static final String LIST = "list";

    /**
     * Subitems list
     */
    protected List<XMLObject> list;
    /**
     * Class which stored in subitem node and should be instantiated
     */
    protected Class<? extends XMLObject> listItemClass;

    public XMLObjectList() {
        this.list = new ArrayList<XMLObject>();
    }

    @Override
    public List<XMLObject> getList() {
        return list;
    }

    /**
     * Create and add to list new subitem
     * @param element node which contains subitem data
     * @return created XMLObject
     */
    protected XMLObject addItem(Element element) {
        XMLObject object = createItem(element);
        list.add(object);
        return object;

    }

    /**
     * Create and initialize new subitem
     * @param element node which contains subitem data
     * @return created XMLObject
     */
    protected XMLObject createItem(Element element) {
        XMLObject object;
        try {
            object = listItemClass.newInstance();
            object.setParentXMLObject(this);
            object.setNodeName(ITEM_NODE_NAME);
            object.setNodes(document, this.privateElement, element);

        } catch (InstantiationException  ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return object;

    }

    @Override
    public Object createItem() {
        return createItem(null);
    }

    @Override
    public void itemRemoved(Object item) {

        XMLObject xmlObject = (XMLObject) item;
        if ((privateElement != null) && (xmlObject.realElement != null)) {
            privateElement.removeChild(xmlObject.realElement);
        }

        setModified(true);
    }

    @Override
    public void itemAdded(Object item) {

    }

    @Override
    public void itemSwaped(int i, int j) {

    }

    @Override
    public void save() {
        XMLObject object;
        for (Iterator<XMLObject> it = list.iterator(); it.hasNext();) {
            object = it.next();
            object.save();
        }
        /*for (Iterator<XMLObject> it = list.iterator(); it.hasNext();) {
         object = it.next();
          

         }*/
        super.save();
    }

    @Override
    public void setNodes(Document document, Node parentNode, Element realElement) {
        super.setNodes(document, parentNode, realElement);
        list.clear();
        if (privateElement != null) {
            for (Node child = privateElement.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child instanceof Element && ITEM_NODE_NAME.equals(child.getNodeName())) {
                    addItem((Element) child);
                }
            }
        }
    }

    /**
     * Gets listItemClass
     * @return listItemClass
     * @see #listItemClass
     */
    public Class<? extends XMLObject> getListItemClass() {
        return listItemClass;
    }

    /**
     * Sets listItemClass
     * @param listItemClass
     * @see #listItemClass
     */
    public void setItemClass(Class listItemClass) {
        this.listItemClass = listItemClass;
    }
}
