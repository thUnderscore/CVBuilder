package org.thUnderscore.common.mvc.word.elements;

import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.thUnderscore.common.utils.CommonUtils;

/**
 * Element which can display list of object as repeatable set of elements. Each
 * object from list will be displayed to same set of elements
 *
 * @author thUnderscore
 */
public class ParagraphContainerRepeater extends TitledElement {

    /**
     * Definition of structure for each list object displaying
     */
    protected ParagraphContainer content;

    public ParagraphContainerRepeater(String objectPropertyName, String listPropertyName) {
        super(objectPropertyName, CommonUtils.getPropertyExpressionByName(listPropertyName));
        title = new ParagraphContentGroup();
        title.setParent(this);
        content = new ParagraphContainer() {
            {
                tableSupported = true;
            }

            @Override
            protected XWPFParagraph createParagraph() {
                return getParagraphFromParent();
            }
        };
        content.setParent(this);
    }

    /**
     * Get XWPFParagraph from parent element for displaying repeater structure
     * element. If parent is ParagraphContainer, XWPFParagraph will be created.
     * If parent is Paragraph, then its XWPFParagraph will be returned.
     *
     * @return
     */
    protected XWPFParagraph getParagraphFromParent() {
        XWPFParagraph result = null;
        BaseElement prnt = getParent();
        if (prnt != null) {
            if (prnt instanceof ParagraphContainer) {
                result = ((ParagraphContainer) prnt).createParagraph();
            } else if (prnt instanceof Paragraph) {
                result = ((Paragraph) prnt).para;
            } else if (prnt instanceof ParagraphContainerRepeater) {
                result = ((ParagraphContainerRepeater) prnt).getParagraphFromParent();
            }
        }
        return result;
    }

    /**
     * Add Run element to structure
     *
     * @param run
     * @return same element
     */
    public ParagraphContainerRepeater addRun(Run run) {
        content.addRun(run);
        return this;
    }

    /**
     * Add Image element to structure
     *
     * @param image
     * @return same element
     */
    public ParagraphContainerRepeater addImage(Image image) {
        content.addImage(image);
        return this;
    }

    /**
     * Add ParagraphContentGroup element to structure
     *
     * @param group
     * @return same element
     */
    public ParagraphContainerRepeater addGroup(ParagraphContentGroup group) {
        content.addGroup(group);
        return this;
    }

    /**
     * Add ParagraphContainerRepeater element to structure
     *
     * @param repeater
     * @return same element
     */
    public ParagraphContainerRepeater addRepeater(ParagraphContainerRepeater repeater) {
        content.addRepeater(repeater);
        return this;
    }

    /**
     * Add BaseTable inheritor to structure
     *
     * @param table
     * @return same element
     */
    public ParagraphContainerRepeater addTable(BaseTable table) {
        content.addTable(table);
        return this;
    }

    /**
     * Get list to be diplayed. Just cast value property as list
     *
     * @return objects list
     */
    protected List getList() {
        return (List) getValue();
    }

    @Override
    protected void internalWriteToWordDoc() {
        super.internalWriteToWordDoc();

        List list = getList();
        for (int i = 0; i < list.size(); i++) {
            content.setObject(list.get(i));
            content.writeToWordDoc();
        }
    }

    @Override
    public ParagraphContainerRepeater clear() {
        content.clear();
        return (ParagraphContainerRepeater) super.clear();
    }

    @Override
    protected XWPFParagraph getTitleParagraph() {
        return getParagraphFromParent();

    }

    @Override
    public ParagraphContainerRepeater setTitle(String title, boolean addBreak, FontStyle fontStyle) {
        return (ParagraphContainerRepeater) super.setTitle(title, addBreak, fontStyle);
    }

    @Override
    public ParagraphContainerRepeater setTitle(boolean enabled, String title, boolean addBreak, FontStyle fontStyle) {
        return (ParagraphContainerRepeater) super.setTitle(enabled, title, addBreak, fontStyle);
    }

    @Override
    public ParagraphContainerRepeater setTitle(String titleText, boolean addBreak) {
        return (ParagraphContainerRepeater) super.setTitle(titleText, addBreak);
    }

    @Override
    public ParagraphContainerRepeater setTitle(boolean enabled, String titleText, boolean addBreak) {
        return (ParagraphContainerRepeater) super.setTitle(enabled, titleText, addBreak);
    }

    @Override
    public ParagraphContainerRepeater setTitle(ParagraphContent titleContent, FontStyle fontStyle) {
        return (ParagraphContainerRepeater) super.setTitle(titleContent, fontStyle);
    }

    @Override
    public ParagraphContainerRepeater setTitle(String titleText) {
        return (ParagraphContainerRepeater) super.setTitle(titleText);
    }

    @Override
    public ParagraphContainerRepeater setTitle(boolean enabled, ParagraphContent titleContent, FontStyle fontStyle) {
        return (ParagraphContainerRepeater) super.setTitle(enabled, titleContent, fontStyle);
    }

    @Override
    public ParagraphContainerRepeater setTitle(String title, FontStyle fontStyle) {
        return (ParagraphContainerRepeater) super.setTitle(title, fontStyle);
    }

    @Override
    public ParagraphContainerRepeater setTitleFontStyle(FontStyle fontStyle) {
        return (ParagraphContainerRepeater) super.setTitleFontStyle(fontStyle);
    }

}
