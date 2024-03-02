package app.opass.ccip

import android.app.Application
import app.opass.ccip.model.CcipModel
import app.opass.ccip.model.PortalModel
import app.opass.ccip.model.ScheduleModel
import app.opass.ccip.source.ccip.RemoteCcipClient
import app.opass.ccip.source.portal.RemotePortalClient
import app.opass.ccip.source.schedule.RemoteScheduleClient
import app.opass.ccip.viewmodel.EnterTokenViewModel
import app.opass.ccip.viewmodel.HomeViewModel
import app.opass.ccip.viewmodel.ScheduleViewModel
import app.opass.ccip.viewmodel.SwitchEventViewModel
import app.opass.ccip.viewmodel.TicketViewModel
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import javax.net.ssl.SSLContext
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class MainApplication : Application(), ImageLoaderFactory {
  override fun onCreate() {
    super.onCreate()

    val globalModule = module {
      single {
        Json {
          ignoreUnknownKeys = true
          coerceInputValues = true
        }
      }
      single {
        HttpClient(OkHttp) {
          if (Config.DEBUG) {
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
          install(Logging) { level = if (Config.DEBUG) LogLevel.ALL else LogLevel.NONE }
          install(HttpCache) { publicStorage(FileStorage(androidContext().cacheDir)) }
        }
      }
      single { RemotePortalClient(get(), Config.PORTAL_BASE_URL) }
      single { RemoteCcipClient(get()) }
      single { RemoteScheduleClient(get()) }
      single { Dispatchers.IO }
    }

    val modelModule = module {
      singleOf(::PortalModel)
      singleOf(::CcipModel)
      singleOf(::ScheduleModel)
    }

    val viewModelModule = module {
      viewModelOf(::HomeViewModel)
      viewModelOf(::EnterTokenViewModel)
      viewModelOf(::SwitchEventViewModel)
      viewModelOf(::ScheduleViewModel)
      viewModelOf(::TicketViewModel)
    }

    startKoin {
      androidLogger()
      androidContext(this@MainApplication)
      modules(globalModule, modelModule)
      modules(viewModelModule)
    }
  }

  override fun newImageLoader(): ImageLoader {
    return ImageLoader.Builder(this).apply { if (Config.DEBUG) logger(DebugLogger()) }.build()
  }
}
