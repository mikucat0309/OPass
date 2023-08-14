package app.opass.ccip.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opass.ccip.LocalDestNavigator
import app.opass.ccip.compose.R
import app.opass.ccip.destinations.HomeViewDestination
import app.opass.ccip.i18n.LocalizedString
import app.opass.ccip.misc.BackIcon
import app.opass.ccip.navGraphViewModel
import app.opass.ccip.theme.AppTheme
import app.opass.ccip.theme.Theme
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.net.URL

@RootNavGraph
@Destination
@Composable
fun SwitchEventView(
    vm: HomeViewModel = navGraphViewModel(),
    navigator: DestinationsNavigator
) {
    CompositionLocalProvider(
        LocalDestNavigator provides navigator
    ) {
        SwitchEventScreen(vm.events) {
            vm.loadRemoteEventDetail(it.id)
            navigator.popBackStack(HomeViewDestination.route, false)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchEventScreen(
    events: List<Event>,
    onClick: (Event) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackIcon()
                },
                title = {
                    Column {
                        Text(stringResource(R.string.title_events), style = Theme.t.headlineSmall)
                        Text(stringResource(R.string.subtitle_events), style = Theme.t.bodySmall)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(events) {
                    EventCard(it) { onClick(it) }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onClick: () -> Unit = {}) {
    Column(
        Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            Modifier.aspectRatio(16.0f / 9)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    event.logoUrl.toString(),
                    event.name.value,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }
        }
        Text(event.name.value, style = Theme.t.labelLarge)
    }
}

@Preview
@Composable
fun SwitchEventPreview() {
    val event = Event(
        "1",
        LocalizedString(default = "COSCUP 2023"),
        URL("https://coscup.org/2020/images/logo-512-white.png")
    )
    val events = listOf(event, event, event, event, event, event, event)
    AppTheme {
        Surface {
            SwitchEventScreen(events)
        }
    }
}
