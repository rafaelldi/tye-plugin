package com.github.rafaelldi.tyeplugin.run

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBCheckBox
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class TyeSettingsEditor : SettingsEditor<TyeRunConfiguration>() {
    companion object {
        const val DEFAULT_PORT: Int = 8000
        const val MIN_PORT: Int = 0
        const val MAX_PORT: Int = 65353
    }

    private lateinit var panel: JPanel
    private lateinit var pathField: LabeledComponent<TextFieldWithBrowseButton>
    private lateinit var portField: LabeledComponent<JBIntSpinner>
    private lateinit var noBuildOption: JBCheckBox
    private lateinit var dockerOption: JBCheckBox
    private lateinit var dashboardOption: JBCheckBox

    override fun createEditor(): JComponent {
        createUIComponents()
        return panel
    }

    override fun resetEditorFrom(runConfig: TyeRunConfiguration) {
        pathField.component.text = runConfig.pathArgument?.path ?: ""
        portField.component.number = runConfig.portArgument
        noBuildOption.isSelected = runConfig.noBuildArgument
        dockerOption.isSelected = runConfig.dockerArgument
        dashboardOption.isSelected = runConfig.dashboardArgument
    }

    override fun applyEditorTo(runConfig: TyeRunConfiguration) {
        runConfig.pathArgument = LocalFileSystem.getInstance().findFileByPath(pathField.component.text)
        runConfig.portArgument = portField.component.number
        runConfig.noBuildArgument = noBuildOption.isSelected
        runConfig.dockerArgument = dockerOption.isSelected
        runConfig.dashboardArgument = dashboardOption.isSelected
    }

    private fun createUIComponents() {
        panel = JPanel().apply {
            layout = VerticalFlowLayout(VerticalFlowLayout.TOP)

            val pathTextField = TextFieldWithBrowseButton().apply {
                addBrowseFolderListener(
                    TextBrowseFolderListener(
                        FileChooserDescriptor(true, true, false, false, false, false)
                            .withFileFilter { vf ->
                                vf.name.toLowerCase() == "tye.yaml" ||
                                    vf.extension?.toLowerCase() == "sln" ||
                                    vf.extension?.toLowerCase() == "csproj" ||
                                    vf.extension?.toLowerCase() == "fsproj"
                            }
                            .withTitle("Select Path Argument")
                    )
                )
            }
            pathField = LabeledComponent.create(pathTextField, "Path")
            pathField.labelLocation = BorderLayout.WEST
            add(pathField)

            add(TitledSeparator("Options"))

            val portSpinner = JBIntSpinner(DEFAULT_PORT, MIN_PORT, MAX_PORT)
            portField = LabeledComponent.create(portSpinner, "Dashboard port")
            portField.labelLocation = BorderLayout.WEST
            add(portField)

            noBuildOption = JBCheckBox("--no-build, does not build projects before running")
            add(noBuildOption)

            dockerOption = JBCheckBox("--docker, run projects as docker containers")
            add(dockerOption)

            dashboardOption = JBCheckBox("--dashboard, launch dashboard on run")
            add(dashboardOption)
        }
    }
}
