package app.opass.ccip

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

data class TimeZonedInstant(
    val dateTime: LocalDateTime,
    val timeZone: TimeZone,
): Comparable<TimeZonedInstant> {
  val instant: Instant
    get() = dateTime.toInstant(timeZone)

  override fun compareTo(other: TimeZonedInstant): Int {
    return instant.compareTo(other.instant)
  }
}
