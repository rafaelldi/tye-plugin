package com.github.rafaelldi.tyeplugin.file

import com.intellij.openapi.fileTypes.LanguageFileType
import icons.TyeIcons
import org.jetbrains.yaml.YAMLLanguage

class TyeFileType : LanguageFileType(YAMLLanguage.INSTANCE) {
    override fun getName() = "Tye"

    override fun getDescription() = "Tye configuration file"

    override fun getDefaultExtension() = "yaml"

    override fun getIcon() = TyeIcons.TYE
}
