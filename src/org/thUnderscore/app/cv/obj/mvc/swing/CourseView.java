package org.thUnderscore.app.cv.obj.mvc.swing;

import org.thUnderscore.app.cv.obj.Course;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.common.mvc.swing.BaseSwingObjectView;
import org.thUnderscore.common.mvc.swing.elements.EditsGridElement;

/**
 * View for course
 * 
 * @author thUnderscore
 */
public class CourseView extends BaseSwingObjectView {

    /*
        Resources constants
    */
    public static final String WHEN_CAPTION_RES = "CourseView.WHEN_CAPTION";
    public static final String WHEN_HINT_RES = "CourseView.WHEN_HINT";
    public static final String SCHOOL_CAPTION_RES = "CourseView.SCHOOL_CAPTION";
    public static final String SCHOOL_HINT_RES = "CourseView.SCHOOL_HINT";
    public static final String NAME_CAPTION_RES = "CourseView.NAME_CAPTION";
    public static final String NAME_HINT_RES = "CourseView.NAME_HINT";

    {
        //define view structure
        addElement(new EditsGridElement(ROOT)
            .addTextElementDescription(Course.WHEN, 
                    CVUtils.i18n(WHEN_CAPTION_RES),
                    CVUtils.i18n(WHEN_HINT_RES), false, 0, 0)
            .addTextElementDescription(Course.SCHOOL, 
                    CVUtils.i18n(SCHOOL_CAPTION_RES), 
                    CVUtils.i18n(SCHOOL_HINT_RES), false, 0, 1)
            .addTextElementDescription(Course.NAME, 
                    CVUtils.i18n(NAME_CAPTION_RES), 
                    CVUtils.i18n(NAME_HINT_RES), false, 1, 1)
            .setGap(EditsGridElement.TITLE_BORDER_GAP)
            .setSize(0, 70)           
        );
    }

}
