package com.github.rafaelldi.tyeplugin.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import icons.TyeIcons

class TyeFileType : LanguageFileType(TyeLanguage.INSTANCE) {
    override fun getName() = "Tye"

    override fun getDescription() = "Tye configuration file"

    override fun getDefaultExtension() = "yaml"

    override fun getIcon() = TyeIcons.TYE
}
