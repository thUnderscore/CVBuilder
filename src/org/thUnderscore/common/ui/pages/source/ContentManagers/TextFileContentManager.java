package org.thUnderscore.common.ui.pages.source.ContentManagers;

import java.io.UnsupportedEncodingException;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.ui.pages.source.SourceTreeNode;

/**
 * Base content manager for text file
 * @author thUnderscore
 */
abstract public class TextFileContentManager extends FileContentManager {    

    @Override
    public String getText(SourceTreeNode node) {
        readContent(node);
        String text;
        if (node.isContentReadedSuccessfully()) {
            try {
                text = new String(node.getContent(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                text = ex.getMessage();
            }
        } else {
            text = CommonUtils.i18n(ERROR_CONTENT_READING_RES);
        }
        return text;
    }

}
