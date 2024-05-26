package app.opass.ccip.model

import app.opass.ccip.misc.I18nText
import java.net.URL

data class Session(
    val id: String,
    val type: SessionType,
    val room: Room,
    val dateTime: DateTimeRange,
    val title: I18nText,
    val description: I18nText,
    val speakers: List<Speaker>,
    val tags: List<Tag> = emptyList(),
    val broadcast: List<Room>? = null,
    val url: URL? = null,
    val qa: URL? = null,
    val slide: URL? = null,
    val coWrite: URL? = null,
    val live: URL? = null,
    val record: URL? = null,
    val language: String? = null,
)

data class Speaker(
    val id: String,
    val avatar: URL,
    val name: I18nText,
    val bio: I18nText,
)

typealias SessionType = ScheduleObject

typealias Room = ScheduleObject

typealias Tag = ScheduleObject

data class ScheduleObject(
    val id: String,
    val name: I18nText,
    val description: I18nText,
)
