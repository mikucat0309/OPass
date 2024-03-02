package app.opass.ccip.model

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import app.opass.ccip.misc.I18nText
import app.opass.ccip.view.destinations.EnterTokenViewDestination
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
  val isRestricted: Boolean
  @get:DrawableRes val icon: Int?
  val iconUrl: String?
}

interface UrlEventFeature {
  val url: String
}

data class InternalUrlEventFeature(
    override val type: String,
    override val name: I18nText,
    override val isRestricted: Boolean,
    override val url: String,
    @get:DrawableRes override val icon: Int?,
    val destination: DirectionDestinationSpec,
    override val iconUrl: String? = null,
) : EventFeature, UrlEventFeature, KoinComponent {

  fun onClick(navigator: DestinationsNavigator, isGuest: Boolean) {
    navigator.navigate(if (isRestricted && isGuest) EnterTokenViewDestination else destination)
  }
}

data class ExternalUrlEventFeature(
    override val type: String,
    override val name: I18nText,
    override val isRestricted: Boolean,
    override val url: String,
    @get:DrawableRes override val icon: Int?,
    override val iconUrl: String? = null,
) : EventFeature, UrlEventFeature, KoinComponent {

  fun onClick(navigator: DestinationsNavigator, isGuest: Boolean, activity: Activity) {
    if (isRestricted && isGuest) {
      navigator.navigate(EnterTokenViewDestination)
    } else {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      activity.startActivity(intent)
    }
  }
}

data class WifiEventFeature(
    override val type: String,
    override val name: I18nText,
    override val isRestricted: Boolean,
    val wifi: Map<String, String>,
    @get:DrawableRes override val icon: Int?,
    override val iconUrl: String? = null,
) : EventFeature
