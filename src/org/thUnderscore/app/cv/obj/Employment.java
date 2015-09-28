package org.thUnderscore.app.cv.obj;

import org.thUnderscore.common.XMLObject;

/**
 * Object contains employment data
 *
 * @author thUnderscore
 */
public class Employment extends XMLObject {

    /*
     Properties names
     */
    public static final String COMPANY_DESCR = "companyDescription";
    public static final String COMPANY = "company";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String ROLE = "role";
    public static final String ROLE_DESCR = "roleDescription";
    public static final String TECHNOLOGIES = "technologies";
    public static final String ACHIEVMENTS = "achievments";
    /*
     Containers for object fields
     */
    protected final StringValueContainer companyDescription = new StringValueContainer(COMPANY_DESCR);
    protected final StringValueContainer company = new StringValueContainer(COMPANY);
    protected final StringValueContainer roleDescription = new StringValueContainer(ROLE_DESCR);
    protected final StringValueContainer role = new StringValueContainer(ROLE);
    protected final StringValueContainer from = new StringValueContainer(FROM);
    protected final StringValueContainer to = new StringValueContainer(TO);
    protected final StringValueContainer technologies = new StringValueContainer(TECHNOLOGIES);
    protected final StringValueContainer achievments = new StringValueContainer(ACHIEVMENTS);    

    /**
     * Gets employment company
     *
     * @return company
     */
    public String getCompany() {
        return company.getStringValue();
    }

    /**
     * Sets employment company
     *
     * @param company company
     */
    public void setCompany(String company) {
        this.company.setValue(company);
    }

    /**
     * Gets company description
     *
     * @return company description
     */
    public String getCompanyDescription() {
        return companyDescription.getStringValue();
    }

    /**
     * Sets company description
     *
     * @param companyDescription company description
     */
    public void setCompanyDescription(String companyDescription) {
        this.companyDescription.setValue(companyDescription);
    }
    
    /**
     * Gets role description
     *
     * @return role description
     */
    public String getRoleDescription() {
        return roleDescription.getStringValue();
    }

    /**
     * Sets role description
     *
     * @param roleDescription role description
     */
    public void setRoleDescription(String roleDescription) {
        this.roleDescription.setValue(roleDescription);
    }

    /**
     * Gets employment role
     *
     * @return role
     */
    public String getRole() {
        return role.getStringValue();
    }

    /**
     * Sets employment role
     *
     * @param role role
     */
    public void setRole(String role) {
        this.role.setValue(role);
    }

    /**
     * Gets employment start
     *
     * @return employment start
     */
    public String getFrom() {
        return from.getStringValue();
    }

    /**
     * Sets employment start
     *
     * @param from employment start
     */
    public void setFrom(String from) {
        this.from.setValue(from);
    }

    /**
     * Gets employment finish
     *
     * @return employment finish
     */
    public String getTo() {
        return to.getStringValue();
    }

    /**
     * Sets employment finish
     *
     * @param to employment finish
     */
    public void setTo(String to) {
        this.to.setValue(to);
    }

    /**
     * Gets technologies list
     *
     * @return technologies list
     */
    public String getTechnologies() {
        return technologies.getStringValue();
    }

    /**
     * Sets technologies list
     *
     * @param technologies technologies list
     */
    public void setTechnologies(String technologies) {
        this.technologies.setValue(technologies);
    }
    
    /**
     * Gets achievments list
     *
     * @return achievments list
     */
    public String getAchievments() {
        return achievments.getStringValue();
    }

    /**
     * Sets employment achievments list
     *
     * @param achievments achievments list
     */
    public void setAchievments(String achievments) {
        this.achievments.setValue(achievments);
    }
}
