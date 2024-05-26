package app.opass.ccip.source.local

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val portalBaseUrl: String,
    val lastEvent: String?,
    val debug: Boolean,
)
