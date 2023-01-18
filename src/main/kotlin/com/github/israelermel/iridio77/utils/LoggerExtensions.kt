package com.github.israelermel.iridio77.utils

import org.slf4j.Logger

//var logger: Logger = LoggerFactory.getLogger(ChangeFontSizeAction::class.java)
inline fun Logger.debug(lazyMessage: () -> String) {
    if (isDebugEnabled) {
        debug(lazyMessage())
    }
}