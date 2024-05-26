package app.opass.ccip.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.model.Session
import app.opass.ccip.source.portal.PortalClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ScheduleViewModel : ViewModel(), KoinComponent {
  private val portalClient: PortalClient by inject()
  private val dispatcher: CoroutineDispatcher by inject()
  var sessions by mutableStateOf(emptyList<Session>())

  fun fetchSessions() {
    viewModelScope.launch(dispatcher) { portalClient.updateSessions().onSuccess { sessions = it } }
  }
}
