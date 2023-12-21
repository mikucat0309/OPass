package app.opass.ccip.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.LocaleManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.LocalSystemLocaleList
import app.opass.ccip.toList
import app.opass.ccip.ui.theme.DefaultTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Suppress("ModifierMissing")
@OptIn(KoinExperimentalAPI::class)
@Composable
fun MainView(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val owner = remember(context) { context as ViewModelStoreOwner }
  val systemLocales = LocaleManagerCompat.getSystemLocales(LocalContext.current).toList()

  KoinAndroidContext {
    CompositionLocalProvider(
        LocalViewModelStoreOwner provides owner,
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

val LocalViewModelStoreOwner = staticCompositionLocalOf<ViewModelStoreOwner?> { null }

@Composable fun <T> StateFlow<T>.cASWL() = collectAsStateWithLifecycle()

@Composable
inline fun <reified T : ViewModel> navGraphViewModel(): T =
    koinViewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current!!,
    )
