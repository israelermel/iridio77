package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbFormActionEvent
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbFontSizeEvent(
    val project: Project,
    val notification: IRNotification
) : AdbFormActionEvent {

    override fun execute(device: IDevice, command: Command) {
        try {
            IRMessage.getAdbChangePropertyMessage(MSG_ADB_FONT_SIZE, command.getCommand()).also {
                notification.adbNotification(it)
            }

            device.executeShellCommand(getEnableCommand(command), NullOutputReceiver())

        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_FONT_SIZE)
        }
    }

    private fun getEnableCommand(command: Command): String {
        return "$ENABLE_CONFIGURATION ${command.getCommand()}"
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "settings put system font_scale 1.0"
        const val ENABLE_CONFIGURATION = "settings put system font_scale"
        const val MSG_ADB_FONT_SIZE = "msg.adb.label.font.size"
    }
}