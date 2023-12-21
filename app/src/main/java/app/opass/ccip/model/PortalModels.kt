package app.opass.ccip.model

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import app.opass.ccip.I18nText
import app.opass.ccip.compose.R
import app.opass.ccip.view.destinations.DirectionDestination
import app.opass.ccip.view.destinations.HomeViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import java.net.URL
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

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
    val eventDate: EventDate,
    val publishDate: EventDate,
    val features: List<EventFeature>,
)

data class EventDate(
    val start: Instant,
    val end: Instant,
)

sealed interface EventFeature {
  val type: String
  val name: I18nText
  val visibleRoles: List<String>?
  val iconUrl: URL?

  @get:DrawableRes val icon: Int

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
    override val visibleRoles: List<String>? = null,
    override val iconUrl: URL? = null,
) : InternalUrlEventFeature, KoinComponent {
  override val icon: Int = R.drawable.badge_36

  override fun onClick(activity: Activity, navigator: DestinationsNavigator) {
    navigator.navigate(destination)
  }
}

data class ExternalUrlEventFeature(
    override val type: String,
    override val name: I18nText,
    val url: String,
    override val visibleRoles: List<String>? = null,
    override val iconUrl: URL? = null,
) : EventFeature, KoinComponent {
  override val icon: Int = R.drawable.badge_36

  override fun onClick(activity: Activity, navigator: DestinationsNavigator) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    activity.startActivity(intent)
  }
}

data class WebViewEventFeature(
    override val type: String,
    override val name: I18nText,
    override val url: String,
    override val visibleRoles: List<String>? = null,
    override val iconUrl: URL? = null,
    override val icon: Int = R.drawable.badge_36,
) : InternalUrlEventFeature {
  override val destination: DirectionDestination = HomeViewDestination
}

data class WifiEventFeature(
    override val type: String,
    override val name: I18nText,
    val wifi: Map<String, String>,
    override val visibleRoles: List<String>? = null,
    override val iconUrl: URL? = null,
    override val icon: Int = R.drawable.wifi_36,
) : EventFeature
