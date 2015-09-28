package org.thUnderscore.common.ui.pages.source;

import java.util.HashMap;
import java.util.Map;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.ui.pages.source.ContentManagers.DirectoryContentManager;
import org.thUnderscore.common.ui.pages.source.ContentManagers.FileContentManager;
import org.thUnderscore.common.ui.pages.source.ContentManagers.JavaContentManager;
import org.thUnderscore.common.ui.pages.source.ContentManagers.PlainContentManager;
import org.thUnderscore.common.ui.pages.source.ContentManagers.SourceContentManager;
import org.thUnderscore.common.ui.pages.source.ContentManagers.XMLContentManager;
import org.thUnderscore.common.ui.treemodel.TreeNodeIsLeaf;
import syntaxhighlighter.brush.Brush;
import syntaxhighlighter.brush.BrushPlain;

/**
 * Sources tree node
 *
 * @author thUnderscore
 */
public class SourceTreeNode implements TreeNodeIsLeaf {

    private static final Brush NOT_SELECTED_BRUSH = new BrushPlain();
    private static final Brush ERROR_BRUSH = new BrushPlain();
    private static final String NOT_SELECTED_RES = "SourceTreeNode.NOT_SELECTED";

    private static final Map<String, SourceContentManager> MANAGER_MAP
            = new HashMap();

    static {
        MANAGER_MAP.put("java", JavaContentManager.INSTANCE);
        MANAGER_MAP.put("form", XMLContentManager.INSTANCE);
        MANAGER_MAP.put("xml", XMLContentManager.INSTANCE);
        MANAGER_MAP.put("cv", XMLContentManager.INSTANCE);
        MANAGER_MAP.put("mf", PlainContentManager.INSTANCE);
        MANAGER_MAP.put("html", XMLContentManager.INSTANCE);
        MANAGER_MAP.put("jnlp", XMLContentManager.INSTANCE);
        MANAGER_MAP.put("properties", PlainContentManager.INSTANCE);
    }

    /**
     * parent node path in tree or null for sources root
     */
    String parentTreePath;
    /**
     * source path in tree
     */
    String treePath;
    /**
     * source (file or directory) name
     */
    String name;
    /**
     * source resource
     */
    String resource;
    /**
     * is source directory
     */
    boolean isDirectory;
    /**
     * Content manager
     */
    SourceContentManager manager;
    /**
     * Source content
     */
    byte[] content;
    /**
     * Indicate was content read successfully
     */
    boolean isContentReadedSuccessfully;

    public SourceTreeNode(String parentTreePath, String name, String resource,
            boolean isDirectory) {
        this.parentTreePath = CommonUtils.getResourceName(null, parentTreePath);
        this.treePath = CommonUtils.getResourceName(parentTreePath, name);
        this.name = name;
        this.isDirectory = isDirectory;
        this.resource = CommonUtils.setStartWith(resource, "/");
    }

    public SourceTreeNode(SourceTreeNode node, String name, boolean isDirectory) {
        this(node, name, isDirectory,
                CommonUtils.getResourceName((node == null) ? null : node.getResource(), name));
    }

    public SourceTreeNode(SourceTreeNode node, String name,
            boolean isDirectory, String resource) {
        this.parentTreePath = (node == null) ? null : node.getTreePath();
        this.treePath = CommonUtils.getResourceName(this.parentTreePath, name);
        this.name = name;
        this.isDirectory = isDirectory;
        this.resource = CommonUtils.setStartWith(resource, "/");
    }

    /**
     * Gets parentTreePath
     *
     * @return parentTreePath
     * @see #parentTreePath
     */
    public String getParentTreePath() {
        return parentTreePath;
    }

    /**
     * Gets treePath
     *
     * @return treePath
     * @see #treePath
     */
    public String getTreePath() {
        return treePath;
    }

    /**
     * Gets name
     *
     * @return name
     * @see #name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets resource
     *
     * @return resource
     * @see #resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * Gets isDirectory
     *
     * @return isDirectory
     * @see #isDirectory
     */
    public boolean getIsDirectory() {
        return isDirectory;
    }

    /**
     * Gets isContentReadedSuccessfully
     *
     * @return isContentReadedSuccessfully
     * @see #isContentReadedSuccessfully
     */
    public boolean isContentReadedSuccessfully() {
        return isContentReadedSuccessfully;
    }

    /**
     * Gets content
     *
     * @return content
     * @see #content
     */
    public byte[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean IsLeaf() {
        return !this.isDirectory;
    }

    /**
     * Gets ContentManager for source
     *
     * @return ContentManager
     */
    public SourceContentManager getContentManager() {
        if (manager == null) {
            if (isDirectory) {
                manager = DirectoryContentManager.INSTANCE;
            } else {
                String extension = "";
                int i = name.lastIndexOf('.');
                if (i > 0) {
                    extension = name.substring(i + 1);
                }
                manager = MANAGER_MAP.get(extension);
                if (manager == null) {
                    manager = FileContentManager.INSTANCE;
                }
            }
        }
        return manager;
    }

    public void SetIsContentReadedSuccessfully(boolean isContentReadedSuccessfully) {
        this.isContentReadedSuccessfully = isContentReadedSuccessfully;
    }

    /**
     * Sets content
     *
     * @param content
     * @see #content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * Gets source content and display way
     *
     * @param node source node
     * @return content data
     */
    public static SourceContentData getContentData(SourceTreeNode node) {
        SourceContentData result = new SourceContentData();
        result.isDirectory = (node == null) ? false : node.getIsDirectory();
        try {
            if (node == null) {
                result.brush = NOT_SELECTED_BRUSH;
                result.text = CommonUtils.i18n(NOT_SELECTED_RES);
            } else {
                SourceContentManager contentManager = node.getContentManager();
                result.brush = contentManager.getBrush();
                result.text = contentManager.getText(node);
                if (contentManager instanceof FileContentManager) {
                    result.content = ((FileContentManager) contentManager).getContent(node);
                }

            }
        } catch (Exception e) {
            result.brush = ERROR_BRUSH;
            result.text = e.getMessage();
        }
        return result;
    }

}
