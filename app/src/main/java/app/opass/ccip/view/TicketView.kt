package app.opass.ccip.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.opass.ccip.compose.R
import app.opass.ccip.misc.QString
import app.opass.ccip.ui.theme.Theme
import app.opass.ccip.viewmodel.TicketViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Destination
@Composable
fun TicketView(
    navigator: DestinationsNavigator,
    vm: TicketViewModel = koinViewModel(),
) {
  val attendee = vm.attendee.cASWL().value ?: return
  TicketScreen(attendee.token, navigator)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TicketScreen(token: String, navigator: DestinationsNavigator) {
  Scaffold(
      topBar = {
        LargeTopAppBar(
            navigationIcon = { BackIcon(navigator) },
            title = {
              Text(
                  "My Ticket",
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
    Column(
        Modifier.padding(pv).padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      QRCode(token)
      TokenTextField(token)
    }
  }
}

@Composable
private fun QRCode(token: String) {
  AsyncImage(
      model = QString(token),
      contentDescription = "token QRCode",
      imageLoader = koinInject(),
      Modifier.fillMaxWidth().aspectRatio(1.0f).padding(64.dp),
  )
}

@Composable
private fun TokenTextField(token: String) {
  var passwordVisible by remember { mutableStateOf(false) }
  val image = if (passwordVisible) R.drawable.visibility_off_24 else R.drawable.visibility_24
  val vt = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
  OutlinedTextField(
      value = token,
      onValueChange = {},
      label = { Text("Token") },
      singleLine = true,
      readOnly = true,
      visualTransformation = vt,
      trailingIcon = {
        IconButton(onClick = { passwordVisible = !passwordVisible }) {
          Icon(painterResource(image), "")
        }
      },
  )
}
