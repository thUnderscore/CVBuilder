package org.thUnderscore.common.ui.treemodel;


/**
 * If object implements this interface, than TreeModelByList can understand
 * is this object leaf or not. In other case ypu should override IsLeaf method
 * in model
 * @author tosh
 */
public interface TreeNodeIsLeaf {

    /**
     *
     * @return Is object tree's leaf
     */
    boolean IsLeaf();
}
