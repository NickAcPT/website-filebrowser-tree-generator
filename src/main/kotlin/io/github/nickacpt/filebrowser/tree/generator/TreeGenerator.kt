package io.github.nickacpt.filebrowser.tree.generator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class TreeGenerator : CliktCommand("Simple tool that takes a file or directory " +
        "and generates a JSON file that can be used in my website's file browser element."
) {
    init {
        subcommands(GenerateCommand(), PrintCommand())
    }

    override fun run() {
    }
}