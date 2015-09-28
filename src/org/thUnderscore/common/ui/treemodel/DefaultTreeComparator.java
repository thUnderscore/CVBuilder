package org.thUnderscore.common.ui.treemodel;

import java.util.Comparator;

/**
 * Default comparator for tree nodes. Compare object.toString() result
 * @author tosh
 */
public class DefaultTreeComparator implements Comparator<TreeModelByList.TreeNode> {

    @Override
    public int compare(TreeModelByList.TreeNode o1, TreeModelByList.TreeNode o2) {
        Object obj1 = o1.object;
        Object obj2 = o2.object;
        if ((obj1 == null) && (obj2 == null)) {
            return 0;
        } else if (obj1 == null) {
            return -1;
        } else if (obj2 == null) {
            return 1;
        }
        boolean isLeaf1 = (obj1 instanceof TreeNodeIsLeaf)
                ? ((TreeNodeIsLeaf) obj1).IsLeaf() : false;
        boolean isLeaf2 = (obj2 instanceof TreeNodeIsLeaf)
                ? ((TreeNodeIsLeaf) obj2).IsLeaf() : false;
        if (isLeaf1 && !isLeaf2) {
            return 1;
        } else if (!isLeaf1 && isLeaf2) {
            return -1;
        }
        return obj1.toString().compareTo(obj2.toString());
    }

}
