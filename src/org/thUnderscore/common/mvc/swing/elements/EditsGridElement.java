package org.thUnderscore.common.mvc.swing.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComponent;
import javax.swing.LayoutStyle;

/**
 * Element which can display grid of edits and labels. Consist of columns. Each
 * column in each row can has label and edit. Edit can display object property,
 * label usually contains property title
 *
 * @author thUnderscore
 */
public class EditsGridElement extends PanelElement {

    /**
     * Gap for title border
     */
    public static final int TITLE_BORDER_GAP = 6;
    /**
     * Default gap
     */
    public static final int DEFAULT_GAP = TITLE_BORDER_GAP;

    /**
     * Suffix for edit component name
     */
    public static final String EDIT_SUFFIX = "Edit";
    /**
     * Suffix for label component
     */
    public static final String LABEL_SUFFIX = "Label";
    /**
     * Default edit width
     */
    public static final int DEF_EDIT_WIDTH = GroupLayout.DEFAULT_SIZE;
    /**
     * Default label width
     */
    public static final int DEF_LABEL_WIDTH = GroupLayout.DEFAULT_SIZE;
    /**
     * default edit height
     */
    public static final int DEF_HEIGHT = 25;
    /**
     * Row height. if < 0, then undefined and default value will be used
     */
    protected int rowHeight = -1;
    /**
     * Edits width. if < 0, then undefined and default value will be used
     */
    protected int colEditWidth = -1;
    /**
     * Label width. if < 0, then undefined and default value will be used
     */
    protected int colLabelWidth = -1;

    /**
     * Class represent column of grid/ Colum has in each row label and edit
     */
    public class Column {

        /**
         * @see #isShowLabel()
         */
        public boolean showLabel = true;

        /**
         * Gets should label be created
         *
         * @return the value of showLabel
         */
        public boolean isShowLabel() {
            return showLabel;
        }

        /**
         * Sets should label be created
         *
         * @param showLabel new value of showLabel
         */
        public void setShowLabel(boolean showLabel) {
            this.showLabel = showLabel;
        }
    }

    /**
     * Instance of this clas define where PropertyElement and LabelElement
     * should be displayed in grid
     */
    protected static class Edit {

        /**
         * @see #getTitle()
         */
        private String title;
        /**
         * @see #getElementName()
         */
        private final String elementName;
        /**
         * @see #getEditElement()
         */
        private final PropertyElement editElement;
        /**
         * @see #getLabelElement()
         */
        private final LabelElement labelElement;
        /**
         * column number
         */
        private final int col;
        /**
         * row number
         */
        private final int row;

        /**
         *
         * @param elementName PropertyElement name
         * @param title object property title
         * @param col column number in grid
         * @param row row number in grid
         * @param editElement element which represent object property
         */
        public Edit(String elementName, String title, int col, int row,
                PropertyElement editElement) {
            this.title = title;
            this.elementName = elementName;
            this.editElement = editElement;
            this.col = col;
            this.row = row;
            labelElement = new LabelElement(getLabelName(elementName), title);
        }

        /**
         * Gets object property title
         *
         * @return
         */
        public String getTitle() {
            return title;
        }

        /**
         * Sets object property title
         *
         * @param title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * Gets PropertyElement name
         *
         * @return
         */
        public String getElementName() {
            return elementName;
        }

        /**
         * Gets element which represent object property
         *
         * @return element
         */
        public PropertyElement getEditElement() {
            return editElement;
        }

        /**
         * Gets element which display title of property
         *
         * @return label element
         */
        public LabelElement getLabelElement() {
            return labelElement;
        }
    }

    /**
     * List of Edit, which displayed in this grid
     *
     * @see Edit
     */
    protected List<Edit> list = new ArrayList<Edit>();
    /**
     * Columns definition. It is created and filled during components creation
     */
    protected Column[] columns;
    /**
     * Array of Edit, where objects located according to it row and col
     * properies. It is created and filled during components creation
     *
     * @see Edit#row
     * @see Edit#col
     */
    protected Edit[][] cells;
    /**
     * Root vertical group
     *
     * @see GroupLayout
     */
    protected Group panelRootVerticalGroup;
    /**
     * Root horizontal group
     *
     * @see GroupLayout
     */
    protected Group panelRootHorizontal;

    /**
     * Rows heights, can be defined for each row. Key - row number value - row
     * height
     */
    protected Map<Integer, Integer> heights;
    /**
     * Columns edit widths, can be defined for each column. Key - col number
     * value - edit width
     */
    protected Map<Integer, Integer> editWidths;
    /**
     * Columns label widths, can be defined for each column. Key - col number
     * value - label width
     */
    protected Map<Integer, Integer> labelWidths;

    /**
     * @see #getGapTop()
     */
    int gapTop;
    /**
     * @see #getGapBottom()
     */
    int gapBottom;
    /**
     * @see #getGapLeft()
     */
    int gapLeft;
    /**
     * @see #getGapRight()
     */
    int gapRight;

    /**
     *
     * @param name Element name
     */
    public EditsGridElement(String name) {
        super(name);
    }

    public EditsGridElement() {
        this(null);
    }

    /**
     * Add text element
     *
     * @param elementName element (object property) name
     * @param title property title
     * @param col col number in grid
     * @param row row number in grid
     * @return same grid element
     */
    public EditsGridElement addTextElementDescription(String elementName,
            String title, int col, int row) {
        return addTextElementDescription(elementName, title, null, false, col, row);
    }

    /**
     * Add text element
     *
     * @param elementName element (object property) name
     * @param title property title
     * @param hintText proprtye hint
     * @param isRequired is property required
     * @param col col number in grid
     * @param row row number in grid
     * @return same grid element
     */
    public EditsGridElement addTextElementDescription(String elementName,
            String title, String hintText, boolean isRequired, int col, int row) {
        TextFieldElement textFieldElement
                = new TextFieldElement(getEditName(elementName), elementName,
                        hintText, isRequired);
        addElementDescription(elementName, title, col, row, textFieldElement);
        return this;
    }

    /**
     * Add text element
     *
     * @param elementName element (object property) name
     * @param title property title
     * @param col col number in grid
     * @param row row number in grid
     * @param element element which can represent object property
     * @return same grid element
     */
    public EditsGridElement addElementDescription(String elementName,
            String title, int col, int row, PropertyElement element) {
        list.add(new Edit(elementName, title, col, row, element));
        return this;
    }

    /**
     * Gets name for edit element by property name
     *
     * @param elementName element (property) name
     * @return
     */
    public static String getEditName(String elementName) {
        return elementName + EDIT_SUFFIX;
    }

    /**
     * Gets name for label element by property name
     *
     * @param elementName element (property) name
     * @return
     */
    public static String getLabelName(String elementName) {
        return elementName + LABEL_SUFFIX;
    }

    @Override
    public void createComponent() {
        super.createComponent();
        for (int i = 0; i < list.size(); i++) {
            Edit edit = list.get(i);
            view.addElement(edit.getEditElement());
            view.addElement(edit.getLabelElement());
        }
    }

    @Override
    public void setObject(Object object) {
        super.setObject(object);
    }

    /**
     * Init internal fields before placement components in grid
     */
    public void init() {
        int maxCol = -1;
        int maxRow = -1;

        for (int i = 0; i < list.size(); i++) {
            Edit edit = list.get(i);
            if (edit.row > maxRow) {
                maxRow = edit.row;
            }
            if (edit.col > maxCol) {
                maxCol = edit.col;
            }
        }
        maxRow++;
        maxCol++;

        columns = new Column[maxCol];
        cells = new Edit[maxRow][maxCol];

        for (int i = 0; i < list.size(); i++) {
            Edit edit = list.get(i);
            setEdit(edit.row, edit.col, edit);
        }
    }

    /**
     * Set Edit in grid
     *
     * @param row
     * @param col
     * @param edit
     */
    public void setEdit(int row, int col, Edit edit) {
        cells[row][col] = edit;
    }

    /**
     * Get Edit from grid
     *
     * @param row
     * @param column
     * @return
     */
    public Edit getEdit(int row, int column) {
        return cells[row][column];
    }

    /**
     * Gets does column has labels
     *
     * @param index
     * @return
     */
    public boolean isColumnLabeled(int index) {
        return ((columns[index] == null) || columns[index].isShowLabel());
    }

    @Override
    public void placeComponents(JComponent container) {
        super.placeComponents(container);
        init();
        GroupLayout layout = new GroupLayout(getComponent());
        getComponent().setLayout(layout);
        createPanelRootGroup(layout);
        layout.setHorizontalGroup(panelRootHorizontal);
        layout.setVerticalGroup(panelRootVerticalGroup);
        fillGroupHorizontal(panelRootHorizontal, layout);
        fillGroupVertical(panelRootVerticalGroup, layout);
    }

    @Override
    public BaseElement setBorder(boolean hasBorder, String title) {
        super.setBorder(hasBorder, title);
        if (this.hasBorder) {
            if (gapLeft == 0) {
                setGapLeft(TITLE_BORDER_GAP);
            }
            if (gapRight == 0) {
                setGapRight(TITLE_BORDER_GAP);
            }
            if (gapTop == 0) {
                setGapTop(TITLE_BORDER_GAP);
            }
            if (gapBottom == 0) {
                setGapBottom(TITLE_BORDER_GAP);
            }
        }
        return this;

    }

    /**
     * Create root groups
     *
     * @param layout
     * @see GroupLayout
     */
    protected void createPanelRootGroup(GroupLayout layout) {
        panelRootHorizontal = layout.createSequentialGroup();
        panelRootVerticalGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
    }

    /**
     * Fill horizontal root group
     *
     * @param parentGroup
     * @param layout
     * @see GroupLayout
     */
    public void fillGroupHorizontal(Group parentGroup, GroupLayout layout) {
        Group group = parentGroup;
        SequentialGroup root = layout.createSequentialGroup();
        group.addGroup(root);
        int size;
        if (gapLeft > 0) {
            root.addGap(gapLeft);
        }
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                root.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
            }
            ParallelGroup labelsGroup = null;
            if (isColumnLabeled(i)) {
                labelsGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
                root.addGroup(labelsGroup).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
            }
            ParallelGroup controlsGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            root.addGroup(controlsGroup);
            for (int j = 0; j < cells.length; j++) {
                Edit cell = getEdit(j, i);
                if (cell == null) {
                    continue;
                }
                if ((labelsGroup != null) && (cell.getLabelElement() != null)) {
                    size = getColLabelWidth(i);
                    labelsGroup.addComponent(cell.getLabelElement().getComponentForPlacement(), size, size, size);
                }
                if (cell.getEditElement() != null) {
                    size = getColEditWidth(i);
                    controlsGroup.addComponent(cell.getEditElement().getComponentForPlacement(),
                            size, 0, Short.MAX_VALUE);
                }
            }
        }
        if (gapRight > 0) {
            root.addGap(gapRight);
        }
    }

    /**
     * Fill vertival root group
     *
     * @param parentGroup
     * @param layout
     * @see GroupLayout
     */
    public void fillGroupVertical(Group parentGroup, GroupLayout layout) {
        Group group = parentGroup;
        SequentialGroup root = layout.createSequentialGroup();
        group.addGroup(root);
        int size;
        if (gapTop > 0) {
            root.addGap(gapTop);
        }
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) {
                root.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
            }
            ParallelGroup controlsGroup = layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.CENTER);
            root.addGroup(controlsGroup);
            for (int j = 0; j < columns.length; j++) {
                Edit cell = getEdit(i, j);
                if (cell == null) {
                    continue;
                }
                size = getRowHeight(i);
                if (isColumnLabeled(j) && (cell.getLabelElement() != null)) {
                    controlsGroup.addComponent(cell.getLabelElement().getComponentForPlacement(),
                            size, size, size);
                }
                if (cell.getEditElement() != null) {
                    controlsGroup.addComponent(cell.getEditElement().getComponentForPlacement(),
                            size, size, size
                    );
                }
            }
        }
        if (gapBottom > 0) {
            root.addGap(gapBottom);
        }
    }

    /**
     * Get column edit component width
     *
     * @param col column number
     * @return width
     */
    public int getColEditWidth(Integer col) {
        return colEditWidth >= 0 ? colEditWidth
                : (editWidths != null) && editWidths.containsKey(col)
                ? editWidths.get(col) : DEF_EDIT_WIDTH;
    }

    /**
     * Get column label component width
     *
     * @param col
     * @return width
     */
    public int getColLabelWidth(Integer col) {
        return colLabelWidth >= 0 ? colLabelWidth
                : (labelWidths != null) && labelWidths.containsKey(col) ? labelWidths.get(col) : DEF_LABEL_WIDTH;
    }

    /**
     * Get row height
     *
     * @param row
     * @return height
     */
    public int getRowHeight(Integer row) {
        return rowHeight >= 0 ? rowHeight
                : (heights != null) && heights.containsKey(row) ? heights.get(row) : DEF_HEIGHT;
    }

    /**
     * Set column edit component width
     *
     * @param col column number
     * @param size
     * @return same grid element
     */
    public EditsGridElement setColEditWidth(Integer col, int size) {
        if (editWidths == null) {
            editWidths = new HashMap<Integer, Integer>();
        }
        editWidths.put(col, size);
        return this;
    }

    /**
     * Set column label component width
     *
     * @param col
     * @param size
     * @return same grid element
     */
    public EditsGridElement setColLabelWidth(Integer col, int size) {
        if (labelWidths == null) {
            labelWidths = new HashMap<Integer, Integer>();
        }
        labelWidths.put(col, size);
        return this;
    }

    /**
     * Set row height
     *
     * @param row
     * @param size
     * @return same grid element
     */
    public EditsGridElement setRowHeight(Integer row, int size) {
        if (heights == null) {
            heights = new HashMap<Integer, Integer>();
        }
        heights.put(row, size);
        return this;
    }

    /**
     * Set columns edit component width
     *
     * @param sizes array of width. index in array - number of column
     * @return same grid element
     */
    public EditsGridElement setColEditWidth(int[] sizes) {
        if (editWidths == null) {
            editWidths = new HashMap<Integer, Integer>();
        }
        for (int i = 0; i < sizes.length; i++) {
            editWidths.put(i, sizes[i]);
        }
        return this;
    }

    /**
     * Set columns edit component width
     *
     * @param sizes array of width. index in array - number of column
     * @return same grid element
     */
    public EditsGridElement setColLabelWidth(int[] sizes) {
        if (labelWidths == null) {
            labelWidths = new HashMap<Integer, Integer>();
        }
        for (int i = 0; i < sizes.length; i++) {
            labelWidths.put(i, sizes[i]);
        }
        return this;
    }

    /**
     * Set rows height
     *
     * @param sizes array of height. index in array - number of row
     * @return same grid element
     */
    public EditsGridElement setRowHeight(int[] sizes) {
        if (heights == null) {
            heights = new HashMap<Integer, Integer>();
        }
        for (int i = 0; i < sizes.length; i++) {
            heights.put(i, sizes[i]);
        }
        return this;
    }

    /**
     * Sets default width of labels
     *
     * @param size
     * @return same grid element
     */
    public EditsGridElement setColLabelWidth(int size) {
        colLabelWidth = size;
        return this;
    }

    /**
     * Sets default width of edits
     *
     * @param size
     * @return same grid element
     */
    public EditsGridElement setColEditWidth(int size) {
        colEditWidth = size;
        return this;
    }

    /**
     * Sets default height of rows
     *
     * @param size
     * @return same grid element
     */
    public EditsGridElement setRowHeight(int size) {
        rowHeight = size;
        return this;
    }

    /**
     * Gets default rows heigtht
     *
     * @return
     */
    public int getRowHeight() {
        return rowHeight;
    }

    /**
     * Gets default width of edits
     *
     * @return
     */
    public int getColEditWidth() {
        return colEditWidth;
    }

    /**
     * Gets default width of labels
     *
     * @return
     */
    public int getColLabelWidth() {
        return colLabelWidth;
    }

    /**
     * Gets gap size at the top of grid
     *
     * @return
     */
    public int getGapTop() {
        return gapTop;
    }

    /**
     * Sets gap size at the top of grid
     *
     * @param gapTop
     * @return same grid element
     */
    public EditsGridElement setGapTop(int gapTop) {
        this.gapTop = gapTop;
        return this;
    }

    /**
     * Sets default gap size at the top of grid
     *
     * @return same grid element
     */
    public EditsGridElement setGapTop() {
        return setGapTop(DEFAULT_GAP);
    }

    /**
     * Gets gap size at the bottom of grid
     *
     * @return
     */
    public int getGapBottom() {
        return gapBottom;
    }

    /**
     * Sets gap size at the bottom of grid
     *
     * @param gapBottom
     * @return same grid element
     */
    public EditsGridElement setGapBottom(int gapBottom) {
        this.gapBottom = gapBottom;
        return this;
    }

    /**
     * Sets default gap size at the bottom of grid
     *
     * @return same grid element
     */
    public EditsGridElement setGapBottom() {
        return setGapBottom(DEFAULT_GAP);
    }

    /**
     * Gets gap size at the left of grid
     *
     * @return
     */
    public int getGapLeft() {
        return gapLeft;
    }

    /**
     * Sets gap size at the left of grid
     *
     * @param gapLeft
     * @return same grid element
     */
    public EditsGridElement setGapLeft(int gapLeft) {
        this.gapLeft = gapLeft;
        return this;
    }

    /**
     * Sets default gap size at the left of grid
     *
     * @return same grid element
     */
    public EditsGridElement setGapLeft() {
        return setGapLeft(DEFAULT_GAP);
    }

    /**
     * Gets gap size at the right of grid
     *
     * @return
     */
    public int getGapRight() {
        return gapRight;
    }

    /**
     * Sets gap size at the right of grid
     *
     * @param gapRight
     * @return same grid element
     */
    public EditsGridElement setGapRight(int gapRight) {
        this.gapRight = gapRight;
        return this;
    }

    /**
     * Sets default gap size at the right of grid
     *
     * @return same grid element
     */
    public EditsGridElement setGapRight() {
        return setGapRight(DEFAULT_GAP);
    }

    /**
     * Sets gap to align grid without title border with other grid which has
     * such border
     *
     * @return same grid element
     */
    public BaseElement setGapForBorderedAlign() {
        setGapLeft(2 * EditsGridElement.TITLE_BORDER_GAP);
        setGapRight(2 * EditsGridElement.TITLE_BORDER_GAP);
        return this;
    }

    /**
     * Sets all gap to default
     *
     * @return same grid element
     */
    public BaseElement setGap() {
        setGapLeft();
        setGapRight();
        setGapBottom();
        setGapTop();
        return this;
    }

    /**
     * Set all gaps
     *
     * @param gapLeft
     * @param gapRight
     * @param gapTop
     * @param gapBottom
     * @return same grid element
     */
    public EditsGridElement setGap(int gapLeft, int gapRight, int gapTop, int gapBottom) {
        this.gapLeft = gapLeft;
        this.gapRight = gapRight;
        this.gapTop = gapTop;
        this.gapBottom = gapBottom;
        return this;
    }

    /**
     * Set all gaps to same size
     *
     * @param gap
     * @return same grid element
     */
    public EditsGridElement setGap(int gap) {
        return setGap(gap, gap, gap, gap);
    }

}
