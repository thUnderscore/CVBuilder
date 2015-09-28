package org.thUnderscore.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;

/**
 * Commot utils
 *
 * @author mikD
 */
public class CommonUtils {

    /**
     * IS current OS Windows
     */
    public static final boolean IS_WIN = System.getProperty("os.name").toLowerCase().startsWith("win");
    /**
     * path to resources inside jar
     */
    public static final String RESOURCES_PATH = "/org/thUnderscore/common/resources/";
    /**
     * bundle name for i18n
     */
    private static final String I18N_BUNDLE_NAME = RESOURCES_PATH + "messages";
    /**
     * bundle for i18n
     */
    private static ResourceBundle i18nBundle;
    /**
     * Cached getters
     */
    private static Map<String, Method> gettersMap;

    /**
     * Gets bundle fot i18n
     *
     * @return bundle which contains i18n strings
     */
    public static ResourceBundle geti18nBundle() {
        if (i18nBundle == null) {
            i18nBundle = ResourceBundle.getBundle(I18N_BUNDLE_NAME.replaceFirst("/", ""));
        }
        return i18nBundle;
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
     * Gets resource as ImageIcon
     *
     * @param resourceName relative path
     * @return ImageIcon
     * @see #RESOURCES_PATH
     */
    public static ImageIcon getResourceAsImage(String resourceName) {
        return new ImageIcon(CommonUtils.class.getResource(RESOURCES_PATH + resourceName));
    }

    /**
     * Gets resource as stream
     *
     * @param resourceName relative path
     * @return stream
     * @see #RESOURCES_PATH
     */
    static InputStream getResourceAsStream(String resourceName) {
        return CommonUtils.class.getResourceAsStream(RESOURCES_PATH + resourceName);
    }

    /**
     * Read resource by name. Correctly work in debug time when resource placed
     * to diectory, not to jar
     *
     * @param name full path to resource
     * @return resource as byte[]
     * @throws IOException
     */
    public static byte[] readResource(String name) throws IOException {
        byte[] content;
        InputStream resource = CommonUtils.class.
                getResourceAsStream(name);
        try  {
            content = readByteArrayFromStream(resource);
        } finally {
            resource.close();
        }
        return content;
    }

    /**
     * Safely load icon from resource
     *
     * @param resourceName
     * @return null if resource not exists
     */
    public static ImageIcon safeLoadIconResource(String resourceName) {
        URL resource = CommonUtils.class.getResource(resourceName);
        return resource == null ? null : new ImageIcon(resource);
    }

    /**
     * Gets is string not null and has length more than 0
     *
     * @param string to be checked
     * @return true if not empty
     */
    public static boolean isNotEmpty(String string) {
        return string != null && string.length() > 0;
    }

    /**
     * Gets is string null or has length == 0
     *
     * @param string to be checked
     * @return true if empty
     */
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * Gets is byte[] null or has length == 0
     *
     * @param arr to be checked
     * @return true if empty
     */
    public static boolean isEmpty(byte[] arr) {
        return (arr == null) || (((byte[]) arr).length == 0);
    }

    /**
     * Read stream to byte[]
     *
     * @param stream source stream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] readByteArrayFromStream(InputStream stream) throws IOException {
        byte[] data = new byte[0];
        if (stream != null) {
            data = new byte[stream.available()];
            int offset = 0;
            int numRead = 0;
            while (offset < data.length
                    && (numRead = stream.read(data, offset,
                    data.length - offset)) >= 0) {
                offset += numRead;
            }
        }
        return data;
    }

    /**
     * get Directory of current jar
     *
     * @return
     */
    public static String getJarDirectory() {
        String path = CommonUtils.class.getProtectionDomain().
                getCodeSource().getLocation().getPath();
        File file = new File(path);
        if (file.isFile()) {
            file = file.getParentFile();
        }
        return file.getAbsolutePath();
    }

    /**
     * Safely gets are object not equal
     *
     * @param obj1
     * @param obj2
     * @return true if objects are not equal
     */
    public static boolean isNotEqual(Object obj1, Object obj2) {
        return (obj1 == null ? obj2 != null : !obj1.equals(obj2));
    }

    /**
     * Safely gets are object equal
     *
     * @param obj1
     * @param obj2
     * @return true if objects are equal
     */
    public static boolean isEqual(Object obj1, Object obj2) {
        return (obj1 == null ? obj2 == null : obj1.equals(obj2));
    }

    /**
     * Find property getter by property name
     *
     * @param object object contains property
     * @param propertyName property name
     * @return getter or null if it was not finded
     */
    public static Method findGetter(Object object, String propertyName) {
        Method method = null;
        if (object != null) {
            Class clazz = object.getClass();
            String classMethodKey = clazz.getName() + "#" + propertyName;
            if (gettersMap == null) {
                gettersMap = new HashMap<String, Method>();
            } else {
                method = gettersMap.get(classMethodKey);
            }
            if (method == null) {
                try {
                    BeanInfo info = Introspector.getBeanInfo(object.getClass(), Object.class);
                    PropertyDescriptor[] props = info.getPropertyDescriptors();
                    for (PropertyDescriptor pd : props) {
                        if (pd.getName().equals(propertyName)) {
                            method = pd.getReadMethod();
                            gettersMap.put(classMethodKey, method);
                            break;
                        }

                    }
                } catch (IntrospectionException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
        return method;
    }

    /**
     * Get object property value by name
     *
     * @param object
     * @param propertyName property name to be returned
     * @return property value
     * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static Object getPropertyValue(Object object, String propertyName)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result = null;
        Method method = CommonUtils.findGetter(object, propertyName);
        if (method != null) {
            result = method.invoke(object, (Object[]) null);
        }
        return result;

    }

    /**
     * Force end string with suffix if it isn't
     *
     * @param str string to be ended
     * @param suffix
     * @return str ended with suffix
     */
    public static String setEndWith(String str, String suffix) {
        if (isNotEmpty(str) && !str.endsWith(suffix)) {
            str = str + suffix;
        }
        return str;
    }

    /**
     * Force start string with prefix if it isn't
     *
     * @param str string to be started
     * @param prefix
     * @return str ended with suffix
     */
    public static String setStartWith(String str, String prefix) {
        if (isNotEmpty(str) && !str.startsWith(prefix)) {
            str = prefix + str;
        }
        return str;
    }

    /**
     * gets property expression for ELProperty
     *
     * @param propertyName property name
     * @return
     */
    public static String getPropertyExpressionByName(String propertyName) {
        return isEmpty(propertyName) ? null : String.format("${%s}", propertyName);
    }

    /**
     * Gets resource name
     *
     * @param parentResource parent resource directory
     * @param name resource name
     * @return
     */
    public static String getResourceName(String parentResource, String name) {
        if (name == null){
            return null;
        }
        if (parentResource == null) {
            parentResource = "";
        }
        return setStartWith(String.format("%s%s", parentResource, 
                setStartWith(name, "/")), "/");
    }
    
    /**
     * Return OS depended script file extension
     * @return 
     */
    public static String getScriptFileExtension(){
        return IS_WIN ? "cmd" : "sh";
    }
}
