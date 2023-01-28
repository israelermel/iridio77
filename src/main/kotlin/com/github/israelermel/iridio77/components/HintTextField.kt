package com.github.israelermel.iridio77.components

import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.JTextField

class HintTextField(private val hint: String) : JTextField(hint), FocusListener {
    private var showingHint = true

    init {
        super.addFocusListener(this)
    }

    override fun focusGained(e: FocusEvent) {
        if (this.text.isEmpty()) {
            super.setText("")
            showingHint = false
        }
    }

    override fun focusLost(e: FocusEvent) {
        if (this.text.isEmpty()) {
            super.setText(hint)
            showingHint = true
        }
    }

    override fun getText(): String {
        return if (showingHint) "" else super.getText()
    }
}