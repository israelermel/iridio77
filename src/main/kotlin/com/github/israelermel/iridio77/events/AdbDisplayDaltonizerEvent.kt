package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbFormActionEvent
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbDisplayDaltonizerEvent(
    val project: Project,
    val notification: IRNotification
) : AdbFormActionEvent {

    override fun execute(device: IDevice, command: Command) {
        try {

            IRMessage.getAdbChangePropertyMessage(MSG_ADB_DISPLAY_DALTONIZER, command.getLabel()).also {
                notification.adbNotification(it)
            }

            val displayDaltonizerCommand = StringBuilder()

            if (disabledCommandIsSelected(command)) {
                displayDaltonizerCommand.append(DEFAULT_CONFIGURATION)
            } else {
                displayDaltonizerCommand.append(ENABLE_DALTONIZER)
                displayDaltonizerCommand.append(getEnableCommand(command))
            }

            device.executeShellCommand(displayDaltonizerCommand.toString(), NullOutputReceiver())

        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_DISPLAY_DALTONIZER)
        }
    }

    private fun disabledCommandIsSelected(command: Command) = command.getCommand().toInt() < 0

    private fun getEnableCommand(command: Command): String {
        return "$ENABLE_CONFIGURATION ${command.getCommand()}"
    }

    companion object {
        const val MSG_ADB_DISPLAY_DALTONIZER = "label.action.daltonizer"
        const val DEFAULT_CONFIGURATION = "settings put secure accessibility_display_daltonizer_enabled 0"
        const val ENABLE_DALTONIZER = "settings put secure accessibility_display_daltonizer_enabled 1"
        const val ENABLE_CONFIGURATION = "; settings put secure accessibility_display_daltonizer"
    }
}