package app.opass.ccip.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opass.ccip.model.DateTimeRange
import app.opass.ccip.model.Session
import app.opass.ccip.ui.theme.Theme
import app.opass.ccip.viewmodel.ScheduleViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@RootNavGraph(start = false)
@Destination
@Composable
fun ScheduleView(navigator: DestinationsNavigator, vm: ScheduleViewModel = navGraphViewModel()) {
  LaunchedEffect(Unit) { vm.fetchSessions() }
  val sessions = vm.sessions.cASWL().value.toImmutableList()
  ScheduleScreen(sessions, navigator)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleScreen(sessions: ImmutableList<Session>, navigator: DestinationsNavigator) {
  val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
  val groups =
      sessions
          .groupBy {
            it.dateTime.start.instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
          }
          .mapValues { it.value.toImmutableList() }
  val dates = groups.keys.sorted().toImmutableList()
  if (groups.keys.isNotEmpty()) {
    selectedDate.value = groups.keys.first()
  }
  Scaffold(
      topBar = {
        TopAppBar(
            navigationIcon = { BackIcon(navigator) },
            title = { Text("Schedule") },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Theme.c.surfaceVariant),
        )
      },
  ) { pv ->
    Column(Modifier.padding(pv)) {
      if (sessions.isNotEmpty() && selectedDate.value != null) {
        DateTab(dates, selectedDate.value) { selectedDate.value = it }
        HorizontalDivider()
        Sessions(groups[selectedDate.value]!!)
      }
    }
  }
}

@Composable
private fun DateTab(
  dates: ImmutableList<LocalDate>,
  selectedDate: LocalDate?,
  setSelectedDate: (LocalDate) -> Unit
) {
  Row(
      Modifier
          .fillMaxWidth()
          .height(56.dp)
          .background(Theme.c.surfaceVariant),
      horizontalArrangement =
      Arrangement.spacedBy(
          32.dp,
          alignment = Alignment.CenterHorizontally,
      ),
  ) {
    for (date in dates) {
      DateTabItem(date.dayOfMonth, date.dayOfWeek, date == selectedDate) { setSelectedDate(date) }
    }
  }
}

@Composable
private fun DateTabItem(
  day: Int,
  dayOfWeek: DayOfWeek,
  isSelected: Boolean = false,
  onClick: () -> Unit = {}
) {
  val textStyle =
      if (isSelected) {
        TextStyle.Default.copy(
            color = Theme.c.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
        )
      } else {
        TextStyle.Default.copy(
            color = Theme.c.outline,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
        )
      }
  Column(
      Modifier
          .fillMaxHeight()
          .width(32.dp)
          .clickable { onClick() },
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(stringResource(WEEKDAY_LABELS[dayOfWeek]!!), style = textStyle)
    Text(day.toString(), style = textStyle.copy(fontSize = 22.sp, fontWeight = FontWeight(700)))
    Spacer(Modifier.weight(1.0f))
    val m = Modifier
        .fillMaxWidth()
        .height(3.dp)
    Box(
        if (isSelected) {
          m.background(
              color = Theme.c.primary,
              shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
          )
        } else {
          m
        },
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Sessions(sessions: ImmutableList<Session>) {
  val m =
      sessions
          .sortedBy { it.dateTime.start.instant.toLocalDateTime(TimeZone.currentSystemDefault()) }
          .groupBy { it.dateTime.start.instant.toLocalDateTime(TimeZone.currentSystemDefault()) }

  LazyColumn {
    m.forEach { (start, part) ->
      stickyHeader { StartTimeTitle(start) }
      itemsIndexed(part) { i, session ->
        SessionItem(session)
        if (i < part.lastIndex) {
          HorizontalDivider(Modifier.padding(horizontal = 24.dp))
        }
      }
    }
  }
}

@Composable
private fun StartTimeTitle(start: LocalDateTime) {
  Box(
      Modifier
          .fillMaxWidth()
          .background(Theme.c.surfaceVariant)
          .padding(horizontal = 24.dp, vertical = 16.dp),
      contentAlignment = Alignment.CenterStart,
  ) {
    // 9:10, 23:59
    Text(
        start.time.format1(),
        style = Theme.t.titleMedium,
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SessionItem(session: Session) {
  Row(
      Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(
        Modifier.weight(1.0f),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        LocationTag(session.room.name.current)
        TimeRange(session.dateTime)
      }
      Box { Text(session.title.current) }
      FlowRow(
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        SessionTypeTag(session.type.name.current)
        for (tag in session.tags) {
          NormalTag(tag.name.current)
        }
      }
    }
    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "", Modifier.size(24.dp))
  }
}

@Composable
private fun TimeRange(range: DateTimeRange) {
  Box(contentAlignment = Alignment.Center) {
    Text(
        range.start.instant.toLocalDateTime(TimeZone.currentSystemDefault()).time.format1() +
            " ~ " +
            range.end.instant.toLocalDateTime(TimeZone.currentSystemDefault()).time.format1(),
        style = Theme.t.labelLarge,
    )
  }
}

@Composable
private fun LocationTag(value: String) {
  Box(
      Modifier
          .background(Theme.c.primaryContainer, RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp),
      contentAlignment = Alignment.Center,
  ) {
    Text(value, style = Theme.t.labelLarge.copy(color = Theme.c.primary))
  }
}

@Composable
private fun SessionTypeTag(value: String) {
  Box(
      Modifier
          .background(Theme.c.surfaceVariant, RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp),
      contentAlignment = Alignment.Center,
  ) {
    Text(value, style = Theme.t.labelLarge, color = Theme.c.onSurfaceVariant)
  }
}

@Composable
private fun NormalTag(value: String) {
  Box(
      Modifier
          .border(
              width = 1.dp,
              color = Theme.c.surfaceVariant,
              shape = RoundedCornerShape(size = 4.dp),
          )
          .padding(horizontal = 6.dp),
      contentAlignment = Alignment.Center,
  ) {
    Text(value, style = Theme.t.labelLarge, color = Theme.c.onSurfaceVariant)
  }
}

private fun LocalTime.format1(): String = "$hour:${minute.toString().padStart(2, '0')}"
