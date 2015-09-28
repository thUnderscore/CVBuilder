package org.thUnderscore.common.mvc.word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.mvc.BaseObjectView;
import org.thUnderscore.common.mvc.word.elements.BaseElement;
import org.thUnderscore.common.mvc.word.elements.Document;
import org.thUnderscore.common.mvc.word.elements.ParagraphContentGroup;
import org.thUnderscore.common.mvc.word.elements.Run;
import org.thUnderscore.common.mvc.word.intf.WordObjectView;

/**
 * Base view, which can display object to MS Word document
 *
 * @author thUnderscore
 */
public class BaseWordObjectView extends BaseObjectView implements WordObjectView {

    /*
     Messages keys in i18n bundle
     */
    private static final String SELECT_DIALOG_TITLE_RES = "BaseWordObjectView.SELECT_DIALOG_TITLE";
    private static final String FILE_FILTER_DESCRIPTION_RES = "BaseWordObjectView.FILE_FILTER_DESCRIPTION";
    private static final String OVERWRITE_REQUEST_MESSAGE_RES = "BaseWordObjectView.OVERWRITE_REQUEST_MESSAGE";
    private static final String OVERWRITE_REQUEST_TITLE_RES = "BaseWordObjectView.OVERWRITE_REQUEST_TITLE";

    /**
     * @see #getFileName()
     */
    protected String fileName;
    /**
     * Check if file already exists and ask for next steps
     */
    protected boolean checkFileExist = true;
    /**
     * Definition of MS Word document structure
     */
    protected Document document;
    /**
     * Bundle which contains i18n strings
     */
    protected ResourceBundle resourceBundle;

    /**
     * Display document structure to XWPFDocument
     *
     * @param wordDoc
     */
    protected void fillDocument(XWPFDocument wordDoc) {
        if (document != null) {
            document.setObject(getObject());
            document.writeToWordDoc(wordDoc);
        }
    }

    /**
     * Create XWPFDocument instance
     *
     * @return
     */
    protected XWPFDocument createDocument() {
        return new XWPFDocument();
    }

    /**
     * Create FontStyle instance
     *
     * @return created FintStyle
     */
    public static BaseElement.FontStyle createFontStyle() {
        return POIUtils.createFontStyle();
    }

    /**
     * Create FontStyle instance and copy properties
     *
     * @param fontStyle source for properies copying
     * @return created FontStyle instance
     */
    public static BaseElement.FontStyle createFontStyle(BaseElement.FontStyle fontStyle) {
        return POIUtils.createFontStyle(fontStyle);
    }

    /**
     * gets property expression for ELProperty
     *
     * @param propertyName property name
     * @return
     */
    public static String getPropertyExpressionByName(String propertyName) {
        return CommonUtils.getPropertyExpressionByName(propertyName);
    }

    /**
     * Create runs group like "title: value" and add break after last run
     *
     * @param propertyName property of object
     * @param titleRes key in resourceBundle
     * @return created ParagraphContentGroup
     * @see resourceBundle
     */
    protected ParagraphContentGroup createTitleValueGroup(String propertyName, String titleRes) {
        return createTitleValueGroup(propertyName, titleRes, true);
    }

    /**
     * Create runs group like "title: value"
     *
     * @param propertyName property of object
     * @param titleRes key in resourceBundle
     * @param addBreakAfter should break after last run be added
     * @return created ParagraphContentGroup
     * @see resourceBundle
     */
    protected ParagraphContentGroup createTitleValueGroup(String propertyName,
            String titleRes, boolean addBreakAfter) {
        return new ParagraphContentGroup()
                .addGroup(new ParagraphContentGroup()
                        .addRun(new Run(false, safeGetI18nString(titleRes)))
                        .addRun(new Run(false, ":  "))
                        .setBold(true)
                )
                .addRun(new Run(propertyName, addBreakAfter));
    }

    /**
     * Safely get string from resourceBundle
     *
     * @param key string key
     * @return i18n string
     * @see #resourceBundle
     */
    protected String safeGetI18nString(String key) {
        return resourceBundle == null ? "" : resourceBundle.getString(key);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void saveToFile() {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            try {
                XWPFDocument wordDoc = createDocument();
                fillDocument(wordDoc);
                wordDoc.write(out);
            } finally {
                out.close();
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public boolean selectFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setIgnoreRepaint(true);
        chooser.setDialogTitle(CommonUtils.i18n(SELECT_DIALOG_TITLE_RES));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter(
                CommonUtils.i18n(FILE_FILTER_DESCRIPTION_RES), "docx"));
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        if (CommonUtils.isNotEmpty(fileName)) {
            chooser.setSelectedFile(new File(fileName));
        }
        boolean result = chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION;
        if (result) {
            fileName = CommonUtils.setEndWith(
                    chooser.getSelectedFile().getAbsolutePath(), ".docx");
            if (checkFileExist && new File(fileName).exists()) {
                result = (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
                        CommonUtils.i18n(OVERWRITE_REQUEST_MESSAGE_RES),
                        CommonUtils.i18n(OVERWRITE_REQUEST_TITLE_RES), JOptionPane.YES_NO_OPTION));
            }
        }
        return result;
    }
}
