package org.thUnderscore.app.cv.obj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static org.thUnderscore.app.cv.CVUtils.i18n;
import org.thUnderscore.common.PropertyChangeSupportedObject;
import org.thUnderscore.common.mvc.intf.EditableObject;

/**
 * Wrapper for CV object
 *
 * @author thUnderscore
 */
public class BuilderItem extends PropertyChangeSupportedObject implements EditableObject {

    /*
     Resources constants
     */
    private static final String ERROR_CV_NOT_ASSIGNED = "BuilderItem.ERROR_CV_NOT_ASSIGNED";
    /*
     Propertuies constants
     */
    public static final String CV = "cv";

    /**
     * wrapped CV
     */
    CV cv;
    /**
     * It indicates item contains correct CV or it was loaded with error
     */
    boolean isError;
    /**
     * Error description if isError = true
     */
    String errorDescription;
    /**
     * File for saving CV
     */
    private File file;

    public BuilderItem(CV cv, boolean isError, String errorDescription) {
        if ((cv == null) && !isError) {
            isError = true;
            errorDescription = i18n(ERROR_CV_NOT_ASSIGNED);
        }

        this.cv = cv;
        this.isError = isError;
        this.errorDescription = errorDescription;
    }

    public BuilderItem(CV cv) {
        this(cv, false, null);
    }

    public BuilderItem() {
        this(new CV());
    }

    public BuilderItem(Exception exception) {
        this(null, true, exception.getMessage());
    }

    @Override
    public boolean isModified() {
        return cv == null ? false : cv.isModified();
    }

    @Override
    public void setModified(boolean isModified) {
        if (cv != null) {
            cv.setModified(isModified);
        }
    }

    @Override
    public void save() {
        try {
            OutputStream stream = new FileOutputStream(file);
            try {
		cv.saveToStream(stream);
            } finally {
                stream.close();
            }            
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void cancel() {
        cv.cancel();
    }

    @Override
    public boolean isNewObject() {
        return cv == null ? false : cv.isNewObject();
    }

    @Override
    public void setNewObject(boolean newObject) {
        if (cv != null) {
            cv.setNewObject(newObject);
        }
    }

    /**
     * Gets CV's id
     *
     * @return CV's id or null if isError = true
     */
    public String getID() {
        return cv == null ? "" : cv.getIDAsStr();
    }

    /**
     * sets file for saving CV
     *
     * @param file file object
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Gets file for saving CV
     *
     * @return file object
     */
    public File getFile() {
        return file;
    }

    /**
     * Gets CV object
     *
     * @return CV object or null if isError
     */
    public CV getCv() {
        return cv;
    }

    /**
     * Gets is object wrap error
     *
     * @return true if wrap error
     */
    public boolean isError() {
        return isError;
    }

    /**
     * Gets error description id isError = true
     *
     * @return
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Itself. Workaraound ELProperty restriction
     *
     * @return itself
     */
    public BuilderItem getSelf() {
        return this;
    }
}
