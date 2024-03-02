package app.opass.ccip.source.portal

import app.opass.ccip.compose.R
import app.opass.ccip.misc.I18nText
import app.opass.ccip.misc.parseISO8601Instant
import app.opass.ccip.model.DateTimeRange
import app.opass.ccip.model.Event
import app.opass.ccip.model.EventConfig
import app.opass.ccip.model.EventFeature
import app.opass.ccip.model.ExternalUrlEventFeature
import app.opass.ccip.model.InternalUrlEventFeature
import app.opass.ccip.model.WifiEventFeature
import app.opass.ccip.view.destinations.HomeViewDestination
import app.opass.ccip.view.destinations.ScheduleViewDestination
import app.opass.ccip.view.destinations.TicketViewDestination
import java.net.URL
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val event_id: String,
    val display_name: Map<String, String>,
    val logo_url: String,
) {
  fun unpack() =
      Event(
          event_id,
          I18nText.parseLocale(display_name),
          URL(logo_url),
      )
}

@Serializable
data class EventConfigDto(
    val event_id: String,
    val display_name: Map<String, String>,
    val logo_url: String,
    val event_website: String,
    val event_date: EventDateDto,
    val publish: EventDateDto,
    val features: List<EventFeatureDto>,
) {
  fun unpack(): EventConfig {
    // compatibility
    val ccipDefaultUrl = features.firstOrNull { it.feature == "fastpass" }?.url
    val features =
        features.map { f ->
          if (f.url == null && (f.feature == "ticket" || f.feature == "announcement")) {
            f.copy(url = ccipDefaultUrl)
          } else {
            f
          }
        }

    return EventConfig(
        event_id,
        I18nText.parseLocale(display_name),
        URL(logo_url),
        URL(event_website),
        event_date.unpack(),
        publish.unpack(),
        features.map { it.unpack() },
    )
  }
}

@Serializable
data class EventDateDto(
    val start: String,
    val end: String,
) {
  fun unpack() =
      DateTimeRange(
          parseISO8601Instant(start),
          parseISO8601Instant(end),
      )
}

@Serializable
data class EventFeatureDto(
    val feature: String,
    val display_text: Map<String, String>,
    val url: String? = null,
    val icon: String? = null,
    val wifi: List<EventFeatureWifiDto>? = null,
    val visible_roles: List<String>? = null,
) {
  fun unpack(): EventFeature {
    check()
    val defaultIcon = getDefaultIcon()
    val isRestricted = feature in listOf("fastpass", "ticket", "puzzle")
    return when (feature) {
      "wifi" ->
          WifiEventFeature(
              feature,
              I18nText.parseLocale(display_text),
              isRestricted,
              wifi!!.associate { it.unpack() },
              defaultIcon,
              icon,
          )
      "fastpass",
      "announcement",
      "ticket",
      "schedule" ->
          InternalUrlEventFeature(
              feature,
              I18nText.parseLocale(display_text),
              isRestricted,
              url!!,
              defaultIcon,
              when (feature) {
                "ticket" -> TicketViewDestination
                "schedule" -> ScheduleViewDestination
                else -> HomeViewDestination
              },
              icon,
          )
      "telegram",
      "im", ->
          ExternalUrlEventFeature(
              feature,
              I18nText.parseLocale(display_text),
              isRestricted,
              url!!,
              defaultIcon,
              icon,
          )
      "puzzle",
      "webview",
      "venue",
      "sponsors",
      "staffs", ->
          ExternalUrlEventFeature(
              feature,
              I18nText.parseLocale(display_text),
              isRestricted,
              url!!,
              defaultIcon,
              icon,
          )
      else -> throw IllegalArgumentException("unknown feature type")
    }
  }

  private fun getDefaultIcon() =
      when (feature) {
        "wifi" -> R.drawable.wifi_36
        "fastpass" -> R.drawable.badge_36
        "announcement" -> R.drawable.campaign_36
        "ticket" -> R.drawable.local_activity_36
        "schedule" -> R.drawable.history_edu_36
        "telegram" -> R.drawable.telegram_36
        "im" -> R.drawable.message_36
        "puzzle" -> R.drawable.extension_36
        "webview" -> R.drawable.public_36
        "venue" -> R.drawable.map_36
        "sponsors" -> R.drawable.handshake_36
        "staffs" -> R.drawable.people_36
        else -> null
      }

  private fun check() {
    when (feature) {
      "fastpass",
      "announcement",
      "ticket",
      "schedule",
      "telegram",
      "im",
      "puzzle",
      "webview",
      "venue",
      "sponsors",
      "staffs", -> checkNotNull(url)
      "wifi" -> checkNotNull(wifi)
    }
  }
}

@Serializable
class EventFeatureWifiDto(
    val SSID: String,
    val password: String,
) {
  fun unpack() = SSID to password
}
