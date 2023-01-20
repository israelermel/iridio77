package com.github.israelermel.iridio77.utils

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.adb.*
import com.github.israelermel.iridio77.impl.AndroidDebugBridgeManagerImplementation
import com.github.israelermel.iridio77.models.AndroidDebugEvent
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.ui.models.LayoutSizes
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils

class AndroidDebugBridgeManager constructor(private val project: Project) : AndroidDebugBridgeManagerImplementation {

    private val notification by lazy { IridioNotification(project) }
    private val msgNoDeviceFound by lazy { IridioMessage.getMessageResource("msgNoDeviceFound") }
    private val adbAnimations by lazy { AdbAnimations(project, notification) }
    private val adbTalkback by lazy { AdbTalkback(project, notification) }
    private val adbLayoutBounds by lazy { AdbLayoutBounds(project, notification) }
    private val adbProfile by lazy { AdbProfile(project, notification) }
    private val adbOverdraw by lazy { AdbOverdraw(project, notification) }
    private val adbResetConfiguration by lazy { AdbResetConfiguration(project, notification) }
    private val adbDisplayDaltonizer by lazy { AdbDisplayDaltonizer(project, notification) }
    private val adbColorInversion by lazy { AdbColorInversion(project, notification) }

    // MESSAGES
    private val MSG_ADB_FONT_SIZE = "msgAdbFontSize"
    private val MSG_ADB_DENSITY = "msgAdbDensity"

    override fun onDebugEventTriggered(event: AndroidDebugEvent) {
        val connectedDevices = AndroidSdkUtils.getDebugBridge(project)?.devices
        if (connectedDevices.isNullOrEmpty()) {
            notification.adbNotification(msgNoDeviceFound)
            return
        }

        connectedDevices.forEach {
            executeEvent(event, it)
        }
    }

    override fun executeEventListener(execute: (device: IDevice) -> Unit) {
        val connectedDevices = AndroidSdkUtils.getDebugBridge(project)?.devices
        if (connectedDevices.isNullOrEmpty()) {
            notification.adbNotification(msgNoDeviceFound)
            return
        }

        connectedDevices.forEach {
            execute(it)
        }
    }

    override fun executeEvent(event: AndroidDebugEvent, device: IDevice) {
        when (event) {
            AndroidDebugEvent.SHOW_LAYOUT_BOUNDS -> adbLayoutBounds.execute(device)
            AndroidDebugEvent.TOOGLE_TALKBACK -> adbTalkback.execute(device)
            AndroidDebugEvent.TOOGLE_ANIMATIONS -> adbAnimations.execute(device)
            AndroidDebugEvent.TOOGLE_PROFILE -> adbProfile.execute(device)
            AndroidDebugEvent.TOOGLE_OVERDRAW -> adbOverdraw.execute(device)
            AndroidDebugEvent.RESET_CONFIGURATION -> adbResetConfiguration.execute(device)
            AndroidDebugEvent.TOOGLE_COLOR_INVERSION -> adbColorInversion.execute(device)
        }
    }

    override fun resizeLayoutDensity(layoutSizes: LayoutSizes) {
        try {

            IridioMessage.getAdbChangePropertyMessage(MSG_ADB_DENSITY, layoutSizes.label.orEmpty()).also {
                notification.adbNotification(it)
            }

            executeEventListener { device ->
                device.executeShellCommand("wm density ${layoutSizes.size}", NullOutputReceiver())
            }
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_DENSITY)
        }
    }

    override fun changeFontSize(command: Command) {
        try {
            IridioMessage.getAdbChangePropertyMessage(MSG_ADB_FONT_SIZE, command.getCommand()).also {
                notification.adbNotification(it)
            }

            executeEventListener { device ->
                device.executeShellCommand(
                    "settings put system font_scale ${command.getCommand()}",
                    NullOutputReceiver()
                )
            }
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_FONT_SIZE)
        }
    }

    override fun changeDisplayDaltonizer(command: Command) {
        executeEventListener {
            adbDisplayDaltonizer.execute(it, command)
        }
    }
}