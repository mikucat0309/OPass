package app.opass.ccip.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opass.ccip.LocalDestNavigator
import app.opass.ccip.dataStore
import app.opass.ccip.destinations.SwitchEventViewDestination
import app.opass.ccip.i18n.LocalizedString
import app.opass.ccip.navGraphViewModel
import app.opass.ccip.theme.AppTheme
import app.opass.ccip.theme.Theme
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.first
import java.net.URL

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeView(
    navigator: DestinationsNavigator,
    vm: HomeViewModel = navGraphViewModel()
) {
    val dataStore = LocalContext.current.dataStore
    LaunchedEffect(vm.eventDetail) {
        val settings = dataStore.data.first()
        if (vm.eventDetail == EventDetail.empty) {
            vm.eventDetail = settings.eventDetail
        } else {
            dataStore.updateData { it.copy(eventDetail = vm.eventDetail) }
        }
    }
    CompositionLocalProvider(
        LocalDestNavigator provides navigator
    ) {
        HomeScreen(vm.eventDetail)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    detail: EventDetail,
    navigator: DestinationsNavigator = LocalDestNavigator.current
) {
    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                Icon(
                    Icons.Outlined.SyncAlt,
                    "switch event",
                    Modifier
                        .clickable { navigator.navigate(SwitchEventViewDestination) }
                        .padding(16.dp)
                )
            },
            title = {
                Text(
                    detail.name.value,
                    Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            actions = {
                Icon(Icons.Outlined.Settings, "settings", Modifier.padding(16.dp))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Theme.c.primaryContainer
            )
        )
    }) {
        Column(Modifier.padding(it)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Theme.c.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = detail.logoUrl.toString(),
                    contentDescription = detail.name.value,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .aspectRatio(2.0f)
                        .heightIn(max = 200.dp)
                )
                AssistChip(
                    onClick = { /*TODO switch token*/ },
                    label = { Text("Mikucat") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, "account avatar")
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        Theme.c.surface.copy(
                            alpha = 0.5f
                        )
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
            }
            FeatureGrid(detail.features)
        }
    }
}

@Composable
fun FeatureGrid(list: List<Feature>) {
    val navigator = LocalDestNavigator.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(72.dp),
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(list) {
            FeatureButton(it) { it.onClick(navigator) }
        }
    }
}

@Composable
fun FeatureButton(features: Feature, onClick: () -> Unit = {}) {
    Column(
        Modifier.clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(72.dp)
                .background(Theme.c.surfaceVariant, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(features.res),
                features.label.value,
                Modifier.size(36.dp)
            )
        }
        Text(
            features.label.value,
            style = Theme.t.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun HomePreview() {
    val f = ScheduleFeature(
        LocalizedString(default = "Schedule"),
        URL("https://coscup.org/2023/json/session.json")
    )
    val ed = EventDetail(
        "1",
        LocalizedString(default = "COSCUP 2023"),
        URL("https://coscup.org/2020/images/logo-512-white.png"),
        URL("https://coscup.org/"),
        "2023-07-29T00:00:00+08:00",
        "2023-07-30T00:00:00+08:00",
        "2023-07-29T00:00:00+08:00",
        "2023-07-30T00:00:00+08:00",
        listOf(f, f, f, f, f, f, f)
    )
    AppTheme(true) {
        Surface {
            HomeScreen(ed, EmptyDestinationsNavigator)
        }
    }
}
