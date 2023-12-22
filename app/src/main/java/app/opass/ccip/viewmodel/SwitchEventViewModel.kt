package app.opass.ccip.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.model.CcipModel
import app.opass.ccip.model.InternalUrlEventFeature
import app.opass.ccip.model.PortalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SwitchEventViewModel : ViewModel(), KoinComponent {
  private val portalModel: PortalModel by inject()
  private val ccipModel: CcipModel by inject()
  private val dispatcher: CoroutineDispatcher by inject()

  val events = portalModel.events

  private var _eventState = mutableStateOf(EventState.INIT)
  val eventState: State<EventState> = _eventState

  fun fetchEvents() {
    viewModelScope.launch(dispatcher) { portalModel.fetchEvents() }
  }

  fun fetchEventConfig(eventId: String) {
    viewModelScope.launch(dispatcher) {
      _eventState.value = EventState.PROCESSING
      portalModel.fetchEventConfig(eventId)
      portalModel.eventConfig.collectLatest { config ->
        val feature =
            config?.features?.firstOrNull { it.type == "fastpass" } as? InternalUrlEventFeature
        if (feature != null) {
          ccipModel.baseUrl = feature.url
        }
        _eventState.value = EventState.DONE
      }
    }
  }

  fun reset() {
    _eventState.value = EventState.INIT
  }
}
