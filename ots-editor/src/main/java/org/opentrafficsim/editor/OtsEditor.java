package org.opentrafficsim.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventListenerMap;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.opentrafficsim.swing.gui.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.javagl.treetable.JTreeTable;

/**
 * Editor window to load, edit and save OTS XML files. The class uses an underlying data structure that is based on the XML
 * Schema for the XML (XSD).<br>
 * <br>
 * This functionality is currently in development.
 * @author wjschakel
 */
public class OtsEditor extends JFrame implements EventProducer
{

    /** */
    private static final long serialVersionUID = 20230217L;

    /** Event when a a new file is started. */
    public static final EventType NEW_FILE = new EventType("NEWFILEs",
            new MetaData("New file", "New file", new ObjectDescriptor("Root", "New root element", XsdTreeNodeRoot.class)));

    /** Event when the selection in the tree is changed. */
    public static final EventType SELECTION_CHANGED = new EventType("SELECTIONCHANGED", new MetaData("Selection",
            "Selection changed", new ObjectDescriptor("Selected node", "Selected node", XsdTreeNode.class)));

    /** Number of pixels between icons when they are combined. */
    private static final int ICON_MARGIN = 3;

    /** Width of the divider between parts of the screen. */
    private static final int DIVIDER_SIZE = 3;

    /** Whether to update the windows as the split is being dragged. */
    private static final boolean UPDATE_SPLIT_WHILE_DRAGGING = true;

    /** Color for inactive nodes (text). */
    private static final Color INACTIVE_COLOR = new Color(160, 160, 160);

    /** Color for invalid nodes and values (background). */
    public static final Color INVALID_COLOR = new Color(255, 240, 240);

    /** Maximum length for tooltips. */
    private static final int MAX_TOOLTIP_LENGTH = 96;

    /** Maximum number of items to show in a dropdown menu. */
    private static final int MAX_DROPDOWN_ITEMS = 20;

    /** Indent for first item shown in dropdown. */
    private int dropdownIndent = 0;

    /** All items eligible to be shown in a dropdown, i.e. they match the currently typed value. */
    private List<String> dropdownOptions = new ArrayList<>();

    /** Map of listeners for {@code EventProducer}. */
    private final EventListenerMap listenerMap = new EventListenerMap();

    /** Main left-right split pane. */
    private final JSplitPane mainSplitPane;

    /** Main tabbed pane at the left-hand side. */
    private final JTabbedPane visualizationPane;

    /** Split pane on the right-hand side. */
    private final JSplitPane rightSplitPane;

    /** Tree table at the top in the right-hand side. */
    private JTreeTable treeTable;

    /** Table for attributes at the bottom of the right-hand side. */
    private final JTable attributesTable;

    /** Prevents a popup when an expand node is being clicked. */
    private boolean mayPresentChoice = true;

    /** Node for which currently a choice popup is being shown, {@code null} if there is none. */
    private XsdTreeNode choiceNode;

    /** Map of custom icons, to be loaded as the icon for a node is being composed based in its properties. */
    private Map<String, Icon> customIcons = new LinkedHashMap<>();

    /** Icon for in question dialog. */
    private final ImageIcon questionIcon;

    /** Root node of the XSD file. */
    private Document xsdDocument;

    /** Last directory from which a file was loaded or in to which a file was saved. */
    private String lastDirectory;

    /** Last file that was loaded or saved. */
    private String lastFile;

    /** Whether there is unsaved content. */
    private boolean unsavedChanges = false;

    /**
     * Constructor.
     * @throws IOException when a resource could not be loaded.
     */
    public OtsEditor() throws IOException
    {
        setSize(1280, 720);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            /** {@inheritDoc} */
            @Override
            public void windowClosing(final WindowEvent e)
            {
                exit();
            }
        });

        // split panes
        this.mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, UPDATE_SPLIT_WHILE_DRAGGING);
        this.mainSplitPane.setDividerSize(DIVIDER_SIZE);
        this.mainSplitPane.setResizeWeight(0.5);
        add(this.mainSplitPane);
        this.rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, UPDATE_SPLIT_WHILE_DRAGGING);
        this.rightSplitPane.setDividerSize(DIVIDER_SIZE);
        this.rightSplitPane.setResizeWeight(0.5);
        this.mainSplitPane.setRightComponent(this.rightSplitPane);

        setIconImage(ImageIO.read(Resource.getResourceAsStream("./OTS_merge.png")));
        this.questionIcon = loadIcon("./Question.png", -1, -1, -1, -1);

        // visualization pane
        this.visualizationPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
        this.visualizationPane.setPreferredSize(new Dimension(900, 900));
        this.mainSplitPane.setLeftComponent(this.visualizationPane);

        // There is likely a better way to do this, but setting the icons specific on the tree is impossible for collapsed and
        // expanded. Also in that case after removal of a node, the tree appearance gets reset and java default icons appear.
        // This happens to the leaf/open/closed icons that can be set on the tree. This needs to be done before the JTreeTable
        // is created, otherwise it loads normal default icons.
        UIManager.put("Tree.collapsedIcon",
                new ImageIcon(ImageIO.read(Resource.getResourceAsStream("/Eclipse_collapsed.png"))));
        UIManager.put("Tree.expandedIcon", new ImageIcon(ImageIO.read(Resource.getResourceAsStream("/Eclipse_expanded.png"))));

        // empty tree table
        this.treeTable = new JTreeTable(new XsdTreeTableModel(null));
        XsdTreeTableModel.applyColumnWidth(this.treeTable);
        this.rightSplitPane.setTopComponent(new JScrollPane(this.treeTable));

        // attributes table
        AttributesTableModel tableModel = new AttributesTableModel(null, this.treeTable);
        DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn column1 = new TableColumn(0);
        column1.setHeaderValue(tableModel.getColumnName(0));
        columns.addColumn(column1);
        TableColumn column2 = new TableColumn(1);
        column2.setHeaderValue(tableModel.getColumnName(1));
        columns.addColumn(column2);
        TableColumn column3 = new TableColumn(2);
        column3.setHeaderValue(tableModel.getColumnName(2));
        columns.addColumn(column3);
        TableColumn column4 = new TableColumn(3);
        column4.setHeaderValue(tableModel.getColumnName(3));
        columns.addColumn(column4);
        this.attributesTable = new JTable(tableModel, columns);
        this.attributesTable.putClientProperty("terminateEditOnFocusLost", true);
        this.attributesTable.setDefaultRenderer(String.class,
                new AttributeCellRenderer(loadIcon("./Info.png", 12, 12, 16, 16)));
        AttributesCellEditor editor = new AttributesCellEditor();
        editor.addCellEditorListener(new CellEditorListener()
        {
            /** {@inheritDoc} */
            @Override
            public void editingStopped(final ChangeEvent e)
            {
                setUnsavedChanges(true);
            }

            /** {@inheritDoc} */
            @Override
            public void editingCanceled(final ChangeEvent e)
            {
                setUnsavedChanges(true);
            }
        });
        this.attributesTable.setDefaultEditor(String.class, editor);
        this.attributesTable.addMouseListener(new AttributesMouseListener());
        AttributesTableModel.applyColumnWidth(this.attributesTable);
        this.rightSplitPane.setBottomComponent(new JScrollPane(this.attributesTable));

        addMenuBar();

        // appear to the user
        setVisible(true);
        this.mainSplitPane.setDividerLocation(0.65);
        this.rightSplitPane.setDividerLocation(0.75);
    }

    /**
     * Adds the menu bar.
     */
    private void addMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem newFile = new JMenuItem("New");
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileMenu.add(newFile);
        newFile.addActionListener(new ActionListener()
        {
            /** {@inheritDoc} */
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                newFile();
            }
        });
        JMenuItem open = new JMenuItem("Open...");
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.add(open);
        open.addActionListener(new ActionListener()
        {
            /** {@inheritDoc} */
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                openFile();
            }
        });
        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.add(save);
        save.addActionListener(new ActionListener()
        {
            /** {@inheritDoc} */
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                saveFile();
            }
        });
        JMenuItem saveAs = new JMenuItem("Save as...");
        fileMenu.add(saveAs);
        saveAs.addActionListener(new ActionListener()
        {
            /** {@inheritDoc} */
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                saveFileAs();
            }
        });
        fileMenu.add(new JSeparator());
        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);
        exit.addActionListener(new ActionListener()
        {
            /** {@inheritDoc} */
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                exit();
            }
        });
    }

    /**
     * Sets whether there are unsaved changes, resulting in a * in the window name, and confirmation pop-ups upon file changes.
     * @param unsavedChanges boolean; whether there are unsaved changes.
     */
    private void setUnsavedChanges(final boolean unsavedChanges)
    {
        this.unsavedChanges = unsavedChanges;
        setTitle("OTS | The Open Traffic Simulator | Editor" + (unsavedChanges ? " *" : ""));
    }

    /**
     * Sets a new schema in the GUI.
     * @param xsdDocument Document; main node from an XSD schema file.
     * @throws IOException when a resource could not be loaded.
     */
    @SuppressWarnings("checkstyle:hiddenfield")
    public void setSchema(final Document xsdDocument) throws IOException
    {
        this.xsdDocument = xsdDocument;
        initializeTree();
    }

    /**
     * Asks for confirmation to discard unsaved changes, if any, and initializes the tree.
     */
    private void newFile()
    {
        if (confirmDiscardChanges())
        {
            try
            {
                initializeTree();
            }
            catch (IOException exception)
            {
                throw new RuntimeException("Unable to load new tree.", exception);
            }
        }
    }

    /**
     * Initializes the tree based on the XSD schema.
     * @throws IOException when a resource can not be loaded.
     */
    private void initializeTree() throws IOException
    {
        // tree table
        XsdTreeTableModel treeModel = new XsdTreeTableModel(this.xsdDocument);
        this.treeTable = new JTreeTable(treeModel);
        this.treeTable.putClientProperty("terminateEditOnFocusLost", true);
        treeModel.setTreeTable(this.treeTable);
        this.treeTable.setDefaultRenderer(String.class, new StringCellRenderer(this.treeTable));
        ((DefaultCellEditor) this.treeTable.getDefaultEditor(String.class)).setClickCountToStart(1);
        ((DefaultCellEditor) this.treeTable.getDefaultEditor(String.class)).addCellEditorListener(new CellEditorListener()
        {
            /** {@inheritDoc} */
            @Override
            public void editingStopped(final ChangeEvent e)
            {
                setUnsavedChanges(true);
            }

            /** {@inheritDoc} */
            @Override
            public void editingCanceled(final ChangeEvent e)
            {
                setUnsavedChanges(true);
            }
        });
        XsdTreeTableModel.applyColumnWidth(this.treeTable);

        addTreeTableListeners();

        int dividerLocation = this.rightSplitPane.getDividerLocation();
        this.rightSplitPane.setTopComponent(null);
        this.rightSplitPane.setTopComponent(new JScrollPane(this.treeTable));
        this.rightSplitPane.setDividerLocation(dividerLocation);

        XsdTreeNodeRoot root = (XsdTreeNodeRoot) treeModel.getRoot();
        EventListener listener = new EventListener()
        {
            /** */
            private static final long serialVersionUID = 20230311L;

            /** {@inheritDoc} */
            @Override
            public void notify(final Event event) throws RemoteException
            {
                setUnsavedChanges(true);
            }
        };
        root.addListener(listener, XsdTreeNodeRoot.NODE_CREATED);
        root.addListener(listener, XsdTreeNodeRoot.NODE_REMOVED);
        root.addListener(listener, XsdTreeNodeRoot.OPTION_CHANGED);
        root.addListener(listener, XsdTreeNodeRoot.ACTIVATION_CHANGED);
        fireEvent(NEW_FILE, root);

        setUnsavedChanges(false);
    }

    /**
     * Adds all listeners to the tree table.
     * @throws IOException on exception.
     */
    private void addTreeTableListeners() throws IOException
    {
        // throws selection events and updates the attributes table
        this.treeTable.getTree().addTreeSelectionListener(new TreeSelectionListener()
        {
            /** {@inheritDoc} */
            @Override
            public void valueChanged(final TreeSelectionEvent e)
            {
                TreePath[] paths = e.getPaths();
                if (paths.length > 0)
                {
                    XsdTreeNode node = (XsdTreeNode) paths[0].getLastPathComponent();
                    // TODO: This does not solve multiple editors on the same value being open in parallel when the editing does
                    // not change the selection. Furthermore, editors in the main screen may change values too.
                    if (OtsEditor.this.attributesTable.isEditing())
                    {
                        OtsEditor.this.attributesTable.editingCanceled(null);
                    }
                    OtsEditor.this.attributesTable.setModel(new AttributesTableModel(node, OtsEditor.this.treeTable));
                    try
                    {
                        fireEvent(SELECTION_CHANGED, node);
                    }
                    catch (RemoteException exception)
                    {
                        exception.printStackTrace();
                    }
                }
                else
                {
                    OtsEditor.this.attributesTable.setModel(new AttributesTableModel(null, OtsEditor.this.treeTable));
                }
            }
        });

        // sets custom icon, prepends grouping icon, and appends the choice icon for choice nodes
        this.treeTable.getTree().setCellRenderer(new XsdTreeCellRenderer());

        // this listener will make sure no choice popup is presented by a left-click on expand/collapse, even for a choice node
        this.treeTable.getTree().addTreeWillExpandListener(new TreeWillExpandListener()
        {
            /** {@inheritDoc} */
            @Override
            public void treeWillExpand(final TreeExpansionEvent event) throws ExpandVetoException
            {
                OtsEditor.this.mayPresentChoice = false;
            }

            /** {@inheritDoc} */
            @Override
            public void treeWillCollapse(final TreeExpansionEvent event) throws ExpandVetoException
            {
                OtsEditor.this.mayPresentChoice = false;
            }
        });

        // this listener makes sure that a choice popup can be presented again after a left-click on an expansion/collapse node
        this.treeTable.addMouseMotionListener(new MouseMotionAdapter()
        {
            /** {@inheritDoc} */
            @Override
            public void mouseMoved(final MouseEvent e)
            {
                OtsEditor.this.mayPresentChoice = true;
            }
        });

        // this listener opens the attributes of a node, and presents the popup for a choice or for addition/deletion of nodes
        this.treeTable.addMouseListener(new XsdTreeMouseListener());

        // this listener removes the selected node, if it is removable
        this.treeTable.addKeyListener(new KeyAdapter()
        {
            /** {@inheritDoc} */
            @Override
            public void keyReleased(final KeyEvent e)
            {
                if (OtsEditor.this.treeTable.isEditing())
                {
                    // prevents row i being removed, being replaced by i+1, and editing then setting the value of i+1 now at i
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    XsdTreeNode node =
                            (XsdTreeNode) OtsEditor.this.treeTable.getTree().getSelectionPath().getLastPathComponent();
                    if (node.isRemovable())
                    {
                        if (confirmNodeRemoval(node))
                        {
                            int selected = OtsEditor.this.treeTable.getSelectedRow();
                            node.remove();
                            OtsEditor.this.treeTable.updateUI();
                            OtsEditor.this.treeTable.getSelectionModel().setSelectionInterval(selected, selected);
                            TreePath path = OtsEditor.this.treeTable.getTree().getSelectionPath();
                            if (path != null) // can be null if last node was removed causing no effective selection
                            {
                                OtsEditor.this.attributesTable.setModel(new AttributesTableModel(
                                        (XsdTreeNode) path.getLastPathComponent(), OtsEditor.this.treeTable));
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Adds a listener to a popup to remove the popop from the component when the popup becomes invisible. This makes sure that
     * a right-clicks on another location that should show a different popup, is not overruled by the popup of a previous click.
     * @param popup JPopupMenu; popup menu.
     * @param component JComponent; component from which the menu will be removed.
     */
    private void preparePopupRemoval(final JPopupMenu popup, final JComponent component)
    {
        popup.addPopupMenuListener(new PopupMenuListener()
        {
            /** {@inheritDoc} */
            @Override
            public void popupMenuWillBecomeVisible(final PopupMenuEvent e)
            {
            }

            /** {@inheritDoc} */
            @Override
            public void popupMenuWillBecomeInvisible(final PopupMenuEvent e)
            {
                component.setComponentPopupMenu(null);
                OtsEditor.this.choiceNode = null;
            }

            /** {@inheritDoc} */
            @Override
            public void popupMenuCanceled(final PopupMenuEvent e)
            {
            }
        });
    }

    /**
     * Sets a custom icon for nodes that comply to the path. The path may be an absolute path (e.g. "OTS.NETWORK.CONNECTOR") or
     * a relative path (e.g. ".NODE"). The image should be a filename relative in resources.
     * @param path String; path.
     * @param icon ImageIcon; image icon.
     */
    public void setCustomIcon(final String path, final ImageIcon icon)
    {
        this.customIcons.put(path, icon);
    }

    /**
     * Loads an icon, possibly rescaled.
     * @param image String; image filename, relative in resources.
     * @param width int; width to resize to, may be -1 to leave as is.
     * @param height int; width to resize to, may be -1 to leave as is.
     * @param bgWidth int; background image width icon will be centered in, may be -1 to leave as is.
     * @param bgHeight int; background image height icon will be centered in, may be -1 to leave as is.
     * @return ImageIcon; image icon.
     * @throws IOException if the file is not in resources.
     */
    public static ImageIcon loadIcon(final String image, final int width, final int height, final int bgWidth,
            final int bgHeight) throws IOException
    {
        Image im = ImageIO.read(Resource.getResourceAsStream(image));
        if (width > 0 || height > 0)
        {
            im = im.getScaledInstance(width > 0 ? width : im.getWidth(null), height > 0 ? height : im.getHeight(null),
                    Image.SCALE_SMOOTH);
        }
        if (bgWidth > 0 && bgHeight > 0)
        {
            BufferedImage bg = new BufferedImage(bgWidth > 0 ? bgWidth : im.getWidth(null),
                    bgHeight > 0 ? bgHeight : im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bg.getGraphics();
            g.drawImage(im, (bg.getWidth() - im.getWidth(null)) / 2, (bg.getHeight() - im.getHeight(null)) / 2, null);
            im = bg;
        }
        return new ImageIcon(im);
    }

    /**
     * Obtains a custom icon for the path, or {@code null} if there is no custom icon specified for the path.
     * @param path String; node path.
     * @return Icon; custom icon, or {@code null} if there is no custom icon specified for the path.
     */
    private Icon getCustomIcon(final String path)
    {
        Icon icon = this.customIcons.get(path);
        if (icon != null)
        {
            return icon;
        }
        for (Entry<String, Icon> entry : this.customIcons.entrySet())
        {
            if (path.endsWith(entry.getKey()))
            {
                return entry.getValue();
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public EventListenerMap getEventListenerMap() throws RemoteException
    {
        return this.listenerMap;
    }

    /**
     * Adds a tab to the main window.
     * @param name String; name of the tab.
     * @param icon Icon; icon for the tab, may be {@code null}.
     * @param component Component; component that will fill the tab.
     * @param tip String; tool-tip for the tab, may be {@code null}.
     */
    public void addTab(final String name, final Icon icon, final Component component, final String tip)
    {
        this.visualizationPane.addTab(name, icon, component, tip);
    }

    /**
     * Returns the component of the tab with given name.
     * @param name String; name of the tab.
     * @return Component; component of the tab with given name.
     */
    public Component getTab(final String name)
    {
        for (int index = 0; index < this.visualizationPane.getTabCount(); index++)
        {
            if (this.visualizationPane.getTitleAt(index).equals(name))
            {
                return this.visualizationPane.getComponentAt(index);
            }
        }
        return null;
    }

    /**
     * Place focus on the tab with given name.
     * @param name String; name of the tab.
     */
    public void focusTab(final String name)
    {
        for (int index = 0; index < this.visualizationPane.getTabCount(); index++)
        {
            if (this.visualizationPane.getTitleAt(index).equals(name))
            {
                this.visualizationPane.setSelectedIndex(index);
            }
        }
    }

    /**
     * Requests the user to confirm the deletion of a node. The default button is "Ok". The window popping up is considered
     * sufficient warning, and in this way a speedy succession of "del" and "enter" may delete a consecutive range of nodes to
     * be deleted.
     * @param node XsdTreeNode; node.
     * @return boolean; {@code true} if the user confirms node removal.
     */
    private boolean confirmNodeRemoval(final XsdTreeNode node)
    {
        return JOptionPane.showConfirmDialog(this, "Remove `" + node + "`?", "Remove?", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, this.questionIcon) == JOptionPane.OK_OPTION;
    }

    /**
     * Shows a dialog in a modal pane to confirm discarding unsaved changes.
     * @return boolean; whether unsaved changes can be discarded.
     */
    private boolean confirmDiscardChanges()
    {
        if (!this.unsavedChanges)
        {
            return true;
        }
        return JOptionPane.showConfirmDialog(this, "Discard unsaved changes?", "Discard unsaved changes?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, this.questionIcon) == JOptionPane.OK_OPTION;
    }

    /**
     * Shows a description in a modal pane.
     * @param description String; description.
     */
    public void showDescription(final String description)
    {
        JOptionPane.showMessageDialog(OtsEditor.this,
                "<html><body><p style='width: 400px;'>" + description + "</p></body></html>");
    }

    /**
     * Places a popup with options under the cell that is being clicked in a table. The popup will show items relevant to what
     * is being typed in the cell. The maximum number of items shown is limited to {@code MAX_DROPDOWN_ITEMS}.
     * @param allOptions List&lt;String&gt;; list of all options, will be filtered when typing.
     * @param table JTable; table, will be either the tree table or the attributes table.
     * @param action Consumer&lt;String&gt;; action to perform based on the option in the popup that was selected.
     */
    private void optionsPopup(final List<String> allOptions, final JTable table, final Consumer<String> action)
    {
        // initially no filtering on current value; this allows a quick reset to possible values
        List<String> options = filterOptions(allOptions, "");
        OtsEditor.this.dropdownOptions = options;
        if (options.isEmpty())
        {
            return;
        }
        JPopupMenu popup = new JPopupMenu();
        int index = 0;
        for (String option : options)
        {
            JMenuItem item = new JMenuItem(option);
            item.setVisible(index++ < MAX_DROPDOWN_ITEMS);
            item.addActionListener(new ActionListener()
            {
                /** {@inheritDoc} */
                @Override
                public void actionPerformed(final ActionEvent e)
                {
                    table.editingCanceled(null);
                    action.accept(option);
                    OtsEditor.this.treeTable.updateUI();
                }
            });
            item.setFont(table.getFont());
            popup.add(item);
        }
        this.dropdownIndent = 0;
        popup.addMouseWheelListener(new MouseWheelListener()
        {
            /** {@inheritDoc} */
            @Override
            public void mouseWheelMoved(final MouseWheelEvent e)
            {
                OtsEditor.this.dropdownIndent += (e.getWheelRotation() * e.getScrollAmount());
                OtsEditor.this.dropdownIndent = OtsEditor.this.dropdownIndent < 0 ? 0 : OtsEditor.this.dropdownIndent;
                int maxIndent = OtsEditor.this.dropdownOptions.size() - MAX_DROPDOWN_ITEMS;
                if (maxIndent > 0)
                {
                    OtsEditor.this.dropdownIndent =
                            OtsEditor.this.dropdownIndent > maxIndent ? maxIndent : OtsEditor.this.dropdownIndent;
                    showOptionsInScope(popup);
                }
            }
        });
        preparePopupRemoval(popup, table);
        // invoke later because JTreeTable removes the popup with editable cells and it may take previous editable field
        SwingUtilities.invokeLater(new Runnable()
        {
            /** {@inheritDoc} */
            @Override
            public void run()
            {
                JTextField field = (JTextField) ((DefaultCellEditor) table.getDefaultEditor(String.class)).getComponent();
                table.setComponentPopupMenu(popup);
                popup.pack();
                popup.setInvoker(table);
                popup.setVisible(true);
                field.requestFocus();
                Rectangle rectangle = field.getBounds();
                placePopup(popup, rectangle, table);
                field.addKeyListener(new KeyAdapter()
                {
                    /** {@inheritDoc} */
                    @Override
                    public void keyTyped(final KeyEvent e)
                    {
                        // invoke later to include this current typed key in the result
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            /** {@inheritDoc} */
                            @Override
                            public void run()
                            {
                                OtsEditor.this.dropdownIndent = 0;
                                String currentValue = field.getText();
                                OtsEditor.this.dropdownOptions = filterOptions(allOptions, currentValue);
                                boolean anyVisible = showOptionsInScope(popup);
                                // if no items left, show what was typed as a single item
                                // it will be hidden later if we are in the scope of the options, or another current value
                                if (!anyVisible)
                                {
                                    JMenuItem item = new JMenuItem(currentValue);
                                    item.addActionListener(new ActionListener()
                                    {
                                        /** {@inheritDoc} */
                                        @Override
                                        public void actionPerformed(final ActionEvent e)
                                        {
                                            table.editingCanceled(null);
                                            action.accept(currentValue);
                                            OtsEditor.this.treeTable.updateUI();
                                        }
                                    });
                                    item.setFont(table.getFont());
                                    popup.add(item);
                                }
                                popup.pack();
                                placePopup(popup, rectangle, table);
                            }
                        });
                    }
                });
                field.addActionListener(new ActionListener()
                {
                    /** {@inheritDoc} */
                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        popup.setVisible(false);
                        table.setComponentPopupMenu(null);
                    }
                });
            }
        });
    }

    /**
     * Updates the options that are shown within an dropdown menu based on an indent from scrolling.
     * @param popup JPopupMenu; dropdown menu.
     * @return boolean; whether at least one item is visible.
     */
    private boolean showOptionsInScope(final JPopupMenu popup)
    {
        int optionIndex = 0;
        for (Component component : popup.getComponents())
        {
            JMenuItem item = (JMenuItem) component;
            boolean visible =
                    optionIndex < MAX_DROPDOWN_ITEMS && this.dropdownOptions.indexOf(item.getText()) >= this.dropdownIndent;
            item.setVisible(visible);
            if (visible)
            {
                optionIndex++;
            }
        }
        popup.pack();
        return optionIndex > 0;
    }

    /**
     * Places a popup either below or above a given rectangle, based on surrounding space in the window.
     * @param popup JPopupMenu; popup.
     * @param rectangle Rectangle; rectangle of cell being edited, relative to the parent component.
     * @param parent JComponent; component containing the cell.
     */
    private void placePopup(final JPopupMenu popup, final Rectangle rectangle, final JComponent parent)
    {
        Point pAttributes = parent.getLocationOnScreen();
        // cannot use screen size in case of multiple monitors, so we keep the popup on the JFrame rather than the window
        Dimension windowSize = OtsEditor.this.getSize();
        Point pWindow = OtsEditor.this.getLocationOnScreen();
        if (pAttributes.y + (int) rectangle.getMaxY() + popup.getBounds().getHeight() > windowSize.height + pWindow.y - 1)
        {
            popup.setLocation(pAttributes.x + (int) rectangle.getMinX(),
                    pAttributes.y + (int) rectangle.getMinY() - 1 - (int) popup.getBounds().getHeight());
        }
        else
        {
            popup.setLocation(pAttributes.x + (int) rectangle.getMinX(), pAttributes.y + (int) rectangle.getMaxY() - 1);
        }
    }

    /**
     * Asks for confirmation to discard unsaved changes, if any, and show a dialog to open a file.
     */
    void openFile()
    {
        if (!confirmDiscardChanges())
        {
            return;
        }
        FileDialog fileDialog = new FileDialog(this, "Open XML", FileDialog.LOAD);
        fileDialog.setFilenameFilter(new FilenameFilter()
        {
            /** {@inheritDoc} */
            @Override
            public boolean accept(final File dir, final String name)
            {
                return name.toLowerCase().endsWith(".xml");
            }
        });
        fileDialog.setVisible(true);
        String fileName = fileDialog.getFile();
        if (fileName == null)
        {
            return;
        }
        if (!fileName.toLowerCase().endsWith(".xml"))
        {
            return;
        }
        this.lastDirectory = fileDialog.getDirectory();
        this.lastFile = fileName;
        File file = new File(this.lastDirectory + this.lastFile);
        try
        {
            Document document = DocumentReader.open(file.toURI());
            initializeTree();
            XsdTreeNodeRoot root = (XsdTreeNodeRoot) OtsEditor.this.treeTable.getTree().getModel().getRoot();
            root.setDirectory(this.lastDirectory);
            root.loadXmlNodes(document.getFirstChild());
            setUnsavedChanges(false);
        }
        catch (SAXException | IOException | ParserConfigurationException exception)
        {
            throw new RuntimeException("Unable to read XML file.", exception);
        }
    }

    /**
     * Saves the file is a file name is known, otherwise forwards to {@code saveFileAs()}.
     */
    private void saveFile()
    {
        if (this.lastFile == null)
        {
            saveFileAs();
            return;
        }
        save();
    }

    /**
     * Shows a dialog to define a file and saves in to it.
     */
    private void saveFileAs()
    {
        FileDialog fileDialog = new FileDialog(this, "Save XML", FileDialog.SAVE);
        fileDialog.setFile(this.lastFile == null ? "*.xml" : this.lastFile);
        fileDialog.setFilenameFilter(new FilenameFilter()
        {
            /** {@inheritDoc} */
            @Override
            public boolean accept(final File dir, final String name)
            {
                return name.toLowerCase().endsWith(".xml");
            }
        });
        fileDialog.setVisible(true);
        String fileName = fileDialog.getFile();
        if (fileName == null)
        {
            return;
        }
        if (!fileName.toLowerCase().endsWith(".xml"))
        {
            fileName = fileName + ".xml";
        }
        this.lastDirectory = fileDialog.getDirectory();
        this.lastFile = fileName;
        save();
    }

    /**
     * Performs the actual saving, either from {@code saveFile()} or {@code saveFileAs()}.
     */
    private void save()
    {
        XsdTreeNodeRoot root = (XsdTreeNodeRoot) OtsEditor.this.treeTable.getTree().getModel().getRoot();
        try
        {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.newDocument();
            /*
             * The following line omits the 'standalone="no"' in the header xml tag. But there will be no new-line after this
             * header tag. It seems a java bug: https://bugs.openjdk.org/browse/JDK-8249867. Result: <?xml version="1.0"
             * encoding="UTF-8"?><ots:OTS xmlns:ots="http://www.opentrafficsim.org/ots" ... etc. Other lines will be on a new
             * line and indented.
             */
            document.setXmlStandalone(true);
            root.saveXmlNodes(document, document);
            Element xmlRoot = (Element) document.getChildNodes().item(0);
            xmlRoot.setAttribute("xmlns:ots", "http://www.opentrafficsim.org/ots");
            xmlRoot.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xmlRoot.setAttribute("xsi:schemaLocation",
                    "http://www.opentrafficsim.org/ots ../../../../../ots-parser-xml/src/main/resources/xsd/ots.xsd");
            xmlRoot.setAttribute("xmlns:xi", "http://www.w3.org/2001/XInclude");

            File file = new File(this.lastDirectory + this.lastFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            StreamResult result = new StreamResult(fileOutputStream);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), result);

            fileOutputStream.close();
            setUnsavedChanges(false);
            root.setDirectory(this.lastDirectory);
        }
        catch (ParserConfigurationException | TransformerException | IOException exception)
        {
            throw new RuntimeException("Unable to save file.", exception);
        }
    }

    /**
     * Exits the system, but not before a confirmation on unsaved changes if there are unsaved changes.
     */
    private void exit()
    {
        if (confirmDiscardChanges())
        {
            System.exit(0);
        }
    }

    // /**
    // * Temporary stub to create map pane.
    // * @return JComponent; component.
    // */
    // private static JComponent buildMapPane()
    // {
    // JLabel map = new JLabel("map");
    // map.setOpaque(true);
    // map.setHorizontalAlignment(JLabel.CENTER);
    // return map;
    // }
    //
    // /**
    // * Temporary stub to create road layout pane.
    // * @return JComponent; component.
    // */
    // private static JComponent buildRoadLayoutPane()
    // {
    // JLabel roadLayout = new JLabel("road layout");
    // roadLayout.setOpaque(true);
    // roadLayout.setHorizontalAlignment(JLabel.CENTER);
    // return roadLayout;
    // }
    //
    // /**
    // * Temporary stub to create OD pane.
    // * @return JComponent; component.
    // */
    // private static JComponent buildOdPane()
    // {
    // JLabel od = new JLabel("od");
    // od.setOpaque(true);
    // od.setHorizontalAlignment(JLabel.CENTER);
    // return od;
    // }
    //
    // /**
    // * Temporary stub to create parameters pane.
    // * @return JComponent; component.
    // */
    // private static JComponent buildParameterPane()
    // {
    // JLabel parameters = new JLabel("parameters");
    // parameters.setOpaque(true);
    // parameters.setHorizontalAlignment(JLabel.CENTER);
    // return parameters;
    // }
    //
    // /**
    // * Temporary stub to create text pane.
    // * @return JComponent; component.
    // */
    // private static JComponent buildTextPane()
    // {
    // JLabel text = new JLabel("text");
    // text.setOpaque(true);
    // text.setHorizontalAlignment(JLabel.CENTER);
    // return text;
    // }

    /**
     * Limits the length of a tooltip message. This is to prevent absurd tooltip texts based on really long patterns that should
     * be matched. Will return {@code null} if the input is {@code null}.
     * @param message String; tooltip message, may be {@code null}.
     * @return String; possibly shortened tooltip message.
     */
    public static String limitTooltip(final String message)
    {
        if (message == null)
        {
            return null;
        }
        if (message.length() < MAX_TOOLTIP_LENGTH)
        {
            return message;
        }
        return message.substring(0, MAX_TOOLTIP_LENGTH - 3) + "...";
    }

    /**
     * Filter options, leaving only those that start with the current value.
     * @param options List&lt;String&gt;; options to filter.
     * @param currentValue String; current value.
     * @return List&lt;String&gt;; filtered options.
     */
    private static List<String> filterOptions(final List<String> options, final String currentValue)
    {
        return options.stream().filter((val) -> currentValue == null || currentValue.isBlank() || val.startsWith(currentValue))
                .distinct().sorted().collect(Collectors.toList());
    }

    /**
     * Listener to the mouse for the attributes table.
     * @author wjschakel
     */
    private final class AttributesMouseListener extends MouseAdapter
    {
        /** {@inheritDoc} */
        @Override
        public void mouseClicked(final MouseEvent e)
        {
            // makes description appear when information icon was clicked
            int col = OtsEditor.this.attributesTable.columnAtPoint(e.getPoint());
            if (OtsEditor.this.attributesTable.convertColumnIndexToModel(col) == 3)
            {
                int row = OtsEditor.this.attributesTable.rowAtPoint(e.getPoint());
                if (OtsEditor.this.attributesTable.getModel().getValueAt(row, col) != null)
                {
                    XsdTreeNode node = ((AttributesTableModel) OtsEditor.this.attributesTable.getModel()).getNode();
                    String description =
                            DocumentReader.getAnnotation(node.getAttributeNode(row), "xsd:documentation", "description");
                    showDescription(description);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void mousePressed(final MouseEvent e)
        {
            // shows popup for attributes with a selection of allowable values (xsd:keyref, xsd:enumeration)
            int col = OtsEditor.this.attributesTable.columnAtPoint(e.getPoint());
            if (OtsEditor.this.attributesTable.convertColumnIndexToModel(col) == 1)
            {
                int row = OtsEditor.this.attributesTable.rowAtPoint(e.getPoint());
                XsdTreeNode node = ((AttributesTableModel) OtsEditor.this.attributesTable.getModel()).getNode();
                if (!node.isInclude())
                {
                    List<String> allOptions = node.getAttributeRestrictions(row);
                    JTable table = OtsEditor.this.attributesTable;
                    optionsPopup(allOptions, table, (t) -> node.setAttributeValue(row, t));
                }
            }
        }
    }

    /**
     * Renderer for the nodes in the tree.
     * @author wjschakel
     */
    private final class XsdTreeCellRenderer extends DefaultTreeCellRenderer
    {
        /** */
        private static final long serialVersionUID = 20230221L;

        /** Image for nodes with a consumer, typically an editor. */
        private final Image consumer;

        /** Image for nodes with a description. */
        private final Image description;

        /** Icon for option nodes, indicating a drop-down can be show. */
        private final Image dropdown;

        /**
         * Constructor. Loads icons.
         * @throws IOException if an icon could not be loaded from resoures.
         */
        private XsdTreeCellRenderer() throws IOException
        {
            this.consumer = ImageIO.read(Resource.getResourceAsStream("./Application.png")).getScaledInstance(12, 12,
                    Image.SCALE_SMOOTH);
            this.description =
                    ImageIO.read(Resource.getResourceAsStream("./Info.png")).getScaledInstance(10, 10, Image.SCALE_SMOOTH);
            this.dropdown = ImageIO.read(Resource.getResourceAsStream("./dropdown.png"));

            this.leafIcon = new ImageIcon(ImageIO.read(Resource.getResourceAsStream("/Eclipse_file.png")));
            this.openIcon = new ImageIcon(ImageIO.read(Resource.getResourceAsStream("/Eclipse_folder_open.png")));
            this.closedIcon = new ImageIcon(ImageIO.read(Resource.getResourceAsStream("/Eclipse_folder.png")));
        }

        /** {@inheritDoc} */
        @Override
        public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected,
                final boolean expanded, final boolean leaf, final int row, final boolean hasFocus)
        {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            XsdTreeNode node = (XsdTreeNode) value;
            Icon customIcon = getCustomIcon(node.getPathString());
            if (customIcon != null)
            {
                setIcon(customIcon);
            }
            if (node.hasConsumer())
            {
                preAppend(this.consumer, false);
            }
            if (node.getDescription() != null)
            {
                preAppend(this.description, false);
            }
            if (node.isChoice() && !node.isInclude())
            {
                preAppend(this.dropdown, false);
            }
            if (node.isActive())
            {
                if (node.isInclude())
                {
                    setForeground(OtsEditor.INACTIVE_COLOR);
                }
                else
                {
                    setForeground(UIManager.getColor("Table.foreground"));
                }
                if (node.equals(OtsEditor.this.choiceNode))
                {
                    setOpaque(true);
                    setBackground(new Color(UIManager.getColor("Panel.background").getRGB())); // ColorUIResource is ignored
                    setBorder(new LineBorder(UIManager.getColor("Menu.acceleratorForeground"), 1, false));
                }
                else
                {
                    if (node.isValid())
                    {
                        setOpaque(false);
                    }
                    else
                    {
                        setOpaque(true);
                        setBackground(OtsEditor.INVALID_COLOR);
                    }
                    setBorder(new EmptyBorder(0, 0, 0, 0));
                }
            }
            else
            {
                setOpaque(false);
                setForeground(OtsEditor.INACTIVE_COLOR);
                setBorder(new EmptyBorder(0, 0, 0, 0));
            }
            return this;
        }

        /**
         * Pre-/appends the provided image to the left-hand or right-hand side of the current icon.
         * @param iconImage Image; image to pre-/append.
         * @param prepend boolean; when true, the image is prepended rather than appended.
         */
        private void preAppend(final Image iconImage, final boolean prepend)
        {
            Icon base = getIcon();
            int w = base.getIconWidth() + ICON_MARGIN + iconImage.getWidth(null);
            int h = Math.max(base.getIconHeight(), iconImage.getHeight(null));
            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            int dy = (h - base.getIconHeight()) / 2;
            int x = prepend ? iconImage.getWidth(null) + ICON_MARGIN : 0;
            base.paintIcon(this, g, x, dy);
            dy = (h - iconImage.getHeight(null)) / 2;
            x = prepend ? 0 : base.getIconWidth() + ICON_MARGIN;
            g.drawImage(iconImage, x, dy, null);
            setIcon(new ImageIcon(image));
        }
    }

    /**
     * Listener for mouse events on the tree.
     * @author wjschakel
     */
    private final class XsdTreeMouseListener extends MouseAdapter
    {
        /** {@inheritDoc} */
        @Override
        public void mousePressed(final MouseEvent e)
        {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1)
            {
                // show value options popup
                int row = OtsEditor.this.treeTable.rowAtPoint(e.getPoint());
                int treeCol = OtsEditor.this.treeTable.convertColumnIndexToView(0); // columns may have been moved in view
                XsdTreeNode treeNode = (XsdTreeNode) OtsEditor.this.treeTable.getValueAt(row, treeCol);
                int col = OtsEditor.this.treeTable.columnAtPoint(e.getPoint());
                if (OtsEditor.this.treeTable.isCellEditable(row, col))
                {
                    if (OtsEditor.this.treeTable.convertColumnIndexToModel(col) == 2)
                    {
                        List<String> allOptions = treeNode.getValueRestrictions();
                        optionsPopup(allOptions, OtsEditor.this.treeTable, (t) -> treeNode.setValue(t));
                    }
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void mouseClicked(final MouseEvent e)
        {
            // show choice popup
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                if (e.getClickCount() > 1 && OtsEditor.this.treeTable
                        .convertColumnIndexToModel(OtsEditor.this.treeTable.columnAtPoint(e.getPoint())) == 0)
                {
                    int row = OtsEditor.this.treeTable.rowAtPoint(e.getPoint());
                    int col = OtsEditor.this.treeTable.convertColumnIndexToView(0); // columns may have been moved in view
                    XsdTreeNode treeNode = (XsdTreeNode) OtsEditor.this.treeTable.getValueAt(row, col);
                    if (!treeNode.isActive() && !treeNode.isInclude())
                    {
                        treeNode.setActive();
                        OtsEditor.this.treeTable.updateUI();
                    }
                    return;
                }
                int row = OtsEditor.this.treeTable.rowAtPoint(e.getPoint());
                int treeCol = OtsEditor.this.treeTable.convertColumnIndexToView(0); // columns may have been moved in view
                XsdTreeNode treeNode = (XsdTreeNode) OtsEditor.this.treeTable.getValueAt(row, treeCol);
                if (!treeNode.isActive() || treeNode.isInclude())
                {
                    return;
                }
                Rectangle labelPortion = OtsEditor.this.treeTable.getTree()
                        .getPathBounds(OtsEditor.this.treeTable.getTree().getPathForLocation(e.getPoint().x, e.getPoint().y));
                if (labelPortion != null && labelPortion.contains(e.getPoint()) && OtsEditor.this.mayPresentChoice
                        && treeNode.isChoice())
                {
                    JPopupMenu popup = new JPopupMenu();
                    boolean firstEntry = true;
                    for (XsdOption option : treeNode.getOptions())
                    {
                        if (option.isFirstInGroup())
                        {
                            if (!firstEntry)
                            {
                                popup.add(new JSeparator());
                            }
                        }
                        firstEntry = false;
                        JMenuItem button = new JMenuItem(option.getOptionNode().getShortString());
                        if (!option.isSelected())
                        {
                            button.addActionListener(new ChoiceListener(option.getChoice(), option.getOptionNode(), row));
                        }
                        button.setFont(OtsEditor.this.treeTable.getFont());
                        popup.add(button);
                        firstEntry = false;
                    }
                    preparePopupRemoval(popup, OtsEditor.this.treeTable);
                    OtsEditor.this.treeTable.setComponentPopupMenu(popup);
                    OtsEditor.this.choiceNode = treeNode;
                    popup.show(OtsEditor.this.treeTable, (int) labelPortion.getMinX(), (int) labelPortion.getMaxY() - 1);
                }
            }
            // show actions popup
            else if (e.getButton() == MouseEvent.BUTTON3)
            {
                int row = OtsEditor.this.treeTable.rowAtPoint(e.getPoint());
                int col = OtsEditor.this.treeTable.columnAtPoint(e.getPoint());
                int treeCol = OtsEditor.this.treeTable.convertColumnIndexToView(0); // columns may have been moved in view
                XsdTreeNode treeNode = (XsdTreeNode) OtsEditor.this.treeTable.getValueAt(row, treeCol);
                if (col == treeCol)
                {
                    OtsEditor.this.treeTable.setRowSelectionInterval(row, row);
                    createRightClickPopup(e, treeNode);
                }
            }
        }

        /**
         * Creates a popup panel with options for a node. These contain active consumers (editors), moving up/down, and addition
         * and removal.
         * @param e MouseEvent; mouse event.
         * @param treeNode XsdTreeNode; node that was clicked on.
         */
        private void createRightClickPopup(final MouseEvent e, final XsdTreeNode treeNode)
        {
            JPopupMenu popup = new JPopupMenu();
            boolean anyAdded = false;

            if (treeNode.isActive())
            {
                for (String menuItem : treeNode.getConsumerMenuItems())
                {
                    JMenuItem item = new JMenuItem(menuItem);
                    item.addActionListener(new ActionListener()
                    {
                        /** {@inheritDoc} */
                        @Override
                        public void actionPerformed(final ActionEvent e)
                        {
                            treeNode.consume(menuItem);
                        }
                    });
                    item.setFont(OtsEditor.this.treeTable.getFont());
                    popup.add(item);
                    anyAdded = true;
                }
            }
            if (treeNode.getDescription() != null) // description is the only thing we show with the node disabled
            {
                JMenuItem item = new JMenuItem("Description...");
                item.addActionListener(new ActionListener()
                {
                    /** {@inheritDoc} */
                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        showDescription(treeNode.getDescription());
                    }
                });
                item.setFont(OtsEditor.this.treeTable.getFont());
                popup.add(item);
                anyAdded = true;
            }

            if (treeNode.isActive() && !treeNode.isInclude())
            {
                anyAdded = addDefaultActions(treeNode, popup, anyAdded);
            }

            if (anyAdded)
            {
                preparePopupRemoval(popup, OtsEditor.this.treeTable);
                popup.setFont(OtsEditor.this.treeTable.getFont());
                OtsEditor.this.treeTable.setComponentPopupMenu(popup);
                popup.show(OtsEditor.this.treeTable, e.getX(), e.getY());
            }
        }

        /**
         * Adds default menu actions Add, Remove, Move up, and Move down, for each when appropriate.
         * @param treeNode XsdTreeNode; node.
         * @param popup JPopupMenu; popup menu.
         * @param added boolean; whether any menu items were added before (which requires a separator).
         * @return boolean; whether any items were added, going in to this method, or during the method.
         */
        private boolean addDefaultActions(final XsdTreeNode treeNode, final JPopupMenu popup, final boolean added)
        {
            boolean anyAdded = added;
            boolean separatorNeeded = anyAdded;
            boolean groupAdded = false;

            if (treeNode.isAddable())
            {
                if (separatorNeeded)
                {
                    separatorNeeded = false;
                    popup.add(new JSeparator());
                }
                JMenuItem add = new JMenuItem("Add");
                add.addActionListener(new ActionListener()
                {
                    /** {@inheritDoc} */
                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        treeNode.add();
                        OtsEditor.this.treeTable.updateUI();
                    }
                });
                add.setFont(OtsEditor.this.treeTable.getFont());
                popup.add(add);
                JMenuItem copy = new JMenuItem("Copy");
                copy.addActionListener(new ActionListener()
                {
                    /** {@inheritDoc} */
                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        treeNode.copy();
                        OtsEditor.this.treeTable.updateUI();
                    }
                });
                copy.setFont(OtsEditor.this.treeTable.getFont());
                popup.add(copy);
                anyAdded = true;
                groupAdded = true;
            }
            if (treeNode.isRemovable())
            {
                if (separatorNeeded)
                {
                    separatorNeeded = false;
                    popup.add(new JSeparator());
                }
                JMenuItem item = new JMenuItem("Remove");
                item.addActionListener(new ActionListener()
                {
                    /** {@inheritDoc} */
                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        if (confirmNodeRemoval(treeNode))
                        {
                            int selected = OtsEditor.this.treeTable.getTree().getLeadSelectionRow();
                            treeNode.remove();
                            OtsEditor.this.treeTable.updateUI();
                            OtsEditor.this.treeTable.getSelectionModel().setSelectionInterval(selected, selected);
                            int column = OtsEditor.this.treeTable.convertColumnIndexToView(0);
                            if (OtsEditor.this.treeTable.getValueAt(selected, column).equals(treeNode))
                            {
                                OtsEditor.this.attributesTable
                                        .setModel(new AttributesTableModel(null, OtsEditor.this.treeTable));
                            }
                        }
                    }
                });
                item.setFont(OtsEditor.this.treeTable.getFont());
                popup.add(item);
                anyAdded = true;
                groupAdded = true;
            }

            separatorNeeded = groupAdded;

            if (treeNode.canMoveUp())
            {
                if (separatorNeeded)
                {
                    separatorNeeded = false;
                    popup.add(new JSeparator());
                }
                JMenuItem item = new JMenuItem("Move up");
                item.addActionListener(new ActionListener()
                {
                    /** {@inheritDoc} */
                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        treeNode.move(-1);
                        OtsEditor.this.treeTable.updateUI();
                    }
                });
                item.setFont(OtsEditor.this.treeTable.getFont());
                popup.add(item);
                anyAdded = true;
            }
            if (treeNode.canMoveDown())
            {
                if (separatorNeeded)
                {
                    separatorNeeded = false;
                    popup.add(new JSeparator());
                }
                JMenuItem item = new JMenuItem("Move down");
                item.addActionListener(new ActionListener()
                {
                    /** {@inheritDoc} */
                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        treeNode.move(1);
                        OtsEditor.this.treeTable.updateUI();
                    }
                });
                item.setFont(OtsEditor.this.treeTable.getFont());
                popup.add(item);
                anyAdded = true;
            }
            return anyAdded;
        }
    }

    /**
     * Listener for selecting choice options.
     * @author wjschakel
     */
    class ChoiceListener implements ActionListener
    {
        /** Choice node of the clicked option. */
        private XsdTreeNode choiceNode;

        /** Option. */
        private XsdTreeNode option;

        /** Row to reset the selection at. */
        private int reselectionRow;

        /**
         * @param choiceNode XsdTreeNode; choice node of the choice.
         * @param option XsdTreeNode; possibly selected option.
         * @param reselectionRow int; row to reset selection.
         */
        ChoiceListener(final XsdTreeNode choiceNode, final XsdTreeNode option, final int reselectionRow)
        {
            this.choiceNode = choiceNode;
            this.option = option;
            this.reselectionRow = reselectionRow;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(final ActionEvent e)
        {
            this.choiceNode.setOption(this.option);
            OtsEditor.this.treeTable.setRowSelectionInterval(this.reselectionRow, this.reselectionRow);
            OtsEditor.this.treeTable.updateUI();
            OtsEditor.this.attributesTable.setModel(new AttributesTableModel(this.option, OtsEditor.this.treeTable));
        }
    }

}