package com.github.rafaelldi.tyeplugin.toolWindow;

import com.github.rafaelldi.tyeplugin.listeners.TyeServicesNotifier;
import com.github.rafaelldi.tyeplugin.model.TyeService;
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
import java.util.List;

public class TyeToolWindow extends SimpleToolWindowPanel {
    private JPanel panel;
    private SimpleTree tree;
    private MessageBus messageBus;

    private final Project project;
    private final TyeApiService apiService;

    public TyeToolWindow(Project project) {
        super(false);

        this.project = project;
        apiService = project.getService(TyeApiService.class);

        messageBus = project.getMessageBus();
        messageBus.connect().subscribe(TyeServicesNotifier.Companion.getTOPIC(), () -> {
            List<TyeService> services = apiService.getServices();
            updateTree(services);
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

    private void updateTree(List<TyeService> services){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Services");
        for(TyeService service: services){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(service.getName());
            root.add(node);
        }

        tree.setModel(new DefaultTreeModel(root));
    }
}
