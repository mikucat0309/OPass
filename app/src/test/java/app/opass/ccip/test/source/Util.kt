package app.opass.ccip.test.source

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json

fun mockJsonHttpClient(vararg pair: Pair<String, String>): HttpClient {
  val map = mapOf(*pair)
  return HttpClient(
      MockEngine { req ->
        respond(
            content = ByteReadChannel(map[req.url.fullPath]!!),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      },
  ) {
    install(ContentNegotiation) {
      json(
          Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
          },
      )
    }
  }
}
