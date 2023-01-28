package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbActionEvent
import com.github.israelermel.iridio77.extensions.isEnabledFromBoolean
import com.github.israelermel.iridio77.receivers.IRSingleLineReceiver
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbOverdrawEvent(val project: Project, val notification: IRNotification) : AdbActionEvent {

    override fun execute(device: IDevice) {
        try {
            device.executeShellCommand(GET_STATUS_CONFIGURATION, IRSingleLineReceiver { firstLine ->
                val isEnabled = firstLine.isEnabledFromBoolean()

                with(isEnabled.not()) {
                    IRMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_OVERDRAW, this).also {
                        notification.adbNotification(it)
                    }

                    when (this) {
                        true -> device.executeShellCommand(ENABLE_CONFIGURATION, NullOutputReceiver())
                        false -> device.executeShellCommand(DEFAULT_CONFIGURATION, NullOutputReceiver())
                    }
                }
            })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_OVERDRAW)
        }
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "setprop debug.hwui.overdraw false"
        const val ENABLE_CONFIGURATION = "setprop debug.hwui.overdraw show"
        const val GET_STATUS_CONFIGURATION = "getprop debug.hwui.overdraw"
        const val MSG_ADB_OVERDRAW = "msg.adb.label.overdraw"
    }
}