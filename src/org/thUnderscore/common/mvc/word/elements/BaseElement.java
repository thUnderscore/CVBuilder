package org.thUnderscore.common.mvc.word.elements;

import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jdesktop.beansbinding.ELProperty;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Base element of MS Word view srtucture
 *
 * @author thUnderscore
 */
public class BaseElement {

    /**
     * Font style description. Is inner class. If some font characteristic is
     * not specified, it gets from font of parent element
     */
    public class FontStyle {

        /**
         * @see #getFontFamily()
         */
        String fontFamily = null;
        /**
         * @see #getFontSize()
         */
        Integer fontSize = null;
        /**
         * @see #isBold()
         */
        Boolean bold = null;
        /**
         * @see #getUnderline()
         */
        UnderlinePatterns underline = null;
        /**
         * @see #isItalic()
         */
        Boolean italic = null;
        /**
         * @see #isStrike()
         */
        Boolean strike = null;
        /**
         * @see #getColor()
         */
        Color color = null;

        /**
         * Create font style with empty fields
         */
        public FontStyle() {
        }

        /**
         * Create and copy filds values from fontStyle
         * @param fontStyle source for copy
         */
        public FontStyle(FontStyle fontStyle) {
            this();
            if (fontStyle != null) {
                this.fontFamily = fontStyle.fontFamily;
                this.fontSize = fontStyle.fontSize;
                this.bold = fontStyle.bold;
                this.underline = fontStyle.underline;
                this.italic = fontStyle.italic;
                this.strike = fontStyle.strike;
                this.color = fontStyle.color;
            }
        }

        /**
         * Gets font name e.g. Arial
         *
         * @return font name
         */
        public String getFontFamily() {
            return fontFamily == null ? getParentFontFamily() : fontFamily;
        }

        /**
         * Sets font name e.g. Arial
         *
         * @param fontFamily
         * @return same FontStyle
         */
        public FontStyle setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }

        /**
         * Gets font size
         *
         * @return font size
         */
        public Integer getFontSize() {
            return fontSize == null ? getParentFontSize() : fontSize;
        }

        /**
         * Sets font size
         *
         * @param fontSize
         * @return same FontStyle
         */
        public FontStyle setFontSize(Integer fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        /**
         * Gets is font bold
         *
         * @return
         */
        public Boolean isBold() {
            return bold == null ? getParentIsBold() : bold;
        }

        /**
         * Sets is font bold
         *
         * @param bold
         * @return same FontStyle
         */
        public FontStyle setBold(Boolean bold) {
            this.bold = bold;
            return this;
        }

        /**
         * Gets font underline style
         *
         * @return font underline style
         */
        public UnderlinePatterns getUnderline() {
            return underline == null ? getParentUnderline() : underline;
        }

        /**
         * Sets font underline style
         *
         * @param underline font underline style
         * @return same FontStyle
         */
        public FontStyle setUnderline(UnderlinePatterns underline) {
            this.underline = underline;
            return this;
        }

        /**
         * Gets is font italic
         *
         * @return is font italic
         */
        public Boolean isItalic() {
            return italic == null ? getParentIsItalic() : italic;
        }

        /**
         * Sets is font italic
         *
         * @param italic
         * @return same FontStyle
         */
        public FontStyle setItalic(Boolean italic) {
            this.italic = italic;
            return this;
        }

        /**
         * Gets is font striked
         *
         * @return
         */
        public Boolean isStrike() {
            return strike == null ? getParentIsStrike() : strike;
        }

        /**
         * Sets is font striked
         *
         * @param strike
         * @return same FontStyle
         */
        public FontStyle setStrike(Boolean strike) {
            this.strike = strike;
            return this;
        }

        /**
         * Gets font color
         *
         * @return
         */
        public Color getColor() {
            return color == null ? getParentColor() : color;
        }

        /**
         * Sets font color
         *
         * @param color
         * @return same FontStyle
         */
        public FontStyle setColor(Color color) {
            this.color = color;
            return this;
        }
    }

    /**
     * @see #getParent()
     */
    protected WeakReference<BaseElement> parent = null;
    /**
     * Nested elements
     */
    protected List<BaseElement> children = new ArrayList<BaseElement>();
    /**
     * store all font properties of element
     */
    protected FontStyle fontStyle;
    /**
     * @see #getObject()
     */
    protected Object object;
    /**
     * @see #getValue()
     */
    protected Object value;
    /**
     * ELProperty which used to get object from object of parent element if
     * object is not predefined. If object and objectELP are not defined, then
     * obect will be getted as object of parent element
     *
     * @see #getObject()
     */
    protected ELProperty objectELP;
    /**
     * ELProperty which used to get value from object if value is not
     * predefined.
     *
     * @see #getValue()
     */
    protected ELProperty valueELP;
    /**
     * Store was value evaluated when element was displayed. Used to reset value
     * property after displaying
     */
    protected boolean valueEvaluated;
    /**
     * Store was object evaluated when element was displayed. Used to reset
     * object property after displaying
     */
    protected boolean objectEvaluated;
    //protected String objectPropertyName;
    //protected String valueExpression;
    /**
     * @see #isEnabled()
     */
    protected boolean enabled = true;

    /**
     * Create and set valueExpression
     * @param valueExpression 
     * @see #setValueExpression(java.lang.String)
     */
    public BaseElement(String valueExpression) {
        this(null, valueExpression);
    }

    /**
     * Create and set objectPropertyName and valueExpression
     * @param objectPropertyName paren element object property name
     * @param valueExpression property expression  which used to get value from object if value is not
     * predefined.
     * @see #setValueExpression(java.lang.String)
     */
    public BaseElement(String objectPropertyName, String valueExpression) {
        setObjectPropertyName(objectPropertyName);
        setValueExpression(valueExpression);
    }

    /**
     * Sets name of property of parent element object for object evaluation
     *
     * @param objectPropertyName paren element object property name
     * @return same element
     */
    public BaseElement setObjectPropertyName(String objectPropertyName) {
        //this.objectPropertyName = objectPropertyName;
        objectELP = CommonUtils.isEmpty(objectPropertyName) ? null
                : ELProperty.create(CommonUtils.getPropertyExpressionByName(objectPropertyName));
        return this;
    }

    /**
     * Sets name of object property for value evaluation. It should by parameter
     * for ELProperty creation
     *
     * @param expression
     * @return same element
     * @see ELProperty#create(java.lang.String)
     */
    public BaseElement setValueExpression(String expression) {
        //this.valuExpression = valueExpression;
        valueELP = CommonUtils.isEmpty(expression) ? null
                : ELProperty.create(expression);
        return this;
    }

    /**
     * Sets name of object property for value evaluation.
     *
     * @param valuePropertyName
     * @return same element
     */
    public BaseElement setValuePropertyName(String valuePropertyName) {
        setValueExpression(CommonUtils.isEmpty(valuePropertyName) ? null
                : CommonUtils.getPropertyExpressionByName(valuePropertyName));
        return this;
    }

    /**
     * Gets parent element
     *
     * @return parent element or null if it is root element
     */
    public BaseElement getParent() {
        return parent == null ? null : parent.get();
    }

    /**
     * Sets parent elemt
     *
     * @param parent
     * @return same element
     */
    public BaseElement setParent(BaseElement parent) {
        this.parent = parent == null ? null : new WeakReference<BaseElement>(parent);
        return this;
    }

    /**
     * Gets object, which properties are displayed in this element
     *
     * @return object
     */
    public Object getObject() {
        return object;
    }

    /**
     * Sets bject, which properties are displayed in this element
     *
     * @param object
     * @return same element
     */
    public BaseElement setObject(Object object) {
        this.object = object;
        return this;
    }

    /**
     * Remove all cildren
     *
     * @return same element
     */
    public BaseElement clear() {
        children.clear();
        return this;
    }

    /**
     * Gets value which is displayed in this element it cab be calculated as
     * object property or setted external
     *
     * @return value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value which is displayed in this element it cab be calculated as
     * object property or setted external
     *
     * @param value
     * @return
     */
    public BaseElement setValue(Object value) {
        this.value = value;
        return this;
    }

    /**
     * Add child to children list
     *
     * @param child
     * @return same element
     */
    protected BaseElement add(BaseElement child) {
        if (child != null) {
            child.setParent(this);
            children.add(child);
        }
        return this;
    }

    /**
     * Gets is element enabled. If element is not enabled, it will not be
     * displayed in word document by default
     *
     * @return
     */
    private boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets is element enabled. If element is not enabled, it will not be
     * displayed in word document by default
     *
     * @param enabled
     * @return
     */
    public BaseElement setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Gets should element be displayed in word document
     *
     * @return true if should be
     */
    protected boolean canWrite() {
        return isEnabled();
    }

    /**
     * Write element to word document. Here object and value properties are
     * evalueted if need. If element can be written, it writes and all its
     * children writes also
     */
    protected void writeToWordDoc() {
        objectEvaluated = (object == null) && (getParent() != null);
        if (objectEvaluated) {
            Object parentObject = getParent().getObject();
            if ((objectELP != null) && (parentObject != null)) {
                try {
                    object = objectELP.getValue(parentObject);
                } catch (Exception e) {
                    object = e.toString();
                }

            } else {
                object = parentObject;
            }
        }
        valueEvaluated = (value == null) && (object != null) && (valueELP != null);
        if (valueEvaluated) {
            try {
                value = valueELP.getValue(object);
            } catch (Exception e) {
                value = e.toString();
            }
        }
        try {
            if (canWrite()) {
                internalWriteToWordDoc();
                writeChildren();
            }
        } finally {
            afterWrite();
        }

    }

    /**
     * Write all childrn to word document
     */
    protected void writeChildren() {
        for (int i = 0; i < children.size(); i++) {
            writeChildElement(children.get(i));
        }
    }

    /**
     * Write element to word document. Should be overridden in inheritor
     */
    protected void internalWriteToWordDoc() {
    }

    /**
     * Executes after element was written. Reference to POI objects can be
     * released here
     */
    protected void afterWrite() {
        if (objectEvaluated) {
            object = null;
        }
        if (valueEvaluated) {
            value = null;
        }
    }

    /**
     * Write single child
     *
     * @param child child to be written
     */
    protected void writeChildElement(BaseElement child) {
    }

    /**
     * Gets XWPFDocument where element is displayed
     *
     * @return XWPFDocument
     */
    protected XWPFDocument getDocument() {
        return getParent() == null ? null : getParent().getDocument();
    }

    /**
     * Create FontStyle instance
     *
     * @return
     */
    public FontStyle createFontStyle() {
        return new FontStyle();
    }

    /**
     * Create FontStyle instance and copy properties
     *
     * @param fontStyle source for properies copying
     * @return created FontStyle instance
     */
    public FontStyle createFontStyle(FontStyle fontStyle) {
        return new FontStyle(fontStyle);
    }

    /**
     * Fill fontStyle field if it's empty
     *
     * @return
     */
    protected FontStyle forceGetFontStyle() {
        if (fontStyle == null) {
            fontStyle = createFontStyle();
        }
        return fontStyle;
    }

    /**
     * Sets fontStyle property
     *
     * @param fontStyle
     * @return same lement
     */
    public BaseElement setFontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle == null ? null
                : new FontStyle(fontStyle);
        return this;
    }

    /**
     * Gets font name e.g. Arial
     *
     * @return font name
     */
    public String getFontFamily() {
        return fontStyle == null ? getParentFontFamily() : fontStyle.getFontFamily();
    }

    /**
     * Sets font name e.g. Arial
     *
     * @param fontFamily
     * @return same element
     */
    public BaseElement setFontFamily(String fontFamily) {
        forceGetFontStyle().setFontFamily(fontFamily);
        return this;
    }

    /**
     * Gets font size
     *
     * @return font size
     */
    public Integer getFontSize() {
        return fontStyle == null ? getParentFontSize() : fontStyle.getFontSize();
    }

    /**
     * Sets font size
     *
     * @param fontSize
     * @return same element
     */
    public BaseElement setFontSize(Integer fontSize) {
        forceGetFontStyle().setFontSize(fontSize);
        return this;
    }

    /**
     * Gets is font bold
     *
     * @return is font bold
     */
    public Boolean isBold() {
        return fontStyle == null ? getParentIsBold() : fontStyle.isBold();
    }

    /**
     * Sets is font bold
     *
     * @param bold
     * @return same element
     */
    public BaseElement setBold(Boolean bold) {
        forceGetFontStyle().setBold(bold);
        return this;
    }

    /**
     * Gets font underline style
     *
     * @return font underline style
     */
    public UnderlinePatterns getUnderline() {
        return fontStyle == null ? getParentUnderline() : fontStyle.getUnderline();
    }

    /**
     * Sets font underline style
     *
     * @param underline font underline style
     * @return same element
     */
    public BaseElement setUnderline(UnderlinePatterns underline) {
        forceGetFontStyle().setUnderline(underline);
        return this;
    }

    /**
     * Gets is font italic
     *
     * @return is font italic
     */
    public Boolean isItalic() {
        return fontStyle == null ? getParentIsItalic() : fontStyle.isItalic();
    }

    /**
     * Sets is font italic
     *
     * @param italic is font italic
     * @return same element
     */
    public BaseElement setItalic(Boolean italic) {
        forceGetFontStyle().setItalic(italic);
        return this;
    }

    /**
     * Gets is font striked
     *
     * @return is font striked
     */
    public Boolean isStrike() {
        return fontStyle == null ? getParentIsStrike() : fontStyle.isStrike();
    }

    /**
     * Sets is font striked
     *
     * @param strike
     * @return same element
     */
    public BaseElement setStrike(Boolean strike) {
        forceGetFontStyle().setStrike(strike);
        return this;
    }

    /**
     * Gets font color
     *
     * @return font color
     */
    public Color getColor() {
        return fontStyle == null ? getParentColor() : fontStyle.getColor();
    }

    /**
     * Sets font color
     *
     * @param color
     * @return same element
     */
    public BaseElement setColor(Color color) {
        forceGetFontStyle().setColor(color);
        return this;
    }

    /**
     * Gets parent element font name e.g. Arial
     *
     * @return font name
     */
    protected String getParentFontFamily() {
        return getParent() == null ? null : getParent().getFontFamily();
    }

    /**
     * Gets parent element font size
     *
     * @return font size
     */
    protected Integer getParentFontSize() {
        return getParent() == null ? null : getParent().getFontSize();
    }

    /**
     * Gets isparent element font bold
     *
     * @return
     */
    protected Boolean getParentIsBold() {
        return getParent() == null ? null : getParent().isBold();
    }

    /**
     * Gets parent element font underline style
     *
     * @return font underline style
     */
    protected UnderlinePatterns getParentUnderline() {
        return getParent() == null ? null : getParent().getUnderline();
    }

    /**
     * Gets is parent element font italic
     *
     * @return is font italic
     */
    protected Boolean getParentIsItalic() {
        return getParent() == null ? null : getParent().isItalic();
    }

    /**
     * Gets isparent element font striked
     *
     * @return
     */
    protected Boolean getParentIsStrike() {
        return getParent() == null ? null : getParent().isStrike();
    }

    /**
     * Gets parent element font color
     *
     * @return
     */
    protected Color getParentColor() {
        return getParent() == null ? null : getParent().getColor();
    }
}
