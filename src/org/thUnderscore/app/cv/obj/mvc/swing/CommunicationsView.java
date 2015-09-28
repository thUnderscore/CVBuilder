package org.thUnderscore.app.cv.obj.mvc.swing;

import org.thUnderscore.app.cv.obj.Communications;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.common.mvc.swing.BaseSwingObjectView;
import org.thUnderscore.common.mvc.swing.elements.EditsGridElement;

/**
 * View for communications
 * 
 * @author thUnderscore
 */
public class CommunicationsView extends BaseSwingObjectView {

    /*
        Resources constants
    */
    public static final String PHONE_CAPTION_RES = "CommunicationsView.PHONE_CAPTION";
    public static final String SKYPE_CAPTION_RES = "CommunicationsView.SKYPE_CAPTION";
    public static final String EMAIL_CAPTION_RES = "CommunicationsView.EMAIL_CAPTION";
    public static final String WEB_CAPTION_RES = "CommunicationsView.WEB_CAPTION";
    public static final String COMMUNICATION_HINT_RES = "CommunicationsView.COMMUNICATION_HINT";
    public static final String BORDER_CAPTION_RES = "CommunicationsView.BORDER_CAPTION";
    
    /*
        UI constans
    */
    public static final int LABEL_WIDTH = 60;

    {
        //define view structure
        addElement(new EditsGridElement(GENERAL_INFO)
            .addTextElementDescription(Communications.PHONE, 
                    CVUtils.i18n(PHONE_CAPTION_RES), 
                    CVUtils.i18n(COMMUNICATION_HINT_RES), true, 0, 0)
            .addTextElementDescription(Communications.SKYPE, 
                    CVUtils.i18n(SKYPE_CAPTION_RES), 
                    CVUtils.i18n(COMMUNICATION_HINT_RES), false, 0, 1)
            .addTextElementDescription(Communications.EMAIL, 
                    CVUtils.i18n(EMAIL_CAPTION_RES), 
                    CVUtils.i18n(COMMUNICATION_HINT_RES), true, 1, 0)
            .addTextElementDescription(Communications.WEB,
                    CVUtils.i18n(WEB_CAPTION_RES), 
                    CVUtils.i18n(COMMUNICATION_HINT_RES), false, 1, 1)
            .setColLabelWidth(new int[] {LABEL_WIDTH, LABEL_WIDTH})                          
            .setBorder(true, CVUtils.i18n(BORDER_CAPTION_RES))
        );
    }

    @Override
    protected void placeComponents(JComponent container) {
        super.placeComponents(container);
        container.setLayout(new BorderLayout());
        container.add(getComponentByName(GENERAL_INFO));

    }
}
