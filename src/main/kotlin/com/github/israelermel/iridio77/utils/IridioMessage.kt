package com.github.israelermel.iridio77.utils

import com.github.israelermel.iridio77.IridioBundle

object IridioMessage {

    fun getAdbPropertyMessageFromBoolean(property: String, isEnabled: Boolean): String {
        val msgProperty = getMessageResource(property)

        return IridioBundle.message(
            "msgAdbProperty",
            msgProperty,
            getLabelFromBoolean(isEnabled)
        )
    }

    fun getMessageResource(property: String): String {
        return IridioBundle.message(property)
    }
    fun getLabelFromBoolean(status: Boolean) =
        if (status) getMessageResource("msgEnabled") else getMessageResource("msgDisabled")

    fun getAdbChangePropertyMessage(property: String, value: String): String {
        val msgProperty = getMessageResource(property)

        return IridioBundle.message(
            "msgAdbChangeProperty",
            msgProperty,
            value
        )
    }

    fun getErrorMessage(property: String): String {
        val msgProperty = getMessageResource(property)
        return IridioBundle.message(
            "msgErrorChangeProperty",
            msgProperty
        )
    }
}