package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice

interface AdbActionEvent {
    fun execute(device: IDevice)
}