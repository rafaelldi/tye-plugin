package com.github.rafaelldi.tyeplugin.util

import com.intellij.openapi.vfs.VirtualFile

const val TYE_FILE_NAME = "tye.yaml"

fun VirtualFile?.isTyeFile() = this?.name?.toLowerCase() == TYE_FILE_NAME
