package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.extensions.doubleIsEnable
import com.github.israelermel.iridio77.receivers.SingleLineAdbReceiver
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbAnimationsEvent(val project: Project, val notification: IridioNotification) : AdbActionEvent {

    override fun execute(device: IDevice) {
        try {
            device.executeShellCommand("settings get global window_animation_scale",
                SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine.doubleIsEnable().not()

                    IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_ANIMATIONS, isEnabled).also {
                        notification.adbNotification(it)
                    }

                    when (isEnabled) {
                        true -> device.executeShellCommand(DEFAULT_CONFIGURATION, NullOutputReceiver())
                        false -> device.executeShellCommand(ENABLE_ANIMATIONS, NullOutputReceiver())
                    }
                })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_ANIMATIONS)
        }
    }

    companion object {
        const val MSG_ADB_ANIMATIONS = "msgAdbAnimations"
        const val ENABLE_ANIMATIONS =
            "settings put global window_animation_scale 0.0 ; settings put global animator_duration_scale 0.0 ; settings put global transition_animation_scale 0.0"
        const val DEFAULT_CONFIGURATION =
            "settings put global window_animation_scale 1.0 ; settings put global animator_duration_scale 1.0 ; settings put global transition_animation_scale 1.0"

    }
}