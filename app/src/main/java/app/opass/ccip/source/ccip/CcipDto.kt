package app.opass.ccip.source.ccip

import app.opass.ccip.I18nText
import app.opass.ccip.model.Announcement
import app.opass.ccip.model.Attendee
import app.opass.ccip.model.Scenario
import app.opass.ccip.source.Unpackable
import java.net.URL
import java.util.Locale
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject

@Serializable
data class AnnouncementDto(
    val datetime: Long,
    val msg_en: String,
    val msg_zh: String,
    val uri: String,
) : Unpackable<Announcement> {
  override fun unpack() =
      Announcement(
          Instant.fromEpochSeconds(datetime),
          I18nText(
              Locale.ENGLISH to msg_en,
              Locale.CHINESE to msg_zh,
          ),
          URL(uri),
      )
}

@Serializable
data class AttendeeDto(
    var event_id: String,
    var token: String,
    var user_id: String,
    var first_use: Long,
    var role: String,
    var scenarios: List<ScenarioDto>,
    var attr: JsonElement = buildJsonObject {},
) : Unpackable<Attendee> {
  override fun unpack() =
      Attendee(
          event_id,
          token,
          user_id,
          Instant.fromEpochSeconds(first_use),
          role,
          scenarios.map { it.unpack() },
          attr,
      )
}

@Serializable
data class ScenarioDto(
    val id: String,
    val order: Int,
    val display_text: Map<String, String>,
    val available_time: Long,
    val expire_time: Long,
    val countdown: Int,
    val used: Long? = null,
    val disabled: String? = null,
    val attr: JsonElement = buildJsonObject {},
) : Unpackable<Scenario> {
  override fun unpack() =
      Scenario(
          id,
          order,
          I18nText.parseLocale(display_text),
          Instant.fromEpochSeconds(available_time),
          Instant.fromEpochSeconds(expire_time),
          DateTimePeriod(seconds = countdown),
          used?.let { Instant.fromEpochSeconds(it) },
          disabled,
          attr,
      )
}
