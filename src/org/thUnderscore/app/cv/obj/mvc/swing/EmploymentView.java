package org.thUnderscore.app.cv.obj.mvc.swing;

import org.thUnderscore.app.cv.obj.Employment;
import javax.swing.BoxLayout;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.mvc.swing.BaseSwingObjectView;
import org.thUnderscore.common.mvc.swing.elements.EditsGridElement;
import org.thUnderscore.common.mvc.swing.elements.SequentialPanelElement;
import org.thUnderscore.common.mvc.swing.elements.TabbedPaneElement;
import org.thUnderscore.common.mvc.swing.elements.TextAreaElement;

/**
 * View for employment
 * 
 * @author thUnderscore
 */
public class EmploymentView extends BaseSwingObjectView {

    /*
        Resources constants
    */
    public static final String FROM_CAPTION_RES = "EmploymentView.FROM_CAPTION";
    public static final String FROM_HINT_RES = "EmploymentView.FROM_HINT";
    public static final String TO_CAPTION_RES = "EmploymentView.TO_CAPTION";
    public static final String TO_HINT_RES = "EmploymentView.TO_HINT";
    public static final String COMPANY_DESCR_CAPTION_RES = "EmploymentView.COMPANY_DESCR_CAPTION";
    public static final String COMPANY_DESCR_HINT_RES = "EmploymentView.COMPANY_DESCR_HINT";
    public static final String COMPANY_CAPTION_RES = "EmploymentView.COMPANY_CAPTION";
    public static final String COMPANY_HINT_RES = "EmploymentView.COMPANY_HINT";
    public static final String ROLE_DESCR_CAPTION_RES = "EmploymentView.ROLE_DESCR_CAPTION";
    public static final String ROLE_DESCR_HINT_RES = "EmploymentView.ROLE_DESCR_HINT";
    public static final String ROLE_CAPTION_RES = "EmploymentView.ROLE_CAPTION";
    public static final String ROLE_HINT_RES = "EmploymentView.ROLE_HINT";
    public static final String TECHNOLOGIES_CAPTION_RES = "EmploymentView.TECHNOLOGIES_CAPTION";
    public static final String TECHNOLOGIES_HINT_RES = "EmploymentView.TECHNOLOGIES_HINT";
    public static final String ACHIEVMENTS_CAPTION_RES = "EmploymentView.ACHIEVMENTS_CAPTION";
    public static final String ACHIEVMENTS_HINT_RES = "EmploymentView.ACHIEVMENTS_HINT";
    
    /*
        Components names constants
    */
    public static final String PERIOD_PANEL = "periodPanel";
    public static final String ROLE_PANEL = "rolePanel";
    /*
        UI constans
    */
    public static final int LABEL_WIDTH = 50;
    
   

    {
        //define view structure
        //add period elements group
        addElement(
            new EditsGridElement(PERIOD_PANEL)
                .addTextElementDescription(Employment.FROM,
                        CVUtils.i18n(FROM_CAPTION_RES), 
                        CVUtils.i18n(FROM_HINT_RES), false, 0, 0)
                .addTextElementDescription(Employment.TO,
                        CVUtils.i18n(TO_CAPTION_RES), 
                        CVUtils.i18n(TO_HINT_RES), false, 1, 0)
                .setColLabelWidth(LABEL_WIDTH)
                .setGap()
        )
        //add role and company controls group
        .addElement(
            new EditsGridElement(ROLE_PANEL)
                .addTextElementDescription(Employment.COMPANY,
                        CVUtils.i18n(COMPANY_CAPTION_RES), 
                        CVUtils.i18n(COMPANY_HINT_RES), false, 0, 0)
                .addTextElementDescription(Employment.ROLE,
                        CVUtils.i18n(ROLE_CAPTION_RES), 
                        CVUtils.i18n(ROLE_HINT_RES), false, 0, 1) 
                .setColLabelWidth(LABEL_WIDTH)
                .setGapLeft()
                .setGapRight()
        )
        //add achivments memo
        .addElement(
            new TextAreaElement(Employment.ACHIEVMENTS, CVUtils.i18n(ACHIEVMENTS_HINT_RES), false)
                .setBorder(true, CVUtils.i18n(ACHIEVMENTS_CAPTION_RES))
                .setSize(375, 205)
        )
        //add technologies memo
        .addElement(
            new TextAreaElement(Employment.TECHNOLOGIES, CVUtils.i18n(TECHNOLOGIES_HINT_RES), false)
                .setBorder(true, CVUtils.i18n(TECHNOLOGIES_CAPTION_RES))
                .setSize(375, 205)
        )
        //add role description memo
        .addElement(
            new TextAreaElement(Employment.ROLE_DESCR, CVUtils.i18n(ROLE_DESCR_HINT_RES), false)
                .setBorder(true, CVUtils.i18n(ROLE_DESCR_CAPTION_RES))
                .setSize(375, 152)
        )
        //add company description memo
        .addElement(
            new TextAreaElement(Employment.COMPANY_DESCR, CVUtils.i18n(COMPANY_DESCR_HINT_RES), false)
                .setBorder(true, CVUtils.i18n(COMPANY_DESCR_CAPTION_RES))
                .setSize(375, 152)
        )
        //group first tab controls
        .addElement(
            new SequentialPanelElement(GENERAL_INFO, BoxLayout.PAGE_AXIS)
                .addElement(PERIOD_PANEL)
                .addElement(ROLE_PANEL)
                .addElement(Employment.COMPANY_DESCR)
                .addElement(Employment.ROLE_DESCR)

        )
        //group second tab controls
        .addElement(
            new SequentialPanelElement(ADDITIONAL_INFO, BoxLayout.PAGE_AXIS)
                .addElement(Employment.TECHNOLOGIES)
                .addElement(Employment.ACHIEVMENTS)
        )

        //define elements hierarchy started from root component
        //as result view look like tabbed pane with two tabs
        .addElement(
            new TabbedPaneElement(ROOT)
                .addTabDescriptionByViewElement(GENERAL_INFO,
                        CommonUtils.i18n(GENERAL_INFO_CAPTION_RES))
                .addTabDescriptionByViewElement(ADDITIONAL_INFO,
                        CommonUtils.i18n(ADDITIONAL_INFO_CAPTION_RES))
        )
        ;
    }
}
