package com.github.israelermel.iridio77.utils

import java.awt.Desktop
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object IRFileUtils {

    fun addProperty(filePath: String, key: String, value: String) {
        val properties = Properties()

        // Load the existing properties file
        val inputStream = FileInputStream(filePath)
        properties.load(inputStream)
        inputStream.close()

        // Set the new property
        properties.setProperty(key, value)

        // Save the properties file
        val outputStream = FileOutputStream(filePath)
        properties.store(outputStream, null)
        outputStream.close()
    }

    fun openHtml(basePath: String) {
        val htmlFile = File(basePath)
        Desktop.getDesktop().browse(htmlFile.toURI())
    }
}