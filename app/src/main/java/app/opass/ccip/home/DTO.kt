@file:UseSerializers(UrlSerializer::class)
@file:Suppress("PropertyName")

package app.opass.ccip.home

import app.opass.ccip.util.UrlSerializer
import app.opass.ccip.util.isChinese
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URL

@Serializable
data class EventDto(
    val event_id: EventId,
    val display_name: I18n,
    val logo_url: URL,
) {
    @Serializable
    data class I18n(
        val zh: String,
        val en: String,
    )

    fun toEvent(): Event {
        return Event(
            id = event_id,
            name = if (isChinese()) display_name.zh else display_name.en,
            logoUrl = logo_url
        )
    }
}

@Serializable
data class EventDetailDto(
    val event_id: String,
    val display_name: I18n,
    val logo_url: URL,
    val event_website: URL,
    val event_date: TimeRange,
    val publish: TimeRange,
    val features: List<FeatureDto>,
) {
    @Serializable
    data class I18n(
        val zh: String,
        val en: String,
    )

    @Serializable
    data class TimeRange(
        val start: String,
        val end: String,
    )

    fun toEventDetail(): EventDetail {
        return EventDetail(
            id = event_id,
            name = if (isChinese()) display_name.zh else display_name.en,
            logoUrl = logo_url,
            website = event_website,
            start = event_date.start,
            end = event_date.end,
            startPublish = publish.start,
            endPublish = publish.end,
            features = features.map { it.toFeature() }
        )
    }
}

@Serializable
data class FeatureDto(
    val feature: FeatureType,
    val display_text: I18n,
    val icon: String? = null,
    val url: String? = null,
    val visible_roles: List<String>? = null,
    val wifi: List<WifiInfoDto>? = null
) {
    @Serializable
    data class I18n(
        val zh: String,
        val en: String,
    )

    fun toFeature(): Feature {
        val label = if (isChinese()) display_text.zh else display_text.en
        return when (feature) {
            FeatureType.FAST_PASS -> FastPassFeature(label)
            FeatureType.SCHEDULE -> ScheduleFeature(label, URL(url))
            FeatureType.ANNOUNCEMENT -> AnnouncementFeature(label)
            FeatureType.PUZZLE -> PuzzleFeature(label)
            FeatureType.TICKET -> TicketFeature(label)
            FeatureType.TELEGRAM -> TelegramFeature(label)
            FeatureType.IM -> ImFeature(label)
            FeatureType.SPONSORS -> SponsorsFeature(label)
            FeatureType.STAFFS -> StaffsFeature(label)
            FeatureType.VENUE -> VenueFeature(label)
            FeatureType.WEBVIEW -> WebViewFeature(label)
            FeatureType.WIFI -> WifiFeature(label)
        }
    }
}

@Serializable
data class WifiInfoDto(
    val SSID: String,
    val password: String? = null
)

@Serializable
enum class FeatureType(val type: String) {
    @SerialName("fastpass")
    FAST_PASS("fastpass"),

    @SerialName("schedule")
    SCHEDULE("schedule"),

    @SerialName("announcement")
    ANNOUNCEMENT("announcement"),

    @SerialName("puzzle")
    PUZZLE("puzzle"),

    @SerialName("ticket")
    TICKET("ticket"),

    @SerialName("telegram")
    TELEGRAM("telegram"),

    @SerialName("im")
    IM("im"),

    @SerialName("sponsors")
    SPONSORS("sponsors"),

    @SerialName("staffs")
    STAFFS("staffs"),

    @SerialName("venue")
    VENUE("venue"),

    @SerialName("webview")
    WEBVIEW("webview"),

    @SerialName("wifi")
    WIFI("wifi");
}
