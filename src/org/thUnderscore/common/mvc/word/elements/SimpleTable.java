package org.thUnderscore.common.mvc.word.elements;

/**
 * Element which can diplsy itself to MS Word document's table. Each cell should
 * be defined addCell addHeaderCell or addFooterCell, otherwise it will be empty
 *
 * @author thUnderscore
 */
public class SimpleTable extends BaseTable {

    /**
     *
     * @param rows body rows count
     * @param cols columns count
     */
    public SimpleTable(int rows, int cols) {
        this(rows, cols, 0, 0);
    }

    /**
     *
     * @param rows body rows count
     * @param cols columns count
     * @param headerRows header rows count
     * @param footerRows footer rows count
     */
    public SimpleTable(int rows, int cols, int headerRows, int footerRows) {
        this(null, null, rows, cols, headerRows, footerRows);

    }

    /**
     *
     * @param objectPropertyName paren element object property name
     * @param valuePropertyName
     * @param rows body rows count
     * @param cols columns count
     * @param headerRows header rows count
     * @param footerRows footer rows count
     */
    public SimpleTable(String objectPropertyName, String valuePropertyName,
            int rows, int cols, int headerRows, int footerRows) {
        super(objectPropertyName, valuePropertyName, rows, cols, headerRows, footerRows);
    }

    /**
     * Add cell to children
     * @param row cell row
     * @param col cell column
     * @param cell cell instance
     * @param type cell type
     * @return same table
     * @see Cell.CellType
     */
    protected SimpleTable addCell(int row, int col, Cell cell, Cell.CellType type) {
        cell.setType(type);
        cell.setPosition(row, col);
        add(cell);
        return this;
    }

    /**
     * Add body cell to children
     * @param row cell row
     * @param col cell column
     * @param cell cell instance
     * @return same table
     */
    public SimpleTable addCell(int row, int col, Cell cell) {
        return addCell(row, col, cell, Cell.CellType.BODY);
    }

    /**
     * Add header cell to children
     * @param row cell row
     * @param col cell column
     * @param cell cell instance
     * @return same table
     */
    public SimpleTable addHeaderCell(int row, int col, Cell cell) {
        return addCell(row, col, cell, Cell.CellType.HEADER);
    }

    /**
     * Add footer cell to children
     * @param row cell row
     * @param col cell column
     * @param cell cell instance
     * @return same table
     */
    public SimpleTable addFooterCell(int row, int col, Cell cell) {
        return addCell(row, col, cell, Cell.CellType.FOOTER);
    }

    @Override
    protected void writeChildElement(BaseElement child) {
        if (child instanceof Cell) {
            writeCell((Cell) child);
        }
        super.writeChildElement(child);
    }
}
