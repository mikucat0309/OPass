@file:UseSerializers(UrlSerializer::class)

package app.opass.ccip.home

import androidx.annotation.DrawableRes
import app.opass.ccip.compose.R
import app.opass.ccip.destinations.ScheduleViewDestination
import app.opass.ccip.i18n.LocalizedString
import app.opass.ccip.misc.UrlSerializer
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URL

@Serializable
sealed class Feature {
    abstract val label: LocalizedString

    @get:DrawableRes
    abstract val res: Int
    open fun onClick(nav: DestinationsNavigator) {}
}

@Serializable
class FastPassFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class ScheduleFeature(
    override val label: LocalizedString,
    private val scheduleUrl: URL,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature() {
    override fun onClick(nav: DestinationsNavigator) {
        val args = ScheduleViewDestination.NavArgs(scheduleUrl)
        nav.navigate(ScheduleViewDestination(args))
    }
}

@Serializable
class AnnouncementFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class PuzzleFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class TicketFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class TelegramFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class ImFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class SponsorsFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class StaffsFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class VenueFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class WebViewFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()

@Serializable
class WifiFeature(
    override val label: LocalizedString,
    @DrawableRes override val res: Int = R.drawable.ic_schedule
) : Feature()
