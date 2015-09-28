package org.thUnderscore.app.cv.obj.mvc.swing;

import org.thUnderscore.app.cv.obj.Education;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.common.mvc.swing.BaseSwingObjectView;
import org.thUnderscore.common.mvc.swing.elements.EditsGridElement;

/**
 * View for education
 * 
 * @author thUnderscore
 */
public class EducationView extends BaseSwingObjectView {

    /*
        Resources constants
    */
    public static final String FROM_CAPTION_RES = "EducationView.FROM_CAPTION";
    public static final String FROM_HINT_RES = "EducationView.FROM_HINT";
    public static final String TO_CAPTION_RES = "EducationView.TO_CAPTION";
    public static final String TO_HINT_RES = "EducationView.TO_HINT";
    public static final String UNIVERSITY_CAPTION_RES = "EducationView.UNIVERSITY_CAPTION";
    public static final String UNIVERSITY_HINT_RES = "EducationView.UNIVERSITY_HINT";
    public static final String DEGREE_CAPTION_RES = "EducationView.DEGREE_CAPTION";
    public static final String DEGREE_HINT_RES = "EducationView.DEGREE_HINT";
    public static final String DEPARTMENT_CAPTION_RES = "EducationView.DEPARTMENT_CAPTION";
    public static final String DEPARTMENT_HINT_RES = "EducationView.DEPARTMENT_HINT";

    {
        //define view structure
        addElement(new EditsGridElement(ROOT)
            .addTextElementDescription(Education.FROM,
                    CVUtils.i18n(FROM_CAPTION_RES),
                    CVUtils.i18n(FROM_HINT_RES), false, 0, 0)
            .addTextElementDescription(Education.TO,
                    CVUtils.i18n(TO_CAPTION_RES),
                    CVUtils.i18n(TO_HINT_RES), false, 1, 0)
            .addTextElementDescription(Education.UNIVERSITY,
                    CVUtils.i18n(UNIVERSITY_CAPTION_RES),
                    CVUtils.i18n(UNIVERSITY_HINT_RES), false, 0, 1)
            .addTextElementDescription(Education.DEPARTMENT,
                    CVUtils.i18n(DEPARTMENT_CAPTION_RES),
                    CVUtils.i18n(DEPARTMENT_HINT_RES), false, 1, 1)
            .addTextElementDescription(Education.DEGREE,
                    CVUtils.i18n(DEGREE_CAPTION_RES),
                    CVUtils.i18n(DEGREE_HINT_RES), false, 0, 2)
            .setGap(EditsGridElement.TITLE_BORDER_GAP)
            .setSize(0, 105)
        );
    }

}
