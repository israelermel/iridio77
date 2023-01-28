package com.github.israelermel.iridio77.listeners

import java.awt.event.KeyEvent
import java.awt.event.KeyListener

@FunctionalInterface
interface IRKeyListener : KeyListener {
    fun update(event : KeyEvent?)

    override fun keyTyped(e: KeyEvent?) {
        update(e)
    }

    override fun keyPressed(e: KeyEvent?) {
        update(e)
    }

    override fun keyReleased(e: KeyEvent?) {
        update(e)
    }
}