package app.opass.ccip.misc

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import app.opass.ccip.LocalDestNavigator
import app.opass.ccip.destinations.HomeViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) { context as ViewModelStoreOwner }
}

@Composable
fun BackIcon(
    navigator: DestinationsNavigator = LocalDestNavigator.current
) {
    Icon(
        Icons.Default.ArrowBack,
        "back",
        Modifier
            .clickable { navigator.popBackStack(HomeViewDestination.route, false) }
            .padding(horizontal = 16.dp)
    )
}
