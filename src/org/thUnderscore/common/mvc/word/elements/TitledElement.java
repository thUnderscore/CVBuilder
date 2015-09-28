package org.thUnderscore.common.mvc.word.elements;

import java.awt.Color;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 * Base class for elements with title
 *
 * @author thUnderscore
 */
public abstract class TitledElement extends BaseElement {

    /**
     * title definition
     */
    protected ParagraphContentGroup title;
    /**
     * is title enabled
     */
    protected boolean titleEnabled;

    public TitledElement(String objectPropertyName, String valueExpression) {
        super(objectPropertyName, valueExpression);
    }

    /**
     * Sets title structure. It will consist of one Run
     *
     * @param title title text
     * @param addBreak add break after title
     * @param fontStyle title font style
     * @return same element
     */
    public TitledElement setTitle(String title, boolean addBreak, FontStyle fontStyle) {
        return setTitle(true, title, addBreak, fontStyle);
    }

    /**
     * Sets title structure. It will consist of one Run
     *
     * @param title title text
     * @param fontStyle title font style
     * @return same element
     */
    public TitledElement setTitle(String title, FontStyle fontStyle) {
        return setTitle(title, false, fontStyle);
    }

    /**
     * Sets title structure. It will consist of one Run
     *
     * @param enabled is title enabled
     * @param title title text
     * @param addBreak add break after title
     * @param fontStyle title font style
     * @return same element
     */
    public TitledElement setTitle(boolean enabled, String title, boolean addBreak, FontStyle fontStyle) {
        return setTitle(enabled, new Run(addBreak, title), fontStyle);
    }

    /**
     * Sets title structure. It will consist of one Run
     *
     * @param enabled is title enabled
     * @param title title text
     * @param addBreak add break after title
     * @return same element
     */
    public TitledElement setTitle(boolean enabled, String title, boolean addBreak) {
        return setTitle(enabled, title, addBreak, null);
    }

    /**
     * Sets title structure.
     *
     * @param titleContent title structure
     * @param fontStyle title font style
     * @return
     */
    public TitledElement setTitle(ParagraphContent titleContent, FontStyle fontStyle) {
        return setTitle(true, titleContent, fontStyle);
    }

    /**
     * Sets title structure
     *
     * @param title title text
     * @param addBreak add break after title
     * @return same element
     */
    public TitledElement setTitle(String title, boolean addBreak) {
        return setTitle(true, title, addBreak);
    }

    /**
     * Sets title structure
     *
     * @param title title text
     * @return same element
     */
    public TitledElement setTitle(String title) {
        return setTitle(title, false);
    }

    /**
     * Sets title structure.
     *
     * @param enabled is title enabled
     * @param titleContent title structure
     * @param fontStyle title font style
     * @return
     */
    public TitledElement setTitle(boolean enabled, ParagraphContent titleContent, FontStyle fontStyle) {
        this.titleEnabled = enabled;
        title.clear();
        title.add(titleContent);
        setTitleFontStyle(fontStyle);
        return this;
    }

    /**
     * Sets title font style
     *
     * @param fontStyle
     * @return same lement
     */
    public TitledElement setTitleFontStyle(FontStyle fontStyle) {
        title.setFontStyle(fontStyle);
        return this;
    }

    /**
     * Gets title font style
     *
     * @return FoontStyle object
     */
    protected FontStyle getTitleFontStyle() {
        return title.forceGetFontStyle();
    }

    /**
     * Gets title font name e.g. Arial
     *
     * @return title font name
     */
    public String getTitleFontFamily() {
        return getTitleFontStyle().getFontFamily();
    }

    /**
     * Sets title font name e.g. Arial
     *
     * @param fontFamily
     * @return same element
     */
    public TitledElement setTitleFontFamily(String fontFamily) {
        getTitleFontStyle().setFontFamily(fontFamily);
        return this;
    }

    /**
     * Gets title font size
     *
     * @return title font size
     */
    public Integer getTitleFontSize() {
        return getTitleFontStyle().getFontSize();
    }

    /**
     * Sets title font size
     *
     * @param fontSize
     * @return same element
     */
    public TitledElement setTitleFontSize(Integer fontSize) {
        getTitleFontStyle().setFontSize(fontSize);
        return this;
    }

    /**
     * Gets is title font bold
     *
     * @return is title font bold
     */
    public Boolean getTitleIsBold() {
        return getTitleFontStyle().isBold();
    }

    /**
     * Sets is title font bold
     *
     * @param bold
     * @return same element
     */
    public TitledElement setTitleBold(Boolean bold) {
        getTitleFontStyle().setBold(bold);
        return this;
    }

    /**
     * Gets title font underline style
     *
     * @return title font underline style
     */
    public UnderlinePatterns getTitleUnderline() {
        return getTitleFontStyle().getUnderline();
    }

    /**
     * Sets title font underline style
     *
     * @param underline title font underline style
     * @return same element
     */
    public TitledElement setTitleUnderline(UnderlinePatterns underline) {
        getTitleFontStyle().setUnderline(underline);
        return this;
    }

    /**
     * Gets is title font italic
     *
     * @return is title font italic
     */
    public Boolean getTitleIsItalic() {
        return getTitleFontStyle().isItalic();
    }

    /**
     * Sets is title font italic
     *
     * @param italic is title font italic
     * @return same element
     */
    public TitledElement setTitleItalic(Boolean italic) {
        getTitleFontStyle().setItalic(italic);
        return this;
    }

    /**
     * Gets is title font striked
     *
     * @return is title font striked
     */
    public Boolean getTitleIsStrike() {
        return getTitleFontStyle().isStrike();
    }

    /**
     * Sets is title font striked
     *
     * @param strike
     * @return same element
     */
    public TitledElement setTitleIsStrike(Boolean strike) {
        getTitleFontStyle().setStrike(strike);
        return this;
    }

    /**
     * Gets title font color
     *
     * @return font color
     */
    public Color getTitleColor() {
        return getTitleFontStyle().getColor();
    }

    /**
     * Sets title font color
     *
     * @param color
     * @return same element
     */
    public TitledElement setTitleColor(Color color) {
        getTitleFontStyle().setColor(color);
        return this;
    }

    /**
     * Wriite title to word document
     */
    protected void writeTitle() {
        if (!titleEnabled) {
            return;
        }
        title.writeToWordDoc(getTitleParagraph());
    }

    @Override
    protected void internalWriteToWordDoc() {
        super.internalWriteToWordDoc();
        writeTitle();
    }

    /**
     * Gets paragraph where title will be written
     *
     * @return
     */
    protected abstract XWPFParagraph getTitleParagraph();

}
