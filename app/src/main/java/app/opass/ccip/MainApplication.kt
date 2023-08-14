package app.opass.ccip

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import app.opass.ccip.home.HomeViewModel
import app.opass.ccip.home.PortalClient
import app.opass.ccip.schedule.ScheduleViewModel
import app.opass.ccip.setting.SettingSerializer
import app.opass.ccip.setting.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val viewModels = module {
            viewModelOf(::ScheduleViewModel)
            viewModelOf(::HomeViewModel)
        }

        val misc = module {
            single {
                Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }
            }
            single {
                HttpClient(OkHttp) {
                    install(ContentNegotiation) {
                        json(get())
                    }
                    install(Logging) {
                        level = LogLevel.INFO
                    }
                    install(HttpCache) {
                        publicStorage(FileStorage(androidContext().cacheDir))
                    }
                }
            }
            single {
                PortalClient()
            }
        }

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)

            modules(viewModels, misc)
        }
    }
}

val Context.dataStore: DataStore<Settings> by dataStore(
    "settings.json",
    SettingSerializer
)
