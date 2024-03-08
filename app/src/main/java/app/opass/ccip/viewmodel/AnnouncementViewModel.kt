package app.opass.ccip.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.model.CcipModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AnnouncementViewModel : ViewModel(), KoinComponent {
  private val ccipModel: CcipModel by inject()
  private val dispatcher: CoroutineDispatcher by inject()

  val announcements = ccipModel.announcements

  fun loadAnnouncements() {
    val token = ccipModel.attendee.value?.token
    viewModelScope.launch(dispatcher) { ccipModel.fetchAnnouncements(token) }
  }
}
