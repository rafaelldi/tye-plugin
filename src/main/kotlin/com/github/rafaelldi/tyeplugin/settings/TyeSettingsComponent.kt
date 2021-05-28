package com.github.rafaelldi.tyeplugin.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBTextField
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class TyeSettingsComponent {
    private lateinit var panel: JPanel
    private lateinit var tyeToolPathField: LabeledComponent<TextFieldWithBrowseButton>
    private lateinit var tyeHostField: LabeledComponent<JBTextField>
    private lateinit var overwriteTyeFileOption: JCheckBox

    init {
        createUIComponents()
    }

    fun getPanel(): JPanel = panel

    fun getPreferredFocusedComponent(): JComponent = tyeToolPathField.component

    fun getTyeToolPath(): String? = if (tyeToolPathField.component.text == "") null else tyeToolPathField.component.text

    fun setTyeToolPath(path: String?) {
        tyeToolPathField.component.text = path ?: ""
    }

    fun getTyeHost(): String = tyeHostField.component.text

    fun setTyeHost(host: String) {
        tyeHostField.component.text = host
    }

    fun getOverwriteTyeFile(): Boolean = overwriteTyeFileOption.isSelected

    fun setOverwriteTyeFile(select: Boolean) {
        overwriteTyeFileOption.isSelected = select
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

            val tyeHostTextField = JBTextField()
            tyeHostField =  LabeledComponent.create(tyeHostTextField, "Tye host")
            tyeHostField.labelLocation = BorderLayout.WEST
            add(tyeHostField)

            add(TitledSeparator("Commands"))

            overwriteTyeFileOption = JCheckBox("Overwrite existing tye.yaml during scaffolding")
            add(overwriteTyeFileOption)
        }
    }
}
