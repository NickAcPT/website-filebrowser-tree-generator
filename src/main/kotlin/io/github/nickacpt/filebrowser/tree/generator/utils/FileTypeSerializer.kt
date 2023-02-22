package io.github.nickacpt.filebrowser.tree.generator.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.nickacpt.filebrowser.tree.generator.model.FileType

object FileTypeSerializer : JsonSerializer<FileType>() {
    override fun serialize(value: FileType?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.toString() ?: throw IllegalArgumentException("Invalid file type"))
    }
}