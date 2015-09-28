package org.thUnderscore.common.mvc.word.elements;

import java.util.List;

/**
 * Element which can display list of object to table
 * 
 * @author thUnderscore
 */
public abstract class ListTable extends BaseTable {

    /**
     * 
     * @param objectPropertyName paren element object property name
     * @param listPropertyName property of table object, which contains toplevel
     * @param cols columns count
     * @param headerRows header rows count
     * @param footerRows  footer rows count
     */
    public ListTable(String objectPropertyName, String listPropertyName, 
              int cols, int headerRows, int footerRows) {
        super(objectPropertyName, listPropertyName, 0, cols, headerRows, footerRows);
    }
    
    /**
     * Gets is list, which should be displayed, empty
     * @return 
     */
    protected boolean isListEmpty(){
         List list = getList();
         return (list == null) || list.isEmpty();
    }
    
    /**
     * Get list to be diplayed. Just cast value property as list
     * @return objects list
     */
    protected List getList() {
        return (List) getValue();
    }   

    @Override
    protected boolean canWrite() {
        //ListTable can not be written if list is empty
        return super.canWrite() && !isListEmpty();
    }
    
    
    
}
