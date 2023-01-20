package com.github.israelermel.iridio77.adb

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.receivers.SingleLineAdbReceiver
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbOverdraw(val project: Project, val notification: IridioNotification) {

    fun execute(device: IDevice) {
        try {
            device.executeShellCommand("getprop debug.hwui.overdraw",
                SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine != "false"

                    with(isEnabled.not()) {
                        IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_OVERDRAW, this).also {
                            notification.adbNotification(it)
                        }

                        when (this) {
                            true -> device.executeShellCommand(
                                "setprop debug.hwui.overdraw show",
                                NullOutputReceiver()
                            )

                            false -> device.executeShellCommand(
                                DEFAULT_CONFIGURATION,
                                NullOutputReceiver()
                            )
                        }
                    }
                })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_OVERDRAW)
        }
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "setprop debug.hwui.overdraw false"
        const val MSG_ADB_OVERDRAW = "msgAdbOverdraw"
    }
}