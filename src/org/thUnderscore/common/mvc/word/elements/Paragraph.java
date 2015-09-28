package org.thUnderscore.common.mvc.word.elements;

import java.awt.Color;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Elements which define paragraph in word document
 *
 * @author thUnderscore
 */
public class Paragraph extends BaseElement {

    /**
     * POI paragraph
     */
    XWPFParagraph para;
    /**
     * Vertical text alignment
     */
    private TextAlignment vAlign = null;

    public Paragraph() {
        this(null, null);
    }

    /**
     * Create and set objectPropertyName and valuePropertyName
     *
     * @param valuePropertyName name of object property for value evaluation, if
     * value is not predefined.
     * @see #setValuePropertyName(java.lang.String)
     */
    public Paragraph(String valuePropertyName) {
        this(null, valuePropertyName);
    }

    /**
     * Create and set objectPropertyName and valuePropertyName
     *
     * @param objectPropertyName paren element object property name
     * @param valuePropertyName name of object property for value evaluation, if
     * value is not predefined.
     * @see #setValuePropertyName(java.lang.String)
     */
    public Paragraph(String objectPropertyName, String valuePropertyName) {
        super(objectPropertyName, CommonUtils.getPropertyExpressionByName(valuePropertyName));
    }

    /**
     * Add Run element to structure
     *
     * @param run
     * @return same element
     */
    public Paragraph addRun(Run run) {
        add(run);
        return this;
    }

    /**
     * Add Image element to structure
     *
     * @param image
     * @return same element
     */
    public Paragraph addImage(Image image) {
        add(image);
        return this;
    }

    /**
     * Add ParagraphContentGroup element to structure
     *
     * @param group
     * @return same element
     */
    public Paragraph addGroup(ParagraphContentGroup group) {
        add(group);
        return this;
    }

    /**
     * Add ParagraphContainerRepeater element to structure
     *
     * @param repeater
     * @return same element
     */
    public Paragraph addRepeater(ParagraphContainerRepeater repeater) {
        add(repeater);
        return this;
    }

    /**
     * Write structure to POI object
     * @param para 
     */
    protected void writeToWordDoc(XWPFParagraph para) {
        this.para = para;
        if (para != null) {
            writeToWordDoc();
        }
    }

    /**
     * Set paragraph text vertical alignment
     * @param vAlign
     * @return 
     */
    public Paragraph setVAlign(TextAlignment vAlign) {
        this.vAlign = vAlign;
        return this;
    }

    @Override
    public Paragraph setFontStyle(FontStyle fontStyle) {
        return (Paragraph) super.setFontStyle(fontStyle);
    }

    @Override
    public Paragraph setBold(Boolean bold) {
        return (Paragraph) super.setBold(bold);
    }

    @Override
    public Paragraph setStrike(Boolean strike) {
        return (Paragraph) super.setStrike(strike);
    }

    @Override
    public Paragraph setItalic(Boolean italic) {
        return (Paragraph) super.setItalic(italic);
    }

    @Override
    public Paragraph setFontFamily(String fontFamily) {
        return (Paragraph) super.setFontFamily(fontFamily);
    }

    @Override
    public Paragraph setFontSize(Integer fontSize) {
        return (Paragraph) super.setFontSize(fontSize);
    }

    @Override
    public Paragraph setUnderline(UnderlinePatterns underline) {
        return (Paragraph) super.setUnderline(underline);
    }

    @Override
    public Paragraph setColor(Color color) {
        return (Paragraph) super.setColor(color);
    }

    @Override
    protected void internalWriteToWordDoc() {
        super.internalWriteToWordDoc();
        if (vAlign != null) {
            para.setVerticalAlignment(vAlign);
        }
    }

    @Override
    protected void writeChildElement(BaseElement child) {
        if (child instanceof ParagraphContainerRepeater) {
            ((ParagraphContainerRepeater) child).writeToWordDoc();
        } else if (child instanceof ParagraphContent) {
            ((ParagraphContent) child).writeToWordDoc(para);
        }

        super.writeChildElement(child);
    }

    @Override
    protected void afterWrite() {
        super.afterWrite();
        para = null;
    }

    @Override
    protected XWPFDocument getDocument() {
        return para == null ? null : para.getDocument();
    }

}
