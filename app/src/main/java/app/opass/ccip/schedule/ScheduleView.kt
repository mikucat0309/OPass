package app.opass.ccip.schedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
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
import app.opass.ccip.BackIcon
import app.opass.ccip.LocalDestNavigator
import app.opass.ccip.compose.R
import app.opass.ccip.theme.AppTheme
import app.opass.ccip.theme.Theme
import app.opass.ccip.theme.tag
import app.opass.ccip.theme.tagContainer
import app.opass.ccip.theme.timeTag
import app.opass.ccip.theme.titleBackGround
import app.opass.ccip.util.dayOfWeekLabel
import app.opass.ccip.util.format1
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.koin.androidx.compose.koinViewModel
import java.net.URL

@RootNavGraph(start = false)
@Destination
@Composable
fun ScheduleView(
    scheduleUrl: String,
    vm: ScheduleViewModel = koinViewModel(),
    navigator: DestinationsNavigator
) {
    LaunchedEffect(Unit) {
        vm.update(URL(scheduleUrl))
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
    navigator: DestinationsNavigator = LocalDestNavigator.current,
) {
    val todaySessions = sessions[selectedDate.value]
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIcon(navigator) },
                title = { Text(stringResource(R.string.title_schedule)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = titleBackGround)
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
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text("No Sessions", style = Theme.t.displayMedium)
    }
}

@Composable
fun DateTab(
    dates: Collection<LocalDate>,
    selected: LocalDate? = null,
    onClick: (LocalDate) -> Unit = {},
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(titleBackGround),
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
    onClick: () -> Unit = {},
) {
    val textStyle =
        if (isSelected) TextStyle.Default.copy(
            color = Theme.c.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight(500)
        ) else TextStyle.Default.copy(
            color = Theme.c.outline,
            fontSize = 14.sp,
            fontWeight = FontWeight(500)
        )
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
            if (isSelected) m.background(
                color = Theme.c.primary,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            )
            else m
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
            .background(titleBackGround)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart,
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
                LocationTag(session.room.name)
                TimeRange(session.start.time, session.end.time)
            }
            Box {
                Text(session.title)
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SessionTypeTag(session.type.name)
                for (tag in session.tags) {
                    NormalTag(tag.name)
                }
            }
        }
        Image(Icons.Default.KeyboardArrowRight, "", Modifier.size(24.dp))
    }
}

@Composable
private fun TimeRange(start: LocalTime, end: LocalTime) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            "${start.format1()} ~ ${end.format1()}",
            style = Theme.t.labelLarge.copy(color = timeTag)
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
            .background(tagContainer, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(value, style = Theme.t.labelLarge.copy(color = tag))
    }
}

@Composable
private fun NormalTag(value: String) {
    Box(
        Modifier
            .border(width = 2.dp, color = tagContainer, shape = RoundedCornerShape(size = 4.dp))
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(value, style = Theme.t.labelLarge.copy(color = tag))
    }
}

@Preview
@Composable
fun SchedulePreview() {
    val speaker = Speaker(
        "1",
        "蓬蓬博士",
        "蓬蓬博士作為研究蓬蓬鬆餅的第一權威人士，他曾經在倫敦蓬蓬大學任教，且在《紐約時報》、《美國商業周刊》、《蓬蓬新報》等國際媒體工作多年，撰寫了大量關於蓬蓬鬆餅的報導，蓬蓬博士的研究領域更涉及了各類甜食的製作及甜點。"
    )
    val type = SessionType("1", "非常開放開放開放開放開放開放式議程")
    val room = Room("1", "R0")
    val tag = SessionTag("1", "鬆餅")
    val sessions = listOf(
        Session(
            id = "1",
            speakers = listOf(speaker),
            type = type,
            room = room,
            start = LocalDateTime(2023, 7, 29, 9, 10, 0),
            end = LocalDateTime(2023, 7, 29, 10, 0, 0),
            title = "蓬蓬鬆餅大集合",
            description = "",
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
            title = "蓬蓬鬆餅大集合",
            description = "",
            language = "中文",
            tags = listOf(tag)
        ),
        Session(
            id = "3",
            speakers = listOf(speaker),
            type = type,
            room = room,
            start = LocalDateTime(2023, 7, 29, 10, 10, 0),
            end = LocalDateTime(2023, 7, 29, 11, 0, 0),
            title = "蓬蓬鬆餅大集合",
            description = "",
            language = "中文",
            tags = listOf(tag)
        ),
    )
    val dates = listOf(
        LocalDate(2023, 7, 29),
        LocalDate(2023, 7, 30),
        LocalDate(2023, 7, 31)
    )
    val m = mapOf(
        LocalDate(2023, 7, 29) to sessions,
        LocalDate(2023, 7, 30) to sessions,
        LocalDate(2023, 7, 31) to sessions,
    )
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    AppTheme {
        Surface {
            ScheduleScreen(dates, m, selectedDate)
        }
    }
}
