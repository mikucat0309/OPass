package app.opass.ccip.model

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import app.opass.ccip.I18nText
import app.opass.ccip.view.destinations.DirectionDestination
import app.opass.ccip.view.destinations.HomeViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import java.net.URL
import org.koin.core.component.KoinComponent

data class Event(
    val id: String,
    val name: I18nText,
    val logoUrl: URL,
)

data class EventConfig(
    val id: String,
    val name: I18nText,
    val logoUrl: URL,
    val websiteUrl: URL,
    val eventDateTimeRange: DateTimeRange,
    val publishDateTimeRange: DateTimeRange,
    val features: List<EventFeature>,
)

sealed interface EventFeature {
  val type: String
  val name: I18nText
  val visibleRoles: List<String>?
  val iconUrl: String?

  @get:DrawableRes val icon: Int?

  fun onClick(activity: Activity, navigator: DestinationsNavigator) {}
}

sealed interface InternalUrlEventFeature : EventFeature {
  val url: String
  val destination: DirectionDestinationSpec
}

data class SimpleInternalUrlEventFeature(
  override val type: String,
  override val name: I18nText,
  override val url: String,
  override val destination: DirectionDestinationSpec,
  @get:DrawableRes override val icon: Int?,
  override val visibleRoles: List<String>? = null,
  override val iconUrl: String? = null,
) : InternalUrlEventFeature, KoinComponent {

  override fun onClick(activity: Activity, navigator: DestinationsNavigator) {
    navigator.navigate(destination)
  }
}

data class ExternalUrlEventFeature(
  override val type: String,
  override val name: I18nText,
  val url: String,
  @get:DrawableRes override val icon: Int?,
  override val visibleRoles: List<String>? = null,
  override val iconUrl: String? = null,
) : EventFeature, KoinComponent {

  override fun onClick(activity: Activity, navigator: DestinationsNavigator) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    activity.startActivity(intent)
  }
}

data class WebViewEventFeature(
  override val type: String,
  override val name: I18nText,
  override val url: String,
  @get:DrawableRes override val icon: Int?,
  override val visibleRoles: List<String>? = null,
  override val iconUrl: String? = null,
) : InternalUrlEventFeature {
  override val destination: DirectionDestination = HomeViewDestination
}

data class WifiEventFeature(
  override val type: String,
  override val name: I18nText,
  val wifi: Map<String, String>,
  @get:DrawableRes override val icon: Int?,
  override val visibleRoles: List<String>? = null,
  override val iconUrl: String? = null,
) : EventFeature
