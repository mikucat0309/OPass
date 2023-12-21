package app.opass.ccip.source.portal

import app.opass.ccip.model.Event
import app.opass.ccip.model.EventConfig
import app.opass.ccip.source.PortalClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent

class RemotePortalClient(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : PortalClient, KoinComponent {
  override suspend fun getEvents(): List<Event> =
      httpClient.get("$baseUrl/events/").body<List<EventDto>>().map { it.unpack() }

  override suspend fun getEventConfig(eventId: String): EventConfig =
      httpClient.get("$baseUrl/events/$eventId/").body<EventConfigDto>().unpack()
}
