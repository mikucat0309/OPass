package app.opass.ccip.model

import kotlinx.coroutines.flow.MutableStateFlow

class MainModel {
  val attendee = MutableStateFlow<Attendee?>(null)
  val eventConfig = MutableStateFlow<EventConfig?>(null)
  val ccipBaseUrl: String?
    get() {
      val features = eventConfig.value?.features ?: return null
      var feature = features.firstOrNull { it.type == EventFeatureTypes.FAST_PASS }
      var url = (feature as? InternalUrlEventFeature)?.url
      if (url != null) return url
      feature = features.firstOrNull { it.type == EventFeatureTypes.ANNOUNCEMENT }
      url = (feature as? InternalUrlEventFeature)?.url
      return url
    }
}
