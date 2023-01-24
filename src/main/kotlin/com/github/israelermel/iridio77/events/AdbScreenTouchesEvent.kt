package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbActionEvent
import com.github.israelermel.iridio77.extensions.toEnableOrDisable
import com.github.israelermel.iridio77.receivers.IRSingleLineReceiver
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbScreenTouchesEvent(val project: Project, val notification: IRNotification) : AdbActionEvent {

    override fun execute(device: IDevice) {
        try {
            device.executeShellCommand(GET_STATUS_CONFIGURATION, IRSingleLineReceiver { firstLine ->
                val isEnabled = firstLine.toEnableOrDisable().not()

                IRMessage.getAdbPropertyMessageFromBoolean(MSG_ADB, isEnabled).also {
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
            notification.showAdbNotificationError(AdbOverdrawEvent.MSG_ADB_OVERDRAW)
        }
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "settings put system show_touches 0"
        const val ENABLE_CONFIGURATION = "settings put system show_touches 1"
        const val GET_STATUS_CONFIGURATION = "settings get system show_touches"
        const val MSG_ADB = "label.action.screen.touches"
    }
}