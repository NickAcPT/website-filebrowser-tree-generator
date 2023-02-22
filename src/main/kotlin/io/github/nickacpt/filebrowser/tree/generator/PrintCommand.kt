package io.github.nickacpt.filebrowser.tree.generator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import io.github.nickacpt.filebrowser.tree.generator.model.FileBrowserEntry

class PrintCommand : CliktCommand(name = "print") {
    private val input by argument(help = "The input file to read from")
        .path(mustExist = true, canBeDir = true, mustBeReadable = true)

    override fun run() {
        val rootEntry = FileSystemLogic.generate(input)
        printEntry(rootEntry, -1)
    }

    fun printEntry(entry: FileBrowserEntry, level: Int = 0) {
        if (level >= 0) {
            println("${"    ".repeat(level)}- ${entry.name}")
        }
        entry.children?.forEach {
            printEntry(it, level + 1)
        }
    }

}