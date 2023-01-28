package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbActionEvent
import com.github.israelermel.iridio77.extensions.doubleIsEnable
import com.github.israelermel.iridio77.receivers.IRSingleLineReceiver
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbAnimationsEvent(val project: Project, val notification: IRNotification) : AdbActionEvent {

    override fun execute(device: IDevice) {
        try {
            device.executeShellCommand(GET_STATUS_CONFIGURATION, IRSingleLineReceiver { firstLine ->
                val isEnabled = firstLine.doubleIsEnable().not()

                IRMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_ANIMATIONS, isEnabled).also {
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
        const val MSG_ADB_ANIMATIONS = "msg.adb.label.animations"
        const val ENABLE_ANIMATIONS =
            "settings put global window_animation_scale 0.0 ; settings put global animator_duration_scale 0.0 ; settings put global transition_animation_scale 0.0"
        const val DEFAULT_CONFIGURATION =
            "settings put global window_animation_scale 1.0 ; settings put global animator_duration_scale 1.0 ; settings put global transition_animation_scale 1.0"
        const val GET_STATUS_CONFIGURATION = "settings get global window_animation_scale"
    }
}