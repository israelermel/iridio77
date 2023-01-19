package com.github.israelermel.iridio77.utils

import com.android.ddmlib.IDevice
import com.android.ddmlib.MultiLineReceiver
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.actions.adb.SingleLineLayoutBoundsReceiver
import com.github.israelermel.iridio77.adb.AdbAnimations
import com.github.israelermel.iridio77.extensions.toEnableOrDisable
import com.github.israelermel.iridio77.impl.AndroidDebugBridgeManagerImplementation
import com.github.israelermel.iridio77.models.AndroidDebugEvent
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.ui.models.LayoutSizes
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils

class AndroidDebugBridgeManager constructor(private val project: Project) : AndroidDebugBridgeManagerImplementation {

    private val DISABLE_OVERDRAW = "setprop debug.hwui.overdraw false"
    private val DISABLE_PROFILE = "setprop debug.hwui.profile false"
    private val DISABLE_TALKBACK =
        "settings put secure enabled_accessibility_services com.android.talkback/com.google.android.marvin.talkback.TalkBackService"

    private val notification by lazy { IridioNotification(project) }
    private val msgNoDeviceFound by lazy { IridioMessage.getMessageResource("msgNoDeviceFound") }
    private val adbAnimations by lazy { AdbAnimations(project, notification) }

    // MESSAGES
    private val MSG_ADB_TALKBACK = "msgAdbTalkback"
    private val MSG_ADB_PROFILE = "msgAdbProfile"
    private val MSG_ADB_OVERDRAW = "msgAdbOverdraw"
    private val MSG_ADB_LAYOUT_BOUNDS = "msgAdbLayoutBounds"
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

    override fun exeucteEventListener(execute: (device: IDevice) -> Unit) {
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
            AndroidDebugEvent.SHOW_LAYOUT_BOUNDS -> toggleLayoutBounds(device)
            AndroidDebugEvent.TOOGLE_TALKBACK -> toggleTalkback(device)
            AndroidDebugEvent.TOOGLE_ANIMATIONS -> adbAnimations.execute(device)
            AndroidDebugEvent.TOOGLE_PROFILE -> toogleProfile(device)
            AndroidDebugEvent.TOOGLE_OVERDRAW -> toogleOverdraw(device)
        }
    }

    override fun toogleOverdraw(device: IDevice) {
        try {
            device.executeShellCommand("getprop debug.hwui.overdraw",
                SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine != "false"

                    with(isEnabled.not()) {
                        IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_OVERDRAW, this).also {
                            notification.adbNotification(it)
                        }

                        when (this) {
                            true -> device.executeShellCommand(
                                "setprop debug.hwui.overdraw show",
                                NullOutputReceiver()
                            )

                            false -> device.executeShellCommand(
                                DISABLE_OVERDRAW,
                                NullOutputReceiver()
                            )
                        }
                    }
                })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_OVERDRAW)
        }
    }

    override fun toogleProfile(device: IDevice) {
        try {
            device.executeShellCommand("getprop debug.hwui.profile",
                SingleLineAdbReceiver { firstLine ->
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

    override fun toggleTalkback(device: IDevice) {
        try {
            device.executeShellCommand("settings get secure accessibility_enabled",
                SingleLineAdbReceiver { firstLine ->

                    val isEnabled = firstLine.toEnableOrDisable().not()
                    val message = IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_TALKBACK, isEnabled)
                    notification.adbNotification(message)

                    when (isEnabled) {
                        true -> device.executeShellCommand(
                            "settings put secure enabled_accessibility_services com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService",
                            NullOutputReceiver()
                        )

                        false -> device.executeShellCommand(DISABLE_TALKBACK, NullOutputReceiver())
                    }
                })
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_TALKBACK)
        }
    }

    override fun resizeLayoutDensity(layoutSizes: LayoutSizes) {
        try {

            IridioMessage.getAdbChangePropertyMessage(MSG_ADB_DENSITY, layoutSizes.label.orEmpty()).also {
                notification.adbNotification(it)
            }

            exeucteEventListener { device ->
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

            exeucteEventListener { device ->
                device.executeShellCommand(
                    "settings put system font_scale ${command.getCommand()}",
                    NullOutputReceiver()
                )
            }
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_ADB_FONT_SIZE)
        }
    }

    class SingleLineAdbReceiver(
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

    override fun toggleLayoutBounds(device: IDevice) {
        device.executeShellCommand(
            "getprop debug.layout",
            SingleLineLayoutBoundsReceiver { firstLine ->
                val isEnabled = firstLine.toBoolean().not()

                IridioMessage.getAdbPropertyMessageFromBoolean(MSG_ADB_LAYOUT_BOUNDS, isEnabled).also {
                    notification.adbNotification(it)
                }

                when (isEnabled) {
                    true -> device.executeShellCommand(
                        "setprop debug.layout true ; service call activity 1599295570",
                        NullOutputReceiver()
                    )

                    false -> device.executeShellCommand(
                        "setprop debug.layout false ; service call activity 1599295570",
                        NullOutputReceiver()
                    )
                }
            }
        )
    }

}