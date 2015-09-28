package org.thUnderscore.common;

import org.thUnderscore.common.utils.CommonUtils;
import javax.swing.JOptionPane;

/**
 * Default uncaught exception handler. Show error message
 *
 * @author thUnderscore
 */
public class DefaultExceptionHandler {

    public static final String ERROR_TITLE_RES = "DefaultExceptionHandler.ERROR_TITLE";

    /**
     * Set DefaultExceptionHandler as uncaught exception handler
     */
    public static void register() {
        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        JOptionPane.showMessageDialog(null, String.format("%s: \"%s\"",
                        e.getClass().getSimpleName(), e.getLocalizedMessage()),
                        CommonUtils.i18n(ERROR_TITLE_RES), JOptionPane.ERROR_MESSAGE);
                    }
                });
    }

}
