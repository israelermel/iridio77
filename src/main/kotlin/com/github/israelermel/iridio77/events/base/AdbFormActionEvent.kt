package com.github.israelermel.iridio77.events.base

import com.android.ddmlib.IDevice
import com.github.israelermel.iridio77.ui.models.command.Command

interface AdbFormActionEvent {
    fun execute(device: IDevice, command: Command)
}