package app.opass.ccip.schedule

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opass.ccip.LocalDestNavigator
import app.opass.ccip.compose.R
import app.opass.ccip.i18n.LocalizedString
import app.opass.ccip.misc.BackIcon
import app.opass.ccip.misc.dayOfWeekLabel
import app.opass.ccip.misc.format1
import app.opass.ccip.navGraphViewModel
import app.opass.ccip.theme.AppTheme
import app.opass.ccip.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import java.net.URL

@RootNavGraph(start = false)
@Destination
@Composable
fun ScheduleView(
    scheduleUrl: URL,
    vm: ScheduleViewModel = navGraphViewModel(),
    navigator: DestinationsNavigator
) {
    LaunchedEffect(Unit) {
        vm.update(scheduleUrl)
    }
    CompositionLocalProvider(
        LocalDestNavigator provides navigator
    ) {
        ScheduleScreen(vm.dates, vm.sessions, vm.selectedDate)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    dates: List<LocalDate>,
    sessions: Map<LocalDate, List<Session>>,
    selectedDate: MutableState<LocalDate?>,
    navigator: DestinationsNavigator = LocalDestNavigator.current
) {
    val todaySessions = sessions[selectedDate.value]
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIcon(navigator) },
                title = { Text(stringResource(R.string.title_schedule)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Theme.c.surfaceVariant)
            )
        }
    ) {
        Column(Modifier.padding(it)) {
            DateTab(dates, selectedDate.value) { date -> selectedDate.value = date }
            Divider()
            if (!todaySessions.isNullOrEmpty()) {
                Sessions(todaySessions)
            } else {
                EmptySessions()
            }
        }
    }
}

@Composable
fun EmptySessions() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No Sessions", style = Theme.t.displayMedium)
    }
}

@Composable
fun DateTab(
    dates: Collection<LocalDate>,
    selected: LocalDate? = null,
    onClick: (LocalDate) -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Theme.c.surfaceVariant),
        horizontalArrangement = Arrangement.spacedBy(
            32.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        for (date in dates) {
            DateTabItem(date.dayOfMonth, date.dayOfWeek, date == selected) { onClick(date) }
        }
    }
}

@Composable
fun DateTabItem(
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
                fontWeight = FontWeight(500)
            )
        } else {
            TextStyle.Default.copy(
                color = Theme.c.outline,
                fontSize = 14.sp,
                fontWeight = FontWeight(500)
            )
        }
    Column(
        Modifier
            .fillMaxHeight()
            .width(32.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(dayOfWeekLabel[dayOfWeek]!!), style = textStyle)
        Text(day.toString(), style = textStyle.copy(fontSize = 22.sp, fontWeight = FontWeight(700)))
        Spacer(Modifier.weight(1.0f))
        val m = Modifier
            .fillMaxWidth()
            .height(3.dp)
        Box(
            if (isSelected) {
                m.background(
                    color = Theme.c.primary,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
            } else {
                m
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Sessions(sessions: List<Session>) {
    val m = sessions.groupBy { it.start }

    LazyColumn {
        m.forEach { (start, part) ->
            stickyHeader {
                StartTimeTitle(start)
            }
            itemsIndexed(part) { i, session ->
                SessionItem(session)
                if (i < part.lastIndex) {
                    Divider(Modifier.padding(horizontal = 24.dp))
                }
            }
        }
    }
}

@Composable
fun StartTimeTitle(start: LocalDateTime) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Theme.c.surfaceVariant)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            start.time.format1(),
            style = Theme.t.titleMedium
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SessionItem(session: Session) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LocationTag(session.room.name.value)
                TimeRange(session.start.time, session.end.time)
            }
            Box {
                Text(session.title.value)
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SessionTypeTag(session.type.name.value)
                for (tag in session.tags) {
                    NormalTag(tag.name.value)
                }
            }
        }
        Icon(Icons.Default.KeyboardArrowRight, "", Modifier.size(24.dp))
    }
}

@Composable
private fun TimeRange(start: LocalTime, end: LocalTime) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            "${start.format1()} ~ ${end.format1()}",
            style = Theme.t.labelLarge
        )
    }
}

@Composable
private fun LocationTag(value: String) {
    Box(
        Modifier
            .background(Theme.c.primaryContainer, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
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
        contentAlignment = Alignment.Center
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
                shape = RoundedCornerShape(size = 4.dp)
            )
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(value, style = Theme.t.labelLarge, color = Theme.c.onSurfaceVariant)
    }
}

@Preview
@Composable
fun SchedulePreview() {
    val speaker = Speaker(
        "1",
        LocalizedString(mapOf("zh" to "蓬蓬博士"), default = "Dr. Pancake"),
        LocalizedString(
            default = "蓬蓬博士作為研究蓬蓬鬆餅的第一權威人士，他曾經在倫敦蓬蓬大學任教，且在《紐約時報》、《美國商業周刊》、《蓬蓬新報》等國際媒體工作多年，撰寫了大量關於蓬蓬鬆餅的報導，蓬蓬博士的研究領域更涉及了各類甜食的製作及甜點。"
        )
    )
    val type = SessionType(
        "1",
        LocalizedString(
            mapOf("zh" to "非常開放開放開放開放開放開放式議程"),
            default = "very loooooooooooooooooong tag"
        )
    )
    val room = Room("1", LocalizedString(default = "R0"))
    val tag = SessionTag("1", LocalizedString(mapOf("zh" to "鬆餅"), default = "pancake"))
    val sessions = listOf(
        Session(
            id = "1",
            speakers = listOf(speaker),
            type = type,
            room = room,
            start = LocalDateTime(2023, 7, 29, 9, 10, 0),
            end = LocalDateTime(2023, 7, 29, 10, 0, 0),
            title = LocalizedString(default = "蓬蓬鬆餅大集合"),
            description = LocalizedString.empty,
            language = "中文",
            tags = listOf(tag, tag, tag, tag)
        ),
        Session(
            id = "2",
            speakers = listOf(speaker),
            type = type,
            room = room,
            start = LocalDateTime(2023, 7, 29, 10, 10, 0),
            end = LocalDateTime(2023, 7, 29, 11, 0, 0),
            title = LocalizedString(
                mapOf("zh" to "[中文] 蓬蓬鬆餅大集合"),
                default = "[Eng] Collection of Pancakes"
            ),
            description = LocalizedString.empty,
            language = "English",
            tags = listOf(tag)
        ),
        Session(
            id = "3",
            speakers = listOf(speaker),
            type = type,
            room = room,
            start = LocalDateTime(2023, 7, 29, 10, 10, 0),
            end = LocalDateTime(2023, 7, 29, 11, 0, 0),
            title = LocalizedString(default = "蓬蓬鬆餅大集合"),
            description = LocalizedString.empty,
            language = "中文",
            tags = listOf(tag)
        )
    )
    val dates = listOf(
        LocalDate(2023, 7, 29),
        LocalDate(2023, 7, 30),
        LocalDate(2023, 7, 31)
    )
    val m = mapOf(
        LocalDate(2023, 7, 29) to sessions,
        LocalDate(2023, 7, 30) to sessions,
        LocalDate(2023, 7, 31) to sessions
    )
    val selectedDate = remember { mutableStateOf<LocalDate?>(dates[0]) }
    AppTheme(true) {
        Surface {
            ScheduleScreen(dates, m, selectedDate)
        }
    }
}
