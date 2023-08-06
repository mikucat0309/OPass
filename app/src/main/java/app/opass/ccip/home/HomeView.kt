package app.opass.ccip.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opass.ccip.LocalDestNavigator
import app.opass.ccip.LocalNavGraphViewModelStoreOwner
import app.opass.ccip.destinations.SwitchEventViewDestination
import app.opass.ccip.theme.AppTheme
import app.opass.ccip.theme.Theme
import app.opass.ccip.theme.titleBackGround
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.net.URL


@RootNavGraph(start = true)
@Destination
@Composable
fun HomeView(
    vm: HomeViewModel = koinViewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current!!),
    navigator: DestinationsNavigator
) {
    CompositionLocalProvider(
        LocalDestNavigator provides navigator
    ) {
        HomeScreen(vm.eventDetail)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    detail: EventDetail?,
    navigator: DestinationsNavigator = LocalDestNavigator.current,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Box(Modifier
                        .clickable { navigator.navigate(SwitchEventViewDestination) }
                        .padding(16.dp)) {
                        Image(Icons.Outlined.SyncAlt, "switch event")
                    }
                },
                title = { Text(detail?.name ?: "Select a Event") },
                actions = {
                    Image(Icons.Outlined.Settings, "settings", Modifier.padding(end = 8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Theme.c.primaryContainer)
            )
        }
    ) {
        Column(Modifier.padding(it)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Theme.c.primaryContainer),
            ) {
                AsyncImage(
                    model = detail?.logoUrl.toString(),
                    contentDescription = detail?.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .aspectRatio(16.0f / 9)
                        .align(Alignment.Center),
                )
                AssistChip(
                    onClick = { /*TODO switch token*/ },
                    label = { Text("Mikucat") },
                    leadingIcon = {
                        Image(Icons.Default.Person, "account avatar")
                    },
                    colors = AssistChipDefaults.assistChipColors(Theme.c.surface),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            FeatureGrid(detail?.features ?: emptyList())
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
        horizontalArrangement = Arrangement.spacedBy(24.dp),
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
                .background(titleBackGround, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(features.res), features.label, Modifier.size(36.dp))
        }
        Text(features.label, style = Theme.t.labelLarge, textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun HomePreview() {
    val f = ScheduleFeature("Schedule", URL("https://coscup.org/2023/json/session.json"))
    val ed = EventDetail(
        "1",
        "COSCUP 2023",
        URL("https://coscup.org/2020/images/logo-512-white.png"),
        URL("https://coscup.org/"),
        "2023-07-29T00:00:00+08:00",
        "2023-07-30T00:00:00+08:00",
        "2023-07-29T00:00:00+08:00",
        "2023-07-30T00:00:00+08:00",
        listOf(f, f, f, f, f, f, f)
    )
    AppTheme {
        Surface {
            HomeScreen(ed, EmptyDestinationsNavigator)
        }
    }
}