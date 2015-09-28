package org.thUnderscore.common.mvc.word.elements;

import java.util.List;

/**
 * Element which can display list of object to table. Each object from list will
 * be displayed to own row
 *
 * @author thUnderscore
 */
public class ObjectListTable extends ListTable {

    /**
     *
     * @param listPropertyName property of table object, which contains list
     * @param cols columns count
     * @param hasHeader has table header or not. If yes - header rows count = 1
     * @param hasFooter has table footer or not. If yes - footer rows count = 1
     */
    public ObjectListTable(String listPropertyName, int cols,
            boolean hasHeader, boolean hasFooter) {
        this(null, listPropertyName, cols, hasHeader, hasFooter);
    }

    /**
     *
     * @param objectPropertyName paren element object property name
     * @param listPropertyName property of table object, which contains list
     * @param cols columns count
     * @param hasHeader has table header or not. If yes - header rows count = 1
     * @param hasFooter has table footer or not. If yes - footer rows count = 1
     */
    public ObjectListTable(String objectPropertyName, String listPropertyName, int cols,
            boolean hasHeader, boolean hasFooter) {
        super(objectPropertyName, listPropertyName, cols, hasHeader ? 1 : 0, hasFooter ? 1 : 0);
    }

    /**
     * Add column definition to child. Column position will be setted as current 
     * columns count
     * @param column ObjectListTable instance
     * @return same ObjectListTable
     */
    public ObjectListTable addColumn(ObjectListColumn column) {
        return addColumn(column, children.size());
    }

    /***
     * Add column definition to child
     * @param column ObjectListTable instance
     * @param position column position
     * @return same ObjectListTable
     */
    public ObjectListTable addColumn(ObjectListColumn column, int position) {
        column.setPosition(position);
        add(column);
        return this;
    }

    @Override
    protected void writeChildElement(BaseElement child) {
        if (child instanceof ObjectListColumn) {
            ((ObjectListColumn) child).writeToWordDoc();
        }
        super.writeChildElement(child);
    }

    @Override
    protected void internalWriteToWordDoc() {
        List list = getList();
        //caculate rows count before writing to wrod document
        rows = (list != null) ? list.size() : 0;
        super.internalWriteToWordDoc();
    }

}
