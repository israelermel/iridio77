package com.github.israelermel.iridio77.adb

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.extensions.toEnableOrDisable
import com.github.israelermel.iridio77.utils.AndroidDebugBridgeManager
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbTalkback(val project: Project, val notification: IridioNotification) {

    private val MSG_ADB_TALKBACK = "msgAdbTalkback"
    private val DISABLE_TALKBACK =
        "settings put secure enabled_accessibility_services com.android.talkback/com.google.android.marvin.talkback.TalkBackService"
    private val ENABLE_TALKBACK =
        "settings put secure enabled_accessibility_services com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"

    fun execute(device: IDevice) {
        try {
            device.executeShellCommand("settings get secure accessibility_enabled",
                AndroidDebugBridgeManager.SingleLineAdbReceiver { firstLine ->

                    val isEnabled = firstLine.toEnableOrDisable().not()
                    val message = IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_TALKBACK, isEnabled)
                    notification.adbNotification(message)

                    when (isEnabled) {
                        true -> device.executeShellCommand(ENABLE_TALKBACK, NullOutputReceiver())
                        false -> device.executeShellCommand(DISABLE_TALKBACK, NullOutputReceiver())
                    }
                })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_TALKBACK)
        }
    }
}