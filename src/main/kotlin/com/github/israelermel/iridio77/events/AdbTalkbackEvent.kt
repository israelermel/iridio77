package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbActionEvent
import com.github.israelermel.iridio77.extensions.toEnableOrDisable
import com.github.israelermel.iridio77.receivers.IRSingleLineReceiver
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbTalkbackEvent(val project: Project, val notification: IRNotification) : AdbActionEvent {

    override fun execute(device: IDevice) {
        try {
            device.executeShellCommand(GET_STATUS_CONFIGURATION, IRSingleLineReceiver { firstLine ->
                val isEnabled = firstLine.toEnableOrDisable().not()
                val message = IRMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_TALKBACK, isEnabled)
                notification.adbNotification(message)

                when (isEnabled) {
                    true -> device.executeShellCommand(ENABLE_TALKBACK, NullOutputReceiver())
                    false -> device.executeShellCommand(DEFAULT_CONFIGURATION, NullOutputReceiver())
                }
            })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_TALKBACK)
        }
    }

    companion object {
        const val DEFAULT_CONFIGURATION =
            "settings put secure enabled_accessibility_services com.android.talkback/com.google.android.marvin.talkback.TalkBackService"
        const val MSG_ADB_TALKBACK = "msg.adb.label.talkback"
        const val ENABLE_TALKBACK =
            "settings put secure enabled_accessibility_services com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"
        const val GET_STATUS_CONFIGURATION = "settings get secure accessibility_enabled"
    }
}