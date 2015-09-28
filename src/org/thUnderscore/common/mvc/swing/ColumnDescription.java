package org.thUnderscore.common.mvc.swing;

import org.thUnderscore.common.mvc.swing.intf.SwingListView;

/**
 * Description of columnn in SwingListView
 *
 * @see SwingListView
 * @author thUnderscore
 */
public class ColumnDescription {

    /**
     * Column name
     */
    String name;
    /**
     * Column title
     */
    String title;
    /**
     * Column objet class
     */
    Class clazz;

    /**
     *
     * @param name Column name
     * @param title Column title
     * @param clazz Column objet class
     */
    public ColumnDescription(String name, String title, Class clazz) {
        this.name = name;
        this.title = title;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Class getClazz() {
        return clazz;
    }

}
