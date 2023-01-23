package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.extensions.showNotification
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.github.israelermel.iridio77.utils.SingleLineLayoutSizeReceiver
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils

class AdbScreenDensityEvent(val project: Project, val notification: IridioNotification) {

    fun execute(device: IDevice, command: Command) {
        try {

            IridioMessage.getAdbChangePropertyMessage(MSG_ADB_SCREEN_DENSITY, command.getLabel()).also {
                notification.adbNotification(it)
            }

            device.executeShellCommand("wm density ${command.getCommand()}", NullOutputReceiver())

        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_SCREEN_DENSITY)
        }
    }

    fun getDefaultDensity(): Int? {
        val devices = project.let { AndroidSdkUtils.getDebugBridge(it)?.devices }

        var defaultDensity: Int? = null

        devices?.takeIf { it.isNotEmpty() }?.forEach { device ->
            device.executeShellCommand("wm density", SingleLineLayoutSizeReceiver { firstLine ->
                defaultDensity = firstLine.split(":")[1].trim().toInt()
            })
        } ?: kotlin.run {
            project.showNotification("${devices?.size} device(s) connected")
        }

        return defaultDensity
    }

    companion object {
        const val DEFAULT_CONFIGURATION = "wm density reset"
        const val MSG_ADB_SCREEN_DENSITY = "msgAdbDensity"
    }
}