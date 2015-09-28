package org.thUnderscore.common.mvc.word.elements;

import java.awt.Color;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.thUnderscore.common.mvc.word.POIUtils;

/**
 * Class describes content of MS word tables cell
 *
 * @author thUnderscore
 */
public class Cell extends ParagraphContainer {

    /**
     * Cell types enum
     * <code>HEADER</code> - placed in header rows
     * <code>BODY</code> - placed in main part of table
     * <code>FOOTER</code> - placed in footer rows
     */
    public enum CellType {

        BODY, HEADER, FOOTER
    }

    /**
     * XWPFTableCell where this element should be displayed
     */
    protected XWPFTableCell cell;
    /**
     * Cell vertical align
     */
    private STVerticalJc.Enum vAlign = null;
    /**
     * Table row
     */
    int row;
    /**
     * table column
     */
    int col;
    /**
     * Cell type
     * @see CellType
     */
    CellType type;

    public Cell setType(CellType type) {
        this.type = type;
        return this;
    }

    /**
     * Set cell position in table part (header, body ir footer)
     * @param row row number - row index strarted from 0 for each CellType
     * @param col column number
     * @return same cell
     */
    public Cell setPosition(int row, int col) {
        this.col = col;
        this.row = row;
        return this;
    }

    /**
     * Writes element to word document table cell
     * @param cell 
     */
    public void writeToWordDoc(XWPFTableCell cell) {
        this.cell = cell;
        writeToWordDoc();
    }

    /**
     * Get parent table element
     * @return BaseTable object
     */
    public BaseTable getTable() {
        BaseElement prnt = getParent();
        return (prnt instanceof ObjectListColumn) ? ((ObjectListColumn) prnt).getTable()
                : (BaseTable) getParent();
    }

    /**
     * Set cell vertical align
     * @param vAlign
     * @return same cell
     */
    public Cell setVAlign(STVerticalJc.Enum vAlign) {
        this.vAlign = vAlign;
        return this;
    }

    @Override
    protected XWPFDocument getDocument() {
        return cell == null ? null : cell.getXWPFDocument();
    }

    @Override
    protected XWPFParagraph createParagraph() {
        return cell.addParagraph();
    }

    @Override
    public Cell addParagraph(Paragraph paragraph) {
        super.addParagraph(paragraph);
        return this;
    }

    @Override
    public Cell addRun(Run run) {
        return (Cell) super.addRun(run);
    }

    @Override
    public Cell addImage(Image image) {
        return (Cell) super.addImage(image);
    }

    @Override
    public Cell addGroup(ParagraphContentGroup group) {
        return (Cell) super.addGroup(group);
    }

    @Override
    public Cell addRepeater(ParagraphContainerRepeater repeater) {
        return (Cell) super.addRepeater(repeater);
    }

    @Override
    public Cell setFontStyle(FontStyle fontStyle) {
        return (Cell) super.setFontStyle(fontStyle);
    }

    @Override
    public Cell setBold(Boolean bold) {
        return (Cell) super.setBold(bold);
    }

    @Override
    public Cell setStrike(Boolean strike) {
        return (Cell) super.setStrike(strike);
    }

    @Override
    public Cell setItalic(Boolean italic) {
        return (Cell) super.setItalic(italic);
    }

    @Override
    public Cell setFontFamily(String fontFamily) {
        return (Cell) super.setFontFamily(fontFamily);
    }

    @Override
    public Cell setFontSize(Integer fontSize) {
        return (Cell) super.setFontSize(fontSize);
    }

    @Override
    public Cell setUnderline(UnderlinePatterns underline) {
        return (Cell) super.setUnderline(underline);
    }

    @Override
    public Cell setColor(Color color) {
        return (Cell) super.setColor(color);
    }

    @Override
    public Cell setValue(Object value) {
        return (Cell) super.setValue(value);
    }

    @Override
    public Cell setValueExpression(String valueExpression) {
        return (Cell) super.setValueExpression(valueExpression);
    }

    @Override
    public Cell setValuePropertyName(String valuePropertyName) {
        return (Cell) super.setValuePropertyName(valuePropertyName);
    }

    @Override
    public Cell setObject(Object object) {
        return (Cell) super.setObject(object);
    }

    @Override
    public Cell setObjectPropertyName(String objectPropertyName) {
        return (Cell) super.setObjectPropertyName(objectPropertyName);
    }

    @Override
    protected String getParentFontFamily() {
        switch (type) {
            case HEADER:
                return getTable().getHeaderFontFamily();
            case BODY:
                return getTable().getFontFamily();
            case FOOTER:
                return getTable().getFooterFontFamily();
            default:
                return null;
        }
    }

    @Override
    protected Integer getParentFontSize() {
        switch (type) {
            case HEADER:
                return getTable().getHeaderFontSize();
            case BODY:
                return getTable().getFontSize();
            case FOOTER:
                return getTable().getFooterFontSize();
            default:
                return null;
        }
    }

    @Override
    protected Boolean getParentIsBold() {
        switch (type) {
            case HEADER:
                return getTable().getHeaderIsBold();
            case BODY:
                return getTable().isBold();
            case FOOTER:
                return getTable().getFooterIsBold();
            default:
                return null;
        }
    }

    @Override
    protected UnderlinePatterns getParentUnderline() {
        switch (type) {
            case HEADER:
                return getTable().getHeaderUnderline();
            case BODY:
                return getTable().getUnderline();
            case FOOTER:
                return getTable().getFooterUnderline();
            default:
                return null;
        }
    }

    @Override
    protected Boolean getParentIsItalic() {
        switch (type) {
            case HEADER:
                return getTable().getHeaderIsItalic();
            case BODY:
                return getTable().isItalic();
            case FOOTER:
                return getTable().getFooterIsItalic();
            default:
                return null;
        }
    }

    @Override
    protected Boolean getParentIsStrike() {
        switch (type) {
            case HEADER:
                return getTable().getHeaderIsStrike();
            case BODY:
                return getTable().isStrike();
            case FOOTER:
                return getTable().getFooterIsStrike();
            default:
                return null;
        }
    }

    @Override
    protected void internalWriteToWordDoc() {
        super.internalWriteToWordDoc();
        if (vAlign != null) {
            POIUtils.setTableCellVAlign(cell, vAlign);
        }
    }

}
