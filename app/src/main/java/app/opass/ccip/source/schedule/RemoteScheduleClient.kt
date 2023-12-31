package app.opass.ccip.source.schedule

import app.opass.ccip.model.Session
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.TimeZone
import org.koin.core.component.KoinComponent
import java.net.URL

class RemoteScheduleClient(
  private val httpClient: HttpClient,
) : KoinComponent {
  suspend fun getSessions(url: URL, fallbackTimeZone: TimeZone): Result<List<Session>> {
    val resp = httpClient.get(url)
    return when (resp.status) {
      HttpStatusCode.OK -> Result.success(resp.body<ScheduleDto>().unpack(fallbackTimeZone))
      else -> Result.failure(IllegalStateException("Unknown error"))
    }
  }
}
