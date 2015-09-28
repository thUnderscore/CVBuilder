package org.thUnderscore.common;

import org.thUnderscore.common.utils.XMLUtils;
import org.thUnderscore.common.utils.CommonUtils;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.xml.bind.DatatypeConverter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.thUnderscore.common.mvc.intf.EditableObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Base class for object, stored in XML node
 *
 * @author thUnderscore
 */
public class XMLObject extends PropertyChangeSupportedObject implements EditableObject {

    /**
     * Base class for representation nodes\atribtes as object properties
     */
    protected abstract class ValueContainer {

        /**
         * chached value
         */
        Object value = null;
        /**
         * this flag indicates whether or notread value or it's already cached
         * (isValueSetted == tue)
         */
        boolean isValueSetted = false;
        /**
         * Node element name
         */
        String fieldName;

        /**
         *
         * @param fieldName object field name, same as attribute\node name
         */
        public ValueContainer(String fieldName) {
            this.fieldName = fieldName;
        }

        /**
         * Gets value from appropriate node element
         *
         * @return
         */
        public Object getValue() {
            if (!isValueSetted) {
                readValue();
            }
            return value;
        }

        /**
         * Read value from appropriate node element
         */
        protected void readValue() {
            isValueSetted = true;
        }

        /**
         * Reset isValueSetted flag and cached value
         *
         * @see isValueSetted
         */
        protected void resetValue() {
            value = null;
            isValueSetted = false;
        }
    }

    /**
     * Base class for representation atributes as object properties
     */
    protected abstract class AttributeValueContainer extends ValueContainer {

        /**
         * 
         * @param fieldName object field name, same as attribute\node name
         */
        public AttributeValueContainer(String fieldName) {
            super(fieldName);
            attributeContainerList.add(this);
        }

        /**
         * Sets value to node attribute
         * @param value 
         */
        public void setValue(Object value) {
            isValueSetted = true;
            privateElement.setAttribute(fieldName, formatValue(value));
            firePropertyChange(fieldName, this.value, this.value = value);
            afterSetValue();
        }

        @Override
        protected void readValue() {
            super.readValue();
            String attrValue = privateElement.getAttribute(fieldName);
            if ("".equals(attrValue)) {
                value = null;
            } else {
                value = parseStringValue(attrValue);
            }

        }

        /**
         * Parse string sttribute value and return object appropriate to container type
         * @param value string value readed from node attribute
         * @return 
         */
        protected abstract Object parseStringValue(String value);

        /**
         * Returns string representation for container value. It will be writed 
         * node to attribute
         * @param value Object to be converted to string
         * @return 
         */
        protected abstract String formatValue(Object value);

        /**
         * Is called after external value changing
         */
        protected void afterSetValue() {
            setModified(true);
        }
    }

    /**
     * Base class for representation node as object property. 
     * Used for nested XMLObjects
     */
    protected abstract class NodeValueContainer extends ValueContainer {

        /**
         * Class which stored in node and should be instantiated
         */
        Class<? extends XMLObject> objectClass;

        /**
         *
         * @param fieldName object field name, same as subnode name
         * @param objectClass class which stored in node and should be instantiated
         */
        
        public NodeValueContainer(String fieldName, Class<? extends XMLObject> objectClass) {
            super(fieldName);
            this.objectClass = objectClass;
            nodeContainerList.add(this);
        }

        /**
         * Create instance of nested object
         * @return new object
         * @throws InstantiationException
         * @throws IllegalAccessException 
         */
        protected XMLObject newXMLObjectInstance() throws InstantiationException, IllegalAccessException {
            XMLObject newInstance = objectClass.newInstance();
            newInstance.setParentXMLObject(XMLObject.this);
            return newInstance;
        }

        @Override
        protected void readValue() {
            try {

                XMLObject object = newXMLObjectInstance();
                object.setNodeName(fieldName);
                Element element = (privateElement == null) ? null
                        : XMLUtils.findChildElement(privateElement, fieldName);
                object.setNodes(document, privateElement, element);
                value = object;
                super.readValue();
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }    

    /**
     * Base class for representation node as nested XMLObject. 
     * @see XMLObject
     */
    protected class XMLObjectValueContainer extends NodeValueContainer {
        /**
         * 
         * @param fieldName object field name, same as subnode name
         * @param objectClass class which stored in node and should be instantiated
         */
        public XMLObjectValueContainer(String fieldName, Class<? extends XMLObject> objectClass) {
            super(fieldName, objectClass);

        }

        /**
         * Gets value casted as XMLObject
         * @return XMLObject
         */
        public XMLObject getXMLObjectValue() {
            return (XMLObject) getValue();
        }
    }

    /**
     * Base class for representation node as nested XMLObjectList
     * @see XMLObjectList
     */
    protected class XMLObjectListContainer extends XMLObjectValueContainer {

        /**
         * Class which stored in subitem node and should be instantiated
         */
        Class<? extends XMLObject> listItemClass;

        /**
         * 
         * @param fieldName object field name, same as subnode name
         * @param listItemClass class which stored in subitem node and should be instantiated
         */
        public XMLObjectListContainer(String fieldName, Class<? extends XMLObject> listItemClass) {
            this(fieldName, XMLObjectList.class, listItemClass);

        }

        /**
         * 
         * @param fieldName object field name, same as subnode name
         * @param objectClass class which stored in node and should be instantiated
         * @param listItemClass class which stored in subitem node and should be instantiated
         */
        public XMLObjectListContainer(String fieldName, Class<? extends XMLObjectList> objectClass,
                Class<? extends XMLObject> listItemClass) {
            super(fieldName, objectClass);
            this.listItemClass = listItemClass;
        }

        /**
         * Gets value casted as XMLObjectList
         * @return XMLObjectList
         */
        public XMLObjectList getXMLObjectList() {
            return (XMLObjectList) getValue();
        }

        /**
         * Gets subitems list
         * @return 
         */
        public List<XMLObject> getList() {
            return getXMLObjectList().getList();
        }

        @Override
        protected XMLObject newXMLObjectInstance() throws InstantiationException, IllegalAccessException {
            XMLObject object = super.newXMLObjectInstance();
            ((XMLObjectList) object).setItemClass(listItemClass);
            return object;
        }
    }

    /**
     * Container for UUID sored in attribute
     */
    protected class UUIDValueContainer extends AttributeValueContainer {

        /**
         * 
         * @param attributeName object field name, same as attribute name
         */
        public UUIDValueContainer(String attributeName) {
            super(attributeName);
        }

        @Override
        protected UUID parseStringValue(String value) {
            return UUID.fromString(value);
        }

        @Override
        protected String formatValue(Object value) {
            return (value == null) ? null : ((UUID) value).toString().toUpperCase();
        }

        /**
         * Gets value casted as UUID
         * @return UUID
         */
        public UUID getUUIDValue() {
            return (UUID) getValue();
        }
    }

    /**
     * Container for byte[] sored in attribute
     */
    protected class ByteArrValueContainer extends AttributeValueContainer {

        /**
         * 
         * @param attributeName object field name, same as attribute name
         */
        public ByteArrValueContainer(String attributeName) {
            super(attributeName);
        }

        @Override
        protected Object parseStringValue(String value) {
            return DatatypeConverter.parseBase64Binary(value);
        }

        @Override
        protected String formatValue(Object value) {
            return CommonUtils.isEmpty((byte[]) value) ? null : DatatypeConverter.printBase64Binary((byte[]) value);
        }

        /**
         * Gets value as IputStream
         * @return InputStream
         */
        public InputStream getInputStream() {
            byte[] val = (byte[]) getValue();
            return CommonUtils.isEmpty(val) ? null : new ByteArrayInputStream(val);
        }

        /**
         * Sets value as IputStream
         * @param stream new value
         */
        public void setInputStream(InputStream stream) {
            try {
                setValue(CommonUtils.readByteArrayFromStream(stream));
            } catch (IOException ex) {
                setValue(null);
                throw new RuntimeException(ex);
            }
        }

        /**
         * Gets value casted as Image
         * @return Image
         */
        public Image getImage() {
            byte[] val = (byte[]) getValue();
            return CommonUtils.isEmpty(val) ? null : new ImageIcon(val).getImage();
        }

        /**
         * Sets value as Image
         * @param image new value
         */
        public void setImage(Image image) {
            if (image == null) {
                setValue(null);
            } else {
                int width = image.getWidth(null);
                int height = image.getHeight(null);
                BufferedImage bi = new BufferedImage(
                        width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bi.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();                
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    try{                                            
                        ImageIO.write(bi, "jpg", stream);
                        setValue(stream.toByteArray());
                    } finally {
                        stream.close();
                    }
                } catch (IOException ex) {
                    setValue(null);
                }
            }
        }
    }
    
    /**
     * Container for String sored in attribute
     */
    protected class StringValueContainer extends AttributeValueContainer {

        /**
         * 
         * @param attributeName object field name, same as attribute name
         */
        public StringValueContainer(String attributeName) {
            super(attributeName);
        }

        @Override
        protected Object parseStringValue(String value) {
            return value;
        }

        @Override
        protected String formatValue(Object value) {
            return (String) value;
        }

        /**
         * Gets value casted as String
         * @return String
         */
        public String getStringValue() {
            return (String) getValue();
        }
    }

    /**
     * Container for float sored in attribute
     */
    protected class FloatValueContainer extends AttributeValueContainer {

        public FloatValueContainer(String attributeName) {
            super(attributeName);
        }

        @Override
        protected Object parseStringValue(String value) {
            return Float.parseFloat(value);
        }

        @Override
        protected String formatValue(Object value) {
            return (value == null) ? null : ((Float) value).toString();
        }

        /**
         * Gets value casted as float
         * @return float
         */
        public float getFloatValue() {
            Object val = getValue();
            return (val == null) ? 0 : (Float) getValue();
        }
    }

    /**
     * Container for int sored in attribute
     */
    protected class IntValueContainer extends AttributeValueContainer {

        /**
         * 
         * @param attributeName object field name, same as attribute name
         */
        public IntValueContainer(String attributeName) {
            super(attributeName);
        }

        @Override
        protected Object parseStringValue(String value) {
            return Integer.parseInt(value);
        }

        @Override
        protected String formatValue(Object value) {
            return (value == null) ? null : ((Integer) value).toString();
        }

        /**
         * Gets value casted as int
         * @return int
         */
        public Integer getIntValue() {
            Object val = getValue();
            return (val == null) ? 0 : (Integer) getValue();
        }
    }

    /**
     * Container for boolean sored in attribute
     */
    protected class BoolValueContainer extends AttributeValueContainer {

        /**
         * 
         * @param attributeName object field name, same as attribute name
         */
        public BoolValueContainer(String attributeName) {
            super(attributeName);
        }

        @Override
        protected Object parseStringValue(String value) {
            return Boolean.parseBoolean(value);
        }

        @Override
        protected String formatValue(Object value) {
            return (value == null) ? null : ((Boolean) value).toString();
        }

        /**
         * Gets value casted as Boolean
         * @return Boolean
         */
        public Boolean getBoolValue() {
            Object val = getValue();
            return (val == null) ? false : (Boolean) getValue();
        }
    }
    
    /**
     * Default subitem node name
     */
    public static final String DEFAULT_NODE_NAME = "item";
    /**
     * ID field name
     */
    public static final String ID = "ID";

    /**
     * Node which contains node of curent XMLObject. If node isroot - return document
     */
    protected Node parentNode;
    /**
     * Real document node, store old properties values
     */
    protected Element realElement;
    /**
     * Temporary document node, store properties values when object is edited
     */
    protected Element privateElement;
    /**
     * Document which contains object node
     */
    protected Document document;
    /**
     * List of subobjects containers
     */
    protected List<NodeValueContainer> nodeContainerList = new ArrayList();
    /**
     * List of attributes containers
     */
    protected List<AttributeValueContainer> attributeContainerList = new ArrayList();

    /**
     * ID field. Every XML has it. If object is new or there is no appropriate
     * attribute in node, ID value will be generated
     */
    protected UUIDValueContainer id = new UUIDValueContainer(ID) {

        @Override
        protected void afterSetValue() {
            //Do nothing. Prevent isModified flag changing
        }
    };
    /**
     * Name of node, which contains XMLObject data
     */
    protected String nodeName;
    /**
     * Parent object, if object is nested
     */
    protected XMLObject parentXMLObject;
    /**
     * @see EditableObject#isModified
     */
    private boolean isModified;
    /**
     * @see EditableObject#isNewObject
     */
    private boolean newObject;

    /**
     * Gets node name
     * @return Name of node, which contains XMLObject data
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets node name
     * @param nodeName Name of node, which contains XMLObject data
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Gets ID field value. Every XML has it. If object is new or there is no appropriate
     * attribute in node, ID value will be generated
     *
     * @return UUID value
     */
    public UUID getID() {
        return id.getUUIDValue();
    }

    /**
     * Set ID value 
     * @param id UUID value
     * @see #getID
     */
    public void setID(UUID id) {
        this.id.setValue(id);
    }

    /**
     * Gets IS as string
     * @return ID as string
     * @see #getID
     */
    public String getIDAsStr() {
        return id.getValue().toString().toUpperCase();
    }

    /**
     * Gets parent object, if object is nested
     * @return parent XMLObject
     */
    public XMLObject getParentXMLObject() {
        return parentXMLObject;
    }

    /**
     * Set parent object
     * @param parentXMLObject 
     */
    protected void setParentXMLObject(XMLObject parentXMLObject) {
        this.parentXMLObject = parentXMLObject;
    }

    /**
     * Gets real document node, witch store old properties values
     * @return Node of this object in Documen. If it's new object - null
     */
    protected Element getRealElement() {
        return realElement;
    }

    /**
     * Sets realElement
     * @param realElement 
     * @see #getRealElement() 
     */
    protected void setRealElement(Element realElement) {
        this.realElement = realElement;
    }
    
    /**
     * Gets temporary document node, store properties values when object is edited
     * @return temporary node, which contain properties of object. Object store its
     * properties in this node, until he saved
     */
    protected Element getPrivateElement() {
        return privateElement;
    }

    /**
     * Gets node which contains node of curent XMLObject. 
     * @return Parent node. If node isroot - return document
     */
    public Node getParentNode() {
        return parentNode;
    }

    /**
     * Gets document which contains object node
     * @return document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Load object from root Document element. 
     * @param document Document
     */
    public void loadFromDocument(Document document) {
        if (document == null) {
            document = XMLUtils.getNewDocument(getNodeName());
        }
        setNodes(document, document, document.getDocumentElement());
    }

    /**
     * Set document, parentNode and realElement. Should be called after object creation
     * Until it object is non-working
     * @param document
     * @param parentNode
     * @param realElement null if XMLObject is new and has no node yet.
     * 
     * @see #getDocument() 
     * @see #getParentNode() 
     * @see #getRealElement()
     */
    public void setNodes(Document document, Node parentNode, Element realElement) {
        this.document = document;
        this.realElement = realElement;
        this.parentNode = parentNode;
        if (realElement == null) {
            this.privateElement = document.createElement(getNodeName());
        } else {
            this.privateElement = (Element) realElement.cloneNode(true);
        }
        if (getID() == null) {
            setID(UUID.randomUUID());
        }
    }

    @Override
    public void save() {
        for (NodeValueContainer container : nodeContainerList) {
            XMLObject object = (XMLObject) container.getValue();
            object.save();
        }
        Node cloneNode = privateElement.cloneNode(true);
        if (realElement == null) {
            parentNode.appendChild(cloneNode);
        } else {
            parentNode.removeChild(realElement);
            parentNode.appendChild(cloneNode);
            //parentNode.replaceChild(cloneNode, realElement);           
        }
        realElement = (Element) cloneNode;
        setModified(false);
        setNewObject(false);
    }

    @Override
    public void cancel() {
        if (realElement == null) {
            privateElement = document.createElement(getNodeName());
        } else {
            XMLUtils.copyNode(realElement, privateElement);
        }
        for (NodeValueContainer container : nodeContainerList) {
            XMLObject object = (XMLObject) container.getValue();
            container.resetValue();
            object.cancel();
        }
        for (AttributeValueContainer container : attributeContainerList) {
            container.resetValue();

        }
        isModified = false;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public boolean isNewObject() {
        return newObject;
    }

    @Override
    public void setNewObject(boolean newObject) {
        this.newObject = newObject;
    }

    public void setModified(boolean isModified) {
        this.isModified = isModified;
        if ((parentXMLObject != null) && isModified) {
            parentXMLObject.setModified(true);
        }

    }

    /**
     * Save document to stream
     * @param stream 
     */
    public void saveToStream(OutputStream stream) {
        if ((stream == null) || (document == null)) {
            return;
        }
        save();
        try {
            Source source = new DOMSource(document);
            Result result = new StreamResult(stream);
            Transformer transformer = XMLUtils.getTransformerFactory().newTransformer();
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + getID() + "]";
    }
}
