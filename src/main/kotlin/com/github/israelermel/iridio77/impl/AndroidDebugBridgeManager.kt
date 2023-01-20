package com.github.israelermel.iridio77.impl

import com.android.ddmlib.IDevice
import com.github.israelermel.iridio77.models.*
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.utils.IridioMessage
import com.github.israelermel.iridio77.utils.IridioNotification
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
    private val adbScreenDensity by lazy { AdbScreenDensity(project, notification) }
    private val adbFontSize by lazy { AdbFontSize(project, notification) }
    private val adbScreenTouches by lazy { AdbScreenTouches(project, notification) }

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
            AndroidDebugEvent.TOOGLE_SCREEN_TOUCHES -> adbScreenTouches.execute(device)
        }
    }

    override fun screenDensity(command: Command) {
        executeEventListener {
            adbScreenDensity.execute(it, command)
        }
    }

    override fun changeFontSize(command: Command) {
        executeEventListener {
            adbFontSize.execute(it, command)
        }
    }

    override fun changeDisplayDaltonizer(command: Command) {
        executeEventListener {
            adbDisplayDaltonizer.execute(it, command)
        }
    }
}