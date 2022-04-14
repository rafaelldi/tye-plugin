@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.settings

import com.github.rafaelldi.tyeplugin.services.TyeCliService
import com.github.rafaelldi.tyeplugin.services.TyePathProvider
import com.github.rafaelldi.tyeplugin.util.isTyeFile
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.NlsContexts.DialogTitle
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.textFieldWithBrowseButton
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import javax.swing.JTextField

class TyeSettingsConfigurable(private val project: Project) : BoundConfigurable("Tye") {
    private val settings: TyeSettings get() = TyeSettings.getInstance(project)
    private lateinit var pathTextField: JBTextFieldWithSecondaryValue

    override fun createPanel(): DialogPanel {
        pathTextField = JBTextFieldWithSecondaryValue()
        val panel = panel {
            row("Tye tool path:") {
                textFieldWithBrowseButton(
                    "Select Path",
                    pathTextField,
                    project,
                    FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor().withFileFilter { it.isTyeFile() })
                { chosenFile ->
                    if (TyePathProvider.isValidTyeToolPath(chosenFile.path)) {
                        runBackgroundableTask("Get tye version", project) {
                            setTyeVersion(chosenFile.path)
                        }
                    }
                    chosenFile.path
                }
                    .validationOnInput {
                        if (TyePathProvider.isValidTyeToolPath(it.text))
                            return@validationOnInput null
                        else
                            return@validationOnInput error("Invalid path for tye executable")
                    }
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(settings::tyeToolPath)
            }
            group("Options") {
                row {
                    checkBox("Overwrite existing tye.yaml during scaffolding")
                        .bindSelected(settings::overwriteTyeFile)
                }
                row {
                    checkBox("Check new versions of the tye global tool during startup")
                        .bindSelected(settings::checkTyeNewVersions)
                }
            }
        }

        val currentPath = settings.tyeToolPath
        if (TyePathProvider.isValidTyeToolPath(currentPath)) {
            runBackgroundableTask("Get tye version", project) {
                setTyeVersion(currentPath)
            }
        }
        return panel
    }

    private fun setTyeVersion(tyePath: String) {
        val tyeCliService = project.service<TyeCliService>()
        val version = tyeCliService.getVersion(tyePath)
        if (version != null) {
            invokeLater {
                pathTextField.setSecondaryValue(version.toString())
            }
        }
    }
}

fun Row.textFieldWithBrowseButton(
    @DialogTitle browseDialogTitle: String,
    textField: JTextField,
    project: Project?,
    fileChooserDescriptor: FileChooserDescriptor,
    fileChosen: ((chosenFile: VirtualFile) -> String)?
): Cell<TextFieldWithBrowseButton> {
    val textFieldWithBrowseButton =
        textFieldWithBrowseButton(project, browseDialogTitle, textField, fileChooserDescriptor, fileChosen)
    val result = cell(textFieldWithBrowseButton)
    result.columns(18)
    return result
}