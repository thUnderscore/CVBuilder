package org.thUnderscore.app.cv.obj.mvc.swing;

import org.thUnderscore.app.cv.obj.SkillItem;
import org.thUnderscore.app.cv.obj.Skill;
import java.awt.event.ActionEvent;
import org.thUnderscore.app.cv.CVUtils;
import static org.thUnderscore.common.mvc.BaseListController.ADD_COMMAND;
import org.thUnderscore.common.mvc.swing.BaseSwingListView;
import static org.thUnderscore.common.mvc.swing.BaseSwingListView.LIST_PANEL;
import static org.thUnderscore.common.mvc.swing.BaseSwingObjectView.GENERAL_INFO;
import org.thUnderscore.common.mvc.swing.elements.EditsGridElement;

/**
 * View for skill
 *
 * @author thUnderscore
 */
public class SkillView extends BaseSwingListView {

    /*
     Resources constants
     */
    public static final String NAME_CAPTION_RES = "SkillView.NAME_CAPTION";
    public static final String NAME_HINT_RES = "SkillView.NAME_HINT";

    {
        //define table columns
        addColumnDescription(SkillItem.NAME,
                CVUtils.i18n(SkillItemView.NAME_CAPTION_RES), String.class);
        //define view structure
        addElement(new EditsGridElement(GENERAL_INFO)
                .addTextElementDescription(Skill.NAME,
                        CVUtils.i18n(NAME_CAPTION_RES),
                        CVUtils.i18n(NAME_HINT_RES), false, 0, 0)
                .setGap(EditsGridElement.TITLE_BORDER_GAP)
                .setSize(423, 32)
        );
        //elements are not placed here. Skills list view do it
    }

    public SkillView() {
        super(SkillItemView.class, SkillItem.class);
    }

    @Override
    protected void fillListPanel() {
        super.fillListPanel();
        //get component from item view and place in itself
        addItemViewComponentByNameToContainer(LIST_PANEL, GENERAL_INFO);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        String actionCommand = e.getActionCommand();
        if (ADD_COMMAND.equals(actionCommand)) {
            getItemViewComponentByName(
                    EditsGridElement.getEditName(SkillItem.NAME)).requestFocus();
        }
    }
}
