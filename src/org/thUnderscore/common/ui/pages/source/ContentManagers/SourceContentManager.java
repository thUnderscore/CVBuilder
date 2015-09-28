package org.thUnderscore.common.ui.pages.source.ContentManagers;

import org.thUnderscore.common.ui.pages.source.SourceTreeNode;
import syntaxhighlighter.brush.Brush;
import syntaxhighlighter.brush.BrushPlain;

/**
 * Base content manager
 * @author thUnderscore
 */
public abstract class SourceContentManager {

    /**
     * define display in syntax highlighter way
     */
    protected Brush brush;

    public SourceContentManager() {
        brush = new BrushPlain();
    }

    /**
     * Get text for diplaying for node 
     * @param node source tree node
     * @return 
     */
    public String getText(SourceTreeNode node) {
        return node.getName();
    }

    /**
     * Gets brush
     * @return brush
     * @see #brush
     */
    public Brush getBrush() {
        return brush;
    }

}
