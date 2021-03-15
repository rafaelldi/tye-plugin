package com.github.rafaelldi.tyeplugin.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class TyeSettingsComponent {
    private lateinit var panel: JPanel
    private lateinit var tyeToolPathField: LabeledComponent<TextFieldWithBrowseButton>

    init {
        createUIComponents()
    }

    fun getPanel(): JPanel = panel

    fun getPreferredFocusedComponent(): JComponent = tyeToolPathField.component

    fun getTyeToolPath(): String = tyeToolPathField.component.text

    fun setTyeToolPath(path: String) {
        tyeToolPathField.component.text = path
    }

    private fun createUIComponents() {
        panel = JPanel().apply {
            layout = VerticalFlowLayout(VerticalFlowLayout.TOP)
            val tyeToolPathTextField = TextFieldWithBrowseButton().apply {
                addBrowseFolderListener(
                    TextBrowseFolderListener(
                        FileChooserDescriptor(true, false, false, false, false, false)
                            .withFileFilter { vf -> vf.nameWithoutExtension.toLowerCase() == "tye" }
                            .withTitle("Select Path")
                    )
                )
            }
            tyeToolPathField = LabeledComponent.create(tyeToolPathTextField, "Tye tool path")
            tyeToolPathField.labelLocation = BorderLayout.WEST
            add(tyeToolPathField)
        }
    }
}
