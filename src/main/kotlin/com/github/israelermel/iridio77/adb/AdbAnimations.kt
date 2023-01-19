package com.github.israelermel.iridio77.adb

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.extensions.doubleIsEnable
import com.github.israelermel.iridio77.utils.AndroidDebugBridgeManager
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbAnimations(val project: Project, val notification: IridioNotification) {

    private val MSG_ADB_ANIMATIONS = "msgAdbAnimations"

    private val ENABLE_ANIMATIONS =
        "settings put global window_animation_scale 1.0 ; settings put global animator_duration_scale 1.0 ; settings put global transition_animation_scale 1.0"
    private val DISABLE_ANIMATIONS =
        "settings put global window_animation_scale 0.0 ; settings put global animator_duration_scale 0.0 ; settings put global transition_animation_scale 0.0"

    fun execute(device: IDevice) {
        try {
            device.executeShellCommand("settings get global window_animation_scale",
                AndroidDebugBridgeManager.SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine.doubleIsEnable().not()

                    IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_ANIMATIONS, isEnabled).also {
                        notification.adbNotification(it)
                    }

                    when (isEnabled) {
                        true -> device.executeShellCommand(ENABLE_ANIMATIONS, NullOutputReceiver())
                        false -> device.executeShellCommand(DISABLE_ANIMATIONS, NullOutputReceiver())
                    }
                })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_ANIMATIONS)
        }
    }

}