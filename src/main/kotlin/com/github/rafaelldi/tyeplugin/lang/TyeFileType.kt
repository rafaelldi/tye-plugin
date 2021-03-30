package com.github.rafaelldi.tyeplugin.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile
import com.intellij.openapi.vfs.VirtualFile
import icons.TyeIcons

class TyeFileType : LanguageFileType(TyeLanguage.INSTANCE), FileTypeIdentifiableByVirtualFile {
    companion object {
        private const val TYE_FILE_NAME = "tye.yaml"
    }

    override fun getName() = "Tye file"

    override fun getDescription() = "Tye file"

    override fun getDefaultExtension() = "yaml"

    override fun getIcon() = TyeIcons.TYE

    override fun isMyFileType(file: VirtualFile): Boolean {
        return file.name == TYE_FILE_NAME
    }
}
