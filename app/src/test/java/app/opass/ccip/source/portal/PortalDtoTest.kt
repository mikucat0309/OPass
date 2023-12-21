package app.opass.ccip.source.portal

import app.opass.ccip.I18nText
import app.opass.ccip.model.Event
import app.opass.ccip.model.EventDate
import app.opass.ccip.model.ExternalUrlEventFeature
import app.opass.ccip.model.SimpleInternalUrlEventFeature
import app.opass.ccip.model.WebViewEventFeature
import app.opass.ccip.model.WifiEventFeature
import app.opass.ccip.view.destinations.EnterTokenViewDestination
import app.opass.ccip.view.destinations.HomeViewDestination
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.util.Locale
import kotlinx.datetime.Instant

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
                EventDate(
                    Instant.fromEpochSeconds(0L),
                    Instant.fromEpochSeconds(1700000000L),
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
                EventDate(
                    Instant.fromEpochSeconds(1672531200L),
                    Instant.fromEpochSeconds(3720988800L),
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
                    HomeViewDestination,
                )
            dto.unpack().shouldBe(expected)
          }

          listOf(
                  "puzzle",
                  "webview",
                  "venue",
                  "sponsors",
                  "staffs",
              )
              .forEach { type ->
                "Unpack WebViewEventFeatureDto - $type" {
                  val dto =
                      EventFeatureDto(
                          type,
                          i18nTextDto,
                          url = urlDto,
                      )
                  val expected =
                      WebViewEventFeature(
                          type,
                          i18nText,
                          url = urlDto,
                      )
                  dto.unpack().shouldBe(expected)
                }
              }
          listOf(
                  "telegram",
                  "im",
              )
              .forEach { type ->
                "Unpack ExternalUrlEventFeature - $type" {
                  val dto =
                      EventFeatureDto(
                          type,
                          i18nTextDto,
                          url = urlDto,
                      )
                  val expected =
                      ExternalUrlEventFeature(
                          type,
                          i18nText,
                          urlDto,
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
                )
            dto.unpack().shouldBe(expected)
          }
        },
    )
