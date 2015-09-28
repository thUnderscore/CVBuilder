package org.thUnderscore.common.mvc.word.elements;

import java.awt.Color;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 * Element which represent root entity - MS Word document
 * @author thUnderscore
 */
public class Document extends ParagraphContainer {

    {
        //Set default font params
        tableSupported = true;
        setFontFamily(TIMES_NEW_ROMAN);
        setFontSize(10);
        setBold(Boolean.FALSE);
        setUnderline(UnderlinePatterns.NONE);
        setItalic(Boolean.FALSE);
        setStrike(Boolean.FALSE);
    }

    /**
     * POI object, which represent MS Word document
     */
    XWPFDocument wordDoc;

    
    /**
     * Display Document to XWPFDocument
     * @param wordDoc 
     */
    public void writeToWordDoc(XWPFDocument wordDoc) {
        this.wordDoc = wordDoc;
        writeToWordDoc();
    }
    
    @Override
    public Document addTable(BaseTable table) {
        super.add(table);
        return this;
    }

    @Override
    public Document addParagraph(Paragraph paragraph) {
        super.addParagraph(paragraph);
        return this;
    }    

    @Override
    public Document addRun(Run run) {
        return (Document)super.addRun(run);
    }

    @Override
    public Document addImage(Image image) {
        return (Document)super.addImage(image);
    }

    @Override
    public Document addGroup(ParagraphContentGroup group) {
        return (Document)super.addGroup(group);
    }

    @Override
    public Document addRepeater(ParagraphContainerRepeater repeater){
       return (Document) super.addRepeater(repeater);
    }
    
    @Override
    public Document setFontStyle(FontStyle fontStyle) {
        return (Document)super.setFontStyle(fontStyle);
    }
    
    @Override
    public Document setBold(Boolean bold) {
        return (Document)super.setBold(bold); 
    }

    @Override
    public Document setStrike(Boolean strike) {
        return (Document)super.setStrike(strike); 
    }

    @Override
    public Document setItalic(Boolean italic) {
        return (Document)super.setItalic(italic);
    }
    
    @Override
    public Document setFontFamily(String fontFamily) {
        return (Document)super.setFontFamily(fontFamily); 
    }

    @Override
    public Document setFontSize(Integer fontSize) {
        return (Document)super.setFontSize(fontSize); 
    }

    @Override
    public Document setUnderline(UnderlinePatterns underline) {
        return (Document)super.setUnderline(underline);
    }

    @Override
    public Document setColor(Color color) {
        return (Document)super.setColor(color);
    }

    @Override
    protected XWPFParagraph createParagraph() {
        return wordDoc.createParagraph();
    }

    @Override
    protected void afterWrite() {
        super.afterWrite(); 
        wordDoc = null;
    }

    
    @Override
    protected void internalWriteToWordDoc() {
        super.internalWriteToWordDoc(); 
        
    }
    
    @Override
    protected XWPFDocument getDocument() {
        return wordDoc;
    }

}
