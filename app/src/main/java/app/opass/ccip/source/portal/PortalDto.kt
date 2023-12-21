package app.opass.ccip.source.portal

import app.opass.ccip.I18nText
import app.opass.ccip.model.Event
import app.opass.ccip.model.EventConfig
import app.opass.ccip.model.EventDate
import app.opass.ccip.model.EventFeature
import app.opass.ccip.model.ExternalUrlEventFeature
import app.opass.ccip.model.SimpleInternalUrlEventFeature
import app.opass.ccip.model.WebViewEventFeature
import app.opass.ccip.model.WifiEventFeature
import app.opass.ccip.parseISO8601Instant
import app.opass.ccip.source.Unpackable
import app.opass.ccip.view.destinations.EnterTokenViewDestination
import app.opass.ccip.view.destinations.HomeViewDestination
import java.net.URL
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val event_id: String,
    val display_name: Map<String, String>,
    val logo_url: String,
) : Unpackable<Event> {
  override fun unpack() =
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
) : Unpackable<EventConfig> {
  override fun unpack(): EventConfig {
    // compatibility
    val ccipDefaultUrl = features.first { it.feature == "fastpass" }.url!!
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
) : Unpackable<EventDate> {
  override fun unpack() =
      EventDate(
          parseISO8601Instant(start),
          parseISO8601Instant(end),
      )
}

@Serializable
data class EventFeatureDto(
    val feature: String,
    val display_text: Map<String, String>,
    val visible_roles: List<String>? = null,
    val wifi: List<EventFeatureWifiDto>? = null,
    val icon: String? = null,
    val url: String? = null,
) : Unpackable<EventFeature> {
  override fun unpack(): EventFeature {
    check()
    return when (feature) {
      "wifi" ->
          WifiEventFeature(
              feature,
              I18nText.parseLocale(display_text),
              wifi!!.associate { it.unpack() },
              visible_roles,
              icon?.let { URL(it) },
          )
      "fastpass",
      "announcement",
      "ticket",
      "schedule" ->
          SimpleInternalUrlEventFeature(
              feature,
              I18nText.parseLocale(display_text),
              url!!,
              when (feature) {
                "ticket" -> EnterTokenViewDestination
                else -> HomeViewDestination
              },
              visible_roles,
              icon?.let { URL(it) },
          )
      "telegram",
      "im", ->
          ExternalUrlEventFeature(
              feature,
              I18nText.parseLocale(display_text),
              url!!,
              visible_roles,
              icon?.let { URL(it) },
          )
      "puzzle",
      "webview",
      "venue",
      "sponsors",
      "staffs", ->
          WebViewEventFeature(
              feature,
              I18nText.parseLocale(display_text),
              url!!,
              visible_roles,
              icon?.let { URL(it) },
          )
      else -> throw IllegalArgumentException("unknown feature type")
    }
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
) : Unpackable<Pair<String, String>> {
  override fun unpack() = SSID to password
}
