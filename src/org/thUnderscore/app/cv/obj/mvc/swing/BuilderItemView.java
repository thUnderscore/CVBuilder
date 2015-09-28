package org.thUnderscore.app.cv.obj.mvc.swing;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import org.thUnderscore.app.cv.obj.BuilderItem;
import org.thUnderscore.common.mvc.swing.BaseSwingObjectView;
import org.thUnderscore.common.mvc.swing.elements.SubObjectElement;

/**
 * View for CV Builder list item of CV builder
 * 
 * @author thUnderscore
 */
public class BuilderItemView extends BaseSwingObjectView {
    
    {
        //define view structure
        addElement(new SubObjectElement(BuilderItem.CV, CVView.class));
    }

    @Override
    protected void placeComponents(JComponent container) {
        super.placeComponents(container);
        container.setLayout(new BorderLayout());
        container.add(getComponentByName(BuilderItem.CV), BorderLayout.CENTER);
    }

}
