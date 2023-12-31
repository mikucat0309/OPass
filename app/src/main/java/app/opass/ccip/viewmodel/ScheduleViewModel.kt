package app.opass.ccip.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.model.CcipModel
import app.opass.ccip.model.InternalUrlEventFeature
import app.opass.ccip.model.PortalModel
import app.opass.ccip.model.ScheduleModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URL

class ScheduleViewModel : ViewModel(), KoinComponent {
  private val ccipModel: CcipModel by inject()
  private val portalModel: PortalModel by inject()
  private val scheduleModel: ScheduleModel by inject()
  private val dispatcher: CoroutineDispatcher by inject()

  val sessions = scheduleModel.sessions
  val eventConfig = portalModel.eventConfig

  fun fetchSessions() {
    val eventConfig = portalModel.eventConfig.value ?: return
    val feature =
        eventConfig.features.firstOrNull { it.type == "schedule" } as? InternalUrlEventFeature
    val timeZone = eventConfig.eventDateTimeRange.start.timeZone
    if (feature != null) {
      viewModelScope.launch(dispatcher) {
        scheduleModel.fetchSessions(URL(ccipModel.replaceTemplate(feature.url)), timeZone)
      }
    }
  }
}
