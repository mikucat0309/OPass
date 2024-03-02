package app.opass.ccip.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.model.CcipModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EnterTokenViewModel : ViewModel(), KoinComponent {
  private val ccipModel: CcipModel by inject()
  private val dispatcher: CoroutineDispatcher by inject()

  val errorMessage: StateFlow<String> = ccipModel.errorMessage

  private var _eventState = mutableStateOf(EventState.INIT)
  val eventState: State<EventState> = _eventState

  fun fetchAttendee(token: String) =
      viewModelScope.launch(dispatcher) {
        _eventState.value = EventState.PROCESSING
        ccipModel.fetchAttendee(token)
        _eventState.value = EventState.DONE
      }

  fun reset() {
    ccipModel.errorMessage.compareAndSet("", "")
    _eventState.value = EventState.INIT
  }
}
