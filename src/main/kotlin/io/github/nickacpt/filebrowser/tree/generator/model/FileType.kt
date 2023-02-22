package io.github.nickacpt.filebrowser.tree.generator.model

enum class FileType {
    FILE, FOLDER, UP_FOLDER, NO_FILES;

    override fun toString(): String {
        return when (this) {
            FILE -> "FILE"
            FOLDER -> "FOLDER"
            UP_FOLDER -> "UP-FOLDER"
            NO_FILES -> "NO-FILES"
        }
    }

    companion object {
        fun fromString(str: String): FileType {
            return when (str) {
                "FILE" -> FILE
                "FOLDER" -> FOLDER
                "UP-FOLDER" -> UP_FOLDER
                "NO-FILES" -> NO_FILES
                else -> throw IllegalArgumentException("Invalid file type: $str")
            }
        }
    }
}