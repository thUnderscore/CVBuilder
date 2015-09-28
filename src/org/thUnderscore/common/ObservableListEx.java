package org.thUnderscore.common;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

/**
 * A {@code List} that notifies listeners of changes
 * @author thUnderscore
 */
public class ObservableListEx<E> extends AbstractList<E>
        implements ObservableList<E> {

    private final boolean supportsElementPropertyChanged;
    private List<E> list;
    private List<ObservableListListener> listeners;

    public ObservableListEx(List<E> list, boolean supportsElementPropertyChanged) {
        this.list = list;
        listeners = new CopyOnWriteArrayList<ObservableListListener>();
        this.supportsElementPropertyChanged = supportsElementPropertyChanged;
    }
    
    public ObservableListEx(List<E> list) {
        this(list, false);
    }

    public void setList(List<? extends E> l) {
        if ((list != null) && !list.isEmpty()) {
            modCount++;
            for (ObservableListListener listener : listeners) {
                listener.listElementsRemoved(this, 0, list);
            }
        }
        list = (List<E>) l;
        if (list != null) {
            modCount++;
            for (ObservableListListener listener : listeners) {
                listener.listElementsAdded(this, 0, list.size());
            }
        }

    }

    //TODO check and remove
    /*private void fireElementChanged(int index) {
        for (ObservableListListener listener : listeners) {
            listener.listElementPropertyChanged(this, index);
        }
    }*/
    
    @Override
    public E get(int index) {
        return list == null ? null : list.get(index);
    }

    @Override
    public int size() {
        return list == null ? 0 : list.size();
    }

    @Override
    public E set(int index, E element) {
        E oldValue;
        if (list != null) {
            oldValue = list.set(index, element);

            for (ObservableListListener listener : listeners) {
                listener.listElementReplaced(this, index, oldValue);
            }
        } else {
            oldValue = null;
        }

        return oldValue;
    }

    @Override
    public void add(int index, E element) {
        if (list != null) {
            list.add(index, element);
            modCount++;

            for (ObservableListListener listener : listeners) {
                listener.listElementsAdded(this, index, 1);
            }
        }
    }

    @Override
    public E remove(int index) {
        E oldValue;
        if (list != null) {
            oldValue = list.remove(index);
            modCount++;

            for (ObservableListListener listener : listeners) {
                listener.listElementsRemoved(this, index,
                        java.util.Collections.singletonList(oldValue));
            }
        } else {
            oldValue = null;
        }
        return oldValue;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size(), c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (list != null) {
            if (list.addAll(index, c)) {
                modCount++;

                for (ObservableListListener listener : listeners) {
                    listener.listElementsAdded(this, index, c.size());
                }
            }
        }
        return false;
    }    
    
    @Override
    public void clear() {
        if (list != null) {
            List<E> dup = new ArrayList<E>(list);
            list.clear();
            modCount++;

            if (!dup.isEmpty()) {
                for (ObservableListListener listener : listeners) {
                    listener.listElementsRemoved(this, 0, dup);
                }
            }
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list == null ? false : list.containsAll(c);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list == null ? null : list.toArray(a);
    }

    @Override
    public Object[] toArray() {
        return list == null ? null : list.toArray();
    }    

    @Override
    public void addObservableListListener(ObservableListListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeObservableListListener(
            ObservableListListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean supportsElementPropertyChanged() {
        return supportsElementPropertyChanged;
    }

}
