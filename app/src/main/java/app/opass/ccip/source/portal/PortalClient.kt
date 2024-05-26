package app.opass.ccip.source.portal

import app.opass.ccip.model.Event
import app.opass.ccip.model.EventConfig
import app.opass.ccip.model.InternalUrlEventFeature
import app.opass.ccip.model.MainModel
import app.opass.ccip.model.Session
import app.opass.ccip.source.schedule.ScheduleDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.update
import kotlinx.datetime.TimeZone
import org.koin.core.component.KoinComponent

class PortalClient(
    private val model: MainModel,
    private val httpClient: HttpClient,
    private val baseUrl: String
) : KoinComponent {

  private fun String.replaceTemplate(): String {
    var result = this
    model.attendee.value?.let { result = result.replace("{token}", it.token) }
    return result
  }

  suspend fun updateEvents(): Result<List<Event>> {
    return fetchEvents()
  }

  private suspend fun fetchEvents(): Result<List<Event>> {
    return try {
      val resp = httpClient.get("$baseUrl/events/")
      Result.success(resp.body<List<EventDto>>().map { it.unpack() })
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun updateEventConfig(id: String): Result<EventConfig> {
    return fetchEventConfig(id).onSuccess { config -> model.eventConfig.update { config } }
  }

  private suspend fun fetchEventConfig(id: String): Result<EventConfig> {
    return try {
      val resp = httpClient.get("$baseUrl/events/$id/")
      Result.success(resp.body<EventConfigDto>().unpack())
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun updateSessions(): Result<List<Session>> {
    val config = model.eventConfig.value ?: return Result.failure(IllegalStateException("no event"))
    val feature =
        config.features.firstOrNull { it.type == "schedule" }
            ?: return Result.failure(IllegalStateException("no schedule"))
    val url = (feature as InternalUrlEventFeature).url.replaceTemplate()
    val timeZone = config.eventDateTimeRange.start.timeZone
    return fetchSessions(url, timeZone)
  }

  private suspend fun fetchSessions(
      url: String,
      fallbackTimeZone: TimeZone
  ): Result<List<Session>> {
    return try {
      val resp = httpClient.get(url)
      return Result.success(resp.body<ScheduleDto>().unpack(fallbackTimeZone))
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
