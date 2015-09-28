package org.thUnderscore.app.cv.obj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.PropertyChangeSupportedObject;
import org.thUnderscore.common.utils.XMLUtils;
import org.thUnderscore.common.mvc.intf.ListInclusiveObject;
import org.w3c.dom.Document;

/**
 * CV Builder object.
 *
 * @author thUnderscore
 */
public class Builder extends PropertyChangeSupportedObject implements ListInclusiveObject {
    /*
     Resources constants
     */

    static final String STATUS_OK_RES = "Builder.STATUS_OK";
    static final String STATUS_CANT_READ_LISTR_RES = "Builder.STATUS_CANT_READ_LIST";
    public static final String DEFAULT_CV_RES = "default.cv";

    public static final String CV_FILE_EXTENSION = ".cv";

    /*
     Propertuies constants
     */
    public static final String STATUS_DESCRIPTION_PROP = "statusDescription";
    public static final String STATUS_PROP = "status";
    public static final String DIRECTORY_PROP = "directory";

    //builder settings file
    static final File PROPERTIES_FILE = new File(CommonUtils.getJarDirectory(), "cvbuilder.properties");

    /**
     * builder items list
     */
    List<BuilderItem> list = new ArrayList<BuilderItem>();
    /**
     * builder items map. used for finding was CV with such ID already loaded
     */
    Map<String, BuilderItem> hash = new HashMap<String, BuilderItem>();
    /**
     * Current directory, which contains CVs
     */
    String directory;
    /**
     * CV loading result. true if OK
     */
    boolean status;
    /**
     * CV loading result description
     */
    String statusDescription;
    /**
     * ID of CV? which was loaded from resources (my CV)
     */
    String defaultCVId;
    /**
     * Error occured during save settings
     */
    boolean storePropertiesExceptionOccured = false;
    /**
     * File object for current directory
     */
    public File currentLoadedDir;

    protected void setStatusDescription(String statusDescription) {
        firePropertyChange(STATUS_DESCRIPTION_PROP,
                this.statusDescription, this.statusDescription = statusDescription);
    }

    /**
     * Gets is CV list loaded successfully
     *
     * @return false if error occured
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Gets status description
     *
     * @return Error description if status is false
     */
    public String getStatusDescription() {
        return status ? CVUtils.i18n(STATUS_OK_RES) : statusDescription;
    }

    /**
     * Save builder settings
     */
    public void saveSettings() {
        Properties properties = new Properties();
        properties.put(DIRECTORY_PROP, getDirectory());
        try {
            final OutputStream stream = new FileOutputStream(PROPERTIES_FILE);
            try {
                properties.store(stream, "");
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            if (!storePropertiesExceptionOccured) {
                storePropertiesExceptionOccured = true;
            }
        }
    }

    /**
     * Load settings
     */
    public void loadSettings() {
        Properties properties = new Properties();
        if (PROPERTIES_FILE.exists()) {
            try {
                final InputStream stream = new FileInputStream(PROPERTIES_FILE);
                try {
                    properties.load(stream);
                } finally {
                    stream.close();
                }
            } catch (IOException e) {
                if (!storePropertiesExceptionOccured) {
                    storePropertiesExceptionOccured = true;
                }
            }
            setDirectory(properties.getProperty(DIRECTORY_PROP));
        }
    }

    /**
     * Load default CV (my CV) from resources
     */
    protected void loadCVFromResources() {
        BuilderItem builderItem;
        builderItem = readCV(CVUtils.getResourceAsStream(DEFAULT_CV_RES));
        addItem(builderItem);
        defaultCVId = builderItem.getID();
    }

    /**
     * Load CV from current directory
     */
    protected void loadCVFromDirectory() {
        if (!currentLoadedDir.exists() || !currentLoadedDir.isDirectory()) {
            return;
        }
        File[] listFiles = currentLoadedDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(CV_FILE_EXTENSION);
            }
        });
        BuilderItem item;
        for (File file : listFiles) {
            try {
                final InputStream stream = new FileInputStream(file);
                try {
                    item = readCV(stream);
                } finally {
                    stream.close();
                }
            } catch (IOException ex) {
                item = new BuilderItem(ex);
            }
            item.setFile(file);
            addItem(item);
        }
    }

    /**
     * Gets current directory
     *
     * @return
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets current directory
     *
     * @param directory - directory
     */
    public void setDirectory(String directory) {
        firePropertyChange(DIRECTORY_PROP, this.directory, this.directory = directory);
    }

    /**
     * Refresh CV list
     */
    public void refreshCVList() {
        if (CommonUtils.isEmpty(directory)) {
            setDirectory(CommonUtils.getJarDirectory());
        }
        File dirFile = new File(directory);
        boolean isOK = true;
        if (!dirFile.exists()) {
            isOK = dirFile.mkdirs();
        }
        setStatus(isOK);
        if (!isOK) {
            setStatusDescription(CVUtils.i18n(STATUS_CANT_READ_LISTR_RES));
        }
        list.clear();
        hash.clear();
        currentLoadedDir = new File(getDirectory());
        loadCVFromResources();
        loadCVFromDirectory();
        BuilderItem defaultItem = hash.get(defaultCVId);
        if (defaultItem != null && list.size() > 1) {
            list.set(list.indexOf(defaultItem), list.get(0));
            list.set(0, defaultItem);
        }
    }

    /**
     * Sets current directory and refresh list
     *
     * @param directory
     */
    public void readCVList(String directory) {
        setDirectory(directory);
        refreshCVList();
    }

    /**
     * Parse CV from stream
     *
     * @param data stream which contains CV
     * @return BuilderItem which wraps CV or parse error
     */
    protected BuilderItem readCV(InputStream data) {
        BuilderItem result;
        try {
            CV cv;
            if (data == null) {
                cv = null;
            } else {
                Document document = XMLUtils.getDocumentBuilder().parse(data);
                cv = new CV(document);
            }
            result = new BuilderItem(cv);
        } catch (Exception e) {
            result = new BuilderItem(e);
        }
        return result;
    }

    /**
     * Sets loading builder status
     *
     * @param status status, true if OK
     */
    protected void setStatus(boolean status) {
        firePropertyChange(STATUS_PROP, this.status, this.status = status);
        setStatusDescription(getStatusDescription());
    }

    /**
     * Add BuilderItem to list
     *
     * @param item item to be added
     * @return true (as specified by {@link Collection#add})
     */
    protected boolean addItem(BuilderItem item) {
        prepareItem(item);
        String id = item.getID();
        if (hash.containsKey(id)) {
            //if item with such id loaded - replace it
            //it happens if default CV was changed, stored to directory and is loading
            list.remove(hash.get(id));
            hash.remove(id);
        }
        boolean added = list.add(item);
        if (added) {
            hash.put(id, item);
        }
        return added;
    }

    /**
     * Prepare just created item
     *
     * @param item created item
     */
    public void prepareItem(BuilderItem item) {
        File file = item.getFile();
        if (file == null) {
            //set file for saving
            String id = item.getID();
            file = new File(currentLoadedDir, id + Builder.CV_FILE_EXTENSION);
            item.setFile(file);
        }
    }

    @Override
    public List getList() {
        return list;
    }

    @Override
    public Object createItem() {
        return new BuilderItem(new CV());
    }

    @Override
    public void itemRemoved(Object item) {
        File file = ((BuilderItem) item).getFile();
        if ((file != null) && (file.exists())) {
            file.delete();
        }
    }

    @Override
    public void itemAdded(Object item) {

    }

    @Override
    public void itemSwaped(int i, int j) {

    }

}
