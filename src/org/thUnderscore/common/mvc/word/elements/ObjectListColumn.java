package org.thUnderscore.common.mvc.word.elements;

import java.util.List;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Definition of single column in ObjectListTable
 *
 * @author thUnderscore
 */
public class ObjectListColumn extends BaseElement {

    /**
     * Cell which displays in header. By default it displays some property of
     * table object or constant
     */
    Cell header;
    /**
     * Cell which displays in body. By default it displays some property of
     * object from displayed list
     */
    Cell body;
    /**
     * Cell which displays in footer. By default it displays some property of
     * table object or constant
     */
    Cell footer;
    /**
     * Column position in table
     */
    int position;

    public ObjectListColumn() {
        this(null);
    }

    /**
     * Create and set propertyName for body cell. This property value of object
     * from list will be displayed in body cell
     *
     * @param bodyPropertyName property of object from list which will be
     * displayed in body
     */
    public ObjectListColumn(String bodyPropertyName) {
        this(null, bodyPropertyName, null);
    }

    /**
     *
     * @param headerValue constant string, which will be displayed in header
     * @param bodyPropertyName property of object from list which will be
     * displayed in body
     * @param footerPropertyName property table object which will be displayed
     * in footer
     */
    public ObjectListColumn(String headerValue, String bodyPropertyName, String footerPropertyName) {
        super(null, null);
        header = createCell(Cell.CellType.HEADER, null, null, null, headerValue, null);
        body = createCell(Cell.CellType.BODY, null, bodyPropertyName, null, null, null);
        footer = createCell(Cell.CellType.FOOTER, null, footerPropertyName, null, null, null);
    }

    /**
     * Set content and display style definition for header cell's run
     *
     * @param objectPropertyName property of parent element for object
     * evaluation
     * @param valueExpression expression for value evaluation
     * @param object object property value (overlay objectPropertyName if is
     * defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined
     * - value will not be evaluated)
     * @param fontStyle content font style
     * @return same ObjectListColumn
     */
    public ObjectListColumn setHeader(String objectPropertyName,
            String valueExpression, Object object, Object value, FontStyle fontStyle) {
        return setCell(header, objectPropertyName, valueExpression, object,
                value, fontStyle);
    }

    /**
     * Set content and display style definition for footer cell's run
     *
     * @param objectPropertyName property of parent element for object
     * evaluation
     * @param valueExpression expression for value evaluation
     * @param object object property value (overlay objectPropertyName if is
     * defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined
     * - value will not be evaluated)
     * @param fontStyle content font style
     * @return same ObjectListColumn
     */
    public ObjectListColumn setFooter(String objectPropertyName,
            String valueExpression, Object object, Object value, FontStyle fontStyle) {
        return setCell(footer, objectPropertyName, valueExpression, object,
                value, fontStyle);
    }

    /**
     * Set content and display style definition for body cell's run
     *
     * @param objectPropertyName property of parent element for object
     * evaluation
     * @param valueExpression expression for value evaluation
     * @param object object property value (overlay objectPropertyName if is
     * defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined
     * - value will not be evaluated)
     * @param fontStyle content font style
     * @return same ObjectListColumn
     */
    public ObjectListColumn setBody(String objectPropertyName,
            String valueExpression, Object object, Object value, FontStyle fontStyle) {
        return setCell(body, objectPropertyName, valueExpression, object,
                value, fontStyle);
    }

    /**
     * Create cell for internal usage
     *
     * @param type type of cell
     * @param objectPropertyName property of parent element for object
     * evaluation
     * @param valuePropertyName expression for value evaluation
     * @param object cell object property value (overlay objectPropertyName if
     * is defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined
     * - value will not be evaluated)
     * @param fontStyle content font style
     * @return created cell
     */
    protected final Cell createCell(Cell.CellType type,
            String objectPropertyName, String valuePropertyName,
            Object object, Object value, FontStyle fontStyle) {
        Cell cell = (Cell) new Cell().setType(type).setParent(this);
        setCell(cell, objectPropertyName, CommonUtils.getPropertyExpressionByName(valuePropertyName),
                object, value, fontStyle);
        return cell;
    }

    /**
     * Sets column position
     *
     * @param position coulumn index
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets parent element casted as ObjectListTable
     *
     * @return
     */
    protected ObjectListTable getTable() {
        return (ObjectListTable) getParent();
    }

    /**
     * Get Run object, which displays cell content
     *
     * @param cell
     * @return Run object
     */
    protected Run getRun(Cell cell) {
        Run run;
        List list = cell.children;
        if (list.isEmpty()) {
            run = new Run(false);
            cell.addRun(run);
        } else {
            run = (Run) list.get(0);
        }
        return run;
    }

    /**
     * Set content and display style definition for cell's run
     *
     * @param cell cell,which content and display style definition is changing
     * now
     * @param objectPropertyName property of parent element for object
     * evaluation
     * @param valueExpression expression for value evaluation
     * @param object object property value (overlay objectPropertyName if is
     * defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined
     * - value will not be evaluated)
     * @param fontStyle content font style
     * @return same ObjectListColumn
     */
    protected ObjectListColumn setCell(Cell cell, String objectPropertyName, String valueExpression,
            Object object, Object value, FontStyle fontStyle) {
        getRun(cell)
                .setFontStyle(fontStyle)
                .setObject(object)
                .setObjectPropertyName(objectPropertyName)
                .setValueExpression(valueExpression)
                .setValue(value);
        return this;

    }

    /**
     * Set valueExpression for cell's run
     *
     * @param cell
     * @param valueExpression
     * @return same ObjectListColumn
     * @see BaseElement#setValueExpression(java.lang.String)
     */
    protected ObjectListColumn setCellValueExpression(Cell cell, String valueExpression) {
        getRun(cell).setValueExpression(valueExpression);
        return this;
    }

    /**
     * Set objectPropertyName for cell's run
     *
     * @param cell
     * @param objectPropertyName
     * @return same ObjectListColumn
     * @see BaseElement#setObjectPropertyName(java.lang.String)
     */
    protected ObjectListColumn setCellObjectPropertyName(Cell cell, String objectPropertyName) {
        getRun(cell).setObjectPropertyName(objectPropertyName);
        return this;
    }

    /**
     * Set object for cell's run
     *
     * @param cell
     * @param object
     * @return same ObjectListColumn
     * @see BaseElement#setObject(java.lang.Object)
     */
    protected ObjectListColumn setCellObject(Cell cell, Object object) {
        getRun(cell).setObject(object);
        return this;
    }

    /**
     * Set value for cell's run
     *
     * @param cell
     * @param value
     * @return same ObjectListColumn
     * @see BaseElement#setValue(java.lang.Object)
     */
    protected ObjectListColumn setCellValue(Cell cell, Object value) {
        getRun(cell).setValue(value);
        return this;
    }

    /**
     * Set font style for cell's run
     *
     * @param cell
     * @param fontStyle
     * @return same ObjectListColumn
     * @see BaseElement#setValue(java.lang.Object)
     */
    protected ObjectListColumn setCellFontStyle(Cell cell, FontStyle fontStyle) {
        getRun(cell).setFontStyle(fontStyle);
        return this;
    }

    /**
     * Call setCellValueExpression for header cell
     *
     * @param valueExpression
     * @return same ObjectListColumn
     * @see #setCellValueExpression(java.lang.String)
     */
    public ObjectListColumn setHeaderValueExpression(String valueExpression) {
        return setCellValueExpression(header, valueExpression);
    }

    /**
     * Call setCellObjectPropertyName for header cell
     *
     * @param objectPropertyName
     * @return same ObjectListColumn
     * @see #setCellObjectPropertyName
     */
    public ObjectListColumn setHeaderObjectPropertyName(String objectPropertyName) {
        return setCellObjectPropertyName(header, objectPropertyName);
    }

    /**
     * Call setCellValue for header cell
     *
     * @param value
     * @return same ObjectListColumn
     * @see #setCellValue
     */
    public ObjectListColumn setHeaderValue(Object value) {
        return setCellValue(header, value);
    }

    /**
     * Call setHeaderObject for header cell
     *
     * @param object
     * @return same ObjectListColumn
     * @see #setHeaderObject
     */
    public ObjectListColumn setHeaderObject(Object object) {
        return setCellObject(header, object);
    }

    /**
     * Call setCellFontStyle for header cell
     *
     * @param fontStyle
     * @return same ObjectListColumn
     * @see #setCellFontStyle
     */
    public ObjectListColumn setHeaderFontStyle(FontStyle fontStyle) {
        return setCellFontStyle(header, fontStyle);
    }

    /**
     * Call setCellValueExpression for footer cell
     *
     * @param valueExpression
     * @return same ObjectListColumn
     * @see #setCellValueExpression
     */
    public ObjectListColumn setFooterValueExpression(String valueExpression) {
        return setCellValueExpression(footer, valueExpression);
    }

    /**
     * Call setCellObjectPropertyName for footer cell
     *
     * @param objectPropertyName
     * @return same ObjectListColumn
     * @see #setCellObjectPropertyName
     */
    public ObjectListColumn setFooterObjectPropertyName(String objectPropertyName) {
        return setCellObjectPropertyName(footer, objectPropertyName);
    }

    /**
     * Call setCellValue for footer cell
     *
     * @param value
     * @return same ObjectListColumn
     * @see #setCellValue
     */
    public ObjectListColumn setFooterValue(Object value) {
        return setCellValue(footer, value);
    }

    /**
     * Call setCellObject for footer cell
     *
     * @param object
     * @return same ObjectListColumn
     * @see #setCellObject
     */
    public ObjectListColumn setFooterObject(Object object) {
        return setCellObject(footer, object);
    }

    /**
     * Call setCellFontStyle for footer cell
     *
     * @param fontStyle
     * @return same ObjectListColumn
     * @see #setCellFontStyle
     */
    public ObjectListColumn setFooterFontStyle(FontStyle fontStyle) {
        return setCellFontStyle(footer, fontStyle);
    }

    /**
     * Call setCellValueExpression for body cell
     *
     * @param valueExpression
     * @return same ObjectListColumn
     * @see #setCellValueExpression
     */
    public ObjectListColumn setBodyValueExpression(String valueExpression) {
        return setCellValueExpression(body, valueExpression);
    }

    /**
     * Call setCellObjectPropertyName for body cell
     *
     * @param objectPropertyName
     * @return same ObjectListColumn
     * @see #setCellObjectPropertyName
     */
    public ObjectListColumn setBodyObjectPropertyName(String objectPropertyName) {
        return setCellObjectPropertyName(body, objectPropertyName);
    }

    /**
     * Call setCellValue for body cell
     *
     * @param value
     * @return same ObjectListColumn
     * @see #setCellValue
     */
    public ObjectListColumn setBodyValue(Object value) {
        return setCellValue(body, value);
    }

    /**
     * Call setCellObject for body cell
     *
     * @param object
     * @return same ObjectListColumn
     * @see #setCellObject
     */
    public ObjectListColumn setBodyObject(Object object) {
        return setCellObject(body, object);
    }

    /**
     * Call setCellFontStyle for body cell
     *
     * @param fontStyle
     * @return same ObjectListColumn
     * @see #setCellFontStyle
     */
    public ObjectListColumn setBodyFontStyle(FontStyle fontStyle) {
        return setCellFontStyle(body, fontStyle);
    }

    /**
     * Sets valuePropertyName for body cell
     *
     * @param valuePropertyName property which displays in cell
     * @return same ObjectListColumn
     */
    public ObjectListColumn setBodyValuePropertyName(String valuePropertyName) {
        return setBodyValueExpression(CommonUtils.getPropertyExpressionByName(valuePropertyName));
    }

    /**
     * Sets valuePropertyName for header cell
     *
     * @param valuePropertyName property which displays in cell
     * @return same ObjectListColumn
     */
    public ObjectListColumn setHeaderValuePropertyName(String valuePropertyName) {
        return setHeaderValueExpression(CommonUtils.getPropertyExpressionByName(valuePropertyName));
    }

    /**
     * Sets valuePropertyName for footer cell
     *
     * @param valuePropertyName property which displays in cell
     * @return same ObjectListColumn
     */
    public ObjectListColumn setFooterValuePropertyName(String valuePropertyName) {
        return setFooterValueExpression(CommonUtils.getPropertyExpressionByName(valuePropertyName));
    }

    @Override
    protected void internalWriteToWordDoc() {
        super.internalWriteToWordDoc();
        ObjectListTable table = getTable();
        List list = table.getList();
        if (getTable().getHasHeader()) {
            //set header cell column, content and write it
            header.setPosition(0, position);
            table.writeCell(header);
        }
        if (getTable().getHasFooter()) {
            //set footer cell column, content and write it
            footer.setPosition(0, position);
            table.writeCell(footer);
        }

        //object for body cell can be specified external and then it will be displayed in
        //table body, not object from list
        boolean useListObject = body.getObject() == null;
        for (int i = 0; i < list.size(); i++) {
            //set body cell row and column, content and write it
            if (useListObject) {
                body.setObject(list.get(i));
            }
            body.setPosition(i, position);
            table.writeCell(body);
        }

        //clear object field, if need
        if (useListObject) {
            body.setObject(null);
        }

    }
}
