package org.korsnaike.logger

import org.korsnaike.logger.outputmethod.ConsoleLog
import org.korsnaike.logger.outputmethod.LogOutputMethod
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class SimpleLogger() {
    companion object {

        var level: LogLevel = LogLevel.INFO
        var outputMethod: LogOutputMethod = ConsoleLog()
        var messageFormat = "[%datetime] %level: %message"

        @JvmStatic
        fun setLoggerSettings(
            level: LogLevel = LogLevel.INFO,
            outputMethod: LogOutputMethod = ConsoleLog(),
            messageFormat: String = "[%datetime] %level: %message"
        ) {
            this.outputMethod = outputMethod
            this.level = level
            this.messageFormat = messageFormat
        }

        @JvmStatic
        fun info(message: String) {
            if (level == LogLevel.INFO) {
                outputMethod.log(formatMessage(message, "INFO"))
            }
        }

        @JvmStatic
        fun error(message: String) {
            if (level == LogLevel.ERROR || level == LogLevel.INFO) {
                outputMethod.log(formatMessage(message, "ERROR"))
            }
        }

        private fun formatMessage(message: String, messageLevel: String): String {
            val datetime = LocalDateTime.now()
            val formattedDateTime = datetime.format(
                DateTimeFormatter.ISO_DATE_TIME
            )
            return messageFormat
                .replace("%datetime", formattedDateTime)
                .replace("%level", messageLevel)
                .replace("%message", message)
        }

    }

}