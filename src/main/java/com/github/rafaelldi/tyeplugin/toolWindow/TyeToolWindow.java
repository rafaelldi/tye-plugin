package com.github.rafaelldi.tyeplugin.toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;

import javax.swing.*;

public class TyeToolWindow extends SimpleToolWindowPanel {
    private JPanel panel;

    public TyeToolWindow() {
        super(false);

        setContent(panel);
    }
}
