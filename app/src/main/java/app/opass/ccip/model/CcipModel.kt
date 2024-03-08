package app.opass.ccip.model

import app.opass.ccip.source.ccip.RemoteCcipClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

class CcipModel(
    private val remote: RemoteCcipClient,
) : KoinComponent {
  var baseUrl: String
    get() = remote.baseUrl
    set(value) {
      remote.baseUrl = value
    }

  private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
  val announcements: StateFlow<List<Announcement>> = _announcements
  private val _attendee = MutableStateFlow<Attendee?>(null)
  val attendee: StateFlow<Attendee?> = _attendee

  val errorMessage = MutableStateFlow("")

  suspend fun fetchAnnouncements(token: String? = null) {
    remote.getAnnouncements(token).onSuccess { v -> _announcements.update { v } }
  }

  suspend fun fetchAttendee(token: String) {
    remote
        .getStatus(token)
        .onSuccess { v -> _attendee.update { v } }
        .onFailure { t -> errorMessage.update { t.message ?: "Unknown error" } }
  }

  fun replaceTemplate(template: String): String {
    var result = template
    val currentAttendee = _attendee.value
    if (currentAttendee != null) {
      result = result.replace("{token}", currentAttendee.token)
      // TODO support more pattern
    }
    return result
  }
}
