package com.github.israelermel.iridio77.events

data class FilePropertiesState(
    var keyBuildType: String = "BUILD_TYPE",
    var valueBuildType: String = "DEBUG",
    var keySampleBuildFlavor: String = "SAMPLE_BUILD_FLAVOR",
    var valueSampleBuildFlavor: String = ""
)
