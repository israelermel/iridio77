package com.github.israelermel.iridio77.ui.models.command

data class SearchRemovePackagesCommand(
    private var search: String,
    private var label: String
) : Command {
    override fun getCommand(): String {
        return search
    }

    override fun getLabel(): String {
        return label
    }

    override fun toString(): String {
        return getLabel()
    }
}