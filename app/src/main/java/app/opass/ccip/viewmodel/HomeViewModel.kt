package app.opass.ccip.viewmodel

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

class HomeViewModel : ViewModel(), KoinComponent {
  private val portalModel: PortalModel by inject()
  private val ccipModel: CcipModel by inject()
  private val dispatcher: CoroutineDispatcher by inject()

  val eventConfig = portalModel.eventConfig
  val attendee = ccipModel.attendee

  fun fetchEventConfig(eventId: String) {
    viewModelScope.launch(dispatcher) {
      portalModel.fetchEventConfig(eventId)
      eventConfig.collectLatest { config ->
        val feature =
            config?.features?.firstOrNull { it.type == "fastpass" } as? InternalUrlEventFeature
        if (feature != null) {
          ccipModel.baseUrl = feature.url
        }
      }
    }
  }
}
