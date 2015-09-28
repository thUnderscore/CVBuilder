package org.thUnderscore.app.cv;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.thUnderscore.app.cv.obj.Builder;
import org.thUnderscore.app.cv.obj.BuilderItem;
import org.thUnderscore.app.cv.obj.CV;
import org.thUnderscore.app.cv.obj.mvc.swing.BuilderController;
import org.thUnderscore.app.cv.obj.mvc.swing.BuilderModel;
import org.thUnderscore.app.cv.obj.mvc.swing.BuilderView;
import org.thUnderscore.app.cv.obj.mvc.swing.CVView;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.mvc.BaseListController;
import org.thUnderscore.common.mvc.swing.BaseSwingListView;
import org.thUnderscore.common.mvc.swing.elements.BaseElement;
import org.thUnderscore.common.mvc.swing.elements.SubObjectElement;
import org.thUnderscore.common.utils.UIUtils;
import org.thUnderscore.common.ui.help.HelpHint;
import org.thUnderscore.common.ui.help.HelpTooltipWindow;
import org.thUnderscore.common.ui.pages.MultiPageFrame;
import org.thUnderscore.common.ui.pages.Page;
import org.thUnderscore.common.ui.pages.PageParams;


/**
 * CV builder page for multipage frame
 *
 * @see MultiPageFrame
 *
 * @author thUnderscore
 */
public class CVBuilderPage extends Page {

    /*
     Resources contants
     */
    public static final String HELP_HI_RES = "CVBuilderPage.HELP_HI";
    public static final String HELP_EXPORT_RES = "CVBuilderPage.HELP_EXPORT";
    public static final String HELP_SAVE_RES = "CVBuilderPage.HELP_SAVE";
    public static final String HELP_ADD_RES = "CVBuilderPage.HELP_ADD";
    public static final String HELP_DELETE_RES = "CVBuilderPage.HELP_DELETE";
    public static final String HELP_REFRESH_RES = "CVBuilderPage.HELP_REFRESH";
    public static final String HELP_SELECT_RES = "CVBuilderPage.HELP_SELECT";
    public static final String HELP_MOVE_UP_RES = "CVBuilderPage.HELP_MOVE_UP";
    public static final String HELP_MOVE_DOWN_RES = "CVBuilderPage.HELP_MOVE_DOWN";
    public static final String HELP_CV_LIST_RES = "CVBuilderPage.HELP_CV_LIST";
    public static final String HELP_CV_DATA_RES = "CVBuilderPage.HELP_CV_DATA";
    public static final String HELP_PHOTO_RES = "CVBuilderPage.HELP_PHOTO";
    public static final String HELP_DETAIL_RES = "CVBuilderPage.HELP_DETAIL";
    public static final String HELP_BY_RES = "CVBuilderPage.HELP_BY";
    
    //create mvc elements
    final protected BuilderModel model = new BuilderModel();
    final protected BuilderController controller = new BuilderController();
    final protected BuilderView view = new BuilderView(model, controller);    

    @Override
    protected void init(PageParams params) {
        super.init(params);
        initComponents();

        this.helpHints = new HelpHint[]{
            new HelpHint(CVUtils.i18n(HELP_HI_RES)) ,
            
            new HelpHint(CVUtils.i18n(HELP_CV_LIST_RES)) {
                    @Override
                    public JComponent getComponent() {
                        return view.getComponentByName(BaseSwingListView.LIST_PANEL);
                    }
            },
            
            new HelpHint(CVUtils.i18n(HELP_EXPORT_RES),
                CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
                    BuilderView.EXPORT_ICON_RES))) {

                    @Override
                    public JComponent getComponent() {
                        return view.getButtonsPanel().getComponent(
                                BuilderController.EXPORT_COMMAND);
                    }

            },
            new HelpHint(
                CVUtils.i18n(HELP_SAVE_RES),
                CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
                    BuilderView.SAVE_ICON_RES))) {

                    @Override
                    public JComponent getComponent() {
                        return view.getButtonsPanel().getComponent(
                                BuilderController.SAVE_COMMAND);
                    }

            },

            new HelpHint(
                CVUtils.i18n(HELP_SELECT_RES),
                CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
                    BuilderView.SELECT_ICON_RES))) {

                    @Override
                    public JComponent getComponent() {
                        return view.getTopButtonsPanel().getComponent(
                                BuilderController.SELECT_COMMAND);
                    }

            },
            new HelpHint(
                CVUtils.i18n(HELP_REFRESH_RES),
                CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
                    BuilderView.REFRESH_ICON_RES))) {

                    @Override
                    public JComponent getComponent() {
                        return view.getTopButtonsPanel().getComponent(
                                BuilderController.REFRESH_COMMAND);
                    }

            },
            new HelpHint(
                CVUtils.i18n(HELP_ADD_RES),
                CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
                BaseSwingListView.ADD_ICON_RES))) {

                    @Override
                    public JComponent getComponent() {
                        return view.getButtonsPanel().getComponent(
                                BaseListController.ADD_COMMAND);
                    }

            },            
            new HelpHint(
                CVUtils.i18n(HELP_DELETE_RES),
                CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
                BaseSwingListView.DELETE_ICON_RES))) {

                    @Override
                    public JComponent getComponent() {
                        return view.getButtonsPanel().getComponent(
                                BaseListController.DELETE_COMMAND);
                    }

            },
            new HelpHint(CVUtils.i18n(HELP_CV_DATA_RES)) {

                    @Override
                    public JComponent getComponent() {
                                                                     
                        return getCVView().getComponentByName(CV.COMMUNICATIONS_NODE_NAME);
                    }

            },
            new HelpHint(CVUtils.i18n(HELP_PHOTO_RES)) {

                    @Override
                    public JComponent getComponent() {
                                                                     
                        return getCVView().getComponentByName(CV.PHOTO);
                    }

            },
            new HelpHint(CVUtils.i18n(HELP_DETAIL_RES)) {

                    @Override
                    public JComponent getComponent() {
                                                                     
                        return getCVDetailsTab();
                    }

            },
            
            new HelpHint(
                CVUtils.i18n(HELP_MOVE_UP_RES),
                CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
                BaseSwingListView.MOVE_UP_ICON_RES))) {

                    @Override
                    public JComponent getComponent() {
                        getCVDetailsTab().setSelectedIndex(2);
                        BaseSwingListView skillsView = getCVDetailView(CV.SKILLS_NODE_NAME);                                               
                        return skillsView.getButtonsPanel().getComponent(
                                BaseListController.MOVE_UP_COMMAND);
                    }

            },
            new HelpHint(
                CVUtils.i18n(HELP_MOVE_DOWN_RES),
                CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
                BaseSwingListView.MOVE_DOWN_ICON_RES))) {

                    @Override
                    public JComponent getComponent() {
                        getCVDetailsTab().setSelectedIndex(2);
                        BaseSwingListView skillsView = getCVDetailView(CV.SKILLS_NODE_NAME);                                               
                        return skillsView.getButtonsPanel().getComponent(
                                BaseListController.MOVE_DOWN_COMMAND);
                    }

            },
            new HelpHint(CVUtils.i18n(HELP_BY_RES))
        };

    }

    /**
     * Get embedded CV view
     * @return CV view
     */
    private CVView getCVView(){
        return (CVView)((SubObjectElement)view.getItemView().
                                getElementByName(BuilderItem.CV)).getSubView();
    }
    
    /**
     * Get embedded CV view tabbed pane
     * @return tabbed pane
     */
    private JTabbedPane getCVDetailsTab(){
        return (JTabbedPane)getCVView().getComponentByName(CVView.TABS);
    }
    
    private BaseSwingListView getCVDetailView(String elementName){
        BaseElement elementByName = getCVView().getElementByName(elementName);       
        return (BaseSwingListView)((SubObjectElement)elementByName).getSubView();
    }
            
            
    /**
     * init page content
     */
    private void initComponents() {
        //create Builder object and init it
        Builder builder = new Builder();
        builder.loadSettings();
        builder.refreshCVList();
        //link model and builder
        model.setObject(builder);
        //build view using page as container
        view.build(this);

    }

    @Override
    public void showHelp() {
        HelpTooltipWindow.showTooltipWindow(getLocationOnScreen().x + 267,
                getLocationOnScreen().y + 150, helpHints, helpMinimumSize);
    }

}
