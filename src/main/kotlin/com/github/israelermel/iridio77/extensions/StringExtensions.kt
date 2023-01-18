package com.github.israelermel.iridio77.extensions

import java.io.File
import java.util.concurrent.TimeUnit

fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String? = runCatching {
    ProcessBuilder("\\s".toRegex().split(this))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().also {
            it.waitFor(timeoutAmount, timeoutUnit)
        }
        .inputStream.bufferedReader().readText()
}.onFailure { it.printStackTrace() }.getOrNull()
