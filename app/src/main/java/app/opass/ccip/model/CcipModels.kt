package app.opass.ccip.model

import app.opass.ccip.misc.I18nText
import java.net.URL
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject

data class Attendee(
    val eventId: String,
    val token: String,
    val publicToken: String?,
    val userId: String,
    val firstUse: Instant,
    val role: String,
    val scenarios: List<Scenario>,
    val attr: JsonElement = buildJsonObject {},
)

data class Scenario(
    val id: String,
    val order: Int,
    val name: I18nText,
    val availableTime: Instant,
    val expireTime: Instant,
    val countdown: DateTimePeriod,
    val usedTime: Instant? = null,
    val disabledReason: String? = null,
    val attr: JsonElement = buildJsonObject {},
)

data class Announcement(
    val datetime: Instant,
    val message: I18nText,
    val url: URL? = null,
)
