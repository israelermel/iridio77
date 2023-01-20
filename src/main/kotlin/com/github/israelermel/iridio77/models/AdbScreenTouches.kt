package com.github.israelermel.iridio77.models

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.extensions.toEnableOrDisable
import com.github.israelermel.iridio77.receivers.SingleLineAdbReceiver
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbScreenTouches(val project: Project, val notification: IridioNotification) {

    fun execute(device: IDevice) {
        try {
            device.executeShellCommand("settings get system show_touches",
                SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine.toEnableOrDisable().not()

                    IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB, isEnabled).also {
                        notification.adbNotification(it)
                    }
                    device.executeShellCommand(
                        when (isEnabled) {
                            true -> ENABLE_CONFIGURATION
                            false -> DEFAULT_CONFIGURATION
                        },
                        NullOutputReceiver()
                    )
                })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(AdbOverdraw.MSG_ADB_OVERDRAW)
        }
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "settings put system show_touches 0"
        const val ENABLE_CONFIGURATION = "settings put system show_touches 1"
        const val MSG_ADB = "label.action.screen.touches"
    }
}