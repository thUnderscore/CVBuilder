package org.thUnderscore.app.cv.obj.mvc.swing;

import org.thUnderscore.app.cv.obj.Employment;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.common.mvc.swing.BaseSwingListView;

/**
 * View for employments list
 * 
 * @author thUnderscore
 */
public class EmploymentsView extends BaseSwingListView {

    {
        //define view structure
        addColumnDescription(Employment.FROM,
                CVUtils.i18n(EmploymentView.FROM_CAPTION_RES), String.class);
        addColumnDescription(Employment.TO,
                CVUtils.i18n(EmploymentView.TO_CAPTION_RES), String.class);
        addColumnDescription(Employment.COMPANY,
                CVUtils.i18n(EmploymentView.COMPANY_CAPTION_RES), String.class);
        addColumnDescription(Employment.ROLE,
                CVUtils.i18n(EmploymentView.ROLE_CAPTION_RES), String.class);
        
    }
    
    public EmploymentsView() {
        super(EmploymentView.class, Employment.class);       
    }
}
