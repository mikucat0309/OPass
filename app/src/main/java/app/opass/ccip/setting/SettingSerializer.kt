package app.opass.ccip.setting

import androidx.datastore.core.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
object SettingSerializer : Serializer<Settings>, KoinComponent {
    override val defaultValue: Settings
        get() = Settings()

    private val j: Json by inject()

    override suspend fun readFrom(input: InputStream): Settings =
        j.decodeFromStream(input)

    override suspend fun writeTo(t: Settings, output: OutputStream) =
        j.encodeToStream(t, output)
}
