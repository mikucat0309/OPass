package app.opass.ccip.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.model.Event
import app.opass.ccip.source.portal.PortalClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SwitchEventViewModel : ViewModel(), KoinComponent {
  private val portalClient: PortalClient by inject()
  private val dispatcher: CoroutineDispatcher by inject()
  var events by mutableStateOf(emptyList<Event>())
  var eventState by mutableStateOf(EventState.INIT)

  fun fetchEvents() {
    viewModelScope.launch(dispatcher) { portalClient.updateEvents().onSuccess { events = it } }
  }

  fun fetchEventConfig(eventId: String) {
    viewModelScope.launch(dispatcher) {
      eventState = EventState.PROCESSING
      portalClient.updateEventConfig(eventId)
      eventState = EventState.DONE
    }
  }
}
