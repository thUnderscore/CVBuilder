package org.thUnderscore.common.mvc.word;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import javax.imageio.ImageIO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.thUnderscore.common.utils.UIUtils;
import org.thUnderscore.common.mvc.word.elements.BaseElement;

/**
 *
 * @author thUnderscore
 */
public class POIUtils {

    /**
     * Instance of BaseElement for FontStyle creation (FontStyle is internal
     * class)
     */
    private static final BaseElement FONT_INITIALIZER = new BaseElement(null);

    /**
     * Create FontStyle instance
     *
     * @return created FontStyle instance
     */
    public static BaseElement.FontStyle createFontStyle() {
        return FONT_INITIALIZER.createFontStyle();
    }

    /**
     * Create FontStyle instance and copy properties
     *
     * @param fontStyle source for properies copying
     * @return created FontStyle instance
     */
    public static BaseElement.FontStyle createFontStyle(BaseElement.FontStyle fontStyle) {
        return FONT_INITIALIZER.createFontStyle(fontStyle);
    }

    /**
     * Hide table borders
     *
     * @param table
     * @param size size for internal borders
     * @param space space for internal borders
     */
    public static void setTableInvisible(XWPFTable table, int size, int space) {
        table.setInsideVBorder(XWPFTable.XWPFBorderType.NONE, size, space, "1C7331");
        table.setInsideHBorder(XWPFTable.XWPFBorderType.NONE, size, space, "1C7331");
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        tblPr.getTblBorders().addNewBottom().setNil();
        tblPr.getTblBorders().addNewTop().setNil();
        tblPr.getTblBorders().addNewLeft().setNil();
        tblPr.getTblBorders().addNewRight().setNil();
    }

    /**
     * Create picture object
     *
     * @param run XWPFRun where it will be placed
     * @param stream stream, which contains image data
     * @param format image format
     * @param width image width in doc
     * @param height image height in doc
     */
    public static void createPicture(XWPFRun run, InputStream stream, int format, int width, int height) {

        int id;
        String blipId;
        XWPFDocument document = run.getDocument();
        try {
            id = document.getNextPicNameNumber(format);
            blipId = document.addPictureData(stream, format);
        } catch (InvalidFormatException ex) {
            throw new RuntimeException(ex);
        }

        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        CTInline inline = run.getCTR().addNewDrawing().addNewInline();

        String picXml = ""
                + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
                + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                + "         <pic:nvPicPr>"
                + "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>"
                + "            <pic:cNvPicPr/>"
                + "         </pic:nvPicPr>"
                + "         <pic:blipFill>"
                + "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
                + "            <a:stretch>"
                + "               <a:fillRect/>"
                + "            </a:stretch>"
                + "         </pic:blipFill>"
                + "         <pic:spPr>"
                + "            <a:xfrm>"
                + "               <a:off x=\"0\" y=\"0\"/>"
                + "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>"
                + "            </a:xfrm>"
                + "            <a:prstGeom prst=\"rect\">"
                + "               <a:avLst/>"
                + "            </a:prstGeom>"
                + "         </pic:spPr>"
                + "      </pic:pic>"
                + "   </a:graphicData>"
                + "</a:graphic>";

        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException ex) {
            throw new RuntimeException(ex);
        }
        inline.set(xmlToken);
        //graphicData.set(xmlToken);

        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }

    /**
     * Create picture object
     *
     * @param run XWPFRun where it will be placed
     * @param image
     * @param width image width in doc
     * @param height image height in doc
     */
    public static void createPicture(XWPFRun run, Image image, int width, int height) {

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                BufferedImage scaledImage = UIUtils.scaleImageToFit(image, width, height,
                        BufferedImage.TYPE_INT_RGB);
                ImageIO.write(scaledImage, "jpeg", os);
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                createPicture(run, is, XWPFDocument.PICTURE_TYPE_JPEG,
                        scaledImage.getWidth(), scaledImage.getHeight());
            } finally {
                os.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Create table and set its borders invisible
     *
     * @param doc document
     * @param rows rows count
     * @param cols columns count
     * @param size internal borders size
     * @param space internal borders space
     * @return
     */
    public static XWPFTable createInvisibleTable(XWPFDocument doc, int rows, int cols,
            int size, int space) {
        XWPFTable table = doc.createTable(rows, cols);
        POIUtils.setTableInvisible(table, size, space);
        return table;
    }

    /**
     * Set table row height
     *
     * @param row row number
     * @param height height
     */
    public static void setTableRowHeight(XWPFTableRow row, int height) {
        CTTrPr trPr = row.getCtRow().addNewTrPr();
        CTHeight ht = trPr.addNewTrHeight();
        ht.setVal(BigInteger.valueOf(height));
    }

    /**
     * Get cell VerticalJc
     *
     * @param cell
     * @return
     */
    public static CTVerticalJc getCTVerticalJc(XWPFTableCell cell) {
        CTTcPr tcpr = getCTTcPr(cell);
        return (tcpr.getVAlign() == null) ? tcpr.addNewVAlign() : tcpr.getVAlign();
    }

    /**
     * Get cell TcW
     *
     * @param cell
     * @return
     */
    public static CTTblWidth getCTTblWidth(XWPFTableCell cell) {
        CTTcPr tcpr = getCTTcPr(cell);
        return (tcpr.getTcW() == null) ? tcpr.addNewTcW() : tcpr.getTcW();
    }

    /**
     * Get cell CTTcPr
     *
     * @param cell
     * @return
     */
    public static CTTcPr getCTTcPr(XWPFTableCell cell) {
        CTTc ctTc = cell.getCTTc();
        return (ctTc.getTcPr() == null) ? cell.getCTTc().addNewTcPr() : ctTc.getTcPr();
    }

    /**
     * Set cell vertical align
     *
     * @param cell
     * @param align
     */
    public static void setTableCellVAlign(XWPFTableCell cell, STVerticalJc.Enum align) {
        getCTVerticalJc(cell).setVal(align);
    }

    /**
     * Set cell width
     *
     * @param cell
     * @param width
     */
    public static void setTableCellWidth(XWPFTableCell cell, int width) {
        getCTTblWidth(cell).setW(BigInteger.valueOf(width));
    }

    /**
     * Create XWPFRun
     *
     * @param para paragraph, contains created run
     * @param text
     * @param fontFamily
     * @param fontSize
     * @param bold
     * @param addBreak
     * @param underline
     * @return
     */
    public static XWPFRun drawRun(XWPFParagraph para, String text, String fontFamily,
            int fontSize, boolean bold, boolean addBreak, UnderlinePatterns underline) {
        return drawRun(para, text, fontFamily, fontSize, bold, addBreak, underline, false, false);
    }

    /**
     * Create XWPFRun
     *
     * @param para paragraph, contains created run
     * @param text
     * @param fontFamily
     * @param fontSize
     * @param bold
     * @param addBreak
     * @return
     */
    public static XWPFRun drawRun(XWPFParagraph para, String text, String fontFamily,
            int fontSize, boolean bold, boolean addBreak) {
        return drawRun(para, text, fontFamily, fontSize, bold, addBreak, UnderlinePatterns.NONE);
    }

    /**
     * Create XWPFRun
     *
     * @param para paragraph, contains created run
     * @param text
     * @param fontFamily
     * @param fontSize
     * @param bold
     * @param addBreak
     * @param underline
     * @param italic
     * @param strike
     * @return
     */
    public static XWPFRun drawRun(XWPFParagraph para, String text, String fontFamily,
            int fontSize, boolean bold, boolean addBreak, UnderlinePatterns underline,
            boolean italic, boolean strike) {
        XWPFRun rh = para.createRun();
        rh.setFontSize(fontSize);
        rh.setBold(bold);
        rh.setUnderline(underline);
        rh.setItalic(italic);
        rh.setStrike(strike);
        rh.setText(text);
        rh.setFontFamily(fontFamily);
        if (addBreak) {
            rh.addBreak();
        }
        return rh;
    }

    /**
     * Create XWPFRun
     *
     * @param cell cell contains created run
     * @param text
     * @param fontFamily
     * @param fontSize
     * @param bold
     * @param addBreak
     * @param underline
     * @param italic
     * @param strike
     * @return
     */
    public static XWPFRun drawRun(XWPFTableCell cell, String text, String fontFamily,
            int fontSize, boolean bold, boolean addBreak, UnderlinePatterns underline,
            boolean italic, boolean strike) {
        XWPFParagraph para = cell.getParagraphs().get(0);
        return drawRun(para, text, fontFamily, fontSize, bold, addBreak,
                underline, italic, strike);
    }

}
