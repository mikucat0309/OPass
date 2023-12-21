package app.opass.ccip.source

import app.opass.ccip.model.Event
import app.opass.ccip.model.EventConfig

interface PortalClient {
  suspend fun getEvents(): List<Event>

  suspend fun getEventConfig(eventId: String): EventConfig
}
