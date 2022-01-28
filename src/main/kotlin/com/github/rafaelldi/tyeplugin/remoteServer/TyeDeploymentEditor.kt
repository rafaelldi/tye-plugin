package com.github.rafaelldi.tyeplugin.remoteServer

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
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.layout.GrowPolicy
import com.intellij.ui.layout.panel
import javax.swing.JPanel

class TyeDeploymentEditor(private val project: Project) : FragmentedSettingsEditor<TyeDeploymentConfiguration>(null) {
    private val pathField: LabeledComponent<TextFieldWithBrowseButton>
    private val tagsField: LabeledComponent<ExpandableTextField>
    private val verbosityPanel: JPanel
    private val debugRadioButton: JBRadioButton
    private val infoRadioButton: JBRadioButton
    private val quietRadioButton: JBRadioButton
    private val logsPanel: JPanel
    private val logsProviderComboBox: ComboBox<LogsProvider>
    private val logsProviderUrlTextField: JBTextField
    private val tracesPanel: JPanel
    private val tracesProviderComboBox: ComboBox<TracesProvider>
    private val tracesProviderUrlTextField: JBTextField

    init {
        val pathTextField = TextFieldWithBrowseButton()
        pathTextField.isEditable = false
        val descriptor = FileChooserDescriptor(true, true, false, false, false, false)
        descriptor.withFileFilter { vf: VirtualFile ->
            vf.name.equals("tye.yaml", ignoreCase = true) ||
                    vf.extension != null &&
                    (vf.extension.equals("sln", ignoreCase = true) ||
                            vf.extension.equals("csproj", ignoreCase = true) ||
                            vf.extension.equals("fsproj", ignoreCase = true))
        }
        pathTextField.addBrowseFolderListener(TextBrowseFolderListener(descriptor))
        pathField = LabeledComponent.create(pathTextField, "Path:")
        pathField.labelLocation = "West"

        val tagsTextField = ExpandableTextField()
        tagsField = LabeledComponent.create(tagsTextField, "Tags:")
        tagsField.labelLocation = "West"

        debugRadioButton = JBRadioButton("Debug")
        infoRadioButton = JBRadioButton("Info", true)
        quietRadioButton = JBRadioButton("Quiet")
        verbosityPanel = panel {
            row {
                buttonGroup("Verbosity:") {
                    debugRadioButton()
                    infoRadioButton()
                    quietRadioButton()
                }
            }
        }

        logsProviderComboBox = ComboBox(LogsProvider.values())
        logsProviderUrlTextField = JBTextField()
        logsPanel = panel {
            row("Logs provider:") {
                logsProviderComboBox()
                    .growPolicy(GrowPolicy.MEDIUM_TEXT)
                row("Logs provider url:") {
                    logsProviderUrlTextField()
                        .growPolicy(GrowPolicy.MEDIUM_TEXT)
                }
            }
        }

        tracesProviderComboBox = ComboBox(TracesProvider.values())
        tracesProviderUrlTextField = JBTextField()
        tracesPanel = panel {
            row("Traces provider:") {
                tracesProviderComboBox()
                    .growPolicy(GrowPolicy.MEDIUM_TEXT)
                row("Traces provider url:") {
                    tracesProviderUrlTextField()
                        .growPolicy(GrowPolicy.MEDIUM_TEXT)
                }
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
            this.fragment("tye.configuration.verbosity", verbosityPanel) { this.setupVerbosity() }
            this.fragment("tye.configuration.logs", logsPanel) { this.setupLogs() }
            this.fragment("tye.configuration.traces", tracesPanel) { this.setupTraces() }
            this.tag("tye.configuration.no-build", "No build") { this.setupNoBuildTag() }
            this.tag("tye.configuration.docker", "Docker") { this.setupDockerTag() }
            this.tag("tye.configuration.dashboard", "Dashboard") { this.setupDashboardTag() }
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

    private fun Fragment<TyeDeploymentConfiguration, JPanel>.setupVerbosity() {
        this.name = "Verbosity"
        this.actionHint = "Sets the output verbosity of the process"
        this.actionDescription = "--verbosity"
        this.visible = { it.verbosityArgument != Verbosity.INFO }
        this.apply = { settings, component ->
            settings.verbosityArgument =
                if (infoRadioButton.isSelected) Verbosity.INFO
                else if (debugRadioButton.isSelected) Verbosity.DEBUG
                else Verbosity.QUIET
        }
        this.reset = { settings, component ->
            when (settings.verbosityArgument) {
                Verbosity.DEBUG -> {
                    debugRadioButton.isSelected = true
                    infoRadioButton.isSelected = false
                    quietRadioButton.isSelected = false
                }
                Verbosity.INFO -> {
                    debugRadioButton.isSelected = false
                    infoRadioButton.isSelected = true
                    quietRadioButton.isSelected = false
                }
                Verbosity.QUIET -> {
                    debugRadioButton.isSelected = false
                    infoRadioButton.isSelected = false
                    quietRadioButton.isSelected = true
                }
            }
        }
    }

    private fun Fragment<TyeDeploymentConfiguration, JPanel>.setupLogs() {
        this.name = "Logs"
        this.actionHint = "Write structured application logs to the specified log providers"
        this.actionDescription = "--logs"
        this.visible = { it.logsProvider != LogsProvider.NONE }
        this.apply = { settings, component ->
            val logsProvider = logsProviderComboBox.item
            settings.logsProvider = logsProvider
            if (logsProvider.isProviderUrlEnabled())
                settings.logsProviderUrl = logsProviderUrlTextField.text
            else
                settings.logsProviderUrl = null
        }
        this.reset = { settings, component ->
            val logsProvider = settings.logsProvider
            logsProviderComboBox.item = logsProvider
            if (logsProvider.isProviderUrlEnabled())
                logsProviderUrlTextField.text = settings.logsProviderUrl
            else
                logsProviderUrlTextField.text = null
        }
    }

    private fun Fragment<TyeDeploymentConfiguration, JPanel>.setupTraces() {
        this.name = "Traces"
        this.actionHint = "Write distributed traces to the specified providers"
        this.actionDescription = "--dtrace"
        this.visible = { it.tracesProvider != TracesProvider.NONE }
        this.apply = { settings, component ->
            val tracesProvider = tracesProviderComboBox.item
            settings.tracesProvider = tracesProvider
            if (tracesProvider.isProviderUrlEnabled())
                settings.tracesProviderUrl = tracesProviderUrlTextField.text
            else
                settings.tracesProviderUrl = null
        }
        this.reset = { settings, component ->
            val tracesProvider = settings.tracesProvider
            tracesProviderComboBox.item = tracesProvider
            if (tracesProvider.isProviderUrlEnabled())
                tracesProviderUrlTextField.text = settings.tracesProviderUrl
            else
                tracesProviderUrlTextField.text = null
        }
    }
}
