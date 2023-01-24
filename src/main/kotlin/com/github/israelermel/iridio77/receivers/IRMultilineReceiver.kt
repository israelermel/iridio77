package com.github.israelermel.iridio77.receivers

import com.android.ddmlib.MultiLineReceiver

class IRMultilineReceiver(
    private val processFirstLine: (response: Array<out String>?) -> Unit
) : MultiLineReceiver() {
    private var cancelled = false
    override fun isCancelled(): Boolean = cancelled

    override fun processNewLines(lines: Array<out String>) {
        processFirstLine(lines)
        cancelled = true
    }
}