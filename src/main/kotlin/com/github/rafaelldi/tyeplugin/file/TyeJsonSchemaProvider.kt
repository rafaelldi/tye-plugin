package com.github.rafaelldi.tyeplugin.file

import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.SchemaType
import com.jetbrains.jsonSchema.remote.JsonFileResolver

class TyeJsonSchemaProvider : JsonSchemaFileProvider {
    private companion object {
        const val TYE_FILE_NAME = "tye.yaml"
        const val SCHEMA_URL = "https://raw.githubusercontent.com/dotnet/tye/main/src/schema/tye-schema.json"
    }

    override fun isAvailable(file: VirtualFile) = file.name == TYE_FILE_NAME

    override fun getName() = "Tye"

    override fun getSchemaFile(): VirtualFile? = JsonFileResolver.urlToFile(SCHEMA_URL)

    override fun getSchemaType() = SchemaType.remoteSchema
}
