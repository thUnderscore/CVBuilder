package org.thUnderscore.common.mvc.word.elements;

import java.awt.Color;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;

/**
 * Element which represent group fo ParagraphContent
 *
 * @author thUnderscore
 */
public class ParagraphContentGroup extends ParagraphContent {

    public ParagraphContentGroup() {
        this(null);
    }

    /**
     * Create and set objectPropertyName and valueExpression
     *
     * @param objectPropertyName paren element object property name predefined.
     * @see #setObjectPropertyName(java.lang.String)
     */
    public ParagraphContentGroup(String objectPropertyName) {
        super(objectPropertyName, null);
    }

    /**
     * Add Run element to structure
     *
     * @param run
     * @return same element
     */
    public ParagraphContentGroup addRun(Run run) {
        add(run);
        return this;
    }

    /**
     * Add Image element to structure
     *
     * @param image
     * @return same element
     */
    public ParagraphContentGroup addImage(Image image) {
        add(image);
        return this;
    }

    /**
     * Add ParagraphContentGroup element to structure
     *
     * @param group
     * @return same element
     */
    public ParagraphContentGroup addGroup(ParagraphContentGroup group) {
        add(group);
        return this;
    }

    @Override
    public ParagraphContentGroup setFontStyle(FontStyle fontStyle) {
        return (ParagraphContentGroup) super.setFontStyle(fontStyle);
    }

    @Override
    public ParagraphContentGroup setBold(Boolean bold) {
        return (ParagraphContentGroup) super.setBold(bold);
    }

    @Override
    public ParagraphContentGroup setStrike(Boolean strike) {
        return (ParagraphContentGroup) super.setStrike(strike);
    }

    @Override
    public ParagraphContentGroup setItalic(Boolean italic) {
        return (ParagraphContentGroup) super.setItalic(italic);
    }

    @Override
    public ParagraphContentGroup setFontFamily(String fontFamily) {
        return (ParagraphContentGroup) super.setFontFamily(fontFamily);
    }

    @Override
    public ParagraphContentGroup setFontSize(Integer fontSize) {
        return (ParagraphContentGroup) super.setFontSize(fontSize);
    }

    @Override
    public ParagraphContentGroup setUnderline(UnderlinePatterns underline) {
        return (ParagraphContentGroup) super.setUnderline(underline);
    }

    @Override
    public ParagraphContentGroup setColor(Color color) {
        return (ParagraphContentGroup) super.setColor(color);
    }

    @Override
    protected void writeChildElement(BaseElement child) {
        ((ParagraphContent) child).writeToWordDoc(para);
        super.writeChildElement(child);
    }
}
