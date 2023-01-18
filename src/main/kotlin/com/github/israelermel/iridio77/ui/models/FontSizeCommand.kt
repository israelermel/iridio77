package com.github.israelermel.iridio77.ui.models

data class FontSizeCommand(
    private val fontSize: Double? = 0.0,
    private val label: String? = ""
) : Command {
    override fun getCommand(): String {
        return fontSize.toString()
    }

    override fun getLabel(): String {
        return label ?: ""
    }

    override fun toString(): String {
        return getLabel()
    }
}
