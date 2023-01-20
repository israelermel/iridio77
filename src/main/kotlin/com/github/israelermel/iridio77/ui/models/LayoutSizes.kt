package com.github.israelermel.iridio77.ui.models

data class LayoutSizes(
    val size: Int? = 0,
    val label: String? = "",
    val width: String? = null,
    val height: String? = null,
    val index: Int = 0
) {
    override fun toString(): String {
        return label ?: ""
    }
}

class DensityCommand(
    private val density: Int? = 0,
    private val label: String? = ""
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

fun LayoutSizes.toCommand() = DensityCommand(
    density = size,
    label = label
)