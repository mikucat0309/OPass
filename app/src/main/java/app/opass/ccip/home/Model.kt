package app.opass.ccip.home

import androidx.annotation.DrawableRes
import app.opass.ccip.compose.R
import app.opass.ccip.destinations.ScheduleViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.net.URL

typealias EventId = String

data class Event(
    val id: EventId,
    val name: String,
    val logoUrl: URL,
)

data class EventDetail(
    val id: EventId,
    val name: String,
    val logoUrl: URL,
    val website: URL,
    val start: String,
    val end: String,
    val startPublish: String,
    val endPublish: String,
    val features: List<Feature>,
)

sealed class Feature(
    val label: String,
    @DrawableRes val res: Int,
    val onClick: (DestinationsNavigator) -> Unit,
)

class FastPassFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class ScheduleFeature(label: String, scheduleUrl: URL) : Feature(label, R.drawable.ic_schedule, {
    val args = ScheduleViewDestination.NavArgs(scheduleUrl.toString())
    it.navigate(ScheduleViewDestination(args))
})

class AnnouncementFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class PuzzleFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class TicketFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class TelegramFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class ImFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class SponsorsFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class StaffsFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class VenueFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class WebViewFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})

class WifiFeature(label: String) : Feature(label, R.drawable.ic_schedule, {

})
