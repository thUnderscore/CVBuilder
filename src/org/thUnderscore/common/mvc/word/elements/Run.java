package org.thUnderscore.common.mvc.word.elements;

import java.awt.Color;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * Element which can display string in word document
 *
 * @author thUnderscore
 */
public class Run extends ParagraphContent {

    /**
     * Add line break after wrote string
     */
    boolean addBreak = false;

    /**
     *
     * @param addBreak add line break after wrote string
     */
    public Run(boolean addBreak) {
        this(null, null, addBreak);
    }

    /**
     *
     * @param addBreak add line break after wrote string
     * @param value value, which string representation to be written
     */
    public Run(boolean addBreak, Object value) {
        this(addBreak);
        this.value = value;
    }

    /**
     *
     * @param valuePropertyName name of object property for value evaluation.
     * String representation of value will be written to wrod document
     * @param addBreak add line break after wrote string
     */
    public Run(String valuePropertyName, boolean addBreak) {
        this(null, valuePropertyName, addBreak);
    }

    /**
     *
     * @param objectPropertyName paren element object property name
     * @param valuePropertyName name of object property for value evaluation.
     * String representation of value will be written to wrod document
     * @param addBreak add line break after wrote string
     */
    public Run(String objectPropertyName, String valuePropertyName, boolean addBreak) {
        super(objectPropertyName, valuePropertyName);
        this.addBreak = addBreak;
    }

    /**
     * Write string to wrod document
     *
     * @param text string to be written
     * @param addBreak add break after string
     */
    protected void writeStringToDoc(String text, boolean addBreak) {
        XWPFRun run = para.createRun();
        String fontFamily = getFontFamily();
        if (fontFamily != null) {
            run.setFontFamily(fontFamily);
        }
        Integer fontSize = getFontSize();
        if (fontSize != null) {
            run.setFontSize(fontSize);
        }
        Boolean bold = isBold();
        if (bold != null) {
            run.setBold(bold);
        }
        UnderlinePatterns underline = getUnderline();
        if (underline != null) {
            run.setUnderline(underline);
        }
        Boolean italic = isItalic();
        if (italic != null) {
            run.setItalic(italic);
        }
        Boolean strike = isStrike();
        if (strike != null) {
            run.setStrike(strike);
        }
        Color color = getColor();
        if (color != null) {
            String rgb = Integer.toHexString(color.getRGB());
            rgb = rgb.substring(2, rgb.length());

            run.setColor(rgb);
        }
        if (text != null) {
            run.setText(text);
        }
        if (addBreak) {
            run.addBreak();
        }
    }

    @Override
    protected void internalWriteToWordDoc() {
        if (value != null) {
            //split value and write each line separately
            String valueStr = value.toString();
            String[] arr = valueStr.split("\n");
            for (int i = 0; i < arr.length; i++) {
                writeStringToDoc(arr[i], addBreak || (i + 1 < arr.length));
            }
        }
    }

    @Override
    public Run setFontStyle(FontStyle fontStyle) {
        return (Run) super.setFontStyle(fontStyle);
    }

    @Override
    public Run setBold(Boolean bold) {
        return (Run) super.setBold(bold);
    }

    @Override
    public Run setStrike(Boolean strike) {
        return (Run) super.setStrike(strike);
    }

    @Override
    public Run setItalic(Boolean italic) {
        return (Run) super.setItalic(italic);
    }

    @Override
    public Run setFontFamily(String fontFamily) {
        return (Run) super.setFontFamily(fontFamily);
    }

    @Override
    public Run setFontSize(Integer fontSize) {
        return (Run) super.setFontSize(fontSize);
    }

    @Override
    public Run setUnderline(UnderlinePatterns underline) {
        return (Run) super.setUnderline(underline);
    }

    @Override
    public Run setColor(Color color) {
        return (Run) super.setColor(color);
    }

    @Override
    public Run setValue(Object value) {
        return (Run) super.setValue(value);
    }

    @Override
    public Run setValueExpression(String valueExpression) {
        return (Run) super.setValueExpression(valueExpression);
    }

    @Override
    public Run setValuePropertyName(String valuePropertyName) {
        return (Run) super.setValuePropertyName(valuePropertyName);
    }

    @Override
    public Run setObject(Object object) {
        return (Run) super.setObject(object);
    }

    @Override
    public Run setObjectPropertyName(String objectPropertyName) {
        return (Run) super.setObjectPropertyName(objectPropertyName);
    }

}
