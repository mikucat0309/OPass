package app.opass.ccip.source.ccip

import app.opass.ccip.model.Announcement
import app.opass.ccip.model.Attendee
import app.opass.ccip.source.CcipClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import org.koin.core.component.KoinComponent

class RemoteCcipClient(
    private val httpClient: HttpClient,
) : CcipClient, KoinComponent {
  lateinit var baseUrl: String

  override suspend fun getStatus(token: String): Result<Attendee> {
    val resp = httpClient.get("$baseUrl/status") { parameter("token", token) }
    return when (resp.status) {
      HttpStatusCode.OK -> Result.success(resp.body<AttendeeDto>().unpack(token))
      HttpStatusCode.BadRequest -> Result.failure(IllegalStateException("Invalid token"))
      else -> Result.failure(IllegalStateException("Unknown error"))
    }
  }

  override suspend fun use(
      token: String,
      scenario: String,
  ): Result<Attendee> {
    val resp = httpClient.get("$baseUrl/use/$scenario") { parameter("token", token) }
    return when (resp.status) {
      HttpStatusCode.OK -> Result.success(resp.body<AttendeeDto>().unpack(token))
      HttpStatusCode.BadRequest ->
          Result.failure(
              IllegalStateException(resp.getErrorMessage()),
          )
      else -> Result.failure(IllegalStateException("Unknown error"))
    }
  }

  override suspend fun getAnnouncements(token: String?): Result<List<Announcement>> {
    val resp = httpClient.get("$baseUrl/announcement") { token?.let { parameter("token", it) } }
    return when (resp.status) {
      HttpStatusCode.OK ->
          Result.success(
              resp.body<List<AnnouncementDto>>().map { it.unpack() },
          )
      else -> Result.failure(IllegalStateException("Unknown error"))
    }
  }
}
