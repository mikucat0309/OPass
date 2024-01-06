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
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
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

    val globalModule = module {
      single {
        Json {
          ignoreUnknownKeys = true
          coerceInputValues = true
        }
      }
      single {
        HttpClient(OkHttp) {
          install(ContentNegotiation) { json(get()) }
          install(Logging) { level = LogLevel.ALL }
          install(HttpCache) { publicStorage(FileStorage(androidContext().cacheDir)) }
        }
      }
      single { RemotePortalClient(get(), "https://portal.opass.app") }
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
    }

    startKoin {
      androidLogger()
      androidContext(this@MainApplication)
      modules(globalModule, modelModule)
      modules(viewModelModule)
    }
  }
}
