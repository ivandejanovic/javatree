/**
 * The MIT License (MIT)
 *
 * Copyright 2008-2016 Ivan Dejanovic and Quine Interactive
 * www.quineinteractive.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package com.quine.javatree;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * JavaTreeController class implements basic control functionality for JavaTree application.
 * 
 * @author Ivan Dejanovic
 * 
 * @version 1.0
 * 
 * @since 1.0
 * 
 */
public class JavaTreeController implements TreeSelectionListener, MouseListener {
    // reference to JavaTreeFrame that is the parent of the controller
    private JavaTreeFrame            parent;

    // tree object that hold the node data
    private JTree                    tree;

    // firstSelection is used by TreeSelectionListener actionPerformed method to
    // determine if
    // current selection is the first selection on a tree
    private boolean                  firstSelection;

    // panel used by JavaTreeFrame
    private JavaTreePanel            panel;

    // controller that provides necessary functionality for actions
    private JavaTreeActionController actionController;

    // file menu actions
    private Action                   newAction;
    private Action                   openAction;
    private Action                   saveAction;
    private Action                   exitAction;

    // node menu actions
    private Action                   addChildAction;
    private Action                   deleteNodeAction;
    private Action                   moveUpAction;
    private Action                   moveDownAction;
    private Action                   moveLevelUpAction;
    private Action                   moveLevelDownAction;

    // help menu actions
    private Action                   helpAction;
    private Action                   aboutAction;

    // JMenuBar to be to be passed to parent
    private JMenuBar                 menuBar;

    /**
     * Creates JavaTreeController. Set parent to p.
     * 
     * @param p
     */
    public JavaTreeController(JavaTreeFrame p) {
        // set parent
        parent = p;

        // set firstSelection
        firstSelection = true;

        // create tree and set action listener
        tree = new JTree(new DefaultMutableTreeNode(new JavaTreeNodeObject("main")));
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(this);

        // create and set panel
        panel = new JavaTreePanel(tree);
        panel.setOpaque(true);

        // create and set
        actionController = new JavaTreeActionController();

        // create actions
        createActions();

        // create menu
        menuBar = createMenuItems();

        // disable all node actions. they are enabled appropriately when user
        // selects a node
        enableAllNodeActions(false);
    }

    /**
     * @return panel
     */
    public JavaTreePanel getPanel() {
        return panel;
    }

    /**
     * @return the menuBar
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void valueChanged(TreeSelectionEvent event) {
        // Set old selected node and copy data to it
        if (!firstSelection) {
            TreePath oldPath = event.getOldLeadSelectionPath();
            if (oldPath != null) {
                DefaultMutableTreeNode oldSelectedNode = (DefaultMutableTreeNode) oldPath.getLastPathComponent();
                if (oldSelectedNode != null) {
                    ((JavaTreeNodeObject) oldSelectedNode.getUserObject()).setTitle(panel.getTextFieldText());
                    ((JavaTreeNodeObject) oldSelectedNode.getUserObject()).setText(panel.getTextAreaText());
                }
            }
        }

        // firstSelection is always false after abode code executes for the
        // first time on a tree
        firstSelection = false;

        // Set new selected node, copy data from it, and enable and disable
        // appropriate actions
        TreePath newPath = event.getNewLeadSelectionPath();
        if (newPath != null) {
            DefaultMutableTreeNode newSelectedNode = (DefaultMutableTreeNode) newPath.getLastPathComponent();
            if (newSelectedNode != null) {
                panel.setTextField((((JavaTreeNodeObject) newSelectedNode.getUserObject()).getTitle()));
                panel.setTextArea((((JavaTreeNodeObject) newSelectedNode.getUserObject()).getText()));

                if (newSelectedNode.isRoot()) {
                    enableDisableRootNodeActions();
                } else {
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) newSelectedNode.getParent();
                    if (parent.isRoot()) {
                        enableDisableRootChildNodeActions();
                    } else {
                        enableAllNodeActions(true);
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            boolean root = false;
            boolean rootChild = false;

            if (clickedNode.isRoot()) {
                enableDisableRootNodeActions();
                root = true;
            } else {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) clickedNode.getParent();
                if (parent.isRoot()) {
                    enableDisableRootChildNodeActions();
                    rootChild = true;
                }
            }
            Rectangle pathBounds = tree.getUI().getPathBounds(tree, path);
            if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
                JPopupMenu menu = new JPopupMenu();

                JMenuItem addChildMenuItem = new JMenuItem(new AddChildToGivenNodeAction(clickedNode));
                addChildMenuItem.setText("Add child");
                menu.add(addChildMenuItem);

                if (!root) {
                    JMenuItem deleteNodeMenuItem = new JMenuItem(new DeleteGivenNodeAction(clickedNode));
                    deleteNodeMenuItem.setText("Delete node");
                    menu.add(deleteNodeMenuItem);
                }

                if (!root) {
                    JMenuItem moveUpMenuItem = new JMenuItem(new MoveGivenNodeUpAction(clickedNode));
                    moveUpMenuItem.setText("Move up");
                    menu.add(moveUpMenuItem);
                }

                if (!root) {
                    JMenuItem moveDownMenuItem = new JMenuItem(new MoveGivenNodeDownAction(clickedNode));
                    moveDownMenuItem.setText("Move down");
                    menu.add(moveDownMenuItem);
                }

                if (!root && !rootChild) {
                    JMenuItem moveLevelUpMenuItem = new JMenuItem(new MoveGivenNodeLevelUpAction(clickedNode));
                    moveLevelUpMenuItem.setText("Move level up");
                    menu.add(moveLevelUpMenuItem);
                }

                if (!root) {
                    JMenuItem moveLevelDownMenuItem = new JMenuItem(new MoveGivenNodeLevelDownAction(clickedNode));
                    moveLevelDownMenuItem.setText("Move level down");
                    menu.add(moveLevelDownMenuItem);
                }

                menu.show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * Creates all necessary actions.
     */
    private void createActions() {

        newAction = new NewAction();
        openAction = new OpenAction();
        saveAction = new SaveAction();
        exitAction = new ExitAction();
        addChildAction = new AddChildAction();
        deleteNodeAction = new DeleteNodeAction();
        moveUpAction = new MoveUpAction();
        moveDownAction = new MoveDownAction();
        moveLevelUpAction = new MoveLevelUpAction();
        moveLevelDownAction = new MoveLevelDownAction();
        helpAction = new HelpAction();
        aboutAction = new AboutAction();
    }

    /**
     * Creates and sets all MenuItems put them in appropriate Menus which in turn are put in menuBar which is returned.
     * 
     * @return menuBar
     */
    private JMenuBar createMenuItems() {

        JMenuBar menuBar = new JMenuBar();

        // create and set File menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem(newAction);
        newMenuItem.setText("New");
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem(openAction);
        openMenuItem.setText("Open");
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem(saveAction);
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        JMenuItem exitMenuItem = new JMenuItem(exitAction);
        exitMenuItem.setText("Exit");
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        // create and set Action Menu
        JMenu actionMenu = new JMenu("Action");

        JMenuItem addChildMenuItem = new JMenuItem(addChildAction);
        addChildMenuItem.setText("Add child");
        actionMenu.add(addChildMenuItem);

        JMenuItem deleteNodeMenuItem = new JMenuItem(deleteNodeAction);
        deleteNodeMenuItem.setText("Delete node");
        actionMenu.add(deleteNodeMenuItem);

        JMenuItem moveUpMenuItem = new JMenuItem(moveUpAction);
        moveUpMenuItem.setText("Move up");
        actionMenu.add(moveUpMenuItem);

        JMenuItem moveDownMenuItem = new JMenuItem(moveDownAction);
        moveDownMenuItem.setText("Move down");
        actionMenu.add(moveDownMenuItem);

        JMenuItem moveLevelUpMenuItem = new JMenuItem(moveLevelUpAction);
        moveLevelUpMenuItem.setText("Move level up");
        actionMenu.add(moveLevelUpMenuItem);

        JMenuItem moveLevelDownMenuItem = new JMenuItem(moveLevelDownAction);
        moveLevelDownMenuItem.setText("Move level down");
        actionMenu.add(moveLevelDownMenuItem);

        menuBar.add(actionMenu);

        // create and set Help menu
        JMenu helpMenu = new JMenu("Help");

        JMenuItem helpMenuItem = new JMenuItem(helpAction);
        helpMenuItem.setText("Help");
        helpMenu.add(helpMenuItem);

        JMenuItem aboutMenuItem = new JMenuItem(aboutAction);
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * Disable all actions that are not applicable to root node. Enable the rest.
     */
    private void enableDisableRootNodeActions() {

        enableAllNodeActions(true);

        deleteNodeAction.setEnabled(false);
        moveUpAction.setEnabled(false);
        moveDownAction.setEnabled(false);
        moveLevelUpAction.setEnabled(false);
        moveLevelDownAction.setEnabled(false);
    }

    /**
     * Disable all actions that are not applicable to root child node. Enable the rest.
     */
    private void enableDisableRootChildNodeActions() {

        enableAllNodeActions(true);

        moveLevelUpAction.setEnabled(false);
    }

    /**
     * Enable or disable all actions depending on enable.
     * 
     * @param enable
     */
    private void enableAllNodeActions(boolean enable) {

        addChildAction.setEnabled(enable);
        deleteNodeAction.setEnabled(enable);
        moveUpAction.setEnabled(enable);
        moveDownAction.setEnabled(enable);
        moveLevelUpAction.setEnabled(enable);
        moveLevelDownAction.setEnabled(enable);
    }

    class NewAction extends AbstractAction {
        private static final long serialVersionUID = -5172137858381143557L;

        @Override
        public void actionPerformed(ActionEvent event) {
            // creates new tree
            tree = actionController.newAction();
            firstSelection = true;
            tree.addTreeSelectionListener(JavaTreeController.this);
            panel.setTree(tree);
            enableAllNodeActions(false);
        }
    }

    class OpenAction extends AbstractAction {
        private static final long serialVersionUID = -3474036987595542747L;

        @Override
        public void actionPerformed(ActionEvent e) {
            // load tree from file
            JTree t = actionController.openAction();
            if (t != null) {
                tree = t;
                firstSelection = true;
                tree.addTreeSelectionListener(JavaTreeController.this);
                tree.addMouseListener(JavaTreeController.this);
                panel.setTree(tree);
                enableAllNodeActions(false);
            }
        }
    }

    class SaveAction extends AbstractAction {
        private static final long serialVersionUID = 27568127447100591L;

        @Override
        public void actionPerformed(ActionEvent e) {
            TreePath path = tree.getSelectionPath();
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (node != null) {
                    ((JavaTreeNodeObject) node.getUserObject()).setTitle(panel.getTextFieldText());
                    ((JavaTreeNodeObject) node.getUserObject()).setText(panel.getTextAreaText());
                }
            }
            // save tree to file
            actionController.saveAction(tree);
        }
    }

    class ExitAction extends AbstractAction {
        private static final long serialVersionUID = 2307736078922711791L;

        @Override
        public void actionPerformed(ActionEvent e) {
            // closes the frame
            parent.dispose();
        }
    }

    class AddChildAction extends AbstractAction {
        private static final long serialVersionUID = 5615170526349378561L;

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.addChildAction(tree);
        }
    }

    class AddChildToGivenNodeAction extends AbstractAction {
        private static final long      serialVersionUID = 4369850568304248929L;
        private DefaultMutableTreeNode node;

        public AddChildToGivenNodeAction(DefaultMutableTreeNode node) {
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.addChildToGivenNodeAction(tree, node);
        }
    }

    class DeleteNodeAction extends AbstractAction {
        private static final long serialVersionUID = 3970342318933658245L;

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.deleteNodeAction(tree);
        }
    }

    class DeleteGivenNodeAction extends AbstractAction {
        private static final long      serialVersionUID = -5583980603769870L;
        private DefaultMutableTreeNode node;

        public DeleteGivenNodeAction(DefaultMutableTreeNode node) {
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.deleteGivenNodeAction(tree, node);
        }
    }

    class MoveUpAction extends AbstractAction {
        private static final long serialVersionUID = -3615888054256103467L;

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.moveUpAction(tree);
        }
    }

    class MoveGivenNodeUpAction extends AbstractAction {
        private static final long      serialVersionUID = 2938691710946526429L;
        private DefaultMutableTreeNode node;

        public MoveGivenNodeUpAction(DefaultMutableTreeNode node) {
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.moveGivenNodeUpAction(tree, node);
        }
    }

    class MoveDownAction extends AbstractAction {
        private static final long serialVersionUID = 6608774811720279872L;

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.moveDownAction(tree);
        }
    }

    class MoveGivenNodeDownAction extends AbstractAction {
        private static final long      serialVersionUID = -424040859626671541L;
        private DefaultMutableTreeNode node;

        public MoveGivenNodeDownAction(DefaultMutableTreeNode node) {
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.moveGivenNodeDownAction(tree, node);
        }
    }

    class MoveLevelUpAction extends AbstractAction {
        private static final long serialVersionUID = -6604717712882510453L;

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.moveLevelUpAction(tree);
        }
    }

    class MoveGivenNodeLevelUpAction extends AbstractAction {
        private static final long      serialVersionUID = 1917736190988919903L;
        private DefaultMutableTreeNode node;

        public MoveGivenNodeLevelUpAction(DefaultMutableTreeNode node) {
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.moveGivenNodeLevelUpAction(tree, node);
        }
    }

    class MoveLevelDownAction extends AbstractAction {
        private static final long serialVersionUID = 8561337995781692578L;

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.moveLevelDownAction(tree);
        }
    }

    class MoveGivenNodeLevelDownAction extends AbstractAction {
        private static final long      serialVersionUID = -3016491520292684865L;
        private DefaultMutableTreeNode node;

        public MoveGivenNodeLevelDownAction(DefaultMutableTreeNode node) {
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.moveGivenNodeLevelDownAction(tree, node);
        }
    }

    class HelpAction extends AbstractAction {
        private static final long serialVersionUID = -8604936465557558816L;

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.HelpAction();
        }
    }

    class AboutAction extends AbstractAction {
        private static final long serialVersionUID = 2928284969793202372L;

        @Override
        public void actionPerformed(ActionEvent e) {
            actionController.AboutAction();
        }
    }
}
