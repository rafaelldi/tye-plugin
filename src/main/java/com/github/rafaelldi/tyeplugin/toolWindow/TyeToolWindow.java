package com.github.rafaelldi.tyeplugin.toolWindow;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.treeStructure.SimpleTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TyeToolWindow extends SimpleToolWindowPanel {
    private JPanel panel;
    private SimpleTree tree;

    private final Project project;

    public TyeToolWindow(Project project) {
        super(false);

        this.project = project;

        initActionToolbar();
        setContent(panel);
    }

    private void initActionToolbar() {
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup actionGroup = (ActionGroup) actionManager.getAction("com.github.rafaelldi.tyeplugin.actions.TyeToolWindowGroupedActions");
        ActionToolbar actionToolbar = actionManager.createActionToolbar("TyeActionToolbar", actionGroup, true);
        setToolbar(actionToolbar.getComponent());
    }

    private void createUIComponents() {
        tree = new SimpleTree();
        tree.getEmptyText().setText("No services");

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Services");
        tree.setModel(new DefaultTreeModel(root));
    }
}
