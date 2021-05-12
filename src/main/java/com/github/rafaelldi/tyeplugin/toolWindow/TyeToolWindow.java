package com.github.rafaelldi.tyeplugin.toolWindow;

import com.github.rafaelldi.tyeplugin.messaging.TyeServicesNotifier;
import com.github.rafaelldi.tyeplugin.model.Replica;
import com.github.rafaelldi.tyeplugin.model.Service;
import com.github.rafaelldi.tyeplugin.model.Tye;
import com.github.rafaelldi.tyeplugin.services.TyeApiService;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.util.messages.MessageBus;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TyeToolWindow extends SimpleToolWindowPanel {
    private JPanel panel;
    private SimpleTree tree;

    private final Project project;
    private final TyeApiService tyeApiService;

    public TyeToolWindow(Project project) {
        super(false);

        this.project = project;
        tyeApiService = project.getService(TyeApiService.class);

        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(TyeServicesNotifier.Companion.getTOPIC(), () -> {
            Tye tye = tyeApiService.getTye();
            updateTree(tye);
        });

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

    private void updateTree(Tye tye) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Services");
        for (Service service : tye.getServices()) {
            DefaultMutableTreeNode serviceNode = new DefaultMutableTreeNode(service.getName());

            for (Replica replica: service.getReplicas()){
                DefaultMutableTreeNode replicaNode = new DefaultMutableTreeNode(replica.getName());
                serviceNode.add(replicaNode);
            }

            root.add(serviceNode);
        }

        tree.setModel(new DefaultTreeModel(root));
    }
}
