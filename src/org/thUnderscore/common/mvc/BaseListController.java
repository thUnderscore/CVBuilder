package org.thUnderscore.common.mvc;

import org.thUnderscore.common.mvc.intf.ListView;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.List;
import org.thUnderscore.common.mvc.intf.ListController;
import org.thUnderscore.common.mvc.intf.ListModel;

/**
 *
 * @author thUnderscore
 */
public class BaseListController extends BaseObjectController implements ListController {

    /*
     Action commands names
     */
    public static final String ADD_COMMAND = "Add";
    public static final String DELETE_COMMAND = "Delete";
    public static final String MOVE_UP_COMMAND = "MoveUp";
    public static final String MOVE_DOWN_COMMAND = "MoveDown";

    /**
     * @see #isSilentDelete()
     */
    protected boolean silentDelete = true;

    @Override
    public boolean checkItemModified() {
        if (dontCheckModified) {
            return true;
        }
        dontCheckModified = true;
        try {
            boolean result = true;
            getListView().beforeCheckItemModified();
                        try {
                result = internalCheckItemModified();
            } finally {
                getListView().afterCheckItemModified(result);
            }
            return result;
        } finally {
            dontCheckModified = false;
        }
    }

    @Override
    protected void bind() {
        super.bind();
        if ((model != null) && (view != null)) {
            getListView().bindList();
        }
    }

    @Override
    protected void unbind() {
        super.unbind();
        if (view != null) {
            getListView().unbindList();
        }
    }

    /**
     * Reaction on add comand from view
     */
    protected void doAdd() {
        if (!checkItemModified()) {
            return;
        }
        getListModel().addNewItem();
    }

    /**
     * Reaction on delete comand from view
     */
    protected void doDelete() {
        Object current = getListModel().getCurrent();
        if (current != null) {
            if (!silentDelete && !getListView().checkDeleteCurrent()) {
                return;

            }
            internalDelete(current);
        }
    }

    /**
     * Reaction on move down comand from view
     */
    private void doMoveDown() {
        Object current = getListModel().getCurrent();
        if (current == null) {
            return;
        }
        List<Object> list = getListModel().getList();
        int index = list.indexOf(current);
        if (index == list.size()) {
            return;
        }
        getListModel().swap(index, index + 1);
        getListView().setCurrent(current);
    }

    /**
     * Reaction on move up comand from view
     */
    private void doMoveUp() {
        Object current = getListModel().getCurrent();
        if (current == null) {
            return;
        }
        List<Object> list = getListModel().getList();
        int index = list.indexOf(current);
        if (index == 0) {
            return;
        }
        getListModel().swap(index, index - 1);
        getListView().setCurrent(current);
    }

    /**
     * Delete item from list. Override if want change
     *
     * @param object deleted item
     */
    protected void internalDelete(Object object) {
        List<Object> list = getListModel().getList();
        int index = list.indexOf(object);
        boolean oldDontCheckModified = dontCheckModified;
        dontCheckModified = true;
        try {
            getListModel().removeItem(object);
        } finally {
            dontCheckModified = oldDontCheckModified;
        }
        getListModel().setCurrent((list.size() > 0)
                ? list.get(Math.min(index, list.size() - 1)) : null);

    }

    @Override
    protected void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (BaseListModel.CURRENT_PROP.equals(evt.getPropertyName())) {
            Object current = evt.getNewValue();
            //if (current != view.getCurrent()) {
            getListView().setCurrent(current);
        }
    }

    @Override
    protected void viewPropertyChange(PropertyChangeEvent evt) {
        super.viewPropertyChange(evt);
        if (BaseListModel.CURRENT_PROP.equals(evt.getPropertyName())) {
            Object current = evt.getNewValue();
            if (current != getListModel().getCurrent()) {
                if (checkItemModified()) {
                    getListModel().setCurrent(current);
                }
            }
        }
    }

    @Override
    public void setSilentDelete(boolean silentDelete) {
        this.silentDelete = silentDelete;
    }

    @Override
    public boolean isSilentDelete() {
        return silentDelete;
    }

    @Override
    public ListModel getListModel() {
        return (ListModel) model;
    }

    @Override
    public ListView getListView() {
        return (ListView) view;
    }

    /**
     * Invoke when view action occurs
     *
     * @param e ActionEvent instance
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        String actionCommand = e.getActionCommand();
        if (ADD_COMMAND.equals(actionCommand)) {
             doAdd();
        } else if (DELETE_COMMAND.equals(actionCommand)) {
             doDelete();
        } else if (MOVE_UP_COMMAND.equals(actionCommand)) {
             doMoveUp();
        } else if (MOVE_DOWN_COMMAND.equals(actionCommand)) {
             doMoveDown();
        }
    }

    protected boolean internalCheckItemModified() {
        Object current = getListModel().getCurrent();
        boolean result = true;
        if (getListModel().isCurrentModified() || getListModel().isCurrentNewObject()) {
            int dialogResult = getListView().getSaveChangedOnChangeCurrentDecision();
            switch (dialogResult) {
                case ListView.YES:
                    try {
                        getListModel().saveCurrent();
                        getListView().setCurrent(current);
                    } catch (Exception e) {
                        getListView().setCurrent(current);
                        throw new RuntimeException(e);
                    }
                    break;
                case ListView.NO:
                    if (getListModel().isCurrentNewObject()) {
                        getListModel().removeItem(current);
                    } else {
                        getListModel().cancel();
                    }
                    break;
                default:
                    if (current != getListView().getCurrent()) {
                        getListView().setCurrent(current);
                    }
                    result = false;
                    break;
            }
        }
        return result;
    }

}
