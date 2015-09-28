package org.thUnderscore.common.ui.treemodel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * TreeModel, which cant get objects from list and build tree. For each class of
 * objects in list Model should know key and parent properties names
 *
 * @author thUnderscore
 */
public class TreeModelByList implements TreeModel {

    /**
     * Tree node comparator
     */
    private Comparator comparator;

    /**
     * Description of objects classes
     */
    public static class NodeClassDescription {

        String keyName;
        String parentKeyName;
        Class clazz;

        public NodeClassDescription(Class clazz, String keyName, String parentKeyName) {
            this.keyName = keyName;
            this.parentKeyName = parentKeyName;
            this.clazz = clazz;
        }

    }

    /**
     * Class description collection
     */
    public static class NodeClassDescriptionCollection extends HashMap<Class, NodeClassDescription> {
    }

    /**
     * Object wrapper. Actualy tree contains this wrapper
     */
    static public class TreeNode {

        protected Object object;
        protected TreeNode parent;
        List<TreeNode> children = new ArrayList<TreeNode>();
        Object key;
        Object parentKey;
        String caption;

        public TreeNode(Object object, Object key, Object parentKey) {
            this.object = object;
            this.key = key;
            this.parentKey = parentKey;
        }

        public TreeNode(TreeNode parent, Object object) {
            this.object = object;
            this.parent = parent;
        }

        public Object getObject() {
            return object;
        }

        public TreeNode getParent() {
            return parent;
        }

        public Iterator<TreeNode> getChildrenIterator() {
            return children.iterator();
        }

        @Override
        public String toString() {
            return caption;
        }
    }

    List<TreeNode> nodes;
    HashMap<Object, TreeNode> hashMap;
    protected EventListenerList listenerList = new EventListenerList();
    NodeClassDescriptionCollection classDescriptions = new NodeClassDescriptionCollection();
    String fakeRootCaption;

    public TreeModelByList(NodeClassDescription[] descriptions, String fakeRootCaption,
            DefaultTreeComparator comparator) {
        init(descriptions, fakeRootCaption, comparator);
    }

    public TreeModelByList(NodeClassDescription[] descriptions, String fakeRootCaption) {
        this(descriptions, fakeRootCaption, null);
    }

    public TreeModelByList(Class clazz, String keyName,
            String parentKeyName, String fakeRootCaption) {
        this(clazz, keyName, parentKeyName, fakeRootCaption, null);
    }

    public TreeModelByList(Class clazz, String keyName,
            String parentKeyName, String fakeRootCaption, DefaultTreeComparator comparator) {
        init(clazz, keyName, parentKeyName, fakeRootCaption, comparator);
    }

    public TreeModelByList(List list, Class clazz, String keyName,
            String parentKeyName, String fakeRootCaption, DefaultTreeComparator comparator) {
        this(clazz, keyName, parentKeyName, fakeRootCaption, comparator);
        rebuild(list);
    }

    public TreeModelByList(List list, Class clazz, String keyName,
            String parentKeyName, String fakeRootCaption) {
        this(list, clazz, keyName, parentKeyName, fakeRootCaption, null);
    }

    public void init(NodeClassDescription[] descriptions, String fakeRootCaption,
            Comparator comparator) {
        classDescriptions.clear();
        for (NodeClassDescription description : descriptions) {
            classDescriptions.put(description.clazz, description);
        }
        this.fakeRootCaption = fakeRootCaption;
        this.comparator = comparator;

    }

    public void init(Class clazz, String keyName, String parentKeyName,
            String fakeRootCaption, DefaultTreeComparator comparator) {
        classDescriptions.clear();
        classDescriptions.put(clazz, new NodeClassDescription(clazz, keyName, parentKeyName));
        this.fakeRootCaption = fakeRootCaption;
        this.comparator = comparator;

    }

    /**
     * Rebuild tree
     * @param list list which contains object s for list
     */
    public void setList(List list) {
        rebuild(list);
    }

    protected void initInternalStorage(int capacity) {
        if (nodes == null) {
            nodes = new ArrayList<TreeNode>(capacity);
            hashMap = new HashMap<Object, TreeNode>(capacity);
        } else {
            nodes.clear();
            hashMap.clear();
        }
    }

    /**
     * Rebuild tree
     * @param list list which contains object s for list
     */
    protected void rebuild(List list) {
        initInternalStorage(list.size());
        Object[] keys = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            NodeClassDescription classDescription = classDescriptions.get(object.getClass());
            if (classDescription == null) {
                throw new RuntimeException("No node description for class: "
                        + object.getClass().getName());
            }

            Object key;
            Object parentKey;

            try {
                key = CommonUtils.getPropertyValue(object, classDescription.keyName);
                parentKey = CommonUtils.getPropertyValue(object, classDescription.parentKeyName);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Can't get object key or parentkey value for tree building", ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Can't get object key or parentkey value for tree building", ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException("Can't get object key or parentkey value for tree building", ex);
            }

            TreeNode node = new TreeNode(object, key, parentKey);
            nodes.add(node);
            keys[i] = (key);
            hashMap.put(key, node);
            hashMap.put(object, node);
        }
        for (int i = nodes.size() - 1; i > -1; i--) {
            TreeNode node = nodes.get(i);
            TreeNode parent = hashMap.get(node.parentKey);
            node.parent = parent;
            if (null != parent) {
                parent.children.add(node);
                nodes.remove(i);
            }
        }
        if (nodes.size() != 1) {
            TreeNode fakeRoot = new TreeNode(null, null, null);
            fakeRoot.object = fakeRoot;
            fakeRoot.children = nodes;
            fakeRoot.caption = fakeRootCaption;
            nodes = new ArrayList<TreeNode>(1);
            nodes.add(fakeRoot);
            hashMap.put(fakeRoot, fakeRoot);
        }

        for (int i = 0; i < keys.length; i++) {
            hashMap.remove(keys[i]);
        }

        if (comparator != null) {
            sortChildren(nodes.get(0));
        }
        reload();
    }

    /**
     * Sort node children if comparator is defined
     * @param node
     */
    protected void sortChildren(TreeNode node) {
        Collections.sort(node.children, comparator);
        for (int i = 0; i < node.children.size(); i++) {
            sortChildren(node.children.get(i));
        }
    }

    @Override
    public Object getChild(Object parent, int index) {
        TreeNode node = hashMap.get(parent);
        if (null == node) {
            return null;
        }
        return node.children.get(index).object;
    }

    @Override
    public int getChildCount(Object parent) {
        TreeNode node = hashMap.get(parent);
        if (null == node) {
            return -1;
        }
        return node.children.size();
    }

    @Override
    public boolean isLeaf(Object node) {
        //TODO think
        return (node instanceof TreeNodeIsLeaf) ? ((TreeNodeIsLeaf) node).IsLeaf()
                : false;//(0 == hashMap.get(node).childred.size());
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        TreeNode parentNode = hashMap.get(parent);
        TreeNode childNode = hashMap.get(child);
        if ((null == parentNode) || (null == childNode)) {
            return -1;
        }
        return parentNode.children.indexOf(childNode);
    }

    /**
     * Clear tree
     */
    public void clear() {
        nodes.clear();
        hashMap.clear();
    }

    /**
     * Sets the root to <code>root</code>. A null <code>root</code> implies the
     * tree is to display nothing, and is legal.
     * @param root
     */
    public void setRoot(Object root) {
        Object oldRoot;
        if ((nodes != null) && (nodes.size() > 0)) {
            oldRoot = nodes.get(0).object;
        } else {
            oldRoot = null;
        }
        initInternalStorage(10);
        if (root != null) {
            TreeNode newRoot = new TreeNode(root, null, null);
            nodes.add(newRoot);
            hashMap.put(root, newRoot);
            nodeStructureChanged(newRoot);
        } else if (oldRoot != null) {
            fireTreeStructureChanged(this, null);
        }
    }

    /**
     * Returns the root of the tree. Returns null only if the tree has no nodes.
     *
     * @return the root of the tree
     */
    @Override
    public Object getRoot() {
        if ((nodes != null) && (nodes.size() > 0)) {
            return nodes.get(0).object;
        } else {
            return null;
        }
    }

    /**
     * Get tree node which contains root object
     * @return
     */
    public TreeNode getRootNode() {
        if ((nodes != null) && (nodes.size() > 0)) {
            return nodes.get(0);
        } else {
            return null;
        }
    }

    /**
     * This sets the user object of the TreeNode identified by path and posts a
     * node changed. If you use custom user objects in the TreeModel you're
     * going to need to subclass this and set the user object of the changed
     * node to something meaningful.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        if (newValue == null) {
            throw new IllegalArgumentException("Can't set value to null");
        }
        TreeNode objectNode = getNode(path.getLastPathComponent());
        objectNode.object = newValue;
        nodeChanged(objectNode);
    }

    private TreeNode getNode(Object object) {
        TreeNode node = hashMap.get(object);
        if (node == null) {
            throw new IllegalArgumentException("Node does not exist.");
        }
        return node;
    }

    /**
     * Invoked this to insert newChild at location index in parents children.
     * This will then message nodesWereInserted to create the appropriate event.
     * This is the preferred way to add children as it will create the
     * appropriate event.
     */
    public void insertObjectInto(Object newChild,
            Object parent, int index) {
        if (hashMap.get(newChild) != null) {
            throw new RuntimeException("Object allready exists in tree: " + newChild);
        }
        TreeNode parentNode = getNode(parent);
        TreeNode childNode = new TreeNode(parentNode, newChild);
        if (index < 0) {
            index = parentNode.children.size();
        }
        parentNode.children.add(index, childNode);
        hashMap.put(newChild, childNode);

        int[] newIndexs = new int[1];
        newIndexs[0] = index;
        nodesWereInserted(parentNode, newIndexs);
    }

    /**
     * Message this to remove node from its parent. This will message
     * nodesWereRemoved to create the appropriate event. This is the preferred
     * way to remove a node as it handles the event creation for you.
     */
    public void removeObjectFromParent(Object object) {
        TreeNode objectNode = getNode(object);
        if (objectNode == getRootNode()) {
            return;
        }
        TreeNode parent = objectNode.parent;
        if (parent == null) {
            throw new IllegalArgumentException("Node does not have a parent.");
        }
        int[] childIndex = new int[1];
        Object[] removedArray = new Object[1];
        childIndex[0] = parent.children.indexOf(objectNode);
        parent.children.remove(objectNode);
        hashMap.remove(object);
        removedArray[0] = object;
        nodesWereRemoved(parent, childIndex, removedArray);
    }

    public boolean objectExist(Object object) {
        return hashMap.get(object) != null;
    }

    /**
     * Invoke this method after you've changed how node is to be represented in
     * the tree.
     */
    public void nodeChanged(Object object) {
        TreeNode objectNode = getNode(object);
        nodeChanged(objectNode);
    }

    private void nodeChanged(TreeNode node) {
        if (listenerList != null && node != null) {
            TreeNode parent = node.parent;

            if (parent != null) {
                int anIndex = parent.children.indexOf(node);
                if (anIndex != -1) {
                    int[] cIndexs = new int[1];

                    cIndexs[0] = anIndex;
                    nodesChanged(parent, cIndexs);
                }
            } else if (node == getRootNode()) {
                nodesChanged(node, null);
            }
        }
    }

    /**
     * Invoke this method if you've modified the {@code TreeNode}s upon which
     * this model depends. The model will notify all of its listeners that the
     * model has changed below the given node.
     *
     * @param node the node below which the model has changed
     */
    public void reload(TreeNode node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Invoke this method if you've modified the {@code TreeNode}s upon which
     * this model depends. The model will notify all of its listeners that the
     * model has changed.
     */
    public void reload() {
        reload(getRootNode());
    }

    /**
     * Invoke this method after you've inserted some TreeNodes into node.
     * childIndices should be the index of the new elements and must be sorted
     * in ascending order.
     */
    public void nodesWereInserted(TreeNode node, int[] childIndices) {
        if (listenerList != null && node != null && childIndices != null
                && childIndices.length > 0) {
            int cCount = childIndices.length;
            Object[] newChildren = new Object[cCount];

            for (int counter = 0; counter < cCount; counter++) {
                newChildren[counter] = node.children.get(counter).object;
            }
            fireTreeNodesInserted(this, getPathToRoot(node), childIndices,
                    newChildren);
        }
    }

    /**
     * Invoke this method after you've removed some TreeNodes from node.
     * childIndices should be the index of the removed elements and must be
     * sorted in ascending order. And removedChildren should be the array of the
     * children objects that were removed.
     */
    public void nodesWereRemoved(TreeNode node, int[] childIndices,
            Object[] removedChildren) {
        if (node != null && childIndices != null) {
            fireTreeNodesRemoved(this, getPathToRoot(node), childIndices,
                    removedChildren);
        }
    }

    /**
     * Invoke this method after you've changed how the children identified by
     * childIndicies are to be represented in the tree.
     */
    public void nodesChanged(TreeNode node, int[] childIndices) {
        if (node != null) {
            if (childIndices != null) {
                int cCount = childIndices.length;

                if (cCount > 0) {
                    Object[] cChildren = new Object[cCount];

                    for (int counter = 0; counter < cCount; counter++) {
                        cChildren[counter] = node.children.get(childIndices[counter]).object;
                    }
                    fireTreeNodesChanged(this, getPathToRoot(node),
                            childIndices, cChildren);
                }
            } else if (node == getRootNode()) {
                fireTreeNodesChanged(this, getPathToRoot(node), null, null);
            }
        }
    }

    /**
     * Invoke this method if you've totally changed the children of node and its
     * childrens children... This will post a treeStructureChanged event.
     */
    private void nodeStructureChanged(TreeNode node) {
        if (node == null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Builds the parents of node up to and including the root node, where the
     * original node is the last element in the returned array. The length of
     * the returned array gives the node's depth in the tree.
     *
     * @param node the TreeNode to get the path for
     * @return 
     */
    public Object[] getPathToRoot(TreeNode node) {
        return getPathToRoot(node, 0);
    }

    /**
     * Builds the parents of node up to and including the root node, where the
     * original node is the last element in the returned array. The length of
     * the returned array gives the node's depth in the tree.
     *
     * @param node
     * @param node the TreeNode to get the path for
     * @param depth an int giving the number of steps already taken towards the
     * root (on recursive calls), used to size the returned array
     * @return an array of TreeNodes giving the path from the root to the
     * specified node
     */
    protected Object[] getPathToRoot(TreeNode node, int depth) {
        Object[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        // Check for null, in case someone passed in a null node, or
        //   they passed in an element that isn't rooted at root. 
        TreeNode parent = null;
        if (node == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new Object[depth];
            }
        } else {
            depth++;
            if (node == getRoot()) {
                retNodes = new Object[depth];
            } else {
                retNodes = getPathToRoot(node.parent, depth);
            }
            retNodes[retNodes.length - depth] = node.object;
        }
        return retNodes;
    }

    public TreePath getPath(Object object) {
        return new TreePath(getPathToRoot(getNode(object)));
    }

    //
    //  Events
    //
    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     *
     * @see #removeTreeModelListener
     * @param l the listener to add
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @see #addTreeModelListener
     * @param l the listener to remove
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    /**
     * Returns an array of all the tree model listeners registered on this
     * model.
     *
     * @return all of this model's <code>TreeModelListener</code>s or an empty
     * array if no tree model listeners are currently registered
     *
     * @see #addTreeModelListener
     * @see #removeTreeModelListener
     *
     * @since 1.4
     */
    public TreeModelListener[] getTreeModelListeners() {
        return (TreeModelListener[]) listenerList.getListeners(
                TreeModelListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     *
     * @param source the node being changed
     * @param path the path to the root node
     * @param childIndices the indices of the changed elements
     * @param children the changed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     *
     * @param source the node where new elements are being inserted
     * @param path the path to the root node
     * @param childIndices the indices of the new elements
     * @param children the new elements
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     *
     * @param source the node where elements are being removed
     * @param path the path to the root node
     * @param childIndices the indices of the removed elements
     * @param children the removed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @param childIndices the indices of the affected elements
     * @param children the affected elements
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /*
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @see EventListenerList
     */
    private void fireTreeStructureChanged(Object source, TreePath path) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * Returns an array of all the objects currently registered as
     * <code><em>Foo</em>Listener</code>s upon this model.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     *
     * <p>
     *
     * You can specify the <code>listenerType</code> argument with a class
     * literal, such as <code><em>Foo</em>Listener.class</code>. For example,
     * you can query a <code>DefaultTreeModel</code> <code>m</code> for its tree
     * model listeners with the following code:
     *
     * <pre>TreeModelListener[] tmls = (TreeModelListener[])(m.getListeners(TreeModelListener.class));</pre>
     *
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType the type of listeners requested; this parameter
     * should specify an interface that descends from
     * <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     * <code><em>Foo</em>Listener</code>s on this component, or an empty array
     * if no such listeners have been added
     * @exception ClassCastException if <code>listenerType</code> doesn't
     * specify a class or interface that implements
     * <code>java.util.EventListener</code>
     *
     * @see #getTreeModelListeners
     *
     * @since 1.3
     */
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return listenerList.getListeners(listenerType);
    }
}
