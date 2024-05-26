package app.opass.ccip.view

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.opass.ccip.model.Event
import app.opass.ccip.ui.theme.Theme
import app.opass.ccip.view.destinations.HomeViewDestination
import app.opass.ccip.viewmodel.EventState
import app.opass.ccip.viewmodel.SwitchEventViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SwitchEventView(
    navigator: DestinationsNavigator,
    vm: SwitchEventViewModel = koinViewModel(),
) {
  if (vm.eventState == EventState.DONE) {
    navigator.popBackStack(HomeViewDestination.route, false)
  }
  val events = vm.events.toImmutableList()
  LaunchedEffect(Unit) { vm.fetchEvents() }
  SwitchEventScreen(events, navigator) { vm.fetchEventConfig(it.id) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwitchEventScreen(
    events: ImmutableList<Event>,
    navigator: DestinationsNavigator,
    onClick: (Event) -> Unit = {}
) {
  Scaffold(
      topBar = {
        TopAppBar(
            navigationIcon = { BackIcon(navigator) },
            title = {
              Column {
                Text("OPass", style = Theme.t.titleLarge)
                Text("select a event", style = Theme.t.bodySmall)
              }
            },
        )
      },
  ) { padding ->
    Box(
        Modifier.padding(padding).padding(16.dp),
    ) {
      LazyVerticalGrid(
          columns = GridCells.Adaptive(160.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        items(events) { EventCard(it) { onClick(it) } }
      }
    }
  }
}

@Composable
private fun EventCard(event: Event, onClick: () -> Unit = {}) {
  Log.d("coil", event.logoUrl.toString())
  Column(
      Modifier.clickable { onClick() },
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
  ) {
    Card(Modifier.aspectRatio(16.0f / 9)) {
      Box(
          Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
          contentAlignment = Alignment.Center,
      ) {
        AsyncImage(
            event.logoUrl.toString(),
            event.name.current,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
        )
      }
    }
    Text(event.name.current, style = Theme.t.labelLarge)
  }
}
