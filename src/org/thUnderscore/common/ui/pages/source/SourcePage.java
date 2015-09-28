package org.thUnderscore.common.ui.pages.source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.thUnderscore.common.utils.CommonUtils;
import org.thUnderscore.common.utils.UIUtils;
import org.thUnderscore.common.ui.components.ButtonsPanel;
import org.thUnderscore.common.ui.help.HelpHint;
import org.thUnderscore.common.ui.pages.Page;
import org.thUnderscore.common.ui.pages.PageParams;
import org.thUnderscore.common.ui.treemodel.DefaultTreeComparator;
import org.thUnderscore.common.ui.treemodel.TreeModelByList;
import sun.misc.IOUtils;
import syntaxhighlight.SyntaxHighlighter;
import syntaxhighlighter.SyntaxHighlighterParser;
import syntaxhighlighter.brush.BrushJava;
import syntaxhighlighter.theme.ThemeRDark;

/**
 * Page which can show source, stored in jar resources
 *
 * @author thUnderscore
 */
public class SourcePage extends Page {

    /*
     Resources contants
     */
    public static final String HELP_HI_RES = "SourcePage.HELP_HI";
    public static final String HELP_TREE_RES = "SourcePage.HELP_TREE";
    public static final String HELP_SOURCE_RES = "SourcePage.HELP_SOURCE";
    public static final String HELP_EXTRACT_RES = "SourcePage.HELP_EXTRACT";

    public static final String EXTRACT_TOOLTIP_RES = "SourcePage.EXTRACT_TOOLTIP";
    public static final String EXTRACT_TITLE_RES = "SourcePage.EXTRACT_TITLE";
    public static final String EXTRACT_DIALOG_TITLE_RES = "SourcePage.EXTRACT_DIALOG_TITLE";
    public static final String BUILD_CMD_PROPOSAL_RES = "SourcePage.BUILD_CMD_PROPOSAL";
    public static final String UNSUPPORTED_SCHEME_ERROR = "BaseListModel.UNSUPPORTED_SCHEME_ERROR";
    public static final String EXTRACT_ICON_RES = CommonUtils.RESOURCES_PATH + "Extract";

    public static final String EXTRACT_COMMAND = "Extract";
    public static final String BUILD_CMD_FILE_NAME = String.format("build.%s", CommonUtils.getScriptFileExtension());
    public static final String BUILD_CMD_FILE_TEMPLATE = String.format(
            "ant -f %s%sbuild.xml%s -Dnb.internal.action.name=rebuild -Dplatforms.JDK_1.6.home=<path_to_JDK_home> clean jar",
            CommonUtils.IS_WIN ? "\"%s" : "%s", File.separator,
            CommonUtils.IS_WIN ? "\"" : ""
    );

    private Map<String, String> sources;
    private SourceTreeNode selectedAfterInitNode;
    private String selectedAfterInitPath;

    /*
     *UI components 
     */
    JSplitPane splitPane;
    JScrollPane scrollPaneSourceTree;
    JTree sourceTree;
    List<SourceTreeNode> sourceList = new ArrayList();
    Map<String, SourceTreeNode> sourceMap = new HashMap<String, SourceTreeNode>();

    ButtonsPanel buttonsPanel;
    SyntaxHighlighterParser parser;
    SyntaxHighlighter highlighter;
    TreeModelByList treeModelByList;

    @Override
    protected void init(PageParams params) {
        super.init(params);
        checkParams(params, false, SourcePageParams.class);
        SourcePageParams sourceParams = ((SourcePageParams) params);
        this.sources = sourceParams.getSources();
        this.selectedAfterInitPath = sourceParams.getSelectedAfterInitPath();
        initComponents();

        this.helpHints = new HelpHint[]{
            new HelpHint(CommonUtils.i18n(HELP_HI_RES)),
            new HelpHint(CommonUtils.i18n(HELP_TREE_RES), null, sourceTree),
            new HelpHint(CommonUtils.i18n(HELP_SOURCE_RES)) {

                @Override
                public JComponent getComponent() {
                    return highlighter;
                }

            },
            new HelpHint(CommonUtils.i18n(HELP_EXTRACT_RES),
            CommonUtils.safeLoadIconResource(UIUtils.getButtonNormalIconRes(
            EXTRACT_ICON_RES)),
            buttonsPanel.getComponent(EXTRACT_COMMAND))
        };
    }

    /**
     * Init UI components
     */
    private void initComponents() {

        initSourceTree();
        scrollPaneSourceTree = new JScrollPane();
        Dimension treeSize = new Dimension(280, 0);
        scrollPaneSourceTree.setMinimumSize(treeSize);
        scrollPaneSourceTree.setPreferredSize(treeSize);
        scrollPaneSourceTree.setRequestFocusEnabled(false);
        scrollPaneSourceTree.setViewportView(sourceTree);

        JPanel leftPanel = new JPanel(new BorderLayout());

        buttonsPanel = new ButtonsPanel() {

            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                if (EXTRACT_COMMAND.equals(e.getActionCommand())) {
                    doExtract();
                }
            }

        };

        buttonsPanel.addButton(CommonUtils.geti18nBundle(), EXTRACT_TITLE_RES, EXTRACT_TOOLTIP_RES,
                EXTRACT_ICON_RES, EXTRACT_COMMAND,
                ButtonsPanel.Alignment.TRAILING, -1);
        buttonsPanel.setCommandColor(EXTRACT_COMMAND, new Color(229, 186, 112));

        leftPanel.add(scrollPaneSourceTree, BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

        splitPane = new JSplitPane();
        splitPane.setDividerLocation(400);
        splitPane.setLeftComponent(leftPanel);

        initContentViewer();

        GroupLayout mainLayout = new GroupLayout(this);
        setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                        .addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 1204, Short.MAX_VALUE)
                        .addGap(0, 0, 0))
        );
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 808, Short.MAX_VALUE)
                        .addGap(0, 0, 0))
        );

        if (selectedAfterInitNode != null) {
            sourceTree.setSelectionPath(treeModelByList.getPath(selectedAfterInitNode));
        }
    }

    /**
     * read source list from resources
     */
    private void initSourceList() {
        sourceList.clear();
        sourceMap.clear();
        selectedAfterInitNode = null;
        for (Map.Entry<String, String> sourcesEntry : sources.entrySet()) {
            SourceTreeNode sourceTreeNode = null;
            String rootResourcePath = sourcesEntry.getKey();
            String rootTreePath = sourcesEntry.getValue();
            if (CommonUtils.isNotEmpty(rootTreePath)) {
                String[] nodes = rootTreePath.split("/");
                for (int i = 0; i < nodes.length; i++) {
                    sourceTreeNode = new SourceTreeNode(sourceTreeNode,
                            nodes[i], true, null);
                    addSourceNode(sourceTreeNode);
                }
            }

            String[] nodes = rootResourcePath.split("/");
            for (int i = 0; i < nodes.length - 1; i++) {
                sourceTreeNode = new SourceTreeNode(sourceTreeNode,
                        nodes[i], true);
                addSourceNode(sourceTreeNode);
            }

            URL resources = SourcePage.class.getResource(
                    CommonUtils.setStartWith(rootResourcePath, "/"));
            String scheme = resources.getProtocol();

            try {
                if ("jar".equals(scheme)) {

                    JarURLConnection con;
                    con = (JarURLConnection) resources.openConnection();

                    JarFile archive = con.getJarFile();
                    Enumeration<JarEntry> entries = archive.entries();

                    String entryName;

                    rootResourcePath = CommonUtils.setEndWith(rootResourcePath, "/");
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        entryName = entry.getName();
                        if (entryName.startsWith(rootResourcePath)) {
                            boolean isLeaf = !entryName.endsWith("/");
                            String name;
                            String parent;
                            if (!isLeaf) {
                                entryName = entryName.substring(0, entryName.length() - 1);
                            }
                            int lastIndexOfSlash = entryName.lastIndexOf("/");
                            if (lastIndexOfSlash >= 0) {
                                name = entryName.substring(lastIndexOfSlash + 1,
                                        entryName.length());
                                parent = CommonUtils.getResourceName(rootTreePath,
                                        entryName.substring(0, lastIndexOfSlash));
                            } else {
                                name = entryName;
                                parent = rootTreePath;
                            }
                            addSourceNode(new SourceTreeNode(parent, name,
                                    entryName, !isLeaf));

                        }
                    }
                } else if ("file".equals(scheme)) {
                    //Just for debug or running class without jar

                    File rootResources = new File(resources.toURI());
                    addFileToResourceList(sourceTreeNode, rootResources);

                } else {
                    throw new IllegalArgumentException(
                            String.format(CommonUtils.i18n(UNSUPPORTED_SCHEME_ERROR), scheme));
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }

        }

    }

    /**
     * Add file to source list (just for debug mode)
     *
     * @param parentNode parentTreeNode
     * @param file file to be added
     */
    protected void addFileToResourceList(SourceTreeNode parentNode, File file) {
        if (file == null) {
            return;

        }
        SourceTreeNode sourceTreeNode = new SourceTreeNode(parentNode,
                file.getName(), file.isDirectory());
        addSourceNode(sourceTreeNode);

        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            Arrays.sort(listFiles);
            for (int i = 0; i < listFiles.length; i++) {
                File childFile = listFiles[i];
                addFileToResourceList(sourceTreeNode, childFile);
            }
        }

    }

    /**
     * Init Jtree which containf source tree
     */
    private void initSourceTree() {

        sourceTree = new JTree();
        initSourceList();
        treeModelByList
                = new TreeModelByList(sourceList,
                        SourceTreeNode.class, "treePath", "parentTreePath", "Sources",
                        new DefaultTreeComparator());

        sourceTree.setModel(treeModelByList);

        sourceTree.addTreeSelectionListener(
                new TreeSelectionListener() {
                    @Override
                    public void valueChanged(TreeSelectionEvent e
                    ) {
                        displaySelectedResource();
                    }
                }
        );

    }

    /**
     * Display source, selected in tree
     */
    private void displaySelectedResource() {
        TreePath path = sourceTree.getSelectionModel().getSelectionPath();
        SourceTreeNode node = (path != null)
                ? (SourceTreeNode) path.getLastPathComponent() : null;

        SourceContentData contentData = SourceTreeNode.getContentData(node);
        //Looks like bug in highlighter
        initContentViewer();
        parser.setBrush(contentData.brush);

        highlighter.setContent(contentData.text);

    }

    /**
     * init component which display source content
     */
    private void initContentViewer() {
        if (parser == null) {
            parser = new SyntaxHighlighterParser(new BrushJava());
        }
        int dividerLocation = splitPane.getDividerLocation();
        highlighter = new SyntaxHighlighter(parser, new ThemeRDark());
        Dimension contentSize = new Dimension(550, 0);
        highlighter.setMinimumSize(contentSize);
        highlighter.setPreferredSize(contentSize);
        splitPane.setRightComponent(highlighter);

        splitPane.setDividerLocation(dividerLocation);
    }

    /**
     * Extract sources from jar to selected durectory
     */
    protected void doExtract() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(CommonUtils.i18n(EXTRACT_DIALOG_TITLE_RES));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        File sourceRootFile;
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.exists()) {
                file.mkdirs();
            }
            sourceRootFile = extractTreeNode(treeModelByList.getRootNode(), file);
            File cmdFile = new File(sourceRootFile, BUILD_CMD_FILE_NAME);

            String fileBody = String.format(BUILD_CMD_FILE_TEMPLATE, sourceRootFile.toString().replace("\\", "\\\\"));

            byte[] bytes = fileBody.getBytes(Charset.forName("UTF-8"));
            FileOutputStream stream;
            try {
                stream = new FileOutputStream(cmdFile.toString());
                try {
                    stream.write(bytes);
                } finally {
                    stream.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(this, String.format(String.format(
                    CommonUtils.i18n(BUILD_CMD_PROPOSAL_RES), "%s",
                    BUILD_CMD_FILE_NAME), sourceRootFile.toString()));
        }

    }

    /**
     * Extract single source
     *
     * @param node tree node
     * @param file file where source to be extracted
     * @return
     */
    protected File extractTreeNode(TreeModelByList.TreeNode node, File file) {
        Iterator<TreeModelByList.TreeNode> childrenIterator = node.getChildrenIterator();
        SourceTreeNode sourceNode = (SourceTreeNode) node.getObject();
        SourceContentData contentData = SourceTreeNode.getContentData(sourceNode);
        File sourceFile = new File(file, sourceNode.name);
        if (contentData.isDirectory) {
            sourceFile.mkdir();
        } else {
            if (sourceFile.exists()) {
                sourceFile.delete();
            }
            if (sourceNode.isContentReadedSuccessfully) {
                FileOutputStream stream;
                try {
                    stream = new FileOutputStream(sourceFile.toString());
                    try {
                        stream.write(contentData.content);
                    } finally {
                        stream.close();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }

        while (childrenIterator.hasNext()) {
            extractTreeNode(childrenIterator.next(), sourceFile);
        }
        return sourceFile;
    }

    /**
     * Adds source tree node
     *
     * @param sourceTreeNode node to be added
     */
    protected void addSourceNode(SourceTreeNode sourceTreeNode) {
        if (!sourceMap.containsKey(sourceTreeNode.getTreePath())) {
            if (sourceTreeNode.getTreePath().equalsIgnoreCase(selectedAfterInitPath)) {
                selectedAfterInitNode = sourceTreeNode;
            }
            sourceMap.put(sourceTreeNode.getTreePath(), sourceTreeNode);
            sourceList.add(sourceTreeNode);
        }
    }

}
