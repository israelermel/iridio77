package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.github.israelermel.iridio77.ui.models.Command

interface AdbFormActionEvent {
    fun execute(device: IDevice, command: Command)
}