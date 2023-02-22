package io.github.nickacpt.filebrowser.tree.generator.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import io.github.nickacpt.filebrowser.tree.generator.model.FileType

object FileTypeDeserializer : JsonDeserializer<FileType>() {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): FileType {
        return FileType.fromString(p?.valueAsString ?: throw IllegalArgumentException("Invalid file type"))
    }

}
