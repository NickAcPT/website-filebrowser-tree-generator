package io.github.nickacpt.filebrowser.tree.generator

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import io.github.nickacpt.filebrowser.tree.generator.model.FileBrowserEntry
import io.github.nickacpt.filebrowser.tree.generator.model.FileType
import io.github.nickacpt.filebrowser.tree.generator.utils.FileTypeDeserializer
import io.github.nickacpt.filebrowser.tree.generator.utils.FileTypeSerializer
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.*
import kotlin.streams.asSequence

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

    @OptIn(ExperimentalPathApi::class)
    override fun run() {
        val rootEntry = FileSystems.newFileSystem(input).use { fs ->
            val root = fs.getPath("/")

            createFileEntry(root).apply {
                name = ""
            }
        }

        output.writeText(mapper.writeValueAsString(rootEntry))
    }

    private fun createFileEntry(root: Path): FileBrowserEntry {
        val type = if (root.isRegularFile()) FileType.FILE else FileType.FOLDER

        val children = root.takeIf { !root.isRegularFile() }?.let {
            Files.list(it).asSequence()
        }?.map {
            createFileEntry(it)
        }?.groupBy {
            it.type
        }?.mapValues { entry ->
            entry.value.sortedBy { it.name.substringBefore('|') }
        }?.map { it }?.sortedByDescending {
            it.key == FileType.FOLDER
        }?.flatMap { it.value }?.toList()

        return FileBrowserEntry(fileFormat(root), type, children)
    }

    private fun fileFormat(root: Path): String {
        return when {
            root.isRegularFile() -> {
                "${root.fileName}|${root.fileSize()}|${root.getLastModifiedTime().toInstant().formatDate()}"
            }

            else -> {
                "${root.fileName}"
            }
        }
    }
}

private fun Instant.formatDate(): String {
    return java.text.SimpleDateFormat("yyyy/MM/dd HH:mm").format(java.util.Date(this.toEpochMilli()))
}
