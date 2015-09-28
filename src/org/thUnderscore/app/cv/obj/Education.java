package org.thUnderscore.app.cv.obj;

import org.thUnderscore.common.XMLObject;

/**
 * Object contains education data
 *
 * @author thUnderscore
 */
public class Education extends XMLObject {

    /*
     Properties names
     */
    public static final String UNIVERSITY = "university";
    public static final String DEGREE = "degree";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String DEPARTMENT = "department";
    /*
     Containers for object fields
     */
    protected final StringValueContainer university = new StringValueContainer(UNIVERSITY);
    protected final StringValueContainer department = new StringValueContainer(DEPARTMENT);
    protected final StringValueContainer degree = new StringValueContainer(DEGREE);
    protected final StringValueContainer from = new StringValueContainer(FROM);
    protected final StringValueContainer to = new StringValueContainer(TO);


    /**
     * Gets education university
     *
     * @return university
     */
    public String getUniversity() {
        return university.getStringValue();
    }

    /**
     * Sets education university
     *
     * @param university university
     */
    public void setUniversity(String university) {
        this.university.setValue(university);
    }
    
    /**
     * Gets education degree
     *
     * @return degree
     */
    public String getDegree() {
        return degree.getStringValue();
    }

    /**
     * Sets education degree
     *
     * @param degree degree
     */
    public void setDegree(String degree) {
        this.degree.setValue(degree);
    }
    
    /**
     * Gets education start
     *
     * @return education start
     */
    public String getFrom() {
        return from.getStringValue();
    }

    /**
     * Sets education education start
     *
     * @param from education start
     */
    public void setFrom(String from) {
        this.from.setValue(from);
    }
    
    /**
     * Gets education finish
     *
     * @return education finish
     */
    public String getTo() {
        return to.getStringValue();
    }

    /**
     * Sets education education finish
     *
     * @param to education finish
     */
    public void setTo(String to) {
        this.to.setValue(to);
    }
    
    /**
     * Gets education department
     *
     * @return department
     */
    public String getDepartment() {
        return department.getStringValue();
    }

    /**
     * Sets education department
     *
     * @param department department
     */
    public void setDepartment(String department) {
        this.department.setValue(department);
    }
}
