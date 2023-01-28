package com.github.israelermel.iridio77.ui.formbase

import com.github.israelermel.iridio77.ui.models.command.Command

interface SearchTableForm {
    fun addSearchListener(onClickListener: (Command) -> Unit)
    fun addExecuteListener(onClickListener: (Command) -> Unit)
    fun showForm()
}