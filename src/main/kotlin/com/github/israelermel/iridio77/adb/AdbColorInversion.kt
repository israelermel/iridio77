package com.github.israelermel.iridio77.adb

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.actions.adb.SingleLineLayoutBoundsReceiver
import com.github.israelermel.iridio77.extensions.toEnableOrDisable
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbColorInversion(
    val project: Project,
    val notification: IridioNotification
) {

    fun execute(device: IDevice) {
        device.executeShellCommand(
            "settings get secure accessibility_display_inversion_enabled",
            SingleLineLayoutBoundsReceiver { firstLine ->
                val isEnabled = firstLine.toEnableOrDisable().not()

                IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB, isEnabled).also {
                    notification.adbNotification(it)
                }

                device.executeShellCommand(
                    when (isEnabled) {
                        true -> ENABLE_COLOR_INVERSION
                        false -> DEFAULT_CONFIGURATION
                    },
                    NullOutputReceiver()
                )
            }
        )
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "settings put secure accessibility_display_inversion_enabled 0"
        const val ENABLE_COLOR_INVERSION = "settings put secure accessibility_display_inversion_enabled 1"
        const val MSG_ADB = "label.action.color.inversion"
    }
}