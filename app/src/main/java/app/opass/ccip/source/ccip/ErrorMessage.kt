package app.opass.ccip.source.ccip

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(
    val message: String,
)

suspend fun HttpResponse.getErrorMessage() = body<ErrorMessage>().message
