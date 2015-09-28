package org.thUnderscore.app.cv.obj;

import org.thUnderscore.common.XMLObjectList;

/**
 * Object contains skill data
 *
 * @author thUnderscore
 */
public class Skill extends XMLObjectList {

    /*
     Properties names
     */
    public static final String NAME = "name";

    {
        //set class of list item
        listItemClass = SkillItem.class;
    }
    /*
     Containers for object fields
     */
    protected final StringValueContainer name = new StringValueContainer(NAME);

    /**
     * Gets skill name
     *
     * @return name
     */
    public String getName() {
        return name.getStringValue();
    }

    /**
     * Sets skill name
     * @param name name
     */
    public void setName(String name) {
        this.name.setValue(name);
    }
}
