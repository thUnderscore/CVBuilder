package org.thUnderscore.common.ui.pages.source.ContentManagers;

import org.thUnderscore.common.ui.pages.source.SourceTreeNode;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Base content manager for files
 * @author thUnderscore
 */
public class FileContentManager extends SourceContentManager {

    public static final String ERROR_CONTENT_READING_RES = "FileContentManagerr.NOT_SELECTED";    
    public static final FileContentManager INSTANCE
            = new FileContentManager();

    /**
     * Gets file content from resouces
     * @param node soorce tree node
     * @return file content as byte[]
     */
    public byte[] getContent(SourceTreeNode node) {
        readContent(node);
        return node.getContent();
    }

    /**
     * read file content from resouces if it was nor readed yet and store in node
     * @param node soorce tree node
     */
    protected void readContent(SourceTreeNode node) {        
        if (node.getContent() != null) {
            return;
        }
        try {
             node.setContent(CommonUtils.readResource(node.getResource()));
             node.SetIsContentReadedSuccessfully(true);
             
        } catch (IOException ex) {
            node.setContent(new byte[0]);
            node.SetIsContentReadedSuccessfully(false);
            Logger.getLogger(TextFileContentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
