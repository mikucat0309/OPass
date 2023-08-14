package app.opass.ccip.setting

import app.opass.ccip.home.EventDetail
import app.opass.ccip.home.EventId
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val eventDetail: EventDetail = EventDetail.empty,
    val tokens: Map<EventId, String> = emptyMap(),
    val isDarkTheme: Boolean = false
)
