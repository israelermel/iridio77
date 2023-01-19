package com.github.israelermel.iridio77.adb

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.actions.adb.SingleLineLayoutBoundsReceiver
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbLayoutBounds(val project: Project, val notification: IridioNotification) {

    private val MSG_ADB_LAYOUT_BOUNDS = "msgAdbLayoutBounds"
    private val DISABLE_LAYOUT_BOUNDS = "setprop debug.layout false ; service call activity 1599295570"
    private val ENABLE_LAYOUT_BOUNDS = "setprop debug.layout true ; service call activity 1599295570"

    fun execute(device: IDevice) {

        device.executeShellCommand(
            "getprop debug.layout",
            SingleLineLayoutBoundsReceiver { firstLine ->
                val isEnabled = firstLine.toBoolean().not()

                IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_LAYOUT_BOUNDS, isEnabled).also {
                    notification.adbNotification(it)
                }

                when (isEnabled) {
                    true -> device.executeShellCommand(ENABLE_LAYOUT_BOUNDS, NullOutputReceiver())
                    false -> device.executeShellCommand(DISABLE_LAYOUT_BOUNDS, NullOutputReceiver())
                }
            }
        )

    }
}