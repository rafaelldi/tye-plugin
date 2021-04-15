package com.github.rafaelldi.tyeplugin

import com.intellij.openapi.vfs.VirtualFile

const val TYE_FILE_NAME = "tye.yaml"

fun VirtualFile?.isTyeFile() = this?.name == TYE_FILE_NAME