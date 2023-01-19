package com.github.israelermel.iridio77.adb

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.utils.AndroidDebugBridgeManager
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbProfile(val project: Project, val notification: IridioNotification) {

    private val DISABLE_PROFILE = "setprop debug.hwui.profile false"
    private val MSG_ADB_PROFILE = "msgAdbProfile"

    fun execute(device: IDevice) {
        try {
            device.executeShellCommand("getprop debug.hwui.profile",
                AndroidDebugBridgeManager.SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine != "false"

                    with(isEnabled.not()) {

                        IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_PROFILE, this).also {
                            notification.adbNotification(it)
                        }

                        when (this) {
                            true -> device.executeShellCommand(
                                "setprop debug.hwui.profile visual_bars",
                                NullOutputReceiver()
                            )

                            false -> device.executeShellCommand(
                                DISABLE_PROFILE,
                                NullOutputReceiver()
                            )
                        }
                    }

                })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_PROFILE)
        }
    }

}