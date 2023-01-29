package com.github.israelermel.iridio77.ui.models.command

data class FontSizeCommand(
    private var fontSize: Double? = 0.0,
    private var label: String? = "",
    private val state: FontSizeState? = null
) : Command {

    init {
        state?.let {
            fontSize = it.value
            label = it.fullLabel()
        }
    }

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

enum class FontSizeState(val value: Double, val label: String) {
    SMALL(0.85, "Small") {
        override fun fullLabel() = "$label $value"
    },
    DEFAULT(1.0, "Default") {
        override fun fullLabel() = "$label $value"
    },
    LARGE(1.15, "Large") {
        override fun fullLabel() = "$label $value"
    },
    LARGEST(1.30, "Largest") {
        override fun fullLabel() = "$label $value"
    },
    XLARGEST(2.0, "XLargest") {
        override fun fullLabel() = "$label $value"
    };

    abstract fun fullLabel(): String
}