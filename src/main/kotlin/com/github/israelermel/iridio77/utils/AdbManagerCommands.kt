package com.github.israelermel.iridio77.utils

import com.android.ddmlib.IDevice
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils

class AdbManagerCommands(val project: Project, val notification: IridioNotification) {

    private val msgNoDeviceFound by lazy { IridioMessage.getMessageResource("msg.adb.no.device.found") }
    
    fun executeEvent(execute: (device: IDevice) -> Unit) {
        val connectedDevices = AndroidSdkUtils.getDebugBridge(project)?.devices
        if (connectedDevices.isNullOrEmpty()) {
            notification.adbNotification(msgNoDeviceFound)
            return
        }

        connectedDevices.forEach {
            execute(it)
        }
    }
}