package com.github.rafaelldi.tyeplugin.util

import com.intellij.openapi.vfs.VirtualFile
import java.io.File

const val TYE_EXECUTABLE = "tye"
const val TYE_FILE_NAME = "tye.yaml"
const val DotNetSlnExtension = "sln"
const val DotNetCsprojExtension = "csproj"
const val DotNetFsprojExtension = "fsproj"

fun File.isTyeExecutable() =
    this.exists() && this.isDirectory.not() && this.canExecute() && this.nameWithoutExtension == TYE_EXECUTABLE

fun VirtualFile.isTyeFile() = this.name.equals(TYE_FILE_NAME, ignoreCase = true)
fun VirtualFile.isDotNetSolutionFile() = this.extension != null &&
        (this.extension.equals(DotNetSlnExtension, ignoreCase = true) ||
                this.extension.equals(DotNetCsprojExtension, ignoreCase = true) ||
                this.extension.equals(DotNetFsprojExtension, ignoreCase = true))
