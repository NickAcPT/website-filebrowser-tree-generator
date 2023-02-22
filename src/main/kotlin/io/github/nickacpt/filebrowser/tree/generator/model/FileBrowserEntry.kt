package io.github.nickacpt.filebrowser.tree.generator.model

data class FileBrowserEntry(
    var name: String,
    var type: FileType,
    var children: List<FileBrowserEntry>?
)