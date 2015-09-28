package org.thUnderscore.app.cv.obj;

import org.thUnderscore.common.XMLObject;

/**
 * Object contains communications data
 *
 * @author thUnderscore
 */
public class Communications extends XMLObject {

    /*
     Properties names
     */
    public static final String PHONE = "phone";
    public static final String SKYPE = "skype";
    public static final String WEB = "web";
    public static final String EMAIL = "email";
    /*
     Containers for object fields
     */
    protected final StringValueContainer phone = new StringValueContainer(PHONE);
    protected final StringValueContainer skype = new StringValueContainer(SKYPE);
    protected final StringValueContainer web = new StringValueContainer(WEB);
    protected final StringValueContainer email = new StringValueContainer(EMAIL);

    /**
     * Gets person's phone
     *
     * @return phone
     */
    public String getPhone() {
        return phone.getStringValue();
    }

    /**
     * Sets person's phone
     *
     * @param phone phone
     */
    public void setPhone(String phone) {
        this.phone.setValue(phone);
    }

    /**
     * Gets person's skype
     *
     * @return skype
     */
    public String getSkype() {
        return skype.getStringValue();
    }

    /**
     * Sets person's skype
     *
     * @param skype skype
     */
    public void setSkype(String skype) {
        this.skype.setValue(skype);
    }

    /**
     * Gets person's web
     *
     * @return web
     */
    public String getWeb() {
        return web.getStringValue();
    }

    /**
     * Sets person's web
     *
     * @param web web
     */
    public void setWeb(String web) {
        this.web.setValue(web);
    }

    /**
     * Gets person's email
     *
     * @return email
     */
    public String getEmail() {
        return email.getStringValue();
    }

    /**
     * Sets person's email
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email.setValue(email);
    }
}
