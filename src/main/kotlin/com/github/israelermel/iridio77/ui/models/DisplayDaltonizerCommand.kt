package com.github.israelermel.iridio77.ui.models

data class DisplayDaltonizerCommand(
    private val code: Int? = -1,
    private val daltonizer: String? = ""
) : Command {
    override fun getCommand(): String {
        return code.toString()
    }

    override fun getLabel(): String {
        return daltonizer ?: ""
    }

    override fun toString(): String {
        return getLabel()
    }
}
