package com.github.israelermel.iridio77.adb

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbDisplayDaltonizer(
    val project: Project,
    val notification: IridioNotification
) {

    fun execute(device: IDevice, command: Command) {
        try {

            IridioMessage.getAdbChangePropertyMessage(MSG_ADB_DISPLAY_DALTONIZER, command.getLabel()).also {
                notification.adbNotification(it)
            }

            val displayDaltonizerCommand = StringBuilder()

            if (command.getCommand().toInt() < 0) {
                displayDaltonizerCommand.append(DEFAULT_CONFIGURATION)
            } else {
                displayDaltonizerCommand.append(ENABLE_CONFIGURATION)
                displayDaltonizerCommand.append("; settings put secure accessibility_display_daltonizer ${command.getCommand()}")
            }

            device.executeShellCommand(displayDaltonizerCommand.toString(), NullOutputReceiver())

        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_DISPLAY_DALTONIZER)
        }
    }

    companion object {
        const val MSG_ADB_DISPLAY_DALTONIZER = "label.action.daltonizer"
        const val DEFAULT_CONFIGURATION = "settings put secure accessibility_display_daltonizer_enabled 0"
        const val ENABLE_CONFIGURATION = "settings put secure accessibility_display_daltonizer_enabled 1"
    }
}