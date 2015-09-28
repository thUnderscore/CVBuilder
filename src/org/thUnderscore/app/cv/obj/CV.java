package org.thUnderscore.app.cv.obj;

import java.awt.Image;
import java.io.InputStream;
import org.thUnderscore.common.XMLObject;
import org.thUnderscore.common.XMLObjectList;
import org.thUnderscore.common.utils.XMLUtils;
import org.w3c.dom.Document;

/**
 * Object contains CV data
 *
 * @author thUnderscore
 */
public class CV extends XMLObject {

    /*
        Properties names
     */
    public static final String ROOT_NODE_NAME = "cv";
    public static final String COMMUNICATIONS_NODE_NAME = "communications";
    public static final String EDUCATIONS_NODE_NAME = "educations";
    public static final String COURSES_NODE_NAME = "courses";
    public static final String SKILLS_NODE_NAME = "skills";
    public static final String EMPLOYMENTS_NODE_NAME = "employments";

    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String PHOTO = "photo";
    public static final String SUMMARY = "summary";

    /*
     Containers for object fields
     */
    protected final StringValueContainer name = new StringValueContainer(NAME);
    protected final StringValueContainer address = new StringValueContainer(ADDRESS);
    protected final ByteArrValueContainer photo = new ByteArrValueContainer(PHOTO);
    protected final XMLObjectValueContainer communications
            = new XMLObjectValueContainer(COMMUNICATIONS_NODE_NAME, Communications.class);
    protected final StringValueContainer summary = new StringValueContainer(SUMMARY);
    protected final XMLObjectListContainer educations
            = new XMLObjectListContainer(EDUCATIONS_NODE_NAME, Education.class);
    protected final XMLObjectListContainer courses
            = new XMLObjectListContainer(COURSES_NODE_NAME, Course.class);
    protected final XMLObjectListContainer skills
            = new XMLObjectListContainer(SKILLS_NODE_NAME, Skill.class);
    protected final XMLObjectListContainer employments
            = new XMLObjectListContainer(EMPLOYMENTS_NODE_NAME, Employment.class);

    public CV() {
        this(XMLUtils.getDocumentBuilder().newDocument());
    }

    public CV(Document document) {
        setNodeName(ROOT_NODE_NAME);
        loadFromDocument(document);
    }

    /**
     * Gets person's name
     *
     * @return name
     */
    public String getName() {
        return name.getStringValue();
    }

    /**
     * Sets person's name
     * @param name name
     */
    public void setName(String name) {
        this.name.setValue(name);
    }

    /**
     * Gets person's address
     *
     * @return address
     */
    public String getAddress() {
        return address.getStringValue();
    }

    /**
     * Sets person's address
     * @param address address
     */
    public void setAddress(String address) {
        this.address.setValue(address);
    }

    /**
     * Gets person's photo
     *
     * @return photo
     */
    public Image getPhoto() {
        return photo.getImage();
    }

    /**
     * Gets person's photo as stream
     *
     * @return stream contains photo
     */
    public InputStream getPhotoAsStream() {
        return photo.getInputStream();
    }

    /**
     * Sets person's photo
     * @param photo photo
     */
    public void setPhoto(Image photo) {
        this.photo.setImage(photo);
    }

    /**
     * Gets person's summary
     *
     * @return summary
     */
    public String getSummary() {
        return summary.getStringValue();
    }

    /**
     * Sets person's summary
     * @param summary summary
     */
    public void setSummary(String summary) {
        this.summary.setValue(summary);
    }
    
    /**
     * Gets object which contains person's communications
     *
     * @return communications object
     */
    public Communications getCommunications() {
        return (Communications) communications.getXMLObjectValue();
    }

    /**
     * Gets object which contains person's educations
     *
     * @return educations object
     */
    public XMLObjectList getEducations() {
        return educations.getXMLObjectList();
    }

    /**
     * Gets object which contains person's cources
     *
     * @return cources object
     */
    public XMLObjectList getCourses() {
        return courses.getXMLObjectList();
    }

    /**
     * Gets object which contains person's skills
     *
     * @return skills object
     */
    public XMLObjectList getSkills() {
        return skills.getXMLObjectList();
    }

    /**
     * Gets object which contains person's employments
     *
     * @return employments object
     */
    public XMLObjectList getEmployments() {
        return employments.getXMLObjectList();
    }   

}
