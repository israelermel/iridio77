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
