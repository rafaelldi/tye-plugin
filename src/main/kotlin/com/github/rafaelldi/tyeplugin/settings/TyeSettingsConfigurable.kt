package com.github.rafaelldi.tyeplugin.settings

import com.github.rafaelldi.tyeplugin.listeners.TyeGlobalToolListener
import com.github.rafaelldi.tyeplugin.services.TyePathProvider.Companion.TYE_TOOL
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel
import com.intellij.util.messages.MessageBus

class TyeSettingsConfigurable(private val project: Project) : BoundConfigurable("Tye") {
    private val settings: TyeSettingsState = TyeSettingsState.getInstance(project)
    private val messageBus: MessageBus = project.messageBus

    override fun createPanel(): DialogPanel = panel {
        row("Tye tool path") {
            textFieldWithBrowseButton(
                settings::tyeToolPath,
                "Select Path",
                project,
                FileChooserDescriptor(true, false, false, false, false, false)
                    .withFileFilter { vf -> vf.nameWithoutExtension.lowercase() == TYE_TOOL }
                    .withTitle("Select Path")
            ) { chosenFile ->
                messageBus.syncPublisher(TyeGlobalToolListener.TOPIC).tyeToolPathChanged(chosenFile.path)
                chosenFile.path
            }
        }
        row("Tye version") {
            textField(settings::tyeToolVersion).enabled(false)
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