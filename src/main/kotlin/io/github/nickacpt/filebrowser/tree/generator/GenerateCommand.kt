package io.github.nickacpt.filebrowser.tree.generator

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import io.github.nickacpt.filebrowser.tree.generator.model.FileType
import io.github.nickacpt.filebrowser.tree.generator.utils.FileTypeDeserializer
import io.github.nickacpt.filebrowser.tree.generator.utils.FileTypeSerializer
import kotlin.io.path.writeText

val mapper = jacksonObjectMapper().apply {
    configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
    setSerializationInclusion(JsonInclude.Include.NON_NULL)

    registerModule(object : SimpleModule() {
        init {
            addSerializer(FileType::class.java, FileTypeSerializer)
            addDeserializer(FileType::class.java, FileTypeDeserializer)
        }
    })
}


class GenerateCommand : CliktCommand(name = "generate") {
    private val input by argument(help = "The input file to read from")
        .path(mustExist = true, canBeDir = true, mustBeReadable = true)

    private val output by argument(help = "The output file to write to")
        .path()

    override fun run() {
        val rootEntry = FileSystemLogic.generate(input)
        output.writeText(mapper.writeValueAsString(rootEntry))
    }

}
