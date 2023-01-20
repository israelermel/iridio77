package com.github.israelermel.iridio77.actions.adb

import com.github.israelermel.iridio77.models.AndroidDebugEvent
import com.github.israelermel.iridio77.utils.AndroidDebugBridgeManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ToogleColorInversionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            AndroidDebugBridgeManager(it).onDebugEventTriggered(AndroidDebugEvent.TOOGLE_COLOR_INVERSION)
        }
    }
}