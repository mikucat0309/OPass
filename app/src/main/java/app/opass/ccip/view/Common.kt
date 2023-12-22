package app.opass.ccip.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.opass.ccip.view.destinations.HomeViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun BackIcon(navigator: DestinationsNavigator, modifier: Modifier = Modifier) {
  Icon(
      Icons.Default.ArrowBack,
      "back",
      modifier
          .clickable { navigator.popBackStack() }
          .padding(horizontal = 16.dp),
  )
}
