package org.thUnderscore.common.mvc.word.intf;

import org.thUnderscore.common.mvc.intf.ObjectView;

/**
 * Interface for class which can display objects to MS Word file
 * @author thUnderscore
 */
public interface WordObjectView extends ObjectView {

    /**
     * Display model object to file
     */
    public void saveToFile();

    /**
     * Gets file name, where model object will be displayed
     * @return file name 
     */
    public String getFileName();

    /**
     * Sets file name, where model object will be displayed
     * @param fileName 
     */
    public void setFileName(String fileName);

    /**
     * Show file selection interface
     * @return true if file was selected, false if action was canceled
     */
    public boolean selectFile();

}
