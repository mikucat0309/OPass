package app.opass.ccip.source.portal

import app.opass.ccip.I18nText
import app.opass.ccip.TimeZonedInstant
import app.opass.ccip.compose.R
import app.opass.ccip.model.DateTimeRange
import app.opass.ccip.model.Event
import app.opass.ccip.model.ExternalUrlEventFeature
import app.opass.ccip.model.SimpleInternalUrlEventFeature
import app.opass.ccip.model.WebViewEventFeature
import app.opass.ccip.model.WifiEventFeature
import app.opass.ccip.view.destinations.EnterTokenViewDestination
import app.opass.ccip.view.destinations.HomeViewDestination
import app.opass.ccip.view.destinations.ScheduleViewDestination
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.util.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone

class PortalDtoTest :
    StringSpec(
        {
          val i18nTextDto =
              mapOf(
                  "en" to "English",
                  "zh-TW" to "臺灣",
                  "zh-CN" to "中国",
              )
          val i18nText =
              I18nText(
                  Locale.ENGLISH to "English",
                  Locale.TAIWAN to "臺灣",
                  Locale.CHINA to "中国",
              )
          val urlDto = "https://example.com/"
          val url = URL(urlDto)
          "Unpack EventDto" {
            val dto =
                EventDto(
                    "Test 2087",
                    i18nTextDto,
                    urlDto,
                )
            val expected =
                Event(
                    "Test 2087",
                    i18nText,
                    url,
                )
            dto.unpack().shouldBe(expected)
          }

          "Unpack EventDateDto #1" {
            val dto =
                EventDateDto(
                    "1970-01-01T08:00:00+08:00",
                    "2023-11-14T12:13:20-10",
                )
            val expected =
                DateTimeRange(
                    TimeZonedInstant(
                        LocalDateTime(1970, 1, 1, 8, 0, 0),
                        UtcOffset(hours = +8).asTimeZone(),
                    ),
                    TimeZonedInstant(
                        LocalDateTime(2023, 11, 14, 12, 13, 20),
                        UtcOffset(hours = -10).asTimeZone(),
                    ),
                )
            dto.unpack().shouldBe(expected)
          }

          "Unpack EventDateDto #2" {
            val dto =
                EventDateDto(
                    "2023-01-01T00:00+00",
                    "2087-11-29T12:00-12:00",
                )
            val expected =
                DateTimeRange(
                    TimeZonedInstant(
                        LocalDateTime(2023, 1, 1, 0, 0, 0),
                        TimeZone.UTC,
                    ),
                    TimeZonedInstant(
                        LocalDateTime(2087, 11, 29, 12, 0, 0),
                        UtcOffset(hours = -12).asTimeZone(),
                    ),
                )
            dto.unpack().shouldBe(expected)
          }

          "Unpack EventFeatureWifiDto" {
            val dto =
                EventFeatureWifiDto(
                    "Free WiFi",
                    "It is free if know the password",
                )
            val expected = "Free WiFi" to "It is free if know the password"
            dto.unpack().shouldBe(expected)
          }

          "Unpack UrlEventFeatureDto - fastpass" {
            val dto =
                EventFeatureDto(
                    "fastpass",
                    i18nTextDto,
                    url = urlDto,
                )
            val expected =
                SimpleInternalUrlEventFeature(
                    "fastpass",
                    i18nText,
                    urlDto,
                    HomeViewDestination,
                    R.drawable.badge_36,
                )
            dto.unpack().shouldBe(expected)
          }
          "Unpack UrlEventFeatureDto - announcement" {
            val dto =
                EventFeatureDto(
                    "announcement",
                    i18nTextDto,
                    url = urlDto,
                )
            val expected =
                SimpleInternalUrlEventFeature(
                    "announcement",
                    i18nText,
                    urlDto,
                    HomeViewDestination,
                    R.drawable.campaign_36,
                )
            dto.unpack().shouldBe(expected)
          }
          "Unpack UrlEventFeatureDto - ticket" {
            val dto =
                EventFeatureDto(
                    "ticket",
                    i18nTextDto,
                    url = urlDto,
                )
            val expected =
                SimpleInternalUrlEventFeature(
                    "ticket",
                    i18nText,
                    urlDto,
                    EnterTokenViewDestination,
                    R.drawable.local_activity_36,
                )
            dto.unpack().shouldBe(expected)
          }
          "Unpack UrlEventFeatureDto - schedule" {
            val dto =
                EventFeatureDto(
                    "schedule",
                    i18nTextDto,
                    url = urlDto,
                )
            val expected =
                SimpleInternalUrlEventFeature(
                    "schedule",
                    i18nText,
                    url = urlDto,
                    ScheduleViewDestination,
                    R.drawable.history_edu_36,
                )
            dto.unpack().shouldBe(expected)
          }

          listOf(
                  "puzzle" to R.drawable.extension_36,
                  "webview" to R.drawable.public_36,
                  "venue" to R.drawable.map_36,
                  "sponsors" to R.drawable.handshake_36,
                  "staffs" to R.drawable.people_36,
              )
              .forEach { params ->
                "Unpack WebViewEventFeatureDto - ${params.first}" {
                  val dto =
                      EventFeatureDto(
                          params.first,
                          i18nTextDto,
                          url = urlDto,
                      )
                  val expected =
                      WebViewEventFeature(
                          params.first,
                          i18nText,
                          url = urlDto,
                          params.second,
                      )
                  dto.unpack().shouldBe(expected)
                }
              }
          listOf(
                  "telegram" to R.drawable.telegram_36,
                  "im" to R.drawable.message_36,
              )
              .forEach { params ->
                "Unpack ExternalUrlEventFeature - ${params.first}" {
                  val dto =
                      EventFeatureDto(
                          params.first,
                          i18nTextDto,
                          url = urlDto,
                      )
                  val expected =
                      ExternalUrlEventFeature(
                          params.first,
                          i18nText,
                          urlDto,
                          params.second,
                      )
                  dto.unpack().shouldBe(expected)
                }
              }

          "WifiEventFeatureDto" {
            val dto =
                EventFeatureDto(
                    "wifi",
                    i18nTextDto,
                    wifi =
                        listOf(
                            EventFeatureWifiDto("Free Wifi", "password"),
                        ),
                )
            val expected =
                WifiEventFeature(
                    "wifi",
                    i18nText,
                    wifi =
                        mapOf(
                            "Free Wifi" to "password",
                        ),
                    R.drawable.wifi_36)
            dto.unpack().shouldBe(expected)
          }
        },
    )
