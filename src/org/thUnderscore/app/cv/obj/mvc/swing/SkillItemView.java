package org.thUnderscore.app.cv.obj.mvc.swing;

import org.thUnderscore.app.cv.obj.SkillItem;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.common.mvc.swing.BaseSwingObjectView;
import org.thUnderscore.common.mvc.swing.elements.EditsGridElement;

/**
 * View for skill item
 * 
 * @author thUnderscore
 */
public class SkillItemView extends BaseSwingObjectView {

    /*
        Resources constants
    */
    public static final String NAME_CAPTION_RES = "SkillItemView.NAME_CAPTION";
    public static final String NAME_HINT_RES = "SkillItemView.NAME_HINT";

    {
        //define view structure
        addElement(new EditsGridElement(GENERAL_INFO)
            .addTextElementDescription(SkillItem.NAME, 
                    CVUtils.i18n(NAME_CAPTION_RES), 
                    CVUtils.i18n(NAME_HINT_RES), false, 0, 0)
            .setGap(EditsGridElement.TITLE_BORDER_GAP)      
            .setSize(423, 32) 
        );
        //elements are not placed here. Skill items list view do it
    }

}
