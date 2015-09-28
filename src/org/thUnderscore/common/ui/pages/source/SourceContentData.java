package org.thUnderscore.common.ui.pages.source;

import syntaxhighlighter.brush.Brush;

/**
 * Clast which contains info abour source content and display way
 * @author thUnderscore
 */
public class SourceContentData {
    /**
     * brush for highlighter
     */
   public Brush brush;
   /**
    * text to be displayd
    */
   public String text;
   /**
    * is this source directory
    */
   public boolean isDirectory;
   /**
    * Source contens (for non directory)
    */
   public byte[] content;
}
