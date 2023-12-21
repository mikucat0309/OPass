package app.opass.ccip.model

import app.opass.ccip.source.portal.RemotePortalClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

class PortalModel(
    private val remote: RemotePortalClient,
) : KoinComponent {
  private val _events = MutableStateFlow<List<Event>>(emptyList())
  val events: StateFlow<List<Event>> = _events
  private val _eventConfig = MutableStateFlow<EventConfig?>(null)
  val eventConfig: StateFlow<EventConfig?> = _eventConfig

  suspend fun fetchEvents() {
    _events.update { remote.getEvents() }
  }

  suspend fun fetchEventConfig(eventId: String) {
    _eventConfig.update { remote.getEventConfig(eventId) }
  }
}
