package org.thUnderscore.app.cv.obj;

import org.thUnderscore.common.XMLObject;

/**
 * Object contains skill item data
 *
 * @author thUnderscore
 */
public class SkillItem extends XMLObject {

    /*
     Properties names
     */
    public static final String NAME = "name";
    /*
     Containers for object fields
     */
    protected final StringValueContainer name = new StringValueContainer(NAME);

    /**
     * Gets skill item name
     *
     * @return name
     */
    public String getName() {
        return name.getStringValue();
    }

    /**
     * Sets skill item name
     * @param name name
     */
    public void setName(String name) {
        this.name.setValue(name);
    }
}
