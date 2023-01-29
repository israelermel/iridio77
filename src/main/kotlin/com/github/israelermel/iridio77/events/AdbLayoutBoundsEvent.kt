package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbActionEvent
import com.github.israelermel.iridio77.receivers.IRSingleLineReceiver
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbLayoutBoundsEvent(val project: Project, val notification: IRNotification) : AdbActionEvent {

    override fun execute(device: IDevice) {

        device.executeShellCommand(GET_STATUS_CONFIGURATION, IRSingleLineReceiver { firstLine ->
            val isEnabled = firstLine.toBoolean().not()

            IRMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_LAYOUT_BOUNDS, isEnabled).also {
                notification.adbNotification(it)
            }

            when (isEnabled) {
                true -> device.executeShellCommand(ENABLE_LAYOUT_BOUNDS, NullOutputReceiver())
                false -> device.executeShellCommand(DEFAULT_CONFIGURATION, NullOutputReceiver())
            }
        }
        )
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "setprop debug.layout false ; service call activity 1599295570"
        const val ENABLE_LAYOUT_BOUNDS = "setprop debug.layout true ; service call activity 1599295570"
        const val GET_STATUS_CONFIGURATION = "getprop debug.layout"
        const val MSG_ADB_LAYOUT_BOUNDS = "msg.adb.label.layout.bounds"

    }
}