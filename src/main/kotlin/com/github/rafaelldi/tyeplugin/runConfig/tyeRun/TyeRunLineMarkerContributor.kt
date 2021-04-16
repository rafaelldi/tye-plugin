package com.github.rafaelldi.tyeplugin.runConfig.tyeRun

import com.github.rafaelldi.tyeplugin.util.isTyeFile
import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.jetbrains.yaml.YAMLTokenTypes.SCALAR_KEY
import org.jetbrains.yaml.psi.YAMLFile

class TyeRunLineMarkerContributor : RunLineMarkerContributor() {
    override fun getInfo(element: PsiElement): Info? {
        if (element.containingFile !is YAMLFile || !element.containingFile.virtualFile.isTyeFile()) {
            return null
        }

        if (element.elementType == SCALAR_KEY && element.text == "services") {
            val actions = ExecutorAction.getActions(0)
            return Info(
                AllIcons.Actions.Execute,
                { "tye run" },
                actions[0]
            )
        }
        return null
    }
}
