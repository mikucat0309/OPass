package app.opass.ccip

import android.app.Application
import app.opass.ccip.home.HomeViewModel
import app.opass.ccip.home.PortalClient
import app.opass.ccip.schedule.ScheduleViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
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
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            coerceInputValues = true
                        })
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