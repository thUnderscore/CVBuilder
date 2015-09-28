package org.thUnderscore.app.cv;

import java.io.InputStream;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;

/**
 * CV builder utils
 *
 * @author thUnderscore
 */
public class CVUtils {

    /**
     * path to resources inside jar
     */
    public static final String RESOURCES_PATH = "/org/thUnderscore/app/cv/resources/";

    /**
     * bundle name for i18n
     */
    private static final String I18N_BUNDLE_NAME = RESOURCES_PATH + "messages";

    /**
     * bundle for i18n
     */
    private static ResourceBundle i18Bundle;

    /**
     * Gets bundle fot i18n
     *
     * @return bundle which contains i18n strings
     */
    public static ResourceBundle geti18nBundle() {
        if (i18Bundle == null) {
            i18Bundle = ResourceBundle.getBundle(I18N_BUNDLE_NAME.replaceFirst("/", ""));
        }
        return i18Bundle;
    }

    /**
     * Get internationalized string by key
     *
     * @param key the key for the desired string
     * @return
     */
    public static String i18n(String key) {
        return geti18nBundle().getString(key);
    }

    /**
     * Gets image relative to RESOURCES_PATH
     *
     * @param resourceName relative path to image resource
     * @return resource as image
     */
    public static ImageIcon getResourceAsImage(String resourceName) {
        return new ImageIcon(CVUtils.class.getResource(RESOURCES_PATH + resourceName));
    }

    /**
     * Gets resource as @see InputStream relative to RESOURCES_PATH
     *
     * @param resourceName relative path to resource
     * @return resource as string
     */
    public static InputStream getResourceAsStream(String resourceName) {
        return CVUtils.class.getResourceAsStream(RESOURCES_PATH + resourceName);
    }
}
