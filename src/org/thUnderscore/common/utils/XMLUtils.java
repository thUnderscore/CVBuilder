package org.thUnderscore.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML utils
 * @author thUnderscore
 */
public class XMLUtils {
    protected static TransformerFactory transformerFactory = null;
    protected static DocumentBuilderFactory documentBuilderFactory = null;
    protected static DocumentBuilder documentBuilder = null;

    /**
     * Get cached TransformerFactory
     * @return TransformerFactory
     */
    public static TransformerFactory getTransformerFactory() {
        if (transformerFactory == null) {
            transformerFactory = TransformerFactory.newInstance();
        }
        return transformerFactory;
    }

    /**
     * Get cached DocumentBuilderFactory
     * @return DocumentBuilderFactory
     */
    public static DocumentBuilderFactory getDocumentBuilderFactory() {
        if (documentBuilderFactory == null) {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
        }
        return documentBuilderFactory;
    }

    /**
     * Get cached DocumentBuilder
     * @return DocumentBuilder
     */
    public static DocumentBuilder getDocumentBuilder() {
        if (documentBuilder == null) {
            try {
                documentBuilder = getDocumentBuilderFactory().newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            }
        }
        return documentBuilder;
    }

    /**
     * Represent xml node as string
     * @param node to be represented
     * @return node text
     */
    public static String xmlToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            Transformer transformer = getTransformerFactory().newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerException e) {
            throw new RuntimeException("Can't transfor XML to String", e);
        }
    }

    /**
     * Create new Document
     * @param rootElementName
     * @return root node name
     */
    public static Document getNewDocument(String rootElementName) {
        Document document = getDocumentBuilder().newDocument();
        if (CommonUtils.isNotEmpty(rootElementName)) {
            Element root = document.createElement(rootElementName);
            document.appendChild(root);
        }
        return document;
    }

    /**
     * Gets node index
     * @param parentNode note in which the search
     * @param node seeking node
     * @return node index. -1 if node does not exists
     */
    public static int getNodeIndex(Node parentNode, Node node) {
        NodeList list = parentNode.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).equals(node)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Copy node content
     * @param source source node
     * @param dest destination node
     */
    public static void copyNode(Element source, Element dest) {
        NamedNodeMap attr = dest.getAttributes();
        for (int i = 0; i < attr.getLength(); i++) {
            attr.removeNamedItem(attr.item(i).getNodeName());
        }
        attr = source.getAttributes();
        Node node;
        for (int i = 0; i < attr.getLength(); i++) {
            node = attr.item(i);
            dest.setAttribute(node.getNodeName(), node.getNodeValue());
        }
    }

    /**
     * Find child element by name
     * @param element parent element
     * @param childName child name
     * @return 
     */
    public static Element findChildElement(Element element, String childName) {
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element && childName.equals(child.getNodeName())) {
                return (Element) child;
            }
        }
        return null;
    }

    /**
     * Find child element and create if not exists
     * @param element parent element
     * @param childName child name
     * @return 
     */
    public static Element getChildElement(Element element, String childName) {
        Element result = findChildElement(element, childName);
        if (element == null) {
            result = element.getOwnerDocument().createElement(childName);
            element.appendChild(result);
        }
        return result;
    }

    /**
     * Delete child element by name
     * @param element parent element
     * @param childName child name
     */
    public static void removeChild(Element element, String childName) {
        Element childElement = findChildElement(element, childName);
        if (childElement != null) {
            childElement.removeChild(element);
        }
    }
    
}
