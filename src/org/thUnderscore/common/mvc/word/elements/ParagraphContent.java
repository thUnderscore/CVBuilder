package org.thUnderscore.common.mvc.word.elements;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Base class for paragraph content
 *
 * @author thUnderscore
 */
public abstract class ParagraphContent extends BaseElement {

    /**
     * POI paragraph where element will be written
     */
    XWPFParagraph para;

    public ParagraphContent(String objectPropertyName, String valuePropertyName) {
        super(objectPropertyName, CommonUtils.getPropertyExpressionByName(valuePropertyName));
    }

    /**
     * Write element to wrod document
     *
     * @param para POI paragraph where element will be written
     */
    protected void writeToWordDoc(XWPFParagraph para) {
        this.para = para;
        if (para != null) {
            writeToWordDoc();
        }
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
