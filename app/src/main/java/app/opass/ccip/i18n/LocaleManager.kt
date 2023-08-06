package app.opass.ccip.i18n

import java.util.Locale


class LocaleManager {
    private val en: MutableMap<String, String> = mutableMapOf()
    private val zh: MutableMap<String, String> = mutableMapOf()

    private val current = if (Locale.getDefault().language == "zh") zh else en

    fun register(id: String, zhStr: String? = null, enStr: String? = null) {
        if (zhStr != null) zh[id] = zhStr
        if (enStr != null) en[id] = enStr
    }

    fun getStr(id: String) = current[id] ?: id
}