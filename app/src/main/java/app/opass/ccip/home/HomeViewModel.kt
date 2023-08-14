package app.opass.ccip.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val client: PortalClient
) : ViewModel() {
    var events: List<Event> by mutableStateOf(emptyList())
    var eventDetail: EventDetail by mutableStateOf(EventDetail.empty)

    init {
        loadRemoteEvents()
    }

    fun loadRemoteEvents(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            events = client.getEvents().map { it.toEvent() }
        }
    }

    fun loadRemoteEventDetail(
        event: EventId,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        viewModelScope.launch(dispatcher) {
            eventDetail = client.getEventDetail(event)
        }
    }
}
