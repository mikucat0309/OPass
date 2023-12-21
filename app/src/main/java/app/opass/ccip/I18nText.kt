package app.opass.ccip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale
import org.koin.core.component.KoinComponent

data class I18nText(private val map: Map<Locale, String>) : KoinComponent {
  constructor(vararg pairs: Pair<Locale, String>) : this(pairs.toMap())

  val current: String
    @Composable
    get() {
      val systemLocales = LocalSystemLocaleList.current
      val locale = getBestMatchedLocale(systemLocales)
      return map[locale]!!
    }

  override fun toString(): String = map.toString()

  private fun getBestMatchedLocale(systemLocales: List<Locale>): Locale {
    val supportedLocales = map.keys.toList()
    for (locale in systemLocales) {
      val langMatched = supportedLocales.filter { it.language == locale.language }
      val scriptMatched = langMatched.filter { it.script == locale.script }
      val countryMatched = scriptMatched.firstOrNull { it.country == locale.country }
      if (countryMatched != null) return countryMatched

      val scriptExactMatched = scriptMatched.firstOrNull { it.country == "" }
      if (scriptExactMatched != null) return scriptExactMatched
      if (scriptMatched.isNotEmpty()) return scriptMatched[0]

      val langExactMatched = langMatched.firstOrNull { it.script == "" && it.country == "" }
      if (langExactMatched != null) return langExactMatched
      if (langMatched.isNotEmpty()) return langMatched[0]
    }
    return supportedLocales[0]
  }

  companion object {
    fun parseLocale(map: Map<String, String>) =
        I18nText(map.mapKeys { Locale.forLanguageTag(it.key) })
  }
}

val LocalSystemLocaleList: ProvidableCompositionLocal<List<Locale>> = staticCompositionLocalOf {
  emptyList()
}
