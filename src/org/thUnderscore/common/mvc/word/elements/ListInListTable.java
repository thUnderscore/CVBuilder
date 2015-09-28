package org.thUnderscore.common.mvc.word.elements;

import java.util.List;
import org.jdesktop.beansbinding.ELProperty;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Element which can diplay list of objects, which have list of other objects as
 * property. Objects of first list displays to table header and columns for each
 * object creates. Sublist,which contains in this objects diplays to apropriate
 * columns - row for each subobject creates. Table row count = max sublist
 * lebgth + 1 row for header + 1 row for footer (if table has footer)
 *
 * @author thUnderscore
 */
public class ListInListTable extends ListTable {

    /**
     * Cell which displays in header. By default it displays some property of
     * toplevel list object
     */
    Cell header;
    /**
     * Cell which displays in body. By default it displays some property of
     * sublist object
     */
    Cell body;
    /**
     * Cell which displays in footer. By default it displays some property of
     * toplevel list object
     */
    Cell footer;

    /**
     *
     */
    ELProperty subListELP;

    /**
     *
     * @param listPropertyName property of table object, which contains toplevel
     * list
     * @param subListPropertyName property of object from toplevel list, which
     * contains sublist list
     * @param hasFooter has table footer or not. If yes - footers row count = 1
     * @param headerPropertyName property of object from toplevel list, which
     * displays in header
     * @param bodyPropertyName property of object from sublist, which displays
     * in body
     * @param footerPropertyName property of object from toplevel list, which
     * displays in footer
     */
    public ListInListTable(String listPropertyName, String subListPropertyName,
            boolean hasFooter, String headerPropertyName, String bodyPropertyName,
            String footerPropertyName) {
        this(null, listPropertyName, subListPropertyName, hasFooter, headerPropertyName,
                bodyPropertyName, footerPropertyName);
    }

    /**
     *
     * @param objectPropertyName paren element object property name, which
     * contains table's object
     * @param listPropertyName property of table object, which contains toplevel
     * list
     * @param subListPropertyName property of object from toplevel list, which
     * contains sublist list
     * @param hasFooter has table footer or not. If yes - footers row count = 1
     * @param headerPropertyName property of object from toplevel list, which
     * displays in header
     * @param bodyPropertyName property of object from sublist, which displays
     * in body
     * @param footerPropertyName property of object from toplevel list, which
     * displays in footer
     */
    public ListInListTable(String objectPropertyName, String listPropertyName,
            String subListPropertyName, boolean hasFooter, String headerPropertyName,
            String bodyPropertyName, String footerPropertyName) {
        super(objectPropertyName, listPropertyName,  0, 1, hasFooter ? 1 : 0);
        header = createCell(Cell.CellType.HEADER, null, headerPropertyName, null, null, null);
        body = createCell(Cell.CellType.BODY, null, bodyPropertyName, null, null, null);
        footer = createCell(Cell.CellType.FOOTER, null, footerPropertyName, null, null, null);
        setSubListPropertyName(subListPropertyName);
    }

    /**
     * Get sublist from object
     *
     * @param object object from toplevel list
     * @return sublist, which displays in column
     */
    protected List getSubList(Object object) {
        return ((object == null) || (subListELP == null)) ? null
                : (List) subListELP.getValue(object);
    }

    /**
     * Sets object property name. This property should contain sublist
     *
     * @param subListPropertyName
     * @return
     */
    public final BaseElement setSubListPropertyName(String subListPropertyName) {
        subListELP = CommonUtils.isEmpty(subListPropertyName) ? null
                : ELProperty.create(CommonUtils.getPropertyExpressionByName(subListPropertyName));
        return this;
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
     * Create cell for internal usage
     * @param type type of cell
     * @param objectPropertyName property of parent element for object evaluation
     * @param valuePropertyName expression for value evaluation
     * @param object cell object property value (overlay objectPropertyName if is defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined - value will not be evaluated)
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
     * Set content and display style definition for header cell's run
     *
     * @param objectPropertyName property of parent element for object evaluation
     * @param valueExpression expression for value evaluation
     * @param object object property value (overlay objectPropertyName if is defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined - value will not be evaluated)
     * @param fontStyle content font style
     * @return same ListInListTable
     */
    public ListInListTable setHeader(String objectPropertyName,
            String valueExpression, Object object, Object value, FontStyle fontStyle) {
        return setCell(header, objectPropertyName, valueExpression, object,
                value, fontStyle);
    }
    
    /**
     * Set content and display style definition for footer cell's run
     *
     * @param objectPropertyName property of parent element for object evaluation
     * @param valueExpression expression for value evaluation
     * @param object object property value (overlay objectPropertyName if is defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined - value will not be evaluated)
     * @param fontStyle content font style
     * @return same ListInListTable
     */
    public ListInListTable setFooter(String objectPropertyName,
            String valueExpression, Object object, Object value, FontStyle fontStyle) {
        return setCell(footer, objectPropertyName, valueExpression, object,
                value, fontStyle);
    }

    /**
     * Set content and display style definition for body cell's run
     *
     * @param objectPropertyName property of parent element for object evaluation
     * @param valueExpression expression for value evaluation
     * @param object object property value (overlay objectPropertyName if is defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined - value will not be evaluated)
     * @param fontStyle content font style
     * @return same ListInListTable
     */
    public ListInListTable setBody(String objectPropertyName,
            String valueExpression, Object object, Object value, FontStyle fontStyle) {
        return setCell(body, objectPropertyName, valueExpression, object,
                value, fontStyle);
    }

    /**
     * Set content and display style definition for cell's run
     *
     * @param cell cell,which content and display style definition is changing now
     * @param objectPropertyName property of parent element for object evaluation
     * @param valueExpression expression for vlue evaluation
     * @param object cell object property value (overlay objectPropertyName if is defined - object property will not be evaluated)
     * @param value value property value (overlay valueExpression if is defined - value will not be evaluated)
     * @param fontStyle content font style
     * @return same ListInListTable
     */
    private ListInListTable setCell(Cell cell, String objectPropertyName, String valueExpression,
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
     * @param cell
     * @param valueExpression
     * @return same ListInListTable
     * @see BaseElement#setValueExpression(java.lang.String) 
     */
    protected ListInListTable setCellValueExpression(Cell cell, String valueExpression) {
        getRun(cell).setValueExpression(valueExpression);
        return this;
    }

    /**
     * Set objectPropertyName for cell's run
     * @param cell
     * @param objectPropertyName
     * @return same ListInListTable
     * @see BaseElement#setObjectPropertyName(java.lang.String) 
     */
    protected ListInListTable setCellObjectPropertyName(Cell cell, String objectPropertyName) {
        getRun(cell).setObjectPropertyName(objectPropertyName);
        return this;
    }

    /**
     * Set object for cell's run
     * @param cell
     * @param object
     * @return same ListInListTable
     * @see BaseElement#setObject(java.lang.Object) 
     */
    protected ListInListTable setCellObject(Cell cell, Object object) {
        getRun(cell).setObject(object);
        return this;
    }

    /**
     * Set value for cell's run
     * @param cell
     * @param value
     * @return same ListInListTable
     * @see BaseElement#setValue(java.lang.Object) 
     */
    protected ListInListTable setCellValue(Cell cell, Object value) {
        getRun(cell).setValue(value);
        return this;
    }

    /**
     * Set font style for cell's run
     * @param cell
     * @param fontStyle
     * @return same ListInListTable
     * @see BaseElement#setValue(java.lang.Object) 
     */
    protected ListInListTable setCellFontStyle(Cell cell, FontStyle fontStyle) {
        getRun(cell).setFontStyle(fontStyle);
        return this;
    }
    
    /**
     * Call setCellValueExpression for header cell
     * @param valueExpression
     * @return same ListInListTable
     * @see #setCellValueExpression(java.lang.String) 
     */
    public ListInListTable setHeaderValueExpression(String valueExpression) {
        return setCellValueExpression(header, valueExpression);
    }

    /**
     * Call setCellObjectPropertyName for header cell
     * @param objectPropertyName
     * @return same ListInListTable
     * @see #setCellObjectPropertyName
     */
    public ListInListTable setHeaderObjectPropertyName(String objectPropertyName) {
        return setCellObjectPropertyName(header, objectPropertyName);
    }

    /**
     * Call setCellValue for header cell
     * @param value
     * @return same ListInListTable
     * @see #setCellValue
     */
    public ListInListTable setHeaderValue(Object value) {
        return setCellValue(header, value);
    }

    /**
     * Call setHeaderObject for header cell
     * @param object
     * @return same ListInListTable
     * @see #setHeaderObject
     */
    public ListInListTable setHeaderObject(Object object) {
        return setCellObject(header, object);
    }

    /**
     * Call setCellFontStyle for header cell
     * @param fontStyle
     * @return same ListInListTable
     * @see #setCellFontStyle
     */
    @Override
    public ListInListTable setHeaderFontStyle(FontStyle fontStyle) {
        return setCellFontStyle(header, fontStyle);
    }
    
    /**
     * Call setCellValueExpression for footer cell
     * @param valueExpression
     * @return same ListInListTable
     * @see #setCellValueExpression
     */
    public ListInListTable setFooterValueExpression(String valueExpression) {
        return setCellValueExpression(footer, valueExpression);
    }

    /**
     * Call setCellObjectPropertyName for footer cell
     * @param objectPropertyName
     * @return same ListInListTable
     * @see #setCellObjectPropertyName
     */
    public ListInListTable setFooterObjectPropertyName(String objectPropertyName) {
        return setCellObjectPropertyName(footer, objectPropertyName);
    }

    /**
     * Call setCellValue for footer cell
     * @param value
     * @return same ListInListTable
     * @see #setCellValue
     */
    public ListInListTable setFooterValue(Object value) {
        return setCellValue(footer, value);
    }

    /**
     * Call setCellObject for footer cell
     * @param object
     * @return same ListInListTable
     * @see #setCellObject
     */
    public ListInListTable setFooterObject(Object object) {
        return setCellObject(footer, object);
    }

    /**
     * Call setCellFontStyle for footer cell
     * @param fontStyle
     * @return same ListInListTable
     * @see #setCellFontStyle
     */
    @Override
    public ListInListTable setFooterFontStyle(FontStyle fontStyle) {
        return setCellFontStyle(footer, fontStyle);
    }
    /**
     * Call setCellValueExpression for body cell
     *
     * @param valueExpression
     * @return same ListInListTable
     * @see #setCellValueExpression
     */
    public ListInListTable setBodyValueExpression(String valueExpression) {
        return setCellValueExpression(body, valueExpression);
    }

    /**
     * Call setCellObjectPropertyName for body cell
     * @param objectPropertyName
     * @return same ListInListTable
     * @see #setCellObjectPropertyName
     */
    public ListInListTable setBodyObjectPropertyName(String objectPropertyName) {
        return setCellObjectPropertyName(body, objectPropertyName);
    }

    /**
     * Call setCellValue for body cell
     * @param value
     * @return same ListInListTable
     * @see #setCellValue
     */
    public ListInListTable setBodyValue(Object value) {
        return setCellValue(body, value);
    }

    /**
     * Call setCellObject for body cell
     * @param object
     * @return same ListInListTable
     * @see #setCellObject
     */
    public ListInListTable setBodyObject(Object object) {
        return setCellObject(body, object);
    }

    /**
     * Call setCellFontStyle for body cell
     * @param fontStyle
     * @return same ListInListTable
     * @see #setCellFontStyle
     */
    public ListInListTable setBodyFontStyle(FontStyle fontStyle) {
        return setCellFontStyle(body, fontStyle);
    }

    /**
     * Sets valuePropertyName for body cell
     * @param valuePropertyName property which displays in cell
     * @return same ListInListTable
     */
    public ListInListTable setBodyValuePropertyName(String valuePropertyName) {
        return setBodyValueExpression(CommonUtils.getPropertyExpressionByName(valuePropertyName));
    }

    /**
     * Sets valuePropertyName for header cell
     * @param valuePropertyName property which displays in cell
     * @return same ListInListTable
     */
    public ListInListTable setHeaderValuePropertyName(String valuePropertyName) {
        return setHeaderValueExpression(CommonUtils.getPropertyExpressionByName(valuePropertyName));
    }

    /**
     * Sets valuePropertyName for footer cell
     * @param valuePropertyName property which displays in cell
     * @return same ListInListTable
     */
    public ListInListTable setFooterValuePropertyName(String valuePropertyName) {
        return setFooterValueExpression(CommonUtils.getPropertyExpressionByName(valuePropertyName));
    }

    @Override
    protected void internalWriteToWordDoc() {
        List subList;
        List list = getList();
        cols = (list != null) ? list.size() : 0;
        rows = 0;
        //get rows count as max sublist length
        for (int i = 0; i < list.size(); i++) {
            subList = getSubList(list.get(i));
            if (subList != null) {
                rows = Math.max(rows, subList.size());
            }

        }
        super.internalWriteToWordDoc();
        Object colObj;
        //for each object from toplebel list
        for (int i = 0; i < list.size(); i++) {
            //set header cell column, content and write it
            colObj = list.get(i);
            header.setPosition(0, i);
            header.setObject(colObj);
            writeCell(header);
            //get sublist
            subList = getSubList(colObj);
            //for each object of sublist
            for (int j = 0; j < subList.size(); j++) {
                //set body cell row and column, content and write it
                body.setPosition(j, i);
                body.setObject(subList.get(j));
                writeCell(body);
            }
            if (getHasFooter()) {
                //set footer cell column, content and write it
                footer.setPosition(0, i);
                footer.setObject(colObj);
                writeCell(footer);
            }
        }
    }
}
