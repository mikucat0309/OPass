package app.opass.ccip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import app.opass.ccip.misc.rememberViewModelStoreOwner
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainView() {
    val owner = rememberViewModelStoreOwner()
    CompositionLocalProvider(
        LocalNavGraphViewModelStoreOwner provides owner
    ) {
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}

val LocalDestNavigator: ProvidableCompositionLocal<DestinationsNavigator> =
    staticCompositionLocalOf { EmptyDestinationsNavigator }

val LocalNavGraphViewModelStoreOwner =
    staticCompositionLocalOf<ViewModelStoreOwner?> { null }

@Composable
inline fun <reified T : ViewModel> navGraphViewModel(): T = koinViewModel(
    viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current!!
)
