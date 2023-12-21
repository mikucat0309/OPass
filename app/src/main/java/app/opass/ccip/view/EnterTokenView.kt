package app.opass.ccip.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.opass.ccip.view.destinations.HomeViewDestination
import app.opass.ccip.viewmodel.EnterTokenViewModel
import app.opass.ccip.viewmodel.EventState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun EnterTokenView(
    navigator: DestinationsNavigator,
    vm: EnterTokenViewModel = navGraphViewModel(),
) {
  val errorMessage = vm.errorMessage.cASWL()
  if (vm.eventState.value == EventState.DONE && errorMessage.value.isBlank()) {
    vm.reset()
    navigator.popBackStack(HomeViewDestination.route, false)
  }
  var token by remember { mutableStateOf("") }
  AlertDialog(
      onDismissRequest = {},
      confirmButton = {
        TextButton(onClick = { if (token.isNotBlank()) vm.fetchAttendee(token) }) { Text("使用") }
      },
      dismissButton = { TextButton(onClick = { navigator.popBackStack() }) { Text("取消") } },
      title = { Column { Text("輸入票券代碼") } },
      text = {
        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            singleLine = true,
            isError = errorMessage.value.isNotBlank(),
        )
      },
  )
}
