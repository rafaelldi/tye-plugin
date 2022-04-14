@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.remoteServer.deployment

import com.github.rafaelldi.tyeplugin.remoteServer.LogsProvider
import com.github.rafaelldi.tyeplugin.remoteServer.TracesProvider
import com.github.rafaelldi.tyeplugin.remoteServer.Verbosity
import com.github.rafaelldi.tyeplugin.util.isDotNetSolutionFile
import com.github.rafaelldi.tyeplugin.util.isTyeFile
import com.intellij.execution.ui.FragmentedSettingsEditor
import com.intellij.execution.ui.SettingsEditorFragment
import com.intellij.execution.ui.utils.Fragment
import com.intellij.execution.ui.utils.FragmentsBuilder
import com.intellij.execution.ui.utils.Group
import com.intellij.execution.ui.utils.Tag
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.dsl.builder.COLUMNS_MEDIUM
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.layout.selectedValueIs
import com.intellij.ui.layout.selectedValueMatches
import javax.swing.JPanel

class TyeDeploymentEditor(private val project: Project) : FragmentedSettingsEditor<TyeDeploymentConfiguration>(null) {
    private val pathField: LabeledComponent<TextFieldWithBrowseButton>
    private val tagsField: LabeledComponent<ExpandableTextField>
    private val debugField: LabeledComponent<JBTextField>
    private val frameworkField: LabeledComponent<JBTextField>
    private val verbosityPanel: JPanel
    private lateinit var verbosityComboBox: Cell<ComboBox<Verbosity>>
    private val logsPanel: JPanel
    private lateinit var logsProviderComboBox: Cell<ComboBox<LogsProvider>>
    private lateinit var logsProviderUrlTextField: Cell<JBTextField>
    private val tracesPanel: JPanel
    private lateinit var tracesProviderComboBox: Cell<ComboBox<TracesProvider>>
    private lateinit var tracesProviderUrlTextField: Cell<JBTextField>

    init {
        val pathTextField = TextFieldWithBrowseButton()
        pathTextField.isEditable = false
        val descriptor = FileChooserDescriptor(true, true, false, false, false, false)
        descriptor.withFileFilter { vf: VirtualFile ->
            vf.isTyeFile() || vf.isDotNetSolutionFile()
        }
        pathTextField.addBrowseFolderListener(TextBrowseFolderListener(descriptor))
        pathField = LabeledComponent.create(pathTextField, "Path:")
        pathField.labelLocation = "West"

        val tagsTextField = ExpandableTextField()
        tagsField = LabeledComponent.create(tagsTextField, "Tags:")
        tagsField.labelLocation = "West"

        val debugTextField = JBTextField()
        debugField = LabeledComponent.create(debugTextField, "Debug service:")
        debugField.labelLocation = "West"

        val frameworkTextField = JBTextField()
        frameworkField = LabeledComponent.create(frameworkTextField, "Framework:")
        frameworkField.labelLocation = "West"

        verbosityPanel = panel {
            row("Verbosity:") {
                verbosityComboBox = comboBox(Verbosity.values().toList())
                    .columns(COLUMNS_MEDIUM)
            }
        }

        logsPanel = panel {
            row("Logs provider:") {
                logsProviderComboBox = comboBox(LogsProvider.values().toList())
                    .horizontalAlign(HorizontalAlign.FILL)
            }
            row("Logs provider url:") {
                logsProviderUrlTextField = textField()
                    .enabledIf(logsProviderComboBox.component.selectedValueMatches {
                        it == LogsProvider.APPLICATIONINSIGHTS
                                || it == LogsProvider.ELASTICSEARCH
                                || it == LogsProvider.SEQ
                    })
                    .horizontalAlign(HorizontalAlign.FILL)
            }
        }

        tracesPanel = panel {
            row("Traces provider:") {
                tracesProviderComboBox = comboBox(TracesProvider.values().toList())
                    .horizontalAlign(HorizontalAlign.FILL)
            }
            row("Traces provider url:") {
                tracesProviderUrlTextField = textField()
                    .enabledIf(tracesProviderComboBox.component.selectedValueIs(TracesProvider.ZIPKIN))
                    .horizontalAlign(HorizontalAlign.FILL)
            }
        }
    }

    override fun createFragments(): MutableCollection<SettingsEditorFragment<TyeDeploymentConfiguration, *>> {
        val builder = FragmentsBuilder<TyeDeploymentConfiguration>(null, "tye", emptyList())
        builder.fragment("tye.configuration.path", pathField) { this.setupPathField() }
        builder.group("tye.configuration.options", "Options") { this.setupOptionsGroup() }

        return builder.build()
    }

    private fun Fragment<TyeDeploymentConfiguration, LabeledComponent<TextFieldWithBrowseButton>>.setupPathField() {
        this.isRemovable = false
        this.apply = { settings, component ->
            settings.pathArgument =
                if (component.component.text.isEmpty()) ""
                else LocalFileSystem.getInstance().findFileByPath(component.component.text)?.path
        }
        this.reset = { settings, component ->
            component.component.text = settings.pathArgument ?: project.basePath ?: ""
        }
    }

    private fun Group<TyeDeploymentConfiguration>.setupOptionsGroup() {
        this.group = "Options"
        this.children = {
            this.fragment("tye.configuration.tags", tagsField) { this.setupTagsField() }
            this.fragment("tye.configuration.debug", debugField) { this.setupDebugField() }
            this.fragment("tye.configuration.framework", frameworkField) { this.setupFrameworkField() }
            this.fragment("tye.configuration.verbosity", verbosityPanel) { this.setupVerbosity() }
            this.fragment("tye.configuration.logs", logsPanel) { this.setupLogs() }
            this.fragment("tye.configuration.traces", tracesPanel) { this.setupTraces() }
            this.tag("tye.configuration.no-build", "No build") { this.setupNoBuildTag() }
            this.tag("tye.configuration.docker", "Docker") { this.setupDockerTag() }
            this.tag("tye.configuration.dashboard", "Dashboard") { this.setupDashboardTag() }
            this.tag("tye.configuration.watch", "Watch") { this.setupWatchTag() }
        }
    }

    private fun Fragment<TyeDeploymentConfiguration, LabeledComponent<ExpandableTextField>>.setupTagsField() {
        this.name = "Tags"
        this.actionHint = "Filter the group of running services by tag"
        this.actionDescription = "--tags"
        this.visible = { !it.tagsArgument.isNullOrEmpty() }
        this.apply = { settings, component ->
            settings.tagsArgument = component.component.text
        }
        this.reset = { settings, component ->
            component.component.text = settings.tagsArgument
        }
    }

    private fun Fragment<TyeDeploymentConfiguration, LabeledComponent<JBTextField>>.setupDebugField() {
        this.name = "Debug service"
        this.actionHint = "Waits for debugger attach to service. Specify * to wait to attach to all services"
        this.actionDescription = "--debug"
        this.visible = { !it.debugArgument.isNullOrEmpty() }
        this.apply = { settings, component ->
            settings.debugArgument = component.component.text
        }
        this.reset = { settings, component ->
            component.component.text = settings.debugArgument
        }
    }

    private fun Fragment<TyeDeploymentConfiguration, LabeledComponent<JBTextField>>.setupFrameworkField() {
        this.name = "Framework"
        this.actionHint = "The target framework hint to use for all cross-targeting projects with multiple TFMs."
        this.actionDescription = "--framework"
        this.visible = { !it.frameworkArgument.isNullOrEmpty() }
        this.apply = { settings, component ->
            settings.frameworkArgument = component.component.text
        }
        this.reset = { settings, component ->
            component.component.text = settings.frameworkArgument
        }
    }

    private fun Tag<TyeDeploymentConfiguration>.setupNoBuildTag() {
        this.actionHint = "Does not build projects before running"
        this.actionDescription = "--no-build"
        this.setter = { configuration, b -> configuration.noBuildArgument = b }
        this.getter = { it.noBuildArgument }
    }

    private fun Tag<TyeDeploymentConfiguration>.setupDockerTag() {
        this.actionHint = "Run projects as docker containers"
        this.actionDescription = "--docker"
        this.setter = { configuration, b -> configuration.dockerArgument = b }
        this.getter = { it.dockerArgument }
    }

    private fun Tag<TyeDeploymentConfiguration>.setupDashboardTag() {
        this.actionHint = "Launch dashboard on run"
        this.actionDescription = "--dashboard"
        this.setter = { configuration, b -> configuration.dashboardArgument = b }
        this.getter = { it.dashboardArgument }
    }

    private fun Tag<TyeDeploymentConfiguration>.setupWatchTag() {
        this.actionHint = "Watches for file changes in all projects that are built by tye"
        this.actionDescription = "--watch"
        this.setter = { configuration, b -> configuration.watchArgument = b }
        this.getter = { it.watchArgument }
    }

    private fun Fragment<TyeDeploymentConfiguration, JPanel>.setupVerbosity() {
        this.name = "Verbosity"
        this.actionHint = "Sets the output verbosity of the process"
        this.actionDescription = "--verbosity"
        this.visible = { it.verbosityArgument != Verbosity.INFO }
        this.apply = { settings, _ ->
            val verbosity = verbosityComboBox.component.item
            settings.verbosityArgument = verbosity
        }
        this.reset = { settings, _ ->
            val verbosity = settings.verbosityArgument
            verbosityComboBox.component.item = verbosity
        }
    }

    private fun Fragment<TyeDeploymentConfiguration, JPanel>.setupLogs() {
        this.name = "Logs"
        this.actionHint = "Write structured application logs to the specified log providers"
        this.actionDescription = "--logs"
        this.visible = { it.logsProvider != LogsProvider.NONE }
        this.apply = { settings, _ ->
            val logsProvider = logsProviderComboBox.component.item
            settings.logsProvider = logsProvider
            if (logsProvider.isProviderUrlEnabled())
                settings.logsProviderUrl = logsProviderUrlTextField.component.text
            else
                settings.logsProviderUrl = null
        }
        this.reset = { settings, _ ->
            val logsProvider = settings.logsProvider
            logsProviderComboBox.component.item = logsProvider
            if (logsProvider.isProviderUrlEnabled())
                logsProviderUrlTextField.component.text = settings.logsProviderUrl
            else
                logsProviderUrlTextField.component.text = null
        }
    }

    private fun Fragment<TyeDeploymentConfiguration, JPanel>.setupTraces() {
        this.name = "Traces"
        this.actionHint = "Write distributed traces to the specified providers"
        this.actionDescription = "--dtrace"
        this.visible = { it.tracesProvider != TracesProvider.NONE }
        this.apply = { settings, _ ->
            val tracesProvider = tracesProviderComboBox.component.item
            settings.tracesProvider = tracesProvider
            if (tracesProvider.isProviderUrlEnabled())
                settings.tracesProviderUrl = tracesProviderUrlTextField.component.text
            else
                settings.tracesProviderUrl = null
        }
        this.reset = { settings, _ ->
            val tracesProvider = settings.tracesProvider
            tracesProviderComboBox.component.item = tracesProvider
            if (tracesProvider.isProviderUrlEnabled())
                tracesProviderUrlTextField.component.text = settings.tracesProviderUrl
            else
                tracesProviderUrlTextField.component.text = null
        }
    }
}
