package org.thUnderscore.app.cv.obj.mvc.swing;

import java.awt.event.ActionEvent;
import org.thUnderscore.app.cv.CVUtils;
import org.thUnderscore.app.cv.obj.Skill;
import static org.thUnderscore.common.mvc.BaseListController.ADD_COMMAND;
import org.thUnderscore.common.mvc.swing.BaseSwingListView;
import org.thUnderscore.common.mvc.swing.elements.EditsGridElement;

/**
 * View for skills list
 * 
 * @author thUnderscore
 */
public class SkillsView extends BaseSwingListView {

    {
        //define table columns
        addColumnDescription(Skill.NAME,
                CVUtils.i18n(SkillView.NAME_CAPTION_RES), String.class);
    }
    public SkillsView() {
        super(SkillView.class, Skill.class);        
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
                        EditsGridElement.getEditName(Skill.NAME)).requestFocus();
        }
    }
}
