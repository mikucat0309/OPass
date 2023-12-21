package app.opass.ccip.source.portal

import app.opass.ccip.I18nText
import app.opass.ccip.model.Event
import app.opass.ccip.model.EventConfig
import app.opass.ccip.model.EventDate
import app.opass.ccip.model.SimpleInternalUrlEventFeature
import app.opass.ccip.model.WebViewEventFeature
import app.opass.ccip.model.WifiEventFeature
import app.opass.ccip.source.mockJsonHttpClient
import app.opass.ccip.view.destinations.EnterTokenViewDestination
import app.opass.ccip.view.destinations.HomeViewDestination
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.util.Locale
import kotlinx.datetime.Instant

class RemotePortalClientTest :
    StringSpec({
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
                            "feature": "webview",
                            "display_text": {
                                "en": "Lightning Talk Election",
                                "zh": "閃電秀投稿/投票"
                            },
                            "icon": "https://sitcon.org/2022/imgs/opass-icon/lightning-talk.png",
                            "url": "https://sitcon.org/2022-lightning-talk/?token={token}"
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
                EventDate(
                    Instant.parse("2022-09-04T00:00:00+08:00"),
                    Instant.parse("2022-09-04T00:00:00+08:00"),
                ),
                EventDate(
                    Instant.parse("2022-07-24T08:00:00+00:00"),
                    Instant.parse("2023-03-28T08:00:00+00:00"),
                ),
                listOf(
                    SimpleInternalUrlEventFeature(
                        "fastpass",
                        I18nText(
                            Locale.ENGLISH to "Fast Pass",
                            Locale.CHINESE to "快速通關",
                        ),
                        "https://sitcon.opass.app",
                        HomeViewDestination,
                        visibleRoles =
                            listOf(
                                "audience",
                                "staff",
                                "speaker",
                            ),
                    ),
                    SimpleInternalUrlEventFeature(
                        "schedule",
                        I18nText(
                            Locale.ENGLISH to "Schedule",
                            Locale.CHINESE to "議程",
                        ),
                        "https://sitcon.org/2022/json/session.json",
                        HomeViewDestination,
                    ),
                    SimpleInternalUrlEventFeature(
                        "ticket",
                        I18nText(
                            Locale.ENGLISH to "Ticket",
                            Locale.CHINESE to "我的票券",
                        ),
                        "https://sitcon.opass.app",
                        EnterTokenViewDestination,
                    ),
                    SimpleInternalUrlEventFeature(
                        "announcement",
                        I18nText(
                            Locale.ENGLISH to "Announcement",
                            Locale.CHINESE to "大會公告",
                        ),
                        "https://sitcon.opass.app",
                        HomeViewDestination,
                    ),
                    WebViewEventFeature(
                        "webview",
                        I18nText(
                            Locale.ENGLISH to "Lightning Talk Election",
                            Locale.CHINESE to "閃電秀投稿/投票",
                        ),
                        "https://sitcon.org/2022-lightning-talk/?token={token}",
                        iconUrl = URL("https://sitcon.org/2022/imgs/opass-icon/lightning-talk.png"),
                    ),
                    WifiEventFeature(
                        "wifi",
                        I18nText(
                            Locale.ENGLISH to "WiFi",
                            Locale.CHINESE to "會場 WiFi",
                        ),
                        mapOf(
                            "SITCON X via Klickklack" to "20220904",
                        ),
                    ),
                ),
            )
        val config = remotePortalClient.getEventConfig("SITCON_2022")
        for (i in 0 until config.features.size) {
          config.features[i].shouldBe(expected.features[i])
        }
        config.shouldBe(expected)
      }
    })
