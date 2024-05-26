package app.opass.ccip.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.opass.ccip.model.Announcement
import app.opass.ccip.source.ccip.CcipClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AnnouncementViewModel : ViewModel(), KoinComponent {
  private val ccipClient by inject<CcipClient>()
  private val dispatcher by inject<CoroutineDispatcher>()

  var announcements by mutableStateOf<List<Announcement>>(emptyList())

  fun updateAnnouncements() {
    viewModelScope.launch(dispatcher) {
      ccipClient.updateAnnouncements().onSuccess { announcements = it }
    }
  }
}
