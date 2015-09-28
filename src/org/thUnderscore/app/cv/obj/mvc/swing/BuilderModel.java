package org.thUnderscore.app.cv.obj.mvc.swing;

import org.thUnderscore.app.cv.obj.BuilderItem;
import org.thUnderscore.app.cv.obj.Builder;
import org.thUnderscore.common.mvc.BaseListModel;

/**
 * Model for root object of CB builder
 * 
 * @author thUnderscore
 */
public class BuilderModel extends BaseListModel {

    public BuilderModel() {
        super(BuilderItem.class);
    }

    /**
     * Gets the object casted to Builder
     * @return object casted to Builder
     */
    protected Builder getBuilder() {
        return (Builder) object;
    }

    /**
     * Gets the directory which contain CVs
     * @return Current builder directory which contain CVs
     */
    public String getDirectory() {
        return object == null ? null : getBuilder().getDirectory();
    }

    /**
     * Sets the directory which contain CVs
     * @param directory directory path
     */
    public void setDirectory(String directory) {
        if (object == null){
            return;
        }
        String oldValue = getDirectory();
        getBuilder().setDirectory(directory);
        firePropertyChange(Builder.DIRECTORY_PROP, oldValue, directory);
    }

    /**
     * Save CV Builder settiings
     */
    public void saveSettings() {
        getBuilder().saveSettings();
    }

    /**
     * Reread CVs list
     */
    public void refreshCVList() {
        list.setList(null);
        getBuilder().refreshCVList();
        addAllItemToList();
        resetCurrent();
    }

    @Override
    protected void prepareNewItem(Object item) {
        super.prepareNewItem(item);
        getBuilder().prepareItem((BuilderItem) item);
    }

}
