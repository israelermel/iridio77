package com.github.israelermel.iridio77.impl

import com.android.ddmlib.IDevice
import com.github.israelermel.iridio77.models.AndroidDebugEvent
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.ui.models.LayoutSizes

interface AndroidDebugBridgeManagerImplementation {

    fun onDebugEventTriggered(event: AndroidDebugEvent)
    fun executeEvent(event: AndroidDebugEvent, device: IDevice)
    fun toggleLayoutBounds(device: IDevice)
    fun toggleTalkback(device: IDevice)
    fun togoleOverdraw(device: IDevice)
    fun changeFontSize(command: Command)
    fun toogleAnimations(device: IDevice)
    fun toogleProfile(device: IDevice)
    fun resizeLayoutDensity(layoutSizes: LayoutSizes)
    fun toggleOverdrawAreas(isEnabled: Boolean, device: IDevice)

    fun exeucteEventListener(execute: (device: IDevice) -> Unit)

    fun isGradleSyncExecution(): Boolean

}
