package com.github.israelermel.iridio77.ui.models

data class DensityCommand(
    private val density: Int? = 0,
    private val label: String? = "",
    val width: String? = null,
    val height: String? = null
) : Command {
    override fun getCommand(): String {
        return density.toString()
    }

    override fun getLabel(): String {
        return label ?: ""
    }

    override fun toString(): String {
        return getLabel()
    }

}