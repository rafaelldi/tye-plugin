package com.github.rafaelldi.tyeplugin.run

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.vfs.LocalFileSystem
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class TyeSettingsEditor : SettingsEditor<TyeRunConfiguration>() {
    private lateinit var panel: JPanel
    private lateinit var pathField: LabeledComponent<TextFieldWithBrowseButton>

    override fun createEditor(): JComponent {
        createUIComponents()
        return panel
    }

    override fun resetEditorFrom(runConfig: TyeRunConfiguration) {
        pathField.component.text = runConfig.pathArgument?.path ?: ""
    }

    override fun applyEditorTo(runConfig: TyeRunConfiguration) {
        runConfig.pathArgument = LocalFileSystem.getInstance().findFileByPath(pathField.component.text)
    }

    private fun createUIComponents() {
        panel = JPanel().apply {
            layout = VerticalFlowLayout(VerticalFlowLayout.TOP)

            // Tye file
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
        }
    }
}
