package app.opass.ccip.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opass.ccip.compose.R
import app.opass.ccip.misc.I18nText
import app.opass.ccip.model.Attendee
import app.opass.ccip.model.EventConfig
import app.opass.ccip.model.EventFeature
import app.opass.ccip.model.ExternalUrlEventFeature
import app.opass.ccip.model.InternalUrlEventFeature
import app.opass.ccip.model.WifiEventFeature
import app.opass.ccip.ui.theme.DefaultTheme
import app.opass.ccip.ui.theme.Theme
import app.opass.ccip.view.destinations.SwitchEventViewDestination
import app.opass.ccip.viewmodel.HomeViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeView(
    navigator: DestinationsNavigator,
    vm: HomeViewModel = koinViewModel(),
) {
  val eventConfig by vm.eventConfig.cASWL()
  val attendee by vm.attendee.cASWL()
  HomeScreen(eventConfig, attendee, navigator)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    eventConfig: EventConfig?,
    attendee: Attendee?,
    navigator: DestinationsNavigator,
) {
  val features = eventConfig?.features ?: emptyList()
  Scaffold(
      topBar = {
        TopAppBar(
            navigationIcon = {
              Icon(
                  painterResource(R.drawable.sync_alt),
                  "switch event",
                  Modifier.clickable { navigator.navigate(SwitchEventViewDestination) }
                      .padding(16.dp),
              )
            },
            title = {
              Text(
                  eventConfig?.name?.current ?: "",
                  Modifier.fillMaxWidth(),
                  textAlign = TextAlign.Center,
              )
            },
            actions = { Icon(Icons.Outlined.Settings, "settings", Modifier.padding(16.dp)) },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Theme.c.surfaceVariant,
                ),
        )
      },
  ) { pv ->
    Column(Modifier.padding(pv)) {
      EventHeader(eventConfig, attendee?.userId)
      if (eventConfig != null) {
        FeatureGrid(features.toImmutableList(), navigator, attendee == null)
      }
    }
  }
}

@Composable
private fun EventHeader(
    eventConfig: EventConfig?,
    name: String?,
) {
  Column(
      Modifier.fillMaxWidth().background(Theme.c.surfaceVariant),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    AsyncImage(
        eventConfig?.logoUrl.toString(),
        eventConfig?.name?.current ?: "",
        koinInject(),
        Modifier.padding(horizontal = 32.dp).aspectRatio(2.0f).heightIn(max = 180.dp),
        contentScale = ContentScale.Fit,
    )
    if (eventConfig?.features?.any { it.type == "ticket" } == true) {
      AssistChip(
          onClick = {},
          label = { Text(name ?: "Guest") },
          leadingIcon = { Icon(Icons.Default.Person, "account avatar") },
          colors =
              AssistChipDefaults.assistChipColors(
                  Theme.c.surface.copy(
                      alpha = 0.5f,
                  ),
              ),
          shape = RoundedCornerShape(30.dp),
      )
    }
  }
}

@Composable
private fun FeatureGrid(
    list: ImmutableList<EventFeature>,
    navigator: DestinationsNavigator,
    isGuest: Boolean,
) {
  LazyVerticalGrid(
      columns = GridCells.Adaptive(72.dp),
      Modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalArrangement = Arrangement.spacedBy(24.dp),
  ) {
    items(list) { FeatureButton(it, onClick = onClick(it, navigator, isGuest)) }
  }
}

@Composable
private fun onClick(
    feature: EventFeature,
    navigator: DestinationsNavigator,
    isGuest: Boolean
): () -> Unit {
  val context = LocalContext.current
  return when (feature) {
    is InternalUrlEventFeature -> {
      { feature.onClick(navigator, isGuest) }
    }
    is ExternalUrlEventFeature -> {
      { feature.onClick(navigator, isGuest, context) }
    }
    is WifiEventFeature -> {
      { Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show() }
    }
  }
}

@Composable
private fun FeatureButton(
    feature: EventFeature,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
  Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    IconButton(
        onClick = onClick,
        Modifier.background(Theme.c.surfaceVariant, RoundedCornerShape(16.dp)).size(72.dp),
        enabled = enabled,
    ) {
      if (feature.iconUrl != null) {
        AsyncImage(
            feature.iconUrl,
            feature.name.current,
            koinInject(),
            Modifier.size(36.dp),
            colorFilter = ColorFilter.tint(LocalContentColor.current),
        )
      } else if (feature.icon != null) {
        Icon(
            painterResource(feature.icon!!),
            feature.name.current,
        )
      }
    }
    Text(
        feature.name.current,
        style = Theme.t.labelLarge,
        textAlign = TextAlign.Center,
    )
  }
}

@Preview
@Composable
private fun FeatureButtonPreview() {
  DefaultTheme {
    Surface {
      FeatureButton(
          WifiEventFeature(
              "wifi",
              I18nText(Locale.ENGLISH to "WiFi"),
              false,
              mapOf(),
              R.drawable.wifi_36,
          ),
      )
    }
  }
}
