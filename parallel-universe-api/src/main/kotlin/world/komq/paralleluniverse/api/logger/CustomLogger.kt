/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.logger

import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Formatter
import java.util.logging.LogRecord

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

class CustomLogger : Formatter() {
    override fun format(rec: LogRecord): String {
        val buf = StringBuffer(1000)
        buf.append("[")
        buf.append(calcDate(rec.millis))
        buf.append("] ")
        buf.append("[")
        buf.append(rec.level)
        buf.append("]")
        buf.append(" ${rec.message}")
        buf.append("\n")
        return buf.toString()
    }

    private fun calcDate(millisecs: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val resultdate = Date(millisecs)
        return dateFormat.format(resultdate)
    }
}