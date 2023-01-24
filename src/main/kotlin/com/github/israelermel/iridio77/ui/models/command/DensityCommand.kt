package com.github.israelermel.iridio77.ui.models.command

private const val MULTIPLER_DENSITY = 1.1

data class DensityCommand(
    private var density: Int? = 0,
    private var label: String? = "",
    private val state: DensityState? = null
) : Command {

    init {
        state?.let {
            this.density = it.adbDensity()
            this.label = it.fullLabel()
        }
    }

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

enum class DensityState(val value: Int, val label: String) {
    HDPI(240, "hdpi") {
        override fun adbDensity() = (value * MULTIPLER_DENSITY).toInt()
        override fun fullLabel() = "$label - ${value}dpi"
    },
    XHDPI(320, "xhdpi") {
        override fun adbDensity() = (value * MULTIPLER_DENSITY).toInt()
        override fun fullLabel() = "$label - ${value}dpi"
    },
    XXHDPI(480, "xxhdpi") {
        override fun adbDensity() = (value * MULTIPLER_DENSITY).toInt()
        override fun fullLabel() = "$label - ${value}dpi"
    },
    XXXHDPI(640, "xxxhdpi") {
        override fun adbDensity() = (value * MULTIPLER_DENSITY).toInt()
        override fun fullLabel() = "$label - ${value}dpi"
    };

    abstract fun adbDensity(): Int
    abstract fun fullLabel(): String
}