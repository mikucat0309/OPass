package app.opass.ccip.i18n

import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class LocalizedString(
    private val map: Map<String, String> = emptyMap(),
    private val default: String = ""
) {
    val value: String
        get() = map.getOrDefault(Locale.getDefault().language, default)

    override fun toString(): String = value

    companion object {
        val empty = LocalizedString()
    }
}
