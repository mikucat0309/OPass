package app.opass.ccip

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import app.opass.ccip.model.MainModel
import app.opass.ccip.source.local.Config
import app.opass.ccip.source.local.JSONSerializer
import app.opass.ccip.source.portal.PortalClient
import app.opass.ccip.viewmodel.AnnouncementViewModel
import app.opass.ccip.viewmodel.EnterTokenViewModel
import app.opass.ccip.viewmodel.HomeViewModel
import app.opass.ccip.viewmodel.ScheduleViewModel
import app.opass.ccip.viewmodel.SwitchEventViewModel
import app.opass.ccip.viewmodel.TicketViewModel
import coil.ImageLoader
import coil.util.DebugLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import javax.net.ssl.SSLContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    val configDataStore = configDataStore

    val config = runBlocking(Dispatchers.IO) { configDataStore.data.first() }

    val globalModule = module {
      single {
        Json {
          ignoreUnknownKeys = true
          coerceInputValues = true
        }
      }
      single {
        HttpClient(OkHttp) {
          if (config.debug) {
            engine {
              config {
                val sslContext =
                    SSLContext.getInstance("TLS").apply {
                      init(null, arrayOf(TrustAllManager), null)
                    }
                sslSocketFactory(sslContext.socketFactory, TrustAllManager)
              }
            }
          }
          install(ContentNegotiation) { json(get()) }
          install(Logging) {
            logger = Logger.ANDROID
            level = if (config.debug) LogLevel.ALL else LogLevel.NONE
          }
          install(HttpCache) { publicStorage(FileStorage(androidContext().cacheDir)) }
          expectSuccess = true
        }
      }
      single { Dispatchers.IO }
    }

    val modelModule = module { singleOf(::MainModel) }

    val sourceModule = module {
      single { PortalClient(get(), get(), config.portalBaseUrl) }
      single {
        ImageLoader.Builder(androidContext())
            .apply { if (config.debug) logger(DebugLogger()) }
            .build()
      }
    }

    val viewModelModule = module {
      viewModelOf(::HomeViewModel)
      viewModelOf(::EnterTokenViewModel)
      viewModelOf(::SwitchEventViewModel)
      viewModelOf(::ScheduleViewModel)
      viewModelOf(::TicketViewModel)
      viewModelOf(::AnnouncementViewModel)
    }

    startKoin {
      androidLogger()
      androidContext(this@MainApplication)
      modules(globalModule, modelModule, sourceModule)
      modules(viewModelModule)
    }
  }
}

val Context.configDataStore: DataStore<Config> by
    dataStore(
        fileName = "config.json",
        serializer = JSONSerializer,
    )
