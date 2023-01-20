package com.github.israelermel.iridio77.adb

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbResetConfiguration(val project: Project, val notification: IridioNotification) {

    fun execute(device: IDevice) {
        try {
            getAllCommands().forEach {
                device.executeShellCommand(it, NullOutputReceiver())
            }

        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_RESET_CONFIGURATION)
        }
    }

    private fun getAllCommands(): List<String> {
        return mutableListOf(
            AdbAnimations.DEFAULT_CONFIGURATION,
            AdbTalkback.DEFAULT_CONFIGURATION,
            AdbProfile.DEFAULT_CONFIGURATION,
            AdbOverdraw.DEFAULT_CONFIGURATION,
            AdbLayoutBounds.DEFAULT_CONFIGURATION,
            AdbScreenDensity.DEFAULT_CONFIGURATION,
            AdbFontSize.DEFAULT_CONFIGURATION,
            AdbColorInversion.DEFAULT_CONFIGURATION,
            AdbDisplayDaltonizer.DEFAULT_CONFIGURATION
        )
    }

    companion object {
        const val MSG_ADB_RESET_CONFIGURATION = "msg.error.adb.reset.configuration"
    }
}