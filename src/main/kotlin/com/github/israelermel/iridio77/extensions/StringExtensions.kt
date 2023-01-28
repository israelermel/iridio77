package com.github.israelermel.iridio77.extensions

fun String.isEnabledFromBoolean(): Boolean {
    return this != "false"
}
