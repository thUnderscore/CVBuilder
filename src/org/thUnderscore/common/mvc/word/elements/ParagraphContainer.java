package org.thUnderscore.common.mvc.word.elements;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 * Elements which can contain paragraph inside it
 *
 * @author thUnderscore
 */
public abstract class ParagraphContainer extends BaseElement {

    /*
     Font names 
     */
    public static final String TIMES_NEW_ROMAN = "Times New Roman";
    public static final String VERNADA = "Vernada";
    public static final String ARIAL = "Arial";

    /**
     * Paragraph, which is created for children which are ParagraphContent
     * inheritors
     */
    protected XWPFParagraph implicitPara;
    /**
     * Gets can BaseTable inheritors be added to children
     */
    protected boolean tableSupported = false;

    public ParagraphContainer() {
        this(null, null);
    }

    /**
     * Create and set objectPropertyName and valueExpression
     *
     * @param objectPropertyName paren element object property name
     * @param valueExpression property which used to get value from object if
     * value is not predefined.
     * @see #setValueExpression(java.lang.String)
     */
    public ParagraphContainer(String objectPropertyName, String valueExpression) {
        super(objectPropertyName, valueExpression);
    }

    /**
     * Add Run element to structure
     *
     * @param run
     * @return same element
     */
    public ParagraphContainer addRun(Run run) {
        add(run);
        return this;
    }

    /**
     * Add Image element to structure
     *
     * @param image
     * @return same element
     */
    public ParagraphContainer addImage(Image image) {
        add(image);
        return this;
    }

    /**
     * Add ParagraphContentGroup element to structure
     *
     * @param group
     * @return same element
     */
    public ParagraphContainer addGroup(ParagraphContentGroup group) {
        add(group);
        return this;
    }

    /**
     * Add Paragraph element to structure
     *
     * @param paragraph
     * @return same element
     */
    public ParagraphContainer addParagraph(Paragraph paragraph) {
        add(paragraph);
        return this;
    }

    /**
     * Add ParagraphContainerRepeater element to structure
     *
     * @param repeater
     * @return same element
     */
    public ParagraphContainer addRepeater(ParagraphContainerRepeater repeater) {
        add(repeater);
        return this;
    }

    /**
     * Add BaseTable inheritor to structure
     *
     * @param table
     * @return same element
     */
    public ParagraphContainer addTable(BaseTable table) {
        if (tableSupported) {
            add(table);
        }
        //TODO log
        return this;
    }

    @Override
    public ParagraphContainer setFontStyle(FontStyle fontStyle) {
        return (ParagraphContainer) super.setFontStyle(fontStyle);
    }

    @Override
    protected void afterWrite() {
        super.afterWrite();
        implicitPara = null;
    }

    @Override
    protected void writeChildElement(BaseElement child) {
        if (child instanceof ParagraphContent) {
            //if child is ParagraphContent and implicitPara is empty
            //create implicit paragrah and write child in it
            if (implicitPara == null) {
                implicitPara = createParagraph();
            }
            ((ParagraphContent) child).writeToWordDoc(implicitPara);
        } else {
            //if child is not ParagraphContent then clear implicitPara field
            //next child which will be ParagraphContent should be written
            //to new implicit paragraph
            implicitPara = null;
            if (child instanceof Paragraph) {
                ((Paragraph) child).writeToWordDoc(createParagraph());
            } else if (child instanceof ParagraphContainerRepeater) {
                ((ParagraphContainerRepeater) child).writeToWordDoc();
            } else if (child instanceof BaseTable) {
                ((BaseTable) child).writeToWordDoc(getDocument());
            }
        }
        super.writeChildElement(child);
    }

    /**
     * Create POI object for paragraph
     * @return 
     */
    abstract protected XWPFParagraph createParagraph();

}
