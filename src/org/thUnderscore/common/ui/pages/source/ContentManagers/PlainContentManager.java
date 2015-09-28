package org.thUnderscore.common.ui.pages.source.ContentManagers;

import syntaxhighlighter.brush.BrushPlain;

/**
 * Content manager for plain text file
 * @author thUnderscore
 */
public class PlainContentManager extends TextFileContentManager{
    public static final PlainContentManager INSTANCE =  
            new PlainContentManager();
    
    public PlainContentManager() {
        brush = new BrushPlain();
    }
    
}
