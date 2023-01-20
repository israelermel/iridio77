package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.models.AndroidDebugEvent
import com.github.israelermel.iridio77.impl.AndroidDebugBridgeManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ToogleAnimationsAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            AndroidDebugBridgeManager(it).onDebugEventTriggered(AndroidDebugEvent.TOOGLE_ANIMATIONS)
        }
    }
}