package app.opass.ccip.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.source.ccip.CcipClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EnterTokenViewModel : ViewModel(), KoinComponent {
  private val mainModel by inject<CcipClient>()
  private val dispatcher by inject<CoroutineDispatcher>()
  val eventState = mutableStateOf(EventState.INIT)

  fun updateAttendee(token: String) {
    viewModelScope.launch(dispatcher) {
      eventState.value = EventState.PROCESSING
      mainModel.updateAttendee(token)
      eventState.value = EventState.DONE
    }
  }
}
