package com.github.rafaelldi.tyeplugin.settings

import com.github.rafaelldi.tyeplugin.services.TyeCliService
import com.github.rafaelldi.tyeplugin.services.TyePathProvider.Companion.TYE_TOOL
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.NlsContexts.DialogTitle
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.textFieldWithBrowseButton
import com.intellij.ui.layout.*
import java.io.File
import javax.swing.JTextField

class TyeSettingsConfigurable(private val project: Project) : BoundConfigurable("Tye") {
    private val settings: TyeSettings get() = TyeSettings.getInstance(project)

    override fun createPanel(): DialogPanel = panel {
        row("Tye tool path") {
            val pathTextField = JBTextFieldWithSecondaryValue()
            textFieldWithBrowseButton(settings::tyeToolPath.toNullableBinding(""),
                "Select Path",
                pathTextField as JTextField,
                project,
                FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor().withFileFilter {
                    it.nameWithoutExtension.lowercase() == TYE_TOOL
                })
            { chosenFile ->
                ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Get tye version") {
                    override fun run(indicator: ProgressIndicator) {
                        val tyeCliService = project.service<TyeCliService>()
                        val version = tyeCliService.getVersion(chosenFile.path)
                        if (version != null) {
                            invokeLater {
                                pathTextField.setSecondaryValue(version.toString())
                            }
                        }
                    }
                })
                chosenFile.path
            }
                .withValidationOnInput {
                    val file = File(it.text)
                    if (file.exists() && file.isDirectory.not() && file.canExecute()) return@withValidationOnInput null
                    return@withValidationOnInput error("Invalid path for tye executable")
                }
        }
        titledRow("Options") {
            row {
                checkBox("Overwrite existing tye.yaml during scaffolding", settings::overwriteTyeFile)
            }
            row {
                checkBox("Check new versions of the tye global tool during startup", settings::checkTyeNewVersions)
            }
        }
    }
}


fun Cell.textFieldWithBrowseButton(
    modelBinding: PropertyBinding<String>,
    @DialogTitle browseDialogTitle: String,
    textField: JTextField,
    project: Project? = null,
    fileChooserDescriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
    fileChosen: ((chosenFile: VirtualFile) -> String)? = null
): CellBuilder<TextFieldWithBrowseButton> {
    val textFieldWithBrowseButton =
        textFieldWithBrowseButton(project, browseDialogTitle, textField, fileChooserDescriptor, fileChosen)
    textFieldWithBrowseButton.text = modelBinding.get()
    return component(textFieldWithBrowseButton).constraints(growX)
        .withBinding(TextFieldWithBrowseButton::getText, TextFieldWithBrowseButton::setText, modelBinding)
}