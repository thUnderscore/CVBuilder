package org.thUnderscore.common.ui.pages;

import java.awt.Dimension;
import javax.swing.JPanel;
import org.thUnderscore.common.ui.components.ButtonTabComponent;
import org.thUnderscore.common.ui.help.HelpHint;
import org.thUnderscore.common.ui.help.HelpTooltipWindow;

/**
 * Base class of page in
 * <code>MultiPageFrame</code>
 *
 * @author thunderscore
 */
public class Page extends JPanel {
    /**
     * Component displayed on tab
     */
    protected ButtonTabComponent tabComponent;
    
    /**
     * Help hints array
     */
    protected HelpHint[] helpHints ;

    /**
     * Minimun help window size
     */
    protected Dimension helpMinimumSize  = new Dimension(250, 40);
    
    /**
     * Is called after Page creation
     *
     * @param params page setting can be passed here
     */
    protected void init(PageParams params) {
    }

    /**
     * Check passed page params
     *
     * @param params page params
     * @param isNullAlowed can params be null
     * @param requiredClass expected params class
     */
    protected void checkParams(PageParams params, boolean isNullAlowed,
            Class<? extends PageParams> requiredClass) {
        if (((params == null) && !isNullAlowed) || ((params != null)
                && (requiredClass != null) && !requiredClass.isInstance(params))) {
            String message = String.format("%s: %s", getClass().getName(),
                    params == null ? "null" : params.getClass().getName());
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Gets is page help option enabled
     *
     * @return is help enabled
     */
    public boolean isHelpEnabled() {
        return true;
    }

    /**
     * Show page help 
     */
    public void showHelp() {
        HelpTooltipWindow.showTooltipWindow(tabComponent, helpHints, helpMinimumSize);
    }

    /**
     * Sets tab component, displayed in tab header
     * @param tabComponent 
     */
    void setTabComponent(ButtonTabComponent tabComponent) {
        this.tabComponent = tabComponent;
    }
}
