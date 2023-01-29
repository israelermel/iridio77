package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbFormActionEvent
import com.github.israelermel.iridio77.extensions.showNotification
import com.github.israelermel.iridio77.receivers.IRSingleLineReceiver
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils

class AdbScreenDensityEvent(val project: Project, val notification: IRNotification) : AdbFormActionEvent {

    override fun execute(device: IDevice, command: Command) {
        try {

            IRMessage.getAdbChangePropertyMessage(MSG_ADB_SCREEN_DENSITY, command.getLabel()).also {
                notification.adbNotification(it)
            }

            device.executeShellCommand(commandChangeScreenDensity(command), NullOutputReceiver())

        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_SCREEN_DENSITY)
        }
    }

    private fun commandChangeScreenDensity(command: Command): String {
        return "$GET_STATUS_CONFIGURATION ${command.getCommand()}"
    }

    fun getDefaultDensity(): Int? {
        val devices = project.let { AndroidSdkUtils.getDebugBridge(it)?.devices }

        var defaultDensity: Int? = null

        devices?.takeIf { it.isNotEmpty() }?.forEach { device ->
            device.executeShellCommand(GET_STATUS_CONFIGURATION, IRSingleLineReceiver { firstLine ->
                defaultDensity = firstLine.split(":")[1].trim().toInt()
            })
        } ?: kotlin.run {
            project.showNotification(IRMessage.getMessageResource("msg.adb.no.device.found"))
        }

        return defaultDensity
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "wm density reset"
        const val GET_STATUS_CONFIGURATION = "wm density"
        const val MSG_ADB_SCREEN_DENSITY = "msg.adb.label.density"
    }
}