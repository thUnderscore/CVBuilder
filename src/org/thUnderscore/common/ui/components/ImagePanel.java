package org.thUnderscore.common.ui.components;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.thUnderscore.common.ui.intf.EditorComponent;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.utils.UIUtils;

/**
 *
 * @author thUnderscore
 */
public class ImagePanel extends JPanel implements EditorComponent{

    /*
        Properties names
     */
    public static final String IMAGE_PROP = "image";
    
    /*
        Resources constants
    */
    private static final String SELECT_DIALOG_TITLE_RES = "ImagePanel.SELECT_DIALOG_TITLE";
    private static final String FILE_FILTER_DESCRIPTION_RES = "ImagePanel.FILE_FILTER_DESCRIPTION";
    private static final String LOAD_MENU_ITEM_RES = "ImagePanel.LOAD_MENU_ITEM";
    private static final String CLEAR_MENU_ITEM_RES = "ImagePanel.CLEAR_MENU_ITEM";

    /**
     * Image to be displayed
     */
    protected Image image;
    /**
     * Popup menu for image operations execution
     */
    protected JPopupMenu popup;

    {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                if (isEnabled()) {
                    getPopup().show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
    }

    /**
     * Gets image
     * @return image
     * @see #image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets image
     * @param image image
     * @see #image
     */
    public void setImage(Image image) {
        firePropertyChange(IMAGE_PROP, this.image, this.image = image);
        repaint();
    }

    /**
     * Gets popup
     * @return popup
     * @see #popup
     */
    protected JPopupMenu getPopup() {
        if (popup == null) {
            popup = new JPopupMenu();
            JMenuItem item = new JMenuItem(CommonUtils.i18n(LOAD_MENU_ITEM_RES));
            popup.add(item);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectFileAndLoad();
                }
            });
            item = new JMenuItem(CommonUtils.i18n(CLEAR_MENU_ITEM_RES));
            popup.add(item);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clear();
                }
            });

        }
        return popup;
    }

    /**
     * Empty image panel
     */
    public void clear() {
        setImage(null);
        repaint();
    }

    /**
     * Selct file and load image from it
     */
    public void selectFileAndLoad() {
        load(selectFile());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UIUtils.drawCenteredScaledImage(g, image, 0, 0, getWidth(), getHeight());

    }

    /**
     * Select file
     * @return selected file or null if canceled
     */
    private File selectFile() {
        File file = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setIgnoreRepaint(true);
        chooser.setDialogTitle(CommonUtils.i18n(SELECT_DIALOG_TITLE_RES));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter(
                CommonUtils.i18n(FILE_FILTER_DESCRIPTION_RES), "jpeg", "jpg"));
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        }
        return file;
    }

    /**
     * Load image from file
     * @param file image file
     */
    private void load(File file) {
        if ((file == null) || !file.exists()) {
            return;
        }
        try {
            setImage(ImageIO.read(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            repaint();
        }
    }

    @Override
    public void flush() {
        //Do nothing
    }
}
