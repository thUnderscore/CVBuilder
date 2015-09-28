package org.thUnderscore.app.cv.obj.mvc.word;

import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.app.cv.obj.CV;
import org.thUnderscore.app.cv.obj.Communications;
import org.thUnderscore.app.cv.obj.Course;
import org.thUnderscore.app.cv.obj.Education;
import org.thUnderscore.app.cv.obj.Employment;
import org.thUnderscore.app.cv.obj.Skill;
import org.thUnderscore.app.cv.obj.SkillItem;
import org.thUnderscore.app.cv.obj.mvc.swing.CommunicationsView;
import org.thUnderscore.app.cv.obj.mvc.swing.EmploymentView;
import org.thUnderscore.common.XMLObjectList;
import org.thUnderscore.common.mvc.word.BaseWordObjectView;
import org.thUnderscore.common.mvc.word.elements.BaseElement;
import org.thUnderscore.common.mvc.word.elements.Cell;
import org.thUnderscore.common.mvc.word.elements.Document;
import org.thUnderscore.common.mvc.word.elements.Image;
import org.thUnderscore.common.mvc.word.elements.ListInListTable;
import org.thUnderscore.common.mvc.word.elements.ObjectListColumn;
import org.thUnderscore.common.mvc.word.elements.ObjectListTable;
import org.thUnderscore.common.mvc.word.elements.Paragraph;
import org.thUnderscore.common.mvc.word.elements.ParagraphContainerRepeater;
import org.thUnderscore.common.mvc.word.elements.ParagraphContentGroup;
import org.thUnderscore.common.mvc.word.elements.Run;
import org.thUnderscore.common.mvc.word.elements.SimpleTable;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Word view for CV. Used for export CV data to word document
 * 
 * @author thUnderscore
 */
public class CVView extends BaseWordObjectView {

    
    /*
        Define font styles
    */
    BaseElement.FontStyle addressFont = createFontStyle()
        .setFontFamily("Vernada")
        .setFontSize(14);
    BaseElement.FontStyle nameFont = createFontStyle(addressFont)
        .setFontSize(16)
        .setBold(true);    
    BaseElement.FontStyle commonFont = createFontStyle()
        .setFontFamily("Times New Roman")
        .setFontSize(11)
        .setBold(false);
    BaseElement.FontStyle commonBoldFont = createFontStyle(commonFont)
        .setBold(true);
    BaseElement.FontStyle headerFont = createFontStyle()
        .setFontFamily("Times New Roman")
        .setFontSize(14)
        .setBold(true)
        .setUnderline(UnderlinePatterns.SINGLE);
    
    {
        //set resourcebundle
        resourceBundle = CVUtils.geti18nBundle();
        
        //define word view structure
        document = new Document()
             //add table with photo and general info
            .addTable(
                new SimpleTable(1, 2)
                     //place photo to first cell
                    .addCell(0, 0, new Cell()
                        .addParagraph(new Paragraph()
                            .addImage(new Image(CV.PHOTO, 120, 160))
                            .setVAlign(TextAlignment.CENTER)
                        )
                        .setVAlign(STVerticalJc.CENTER)
                    )
                    //place general info to second cell    
                    .addCell(0, 1, new Cell()
                         //add name and adresss
                        .addRun(new Run(CV.NAME, true).setFontStyle(nameFont))
                        .addRun(new Run(CV.ADDRESS, true).setFontStyle(addressFont))
                         //add communications data
                        .addGroup(new ParagraphContentGroup(CV.COMMUNICATIONS_NODE_NAME)
                            .addGroup(createTitleValueGroup(Communications.PHONE, CommunicationsView.PHONE_CAPTION_RES))
                            .addGroup(createTitleValueGroup(Communications.EMAIL, CommunicationsView.EMAIL_CAPTION_RES))
                            .addGroup(createTitleValueGroup(Communications.SKYPE, CommunicationsView.SKYPE_CAPTION_RES))
                            .addGroup(createTitleValueGroup(Communications.WEB, CommunicationsView.WEB_CAPTION_RES))               
                        )
                    )
                    .setRowHeight(720)
                    .setBorderVisible(false)
             )
             //add summary
             .addParagraph(new Paragraph()
                .addRun(new Run(true, CVUtils.i18n(
                        org.thUnderscore.app.cv.obj.mvc.swing.CVView.SUMMARY_CAPTION_RES))
                    .setFontStyle(headerFont)
                )
                .addRun(new Run(CV.SUMMARY, false)
                    .setItalic(true)
                )
             )
             //add educations table
             .addTable(                    
                new ObjectListTable(CV.EDUCATIONS_NODE_NAME, XMLObjectList.LIST, 4, false, false)
                    .addColumn(new ObjectListColumn()
                        .setBodyFontStyle(headerFont)
                        .setBodyValueExpression(String.format("%s-%s", 
                            CommonUtils.getPropertyExpressionByName(Education.FROM),
                            CommonUtils.getPropertyExpressionByName(Education.TO))
                        )
                    )
                    .addColumn(new ObjectListColumn(Education.UNIVERSITY))
                    .addColumn(new ObjectListColumn(Education.DEGREE))
                    .addColumn(new ObjectListColumn(Education.DEPARTMENT))                        
                    .setBorderVisible(false)
                    .setColWidth(new int[]{1660, 5850, 1660, 1660})
                    .setTitle(CVUtils.i18n(
                            org.thUnderscore.app.cv.obj.mvc.swing.CVView.EDUCATIONS_TAB_CAPTION_RES),
                            headerFont)                 
             )
             //add cources table
             .addTable(                     
                new ObjectListTable(CV.COURSES_NODE_NAME, XMLObjectList.LIST, 3, false, false)
                    .addColumn(new ObjectListColumn(Course.WHEN)
                        .setBodyFontStyle(headerFont)                     
                    )
                    .addColumn(new ObjectListColumn(Course.SCHOOL))
                    .addColumn(new ObjectListColumn(Course.NAME))
                    .setBorderVisible(false)
                    .setColWidth(new int[]{1660, 5850, 3320})
                    .setTitle(CVUtils.i18n(
                            org.thUnderscore.app.cv.obj.mvc.swing.CVView.COURSES_TAB_CAPTION_RES),
                            headerFont)
                    
             )
             //add skills table. Each column contains skill data. Each row in column 
             //contains skill item data
             .addTable(
                new ListInListTable(CV.SKILLS_NODE_NAME, XMLObjectList.LIST,  XMLObjectList.LIST, false, 
                    Skill.NAME, SkillItem.NAME, null)
                    .setHeaderFontStyle(commonBoldFont)
                    .setColWidth(10000)
                    .setTitle(CVUtils.i18n(
                            org.thUnderscore.app.cv.obj.mvc.swing.CVView.SKILLS_TAB_CAPTION_RES),
                            headerFont)
                    .setAddParagraphAfterTable(true)
             )
             //add repeater from employment data
             .addRepeater(new ParagraphContainerRepeater(CV.EMPLOYMENTS_NODE_NAME, XMLObjectList.LIST)
                     .setTitle(CVUtils.i18n(
                             org.thUnderscore.app.cv.obj.mvc.swing.CVView.EMPLOYMENT_TAB_CAPTION_RES),
                            headerFont)
                     .addRun(new Run(true)
                             .setValueExpression(String.format("%s        %s-%s", 
                                CommonUtils.getPropertyExpressionByName(Employment.COMPANY),
                                CommonUtils.getPropertyExpressionByName(Employment.FROM),
                                CommonUtils.getPropertyExpressionByName(Employment.TO))
                             )
                             .setFontStyle(commonBoldFont)
                             .setUnderline(UnderlinePatterns.SINGLE)
                             
                     )
                     .addRun(new Run(Employment.COMPANY_DESCR, true))
                     .addGroup(createTitleValueGroup(Employment.ROLE, EmploymentView.ROLE_CAPTION_RES))
                     .addRun(new Run(Employment.ROLE_DESCR, true))                     
                     .addGroup(createTitleValueGroup(Employment.TECHNOLOGIES, EmploymentView.TECHNOLOGIES_CAPTION_RES))                     
                     .addGroup(createTitleValueGroup(Employment.ACHIEVMENTS, EmploymentView.ACHIEVMENTS_CAPTION_RES, false))
                     
             )
             .setFontStyle(commonFont);
    }    

}
