package app.opass.ccip

import androidx.core.os.LocaleListCompat
import java.util.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant

fun parseISO8601Instant(raw: String): Instant {
  val list = raw.split('T', limit = 2)
  val date = LocalDate.parse(list[0])
  var time = LocalTime(0, 0, 0)
  var tz = TimeZone.UTC

  if (list.size == 2) {
    val result = Regex("""(\d\d(?::\d\d){0,2})([+-]\d\d(?::\d\d)?)""").find(list[1])
    if (result != null) {
      println(result.groupValues)
      time = LocalTime.parse(result.groupValues[1])
      tz = UtcOffset.parse(result.groupValues[2]).asTimeZone()
    } else {
      time = LocalTime.parse(list[1])
    }
  }
  return date.atTime(time).toInstant(tz)
}

fun LocaleListCompat.toList(): List<Locale> {
  val list: MutableList<Locale> = mutableListOf()
  for (i in 0 until size()) {
    list.add(get(i)!!)
  }
  return list.toList()
}
