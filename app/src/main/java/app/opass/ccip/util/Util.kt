package app.opass.ccip.util

import app.opass.ccip.compose.R
import kotlinx.datetime.LocalTime
import java.time.DayOfWeek
import java.util.Locale


val dayOfWeekLabel = mapOf(
    DayOfWeek.MONDAY to R.string.dayOfWeek_mon,
    DayOfWeek.TUESDAY to R.string.dayOfWeek_tue,
    DayOfWeek.WEDNESDAY to R.string.dayOfWeek_wed,
    DayOfWeek.THURSDAY to R.string.dayOfWeek_thu,
    DayOfWeek.FRIDAY to R.string.dayOfWeek_fri,
    DayOfWeek.SATURDAY to R.string.dayOfWeek_sat,
    DayOfWeek.SUNDAY to R.string.dayOfWeek_sun,
)

fun isChinese() = Locale.getDefault().language == "zh"

fun LocalTime.format1(): String = "$hour:${minute.toString().padStart(2, '0')}"