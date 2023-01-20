package com.github.israelermel.iridio77.adb

import com.github.israelermel.iridio77.extensions.showNotification
import com.github.israelermel.iridio77.utils.IridioNotification
import com.github.israelermel.iridio77.utils.SingleLineLayoutSizeReceiver
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils

class AdbDensity(val project: Project, val notification: IridioNotification) {

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
    }
}