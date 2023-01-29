package com.github.israelermel.iridio77.utils

import com.github.israelermel.iridio77.IridioBundle

object IRMessage {

    fun getMessageResourceWithDescription(property: String, desc:String) :String {
        return "${getMessageResource(property)}\n $desc"
    }

    fun getAdbPropertyMessageFromBoolean(property: String, isEnabled: Boolean): String {
        val msgProperty = getMessageResource(property)

        return IridioBundle.message(
            "msg.adb.property",
            msgProperty,
            getLabelFromBoolean(isEnabled)
        )
    }

    fun getAdbPropertyMessage(property: String): String {
        val msgProperty = getMessageResource(property)

        return IridioBundle.message(
            "msg.adb.single.property",
            msgProperty
        )
    }

    fun getMessageResource(property: String): String {
        return IridioBundle.message(property)
    }

    fun getLabelFromBoolean(status: Boolean) =
        if (status) getMessageResource("msg.adb.enabled") else getMessageResource("msg.adb.disabled")

    fun getAdbChangePropertyMessage(property: String, value: String): String {
        val msgProperty = getMessageResource(property)

        return IridioBundle.message(
            "msg.adb.change.property",
            msgProperty,
            value
        )
    }

    fun getErrorMessage(property: String): String {
        val msgProperty = getMessageResource(property)
        return IridioBundle.message(
            "msg.adb.error.change.property",
            msgProperty
        )
    }
}