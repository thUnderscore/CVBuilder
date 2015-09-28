package org.thUnderscore.app.cv.obj.mvc.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.app.cv.obj.Builder;
import org.thUnderscore.app.cv.obj.BuilderItem;
import org.thUnderscore.app.cv.obj.CV;
import org.thUnderscore.common.mvc.swing.BaseSwingListView;
import static org.thUnderscore.common.mvc.swing.BaseSwingListView.LIST_PANEL;
import org.thUnderscore.common.mvc.swing.elements.ButtonsPanelElement;
import org.thUnderscore.common.utils.UIUtils;
import org.thUnderscore.common.ui.components.ButtonsPanel;
import org.thUnderscore.common.ui.components.FormattedTextField;

/**
 * View for root object of CV builder
 * 
 * @author thUnderscore
 */
public class BuilderView extends BaseSwingListView {

    /*
        Resources constants
    */
    public static final String SELECT_TOOLTIP_RES = "BuilderView.SELECT_TOOL_TIP";
    public static final String REFRESH_TOOLTIP_RES = "BuilderView.REFRESH_TOOL_TIP";
    public static final String EXPORT_TOOLTIP_RES = "BuilderView.EXPORT_TOOL_TIP";
    public static final String SAVE_TOOLTIP_RES = "BuilderView.SAVE_TOOL_TIP";
    public static final String PATH_RES = "BuilderView.PATH";

    public static final String SELECT_ICON_RES = CVUtils.RESOURCES_PATH + "Select";
    public static final String REFRESH_ICON_RES = CVUtils.RESOURCES_PATH + "Refresh";
    public static final String EXPORT_ICON_RES = CVUtils.RESOURCES_PATH + "Export";
    public static final String SAVE_ICON_RES = CVUtils.RESOURCES_PATH + "Save";
    
    public static final String ERROR_ICON_RES = "Error.png";

    public static final String CV_ADD_TOOLTIP_RES = "BuilderView.ADD_TOOL_TIP";
    public static final String CV_DELETE_TOOLTIP_RES = "BuilderView.DELETE_TOOL_TIP";
    public static final String CV_REQUEST_SAVE_CHANGED_MESS_RES = "BuilderView.REQUEST_SAVE_CHANGED_MESS";
    public static final String CV_REQUEST_DELETE_MESS_RES = "BuilderView.REQUEST_DELETE_MESS";

    public static final String TITLE_CAPTION_RES = "BuilderView.TITLE_CAPTION";
    public static final String NULL_NAME_RES = "BuilderView.NULL_NAME";

    /*
        UI constans
    */
    private final int ROW_HEIGHT = 100;
    private final Insets CELL_INSETS = new Insets(4, 4, 4, 4);
    private final int PHOTO_WIDTH = 3 * (ROW_HEIGHT - CELL_INSETS.top - CELL_INSETS.bottom) / 4;
    private final int PHOTO_HEIGHT = ROW_HEIGHT - CELL_INSETS.top - CELL_INSETS.bottom;
    private final int CELL_LINE_HEIGHT = 20;
    private final Dimension PATH_FIELD_SIZE = new Dimension(400, 20);

    /*
        Components names constants
    */
    public static final String TOP_BUTTONS_PANEL = "topButtonsPanel";

    protected ButtonsPanel topButtonsPanel;
    protected FormattedTextField pathField;
    protected JLabel pathLabel;

    {
        //defineview structure
        addElement(new ButtonsPanelElement(TOP_BUTTONS_PANEL));
        addColumnDescription("self", CVUtils.i18n(TITLE_CAPTION_RES), Object.class);

        //change UI strings
        addToolTip = CVUtils.i18n(CV_ADD_TOOLTIP_RES);
        deleteToolTip = CVUtils.i18n(CV_DELETE_TOOLTIP_RES);
        deleteRequestMessage = CVUtils.i18n(CV_REQUEST_DELETE_MESS_RES);
        saveChangedRequestMessage = CVUtils.i18n(CV_REQUEST_SAVE_CHANGED_MESS_RES);
        
        showMoveControl = false;
        
        setListPanelSize(535, 835 - 3);

    }

    public BuilderView(BuilderModel model, BuilderController controller) {
        super(model, controller, BuilderItemView.class);
    }

    /**
     * Gets top buttons panel
     * @return ButtonsPanel instance
     */
    public ButtonsPanel getTopButtonsPanel() {
        return topButtonsPanel;
    }

    
    /**
     * Sets the directory which contain CVs
     * @param directory directory path
     */
    public void setDirectory(String directory) {
        if (pathField == null) {
            return;
        }
        pathField.setText(directory);
    }

    /**
     * Gets the directory which contain CVs
     * @return Current builder directory which contain CVs
     */
    public String getDirectory() {
        return pathField == null ? null : pathField.getText();
    }

    @Override
    protected void createButtons() {
        super.createButtons();
        //create additional buttons panel above the items list
        createTopPanel();

        //add Save and Export buttons
        buttonsPanel.addButton(CVUtils.geti18nBundle(), "", EXPORT_TOOLTIP_RES,
                EXPORT_ICON_RES, BuilderController.EXPORT_COMMAND,
                ButtonsPanel.Alignment.LEADING, -1);
        buttonsPanel.addButton(CVUtils.geti18nBundle(), "", SAVE_TOOLTIP_RES,
                SAVE_ICON_RES, BuilderController.SAVE_COMMAND,
                ButtonsPanel.Alignment.LEADING, -1);

        buttonsPanel.setCommandColor(BuilderController.EXPORT_COMMAND, new Color(1, 144, 83));
        buttonsPanel.setCommandColor(BuilderController.SAVE_COMMAND, new Color(47, 67, 67));        

    }

    @Override
    protected void fillListPanel() {
        super.fillListPanel();
        //place additional buttonspanel above the table
        JPanel listPanel = (JPanel) getComponentByName(LIST_PANEL);
        listPanel.add(getComponentByName(TOP_BUTTONS_PANEL), 0);
        
        
    }

    @Override
    protected void enableButtonsComponents() {
        super.enableButtonsComponents();
        //enable additional buttons according to CV table is empty
        enableButtonsComponent(BuilderController.EXPORT_COMMAND, getCurrent() != null);
        enableButtonsComponent(BuilderController.SAVE_COMMAND, getCurrent() != null);

    }

    @Override
    public void build(JComponent container) {
        
        super.build(container);
        //change CV table look
        DefaultTableCellRenderer renderer = createCellRenderer();
        table.setRowHeight(ROW_HEIGHT);
        table.getColumnModel().getColumn(0).setCellRenderer(
                renderer);
    }

    
    /**
     * Create buttons panel above the CV table
     */
    protected void createTopPanel() {
        topButtonsPanel = (ButtonsPanel) getComponentByName(TOP_BUTTONS_PANEL);

        topButtonsPanel.beginUpdate();
        try {

            //add path label and control
            pathLabel = new JLabel(CVUtils.i18n(PATH_RES));
            topButtonsPanel.addComponent(pathLabel, "", ButtonsPanel.Alignment.LEADING, -1);
            pathField = new FormattedTextField();
            pathField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    notifyController(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    notifyController(e);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    notifyController(e);
                }

                public void notifyController(DocumentEvent e) {
                    changeSupport.firePropertyChange(Builder.DIRECTORY_PROP,
                            null, pathField.getText());
                }
            });
            pathField.setMaximumSize(PATH_FIELD_SIZE);
            pathField.setMinimumSize(PATH_FIELD_SIZE);
            pathField.setPreferredSize(PATH_FIELD_SIZE);
            topButtonsPanel.addComponent(pathField, "", ButtonsPanel.Alignment.LEADING, -1);
            
            //add Select and Refresh buttons
            topButtonsPanel.addButton(CVUtils.geti18nBundle(), "", SELECT_TOOLTIP_RES,
                    SELECT_ICON_RES, BuilderController.SELECT_COMMAND,
                    ButtonsPanel.Alignment.TRAILING, -1);
            topButtonsPanel.addButton(CVUtils.geti18nBundle(), "", REFRESH_TOOLTIP_RES,
                    REFRESH_ICON_RES, BuilderController.REFRESH_COMMAND,
                    ButtonsPanel.Alignment.TRAILING, -1);
            topButtonsPanel.setCommandColor(BuilderController.SELECT_COMMAND, new Color(168, 95, 12));
            topButtonsPanel.setCommandColor(BuilderController.REFRESH_COMMAND, new Color(82, 146, 235));
        } finally {
            topButtonsPanel.endUpdate();
        }
        
        
    }

    /**
     * Create cell renderer from CV table
     * @return cell renderer
     */
    protected DefaultTableCellRenderer createCellRenderer() {
        return new DefaultTableCellRenderer() {
            BuilderItem listItem;
            Font titleFont = getFont();
            Font nameFont = new Font(titleFont.getName(), Font.BOLD, titleFont.getSize() + 4);
            Font commFont = new Font(titleFont.getName(), Font.BOLD, titleFont.getSize());
            Image errorImage;

            @Override
            protected void setValue(Object value) {
                listItem = (BuilderItem) value;
            }

            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(getBackground());

                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setClip(CELL_INSETS.left, CELL_INSETS.top,
                        getWidth(), getHeight());

                g2d.setClip(CELL_INSETS.left, CELL_INSETS.top,
                        getWidth() - CELL_INSETS.right - 4, getHeight() - CELL_INSETS.bottom);

                //draw photo or error sign
                Image image = listItem.isError() ? getErrorImage() : listItem.getCv().getPhoto();
                UIUtils.drawCenteredScaledImage(g2d, image,
                        CELL_INSETS.left, CELL_INSETS.top,
                        PHOTO_WIDTH, PHOTO_HEIGHT);

                int textPartLeft = PHOTO_WIDTH
                        + CELL_INSETS.left + CELL_INSETS.right + 10;
                int textPartWidth = getWidth() - textPartLeft - CELL_INSETS.right;
                int top = CELL_INSETS.top;
                if (listItem.isError()) {
                    //draw error text
                    UIUtils.drawText(g2d, listItem.getErrorDescription(), textPartLeft, 0, textPartWidth,
                            ROW_HEIGHT, nameFont, getForeground(), true, true);
                    
                } else {
                    //draw shot CV info
                    CV cv = listItem.getCv();

                    String text = cv.getName();
                    if (text == null) {
                        text = CVUtils.i18n(NULL_NAME_RES);
                    }
                    
                    UIUtils.drawText(g2d, text, textPartLeft, top, textPartWidth,
                            CELL_LINE_HEIGHT + 10, nameFont, getForeground(), true, true);

                    top += CELL_LINE_HEIGHT + 10;
                    drawTitleAndValue(g2d, cv.getAddress(), CVView.ADDRESS_CAPTION_RES,
                            textPartLeft, top, textPartWidth);
                    top += CELL_LINE_HEIGHT;
                    drawTitleAndValue(g2d, cv.getCommunications().getPhone(), CommunicationsView.PHONE_CAPTION_RES,
                            textPartLeft, top, textPartWidth / 2);
                    drawTitleAndValue(g2d, cv.getCommunications().getEmail(), CommunicationsView.EMAIL_CAPTION_RES,
                            textPartLeft + textPartWidth / 2, top, textPartWidth / 2);
                    top += CELL_LINE_HEIGHT;
                    drawTitleAndValue(g2d, cv.getCommunications().getSkype(), CommunicationsView.SKYPE_CAPTION_RES,
                            textPartLeft, top, textPartWidth / 2);
                    drawTitleAndValue(g2d, cv.getCommunications().getWeb(), CommunicationsView.WEB_CAPTION_RES,
                            textPartLeft + textPartWidth / 2, top, textPartWidth / 2);
                }

            }
            /**
             * Draw title and bold value centered vertically
             * @param g Graphics for drawing
             * @param textValue value
             * @param titleRes title resource name
             * @param left most left point
             * @param top most top point
             * @param width max width
             */
            private void drawTitleAndValue(Graphics g, String textValue, String titleRes,
                    int left, int top, int width) {

                UIUtils.drawText(g, CVUtils.i18n(titleRes), left, top,
                        width, CELL_LINE_HEIGHT, titleFont, getForeground(),
                        false, true);
                if (textValue != null) {
                    UIUtils.drawText(g, textValue, left + 60, top,
                            width - 60, CELL_LINE_HEIGHT, commFont, getForeground(),
                            false, true);
                }
            }

            /**
             * Gets error sign, which is drawed in error case
             * @return Image object which contains error sign
             */
            private Image getErrorImage() {
                return errorImage == null ? 
                        errorImage = CVUtils.getResourceAsImage(ERROR_ICON_RES).getImage() : errorImage;

            }

        };
    }

}
