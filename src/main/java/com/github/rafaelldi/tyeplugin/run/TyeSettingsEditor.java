package com.github.rafaelldi.tyeplugin.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBIntSpinner;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBRadioButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.github.rafaelldi.tyeplugin.run.OptionsConstants.*;

public class TyeSettingsEditor extends SettingsEditor<TyeRunConfiguration> {
    private JPanel panel;
    private TextFieldWithBrowseButton pathField;
    private JBIntSpinner portSpinner;
    private JBCheckBox noBuildCheckBox;
    private JBCheckBox dockerCheckBox;
    private JBCheckBox dashboardCheckBox;
    private JBRadioButton debugRadioButton;
    private JBRadioButton infoRadioButton;
    private JBRadioButton quietRadioButton;

    @Override
    protected void resetEditorFrom(@NotNull TyeRunConfiguration runConfig) {
        VirtualFile pathArgument = runConfig.getPathArgument();
        if (pathArgument != null){
            pathField.setText(pathArgument.getPath());
        } else {
            pathField.setText("");
        }
        portSpinner.setNumber(runConfig.getPortArgument());
        noBuildCheckBox.setSelected(runConfig.getNoBuildArgument());
        dockerCheckBox.setSelected(runConfig.getDockerArgument());
        dashboardCheckBox.setSelected(runConfig.getDashboardArgument());

        String verbosity = runConfig.getVerbosityArgument();
        if (verbosity.equals(DEBUG_VERBOSITY)) {
            debugRadioButton.setSelected(true);
        } else if (verbosity.equals(INFO_VERBOSITY)) {
            infoRadioButton.setSelected(true);
        } else {
            quietRadioButton.setSelected(true);
        }
    }

    @Override
    protected void applyEditorTo(@NotNull TyeRunConfiguration runConfig) {
        runConfig.setPathArgument(LocalFileSystem.getInstance().findFileByPath(pathField.getText()));
        runConfig.setPortArgument(portSpinner.getNumber());
        runConfig.setNoBuildArgument(noBuildCheckBox.isSelected());
        runConfig.setDockerArgument(dockerCheckBox.isSelected());
        runConfig.setDashboardArgument(dashboardCheckBox.isSelected());

        if (debugRadioButton.isSelected()) {
            runConfig.setVerbosityArgument(DEBUG_VERBOSITY);
        } else if (infoRadioButton.isSelected()) {
            runConfig.setVerbosityArgument(INFO_VERBOSITY);
        } else {
            runConfig.setVerbosityArgument(QUIET_VERBOSITY);
        }
    }

    @Override
    protected @NotNull JComponent createEditor() {
        return panel;
    }

    private void createUIComponents() {
        pathField = new TextFieldWithBrowseButton();
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, true, false, false, false, false);
        descriptor.withFileFilter(vf ->
                vf.getName().equalsIgnoreCase("tye.yaml") ||
                        (vf.getExtension() != null &&
                                (vf.getExtension().equalsIgnoreCase("sln") ||
                                        vf.getExtension().equalsIgnoreCase("csproj") ||
                                        vf.getExtension().equalsIgnoreCase("fsproj"))));
        pathField.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));

        portSpinner = new JBIntSpinner(DEFAULT_PORT, 0, 65353);
    }
}
