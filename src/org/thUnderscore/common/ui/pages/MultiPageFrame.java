package org.thUnderscore.common.ui.pages;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.thUnderscore.common.utils.UIUtils;
import org.thUnderscore.common.ui.components.ButtonTabComponent;

/**
 * Frame which can create tabs by description
 *
 * @author tosh
 */
public class MultiPageFrame extends JFrame {

    /**
     * tab description
     */
    public static class PageDescription {

        /**
         * Page class
         */
        Class<? extends Page> componentClass;
        /**
         * Created bage, which is placed on tab
         */
        Page component;
        /**
         * tab title
         */
        String title;
        /**
         * page params
         */
        PageParams params;

        /**
         * Page for multi page frame description
         *
         * @param componentClass class component which displays on page
         * @param title page title
         * @param params page initialization params
         */
        public PageDescription(Class<? extends Page> componentClass,
                String title, PageParams params) {
            this.componentClass = componentClass;
            this.title = title;
            this.params = params;
        }

        /**
         * Gets page component
         * @return Page instance
         */
        public Page getComponent() {
            return component;
        }
        
        

    }
    /**
     * array of page descriptions
     */
    final PageDescription[] pages;
    /**
     * tabbed pane
     */
    JTabbedPane tabbedPane;

    /**
     *
     * @param title frame title
     * @param pages array of page descriptions
     * @param size frame size
     * @param defaultCloseOperation frame default close operation
     * @param type frame window type
     */
    public MultiPageFrame(String title, final PageDescription[] pages, Dimension size,
            int defaultCloseOperation/*, Type type*/) {

        this.pages = pages;
        setDefaultCloseOperation(defaultCloseOperation);
        setMinimumSize(size);
        setPreferredSize(size);
        /*setType(type);*/
        setTitle(title);

        tabbedPane = new JTabbedPane();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPane)
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPane)
                );
        for (PageDescription page : pages) {
            try {
                Page pageComponent = (Page) page.componentClass.newInstance();
                pageComponent.init(page.params);
                page.component = pageComponent;

                int index = tabbedPane.getTabCount();
                tabbedPane.addTab(page.title, page.component);
                ButtonTabComponent tabComponent = new ButtonTabComponent(tabbedPane,
                        pageComponent.isHelpEnabled()) {

                            @Override
                            protected void drawTabButton(Graphics2D g2, boolean pressed,
                            int indexOfTabComponent, ButtonTabComponent.TabButton button
                            ) {
                                super.drawTabButton(g2, pressed, indexOfTabComponent, button);
                                g2.setStroke(new BasicStroke(2));
                                g2.setColor(button.getModel().isRollover()
                                ? UIUtils.OVER_COLOR : UIUtils.OUT_COLOR);

                                Font commoon = getFont();
                                Font boldFont = new Font(commoon.getName(), Font.BOLD, commoon.getSize() + 2);

                                g2.setFont(boldFont);
                                g2.drawString("?", 4, getHeight() - 6);

                                g2.dispose();
                            }

                            @Override
                            protected void buttonPressed(int indexOfTabComponent, ButtonTabComponent.TabButton button) {
                                if (indexOfTabComponent > -1) {
                                    pages[indexOfTabComponent].component.showHelp();
                                }
                            }

                        };
                tabbedPane.setTabComponentAt(index,
                        tabComponent);
                pageComponent.setTabComponent(tabComponent);
            } catch (InstantiationException ex) {
                Logger.getLogger(MultiPageFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(MultiPageFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        pack();
    }

}
