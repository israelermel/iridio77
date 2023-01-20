package com.github.israelermel.iridio77.actions

import com.android.ddmlib.IDevice
import com.android.ddmlib.MultiLineReceiver
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.extensions.showNotification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils

class ToogleLayoutBoundsAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val devices = event.project?.let { AndroidSdkUtils.getDebugBridge(it)?.devices }

        devices?.takeIf { it.isNotEmpty() }?.forEach { device ->
            device.executeShellCommand(
                "getprop debug.layout",
                SingleLineLayoutBoundsReceiver { firstLine ->
                    val enable = firstLine.toBoolean().not()
                    device.setLayoutBounds(enable)
                }
            )
        } ?: kotlin.run {
            event.project?.showNotification("${devices?.size} device(s) connected")
        }
    }
}

class SingleLineLayoutBoundsReceiver(
    private val processFirstLine: (response: String) -> Unit
) : MultiLineReceiver() {
    private var cancelled = false
    override fun isCancelled(): Boolean = cancelled

    override fun processNewLines(lines: Array<out String>?) {
        lines?.getOrNull(0)?.let { firstLine ->
            processFirstLine(firstLine)
            cancelled = true
        }
    }

}

private fun IDevice.setLayoutBounds(enable: Boolean) {
    executeShellCommand(
        "setprop debug.layout $enable ; service call activity 1599295570",
        NullOutputReceiver()
    )
}