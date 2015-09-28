package org.thUnderscore.common.ui.pages.source.ContentManagers;

import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.ui.pages.source.SourceTreeNode;

/**
 * Content manager for directory
 * @author thUnderscore
 */
public class DirectoryContentManager  extends SourceContentManager{

    public static final String TITLE_TEMPLATE_RES = "DirectoryContentManager.TITLE_TEMPLATE";
    
    public static final DirectoryContentManager INSTANCE =  
            new DirectoryContentManager();   
    
    public DirectoryContentManager() {
       super();
    }

    @Override
    public String getText(SourceTreeNode node) {
        return String.format(CommonUtils.i18n(TITLE_TEMPLATE_RES), super.getText(node));
    }
    
    
    
}
