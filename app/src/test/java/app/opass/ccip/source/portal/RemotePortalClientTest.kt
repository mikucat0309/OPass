package app.opass.ccip.source.portal

import app.opass.ccip.compose.R
import app.opass.ccip.misc.I18nText
import app.opass.ccip.misc.TimeZonedInstant
import app.opass.ccip.model.DateTimeRange
import app.opass.ccip.model.Event
import app.opass.ccip.model.EventConfig
import app.opass.ccip.model.InternalUrlEventFeature
import app.opass.ccip.model.WifiEventFeature
import app.opass.ccip.source.mockJsonHttpClient
import app.opass.ccip.view.destinations.HomeViewDestination
import app.opass.ccip.view.destinations.ScheduleViewDestination
import app.opass.ccip.view.destinations.TicketViewDestination
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.util.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone

class RemotePortalClientTest :
    StringSpec(
        {
          coroutineTestScope = true

          val httpClient =
              mockJsonHttpClient(
                  "/events/" to
                      """
                [
                    {
                        "event_id": "COSCUP_2023",
                        "display_name": {
                            "en": "COSCUP 2023",
                            "zh-TW": "COSCUP 2023"
                        },
                        "logo_url": "https://coscup.org/2020/images/logo-512-white.png"
                    }
                ]
                """
                          .trimIndent(),
                  "/events/SITCON_2022/" to
                      """
                {
                    "event_id": "SITCON_2022",
                    "display_name": {
                        "en": "SITCON 2022",
                        "zh": "SITCON 2022"
                    },
                    "logo_url": "https://sitcon.org/branding/assets/logos/withname-white.png",
                    "event_website": "https://sitcon.org/2022/",
                    "event_date": {
                        "start": "2022-09-04T00:00:00+08:00",
                        "end": "2022-09-04T00:00:00+08:00"
                    },
                    "publish": {
                        "start": "2022-07-24T08:00",
                        "end": "2023-03-28T08:00"
                    },
                    "features": [
                        {
                            "feature": "fastpass",
                            "display_text": {
                                "en": "Fast Pass",
                                "zh": "快速通關"
                            },
                            "visible_roles": [
                                "audience",
                                "staff",
                                "speaker"
                            ],
                            "url": "https://sitcon.opass.app"
                        },
                        {
                            "feature": "schedule",
                            "display_text": {
                                "en": "Schedule",
                                "zh": "議程"
                            },
                            "url": "https://sitcon.org/2022/json/session.json"
                        },
                        {
                            "feature": "ticket",
                            "display_text": {
                                "en": "Ticket",
                                "zh": "我的票券"
                            }
                        },
                        {
                            "feature": "announcement",
                            "display_text": {
                                "en": "Announcement",
                                "zh": "大會公告"
                            },
                            "url": "https://sitcon.opass.app"
                        },
                        {
                            "feature": "wifi",
                            "display_text": {
                                "en": "WiFi",
                                "zh": "會場 WiFi"
                            },
                            "wifi": [
                                {
                                    "SSID": "SITCON X via Klickklack",
                                    "password": "20220904"
                                }
                            ]
                        }
                    ]
                }
                """
                          .trimIndent(),
              )
          val remotePortalClient = RemotePortalClient(httpClient, "https://example.com")

          "getEvents" {
            val expected =
                listOf(
                    Event(
                        "COSCUP_2023",
                        I18nText(
                            Locale.ENGLISH to "COSCUP 2023",
                            Locale.TAIWAN to "COSCUP 2023",
                        ),
                        URL("https://coscup.org/2020/images/logo-512-white.png"),
                    ),
                )
            val events = remotePortalClient.getEvents()
            events.shouldBe(expected)
          }

          "getEventConfig" {
            val expected =
                EventConfig(
                    "SITCON_2022",
                    I18nText(
                        Locale.ENGLISH to "SITCON 2022",
                        Locale.CHINESE to "SITCON 2022",
                    ),
                    URL("https://sitcon.org/branding/assets/logos/withname-white.png"),
                    URL("https://sitcon.org/2022/"),
                    DateTimeRange(
                        TimeZonedInstant(
                            LocalDateTime(2022, 9, 4, 0, 0, 0),
                            UtcOffset(hours = 8).asTimeZone(),
                        ),
                        TimeZonedInstant(
                            LocalDateTime(2022, 9, 4, 0, 0, 0),
                            UtcOffset(hours = 8).asTimeZone(),
                        ),
                    ),
                    DateTimeRange(
                        TimeZonedInstant(
                            LocalDateTime(2022, 7, 24, 8, 0, 0),
                            TimeZone.UTC,
                        ),
                        TimeZonedInstant(
                            LocalDateTime(2023, 3, 28, 8, 0, 0),
                            TimeZone.UTC,
                        ),
                    ),
                    listOf(
                        InternalUrlEventFeature(
                            "fastpass",
                            I18nText(
                                Locale.ENGLISH to "Fast Pass",
                                Locale.CHINESE to "快速通關",
                            ),
                            true,
                            "https://sitcon.opass.app",
                            R.drawable.badge_36,
                            HomeViewDestination,
                        ),
                        InternalUrlEventFeature(
                            "schedule",
                            I18nText(
                                Locale.ENGLISH to "Schedule",
                                Locale.CHINESE to "議程",
                            ),
                            false,
                            "https://sitcon.org/2022/json/session.json",
                            R.drawable.history_edu_36,
                            ScheduleViewDestination,
                        ),
                        InternalUrlEventFeature(
                            "ticket",
                            I18nText(
                                Locale.ENGLISH to "Ticket",
                                Locale.CHINESE to "我的票券",
                            ),
                            true,
                            "https://sitcon.opass.app",
                            R.drawable.local_activity_36,
                            TicketViewDestination,
                        ),
                        InternalUrlEventFeature(
                            "announcement",
                            I18nText(
                                Locale.ENGLISH to "Announcement",
                                Locale.CHINESE to "大會公告",
                            ),
                            false,
                            "https://sitcon.opass.app",
                            R.drawable.campaign_36,
                            HomeViewDestination,
                        ),
                        WifiEventFeature(
                            "wifi",
                            I18nText(
                                Locale.ENGLISH to "WiFi",
                                Locale.CHINESE to "會場 WiFi",
                            ),
                            false,
                            mapOf(
                                "SITCON X via Klickklack" to "20220904",
                            ),
                            R.drawable.wifi_36,
                        ),
                    ),
                )
            val config = remotePortalClient.getEventConfig("SITCON_2022")
            for (i in 0 until config.features.size) {
              config.features[i].shouldBe(expected.features[i])
            }
            config.shouldBe(expected)
          }
        },
    )
