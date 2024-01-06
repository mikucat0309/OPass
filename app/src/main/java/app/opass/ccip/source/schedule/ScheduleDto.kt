package app.opass.ccip.source.schedule

import app.opass.ccip.I18nText
import app.opass.ccip.model.DateTimeRange
import app.opass.ccip.model.Room
import app.opass.ccip.model.ScheduleObject
import app.opass.ccip.model.Session
import app.opass.ccip.model.SessionType
import app.opass.ccip.model.Speaker
import app.opass.ccip.model.Tag
import app.opass.ccip.parseISO8601Instant
import app.opass.ccip.suppress
import java.net.URL
import java.util.Locale
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDto(
    val sessions: List<SessionDto>,
    val speakers: List<SpeakerDto>,
    val session_types: List<ScheduleObjectDto>,
    val rooms: List<ScheduleObjectDto>,
    val tags: List<ScheduleObjectDto>,
) {
  fun unpack(timeZone: TimeZone): List<Session> {
    val speakers2 = speakers.map { it.unpack() }
    val sessionTypes2 = session_types.map { it.unpack() }
    val rooms2 = rooms.map { it.unpack() }
    val tags2 = tags.map { it.unpack() }
    return sessions.map { s ->
      s.unpack(
          speakers2.associateBy { it.id },
          sessionTypes2.associateBy { it.id },
          rooms2.associateBy { it.id },
          tags2.associateBy { it.id },
          timeZone,
      )
    }
  }
}

@Serializable
data class SessionDto(
    val id: String,
    val type: String,
    val room: String,
    val broadcast: List<String>? = null,
    val start: String,
    val end: String,
    val qa: String? = null,
    val slide: String? = null,
    val co_write: String? = null,
    val live: String? = null,
    val record: String? = null,
    val language: String? = null,
    val uri: String? = null,
    val zh: TitleDescDto,
    val en: TitleDescDto,
    val speakers: List<String>,
    val tags: List<String>,
) {
  fun unpack(
      speakers: Map<String, Speaker>,
      sessionTypes: Map<String, SessionType>,
      rooms: Map<String, Room>,
      tags: Map<String, Tag>,
      timeZone: TimeZone,
  ) =
      Session(
          id,
          sessionTypes[type]!!,
          rooms[room]!!,
          DateTimeRange(
              parseISO8601Instant(start, timeZone),
              parseISO8601Instant(end, timeZone),
          ),
          I18nText(
              Locale.ENGLISH to en.title,
              Locale.CHINESE to zh.title,
          ),
          I18nText(
              Locale.ENGLISH to en.description,
              Locale.CHINESE to zh.description,
          ),
          this.speakers.map { speakers[it]!! },
          this.tags.map { tags[it]!! },
          broadcast?.map { rooms[it]!! },
          uri?.let { suppress { URL(it) } },
          qa?.let { suppress { URL(it) } },
          slide?.let { suppress { URL(it) } },
          co_write?.let { suppress { URL(it) } },
          live?.let { suppress { URL(it) } },
          record?.let { suppress { URL(it) } },
          language,
      )
}

@Serializable
data class TitleDescDto(
    val title: String,
    val description: String,
)

@Serializable
data class SpeakerDto(
    val id: String,
    val avatar: String,
    val zh: NameBioDto,
    val en: NameBioDto,
) {
  fun unpack() =
      Speaker(
          id,
          URL(avatar),
          I18nText(
              Locale.ENGLISH to en.name,
              Locale.ENGLISH to zh.name,
          ),
          I18nText(
              Locale.ENGLISH to en.bio,
              Locale.ENGLISH to zh.bio,
          ),
      )
}

@Serializable
data class NameBioDto(
    val name: String,
    val bio: String,
)

@Serializable
data class ScheduleObjectDto(
    val id: String,
    val zh: NameDescDto,
    val en: NameDescDto,
) {
  fun unpack() =
      ScheduleObject(
          id,
          I18nText(
              Locale.ENGLISH to en.name,
              Locale.ENGLISH to zh.name,
          ),
          I18nText(
              Locale.ENGLISH to en.description,
              Locale.ENGLISH to zh.description,
          ),
      )
}

@Serializable
data class NameDescDto(
    val name: String,
    val description: String = "",
)
