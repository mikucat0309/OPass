package app.opass.ccip.source.local

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

object JSONSerializer : Serializer<Config> {
  override val defaultValue: Config = defaultConfig
  private val serializer = Json

  override suspend fun readFrom(input: InputStream): Config {
    return withContext(Dispatchers.IO) {
      serializer.decodeFromString(input.bufferedReader().readText())
    }
  }

  override suspend fun writeTo(t: Config, output: OutputStream) {
    withContext(Dispatchers.IO) { output.bufferedWriter().write(serializer.encodeToString(t)) }
  }
}

object StringMapSerializer : Serializer<Map<String, String>> {
  override val defaultValue: Map<String, String> = emptyMap()
  private val serializer = Json

  override suspend fun readFrom(input: InputStream): Map<String, String> {
    return withContext(Dispatchers.IO) {
      val obj = serializer.decodeFromString<JsonObject>(input.bufferedReader().readText())
      obj.mapValues { it.value.jsonPrimitive.content }
    }
  }

  override suspend fun writeTo(t: Map<String, String>, output: OutputStream) {
    withContext(Dispatchers.IO) { output.bufferedWriter().write(serializer.encodeToString(t)) }
  }
}
