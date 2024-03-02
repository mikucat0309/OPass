package app.opass.ccip.viewmodel

import androidx.lifecycle.ViewModel
import app.opass.ccip.model.CcipModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TicketViewModel : ViewModel(), KoinComponent {
  private val ccipModel: CcipModel by inject()

  val attendee = ccipModel.attendee
}
