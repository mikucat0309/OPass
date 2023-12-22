package app.opass.ccip.viewmodel

import androidx.lifecycle.ViewModel
import app.opass.ccip.model.CcipModel
import app.opass.ccip.model.PortalModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {
  private val portalModel: PortalModel by inject()
  private val ccipModel: CcipModel by inject()

  val eventConfig = portalModel.eventConfig
  val attendee = ccipModel.attendee
}
