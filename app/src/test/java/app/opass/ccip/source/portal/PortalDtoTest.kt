package app.opass.ccip.source.portal

import app.opass.ccip.compose.R
import app.opass.ccip.misc.I18nText
import app.opass.ccip.misc.TimeZonedInstant
import app.opass.ccip.model.DateTimeRange
import app.opass.ccip.model.Event
import app.opass.ccip.model.ExternalUrlEventFeature
import app.opass.ccip.model.InternalUrlEventFeature
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

          "Unpack EventFeatureDto - fastpass" {
            val dto =
                EventFeatureDto(
                    "fastpass",
                    i18nTextDto,
                    url = urlDto,
                )
            val expected =
                InternalUrlEventFeature(
                    "fastpass",
                    i18nText,
                    true,
                    urlDto,
                    R.drawable.badge_36,
                    HomeViewDestination,
                )
            dto.unpack().shouldBe(expected)
          }
          "Unpack EventFeatureDto - announcement" {
            val dto =
                EventFeatureDto(
                    "announcement",
                    i18nTextDto,
                    url = urlDto,
                )
            val expected =
                InternalUrlEventFeature(
                    "announcement",
                    i18nText,
                    false,
                    urlDto,
                    R.drawable.campaign_36,
                    HomeViewDestination,
                )
            dto.unpack().shouldBe(expected)
          }
          "Unpack EventFeatureDto - ticket" {
            val dto =
                EventFeatureDto(
                    "ticket",
                    i18nTextDto,
                    url = urlDto,
                )
            val expected =
                InternalUrlEventFeature(
                    "ticket",
                    i18nText,
                    true,
                    urlDto,
                    R.drawable.local_activity_36,
                    EnterTokenViewDestination,
                )
            dto.unpack().shouldBe(expected)
          }
          "Unpack EventFeatureDto - schedule" {
            val dto =
                EventFeatureDto(
                    "schedule",
                    i18nTextDto,
                    url = urlDto,
                )
            val expected =
                InternalUrlEventFeature(
                    "schedule",
                    i18nText,
                    false,
                    url = urlDto,
                    R.drawable.history_edu_36,
                    ScheduleViewDestination,
                )
            dto.unpack().shouldBe(expected)
          }
          listOf(
                  Triple("puzzle", true, R.drawable.extension_36),
                  Triple("webview", false, R.drawable.public_36),
                  Triple("venue", false, R.drawable.map_36),
                  Triple("sponsors", false, R.drawable.handshake_36),
                  Triple("staffs", false, R.drawable.people_36),
                  Triple("telegram", false, R.drawable.telegram_36),
                  Triple("im", false, R.drawable.message_36),
              )
              .forEach { params ->
                "Unpack EventFeatureDto - ${params.first}" {
                  val dto =
                      EventFeatureDto(
                          params.first,
                          i18nTextDto,
                          urlDto,
                      )
                  val expected =
                      ExternalUrlEventFeature(
                          params.first,
                          i18nText,
                          params.second,
                          urlDto,
                          params.third,
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
                    false,
                    mapOf(
                        "Free Wifi" to "password",
                    ),
                    R.drawable.wifi_36,
                )
            dto.unpack().shouldBe(expected)
          }
        },
    )
