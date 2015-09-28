package org.thUnderscore.common.mvc.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.mvc.BaseListController;
import org.thUnderscore.common.mvc.BaseListModel;
import org.thUnderscore.common.mvc.intf.ListController;
import org.thUnderscore.common.mvc.intf.ListModel;
import org.thUnderscore.common.mvc.intf.ListView;
import org.thUnderscore.common.mvc.swing.elements.PanelElement;
import org.thUnderscore.common.mvc.swing.intf.SwingListView;
import org.thUnderscore.common.mvc.swing.intf.SwingObjectView;
import org.thUnderscore.common.utils.UIUtils;
import org.thUnderscore.common.ui.components.ButtonsPanel;
import org.thUnderscore.common.ui.intf.EditorComponent;

/**
 *
 * @author thUnderscore
 */
public class BaseSwingListView extends BaseSwingObjectView implements SwingListView {

    /**
     * Panel where children list will be placed
     */
    public static final String LIST_PANEL = "listPanel";
    /**
     * Panel where current child view will be placed
     */
    public static final String LIST_ITEM_PANEL = "listItemPanel";
    /**
     * Panel where this view object properties components will be placed
     */
    public static final String OWN_ELEMENTS_PANEL = "ownElementsPanel";

    /*
     Icons and string resource names
     */
    public static final String ADD_ICON_RES = CommonUtils.RESOURCES_PATH + "Add";
    public static final String DELETE_ICON_RES = CommonUtils.RESOURCES_PATH + "Delete";
    public static final String MOVE_UP_ICON_RES = CommonUtils.RESOURCES_PATH + "MoveUp";
    public static final String MOVE_DOWN_ICON_RES = CommonUtils.RESOURCES_PATH + "MoveDown";

    public static final String ADD_TOOLTIP_RES = "BaseListController.ADD_TOOL_TIP";
    public static final String DELETE_TOOLTIP_RES = "BaseListController.DELETE_TOOL_TIP";
    public static final String MOVE_UP_TOOLTIP_RES = "BaseListController.MOVE_UP_TOOL_TIP";
    public static final String MOVE_DOWN_TOOLTIP_RES = "BaseListController.MOVE_DOWN_TOOL_TIP";
    public static final String REQUEST_SAVE_CHANGED_MESS_RES = "BaseListController.REQUEST_SAVE_CHANGED_MESS";
    public static final String REQUEST_SAVE_CHANGED_TITLE_RES = "BaseListController.REQUEST_SAVE_CHANGED_TITLE";
    public static final String REQUEST_DELETE_MESS_RES = "BaseListController.REQUEST_DELETE_MESS";
    public static final String REQUEST_DELETE_TITLE_RES = "BaseListController.REQUEST_DELETE_TITLE";

    /*
     Icons, tooltips and messages text properties. Can be overrided in inheritor
     */
    protected String addIconRes = ADD_ICON_RES;
    protected String deleteIconRes = DELETE_ICON_RES;
    protected String moveUpIconRes = MOVE_UP_ICON_RES;
    protected String moveDownIconRes = MOVE_DOWN_ICON_RES;
    protected String addToolTip = CommonUtils.i18n(ADD_TOOLTIP_RES);
    protected String deleteToolTip = CommonUtils.i18n(DELETE_TOOLTIP_RES);
    protected String moveUpToolTip = CommonUtils.i18n(MOVE_UP_TOOLTIP_RES);
    protected String moveDownToolTip = CommonUtils.i18n(MOVE_DOWN_TOOLTIP_RES);
    protected String deleteRequestMessage = CommonUtils.i18n(REQUEST_DELETE_MESS_RES);
    protected String deleteRequestTitle = CommonUtils.i18n(REQUEST_DELETE_TITLE_RES);
    protected String saveChangedRequestMessage = CommonUtils.i18n(REQUEST_SAVE_CHANGED_MESS_RES);
    protected String saveChangedRequestTitle = CommonUtils.i18n(REQUEST_SAVE_CHANGED_TITLE_RES);

    protected JTable table;
    protected JScrollPane scrollPane;
    protected JTableBinding tableBinding;
    protected ButtonsPanel buttonsPanel;

    /**
     * @see #setAxis(int)
     */
    protected int axis;
    /**
     * @see #setListPanelSize(int, int)
     */
    protected Dimension listPanelSize;
    /**
     * Should panel for this view components be created
     */
    protected boolean createOwnElementsPanel = false;
    /**
     * Should panel for child view components be created
     */
    protected boolean createListItemPanel = true;
    /**
     * @see #setShowMoveControl(boolean)
     */
    protected boolean showMoveControl = true;
    /**
     * @see #getColumnDescriptions() o
     */
    protected List<ColumnDescription> columnDescriptions;
    /**
     * Class of view for current child
     */
    protected Class<? extends SwingObjectView> itemViewClass;
    /**
     * View for current child
     */
    protected SwingObjectView itemView;
    /**
     * Color for move up button ripples effect
     */
    protected Color moveColorUp = new Color(166, 213, 81);
    /**
     * Color for move down button ripples effect
     */
    protected Color moveColorDown = moveColorUp;

    /**
     * As model used BaseListModel instance, as controller used
     * BaseListController
     *
     * @param itemViewClass Class of view for current child
     * @param itemClass Class of items
     */
    public BaseSwingListView(Class<? extends SwingObjectView> itemViewClass, Class itemClass) {
        this(new BaseListModel(itemClass), new BaseListController(), itemViewClass);
    }

    /**
     *
     * @param model model of MVC bunch
     * @param controller controller of MVC bunch
     * @param itemViewClass Class of view for current child
     */
    public BaseSwingListView(ListModel model, ListController controller,
            Class<? extends SwingObjectView> itemViewClass) {
        super(model, controller);
        this.rootPosition = BorderLayout.CENTER;
        this.columnDescriptions = new ArrayList<ColumnDescription>();
        this.axis = BoxLayout.LINE_AXIS;
        this.itemViewClass = itemViewClass;
    }

    /**
     * Create component fo displaying children list
     */
    protected void createListComponent() {

        table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrollPane = new JScrollPane(table);
        Color background = table.getBackground();
        scrollPane.setBackground(background);
        scrollPane.getViewport().setBackground(background);
        table.getTableHeader().setBackground(background);

        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                changeSupport.firePropertyChange(BaseListModel.CURRENT_PROP,
                        null, getCurrent());
            }
        });
        table.setSelectionBackground(UIUtils.TABLE_SELECTED_COLOR);
        table.setSelectionForeground(table.getForeground());
    }

    /**
     * Create child view and build it to panel named LIST_ITEM_PANEL
     *
     * @see #LIST_ITEM_PANEL
     */
    protected void createItemComponent() {
        try {
            itemView = itemViewClass.newInstance();
            itemView.build(getComponentByName(LIST_ITEM_PANEL));
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Add component from child view to this view's component
     *
     * @param container this view's component
     * @param componentName child component name
     * @return
     */
    protected boolean addItemViewComponentByNameToContainer(JComponent container, String componentName) {
        return addComponentToContainer(container,
                getItemViewComponentForPlacementByName(componentName));
    }

    /**
     * Add component from child view to this view's component
     *
     * @param containerName child component name
     * @param componentName this view component name
     * @return
     */
    protected boolean addItemViewComponentByNameToContainer(String containerName, String componentName) {
        return addComponentToContainer(containerName,
                getItemViewComponentForPlacementByName(componentName));
    }

    /**
     * Set object for child view's model
     */
    protected void setItemViewObject() {
        Object current = getCurrent();
        if ((itemView != null)) {
            itemView.getModel().setObject(current);
        }
    }

    /**
     * Fill panel which contains tabel. By default it looks like tabel and
     * buttons panel under it
     */
    protected void fillListPanel() {
        JPanel listPanel = (JPanel) getComponentByName(LIST_PANEL);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
        listPanel.add(scrollPane);
        listPanel.add(buttonsPanel);
    }

    /**
     * Enable components on buttons panel. E.g. disable Delete botton if list is
     * empty. Override if add new buttons
     */
    protected void enableButtonsComponents() {
        enableButtonsComponent(BaseListController.ADD_COMMAND, getObject() != null);
        Object current = getCurrent();
        boolean isCurrentNotNull = current != null;
        enableButtonsComponent(BaseListController.DELETE_COMMAND, isCurrentNotNull);

        int currentIndex = isCurrentNotNull ? getListModel().getList().indexOf(current) : -1;
        enableButtonsComponent(BaseListController.MOVE_UP_COMMAND, currentIndex > 0);
        enableButtonsComponent(BaseListController.MOVE_DOWN_COMMAND, currentIndex >= 0
                && (getListModel().getList().size() > (currentIndex + 1)));
    }

    /**
     * Enable component in buttons panel by it's name
     *
     * @param id component name (for buttons it will be action name)
     * @param enabled desired button state
     */
    protected void enableButtonsComponent(String id, boolean enabled) {
        JComponent component = buttonsPanel.getComponent(id);
        if (component != null) {
            component.setEnabled(enabled);
        }
    }

    /**
     * Create bidndings for children list by columns description
     */
    protected void createListBindings() {
        for (ColumnDescription description : getColumnDescriptions()) {
            JTableBinding.ColumnBinding columnBinding;
            columnBinding = tableBinding.addColumnBinding(
                    ELProperty.create(CommonUtils.getPropertyExpressionByName(description.getName())),
                    description.getName());
            columnBinding.setColumnName(description.getTitle());
            columnBinding.setColumnClass(description.getClazz());
        }
    }

    /**
     * Create buttons on buttons panel under the item list. Override if need
     * additional buttons
     *
     * @see #enableButtonsComponents
     */
    protected void createButtons() {
        buttonsPanel.addButton("", addToolTip,
                addIconRes, BaseListController.ADD_COMMAND,
                ButtonsPanel.Alignment.TRAILING, -1);
        buttonsPanel.addButton("", deleteToolTip,
                deleteIconRes, BaseListController.DELETE_COMMAND,
                ButtonsPanel.Alignment.TRAILING, -1);
        buttonsPanel.setCommandColor(BaseListController.ADD_COMMAND, Color.blue);
        buttonsPanel.setCommandColor(BaseListController.DELETE_COMMAND, Color.red);
        if (showMoveControl) {
            buttonsPanel.addButton("", moveUpToolTip,
                    moveUpIconRes, BaseListController.MOVE_UP_COMMAND,
                    ButtonsPanel.Alignment.LEADING, -1);
            buttonsPanel.addButton("", moveDownToolTip,
                    moveDownIconRes, BaseListController.MOVE_DOWN_COMMAND,
                    ButtonsPanel.Alignment.LEADING, -1);
            buttonsPanel.setCommandColor(BaseListController.MOVE_UP_COMMAND, moveColorUp);
            buttonsPanel.setCommandColor(BaseListController.MOVE_DOWN_COMMAND, moveColorUp);
        }

    }

    /**
     * Get component which represent children list
     *
     * @return
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Get description of columns in children list
     *
     * @return
     */
    public List<ColumnDescription> getColumnDescriptions() {
        return columnDescriptions;
    }

    /**
     * Gets buttons panel 
     * @return view buttons panel
     */
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    
    @Override
    public void refresh() {
        super.refresh();
        setItemViewObject();
        if (buttonsPanel != null) {
            enableButtonsComponents();
        }

    }

    @Override
    public SwingObjectView getItemView() {
        return itemView;
    }

    @Override
    public JComponent getItemViewComponentByName(String name) {
        return itemView == null ? null : itemView.getComponentByName(name);
    }

    @Override
    public JComponent getItemViewComponentForPlacementByName(String name) {
        return itemView == null ? null : itemView.getComponentForPlacementByName(name);
    }

    @Override
    public SwingListView setListPanelSize(int width, int height) {
        listPanelSize = new Dimension(width, height);
        return this;
    }

    @Override
    public void build(JComponent container) {
        super.build(container);
        setCurrent(getListModel().getCurrent());
    }

    @Override
    public SwingListView addColumnDescription(String name, String title, Class clazz) {
        columnDescriptions.add(new ColumnDescription(name, title, clazz));
        return this;
    }

    @Override
    public SwingListView setAxis(int axis) {
        this.axis = axis;
        return this;
    }

    @Override
    public void bindList() {
        tableBinding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                getListModel().getList(), getTable());
        createListBindings();
        tableBinding.bind();
    }

    @Override
    public void unbindList() {
        if (tableBinding != null) {
            tableBinding.unbind();
            tableBinding = null;
        }
    }

    @Override
    public boolean checkDeleteCurrent() {
        return JOptionPane.showConfirmDialog(null, deleteRequestMessage,
                deleteRequestTitle, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public int getSaveChangedOnChangeCurrentDecision() {
        return JOptionPane.showConfirmDialog(null,
                saveChangedRequestMessage, saveChangedRequestTitle,
                JOptionPane.YES_NO_CANCEL_OPTION);
    }

    @Override
    public ListModel getListModel() {
        return (ListModel) model;
    }

    @Override
    public ListController getListController() {
        return (ListController) controller;
    }

    /**
     *
     * @return selected table item
     */
    @Override
    public Object getCurrent() {
        int index = table == null ? -1 : table.getSelectedRow();
        if (index < 0) {
            return null;
        }
        return getListModel().getList().get(table.convertRowIndexToModel(index));
    }

    @Override
    public void setCurrent(Object item) {
        int index = getListModel().getList().indexOf(item);
        if ((index > -1) && (getCurrent() != item)) {
            table.getSelectionModel().setSelectionInterval(index, index);
        }
        setItemViewObject();
        enableButtonsComponents();
    }

    @Override
    public boolean isShowMoveControl() {
        return showMoveControl;
    }

    @Override
    public ListView setShowMoveControl(boolean showMoveControls) {
        this.showMoveControl = showMoveControls;
        return this;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        controller.propertyChange(evt);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.actionPerformed(e);

    }

    @Override
    protected void createComponents() {
        PanelElement listPanel = new PanelElement(LIST_PANEL);
        if (listPanelSize != null) {
            listPanel.setSize(listPanelSize);
        }
        addElement(listPanel);
        createListItemPanel = createListItemPanel && (itemViewClass != null);
        if (createListItemPanel) {
            addElement(new PanelElement(LIST_ITEM_PANEL));
        }
        if (createOwnElementsPanel) {
            addElement(new PanelElement(OWN_ELEMENTS_PANEL));
        }
        super.createComponents();
        createListComponent();
        createItemComponent();
        buttonsPanel = new ButtonsPanel();

        //buttonsPanel.addActionListener(controller);
        buttonsPanel.addActionListener(this);

        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        buttonsPanel.beginUpdate();
        try {
            createButtons();
        } finally {
            buttonsPanel.endUpdate();
        }
        fillListPanel();
    }

    @Override
    protected void placeComponents(JComponent container) {
        super.placeComponents(container);
        container.setLayout(new BoxLayout(container, axis));
        addComponentByNameToContainer(container, LIST_PANEL);
        addComponentByNameToContainer(container, OWN_ELEMENTS_PANEL);
        addComponentByNameToContainer(container, LIST_ITEM_PANEL);
    }

    @Override
    public void beforeCheckItemModified() {
        Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();
        if (focusOwner instanceof EditorComponent){
            ((EditorComponent)focusOwner).flush();
        }        
    }

    @Override
    public void afterCheckItemModified(boolean result) {
        
    }
}
