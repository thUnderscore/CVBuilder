package org.thUnderscore.app.cv;

import java.awt.Dimension;
import java.util.Locale;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import org.thUnderscore.common.DefaultExceptionHandler;
import org.thUnderscore.common.utils.UIUtils;
import org.thUnderscore.common.ui.pages.MultiPageFrame;
import org.thUnderscore.common.ui.pages.MultiPageFrame.PageDescription;
import org.thUnderscore.common.ui.pages.source.SourcePage;
import org.thUnderscore.common.ui.pages.source.SourcePageParams;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * CV builder application class. Contains main() method
 *
 * @author thUnderscore
 */
public class CVBuilderApp {

    /*
        Resources constants
    */
    private static final String CV_BUILDER_PAGE_TITLE_RES = "CVBuilderApp.CV_BUILDER_PAGE_TITLE";
    private static final String SOURCE_PAGE_TITLE_RES = "CVBuilderApp.SOURCE_PAGE_TITLE";
    private static final String MAIN_FRAME_TITLE_RES = "CVBuilderApp.MAIN_FRAME_TITLE";

    /**
     * Main function 
     * 
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws javax.swing.UnsupportedLookAndFeelException
     */
    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        
        //Use default exception handler
        DefaultExceptionHandler.register();
        
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //workaround swing issue
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        UIManager.put("TextArea.font", UIManager.get("TextField.font"));
        
        //TODO
        //Locale.setDefault(new Locale("en", "EN"));
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                //define tab list
                PageDescription[] pagesDescription = {
                    new PageDescription(CVBuilderPage.class,
                        CVUtils.i18n(CV_BUILDER_PAGE_TITLE_RES), null
                    ),
                    new PageDescription(SourcePage.class,
                        CVUtils.i18n(SOURCE_PAGE_TITLE_RES),
                        new SourcePageParams("/sources/src/org/thUnderscore/app/cv/obj/mvc/word/CVView.java")
                            .addRoot("sources")
                            .addSource("org/apache", "sources/classes")
                            .addSource("org/dom4j", "sources/classes")
                            .addSource("org/jdesktop", "sources/classes")
                            .addSource("org/openxmlformats", "sources/classes")
                            .addSource("org/w3c", "sources/classes")
                            .addSource("schemaorg_apache_xmlbeans", "sources/classes")
                            .addSource("syntaxhighlight", "sources/classes")
                            .addSource("syntaxhighlighter", "sources/classes")
                    )

                };
                //create main frame
                MultiPageFrame multiPageFrame = new MultiPageFrame(
                        CVUtils.i18n(MAIN_FRAME_TITLE_RES), pagesDescription,
                        new Dimension(1400, 900), WindowConstants.EXIT_ON_CLOSE/*,
                        CommonUtils.IS_WIN ? Type.POPUP : Type.UTILITY*/);
                //show main frame
                UIUtils.showMainFrame(true, false, multiPageFrame);
                
                pagesDescription[0].getComponent().showHelp();
                
            }
        });
    }


}
