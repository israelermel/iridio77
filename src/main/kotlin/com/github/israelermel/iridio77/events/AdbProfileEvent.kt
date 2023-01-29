package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbActionEvent
import com.github.israelermel.iridio77.extensions.isEnabledFromBoolean
import com.github.israelermel.iridio77.receivers.IRSingleLineReceiver
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbProfileEvent(val project: Project, val notification: IRNotification) : AdbActionEvent {

    override fun execute(device: IDevice) {
        try {
            device.executeShellCommand(GET_STATUS_PROFILE, IRSingleLineReceiver { firstLine ->
                val isEnabled = firstLine.isEnabledFromBoolean()

                with(isEnabled.not()) {
                    IRMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_PROFILE, this).also {
                        notification.adbNotification(it)
                    }

                    when (this) {
                        true -> device.executeShellCommand(ENABLE_CONFIGURATION, NullOutputReceiver())
                        false -> device.executeShellCommand(DEFAULT_CONFIGURATION, NullOutputReceiver())
                    }
                }
            })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_PROFILE)
        }
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "setprop debug.hwui.profile false"
        const val ENABLE_CONFIGURATION = "setprop debug.hwui.profile visual_bars"
        const val MSG_ADB_PROFILE = "msg.adb.label.profile"
        const val GET_STATUS_PROFILE = "getprop debug.hwui.profile"
    }

}