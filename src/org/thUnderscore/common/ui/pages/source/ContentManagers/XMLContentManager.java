package org.thUnderscore.common.ui.pages.source.ContentManagers;

import syntaxhighlighter.brush.BrushXml;

/**
 * Content manager for xml file
 * @author thUnderscore
 */
public class XMLContentManager extends TextFileContentManager{
    public static final XMLContentManager INSTANCE =  
            new XMLContentManager();
    
    public XMLContentManager() {
        brush = new BrushXml();
    }
    
}
