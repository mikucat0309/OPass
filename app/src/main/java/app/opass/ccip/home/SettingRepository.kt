package app.opass.ccip.home

import androidx.datastore.core.DataStore
import app.opass.ccip.setting.Settings

class SettingRepository(
    val dataStore: DataStore<Settings>
)
