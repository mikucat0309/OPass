@file:UseSerializers(UrlSerializer::class)

package app.opass.ccip.home

import app.opass.ccip.i18n.LocalizedString
import app.opass.ccip.misc.UrlSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URL

typealias EventId = String

data class Event(
    val id: EventId,
    val name: LocalizedString,
    val logoUrl: URL
)

@Serializable
data class EventDetail(
    val id: EventId,
    val name: LocalizedString,
    val logoUrl: URL?,
    val website: URL?,
    val start: String,
    val end: String,
    val startPublish: String,
    val endPublish: String,
    val features: List<Feature>
) {
    companion object {
        val empty = EventDetail(
            "",
            LocalizedString.empty,
            null,
            null,
            "",
            "",
            "",
            "",
            emptyList()
        )
    }
}
