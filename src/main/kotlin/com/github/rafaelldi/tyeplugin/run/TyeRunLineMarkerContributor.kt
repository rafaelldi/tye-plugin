package com.github.rafaelldi.tyeplugin.run

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.jetbrains.yaml.YAMLTokenTypes.SCALAR_KEY

class TyeRunLineMarkerContributor : RunLineMarkerContributor() {
    private companion object {
        const val TYE_FILE_NAME = "tye.yaml"
    }

    override fun getInfo(element: PsiElement): Info? {
        if (element.containingFile.name == TYE_FILE_NAME) {
            if (element.elementType == SCALAR_KEY && element.text == "services") {
                val actions = ExecutorAction.getActions(0)
                return Info(
                    AllIcons.Actions.RunAll,
                    { "tye run" },
                    actions[0]
                )
            }
        }
        return null
    }
}
