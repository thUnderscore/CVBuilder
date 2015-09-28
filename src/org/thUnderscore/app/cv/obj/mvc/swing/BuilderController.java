package org.thUnderscore.app.cv.obj.mvc.swing;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.app.cv.obj.Builder;
import org.thUnderscore.app.cv.obj.BuilderItem;
import org.thUnderscore.app.cv.obj.CV;
import org.thUnderscore.app.cv.obj.mvc.word.CVView;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.mvc.BaseListController;
import org.thUnderscore.common.mvc.word.intf.WordObjectView;

/**
 * Controller for root object of CV builder
 * 
 * @author thUnderscore
 */
public class BuilderController extends BaseListController {

    public static final String REFRESH_COMMAND = "Refresh";
    public static final String EXPORT_COMMAND = "Export";
    public static final String SAVE_COMMAND = "Save";
    public static final String SELECT_COMMAND = "Select";
    private static final String SELECT_DIALOG_TITLE_RES = "BuilderController.SELECT_DIALOG_TITLE";
    private static final String WRONG_DIRECTORY_RES = "BuilderController.WRONG_DIRECTORY";
    private static final String REQUEST_REFRESH_RES = "BuilderController.REQUEST_REFRESH";

    /**
     * View for CV exporting to word
     */
    private WordObjectView cvWordView;

    public BuilderController() {
        super();
        dontCheckModified = false;
        silentDelete = false;
    }

    /**
     * Reaction on press Refresh button
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected void doRefresh() throws FileNotFoundException, IOException {
        if (!checkItemModified()) {
            return;
        }
        ((BuilderModel) model).refreshCVList();
        ((BuilderModel) model).saveSettings();
    }

    /**
     * Reaction on press Select button
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected void doSelect() throws FileNotFoundException, IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setIgnoreRepaint(true);
        chooser.setCurrentDirectory(new File(((BuilderModel) model).getDirectory()));
        chooser.setDialogTitle(CVUtils.i18n(SELECT_DIALOG_TITLE_RES));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    JOptionPane.showMessageDialog(null, CVUtils.i18n(WRONG_DIRECTORY_RES),
                            "", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            ((BuilderModel) model).setDirectory(file.getAbsolutePath());
            if (JOptionPane.showConfirmDialog(null,
                    CVUtils.i18n(REQUEST_REFRESH_RES), "",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                doRefresh();
            }

        }
    }

    /**
     * Reaction on press Save button
     * @throws IOException
     */
    protected void doSave() throws IOException {
        BuilderItem current = (BuilderItem) getListModel().getCurrent();
        if (current != null) {
            current.save();
        }
    }

    /**
     * Reaction on press Export button
     */
    protected void doExport() {
        BuilderItem current = (BuilderItem) getListModel().getCurrent();
        CV cv = current == null ? null : current.getCv();
        if (cv == null) {
            return;
        }
        if (!checkItemModified()) {
            return;
        }

        if (cvWordView == null) {
            cvWordView = new CVView();
        }
        cvWordView.getModel().setObject(cv);
        String newFileName = cv.getName();
        String fileName = cvWordView.getFileName();
        if (CommonUtils.isNotEmpty(fileName)) {
            fileName = new File(new File(fileName), newFileName).getName();
        } else {
            fileName = newFileName;
        }
        cvWordView.setFileName(fileName);
        if (cvWordView.selectFile()) {
            cvWordView.saveToFile();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        String actionCommand = e.getActionCommand();
        try {
            if (BuilderController.SELECT_COMMAND.equals(actionCommand)  ){
                doSelect();
            } else if (BuilderController.REFRESH_COMMAND.equals(actionCommand)  ){
                doRefresh();
            } else if (BuilderController.EXPORT_COMMAND.equals(actionCommand)  ){
                doExport();
            } else if (BuilderController.SAVE_COMMAND.equals(actionCommand)  ){
                doSave();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void createPropertiesBindings() {
        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                model,
                ELProperty.create(CommonUtils.getPropertyExpressionByName(Builder.DIRECTORY_PROP)),
                view, BeanProperty.create(Builder.DIRECTORY_PROP),
                Builder.DIRECTORY_PROP);
        propertiesBindingGroup.addBinding(binding);
    }
}
