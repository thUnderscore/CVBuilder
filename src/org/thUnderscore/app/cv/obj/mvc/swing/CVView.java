package org.thUnderscore.app.cv.obj.mvc.swing;

import javax.swing.BoxLayout;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.app.cv.obj.CV;
import org.thUnderscore.app.cv.obj.Course;
import org.thUnderscore.app.cv.obj.Education;
import org.thUnderscore.common.mvc.swing.BaseSwingListView;
import org.thUnderscore.common.mvc.swing.BaseSwingObjectView;
import org.thUnderscore.common.mvc.swing.elements.EditsGridElement;
import org.thUnderscore.common.mvc.swing.elements.ImagePanelElement;
import org.thUnderscore.common.mvc.swing.elements.SequentialPanelElement;
import org.thUnderscore.common.mvc.swing.elements.SubObjectElement;
import org.thUnderscore.common.mvc.swing.elements.TabbedPaneElement;
import org.thUnderscore.common.mvc.swing.elements.TextAreaElement;

/**
 * View for CV
 * 
 * @author thUnderscore
 */
public class CVView extends BaseSwingObjectView {

    /*
        Components names constants
    */
    public static final String TABS = "tabs";

    /*
        Resources constants
    */
    public static final String EDUCATIONS_TAB_CAPTION_RES = "CVView.EDUCATIONS_TAB_CAPTION";
    public static final String COURSES_TAB_CAPTION_RES = "CVView.COURSES_TAB_CAPTION";
    public static final String SKILLS_TAB_CAPTION_RES = "CVView.SKILLS_TAB_CAPTION";
    public static final String EMPLOYMENT_TAB_CAPTION_RES = "CVView.EMPLOYMENT_TAB_CAPTION";

    public static final String NAME_CAPTION_RES = "CVView.NAME_CAPTION";
    public static final String NAME_HINT_RES = "CVView.NAME_HINT";
    public static final String ADDRESS_CAPTION_RES = "CVView.ADDRESS_CAPTION";
    public static final String ADDRESS_HINT_RES = "CVView.ADDRESS_HINT";
    public static final String PHOTO_CAPTION_RES = "CVView.PHOTO_CAPTION";
    public static final String SUMMARY_CAPTION_RES = "CVView.SUMMARY_CAPTION";
    public static final String SUMMARY_HINT_RES = "CVView.SUMMARY_HINT";

    {
        //define view structure
        
        //add general info block
        addElement(
            new EditsGridElement(GENERAL_INFO)
                .addTextElementDescription(CV.NAME, CVUtils.i18n(NAME_CAPTION_RES),
                        CVUtils.i18n(NAME_HINT_RES), true, 0, 0)
                .addTextElementDescription(CV.ADDRESS, CVUtils.i18n(ADDRESS_CAPTION_RES),
                        CVUtils.i18n(ADDRESS_HINT_RES), false, 0, 1)
                .setColLabelWidth(0, CommunicationsView.LABEL_WIDTH)
                .setGapTop()
                .setGapBottom()
                .setGapForBorderedAlign()
        );
        //add communications subview
        addElement(
            new SubObjectElement(CV.COMMUNICATIONS_NODE_NAME, CommunicationsView.class)
                .setMaximumSize(Short.MAX_VALUE, 90)
        );
        //add educations subview
        addElement(
            new SubObjectElement(CV.EDUCATIONS_NODE_NAME,
                new BaseSwingListView(EducationView.class, Education.class)
                   //define education columns
                   .addColumnDescription(Education.FROM,
                           CVUtils.i18n(EducationView.FROM_CAPTION_RES), String.class)
                   .addColumnDescription(Education.TO,
                           CVUtils.i18n(EducationView.TO_CAPTION_RES), String.class)
                   .addColumnDescription(Education.UNIVERSITY,
                           CVUtils.i18n(EducationView.UNIVERSITY_CAPTION_RES), String.class)
                    .addColumnDescription(Education.DEPARTMENT,
                           CVUtils.i18n(EducationView.DEPARTMENT_CAPTION_RES), String.class)
                   .addColumnDescription(Education.DEGREE,
                           CVUtils.i18n(EducationView.DEGREE_CAPTION_RES), String.class)
                        
                   .setAxis(BoxLayout.PAGE_AXIS)
            )
        );
        //add cources subview
        addElement(
            new SubObjectElement(CV.COURSES_NODE_NAME,
                new BaseSwingListView(CourseView.class, Course.class)
                    //define cource columns
                    .addColumnDescription(Course.WHEN,
                            CVUtils.i18n(CourseView.WHEN_CAPTION_RES), String.class)
                    .addColumnDescription(Course.SCHOOL,
                            CVUtils.i18n(CourseView.SCHOOL_CAPTION_RES), String.class)
                    .addColumnDescription(Course.NAME,
                            CVUtils.i18n(CourseView.NAME_CAPTION_RES), String.class)
                        
                    .setAxis(BoxLayout.PAGE_AXIS)
            )
        );
        //add employments subview
        addElement(new SubObjectElement(CV.EMPLOYMENTS_NODE_NAME, EmploymentsView.class));
        //add skills subview
        addElement(new SubObjectElement(CV.SKILLS_NODE_NAME, SkillsView.class));
        //add tabbed element
        addElement(new TabbedPaneElement(TABS)
                //set subviews positions on tabs
                .addTabDescriptionByViewElement(CV.EDUCATIONS_NODE_NAME,
                        CVUtils.i18n(EDUCATIONS_TAB_CAPTION_RES))
                .addTabDescriptionByViewElement(CV.COURSES_NODE_NAME,
                        CVUtils.i18n(COURSES_TAB_CAPTION_RES))
                .addTabDescriptionByViewElement(CV.SKILLS_NODE_NAME,
                        CVUtils.i18n(SKILLS_TAB_CAPTION_RES))
                .addTabDescriptionByViewElement(CV.EMPLOYMENTS_NODE_NAME,
                        CVUtils.i18n(EMPLOYMENT_TAB_CAPTION_RES))
                .setSize(900, 500 - 1)

        );

        //add image panel for photo
        addElement(new ImagePanelElement(CV.PHOTO)
                .setBorder(true, CVUtils.i18n(PHOTO_CAPTION_RES))
                .setWrapScrollPane(true)
                .setSize(180, 240)
        );
        //add summary memo
        addElement(new TextAreaElement(CV.SUMMARY, CVUtils.i18n(SUMMARY_HINT_RES), true)
                .setBorder(true, CVUtils.i18n(SUMMARY_CAPTION_RES))
                .setSize(675 - 10, 160 - 10)
        );
        //define elements hierarchy started from root component
        addElement(new SequentialPanelElement(ROOT, BoxLayout.PAGE_AXIS)
                .addSubsequential(new SequentialPanelElement(BoxLayout.LINE_AXIS)
                        .addElement(CV.PHOTO)
                        .addSubsequential(new SequentialPanelElement(BoxLayout.PAGE_AXIS)
                                .addElement(GENERAL_INFO)
                                .addElement(CV.COMMUNICATIONS_NODE_NAME)
                                .addElement(CV.SUMMARY)
                                .setSize(675 - 10, 330)
                        )
                        .setSize(855, 330)
                )
                .addElement(TABS)
                .setSize(855, 715)
        );
    }
}
