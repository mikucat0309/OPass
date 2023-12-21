package app.opass.ccip.source

import app.opass.ccip.model.Announcement
import app.opass.ccip.model.Attendee

interface CcipClient {
  suspend fun getStatus(token: String): Result<Attendee>

  suspend fun use(
      token: String,
      scenario: String,
  ): Result<Attendee>

  suspend fun getAnnouncements(token: String?): Result<List<Announcement>>
}
