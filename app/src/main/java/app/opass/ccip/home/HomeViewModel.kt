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

    private var _selectedEvent: Event? by mutableStateOf(null)
    var selectedEvent: Event?
        get() = _selectedEvent
        set(value) {
            _selectedEvent = value
            updateEventDetail()
        }
    var eventDetail: EventDetail? by mutableStateOf(null)

    init {
        println("HomeViewModel init")
        updateEvents()
    }

    fun updateEvents(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            events = client.getEvents().map { it.toEvent() }
        }
    }

    fun updateEventDetail(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            eventDetail = client.getEvent(selectedEvent!!)
            println("event detail name")
            println(eventDetail?.name ?: "NULL")
        }
    }
}
