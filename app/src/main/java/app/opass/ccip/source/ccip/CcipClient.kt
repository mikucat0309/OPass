package app.opass.ccip.source.ccip

import app.opass.ccip.model.Announcement
import app.opass.ccip.model.Attendee
import app.opass.ccip.model.MainModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

class CcipClient(
    private val model: MainModel,
    private val httpClient: HttpClient,
) : KoinComponent {

  suspend fun updateAnnouncements(): Result<List<Announcement>> {
    return fetchAnnouncements(model.attendee.value?.token)
  }

  private suspend fun fetchAnnouncements(token: String?): Result<List<Announcement>> {
    return try {
      val resp =
          httpClient.get("${model.ccipBaseUrl}/announcement") {
            token?.let {
              parameter(
                  "token",
                  it,
              )
            }
          }
      Result.success(resp.body<List<AnnouncementDto>>().map { it.unpack() })
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun updateAttendee(token: String): Result<Attendee> {
    return fetchStatus(token).onSuccess { v -> model.attendee.update { v } }
  }

  private suspend fun fetchStatus(token: String): Result<Attendee> {
    return try {
      val resp = httpClient.get("${model.ccipBaseUrl}/status") { parameter("token", token) }
      Result.success(resp.body<AttendeeDto>().unpack(token))
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
