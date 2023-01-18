package com.github.israelermel.iridio77.utils

import com.android.ddmlib.IDevice
import com.android.ddmlib.MultiLineReceiver
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.actions.adb.SingleLineLayoutBoundsReceiver
import com.github.israelermel.iridio77.extensions.doubleIsEnable
import com.github.israelermel.iridio77.extensions.showNotification
import com.github.israelermel.iridio77.extensions.showNotificationError
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
    private val DISABLE_ANIMATIONS =
        "settings put global window_animation_scale 0.0 ; settings put global animator_duration_scale 0.0 ; settings put global transition_animation_scale 0.0"
    private val DISABLE_TALKBACK =
        "settings put secure enabled_accessibility_services com.android.talkback/com.google.android.marvin.talkback.TalkBackService"

    private val msgNoDeviceFound by lazy { getMessageResource("msgNoDeviceFound") }

    // MESSAGES
    private val MSG_ADB_TALKBACK = "msgAdbTalkback"
    private val MSG_ADB_PROFILE = "msgAdbProfile"
    private val MSG_ADB_OVERDRAW = "msgAdbOverdraw"
    private val MSG_ADB_ANIMATIONS = "msgAdbAnimations"
    private val MSG_ADB_LAYOUT_BOUNDS = "msgAdbLayoutBounds"
    private val MSG_ADB_FONT_SIZE = "msgAdbFontSize"
    private val MSG_ADB_DENSITY = "msgAdbDensity"
    private val ADB_TITLE = "ADB Events"

    override fun onDebugEventTriggered(event: AndroidDebugEvent) {
        val connectedDevices = AndroidSdkUtils.getDebugBridge(project)?.devices
        if (connectedDevices.isNullOrEmpty()) {
            project.showNotification(msgNoDeviceFound, ADB_TITLE)
            return
        }

        connectedDevices.forEach {
            executeEvent(event, it)
        }
    }

    override fun exeucteEventListener(execute: (device: IDevice) -> Unit) {
        val connectedDevices = AndroidSdkUtils.getDebugBridge(project)?.devices
        if (connectedDevices.isNullOrEmpty()) {
            project.showNotification(msgNoDeviceFound, ADB_TITLE)
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
            AndroidDebugEvent.TOOGLE_ANIMATIONS -> toogleAnimations(device)
            AndroidDebugEvent.TOOGLE_PROFILE -> toogleProfile(device)
            AndroidDebugEvent.TOOGLE_OVERDRAW -> toogleOverdraw(device)
        }
    }

    override fun toogleAnimations(device: IDevice) {
        try {
            device.executeShellCommand("settings get global window_animation_scale",
                SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine.doubleIsEnable().not()

                    getAdbPropertyMessageFromBoolean(MSG_ADB_ANIMATIONS, isEnabled).also {
                        notification(it)
                    }

                    when (isEnabled) {
                        true -> device.executeShellCommand(
                            "settings put global window_animation_scale 1.0 ; settings put global animator_duration_scale 1.0 ; settings put global transition_animation_scale 1.0",
                            NullOutputReceiver()
                        )

                        false -> device.executeShellCommand(
                            DISABLE_ANIMATIONS,
                            NullOutputReceiver()
                        )
                    }
                })
        } catch (ex: Exception) {
            showAdbNotificationError(MSG_ADB_ANIMATIONS)
        }
    }

    override fun toogleOverdraw(device: IDevice) {
        try {
            device.executeShellCommand("getprop debug.hwui.overdraw",
                SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine != "false"

                    with(isEnabled.not()) {
                        getAdbPropertyMessageFromBoolean(MSG_ADB_OVERDRAW, this).also {
                            notification(it)
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
            showAdbNotificationError(MSG_ADB_OVERDRAW)
        }
    }

    override fun toogleProfile(device: IDevice) {
        try {
            device.executeShellCommand("getprop debug.hwui.profile",
                SingleLineAdbReceiver { firstLine ->
                    val isEnabled = firstLine != "false"

                    with(isEnabled.not()) {

                        getAdbPropertyMessageFromBoolean(MSG_ADB_PROFILE, this).also {
                            notification(it)
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
            showAdbNotificationError(MSG_ADB_PROFILE)
        }
    }

    override fun toggleTalkback(device: IDevice) {
        try {
            device.executeShellCommand("settings get secure accessibility_enabled",
                SingleLineAdbReceiver { firstLine ->

                    val isEnabled = firstLine.toEnableOrDisable().not()
                    val message = getAdbPropertyMessageFromBoolean(MSG_ADB_TALKBACK, isEnabled)
                    notification(message)

                    when (isEnabled) {
                        true -> device.executeShellCommand(
                            "settings put secure enabled_accessibility_services com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService",
                            NullOutputReceiver()
                        )

                        false -> device.executeShellCommand(DISABLE_TALKBACK, NullOutputReceiver())
                    }
                })
        } catch (ex: Exception) {
            showAdbNotificationError(MSG_ADB_TALKBACK)
        }
    }

    override fun resizeLayoutDensity(layoutSizes: LayoutSizes) {
        try {

            getAdbChangePropertyMessage(MSG_ADB_DENSITY, layoutSizes.label.orEmpty()).also {
                notification(it)
            }

            exeucteEventListener { device ->
                device.executeShellCommand("wm density ${layoutSizes.size}", NullOutputReceiver())
            }
        } catch (ex: Exception) {
            showAdbNotificationError(MSG_ADB_DENSITY)
        }
    }

    override fun changeFontSize(command: Command) {
        try {

            getAdbChangePropertyMessage(MSG_ADB_FONT_SIZE, command.getCommand()).also {
                notification(it)
            }

            exeucteEventListener { device ->
                device.executeShellCommand(
                    "settings put system font_scale ${command.getCommand()}",
                    NullOutputReceiver()
                )
            }
        } catch (ex: Exception) {
            showAdbNotificationError(MSG_ADB_FONT_SIZE)
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

                getAdbPropertyMessageFromBoolean(MSG_ADB_LAYOUT_BOUNDS, isEnabled).also {
                    notification(it)
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


    private fun getMessageResource(property: String): String {
        return IridioBundle.message(property)
    }

    private fun getLabelFromBoolean(status: Boolean) =
        if (status) getMessageResource("msgEnabled") else getMessageResource("msgDisabled")

    private fun getAdbPropertyMessageFromBoolean(property: String, isEnabled: Boolean): String {
        val msgProperty = getMessageResource(property)

        return IridioBundle.message(
            "msgAdbProperty",
            msgProperty,
            getLabelFromBoolean(isEnabled)
        )
    }

    private fun getAdbChangePropertyMessage(property: String, value: String): String {
        val msgProperty = getMessageResource(property)

        return IridioBundle.message(
            "msgAdbChangeProperty",
            msgProperty,
            value
        )
    }

    private fun getErrorMessage(property: String): String {
        val msgProperty = getMessageResource(property)
        return IridioBundle.message(
            "msgErrorChangeProperty",
            msgProperty
        )
    }

    private fun showAdbNotificationError(property: String) {
        project.showNotificationError(
            getErrorMessage(property), ADB_TITLE
        )
    }

    private fun notification(msg: String) {
        project.showNotification(
            msg,
            ADB_TITLE
        )
    }

}