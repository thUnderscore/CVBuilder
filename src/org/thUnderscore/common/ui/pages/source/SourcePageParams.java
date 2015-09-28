package org.thUnderscore.common.ui.pages.source;

import java.util.LinkedHashMap;
import org.thUnderscore.common.ui.pages.PageParams;

/**
 * Params for SourcePage
 *
 * @author thUnderscore
 */
public class SourcePageParams extends PageParams {

    /**
     * resorces to source tree positions map
     */
    private final LinkedHashMap<String, String> sources = new LinkedHashMap<String, String>();
    /**
     * Path to node which should be selected aftersources tree was built
     */
    private final String selectedAfterInitPath;

    public SourcePageParams(String selectedAfterInitPath) {
        this.selectedAfterInitPath = selectedAfterInitPath;
    }

    /**
     * Gets resorces to source tree positions map
     *
     * @return Map
     */
    public LinkedHashMap<String, String> getSources() {
        return (LinkedHashMap<String, String>) sources.clone();
    }

    /**
     * Clear params
     */
    public void clearSources() {
        sources.clear();
    }

    /**
     * Add tree source root
     *
     * @param resourcePath path to resource
     * @return same SourcePageParams
     */
    public SourcePageParams addRoot(String resourcePath) {
        return addSource(resourcePath, null);
    }

    /**
     * Add resource path to tree path map record
     *
     * @param resourcePath path to resource inside jar
     * @param treePath path in tree. Without resource itself node name. If node
     * is root - null
     * @return same SourcePageParams
     */
    public SourcePageParams addSource(String resourcePath, String treePath) {
        sources.put(resourcePath, treePath);
        return this;
    }

    /**
     * Gets path to node which should be selected aftersources tree was built
     *
     * @return
     */
    public String getSelectedAfterInitPath() {
        return selectedAfterInitPath;
    }

}
