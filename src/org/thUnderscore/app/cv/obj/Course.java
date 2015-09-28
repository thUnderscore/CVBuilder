package org.thUnderscore.app.cv.obj;

import org.thUnderscore.common.XMLObject;

/**
 * Object contains course data
 *
 * @author thUnderscore
 */
public class Course extends XMLObject {
    /*
     Properties names
     */
    public static final String SCHOOL = "school";
    public static final String NAME = "name";
    public static final String WHEN = "when";
    /*
     Containers for object fields
     */
    protected final StringValueContainer school = new StringValueContainer(SCHOOL);
    protected final StringValueContainer name = new StringValueContainer(NAME);
    protected final StringValueContainer when = new StringValueContainer(WHEN);

    /**
     * Gets course school
     *
     * @return school
     */
    public String getSchool() {
        return school.getStringValue();
    }

    /**
     * Sets course school
     *
     * @param school school
     */
    public void setSchool(String school) {
        this.school.setValue(school);
    }
    
    /**
     * Gets course name
     *
     * @return name
     */
    public String getName() {
        return name.getStringValue();
    }

    /**
     * Sets course name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name.setValue(name);
    }
    
    /**
     * Gets course period
     *
     * @return period
     */
    public String getWhen() {
        return when.getStringValue();
    }

    /**
     * Sets course period
     *
     * @param when period
     */
    public void setWhen(String when) {
        this.when.setValue(when);
    }
}
