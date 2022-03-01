/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api

import world.komq.paralleluniverse.api.logger.CustomLogger
import java.util.logging.ConsoleHandler
import java.util.logging.Logger

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

object LoggerConfigurator {

    private val globalLogger = Logger.getGlobal()

    val logger: Logger = if (globalLogger.handlers[0].formatter != CustomLogger()) {
        setupLogger()
        globalLogger
    }
    else {
        globalLogger
    }

    private fun setupLogger() {
        val logger = Logger.getGlobal()
        val rootLogger = Logger.getLogger("")
        val handlers = rootLogger.handlers
        if (handlers[0] is ConsoleHandler) {
            rootLogger.removeHandler(handlers[0])
        }

        val handler = ConsoleHandler()
        val formatter = CustomLogger()
        handler.formatter = formatter
        logger.addHandler(handler)
    }
}