package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.persistancestate.DisplayDaltonizerPersistanceState
import com.github.israelermel.iridio77.persistancestate.FontSizePersistanceState
import com.github.israelermel.iridio77.persistancestate.LayoutSizePersistanceState
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class AdbResetConfigurationEvent(val project: Project, val notification: IridioNotification) : AdbActionEvent {

    override fun execute(device: IDevice) {
        try {
            getAllCommands().forEach {
                device.executeShellCommand(it, NullOutputReceiver())
            }

            FontSizePersistanceState.getInstance(project).clearData()
            DisplayDaltonizerPersistanceState.getInstance(project).clearData()
            LayoutSizePersistanceState.getInstance(project).clearData()

            IridioMessage.getAdbPropertyMessage(MSG_ADB_SUCCESS_RESET_CONFIGURATION).also {
                notification.adbNotification(it)
            }
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_ERROR_RESET_CONFIGURATION)
        }
    }

    private fun getAllCommands(): List<String> {
        return mutableListOf(
            AdbAnimationsEvent.DEFAULT_CONFIGURATION,
            AdbTalkbackEvent.DEFAULT_CONFIGURATION,
            AdbProfileEvent.DEFAULT_CONFIGURATION,
            AdbOverdrawEvent.DEFAULT_CONFIGURATION,
            AdbLayoutBoundsEvent.DEFAULT_CONFIGURATION,
            AdbScreenDensityEvent.DEFAULT_CONFIGURATION,
            AdbFontSizeEvent.DEFAULT_CONFIGURATION,
            AdbColorInversionEvent.DEFAULT_CONFIGURATION,
            AdbDisplayDaltonizerEvent.DEFAULT_CONFIGURATION,
            AdbScreenTouchesEvent.DEFAULT_CONFIGURATION
        )
    }

    companion object {
        const val MSG_ADB_ERROR_RESET_CONFIGURATION = "msg.error.adb.reset.configuration"
        const val MSG_ADB_SUCCESS_RESET_CONFIGURATION = "msg.success.adb.reset.configuration"
    }
}