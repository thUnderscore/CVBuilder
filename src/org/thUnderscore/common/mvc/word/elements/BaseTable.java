package org.thUnderscore.common.mvc.word.elements;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.mvc.word.POIUtils;

/**
 * Base element which can diplsy itself to MS Word document's table
 *
 * @author thUnderscore
 */
public abstract class BaseTable extends TitledElement {

    /**
     * Default rows height
     */
    public static final int DEF_ROW_HEIGHT = -1;
    /**
     * Default columns width
     */
    public static final int DEF_COL_WIDTH = -1;

    /**
     * Header cells font style
     */
    protected FontStyle headerFontStyle;
    /**
     * Footer cells font style
     */
    protected FontStyle footerFontStyle;
    /**
     * Body rows vount
     */
    int rows;
    /**
     * Columns count
     */
    int cols;
    /**
     * Header rows count
     */
    int headerRows;
    /**
     * Gets has tabel header rows
     */
    boolean hasHeader;
    /**
     * Footer rows count
     */
    int footerRows;
    /**
     * Gets has tabel footer rows
     */
    boolean hasFooter;
    /**
     * Are table borders visible
     */
    boolean borderVisible = true;
    /**
     * Temporary variable, which store XWPFDocument where element is displayed
     */
    XWPFDocument wordDoc;
    /**
     * Temporary variable, which store XWPFTable where element is displayed
     */
    XWPFTable table;
    /**
     * Rows height. if < 0, then undefined and default value will be used
     */
    protected int rowHeight = DEF_ROW_HEIGHT;
    /**
     * Columns width. if < 0, then undefined and default value will be used
     */
    protected int colWidth = DEF_COL_WIDTH;
    /**
     * Rows heights, can be defined for each row. Key - row number value - row
     * height
     */
    protected Map<Integer, Integer> heights;
    /**
     * Columns widths, can be defined for each column. Key - col number value -
     * edit width
     */
    protected Map<Integer, Integer> colWidths;
    /**
     * Should empty paragrsphbe be written to document after table
     */
    protected boolean addParagraphAfterTable = false;

    /**
     * @param objectPropertyName paren element object property name
     * @param valuePropertyName
     * @param rows body rows count
     * @param cols columns count
     * @param headerRows header rows count
     * @param footerRows footer rows count
     * @see #setValueExpression(java.lang.String)
     */
    public BaseTable(String objectPropertyName, String valuePropertyName,
            int rows, int cols, int headerRows, int footerRows) {
        super(objectPropertyName, CommonUtils.getPropertyExpressionByName(valuePropertyName));
        this.rows = rows;
        this.cols = cols;
        this.headerRows = headerRows;
        this.hasHeader = headerRows > 0;
        this.footerRows = footerRows;
        this.hasFooter = footerRows > 0;
        this.title = new ParagraphContentGroup();
        this.title.setParent(this);
    }

    @Override
    protected void afterWrite() {
        super.afterWrite();
        if (table != null) {
            //create paragraph in empty cell
            List<XWPFTableRow> tableRows = table.getRows();
            XWPFTableCell cell;
            for (int i = 0; i < tableRows.size(); i++) {
                List<XWPFTableCell> cells = tableRows.get(i).getTableCells();
                for (int j = 0; j < cells.size(); j++) {
                    cell = cells.get(j);
                    if (cells.get(j).getParagraphs().isEmpty()) {
                        cell.addParagraph();
                    }
                }

            }
            table = null;
        }
        wordDoc = null;
    }

    /**
     * Write table to XWPFDocument
     *
     * @param wordDoc XWPFDocument, where table shoul be written
     */
    protected void writeToWordDoc(XWPFDocument wordDoc) {
        this.wordDoc = wordDoc;
        writeToWordDoc();
    }

    @Override
    protected void internalWriteToWordDoc() {
        super.internalWriteToWordDoc();
        table = wordDoc.createTable(getTotalRowsCount(), cols);
        if (addParagraphAfterTable) {
            wordDoc.createParagraph();
        }
        List<XWPFTableRow> tableRows = table.getRows();
        //remove existing paragrah in cells
        for (int i = 0; i < tableRows.size(); i++) {
            List<XWPFTableCell> cells = tableRows.get(i).getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                cells.get(j).removeParagraph(0);
                int width = getColWidth(j);
                if (width >= 0) {
                    POIUtils.setTableCellWidth(cells.get(j), width);
                }
            }
            int height = getRowHeight(i);
            if (height >= 0) {
                POIUtils.setTableRowHeight(tableRows.get(i), height);
            }
        }
        if (!borderVisible) {
            POIUtils.setTableInvisible(table, 5, 5);
        }
    }

    /**
     * Gets has tabel footer rows
     *
     * @return
     */
    public boolean getHasFooter() {
        return hasFooter;
    }

    /**
     * Gets has tabel header rows
     *
     * @return
     */
    public boolean getHasHeader() {
        return hasHeader;
    }

    /**
     * Gets should empty paragrsph be written to document after table
     *
     * @return
     */
    public boolean isAddParagraphAfterTable() {
        return addParagraphAfterTable;
    }

    /**
     * Sets should empty paragrsph be written to document after table
     *
     * @param addParagraphAfterTable
     * @return same table
     */
    public BaseTable setAddParagraphAfterTable(boolean addParagraphAfterTable) {
        this.addParagraphAfterTable = addParagraphAfterTable;
        return this;
    }

    @Override
    public BaseTable setFontStyle(FontStyle fontStyle) {
        return (BaseTable) super.setFontStyle(fontStyle);
    }

    @Override
    public BaseTable setBold(Boolean bold) {
        return (BaseTable) super.setBold(bold);
    }

    @Override
    public BaseTable setStrike(Boolean strike) {
        return (BaseTable) super.setStrike(strike);
    }

    @Override
    public BaseTable setItalic(Boolean italic) {
        return (BaseTable) super.setItalic(italic);
    }

    @Override
    public BaseTable setFontFamily(String fontFamily) {
        return (BaseTable) super.setFontFamily(fontFamily);
    }

    @Override
    public BaseTable setFontSize(Integer fontSize) {
        return (BaseTable) super.setFontSize(fontSize);
    }

    @Override
    public BaseTable setUnderline(UnderlinePatterns underline) {
        return (BaseTable) super.setUnderline(underline);
    }

    @Override
    public BaseTable setColor(Color color) {
        return (BaseTable) super.setColor(color);
    }

    @Override
    protected XWPFParagraph getTitleParagraph() {
        BaseElement prnt = getParent();
        return prnt instanceof ParagraphContainer
                ? ((ParagraphContainer) getParent()).createParagraph()
                : null;
    }

    @Override
    public BaseTable setTitle(String title, boolean addBreak, FontStyle fontStyle) {
        return (BaseTable) super.setTitle(title, addBreak, fontStyle);
    }

    @Override
    public BaseTable setTitle(boolean enabled, String title, boolean addBreak, FontStyle fontStyle) {
        return (BaseTable) super.setTitle(enabled, title, addBreak, fontStyle);
    }

    @Override
    public BaseTable setTitle(String titleText, boolean addBreak) {
        return (BaseTable) super.setTitle(titleText, addBreak);
    }

    @Override
    public BaseTable setTitle(boolean enabled, String titleText, boolean addBreak) {
        return (BaseTable) super.setTitle(enabled, titleText, addBreak);
    }

    @Override
    public BaseTable setTitle(ParagraphContent titleContent, FontStyle fontStyle) {
        return (BaseTable) super.setTitle(titleContent, fontStyle);
    }

    @Override
    public BaseTable setTitle(boolean enabled, ParagraphContent titleContent, FontStyle fontStyle) {
        return (BaseTable) super.setTitle(enabled, titleContent, fontStyle);
    }

    @Override
    public BaseTable setTitle(String titleText) {
        return (BaseTable) super.setTitle(titleText);
    }

    @Override
    public BaseTable setTitle(String title, FontStyle fontStyle) {
        return (BaseTable) super.setTitle(title, fontStyle);
    }

    @Override
    public BaseTable setTitleFontStyle(FontStyle fontStyle) {
        return (BaseTable) super.setTitleFontStyle(fontStyle);
    }

    @Override
    protected XWPFDocument getDocument() {
        return wordDoc;
    }

    /**
     * Sets header font style
     *
     * @param fontStyle
     * @return same table
     */
    public BaseTable setHeaderFontStyle(FontStyle fontStyle) {
        this.headerFontStyle = fontStyle == null ? null : new FontStyle(fontStyle);
        return this;
    }

    /**
     * Sets footer font style
     *
     * @param fontStyle
     * @return same table
     */
    public BaseTable setFooterFontStyle(FontStyle fontStyle) {
        this.footerFontStyle = fontStyle == null ? null : new FontStyle(fontStyle);
        return this;
    }

    /**
     * Gets total rows count: body, header, footer
     *
     * @return total rows count
     */
    protected int getTotalRowsCount() {
        return rows + getHeaderRowsCount() + getFooterRowsCount();
    }

    /**
     * Gets header rows count
     *
     * @return header rows count
     */
    protected int getHeaderRowsCount() {
        return hasHeader ? headerRows : 0;
    }

    /**
     * Gets footer rows count
     *
     * @return footer rows count
     */
    protected int getFooterRowsCount() {
        return hasFooter ? footerRows : 0;
    }

    /**
     * Gets are table borders visible
     *
     * @return are table borders visible
     */
    public boolean isBorderVisible() {
        return borderVisible;
    }

    public BaseTable setBorderVisible(boolean borderVisible) {
        this.borderVisible = borderVisible;
        return this;
    }

    /**
     * Gets header font style
     *
     * @return header font style object
     */
    protected FontStyle getHeaderFontStyle() {
        if (headerFontStyle == null) {
            headerFontStyle = new FontStyle();
        }
        return headerFontStyle;
    }

    /**
     * Gets header font name e.g. Arial
     *
     * @return header font name
     */
    public String getHeaderFontFamily() {
        return headerFontStyle == null ? getFontFamily() : headerFontStyle.getFontFamily();
    }

    /**
     * Sets header font name e.g. Arial
     *
     * @param fontFamily
     * @return same element
     */
    public BaseTable setHeaderFontFamily(String fontFamily) {
        getHeaderFontStyle().setFontFamily(fontFamily);
        return this;
    }

    /**
     * Gets header font size
     *
     * @return header font size
     */
    public Integer getHeaderFontSize() {
        return headerFontStyle == null ? getFontSize() : headerFontStyle.getFontSize();
    }

    /**
     * Sets header font size
     *
     * @param fontSize
     * @return same element
     */
    public BaseTable setHeaderFontSize(Integer fontSize) {
        getHeaderFontStyle().setFontSize(fontSize);
        return this;
    }

    /**
     * Gets is header font bold
     *
     * @return is header font bold
     */
    public Boolean getHeaderIsBold() {
        return headerFontStyle == null ? isBold() : headerFontStyle.isBold();
    }

    /**
     * Sets is header font bold
     *
     * @param bold
     * @return same element
     */
    public BaseTable setHeaderBold(Boolean bold) {
        getHeaderFontStyle().setBold(bold);
        return this;
    }

    /**
     * Gets header font underline style
     *
     * @return header font underline style
     */
    public UnderlinePatterns getHeaderUnderline() {
        return headerFontStyle == null ? getUnderline() : headerFontStyle.getUnderline();
    }

    /**
     * Sets header font underline style
     *
     * @param underline header font underline style
     * @return same element
     */
    public BaseTable setHeaderUnderline(UnderlinePatterns underline) {
        getHeaderFontStyle().setUnderline(underline);
        return this;
    }

    /**
     * Gets is header font italic
     *
     * @return is header font italic
     */
    public Boolean getHeaderIsItalic() {
        return headerFontStyle == null ? isItalic() : headerFontStyle.isItalic();
    }

    /**
     * Sets is header font italic
     *
     * @param italic
     * @return same element
     */
    public BaseTable setHeaderItalic(Boolean italic) {
        getHeaderFontStyle().setItalic(italic);
        return this;
    }

    /**
     * Gets is header font striked
     *
     * @return
     */
    public Boolean getHeaderIsStrike() {
        return headerFontStyle == null ? isStrike() : headerFontStyle.isStrike();
    }

    /**
     * Sets is header font striked
     *
     * @param strike
     * @return same element
     */
    public BaseTable setHeaderIsStrike(Boolean strike) {
        getHeaderFontStyle().setStrike(strike);
        return this;
    }

    /**
     * Gets header font color
     *
     * @return
     */
    public Color getHeaderColor() {
        return footerFontStyle == null ? getColor() : headerFontStyle.getColor();
    }

    /**
     * Sets header font color
     *
     * @param color
     * @return same element
     */
    public BaseTable setHeaderColor(Color color) {
        getHeaderFontStyle().setColor(color);
        return this;
    }

    /**
     * Gets footer font style
     *
     * @return footer font style
     */
    protected FontStyle getFooterFontStyle() {
        if (footerFontStyle == null) {
            footerFontStyle = new FontStyle();
        }
        return footerFontStyle;
    }

    /**
     * Gets footer font name e.g. Arial
     *
     * @return footer font name
     */
    public String getFooterFontFamily() {
        return footerFontStyle == null ? getFontFamily() : footerFontStyle.getFontFamily();
    }

    /**
     * Sets footer font name e.g. Arial
     *
     * @param fontFamily
     * @return same element
     */
    public BaseTable setFooterFontFamily(String fontFamily) {
        getFooterFontStyle().setFontFamily(fontFamily);
        return this;
    }

    /**
     * Gets footer font size
     *
     * @return footer font size
     */
    public Integer getFooterFontSize() {
        return footerFontStyle == null ? getFontSize() : footerFontStyle.getFontSize();
    }

    /**
     * Sets footer font size
     *
     * @param fontSize
     * @return same element
     */
    public BaseTable setFooterFontSize(Integer fontSize) {
        getFooterFontStyle().setFontSize(fontSize);
        return this;
    }

    /**
     * Gets is footer font bold
     *
     * @return is footer font bold
     */
    public Boolean getFooterIsBold() {
        return footerFontStyle == null ? isBold() : footerFontStyle.isBold();
    }

    /**
     * Sets is footer font bold
     *
     * @param bold
     * @return same element
     */
    public BaseTable setFooterBold(Boolean bold) {
        getFooterFontStyle().setBold(bold);
        return this;
    }

    /**
     * Gets footer font underline style
     *
     * @return footer font underline style
     */
    public UnderlinePatterns getFooterUnderline() {
        return footerFontStyle == null ? getUnderline() : footerFontStyle.getUnderline();
    }

    /**
     * Sets footer font underline style
     *
     * @param underline footer font underline style
     * @return same element
     */
    public BaseTable setFooterUnderline(UnderlinePatterns underline) {
        getFooterFontStyle().setUnderline(underline);
        return this;
    }

    /**
     * Gets is footer font italic
     *
     * @return is footer font italic
     */
    public Boolean getFooterIsItalic() {
        return footerFontStyle == null ? isItalic() : footerFontStyle.isItalic();
    }

    /**
     * Sets is footer font italic
     *
     * @param italic is footer font italic
     * @return same element
     */
    public BaseTable setFooterItalic(Boolean italic) {
        getFooterFontStyle().setItalic(italic);
        return this;
    }

    /**
     * Gets is footer font striked
     *
     * @return is footer font striked
     */
    public Boolean getFooterIsStrike() {
        return footerFontStyle == null ? isStrike() : footerFontStyle.isStrike();
    }

    /**
     * Sets is footer font striked
     *
     * @param strike
     * @return same element
     */
    public BaseTable setFooterIsStrike(Boolean strike) {
        getFooterFontStyle().setStrike(strike);
        return this;
    }

    /**
     * Gets footer font color
     *
     * @return footer font color
     */
    public Color getFooterColor() {
        return footerFontStyle == null ? getColor() : footerFontStyle.getColor();
    }

    /**
     * Sets footer font color
     *
     * @param color
     * @return same element
     */
    public BaseTable setFooterColor(Color color) {
        getFooterFontStyle().setColor(color);
        return this;
    }

    /**
     * Get row height
     *
     * @param row number
     * @return height
     */
    public int getRowHeight(Integer row) {
        return rowHeight >= 0 ? rowHeight : (heights != null) && heights.containsKey(row) ? heights.get(row) : DEF_ROW_HEIGHT;
    }

    /**
     * Set row height
     *
     * @param row row number
     * @param size
     * @return same table
     */
    public BaseTable setRowHeight(Integer row, int size) {
        if (heights == null) {
            heights = new HashMap<Integer, Integer>();
        }
        heights.put(row, size);
        return this;
    }

    /**
     * Set rows height
     *
     * @param sizes array of height. index in array - number of row
     * @return same table
     */
    public BaseTable setRowHeight(int[] sizes) {
        if (heights == null) {
            heights = new HashMap<Integer, Integer>();
        }
        for (int i = 0; i < sizes.length; i++) {
            heights.put(i, sizes[i]);
        }
        return this;
    }

    /**
     * Sets default height of rows
     *
     * @param size
     * @return same table
     */
    public BaseTable setRowHeight(int size) {
        rowHeight = size;
        return this;
    }

    /**
     * Gets default rows heigtht
     *
     * @return default rows height
     */
    public int getRowHeight() {
        return rowHeight;
    }

    /**
     * Get column width
     *
     * @param col column number
     * @return width
     */
    public int getColWidth(Integer col) {
        return colWidth >= 0 ? colWidth
                : (colWidths != null) && colWidths.containsKey(col)
                ? colWidths.get(col) : DEF_COL_WIDTH;
    }

    /**
     * Set column t width
     *
     * @param col column number
     * @param size
     * @return same table
     */
    public BaseTable setColWidth(Integer col, int size) {
        if (colWidths == null) {
            colWidths = new HashMap<Integer, Integer>();
        }
        colWidths.put(col, size);
        return this;
    }

    /**
     * Set columns width
     *
     * @param sizes array of width. index in array - number of column
     * @return same table
     */
    public BaseTable setColWidth(int[] sizes) {
        if (colWidths == null) {
            colWidths = new HashMap<Integer, Integer>();
        }
        for (int i = 0; i < sizes.length; i++) {
            colWidths.put(i, sizes[i]);
        }
        return this;
    }

    /**
     * Sets default width of column
     *
     * @param size
     * @return same table
     */
    public BaseTable setColWidth(int size) {
        colWidth = size;
        return this;
    }

    /**
     * Gets default width of columns
     *
     * @return default columns width
     */
    public int getColWidth() {
        return colWidth;
    }

    /**
     * Gets XWPFTableCell object, where Cell object should be diplayed
     *
     * @param cell
     * @return
     */
    protected XWPFTableCell getWordCell(Cell cell) {
        int row = -1;
        switch (cell.type) {
            case BODY:
                if (cell.row < rows) {
                    row = cell.row + getHeaderRowsCount();
                }
                break;
            case HEADER:
                if (hasHeader && (cell.row < headerRows)) {
                    row = cell.row;
                }
                break;
            case FOOTER:
                if (hasFooter && (cell.row < footerRows)) {
                    row = getTotalRowsCount() - footerRows + cell.row;
                }
                break;
        }
        int col = cell.col;
        return row > -1 ? table.getRow(row).getCell(col) : null;
    }

    /**
     * Write child Cell object to document
     *
     * @param cell
     */
    protected void writeCell(Cell cell) {
        if (cell != null) {
            XWPFTableCell wordCell = getWordCell(cell);
            if (wordCell != null) {
                cell.writeToWordDoc(wordCell);
            }
        }
    }

}
