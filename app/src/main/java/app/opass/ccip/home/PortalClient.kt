package app.opass.ccip.home

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PortalClient : KoinComponent {

    private val client: HttpClient by inject()

    suspend fun getEvents(): List<EventDto> {
        return client.get("$baseUrl/events/").body()
    }

    suspend fun getEvent(event: Event): EventDetail {
        return client.get("$baseUrl/events/${event.id}").body<EventDetailDto>().toEventDetail()
    }

    companion object {
        const val baseUrl = "https://portal.opass.app"
    }
}
