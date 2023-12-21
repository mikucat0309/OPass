package app.opass.ccip.model

import app.opass.ccip.source.ccip.RemoteCcipClient
import kotlinx.coroutines.flow.MutableStateFlow
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

  val announcements = MutableStateFlow<List<Announcement>>(emptyList())
  val attendee = MutableStateFlow<Attendee?>(null)

  val errorMessage = MutableStateFlow("")

  suspend fun fetchAnnouncements(token: String? = null) {
    remote.getAnnouncements(token).onSuccess { v -> announcements.update { v } }
  }

  suspend fun fetchAttendee(token: String) {
    remote
        .getStatus(token)
        .onSuccess { v -> attendee.update { v } }
        .onFailure { t -> errorMessage.update { t.message ?: "Unknown Error" } }
  }
}
