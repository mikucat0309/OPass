package app.opass.ccip.model

import app.opass.ccip.misc.TimeZonedInstant

data class DateTimeRange(
    val start: TimeZonedInstant,
    val end: TimeZonedInstant,
)
