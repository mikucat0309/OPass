package app.opass.ccip.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opass.ccip.misc.I18nText
import app.opass.ccip.model.Announcement
import app.opass.ccip.ui.theme.DefaultTheme
import app.opass.ccip.ui.theme.Theme
import app.opass.ccip.viewmodel.AnnouncementViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.net.URL
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Destination
@Composable
fun AnnouncementView(
    navigator: DestinationsNavigator,
    vm: AnnouncementViewModel = navGraphViewModel()
) {
  LaunchedEffect(Unit) { vm.loadAnnouncements() }
  val announcements = vm.announcements.cASWL().value.toImmutableList()
  AnnouncementScreen(navigator, announcements)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnnouncementScreen(
    navigator: DestinationsNavigator,
    announcements: ImmutableList<Announcement>
) {
  Scaffold(
      topBar = {
        LargeTopAppBar(
            navigationIcon = { BackIcon(navigator) },
            title = {
              Text(
                  "Announcement",
                  Modifier.fillMaxWidth(),
                  textAlign = TextAlign.Left,
              )
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Theme.c.surfaceVariant,
                ),
        )
      },
  ) { pv ->
    Announcements(
        announcements,
        Modifier.padding(pv).padding(16.dp),
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Announcements(
    announcements: ImmutableList<Announcement>,
    modifier: Modifier = Modifier
) {
  val m =
      announcements
          .sortedBy { it.datetime }
          .groupBy { it.datetime.toLocalDateTime(TimeZone.currentSystemDefault()).date }
  LazyColumn(modifier) {
    m.forEach { (start, part) ->
      stickyHeader { DateDivider(start) }
      itemsIndexed(part) { i, session -> AnnouncementItem(session) }
    }
  }
}

@Composable
private fun DateDivider(date: LocalDate) {
  Row(
      Modifier.height(32.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    HorizontalDivider(Modifier.weight(1.0f))
    Text("${date.monthNumber}/${date.dayOfMonth}", style = Theme.t.labelMedium)
    HorizontalDivider(Modifier.weight(1.0f))
  }
}

@Composable
private fun AnnouncementItem(announcement: Announcement) {
  val context = LocalContext.current
  val time = announcement.datetime.toLocalDateTime(TimeZone.currentSystemDefault()).time
  Row(verticalAlignment = Alignment.Bottom) {
    Surface(
        Modifier.weight(1.0f, fill = false)
            .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp))
            .background(Theme.c.primary)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        color = Theme.c.primary,
    ) {
      SelectionContainer {
        Column {
          Text(announcement.message.current, style = Theme.t.titleMedium)
          if (announcement.url != null) {
            ClickableText(
                AnnotatedString(
                    announcement.url.toString(),
                    spanStyle = SpanStyle(textDecoration = TextDecoration.Underline),
                ),
                style = Theme.t.titleMedium.copy(color = LocalContentColor.current),
            ) {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse(announcement.url.toString()))
              context.startActivity(intent)
            }
          }
        }
      }
    }
    Spacer(Modifier.width(4.dp))
    Text("${time.hour}:${time.minute}", Modifier.width(30.dp), style = Theme.t.labelSmall)
  }
}

@Preview
@Composable
private fun Preview() {
  val ann =
      Announcement(
          Instant.fromEpochSeconds(170000000L),
          I18nText(Locale.ENGLISH to "Test"),
          URL("https://example.com/"),
      )
  DefaultTheme {
    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
      Column {
        DateDivider(date = LocalDate(2000, 9, 4))
        AnnouncementItem(ann)
      }
    }
  }
}
