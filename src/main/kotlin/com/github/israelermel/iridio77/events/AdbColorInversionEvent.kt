package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbActionEvent
import com.github.israelermel.iridio77.extensions.toEnableOrDisable
import com.github.israelermel.iridio77.receivers.IRSingleLineReceiver
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbColorInversionEvent(
    val project: Project,
    val notification: IRNotification
) : AdbActionEvent {

    override fun execute(device: IDevice) {
        device.executeShellCommand(GET_STATUS_CONFIGURATION, IRSingleLineReceiver { firstLine ->
            val isEnabled = firstLine.toEnableOrDisable().not()

            IRMessage.getAdbPropertyMessageFromBoolean(MSG_ADB, isEnabled).also {
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
        const val GET_STATUS_CONFIGURATION = "settings get secure accessibility_display_inversion_enabled"
        const val MSG_ADB = "label.action.color.inversion"
    }
}