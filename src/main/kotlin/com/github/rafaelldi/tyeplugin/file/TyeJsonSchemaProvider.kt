package com.github.rafaelldi.tyeplugin.file

import com.github.rafaelldi.tyeplugin.util.isTyeFile
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.SchemaType
import com.jetbrains.jsonSchema.remote.JsonFileResolver

class TyeJsonSchemaProvider : JsonSchemaFileProvider {
    private companion object {
        const val SCHEMA_URL = "https://raw.githubusercontent.com/dotnet/tye/main/src/schema/tye-schema.json"
    }

    override fun isAvailable(file: VirtualFile) = file.isTyeFile()

    override fun getName() = "Tye"

    override fun getSchemaFile(): VirtualFile? = JsonFileResolver.urlToFile(SCHEMA_URL)

    override fun getSchemaType() = SchemaType.remoteSchema
}
