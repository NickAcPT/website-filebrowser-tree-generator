package io.github.nickacpt.filebrowser.tree.generator

import io.github.nickacpt.filebrowser.tree.generator.model.FileBrowserEntry
import io.github.nickacpt.filebrowser.tree.generator.model.FileType
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.fileSize
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.isRegularFile
import kotlin.streams.asSequence

object FileSystemLogic {
    fun generate(rootPath: Path): FileBrowserEntry {
        val action = { fs: FileSystem, root: FileSystem.() -> Path ->
            createFileEntry(root(fs)).apply {
                name = ""
            }
        };

        val rootEntry = if (rootPath.isRegularFile()) {
            FileSystems.newFileSystem(rootPath).use { action(it) { getPath("/") } }
        } else {
            action(FileSystems.getDefault()) { rootPath }
        }

        return rootEntry
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

    private fun Instant.formatDate(): String {
        return java.text.SimpleDateFormat("yyyy/MM/dd HH:mm").format(java.util.Date(this.toEpochMilli()))
    }
}