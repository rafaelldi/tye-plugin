package com.github.rafaelldi.tyeplugin.run

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.vfs.LocalFileSystem
import javax.swing.JComponent
import javax.swing.JPanel

class TyeSettingsEditor(private val project: Project) : SettingsEditor<TyeRunConfiguration>() {
    private lateinit var panel: JPanel
    private lateinit var tyeFile: LabeledComponent<TextFieldWithBrowseButton>

    override fun createEditor(): JComponent {
        createUIComponents()
        return panel
    }

    override fun resetEditorFrom(runConfig: TyeRunConfiguration) {
        tyeFile.component.text = runConfig.tyeFile?.path ?: ""
    }

    override fun applyEditorTo(runConfig: TyeRunConfiguration) {
        runConfig.tyeFile = LocalFileSystem.getInstance().findFileByPath(tyeFile.component.text)
    }

    private fun createUIComponents() {
        panel = JPanel().apply {
            layout = VerticalFlowLayout(VerticalFlowLayout.TOP)

            // Tye file
            val tyeFileField = TextFieldWithBrowseButton().apply {
                addBrowseFolderListener(
                    TextBrowseFolderListener(
                        FileChooserDescriptor(true, false, false, false, false, false)
                            .withFileFilter { vf -> vf.extension?.toLowerCase() == "yaml" }
                            .withTitle("Select tye.yaml file...")
                    )
                )
            }
            tyeFile = LabeledComponent.create(tyeFileField, "Tye file")
            add(tyeFile)
        }
    }
}
