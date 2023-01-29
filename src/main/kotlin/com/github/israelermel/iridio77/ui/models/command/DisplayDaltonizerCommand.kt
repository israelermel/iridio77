package com.github.israelermel.iridio77.ui.models.command

data class DisplayDaltonizerCommand(
    private var code: Int? = -1,
    private var daltonizer: String? = "",
    private val state: DisplayDaltonizerState? = null
) : Command {

    init {
        state?.let {
            code = it.value
            daltonizer = it.label
        }
    }

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

enum class DisplayDaltonizerState(val label: String, val value: Int) {
    DISABLED("Disabled Daltonizer", -1),
    MONOCHROMATIC("Monochromatic", 0),
    PROTANOMALY("Protanomaly", 11),
    DEUTERANOMALY("Deuteranomaly", 12),
    TRITANOMALY("Tritanomaly", 13)
}