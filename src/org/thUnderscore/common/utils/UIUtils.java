package org.thUnderscore.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.thUnderscore.common.ui.GlassPane;

/**
 * UI utils
 *
 * @author tosh
 */
public class UIUtils {

    /**
     * Color hint, drawed in empty controls
     */
    public static final Color COMPONENT_HINT_COLOR = Color.LIGHT_GRAY;
    /**
     * Color hint, drawed in empty controls id controls are required
     */
    public static final Color REQUIRED_COMPONENT_HINT_COLOR = Color.PINK;
    /**
     * Selected table row color
     */
    public static final Color TABLE_SELECTED_COLOR = new Color(236, 250, 253);
    /**
     * Selected text color for components
     */
    public static final Color TEXT_SELECTED_COLOR = new Color(200, 240, 249);

    /**
     * Color for text\shape component which is under mouse
     */
    public static final Color OVER_COLOR = new Color(122, 122, 122);
    /**
     * Color for text\shape component which is not under mouse
     */
    public static final Color OUT_COLOR = new Color(162, 162, 162);

    /**
     * Glass pane for main frame
     */
    protected static GlassPane glassPane = new GlassPane();
    /**
     * Main app frame
     */
    protected static JFrame mainFrame;

    /**
     * Gets main frame
     * @return JFrame instance
     */
    public static JFrame getMainFrame() {
        return mainFrame;
    }

    
    
    /**
     * Show fraim and set it as main app fraim
     *
     * @param center if true frame wiil by centre on screen
     * @param resizable if true frame wiil be resizable
     * @param frame frame to be showed
     */
    public static void showMainFrame(boolean center, boolean resizable, JFrame frame) {
        mainFrame = frame;
        frame.setResizable(false);
        if (center) {
            frame.setLocationRelativeTo(null);
        }
        frame.pack();
        frame.setGlassPane(glassPane);
        frame.setVisible(true);
        glassPane.setVisible(true);

    }

    /**
     * Start ripples effect
     *
     * @param x x center coordinate
     * @param y y center coordinate
     * @param color ripples color
     */
    public static void startRipplesEffect(int x, int y, Color color) {
        glassPane.startRipplesEffect(x, y, color);
    }

    /**
     * Start ripples effect
     *
     * @param component component in which center effect should be started
     * @param color ripples color
     */
    public static void startRipplesEffect(JComponent component, Color color) {
        if (component != null) {
            Point p = component.getLocationOnScreen();
            startRipplesEffect(p.x + component.getWidth() / 2,
                    p.y + component.getHeight() / 2, color);

        }
    }

    /**
     * Get component border insets
     *
     * @param component
     * @return border insets. if border is null - new Insets(2, 2, 2, 2)
     */
    public static Insets getComponentInsets(JComponent component) {
        Insets insets;
        if (component.getBorder() == null) {
            insets = new Insets(2, 2, 2, 2);
        } else {
            insets = component.getBorder().getBorderInsets(component);
        }
        return insets;
    }

    /**
     * Draw image fitted in rectangle
     *
     * @param g Graphics for drawing
     * @param image image to be drawn
     * @param left most left rectangle point on g
     * @param top most top rectangle point on g
     * @param width rectangle width
     * @param height rectangle height
     */
    public static void drawCenteredScaledImage(Graphics g, Image image,
            int left, int top, int width, int height) {
        if (image == null) {
            return;
        }
        int imageHeight = image.getHeight(null);
        int imageWidth = image.getWidth(null);
        if ((imageHeight < 1) || (imageWidth < 1) || (height < 1) || (width < 1)) {
            return;
        }
        int x;
        int y;
        Dimension size = getSacaledImageSizeToFit(imageWidth, imageHeight, width, height);
        x = (width - size.width) / 2;
        y = (height - size.height) / 2;
        g.drawImage(image, left + x, top + y, size.width, size.height, null);
    }

    /**
     * Get image size after resizing to be fitted
     *
     * @param imageWidth
     * @param imageHeight
     * @param canvaWidth
     * @param canvaHeight
     * @return fitted image size as Dimension
     */
    public static Dimension getSacaledImageSizeToFit(int imageWidth, int imageHeight,
            int canvaWidth, int canvaHeight) {
        int scaledWidth;
        int scaledHeight;
        if ((float) canvaHeight / canvaWidth > (float) imageHeight / imageWidth) {
            scaledWidth = canvaWidth;
            scaledHeight = scaledWidth * imageHeight / imageWidth;
        } else {
            scaledHeight = canvaHeight;
            scaledWidth = scaledHeight * imageWidth / imageHeight;
        }
        return new Dimension(scaledWidth, scaledHeight);
    }

    /**
     * Draw string in rectangle
     *
     * @param g Graphics for drawing
     * @param text string to be written
     * @param left most left rectangle point on g
     * @param top most top rectangle point on g
     * @param width rectangle width
     * @param height rectangle height
     * @param font text font
     * @param color text colot
     * @param centerHorizontal if true - centre text horizontally
     * @param centerVertical if true - centre text vertically
     */
    public static void drawText(Graphics g, String text,
            int left, int top, int width, int height, Font font, Color color,
            boolean centerHorizontal, boolean centerVertical) {
        if (text == null) {
            return;
        }
        if (font != null) {
            g.setFont(font);
        }
        if (color != null) {
            g.setColor(color);
        }

        FontMetrics fontMetrics = g.getFontMetrics();

        int stringHeight = fontMetrics.getHeight();
        g.setClip(left, top, width, height);

        if (centerHorizontal) {
            int stringWidth = fontMetrics.stringWidth(text);
            if (stringWidth < width) {
                left = left + (width - stringWidth) / 2;
            }
        }
        if (centerVertical) {
            top = top + ((height - stringHeight) / 2);
        }

        g.drawString(text, left, top + stringHeight);
    }

    /**
     * Convert Icon to Image or cast if it's ImageIcon
     *
     * @param icon source Icon
     * @return Image result
     */
    public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge
                    = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            try {
                icon.paintIcon(null, g, 0, 0);
            } finally {
                g.dispose();
            }
            return image;
        }
    }

    /**
     * Scale image to fit size
     * @param image image to be scaled
     * @param width width to be fitted
     * @param height height to be fitted
     * @param imageType  type of the result image
     * @return scaled image
     * @see BufferedImage#BufferedImage(int, int, int)
     */
    public static BufferedImage scaleImageToFit(Image image, int width, int height,
            int imageType) {
        if (image == null) {
            return null;
        }
        Dimension size = getSacaledImageSizeToFit(image.getWidth(null),
                image.getHeight(null), width, height);

        BufferedImage resizedImage = new BufferedImage(size.width, size.height, imageType);
        Graphics2D g = resizedImage.createGraphics();
        try {

            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(image, 0, 0,
                    size.width, size.height, null);
        } finally {
            g.dispose();
        }

        return resizedImage;

    }

    /**
     * Scale icon to fit size
     * @param icon icon to be scaled
     * @param width width to be fitted
     * @param height height to be fitted
     * @param imageType type of the result image
     * @return scaled image
     * @see BufferedImage#BufferedImage(int, int, int)
     */
    public static BufferedImage scaleImageToFit(ImageIcon icon, 
            int width, int height, int imageType){        
        if (icon == null) {
            return null;
        }
        return scaleImageToFit(icon.getImage(), width, height, imageType)  ;
    }

    /**
     * Create JButton
     *
     * @param text button text
     * @param toolTip button tooltip
     * @param imageResourceName base resource name. "Normal.png" "Rollover.png"
     * "Pressed.png" "Disabled.png" will be addded to it for appropriate type of
     * image
     * @param actionListener button listener
     * @return created button
     */
    public static JButton createButton(String text, String toolTip,
            String imageResourceName, ActionListener actionListener) {
        JButton button = new JButton() {
            @Override
            public String getActionCommand() {
                String command = getName();
                if (CommonUtils.isEmpty(command)) {
                    command = super.getActionCommand();
                }
                return command;
            }
        };
        button.setText(text);
        button.setToolTipText(toolTip);
        button.setRolloverEnabled(true);
        button.setIcon(CommonUtils.safeLoadIconResource(getButtonNormalIconRes(imageResourceName)));
        button.setRolloverIcon(CommonUtils.safeLoadIconResource(getButtonRolloverIconRes(imageResourceName)));
        button.setPressedIcon(CommonUtils.safeLoadIconResource(getButtonPressedIconRes(imageResourceName)));
        button.setDisabledIcon(CommonUtils.safeLoadIconResource(getButtonDisabledIconRes(imageResourceName)));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    /**
     * Gets button normal icon res name by base resource name
     *
     * @param imageResourceName base resource name
     * @return normal icon resource name
     */
    public static String getButtonNormalIconRes(String imageResourceName) {
        return imageResourceName + "Normal.png";
    }

    /**
     * Gets button rollover icon res name by base resource name
     *
     * @param imageResourceName base resource name
     * @return rollover icon resource name
     */
    public static String getButtonRolloverIconRes(String imageResourceName) {
        return imageResourceName + "Rollover.png";
    }

    /**
     * Gets button pressed icon res name by base resource name
     *
     * @param imageResourceName base resource name
     * @return pressed icon resource name
     */
    public static String getButtonPressedIconRes(String imageResourceName) {
        return imageResourceName + "Pressed.png";
    }

    /**
     * Gets button disabled icon res name by base resource name
     *
     * @param imageResourceName base resource name
     * @return disabled icon resource name
     */
    public static String getButtonDisabledIconRes(String imageResourceName) {
        return imageResourceName + "Disabled.png";
    }
}

