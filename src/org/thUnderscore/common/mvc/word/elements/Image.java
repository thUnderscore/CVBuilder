package org.thUnderscore.common.mvc.word.elements;

import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.thUnderscore.common.mvc.word.POIUtils;

/**
 * Element which can display images in word document
 *
 * @author thUnderscore
 */
public class Image extends ParagraphContent {

    /**
     * Image width in word document
     */
    int width;
    /**
     * Image height in word document
     */
    int height;

    /**
     *
     * @param width Image width in word document
     * @param height Image height in word document
     */
    public Image(int width, int height) {
        this(null, null, width, height);
    }

    /**
     *
     * @param valuePropertyName
     * @param width Image width in word document
     * @param height Image height in word document
     * @see #setValueExpression(java.lang.String)
     */
    public Image(String valuePropertyName, int width, int height) {
        this(null, valuePropertyName, width, height);
    }

    /**
     *
     * @param objectPropertyName paren element object property name
     * @param valuePropertyName name of object property for value evaluation.
     * Value should contains image
     * @param width Image width in word document
     * @param height Image height in word document
     * @see #setValueExpression(java.lang.String)
     */
    public Image(String objectPropertyName, String valuePropertyName, int width, int height) {
        super(objectPropertyName, valuePropertyName);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void internalWriteToWordDoc() {
        super.internalWriteToWordDoc();
        java.awt.Image image = (java.awt.Image) getValue();
        if (image != null) {
            XWPFRun rh = para.createRun();
            POIUtils.createPicture(rh, image, width, height);
        }
    }

    @Override
    public Image setValue(Object value) {
        return (Image) super.setValue(value);
    }

    @Override
    public Image setValueExpression(String valueExpression) {
        return (Image) super.setValueExpression(valueExpression);
    }

    @Override
    public Image setValuePropertyName(String valuePropertyName) {
        return (Image) super.setValuePropertyName(valuePropertyName);
    }

    @Override
    public Image setObject(Object object) {
        return (Image) super.setObject(object);
    }

    @Override
    public Image setObjectPropertyName(String objectPropertyName) {
        return (Image) super.setObjectPropertyName(objectPropertyName);
    }

}
