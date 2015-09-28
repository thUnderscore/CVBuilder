package org.thUnderscore.common.ui.pages.source.ContentManagers;

import syntaxhighlighter.brush.BrushJava;

/**
 * Content manager for java file
 * @author thUnderscore
 */
public class JavaContentManager extends TextFileContentManager{

    public static final JavaContentManager INSTANCE =  
            new JavaContentManager();
    
    public JavaContentManager() {
        brush = new BrushJava();
    }
    
}
