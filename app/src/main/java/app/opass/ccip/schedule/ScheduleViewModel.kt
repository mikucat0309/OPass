package app.opass.ccip.schedule

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.net.URL

class ScheduleViewModel(
    private val client: HttpClient
) : ViewModel() {
    var dates: List<LocalDate> by mutableStateOf(listOf())
    var selectedDate: MutableState<LocalDate?> = mutableStateOf(null)
    var sessions: Map<LocalDate, List<Session>> by mutableStateOf(emptyMap())
        private set
    var speakers: List<Speaker> by mutableStateOf(emptyList())
        private set
    var sessionTypes: List<SessionType> by mutableStateOf(emptyList())
        private set
    var rooms: List<Room> by mutableStateOf(emptyList())
        private set
    var tags: List<SessionTag> by mutableStateOf(emptyList())
        private set

    fun update(url: URL, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val schedule = client.get(url).body<ScheduleDto>().toSchedule()
            sessions = schedule.sessions.sortedBy { it.start }.groupBy { it.start.date }
            dates = sessions.keys.sorted()
            selectedDate.value = dates.firstOrNull()
            speakers = schedule.speakers
            sessionTypes = schedule.sessionTypes
            rooms = schedule.rooms
            tags = schedule.tags
        }
    }
}
