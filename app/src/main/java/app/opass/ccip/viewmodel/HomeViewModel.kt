package app.opass.ccip.viewmodel

import androidx.lifecycle.ViewModel
import app.opass.ccip.model.MainModel
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {
  private val mainModel by inject<MainModel>()
  val attendee = mainModel.attendee.asStateFlow()
  val eventConfig = mainModel.eventConfig.asStateFlow()
}
