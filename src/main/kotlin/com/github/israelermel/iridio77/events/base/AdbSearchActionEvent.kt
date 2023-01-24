package com.github.israelermel.iridio77.events.base

import com.android.ddmlib.IDevice
import com.github.israelermel.iridio77.ui.models.PackagesModel
import com.github.israelermel.iridio77.ui.models.command.Command

interface AdbSearchActionEvent {
    fun search(device: IDevice, command: Command): List<PackagesModel>
    fun remove(device: IDevice, command: Command)
}