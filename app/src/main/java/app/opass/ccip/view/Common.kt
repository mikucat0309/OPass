package app.opass.ccip.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.opass.ccip.compose.R
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.EnumMap
import kotlinx.datetime.DayOfWeek

@Composable
fun BackIcon(navigator: DestinationsNavigator, modifier: Modifier = Modifier) {
  Icon(
      Icons.Default.ArrowBack,
      "back",
      modifier.clickable { navigator.popBackStack() }.padding(horizontal = 16.dp),
  )
}

val WEEKDAY_LABELS =
    EnumMap(
        mapOf(
            DayOfWeek.MONDAY to R.string.monday_short,
            DayOfWeek.TUESDAY to R.string.tuesday_short,
            DayOfWeek.WEDNESDAY to R.string.wednesday_short,
            DayOfWeek.THURSDAY to R.string.thursday_short,
            DayOfWeek.FRIDAY to R.string.friday_short,
            DayOfWeek.SATURDAY to R.string.saturday_short,
            DayOfWeek.SUNDAY to R.string.sunday_short,
        ))
