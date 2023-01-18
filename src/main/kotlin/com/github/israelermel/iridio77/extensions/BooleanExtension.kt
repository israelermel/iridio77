package com.github.israelermel.iridio77.extensions

fun Boolean.toIntString() = if (this) "1" else "0"
fun String.toEnableOrDisable() = this == "1"
fun String.doubleIsEnable() = this >= "1.0"