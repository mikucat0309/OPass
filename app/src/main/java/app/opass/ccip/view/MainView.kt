package app.opass.ccip.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.LocaleManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.misc.LocalSystemLocaleList
import app.opass.ccip.misc.toList
import app.opass.ccip.ui.theme.DefaultTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

@Suppress("ModifierMissing")
@OptIn(KoinExperimentalAPI::class)
@Composable
fun MainView(modifier: Modifier = Modifier) {
  val systemLocales = LocaleManagerCompat.getSystemLocales(LocalContext.current).toList()

  KoinAndroidContext {
    CompositionLocalProvider(
        LocalSystemLocaleList provides systemLocales,
    ) {
      DefaultTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
          DestinationsNavHost(navGraph = NavGraphs.root)
        }
      }
    }
  }
}

@Composable fun <T> StateFlow<T>.cASWL() = collectAsStateWithLifecycle()
