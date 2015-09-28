package org.thUnderscore.common.mvc;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.thUnderscore.common.mvc.intf.EditableObject;
import org.thUnderscore.common.mvc.intf.ListInclusiveObject;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.ObservableListEx;
import org.thUnderscore.common.mvc.intf.ListModel;

/**
 *
 * @author thUnderscore
 */
public class BaseListModel extends BaseObjectModel implements ListModel {

    /**
     * Keys in bndle for messages
     */
    public static final String ADD_NEW_ITEM_ERROR = "BaseListModel.ADD_NEW_ITEM_ERROR";
    public static final String REMOVE_ITEM_NOT_OVERRIDED_ERROR = "BaseListModel.REMOVE_ITEM_NOT_OVERRIDED_ERROR";
    public static final String ITEM_CLASS_NOT_ASSIGNED_ERROR = "BaseListModel.ITEM_CLASS_NOT_ASSIGNED_ERROR";
    public static final String CANT_GET_LIST_ERROR = "BaseListModel.CANT_GET_LIST_ERROR";

    /**
     * Current property name
     */
    public static final String CURRENT_PROP = "current";
    /**
     * List if object child
     * @see #getObject() 
     */
    protected final ObservableListEx<Object> list = new ObservableListEx(null);
    /**
     * Current child
     * @see #getCurrent() 
     */
    protected Object current;
    /**
     * Class of children
     */
    protected Class itemClass;

    /**
     * 
     * @param itemClass Class of children
     */
    public BaseListModel(Class itemClass) {
        this.itemClass = itemClass;
    }

    /**
     * Set first child as current or null id children list is empty
     */
    protected void resetCurrent() {
        setCurrent(list.size() > 0 ? list.get(0) : null);
    }

    /**
     * Check and add item to children list
     * @param item item to be added
     * @return true if item was added
     * @see #isAcceptable(java.lang.Object) 
     */
    protected boolean addItem(Object item) {
        boolean result = isAcceptable(item);
        if (result) {
            result = list.add(item);
            if (result) {
                if (object instanceof ListInclusiveObject) {
                    ((ListInclusiveObject) object).itemAdded(item);
                }
            }
        }
        return result;
    }

    /**
     * Create new child. if represented object is instance of ListInclusiveObject
     * or itemClass is defined - it will be created. in other case metthod should 
     * be overriden in inheritor
     * @return created item
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    protected Object createItem() throws InstantiationException, IllegalAccessException {
        if (object instanceof ListInclusiveObject) {
            return ((ListInclusiveObject) object).createItem();
        } else if (itemClass != null) {
            return itemClass.newInstance();
        } else {
            throw new InstantiationError(
                    String.format(CommonUtils.i18n(ITEM_CLASS_NOT_ASSIGNED_ERROR),
                            getClass().getSimpleName()));
        }
    }

    /**
     * Add all children from represented object to internal model list
     */
    protected void addAllItemToList() {
        if (object != null) {
            if (object instanceof ListInclusiveObject) {
                list.setList(((ListInclusiveObject) object).getList());
            } else {
                throw new RuntimeException(String.format(CommonUtils.i18n(CANT_GET_LIST_ERROR),
                        getClass().getSimpleName()));
            }
        } else {
            list.setList(null);
        }
    }

    /**
     * Prepare list item after creation. Override if need additional item initialization
     * @param item created item
     */
    protected void prepareNewItem(Object item) {
        if (item instanceof EditableObject) {
            ((EditableObject) item).setNewObject(true);
        }
    }

    @Override
    public void setObject(Object object) {
        if (this.object == object) {
            return;
        }
        super.setObject(object);
        addAllItemToList();
        resetCurrent();
    }

    @Override
    public List<Object> getList() {
        return list;
    }

    /**
     * Gets current list item
     * @return current list item (like current record in dataset)
     */
    @Override
    public Object getCurrent() {
        return current;
    }

    @Override
    public void setCurrent(Object current) {
        if (current != null) {
            int index = list.indexOf(current);
            if (index < 0) {
                return;
            }
        }
        firePropertyChange(CURRENT_PROP, this.current, this.current = current);
    }

    @Override
    public Object addNewItem() {
        try {
            Object item = createItem();
            prepareNewItem(item);
            if (item instanceof EditableObject) {
                ((EditableObject) item).setModified(true);
            }
            addItem(item);
            setCurrent(item);
            return item;
        } catch (InstantiationException ex) {
            throw new RuntimeException(String.format(
                    CommonUtils.i18n(ADD_NEW_ITEM_ERROR), ex.getMessage()));
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(String.format(
                    CommonUtils.i18n(ADD_NEW_ITEM_ERROR), ex.getMessage()));
        }
    }

    @Override
    public void removeItem(Object item) {
        if (list.remove(item)) {
            if (object instanceof ListInclusiveObject) {
                ((ListInclusiveObject) object).itemRemoved(item);
            } else {
                throw new UnsupportedOperationException(
                        CommonUtils.i18n(REMOVE_ITEM_NOT_OVERRIDED_ERROR));
            }
        }
    }

    @Override
    public boolean isCurrentModified() {
        return (current == null) || !(current instanceof EditableObject) ? false
                : ((EditableObject) current).isModified();
    }

    @Override
    public boolean isCurrentNewObject() {
        return (current == null) || !(current instanceof EditableObject) ? false
                : ((EditableObject) current).isNewObject();
    }

    @Override
    public void cancel() {
        if ((current != null) && (current instanceof EditableObject)) {
            ((EditableObject) current).cancel();
        }
    }

    @Override
    public void saveCurrent() {
        if ((current != null) && (current instanceof EditableObject)) {
            ((EditableObject) current).save();
        }
    }

    @Override
    public boolean isAcceptable(Object item) {
        return (item != null) && (itemClass != null)
                && itemClass.isInstance(item);
    }

    @Override
    public void swap(int i, int j) {
        if (list != null) {
            Collections.swap(list, i, j);
            if (object instanceof ListInclusiveObject) {
                ((ListInclusiveObject) object).itemSwaped(i, j);
            }
        }
    }

}
