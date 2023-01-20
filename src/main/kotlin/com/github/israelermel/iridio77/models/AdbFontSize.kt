package com.github.israelermel.iridio77.models

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbFontSize(
    val project: Project,
    val notification: IridioNotification
) {

    fun execute(device: IDevice, command: Command) {
        try {
            IridioMessage.getAdbChangePropertyMessage(MSG_ADB_FONT_SIZE, command.getCommand()).also {
                notification.adbNotification(it)
            }

            device.executeShellCommand(
                "settings put system font_scale ${command.getCommand()}",
                NullOutputReceiver()
            )
            
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_FONT_SIZE)
        }
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "settings put system font_scale 1.0"
        const val MSG_ADB_FONT_SIZE = "msgAdbFontSize"
    }
}